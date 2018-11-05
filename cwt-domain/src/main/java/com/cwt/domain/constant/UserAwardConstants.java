package com.cwt.domain.constant;

public class UserAwardConstants {

    /**
     * 奖金类型（1.销售提成2.差额利润3.服务补贴4.加权分红）
     */
    public static final String AWARD_TYPE_SALES = "1";
    public static final String AWARD_TYPE_PROFIT = "2";
    public static final String AWARD_TYPE_SUBSIDY = "3";
    public static final String AWARD_TYPE_WEIGHTED = "4";

    /**
     * 奖金发放状态
     */
    public static final String AWARD_STATUS_UNISSUED = "0";//待处理
    public static final String AWARD_STATUS_ISSUED = "1";//已经处理
}
