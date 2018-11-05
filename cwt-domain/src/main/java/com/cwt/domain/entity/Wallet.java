package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Wallet 数据传输类
 * @date 2018-08-09 17:22:37
 * @version 1.0
 */
@Table(name = "app_wallet")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet extends BaseModel {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "user_id")
	private String userId;
	@Column(name = "flow_balance")
	private BigDecimal flowBalance;
	@Column(name = "calculation_force")
	private BigDecimal calculationForce;
	@Column(name = "game_cion")
	private BigDecimal gameCion;
	@Column(name = "integral")
	private BigDecimal integral;
	@Column(name = "version")
	private Long version;
	@Column(name = "create_time")
	private java.util.Date createTime;
	@Column(name = "modify_time")
	private java.util.Date modifyTime;

}