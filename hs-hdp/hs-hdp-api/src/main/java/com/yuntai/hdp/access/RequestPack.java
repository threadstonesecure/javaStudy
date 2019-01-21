package com.yuntai.hdp.access;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author denglt@hundsun.com
 * @Description 数据请求包
 * @CopyRight: 版权归Hundsun 所有
 */

public final class RequestPack implements Serializable {

    private static final long serialVersionUID = 1404434278204638711L;

    private String clientId; // 消息发送端唯一id(UUID)
    private String seqno; // 业务流水号
    private String hosId; // 医院id
    private String cmd; // 命令编号
    private String body; // 任意JSON数据包(由应用解包)
    private long sendTime = 0; // 请求时间戳
    private int callMode = 0; // 调用方式　(0:异步，1：同步) 由hdp内部维护
    private String hdpSeqno; // hdp内部流水号，由HDP来维护
    private String hdpOther; // 由HDP来维护,预留

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
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
        return "RequestPack [clientId=" + clientId + ", seqno=" + seqno
                + ", hdpSeqno=" + hdpSeqno + ", hostId=" + hosId + ", cmd="
                + cmd + ", body=" + body + ", callMode=" + callMode
                + ", sendTime=" + dateFormat.format(new Date(sendTime)) + "]";
    }

    public String toKeyString() {
        return "RequestPack{" + "seqno=" + seqno + ", hdpSeqno=" + hdpSeqno
                + ", hdpOther=" + hdpOther + ", hosId=" + hosId + ", cmd="
                + cmd + ", callMode=" + callMode + '}';
    }

    public RequestPack(String hosId, String cmd, String body) {
        this.body = body;
        this.hosId = hosId;
        this.cmd = cmd;
    }

    public RequestPack() {

    }
}
