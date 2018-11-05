package com.cwt.common.exception;

import com.cwt.common.enums.BaseResultEnums;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/7/4 10:24
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
public class BaseException extends RuntimeException {
    /**
     * 错误代码
     */
    private String code;

    /**
     * 错误提示信息
     */
    private String msg;

    public BaseException(BaseResultEnums enums) {
        this(enums.getCode(), enums.getMsg());
    }

    public BaseException(String code, String msg) {
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
