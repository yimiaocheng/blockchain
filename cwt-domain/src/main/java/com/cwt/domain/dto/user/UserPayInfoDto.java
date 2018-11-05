package com.cwt.domain.dto.user;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User 数据传输类
 * @date 2018-08-09 17:21:35
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPayInfoDto extends BaseDto {
	private String paymentMethodBankcard;
	private String paymentMethodZfb;
	private String paymentMethodWx;
	private String userName;
	private String bankLocation;
	private String bankName;
}