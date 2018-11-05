package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * BalanceOrder 数据传输类
 * @date 2018-08-22 17:28:41
 * @version 1.0
 */
@Table(name = "app_balance_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceOrder extends BaseModel {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "user_id")
	private String userId;
	@Column(name = "balance_convert_percent")
	private BigDecimal balanceConvertPercent;
	@Column(name = "server_charge")
	private BigDecimal serverCharge;
	@Column(name = "subsidy")
	private BigDecimal subsidy;
	@Column(name = "order_total_num")
	private BigDecimal orderTotalNum;
	@Column(name = "order_num")
	private BigDecimal orderNum;
	@Column(name = "limit_num_min")
	private BigDecimal limitNumMin;
	@Column(name = "limit_num_max")
	private BigDecimal limitNumMax;
	@Column(name = "pay_type")
	private Integer payType;
	@Column(name = "order_type")
	private Integer orderType;
	@Column(name = "order_status")
	private Integer orderStatus;
	@Column(name = "create_time")
	private java.util.Date createTime;
	@Column(name = "modify_time")
	private java.util.Date modifyTime;

}