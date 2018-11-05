package com.cwt.domain.dto.wallet;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author huangxl
 * 余额转出封装类
 */
@Data
public class WalletBalanceTransferOutDto {
    private String userName;//转出的手机号
    private BigDecimal serviceCharge;//手续费
    private BigDecimal calculatePower;//算力
    private BigDecimal amount;//转出余额
    private Integer changeType;//类型
    private Date createTime;//创建时间
}
