package com.cwt.common.exception;

import com.cwt.common.enums.BalanceOrderResultEnums;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/8/27 0027 14:02
 * \* User: YeHao
 * \
 */
public class BalanceOrderException extends BaseException{
    public BalanceOrderException(BalanceOrderResultEnums code) {
        super(code.getCode(), code.getMsg());
    }
}
