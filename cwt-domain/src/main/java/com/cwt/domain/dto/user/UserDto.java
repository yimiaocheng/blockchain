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
public class UserDto extends BaseDto {
	private String id;
	private String userName;
	private String password;
	private String nickName;
	private String headImg;
	private String inviterId;
	private String status;
	private String paymentPassword;
	private String paymentMethodBankcard;
	private String paymentMethodZfb;
	private String paymentMethodWx;
	private Integer invitationCode;
	private java.util.Date createTime;
	private java.util.Date modifyTime;

}