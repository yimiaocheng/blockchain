package com.cwt.domain.dto.wallet;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Wallet 数据传输类
 * @date 2018-08-09 17:22:37
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletDto extends BaseDto {
	private String id;
	private String userId;
	private BigDecimal flowBalance;
	private BigDecimal calculationForce;
	private BigDecimal gameCion;
	private BigDecimal integral;
	private Long version;
	private Date createTime;
	private Date modifyTime;

}