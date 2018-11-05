package com.cwt.domain.dto.grade;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Grade 数据传输类
 * @date 2018-08-10 13:40:28
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeDto extends BaseDto {
	private int id;
	private String level;
	private String levelName;
	private Long minUpgrade;
	private Long maxUpgrade;
	private BigDecimal autoTransfer;
	private BigDecimal levelAward;
	private int generation;
	private int shopSpace;
}