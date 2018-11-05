package com.cwt.domain.dto.mySettings;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * AboutUsDto 数据传输类
 * @date 2018-08-27 14:24:47
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AboutUsDto extends BaseDto {
	private String id;
	private String textContent;
	private java.util.Date createTime;
	private java.util.Date modifyTime;
	private Date beginTime;//开始时间
	private Date endTime;//结束时间

}