package com.cwt.domain.dto.balance;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * BalanceOrderBill 数据传输类
 * @date 2018-08-22 17:28:41
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceOrderBillDto extends BaseDto {
	private String id;
	private String orderId;
	private String orderUserId;
	private Integer orderUserStatus;
	private String optUserId;
	private Integer optUserStatus;
	private Integer orderType;
	private BigDecimal moneyAmount;
	private BigDecimal amount;
	private BigDecimal amountReal;
	private Integer billStatus;
	private java.util.Date createTime;
	private java.util.Date modifyTime;

}