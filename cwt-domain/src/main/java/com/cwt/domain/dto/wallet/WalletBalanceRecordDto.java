package com.cwt.domain.dto.wallet;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * WalletBalanceRecord 数据传输类
 * @date 2018-08-23 14:41:59
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletBalanceRecordDto extends BaseDto {
	private String id;
	private String targetUserId;
	private String optUserId;
	private String optUserShowMsg;
	private BigDecimal amount;
	private BigDecimal serviceCharge;
	private BigDecimal subsidy;
	private String eventId;
	private Integer arithmeticType;
	private Integer changeType;
	private java.util.Date createTime;

}