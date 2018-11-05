package com.cwt.service.service.impl;

import com.cwt.domain.dto.balance.BalanceConverRecordDto;
import com.cwt.domain.entity.BalanceConverRecord;
import com.cwt.persistent.mapper.BalanceConverRecordMapper;
import com.cwt.service.service.BalanceConverRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("balanceConverRecordService")
public class BalanceConverRecordServiceImpl implements BalanceConverRecordService {

    @Autowired
    private BalanceConverRecordMapper balanceConverRecordMapper;

    //保存余额兑换记录
    @Override
    public void saveConverBill(BalanceConverRecordDto balanceConverRecordDto) {
        BalanceConverRecord balanceConverRecord = new BalanceConverRecord();
        BeanUtils.copyProperties(balanceConverRecordDto,balanceConverRecord);
        balanceConverRecordMapper.insert(balanceConverRecord);
    }
}
