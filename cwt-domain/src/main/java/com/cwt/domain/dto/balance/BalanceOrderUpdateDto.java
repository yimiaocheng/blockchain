package com.cwt.domain.dto.balance;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * BalanceOrderUpdateInfoDto 用户修改数据传输类
 * @date 2018-08-22 17:28:41
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceOrderUpdateDto extends BaseDto {
	private String id;	// 订单ID
	private BigDecimal orderTotalNum;	// 交易总额
	private BigDecimal orderNum; // 交已剩余额度
	private BigDecimal limitNumMin; // 交易下限
	private BigDecimal limitNumMax; // 交易上限
	private Integer payType;	// 支付方式
	private BigDecimal balanceConvertPercent; //余额 : 现金
}