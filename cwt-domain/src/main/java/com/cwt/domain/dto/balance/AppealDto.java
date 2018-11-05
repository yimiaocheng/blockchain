package com.cwt.domain.dto.balance;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Appeal 数据传输类
 * @date 2018-08-30 10:44:52
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppealDto extends BaseDto {
	private String id;
	private String userId;
	private String balanceBillId;
	private String appealFile;
	private String appealExplain;
	private String status;
	private java.util.Date creatDatetime;

}