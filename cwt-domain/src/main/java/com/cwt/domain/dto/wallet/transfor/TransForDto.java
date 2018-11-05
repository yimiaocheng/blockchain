package com.cwt.domain.dto.wallet.transfor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


/***
 * 转换算力更新数据Dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransForDto {
    private String id;//钱包id
    private BigDecimal variationBalance;//变动的余额
    private BigDecimal variationForce;//变动的算力
    private BigDecimal variationIntegral;//变动的积分
    private BigDecimal variationGameCoin;//变动的游戏币
}
