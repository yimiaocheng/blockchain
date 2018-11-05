package com.cwt.common.enums;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/8/9 11:05
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
public enum BaseResultEnums {
    SUCCESS("200","SUCCESS"),
    NO_LOGIN("201","未登录"),
    USER_NULL("1000","用户不存在"),
    USERID_NULL("1001","用户id不能为空"),
    TELEPHONE_NULL("1002","手机号不能为空"),
    NO_PAYPASSWORD("202","未设置支付密码"),
    PAYPASSWORD_NULL("203","支付密码不能为空"),
    PAYPASSWORD_ERROR("204","支付密码不正确"),
    BALANCE_NOT_ENOUGH("2001","钱包余额不足"),

    HTTP_GET_ERROR("505","请求出错"),
    HTTP_POST_ERROR("505","请求出错"),
    DEFAULT("100","SYSTEM_ERROR");

    private String code;
    private String msg;

    BaseResultEnums(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
