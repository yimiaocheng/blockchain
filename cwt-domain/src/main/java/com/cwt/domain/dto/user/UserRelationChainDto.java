package com.cwt.domain.dto.user;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserRelationChain 数据传输类
 * @date 2018-08-25 14:47:39
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRelationChainDto extends BaseDto {
	private String id;
	private String userId;
	private String pid;
	private String relationChain;
	private Integer treeDepth;
	private java.util.Date createTime;

}