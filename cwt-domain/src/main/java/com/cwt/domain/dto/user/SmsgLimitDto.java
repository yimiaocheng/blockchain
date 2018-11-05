package com.cwt.domain.dto.user;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * SmsgLimit 数据传输类
 * @date 2018-08-09 17:21:35
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsgLimitDto extends BaseDto {
	private String id;
	private String mobilephone;
	private Integer smsgCount;
	private Date smsgDate;

}