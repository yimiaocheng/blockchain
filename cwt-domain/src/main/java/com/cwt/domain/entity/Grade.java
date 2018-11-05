package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Grade 数据传输类
 * @date 2018-08-10 13:40:28
 * @version 1.0
 */
@Table(name = "app_grade")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Grade extends BaseModel {
	@Id
	@Column(name = "id")
	private int id;
	@Column(name = "level")
	private String level;
	@Column(name = "level_name")
	private String levelName;
	@Column(name = "min_upgrade")
	private Long minUpgrade;
	@Column(name = "max_upgrade")
	private Long maxUpgrade;
	@Column(name = "auto_transfer")
	private BigDecimal autoTransfer;
	@Column(name = "level_award")
	private BigDecimal levelAward;
	@Column(name = "generation")
	private Integer generation;
	@Column(name = "shop_space")
	private Integer shopSpace;

}