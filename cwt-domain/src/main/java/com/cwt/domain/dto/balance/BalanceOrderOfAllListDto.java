package com.cwt.domain.dto.balance;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author huangxl
 * 用于后台显示所有用户交易总订单记录
 */
@Data
public class BalanceOrderOfAllListDto {
    private String id;//订单id
    private String userName;//用户手机号
    private BigDecimal orderTotalNum;//订单总数
    private BigDecimal orderNum;//订单剩余数量
    private BigDecimal limitNumMin;//最小成交限制
    private BigDecimal limitNumMax;//最大成交限制
    private BigDecimal serverCharge;//平台收取的手续费
    private BigDecimal subsidy;//给买家的补贴
    private Integer payType;//交易方式
    private String paymentMethodWx;//微信账号
    private String paymentMethodZfb;//支付宝账号
    private String paymentMethodBankcard;//银行卡账号
    private Integer orderType;//订单类型
    private Integer orderStatus;//订单状态
    private Date createTime;//创建时间
}
