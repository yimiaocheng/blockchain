package com.cwt.domain.entity;

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
@Table(name = "app_information")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Information extends BaseModel {
	@Id
	@Column(name = "id")
	private Integer id;
	@Column(name = "grouping")
	private String grouping;
	@Column(name = "data_name")
	private String dataName;
	@Column(name = "data_value")
	private String dataValue;
	@Column(name = "status")
	private Integer status;
	@Column(name = "label_text")
	private String labelText;
	@Column(name = "create_time")
	private java.util.Date createTime;
	@Column(name = "modify_time")
	private java.util.Date modifyTime;

}