package com.cwt.common.enums;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/7/9 18:30
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
public enum LoginResultEnums {
    API_ERROR("1000","API不存在"),
    SMSGCODE_ERR("2001", "验证码获取失败");
    private String code;
    private String msg;

    LoginResultEnums(String code, String msg) {
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
