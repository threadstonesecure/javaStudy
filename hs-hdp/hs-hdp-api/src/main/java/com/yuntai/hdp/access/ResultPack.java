package com.yuntai.hdp.access;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description 数据结果包
 * @author denglt@hundsun.com
 * 
 *         处理结果 ResultPack.kind定义: Kind=0:处理成功
 *         Kind=-1:dubbo服务异常(如：服务不存在、调用失败、调用超时等等) Kind=1000:
 *         RequestPack.body的数据格式不对（msg返回数据格式要求，如：请使用com.xxx.Bean进行数据传递）
 *         Kind=1001: RequestPack.body中缺失关键数据(msg返回提示,如:属性患者id[patId]不能为null等等)
 *         Kind=1002: DAO操作入库失败(msg返回数据库失败信息) Kind=1009: 运行时意外(msg返回运行时的异常信息)
 * @CopyRight: 版权归Hundsun 所有
 */
public final class ResultPack implements Serializable {

	private static final long serialVersionUID = -5993068065975332469L;
	private String seqno; // 流水号
	private String hosId; // 医院id
	private String cmd; // 命令编号
	private String kind;
	private String msg; // 消息
	private String body; // 可选 返回操作结果集
	private long returnTime; // 返回 时间戳
	private int callMode = 0; // 调用方式　(0:异步，1：同步)

	private String hdpSeqno; // hdp内部流水号，由HDP来维护
	private String hdpOther; // 由HDP来维护,预留

	public String getSeqno() {
		return seqno;
	}

	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}

	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public long getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(long returnTime) {
		this.returnTime = returnTime;
	}

	public int getCallMode() {
		return callMode;
	}

	public void setCallMode(int callMode) {
		this.callMode = callMode;
	}

	public String getHdpSeqno() {
		return hdpSeqno;
	}

	public void setHdpSeqno(String hdpSeqno) {
		this.hdpSeqno = hdpSeqno;
	}

	public String getHdpOther() {
		return hdpOther;
	}

	public void setHdpOther(String hdpOther) {
		this.hdpOther = hdpOther;
	}

	@Override
	public String toString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "ResultPack [seqno=" + seqno + ", hdpSeqno=" + hdpSeqno
				+ ", hosId=" + hosId + ", cmd=" + cmd + ", kind=" + kind
				+ ", msg=" + msg + ", body=" + body + ", returnTime="
				+ dateFormat.format(new Date(returnTime)) + "]";
	}

	public String toKeyString() {
		return "ResultPack{" + "seqno=" + seqno + ", hdpSeqno=" + hdpSeqno
				+ ", hdpOther=" + hdpOther + ", hosId=" + hosId +  ", cmd=" + cmd + ", kind="
				+ kind + ", msg=" + msg + '}';
	}
}
