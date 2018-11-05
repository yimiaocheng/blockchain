package com.cwt.domain.constant;

public class UserPayConstants {

    /**
     * 支付状态
     */
    public static final String PAY_STATUS_UNPAID = "1";//待支付
    public static final String PAY_STATUS_PAID = "2";//已支付

    /**
     * 支付类型
     */
    public static final String PAY_TYPE_CT = "ct";//ct支付
    public static final String PAY_TYPE_INTEGRAL = "integral";//积分支付

    /**
     * 确认收款类型
     */
    public static final String RECEIVE_TYPE_1 = "1";//待收款
    public static final String RECEIVE_TYPE_2 = "2";//已收款


    /**
     * 退款状态
     */
    public static final String REFUND_TYPE_1 = "1";//退款失败
    public static final String REFUND_TYPE_2 = "2";//退款成功

    /**
     * 商户类型
     */
    public static final String USER_TYPE_0 = "0";//单商户
    public static final String USER_TYPE_1 = "1";//多商户


    /**
     * 请求地址
     */
    //商城回调地址
    public static final String REGISTER_URL = "http://ctyz.hqshop.com:8087/app/index.php?i=1&c=entry&m=ewei_shopv2&do=mobile&r=api.reg";

}
