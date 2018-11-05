package com.cwt.domain.constant;

public class IntegralConstants {
    /*********以下常量用商城积分的各种状态值*********/
    public static final int CT_TO_INTEGRAL = 0;  //资产兑换商城积分

    public static final int PAY_INTEGRAL_ONPAID_DELETE = 1;  //用户支付时，用户减少积分
    public static final int RECEIVE_INTEGRAL_BUSINESS_TO_ADD = 2;  //确认收款时，商户增加积分
    public static final int REFUND_INTEGRAL_PERSON_FROM_ADD = 3;  //单商户用户退款，增加积分
    public static final int REFUND_INTEGRAL_BUSINESS_FROM_ADD = 4;//多商户退款时，用户增加积分


}
