package com.cwt.domain.dto.information;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Information 数据传输类
 * @date 2018-08-15 14:13:46
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InformationForCtRulesDTO extends BaseDto {
	private String transRate;//资产兑换算力杠杆
	private String transBalanceRate;//资产兑换算力比例
	private String ctToIntegralRate;//资产兑换商城积分比例
	private String ctToGameCoinRate;//资产兑换游戏币比例
}