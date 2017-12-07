package com.yuntai.hdp.server.updata.dynamic;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import com.alibaba.dubbo.rpc.service.EchoService;
import com.google.common.collect.Ordering;
import com.yuntai.util.HdpHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.yuntai.hdp.access.service.UpdataHandler;
import com.yuntai.hdp.server.updata.CommandProvider;
import com.yuntai.hdp.server.updata.UpdataHandlerManager;

/**
 * @author denglt
 * @Description 通过数据库HDP配置，动态发现UpdataHandler.
 */
@Service("discoveryUpdataHandler")
public class DiscoveryUpdataHandler {
    private ApplicationConfig application = new ApplicationConfig("DynamicDiscoveryService");
    private static Log log = LogFactory.getLog(DiscoveryUpdataHandler.class);
    private ConcurrentMap<Integer, Registry> mapReg = new ConcurrentHashMap<Integer, Registry>(); // 动态发现的Registry
    private ConcurrentMap<String, Command> mapCmd = new ConcurrentHashMap<String, Command>();
    private ConcurrentMap<String, ReferenceConfig<UpdataHandler>> mapRefer = new ConcurrentHashMap<String, ReferenceConfig<UpdataHandler>>(); // 动态发现的Reference

    @Resource
    private HdpDao hdpDao;

    private UpdataHandlerManager updataHandlerManager;

    private boolean availableRegistry(Registry reg) {
        try {
            if (reg.getProtocol() == null || reg.getProtocol().length() == 0) {
                return false;
            }
            if (reg.getAddress() == null || reg.getAddress().length() == 0) {
                return false;
            }

            if (reg.getProtocol().equals("multicast")) {
                if (reg.getAddress().equals("224.5.6.7:1234")) {
                    return true;
                }

                if (reg.getAddress().equals("224.5.6.7")
                        && reg.getPort().intValue() == 1234) {
                    return true;
                }
                return false;
            }
            String[] ips = reg.getAddress().split(",");
            for (String ip : ips) {
                String[] socket = ip.split(":");
                if (HdpHelper.isPortInUse(socket[0],
                                          socket.length > 1 ? Integer.parseInt(socket[1]) : reg.getPort()
                                          )
                    ) {
                    return true;
                }
            }

        } catch (Exception e) {
            log.error(String.format("Check %s", reg.toString()), e);
        }
        return false;
    }

    private void addRegistry() throws Exception {
        List<Registry> regs = hdpDao.getRegistry();
        for (Registry reg : regs) {
            Registry temp = mapReg.get(reg.getRegistryId());
            if (temp == null) {
                if (availableRegistry(reg)) {
                    mapReg.put(reg.getRegistryId(), reg);
                } else {
                    log.error(String.format("%s is unavailable!", reg));
                }
            }
        }
    }

    private List<Command> findNewCmds() throws Exception {
        List<Command> cmds = hdpDao.getCommand();
        List<Command> newCmds = new ArrayList<Command>();
        for (Command cmd : cmds) {
            if (mapCmd.get(cmd.getCode()) == null) {
                newCmds.add(cmd);
            }
        }
        return newCmds;
    }

    /**
     * 删除扫描到的UpdataHandler
     * @param cmdCode
     */
    public Command removeCmd(String cmdCode) {
        Command command = mapCmd.remove(cmdCode);
        updataHandlerManager.delete(cmdCode);
        ReferenceConfig<UpdataHandler> referenceConfig = mapRefer.remove(cmdCode);
        if (referenceConfig != null)
            referenceConfig.destroy();
        log.info(String.format("Command[%s]  is removed!",
                command));
        return command;
    }

    @Deprecated
    private void removeCmdInRegistry(int registryId) {
        Set<String> codes = mapCmd.keySet();
        for (String code : codes) {
            Command command = mapCmd.get(code);
            if (command.getRegistryId() == registryId) {
                removeCmd(command.getCode());
            }
        }

    }

    /**
     * dubbo服务健康检查
     */
    @Deprecated
    // 由于多次创建、销毁ReferenceConfig存在内存泄漏;改为监控时做回声测试
    private void checkHealth() {
        // 检查注册中心
        Set<Integer> regids = mapReg.keySet();
        for (Integer regid : regids) {
            Registry reg = mapReg.get(regid);
            if (!availableRegistry(reg)) { // 删除不可用的Registry，同时删除Registry里的dubbo服务
                mapReg.remove(reg.getRegistryId());
                removeCmdInRegistry(reg.getRegistryId());
                log.error(String.format("%s is unavailable!", reg));
            }
        }

        // Echo Test 动态发现的dubbo服务
        Set<String> cmdCodes = mapRefer.keySet();
        for (String code : cmdCodes) {
            ReferenceConfig<UpdataHandler> reference = mapRefer.get(code);
            UpdataHandler handler = reference.get();
            try {
                EchoService echoService = (EchoService) handler;
                String status = (String) echoService.$echo("OK");
                if (!status.equals("ok")) {
                    removeCmd(code);
                }
            } catch (Exception ex) {
                log.error(String.format("Tech Test Command[%s] is error，this command will be removed！", code), ex );
                removeCmd(code);
            }
        }

    }


    @Scheduled(fixedDelay = 60000)
    public void discovery() {
        try {
            // checkHealth();
            addRegistry();
            List<Command> newCmds = findNewCmds();
            for (Command newCmd : newCmds) {
                Registry registry = mapReg.get(newCmd.getRegistryId());
                if (registry == null) {
                    log.error(String.format("Auto discovery Command[%s] dosen't find registry!",newCmd.toString()));
                    continue;
                }
                ReferenceConfig<UpdataHandler> reference = new ReferenceConfig<UpdataHandler>();
                try {
                    reference.setApplication(application);
                    RegistryConfig rc = registry.getRegistryConfig();
                    reference.setRegistry(rc);
                    reference.setInterface(newCmd.getInterfaceName());
                    reference.setGroup(newCmd.getGroupName());
                    reference.setCheck(false); // 检查是否有providers  // 由于ReferenceConfig.destroy()的bug,不进行check检查,修改为回声测试
                    reference.setTimeout(60000); // 超时设置
                    reference.setRetries(0);
                    UpdataHandler updataHandler = reference.get();
                    //不进行回声测试
                    // EchoService echoService = (EchoService) updataHandler;
                    String status =  "OK"; // (String) echoService.$echo("OK");
                    if (status.equals("OK")) {
                        CommandProvider provider = new CommandProvider(newCmd, updataHandler);
                        updataHandlerManager.add(provider);
                        mapCmd.put(newCmd.getCode(), newCmd);
                        mapRefer.put(newCmd.getCode(), reference);
                        log.info(String.format("Auto discovery Command[%s] ", newCmd.toString()));
                    }else{
                        reference.destroy();
                        log.error(String.format("Auto discovery Command[%s] is error, please check dubbo server provider!", newCmd.toString()));
                    }
                } catch (Exception ex) {
                    reference.destroy();
                    log.error(String.format("Auto discovery Command[%s] is error.", newCmd.toString()), ex);

                }
            }

        } catch (Exception ex) {
            log.error("Auto discovery dubbo service is error.", ex);
        }
    }



    public UpdataHandlerManager getUpdataHandlerManager() {
        return updataHandlerManager;
    }

    @Resource
    public void setUpdataHandlerManager(UpdataHandlerManager updataHandlerManager) {

        this.updataHandlerManager = updataHandlerManager;
        List<CommandProvider> providers = updataHandlerManager.getProviders();
        for (CommandProvider provider : providers) {// spring 配置的service 优先
            Command cmd = new Command();
            cmd.setCode(provider.getCode());
            cmd.setInterfaceName(provider.getHandler().getClass().getName());
            cmd.setRegistryId(-1);
            mapCmd.put(cmd.getCode(), cmd);
        }
    }

    private boolean echoTest(String cmdCode) {
        ReferenceConfig<UpdataHandler> reference = mapRefer.get(cmdCode);
        if (reference == null)
            return false;

        try {
            UpdataHandler handler = reference.get();
            EchoService echoService = (EchoService) handler;
            String status = (String) echoService.$echo("OK");
            return status.equals("OK");
        } catch (Exception ex) {
            log.error(String.format("Tech Test Command[%s] is error！", cmdCode), ex);
            return false;
        }
    }

    public List<String> getRegistryInfo(){
        List<String> registryInfo = new ArrayList<String>();
        Collection<Registry> regs = mapReg.values();
        for (Registry reg : regs) {
            if (availableRegistry(reg)) {
                registryInfo.add(reg.toString() + ": ok");
            } else {
                registryInfo.add(reg.toString() + ": unavailable");
            }
        }
        return registryInfo;
    }

    public List<String> getUpdataHandlerInfo() {
        List<String> handlerInfo = new ArrayList<String>();

        Ordering<Command> ordering = Ordering.from(new Comparator<Command>() {
            @Override
            public int compare(Command o1, Command o2) {
                return o1.getCode().compareTo(o2.getCode());
            }
        });

        for (Command cmd : ordering.sortedCopy(mapCmd.values())) {
            if (cmd.getRegistryId() != -1) {
                if (echoTest(cmd.getCode())) {
                    handlerInfo.add(cmd.toString() + ": Echo Test Ok.");
                } else {
                    handlerInfo.add(cmd.toString() + ": Echo Test Fail.");
                }
            } else {
                handlerInfo.add(cmd.toString() + ": Spring Config Service.");
            }
        }
        return handlerInfo;
    }

    public void printUpdataHandler() {
        List<String> handlerInfo = getUpdataHandlerInfo();
        for (String s : handlerInfo) {
            log.info(s);
        }

    }

    public static void main(String[] args) {

        String ips = "192.9.145.19:2181,192.9.145.19:2182,192.9.145.19:2183";
        ips = "224.5.6.7:1234";
        ips = "127.0.0.1:3306";
        for (String ip : ips.split(",")) {
            System.out.println(ip);
            String[] ser = ip.split(":");
            System.out.println(HdpHelper.isPortInUse(ser[0], Integer.parseInt(ser[1])));
        }
    }
}
