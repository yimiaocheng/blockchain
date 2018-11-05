package com.cwt.domain.dto.appeal;

import lombok.Data;

import java.util.Date;

/**
 * 查询申诉条件
 */
@Data
public class AppealListConditionDto {
    private String userName;//申诉人手机号
    private Integer status;//状态 新建(0)，同意(1)，驳回(2)，作废(3)
    private Date beginTime;//开始时间
    private Date endTime;//结束时间
}
