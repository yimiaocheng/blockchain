package com.cwt.domain.dto.calculate;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author huangxl
 * 用于后台展示算力记录
 */
@Data
public class CalculatePowerListDto {
    private String targetUserName;//变动人的手机号
    private String sourceUserName;//操作人的手机号
    private BigDecimal amount;//数额，正数或者是负数
    private Integer changeType;//变化类型
    private Integer arithmeticType;//增加或者减少
    private Date createTime;//时间
}
