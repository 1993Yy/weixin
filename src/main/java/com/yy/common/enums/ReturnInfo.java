package com.yy.common.enums;

/**
 * @Package: com.yy.common.enums
 * @ClassName: ReturnInfo
 * @Author: Created By Yy
 * @Date: 2019-05-29 15:24
 */
public enum ReturnInfo {
    /**
     * 成功
     */
    Success("000000", "成功"),
    /**
     * 失败
     */
    Fail("000001", "失败"),
    /**
     * 业务异常
     */
    businessError("000995", "业务异常");

    private String code;
    private String msg;

    ReturnInfo(String code, String msg){
        this.code=code;
        this.msg=msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
