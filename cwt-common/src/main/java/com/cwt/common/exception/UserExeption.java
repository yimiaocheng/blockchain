package com.cwt.common.exception;


import com.cwt.common.enums.UserResultEnums;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/7/9 18:29
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
public class UserExeption extends BaseException {
    public UserExeption(UserResultEnums code) {
        super(code.getCode(), code.getMsg());
    }
}
