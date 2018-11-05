package com.cwt.domain.dto.wallet;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class CenterAccountDto extends BaseDto {

	private BigDecimal autoTransferCount;
	private BigDecimal backendRecharge;
	private BigDecimal centerAccountTotal;
	private Date dateTime;
	private BigDecimal cpTrans;

}