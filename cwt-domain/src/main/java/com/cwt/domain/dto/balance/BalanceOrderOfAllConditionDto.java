package com.cwt.domain.dto.balance;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author huangxl
 * 用于封装后台查询所有交易总订单的筛选条件
 */
@Data
public class BalanceOrderOfAllConditionDto {
    private String userName;//发起人手机号
    private BigDecimal minNum;//最小数额
    private BigDecimal maxNum;//最大数额
    private Integer payType;//交易方式
    private Integer orderType;//订单类型
    private Integer orderStatus;//订单状态
    private Date beginTime;//开始时间
    private Date endTime;//结束时间
}
