package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * UserTransactionInfo 数据传输类
 * @date 2018-08-24 14:18:17
 * @version 1.0
 */
@Table(name = "app_user_transaction_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTransactionInfo extends BaseModel {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "user_id")
	private String userId;
	@Column(name = "transation_total")
	private Integer transationTotal;
	@Column(name = "appeal_total")
	private Integer appealTotal;
	@Column(name = "appeal_success")
	private Integer appealSuccess;
	@Column(name = "appeal_fail")
	private Integer appealFail;
	@Column(name = "create_time")
	private java.util.Date createTime;
	@Column(name = "modify_time")
	private java.util.Date modifyTime;

}