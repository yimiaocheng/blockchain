package com.cwt.domain.dto.integral;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * IntegralRecord 数据传输类
 * @date 2018-10-16 09:44:49
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntegralDTO extends BaseDto {
	private String id;
	private String userId;
	private String userMobile;
	private BigDecimal totalNum;
	private Integer changeType;
	private Integer arithmeticType;
	private java.util.Date createTime;
	private java.util.Date modifyTime;

}