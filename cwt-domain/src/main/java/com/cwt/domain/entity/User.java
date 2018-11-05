package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User 数据传输类
 * @date 2018-08-09 17:21:35
 * @version 1.0
 */
@Table(name = "app_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseModel {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "user_name")
	private String userName;
	@Column(name = "password")
	private String password;
	@Column(name = "nick_name")
	private String nickName;
	@Column(name = "head_img")
	private String headImg;
	@Column(name = "inviter_id")
	private String inviterId;
	@Column(name = "payment_password")
	private String paymentPassword;
	@Column(name = "payment_method_bankcard")
	private String paymentMethodBankcard;
	@Column(name = "payment_method_zfb")
	private String paymentMethodZfb;
	@Column(name = "payment_method_wx")
	private String paymentMethodWx;
	@Column(name = "invitation_code")
	private Integer invitationCode;
	@Column(name = "status")
	private String status;
	@Column(name = "create_time")
	private java.util.Date createTime;
	@Column(name = "modify_time")
	private java.util.Date modifyTime;
}