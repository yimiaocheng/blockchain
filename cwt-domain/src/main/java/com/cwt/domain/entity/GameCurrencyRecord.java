package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * GameCurrencyRecord 数据传输类
 * @date 2018-10-16 09:44:49
 * @version 1.0
 */
@Table(name = "app_game_currency_record")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameCurrencyRecord extends BaseModel {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "user_id")
	private String userId;
	@Column(name = "total_num")
	private BigDecimal totalNum;
	@Column(name = "change_type")
	private Integer changeType;
	@Column(name = "arithmetic_type")
	private Integer arithmeticType;
	@Column(name = "create_time")
	private java.util.Date createTime;
	@Column(name = "modify_time")
	private java.util.Date modifyTime;

}