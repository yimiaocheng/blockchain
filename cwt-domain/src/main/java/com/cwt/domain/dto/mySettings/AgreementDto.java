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
public class AgreementDto extends BaseDto {
	private String id;
	private String textContent;
	private Date createTime;
	private Date modifyTime;

}