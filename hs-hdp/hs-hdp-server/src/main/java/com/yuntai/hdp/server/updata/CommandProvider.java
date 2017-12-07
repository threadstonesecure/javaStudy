package com.yuntai.hdp.server.updata;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.access.service.UpdataHandler;
import com.yuntai.hdp.server.updata.dynamic.Command;

/**
 * @Description 指令提供者类 建立指令编码与UpdataHander的对应关系
 * @author denglt@hundsun.com
 * @CopyRight: 版权归Hundsun 所有
 */
public class CommandProvider {

	private static Log log = LogFactory.getLog(CommandProvider.class.getName());
	private String code;
	private Command cmd;
	private UpdataHandler handler;

	public CommandProvider() {

	}

	public CommandProvider(String cmdCode, UpdataHandler handler) {
		this.code = cmdCode;
		this.handler = handler;
	}

	public CommandProvider(Command cmd, UpdataHandler handler) {
		this.code = cmd.getCode();
		this.cmd = cmd;
		this.handler = handler;
	}

	public boolean matches(String cmdCode) {
		if (code == null || !code.equals(cmdCode))
			return false;
		return true;
	}

	public ResultPack process(RequestPack request) {
		if (cmd == null) {
			log.info(String
					.format("UpdataHandler[%s]  is processing RequestPack from Hospital[%s]!",
							handler.getClass(), request.getHosId()));
		} else {
			log.info(String
					.format("%s  is processing RequestPack from Hospital[%s]!",
							cmd, request.getHosId()));
		}
		return handler.process(request);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public UpdataHandler getHandler() {
		return handler;
	}

	public void setHandler(UpdataHandler handler) {
		this.handler = handler;
	}

	public Command getCmd() {
		return cmd;
	}

	public void setCmd(Command cmd) {
		this.cmd = cmd;
	}

	
}
