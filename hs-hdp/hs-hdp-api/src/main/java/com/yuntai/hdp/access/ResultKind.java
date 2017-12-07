package com.yuntai.hdp.access;

/**
 * HDP 结果类型定义， -1000 <= Kind<=-1 的值为Hdp内部使用
 */
public enum ResultKind {

	OK("0", "处理成功！"),
    ERROR_CALL_DUBBO("-1","调用dubbo服务异常 如：服务不存在、调用失败、调用超时等等"),
    ERROR_TIMEOUT("-2","请求对接超时!超时设置[%s]秒"),
    ERROR_NOEXISTS_HOSP("-3","医院[%s]没有登陆HDP"),
    ERROR_NOEXISTS_ACCESSHOSP("-4","医院[%s]端HdpClient没有配置同步对接处理器"),
    ERROR_ACCESS_HOSPITAL("-5","医院[%s]对接服务发生异常"),
    ERROR_NET("-6","网络异常"),
	ERROR_DATAFORMAT("1000", "请使用[%s]进行数据传递"),
	ERROR_MISS_KEYFIELD("1001", "缺失关键字段[%s]数据!"),
	ERROR_DAO("1002", "入库失败:%s"),
    ERROR_BUSINESS("1003","业务处理错误:%s"),
	ERROR_RUNTIME("1009", "运行时意外:%s");

	private String kind;
	private String msg;

	private ResultKind(String kind, String msg) {
		this.kind = kind;
		this.msg = msg;
	}

	public String getKind() {
		return kind;
	}

	public String getOriginalMessage() {
		return msg;
	}

	public String getMessage(String info) {
		try {
			String msg = String.format(this.msg, info);
			return msg;
		} catch (Exception e) {
			return msg;
		}
	}
}
