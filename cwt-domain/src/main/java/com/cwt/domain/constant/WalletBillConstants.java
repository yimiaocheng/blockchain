package com.cwt.domain.constant;

public class WalletBillConstants {
    /*********以下常量用作余额、算力记录表的各种状态值*********/
    public static final int ARITHMETIC_TYPE_ADD = 1;//类型 1 增加
    public static final int ARITHMETIC_TYPE_SUB = -1;//类型 2 减少

    //获取算力、算力的途径
    public static final int CHANGE_TYPE_REGISTER = 0;//注册时
    public static final int CHANGE_TYPE_TRANS = 1;//余额兑换时
    public static final int CHANGE_TYPE_TRANSFER = 2;//节点转账时
    public static final int CHANGE_TYPE_DEAL = 3;//余额交易时
    public static final int CHANGE_TYPE_RECOMMEND_AWARD = 4;//推荐奖
    public static final int CHANGE_TYPE_WEIGHTING_AWARD = 5;//加权奖
    public static final int CHANGE_TYPE_AUTO_TRANSFER = 6;//智能释放
    public static final int CHANGE_TYPE_CREATE_BALANCE_ORDER = 7;//创建卖单扣减
    public static final int CHANGE_TYPE_TRANSFER_SCAN_OUT = 8;//扫码支付
    public static final int CHANGE_TYPE_TRANSFER_SCAN_IN = 9;//二维码收款
    public static final int CHANGE_TYPE_UPDATE_BALANCE_ORDER = 10;//修改卖单
    public static final int CHANGE_TYPE_UPDATE_BACKEND = 11;//后台修改
    public static final int CHANGE_TYPE_REVOCATION_SELL_ORDER = 12;//撤销卖单返还
    public static final int CHANGE_TYPE_ROLL_BACK = 13;//余额交易数量不足以下一次交易，返回余额
    public static final int CHANGE_TYPE_APPEAL_ROLL_BACK = 14;//申诉作废
    public static final int CHANGE_TYPE_ORDERS_FOR_FAILURE = 15;//订单失效时返回余额

    public static final int PAY_CT_ONPAID_DELETE = 16;//用户支付时，用户减少ct
    public static final int RECEIVE_CT_BUSINESS_TO_ADD = 17;//确认收款时，商户增加ct
    public static final int RECEIVE_CALCULATIONFORCE_BUSINESS_FROM_ADD = 18;//确认收款时，用户增加相应的算力
    public static final int REFUND_CT_BUSINESS_FROM_ADD = 19;//多商户退款时，用户增加ct
    public static final int REFUND_CT_BUSINESS_TO_DELETE = 20;//多商户退款时，商户减少ct
    public static final int REFUND_CT_PERSON_FROM_ADD = 21;//单商户退款时，用户增加ct


    public static final int USER_AWARD_FROM_ADD_1 = 22;//奖金发放时，用户增加ct(销售提成)
    public static final int USER_AWARD_FROM_ADD_2 = 23;//奖金发放时，用户增加ct(差额利润)
    public static final int USER_AWARD_FROM_ADD_3 = 24;//奖金发放时，用户增加ct(服务补贴)
    public static final int USER_AWARD_FROM_ADD_4 = 25;//奖金发放时，用户增加ct(加权分红)


    //余额兑换算力记录表 该状态用作判断推荐奖是否已添加至推荐人账户
    public static final int STATUS_UNTREATED = 0;//未处理
    public static final int STATUS_PROCESSED = 1;//已处理



}
