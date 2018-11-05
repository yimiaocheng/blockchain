package com.cwt.domain.entity;

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
@Table(name = "app_balance_order_bill")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceOrderBill extends BaseModel {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "order_id")
	private String orderId;
	@Column(name = "order_user_id")
	private String orderUserId;
	@Column(name = "order_user_status")
	private Integer orderUserStatus;
	@Column(name = "opt_user_id")
	private String optUserId;
	@Column(name = "opt_user_status")
	private Integer optUserStatus;
	@Column(name = "order_type")
	private Integer orderType;
	@Column(name = "money_amount")
	private BigDecimal moneyAmount;
	@Column(name = "amount")
	private BigDecimal amount;
	@Column(name = "amount_real")
	private BigDecimal amountReal;
	@Column(name = "bill_status")
	private Integer billStatus;
	@Column(name = "create_time")
	private java.util.Date createTime;
	@Column(name = "modify_time")
	private java.util.Date modifyTime;

}