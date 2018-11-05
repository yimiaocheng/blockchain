package com.cwt.common.enums;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/7/9 18:30
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
public enum BalanceOrderResultEnums {
    ORDER_ID_NULL("2000","订单id为空"),
    ORDER_USERID_NULL("2000","发起用户id为空"),
    OPT_USERID_NULL("2000","操作用户id为空"),
    MONEY_AMOUNT_NULL("2000","金额数量为空"),
    AMOUNT_NULL("2000","资产数量为空"),

    SELLER_NEXT("200","卖家已点下一步，请刷新页面！"),
    BUYER_NEXT("200","买家已付款，请刷新页面！"),
    BILL_CHANGE("200","订单状态已改变，请刷新页面！"),
    BIll_AFFIRM("200","卖家已点下一步无法取消订单，请刷新页面！"),
    BIll_APPEAL("200","对方已提出申诉，请刷新页面！"),
    BIll_APPEAL_AGAIN("200","对方已提出申诉，如有疑问请联系工作人员！"),
    BILL_FINNSH("200","订单已完成，如有疑问请联系工作人员！"),

    NO_ORDER("2001","订单不存在"),
    NO_ORDER_USER("2001","发布用户不存在"),
    NO_OPT_USER("2001","操作用户不存在"),
    USER_ERROR("2001","该订单与当前用户没有交易关系"),
    USER_APPEAL_ERROR("2001","订单异常，没有用户进行申诉"),
    ORDER_BILL_ERROR("2001","订单信息异常！"),
    ORDER_TYPE_CHANGE("2001","订单状态已变更，请刷新重试"),

    ORDER_NO_AFFIRM("2001","订单已结束！"),

    AMOUNT_MORE_THAN_BALANCE("2002","交易的资产数量超过当前订单数量！"),
    AMOUNT_INCONFORMITY("2002","交易的资产数量不符合交易限额"),
    AMOUNT_INSUFFICIENT("2002","订单资产不足，订单已结束"),

    OBJECT_NULL("3000","订单创建失败，请把信息补充完整"),
    //比例数值 String 类型转 BigDecimal 类型失败，预计为配置表数据有误
    BIGDECIMAL_ERROR("3000","订单创建失败，请联系客服"),
    // 方法执行成功，但是添加失败！
    ADD_ERROR("3000","订单创建失败，请联系客服"),
    FLOW_BALANCE_INSUFFICIENT("3000","订单创建失败，数字资产不足"),
    FLOW_BALANCE_MIN_ERROR("3000","交易限额的 '最小限额' 不能小于 1 "),
    FLOW_BALANCE_MAX_ERROR("3000","交易限额的 '最小限额' 不能大于 '最大限额'"),
    FLOW_BALANCE_MAX_ERROR_TWO("3000","交易限额的 '最大限额' 不能大于 number"),
    BALANCE_CONVERT_PERCENT_ERROR("3000","交易单价最低限价：number CNY"),
    FLOW_BALANCE_ORDERNUM_ERROR("3000","交易限额的 '最大限额' 不能大于 '交易总额'"),

    TRANSACTION_PRODUCTION("3001","交易已产生，订单无法修改"),
    UPDATE_FLOW_BALANCE_INSUFFICIENT("3001","订单修改失败，数字资产不足"),
    UPDATE_ERROR("3001","订单修改失败，请联系客服"),
    TRANSACTION_PRODUCTION_INVALID("3003","交易已产生，撤单操作无法进行"),
    TRANSACTION_COMPLETE_ERROR("3003","订单已完成，撤单操作无法进行");

    private String code;
    private String msg;

    BalanceOrderResultEnums(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }
}
