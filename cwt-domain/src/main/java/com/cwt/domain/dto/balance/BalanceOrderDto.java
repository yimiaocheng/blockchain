package com.cwt.domain.dto.balance;

import com.cwt.domain.dto.BaseDto;
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
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceOrderDto extends BaseDto {
	private String id;
	private String userId;
	private BigDecimal balanceConvertPercent;
	private BigDecimal serverCharge;
	private BigDecimal subsidy;
	private BigDecimal orderTotalNum;
	private BigDecimal orderNum;
	private BigDecimal limitNumMin;
	private BigDecimal limitNumMax;
	private Integer payType;
	private Integer orderType;
	private Integer orderStatus;
	private java.util.Date createTime;
	private java.util.Date modifyTime;
}