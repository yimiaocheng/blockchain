package com.cwt.service.service;


import com.cwt.domain.dto.balance.BalanceConverRecordDto;

public interface BalanceConverRecordService {

    //保存余额兑换记录
    void saveConverBill(BalanceConverRecordDto balanceConverRecordDto);
}
