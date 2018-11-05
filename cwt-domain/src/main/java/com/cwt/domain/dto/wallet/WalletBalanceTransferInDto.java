package com.cwt.domain.dto.wallet;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author huangxl
 * 余额转入封装类
 */
@Data
public class WalletBalanceTransferInDto {
    private String userName;//转出余额用户的手机号
    private BigDecimal serviceCharge;//平台手续费
    private BigDecimal calculatePower;//获取算力
    private BigDecimal amount;//数额
    private Integer changeType;//类型
    private Date createTime;//创建时间
}
