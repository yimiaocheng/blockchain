package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * UserRelationChain 数据传输类
 * @date 2018-08-25 14:47:39
 * @version 1.0
 */
@Table(name = "app_user_relation_chain")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRelationChain extends BaseModel {
	@Column(name = "id")
	private String id;
	@Column(name = "user_id")
	private String userId;
	@Column(name = "pid")
	private String pid;
	@Column(name = "relation_chain")
	private String relationChain;
	@Column(name = "tree_depth")
	private Integer treeDepth;
	@Column(name = "create_time")
	private java.util.Date createTime;

}