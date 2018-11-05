package com.cwt.domain.dto.user;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * UserBank 数据传输类
 * @date 2018-08-22 10:10:40
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBankDto extends BaseDto {
	private String id;
	private String userId;
	private String userName;
	private String bankLocation;
	private String bankName;
	private String branchName;
	private String bankNumber;
	private Date createTime;
	private Date modifyTime;

}