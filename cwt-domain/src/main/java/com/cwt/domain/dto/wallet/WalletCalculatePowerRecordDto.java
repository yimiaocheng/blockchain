package com.cwt.domain.dto.wallet;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * WalletCalculatePowerRecord 数据传输类
 * @date 2018-08-23 14:41:59
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletCalculatePowerRecordDto extends BaseDto {
	private String id;
	private String targetUserId;
	private String optUserId;
	private String optUserShowMsg;
	private BigDecimal amount;
	private String eventId;
	private Integer arithmeticType;
	private Integer changeType;
	private java.util.Date createTime;

}