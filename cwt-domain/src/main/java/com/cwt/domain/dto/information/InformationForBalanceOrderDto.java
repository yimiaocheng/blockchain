package com.cwt.domain.dto.information;

import lombok.Data;

/**
 * 用于返回余额交易平台手续费和买家补贴比例
 * @author huangxl
 * Created on 2018/9/8
 */
@Data
public class InformationForBalanceOrderDto {
    private String serviceCharge = "0";//平台手续费
    private String subsidy = "0";//买家补贴
    private String orderMinNum = "1";//资产交易最低限额
}
