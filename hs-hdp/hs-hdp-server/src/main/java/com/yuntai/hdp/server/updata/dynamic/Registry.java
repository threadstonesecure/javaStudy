package com.yuntai.hdp.server.updata.dynamic;

import com.alibaba.dubbo.config.RegistryConfig;
import org.apache.commons.lang3.StringUtils;

/**
 * @author denglt
 * @Description hdp_dubbo_registry表的PO
 */
public class Registry {
    private int registryId;
    private String protocol;
    private String address;
    private Integer port;
    private String userName;
    private String password;
    private String groupname;
    private volatile RegistryConfig rConfig;

    public RegistryConfig getRegistryConfig() {
        RegistryConfig rConfigToUse = this.rConfig;
        if (rConfigToUse == null)
            synchronized (this) {
                if (this.rConfig == null) {
                    rConfigToUse = new RegistryConfig();
                    rConfigToUse.setProtocol(protocol);
                    rConfigToUse.setAddress(address);
                    rConfigToUse.setPort(port);
                    rConfigToUse.setUsername(userName);
                    rConfigToUse.setPassword(password);
                    rConfigToUse.setCheck(true);
                    if (!StringUtils.isBlank(groupname))
                        rConfigToUse.setGroup(groupname);

                    this.rConfig = rConfigToUse;
                }

            }

        return rConfig;
    }


    public int getRegistryId() {
        return registryId;
    }


    public void setRegistryId(int registryId) {
        this.registryId = registryId;
    }


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    @Override
    public String toString() {
        return "Registry [registryId=" + registryId + ", protocol=" + protocol
                + ", address=" + address + ", port=" + port + ", userName="
                + userName + ", password=" + password + ", groupname=" + groupname + "]";
    }


}
