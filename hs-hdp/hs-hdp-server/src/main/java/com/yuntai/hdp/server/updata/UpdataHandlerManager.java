package com.yuntai.hdp.server.updata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.yuntai.hdp.access.ResultKind;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.access.service.UpdataHandler;

/**
 * @Description UpdataHandler管理者，代理UpdataHandler
 * @author denglt@hundsun.com
 * @CopyRight: 版权归Hundsun 所有
 */

public class UpdataHandlerManager implements UpdataHandler {

	private static Log log = LogFactory.getLog(UpdataHandlerManager.class);

	private List<CommandProvider> providers = new ArrayList<CommandProvider>();

	private ReentrantLock lock = new ReentrantLock();

	@Override
	public boolean checkData(RequestPack data) {
		return true;
	}

	@Override
	public ResultPack process(RequestPack request) {
        //log.info(String.format("===>收到调用云服务请求:%s", request.toKeyString()));
		String cmdCode = request.getCmd();
		if (cmdCode == null || cmdCode.equals("")) {
			log.error("RequestPack cmd is null。");
			return null;
		}
		CommandProvider provider = null;
		try {
			lock.lock();
			for (CommandProvider temp : providers) {
				if (temp.matches(cmdCode)) {
					provider = temp;
					break;
				}

			}
		} finally {
			lock.unlock();
		}
		if (provider != null) {
			try {
				ResultPack resultPack = provider.process(request);
                resultPack.setHdpSeqno(request.getHdpSeqno());
                resultPack.setCallMode(request.getCallMode());
				return resultPack;
			} catch (Exception ex) {
				log.error(String.format("云服务[%s]异常：", provider.getCmd()), ex);
				ResultPack resultPack = new ResultPack();
				resultPack.setSeqno(request.getSeqno());
				resultPack.setCmd(request.getCmd());
				resultPack.setHosId(request.getHosId());
				resultPack.setKind(ResultKind.ERROR_CALL_DUBBO.getKind());
				resultPack.setMsg(String.format("云服务[%s]异常。\n 错误信息：%s", provider.getCmd(),ex.getMessage()));
				resultPack.setReturnTime(System.currentTimeMillis());
				return resultPack;
			}
		}

		ResultPack resultPack = new ResultPack();
		resultPack.setSeqno(request.getSeqno());
		resultPack.setCmd(request.getCmd());
		resultPack.setHosId(request.getHosId());
		resultPack.setKind(ResultKind.ERROR_CALL_DUBBO.getKind());
		resultPack.setMsg("Command[" + cmdCode + "] doesn't have a provider or not exists!");
		resultPack.setReturnTime(System.currentTimeMillis());
		return resultPack;
	}

	public List<CommandProvider> getProviders() {
		return providers;
	}

	public void setProviders(List<CommandProvider> providers) {

		for (CommandProvider provider : providers) {
			if (provider.getHandler() != null)
				this.providers.add(provider);
		}
	}

	public void add(CommandProvider provider) {
		try {
			lock.lock();
			providers.add(provider);
		} finally {
			lock.unlock();
		}
	}

	public void delete(String cmd) {
		try {
			lock.lock();
			Iterator<CommandProvider> it = providers.iterator();
			while (it.hasNext()) {
				CommandProvider cp = it.next();
				if (cp.matches(cmd)) {
					it.remove();
				}
			}
		} finally {
			lock.unlock();
		}
	}
}
