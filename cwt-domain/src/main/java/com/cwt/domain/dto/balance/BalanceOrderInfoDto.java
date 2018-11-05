package com.cwt.domain.dto.balance;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * BalanceOrderInfoDto 确认信息数据传输类
 *
 * @version 1.0
 * @date 2018-08-22 17:28:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceOrderInfoDto extends BaseDto {
    private String id;
    private BigDecimal balanceConvertPercent;
    private BigDecimal serverCharge;
    private BigDecimal subsidy;
    private BigDecimal orderTotalNum;
    private BigDecimal orderNum;
    private BigDecimal limitNumMin;
    private BigDecimal limitNumMax;
    private Integer payType;

    private BigDecimal serverChargeFee; // 平台手续费
    private BigDecimal subsidyFee; // 买、卖家手续费
    private BigDecimal realCash; //实际收入支出的现金
    private BigDecimal flowBalanceCash;//实际增加，减少的流动余额
}