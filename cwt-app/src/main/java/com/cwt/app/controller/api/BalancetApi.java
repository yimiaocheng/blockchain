package com.cwt.app.controller.api;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/7/21 11:13
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
public class BalancetApi {
    public static final String BALANCE_CONTROLLER_API = "订单信息控制器";

    public static class addBuyOrderConfirm {
        public static final String METHOD_API_NAME = "创建余额交易前买单详情";
        public static final String METHOD_API_NOTE = "创建余额交易前买单详情";
        public static final String METHOD_API_ORDER_TOTAL_NUM = "交易总额";
        public static final String METHOD_API_LIMIT_NUM_MIN = "购买最小限制";
        public static final String METHOD_API_LIMIT_NUM_MAX = "购买上限";
        public static final String METHOD_API_PAY_TYPE = "支付方式：微信(0)、支付宝(1)、银行(2)";
        public static final String METHOD_API_PAY_BALANCECONVERTPERCENT = "余额 ： 现金";
    }

    public static class addSellOrderConfirm {
        public static final String METHOD_API_NAME = "创建余额交易前卖单详情";
        public static final String METHOD_API_NOTE = "创建余额交易前卖单详情";
        public static final String METHOD_API_ORDER_TOTAL_NUM = "交易总额";
        public static final String METHOD_API_LIMIT_NUM_MIN = "出售最小限制";
        public static final String METHOD_API_LIMIT_NUM_MAX = "出售上限";
        public static final String METHOD_API_PAY_TYPE = "支付方式：微信(0)、支付宝(1)、银行(2)";
        public static final String METHOD_API_PAY_BALANCECONVERTPERCENT = "余额 ： 现金";
    }

    public static class addBuyOrder {
        public static final String METHOD_API_NAME = "创建余额交易一个进行中的买单";
        public static final String METHOD_API_NOTE = "创建余额交易一个进行中的买单";
        public static final String METHOD_API_BALANCE_ORDER = "买单信息";
    }

    public static class addSellOrder {
        public static final String METHOD_API_NAME = "创建余额交易一个进行中的卖单";
        public static final String METHOD_API_NOTE = "创建余额交易一个进行中的卖单";
        public static final String METHOD_API_BALANCE_ORDER = "卖单信息";
    }

    public static class selectByCondition {
        public static final String METHOD_API_NAME = "根据筛选条件查询 *发布* 订单";
        public static final String METHOD_API_NOTE = "根据买/卖单类型、支付方式、交易数量、当前页码查询";
        public static final String METHOD_API_ORDERTYPE = "订单类型:买(1)、卖(-1)";
        public static final String METHOD_API_PAYTYPE = "支付方式:微信(0)、支付宝(1)、银行(2)、(可以没有)";
        public static final String METHOD_API_ORDERNUM = "交易数量(可以没有)";
        public static final String METHOD_API_PAGENUM = "起始行号";
        public static final String METHOD_API_PAGESIZE = "总行数";
        public static final String METHOD_API_ORDERBYTYPE = "分组类型";
    }

    public static class listByOrderTypeAndBillStatus {
        public static final String METHOD_API_NAME = "根据筛选条件查询 *交易* 订单";
        public static final String METHOD_API_NOTE = "根据买/卖单类型、交易状态查询、当前页码查询";
        public static final String METHOD_API_ORDERTYPE = "订单类型:买(1)、卖(-1) 可以为空";
        public static final String METHOD_API_BILLSTATUS = "交易状态:待处理(0)、进行中(1)、申诉中(3)、已完成(4)";
        public static final String METHOD_API_PAGENUM = "起始行号";
        public static final String METHOD_API_PAGESIZE = "总行数";
    }

    public static class headerOrderConfirmation {
        public static final String METHOD_API_NAME = "下单确认";
        public static final String METHOD_API_NOTE = "创建一个待处理交易订单";
        public static final String METHOD_AIP_ID = "订单ID";
        public static final String METHOD_AIP_ORDERUSERID = "发起订单的用户ID";
        public static final String METHOD_AIP_MONEYAMOUNT = "金额数量";
        public static final String METHOD_AIP_AMOUNT = "余额数量";
    }

    public static class headerAbandonBuy {
        public static final String METHOD_API_NAME = "放弃订单";
        public static final String METHOD_API_NOTE = "将订单状态改成失效（4）";
        public static final String METHOD_AIP_ID = "订单ID";
    }

    public static class updateOrderBillStatus {
        public static final String METHOD_API_NAME = "更新订单状态";
        public static final String METHOD_API_NOTE = "卖家点击下一步，将用户状态和订单状态设置为进行中";
        public static final String METHOD_AIP_ID = "订单ID";
    }

    public static class headerBuyAffirm {
        public static final String METHOD_API_NAME = "确认付款";
        public static final String METHOD_API_NOTE = "确认付款";
        public static final String METHOD_AIP_ID = "订单ID";
        public static final String METHOD_AIP_PASSWORD = "支付密码";
    }

    public static class headerSellAffirm {
        public static final String METHOD_API_NAME = "确认收款";
        public static final String METHOD_API_NOTE = "确认收款";
        public static final String METHOD_AIP_ID = "订单ID";
        public static final String METHOD_AIP_PASSWORD = "支付密码";
    }

    public static class headerAppealFile {
        public static final String METHOD_API_NAME = "申诉文件上传";
        public static final String METHOD_API_NOTE = "申诉文件上传";
        public static final String METHOD_AIP_APPEALFILE = "申诉上传的文件";
    }

    public static class headerAppeal {
        public static final String METHOD_API_NAME = "申诉";
        public static final String METHOD_API_NOTE = "申诉";
        public static final String METHOD_AIP_BILLID = "交易订单id";
        public static final String METHOD_AIP_APPEALFILENAME = "申诉上传文件路径";
        public static final String METHOD_AIP_APPEALTEXT = "申诉描述";
    }

    public static class headerAgreed2Appeal {
        public static final String METHOD_API_NAME = "同意申诉";
        public static final String METHOD_API_NOTE = "同意申诉";
        public static final String METHOD_AIP_ID = "订单ID";
        public static final String METHOD_AIP_APPEALID = "申诉记录ID";
    }

    public static class headerReject2Appeal {
        public static final String METHOD_API_NAME = "驳回申诉";
        public static final String METHOD_API_NOTE = "驳回申诉";
        public static final String METHOD_AIP_ID = "订单ID";
        public static final String METHOD_AIP_APPEALID = "申诉记录ID";
    }

    public static class headerRepeal2Appeal {
        public static final String METHOD_API_NAME = "撤回申诉";
        public static final String METHOD_API_NOTE = "同意申诉的反向操作";
        public static final String METHOD_AIP_ID = "订单ID";
        public static final String METHOD_AIP_APPEALID = "申诉记录ID";
    }

    public static class handleCancelOrder {
        public static final String METHOD_API_NAME = "强制撤销总单";
        public static final String METHOD_API_NOTE = "强制撤销总单";
        public static final String METHOD_AIP_ORDERID = "总单ID";
    }

    /***
     * 18.8.30
     * lzf
     *
     *
     */

    public static class selectBalanceOrderUpdateById {
        public static final String METHOD_API_NAME = "查询交易订单的基本信息";
        public static final String METHOD_API_NOTE = "根据 ID 查询交易订单的基本信息";
        public static final String METHOD_AIP_ID = "订单ID";
    }

    public static class headerBalanceOrderBuyUpdateConfirm {
        public static final String METHOD_API_NAME = "返回修改买单的状态详情";
        public static final String METHOD_API_NOTE = "返回修改买单的状态详情";
    }

    public static class headerBalanceOrderSellUpdateConfirm {
        public static final String METHOD_API_NAME = "返回修改卖单的状态详情";
        public static final String METHOD_API_NOTE = "返回修改卖单的状态详情";
    }

    public static class updateBuyOrder {
        public static final String METHOD_API_NAME = "修改余额交易买单订单";
        public static final String METHOD_API_NOTE = "修改余额交易买单订单接口";
    }

    public static class updateSellOrder {
        public static final String METHOD_API_NAME = "修改余额交易卖单订单";
        public static final String METHOD_API_NOTE = "修改余额交易卖单订单接口";
    }

    public static class listByReleaseRecords {
        public static final String METHOD_API_NAME = "已发布分页查询接口";
        public static final String METHOD_API_NOTE = "已发布分页查询接口";
        public static final String METHOD_API_NOTE_PAGENUM = "第几页";
        public static final String METHOD_API_NOTE_PAGESIZE = "一页几条";
    }

    public static class updateBalanceBuyOrderInvalid {
        public static final String METHOD_API_NAME = "买单撤单接口";
        public static final String METHOD_API_NOTE = "买单撤单接口";
        public static final String METHOD_API_ID = "买单ID";
    }

    public static class updateBalanceSellOrderInvalid {
        public static final String METHOD_API_NAME = "卖单撤单接口";
        public static final String METHOD_API_NOTE = "卖单撤单接口";
        public static final String METHOD_API_ID = "卖单ID";
    }
}
