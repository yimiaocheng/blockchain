package com.cwt.domain.dto.information;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Information 数据传输类
 * @date 2018-08-15 14:13:46
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InformationDto extends BaseDto {
	private Integer id;
	private String dataName;
	private String dataValue;
	private Integer status;

}