package com.cwt.domain.dto.wallet;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class WalletBillDto extends BaseDto {
	private String id;
	private String userId;
	private String walletId;
	private String beUserId;
	private String beWalletId;
	private Double amount;
	private String billType;
	private java.util.Date createTime;

}