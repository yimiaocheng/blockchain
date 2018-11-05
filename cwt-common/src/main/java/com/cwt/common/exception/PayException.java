package com.cwt.common.exception;


import com.cwt.common.enums.PayResultEnums;

/**
 * 商城对接支付
 */
public class PayException extends BaseException {

    public PayException(PayResultEnums code) {
        super(code.getCode(), code.getMsg());
    }
}
