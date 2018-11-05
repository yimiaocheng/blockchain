package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Appeal 数据传输类
 * @date 2018-08-30 10:44:52
 * @version 1.0
 */
@Table(name = "app_appeal")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appeal extends BaseModel {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "user_id")
	private String userId;
	@Column(name = "balance_bill_id")
	private String balanceBillId;
	@Column(name = "appeal_file")
	private String appealFile;
	@Column(name = "appeal_explain")
	private String appealExplain;
	@Column(name = "status")
	private Integer status;
	@Column(name = "creat_datetime")
	private Date creatDatetime;
	@Column(name = "modify_time")
	private Date modifyTime;


}