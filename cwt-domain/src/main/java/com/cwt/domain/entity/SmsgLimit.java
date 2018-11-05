package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * SmsgLimit 数据传输类
 * @date 2018-08-09 17:21:35
 * @version 1.0
 */
@Table(name = "app_smsg_limit")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsgLimit extends BaseModel {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "mobilephone")
	private String mobilephone;
	@Column(name = "smsg_count")
	private Integer smsgCount;
	@Column(name = "smsg_date")
	private Date smsgDate;

}