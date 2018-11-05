package com.cwt.domain.dto.balance;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BalanceOrderBillOfAllListDto {
    private String orderUserName;//发起人手机号
    private Integer orderUserStatus;//发起人状态
    private String optUserName;//操作人手机号
    private Integer optUserStatus;//操作人状态
    private BigDecimal moneyAmount;//人民币
    private BigDecimal amount;//购买|出售的余额数
    private BigDecimal amountReal;//真实获得的余额数
    private Integer billStatus;//订单状态
    private Integer orderType;//订单状态
    private Date createTime;//创建时间
}
