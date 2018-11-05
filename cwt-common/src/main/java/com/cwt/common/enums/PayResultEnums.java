package com.cwt.common.enums;

public enum PayResultEnums {

    HTTP_PARAM_NULL("201","请求参数不能为空"),
    FROM_ID_NULL("202","用户id不能为空"),
    TO_ID_NULL("203","商户id不能为空"),
    ORDER_NO_NULL("204","订单id不能为空"),
    ORDER_FEE_NULL("205","订单费用不能为空"),
    PAY_TYPE_NULL("206","支付类型不能为空"),
    ORDER_BODY_NULL("207","商品描述不能为空"),
    NOTIFY_URL_NULL("208","支付回调地址不能为空"),
    SIGN_NULL("200","签名不能为空"),
    TIMESIAMP_NULL("210","签名时间戳不能为空"),

    SIGN_ERROR("211","签名错误"),
    FLOWBALANCE_NOT_ENOUGH("212","用户ct余额不足"),
    INTEGRAL_NOT_ENOUGH("213","用户积分余额不足"),

    PAYMENT_TOO_LONG("214","支付超时"),
    PAYPASSWORD_NULL("215","支付密码为空"),
    PAYPASSWORD_ERROR("216","支付密码不正确"),
    ORDER_PAYPAL("216","重复支付"),
    PAY_FAIL("217","扣款失败"),
    RECEIVE_FAIL("221","扣款失败"),
    UPDATE_PAY_STAYUS("218","更新支付状态失败"),
    ORDER_NOT_EXISTENT("219","订单不存在"),
    ORDER_NO_PAID("220","用户还未支付，商户不能进行收款"),
    UPDATE_RECEIVE_STAYUS("222","更新支付状态失败"),
    REFUNDFEE_NONULL("223","退款金额不能为空"),
    REFUNDFEE_ERROR("224","退款金额大于订单金额"),
    REFUNDFEE_FAIL("225","退款失败"),
    AWARD_TYPE_NULL("226","奖金发放类型为空"),
    AWARD_FAIL("227","奖金发放失败");


    private String code;
    private String msg;

    PayResultEnums(String code, String msg) {
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
