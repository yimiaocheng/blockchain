package com.cwt.domain.dto.user;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserTransactionInfo 数据传输类
 * @date 2018-08-24 14:18:17
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTransactionInfoDto extends BaseDto {
	private String id;
	private String userId;
	private Integer transationTotal;
	private Integer appealTotal;
	private Integer appealSuccess;
	private Integer appealFail;
	private java.util.Date createTime;
	private java.util.Date modifyTime;

}