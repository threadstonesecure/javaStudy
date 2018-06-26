package com.yuntai.hdp.server.updata;

/**
 * @Description hdp_updatahandler表的PO
 * @author denglt
 *
 */
public class Command {

	private String code;
	private String interfaceName;
	private String groupName;
	private int registryId;
	private String memo;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public int getRegistryId() {
		return registryId;
	}

	public void setRegistryId(int registryId) {
		this.registryId = registryId;
	}
	

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public String toString() {
		return "Command [code=" + code + ", interfaceName=" + interfaceName
				+ ", groupName=" + groupName + ", registryId=" + registryId + ", memo=" + memo
				+ "]";
	}

	




}
