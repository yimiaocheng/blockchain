package com.cwt.domain.dto.balance;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author huangxl
 * 用于后台展示余额明细记录
 */
@Data
public class BalanceRecordListDto {
    private String targetUserName;//变动人的手机号
    private String sourceUserName;//操作人的手机号
    private BigDecimal amount;//数额，正数或者是负数
    private BigDecimal serviceCharge;//手续费
    private BigDecimal subsidy;//补贴
    private String arithmeticType;
    private String changeType;//变化类型
    private Date createTime;//时间
}
