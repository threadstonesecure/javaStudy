package dlt.study.comm;

/**
 * Created by denglt on 2017/6/28.
 */
public enum PayStatusEnum {
    UNPAID(0,"未支付"), PAID(1,"已支付");
    private Integer status;
    private String statusDesc;

    PayStatusEnum(Integer status, String statusDesc) {
        this.status = status;
        this.statusDesc = statusDesc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}

