package com.cwt.domain.dto.balance;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * BalanceConverRecord 数据传输类
 * @date 2018-08-23 16:07:18
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceConverRecordDto extends BaseDto {
	private String id;
	private String userId;
	private String balanceRecordId;
	private BigDecimal converAmount;
	private Integer status;
	private Date createTime;
	private Date modifyTime;

}