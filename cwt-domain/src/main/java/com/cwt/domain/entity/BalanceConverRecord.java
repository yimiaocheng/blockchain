package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * BalanceConverRecord 数据传输类
 * @date 2018-08-23 16:07:18
 * @version 1.0
 */
@Table(name = "app_balance_conver_record")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceConverRecord extends BaseModel {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "user_id")
	private String userId;
	@Column(name = "balance_record_id")
	private String balanceRecordId;
	@Column(name = "conver_amount")
	private BigDecimal converAmount;
	@Column(name = "status")
	private Integer status;
	@Column(name = "create_time")
	private Date createTime;
	@Column(name = "modify_time")
	private Date modifyTime;

}