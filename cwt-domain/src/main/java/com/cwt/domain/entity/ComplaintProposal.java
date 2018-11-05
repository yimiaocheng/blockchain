package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Map;

/**
 * ComplaintProposalDto 数据传输类
 * @date 2018-08-27 14:24:47
 * @version 1.0
 */
@Table(name = "app_complaint_proposal")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintProposal extends BaseModel {
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "user_id")
	private String userId;
	@Column(name = "content")
	private String content;
	@Column(name = "status")
	private String status;
	@Column(name = "create_time")
	private java.util.Date createTime;
	@Column(name = "modify_time")
	private java.util.Date modifyTime;

}