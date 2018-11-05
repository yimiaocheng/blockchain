package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * UserBank 数据传输类
 * @date 2018-08-22 10:10:40
 * @version 1.0
 */
@Table(name = "app_user_bank")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBank extends BaseModel {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "user_id")
	private String userId;
	@Column(name = "user_name")
	private String userName;
	@Column(name = "bank_location")
	private String bankLocation;
	@Column(name = "bank_name")
	private String bankName;
	@Column(name = "branch_name")
	private String branchName;
	@Column(name = "bank_number")
	private String bankNumber;
	@Column(name = "create_time")
	private java.util.Date createTime;
	@Column(name = "modify_time")
	private java.util.Date modifyTime;

}