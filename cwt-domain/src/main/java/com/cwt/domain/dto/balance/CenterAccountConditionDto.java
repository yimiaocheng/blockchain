package com.cwt.domain.dto.balance;

import com.cwt.domain.constant.WalletBillConstants;
import lombok.Data;

import java.util.Date;

@Data
public class CenterAccountConditionDto {
    private Integer TRANS = WalletBillConstants.CHANGE_TYPE_TRANS;//变化类型
    private Integer AUTO_TRANSFER = WalletBillConstants.CHANGE_TYPE_AUTO_TRANSFER;//变化类型
    private Integer UPDATE_BACKEND = WalletBillConstants.CHANGE_TYPE_UPDATE_BACKEND;//变化类型

    private Date beginTime;//开始时间
    private Date endTime;//结束时间
}
