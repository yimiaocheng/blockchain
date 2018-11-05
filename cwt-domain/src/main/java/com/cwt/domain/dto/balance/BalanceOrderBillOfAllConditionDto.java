package com.cwt.domain.dto.balance;

import lombok.Data;

import java.util.Date;

/**
 * @author huangxl
 * 用于封装后台查询所有交易明细的筛选条件
 */
@Data
public class BalanceOrderBillOfAllConditionDto {
    private String orderId;//总订单ID
    private String billId;//订单明细ID
    private String orderUserName;//发起人手机号
    private String optUserName;//操作人手机号
    private Integer billStatus;//订单状态
    private Date beginTime;//开始时间
    private Date endTime;//结束时间
}
