package com.cwt.domain.constant;

import java.math.BigDecimal;

public class InformationConstants {
    //配置信息枚举
    public static final String DATA_NAME_SERVICE_CHARGE = "service_charge";//扫码转账手续费
    public static final String DATA_NAME_SERVICE_CHARGE_C2C = "service_charge_c2c";//节点转账手续费

    public static final String DATA_NAME_AWARD_AMOUNT = "award_amount";//可以获得推荐奖的上级总数
    public static final String DATA_NAME_INITIAL_FORCE = "initial_force";//注册时赠送的算力

    public static final String DATA_NAME_WEIGHTING_RATIO = "weighting_ratio";//加权奖比率
    public static final String DATA_NAME_WEIGHTING_TOP = "weighting_top";//加权奖奖池封顶值
    public static final String DATA_NAME_CURRENT_VERSION = "current_version";//手机App当前版本
    public static final String DATA_NAME_LEAST_FORCE = "least_force";//算力最少是多少才可以获得智能释放奖励
    public static final String DATA_NAME_CENTER_ACCOUNT = "center_account";//中央账户发行总量
    // 余额交易比例配置
    public static final String DATA_NAME_SELL_PLATFORM_FEE_RATIO = "sell_platform_fee_ratio";//余额交易卖单平台手续费比例
    public static final String DATA_NAME_SELL_SUBSIDY_RATIO = "sell_subsidy_ratio";//余额交易卖单补贴比例
    public static final String DATA_NAME_BALANCE_ORDER_MIN_NUM = "balance_order_min_num";//余额交易最低限制值

    //数字资产兑换商城积分、游戏代币等规则
    public static final String DATA_NAME_TRANS_RATE = "trans_rate";//余额兑换的算力杠杆
    public static final String DATA_NAME_TRANS_BALANCE_RATE = "trans_balance_rate";//余额兑换的比例
    public static final String DATA_NAME_CT_RULES_GROUPING = "ct_rules";//数字资产兑换规则分组名称
    public static final String DATA_NAME_CT_TO_INTEGRAL_RATE = "ct_to_integral_rate";//数字资产兑换商城积分比例
    public static final String DATA_NAME_CT_TO_GAME_COIN_RATE = "ct_to_game_coin_rate";//数字资产兑换游戏代币比例
    public static final String DATA_NAME_GAME_COIN_TO_CALCULATION = "game_coin_to_calculation";//游戏代币兑换智能算力比例

    //状态
    public static final Integer STATUS_SHOW = 1;
    public static final Integer STATUS_HIDE = 0;
}
