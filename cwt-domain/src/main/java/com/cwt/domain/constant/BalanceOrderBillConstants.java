package com.cwt.domain.constant;

public class BalanceOrderBillConstants {
    //用户操作状态
    public static final int USER_STATUS_PENDING = 0;//待处理
    public static final int USER_STATUS_AFFIRM = 1;//确认支付/收款
    public static final int USER_STATUS_APPEAL = 2;//申诉

    //交易订单状态
    public static final int BILL_STATUS_PENDING = 0;//待处理
    public static final int BILL_STATUS_AFFIRM = 1;//进行中
    public static final int BILL_STATUS_APPEAL = 2;//申诉
    public static final int BILL_STATUS_FINISH = 3;//已完成
    public static final int BILL_STATUS_LOSE_EFFICACY = 4;//失效

    //发布订单状态
    public static final int ORDER_STATUS_NEW = 0;//新建(待处理)
    public static final int ORDER_STATUS_UNDERWAY = 1;//进行中
    public static final int ORDER_STATUS_FINISH = 2;//已完成
    public static final int ORDER_STATUS_INVALID = 3;//已失效
    public static final int ORDER_STATUS_TRADING = 4;//交易中

    //交易类型
    public static final int ORDER_TYPE_SELL = -1;//卖单
    public static final int ORDER_TYPE_BUY = 1;//买单

    //支付方式
    public static final int PAY_TYPE_WECHAT = 0;//微信
    public static final int PAY_TYPE_ALIPAY = 1;//支付宝
    public static final int PAY_TYPE_BANK = 2;//银行卡

    //申诉记录状态
    public static final int APPEAL_STATUS_NEW = 0;//新建
    public static final int APPEAL_STATUS_AGREED = 1;//同意申诉
    public static final int APPEAL_STATUS_REJECT = 2;//驳回申诉
    public static final int APPEAL_STATUS_REPEAL = 3;//撤销申诉

    //角色
    public static final int TRADER = 0 ;//代表操作交易者
    public static final int PROMULGATOR = 1;//代表操作发布者
    public static final int NULL_ROLE = 2;//没有角色,代表不操作

}
