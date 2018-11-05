package com.cwt.domain.dto.calculate;

import lombok.Data;

import java.util.Date;

/**
 * @author huangxl
 * 用于封装展示算力记录的查询条件
 */
@Data
public class CalculatePowerListConditionDto {
    private String telephone;//手机号
    private String arithmeticType;//加减类型
    private Integer changeType;//变化类型
    private Date beginTime;//开始时间
    private Date endTime;//结束时间
}
