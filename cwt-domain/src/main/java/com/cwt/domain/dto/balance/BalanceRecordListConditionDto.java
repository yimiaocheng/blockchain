package com.cwt.domain.dto.balance;

import lombok.Data;

import java.util.Date;

/**
 * @author huangxl
 * 用于封装后台查询余额记录条件
 */
@Data
public class BalanceRecordListConditionDto {
    private String telephone;//手机号
    private String arithmeticType;//加减类型
    private Integer changeType;//变化类型
    private Integer[] typeLimit;//类型限制
    private Date beginTime;//开始时间
    private Date endTime;//结束时间
}
