package com.cwt.domain.dto.user;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * User 数据传输类
 * @date 2018-08-09 17:21:35
 * @version 1.0
 */
@Data
public class UserGradeWalletDto extends BaseDto {
	private String uId;//用户id
	private String inviterId;//上级id
	private String userName;//用户账号
	private String wId;//钱包id
	private BigDecimal calculationForce;//算力
	private BigDecimal flowBalance;//余额
	private Long version;//钱包版本
	private BigDecimal levelAward;//用户等级奖励比率
}