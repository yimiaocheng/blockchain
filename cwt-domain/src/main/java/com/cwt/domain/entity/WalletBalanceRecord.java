package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * WalletBalanceRecord 数据传输类
 * @date 2018-08-23 14:41:59
 * @version 1.0
 */
@Table(name = "app_wallet_balance_record")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletBalanceRecord extends BaseModel {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "target_user_id")
	private String targetUserId;
	@Column(name = "opt_user_id")
	private String optUserId;
	@Column(name = "opt_user_show_msg")
	private String optUserShowMsg;
	@Column(name = "amount")
	private BigDecimal amount;
	@Column(name = "service_charge")
	private BigDecimal serviceCharge;
	@Column(name = "subsidy")
	private BigDecimal subsidy;
	@Column(name = "event_id")
	private String eventId;
	@Column(name = "arithmetic_type")
	private Integer arithmeticType;
	@Column(name = "change_type")
	private Integer changeType;
	@Column(name = "create_time")
	private java.util.Date createTime;

}