package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * WalletBill 数据传输类
 * @date 2018-07-21 11:10:57
 * @version 1.0
 */
@Table(name = "app_wallet_bill")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Deprecated
public class WalletBill extends BaseModel {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "user_id")
	private String userId;
	@Column(name = "wallet_id")
	private String walletId;
	@Column(name = "be_user_id")
	private String beUserId;
	@Column(name = "be_wallet_id")
	private String beWalletId;
	@Column(name = "amount")
	private Double amount;
	@Column(name = "bill_type")
	private String billType;
	@Column(name = "create_time")
	private java.util.Date createTime;

}