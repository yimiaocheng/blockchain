package com.cwt.service.service.impl;

import com.cwt.domain.dto.calculate.CalculatePowerListConditionDto;
import com.cwt.domain.dto.calculate.CalculatePowerListDto;
import com.cwt.domain.dto.wallet.CalculatePowerRecordDto;
import com.cwt.domain.dto.wallet.GetBillDto;
import com.cwt.domain.dto.wallet.WalletCalculatePowerRecordDto;
import com.cwt.domain.entity.WalletCalculatePowerRecord;
import com.cwt.persistent.mapper.WalletCalculatePowerRecordMapper;
import com.cwt.service.service.WalletCalculatePowerRecordService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("walletCalculatePowerRecordService")
public class WalletCalculatePowerRecordServiceImpl implements WalletCalculatePowerRecordService {

    @Autowired
    private WalletCalculatePowerRecordMapper walletCalculatePowerRecordMapper;

    //保存算力变更记录
    @Override
    public void saveForce(WalletCalculatePowerRecordDto walletCalculatePowerRecordDto) {
        WalletCalculatePowerRecord walletCalculatePowerRecord = new WalletCalculatePowerRecord();
        BeanUtils.copyProperties(walletCalculatePowerRecordDto, walletCalculatePowerRecord);
        walletCalculatePowerRecordMapper.insert(walletCalculatePowerRecord);
    }

    @Override
    public List<GetBillDto> getRecommendAwardBill(String userId) {
        return walletCalculatePowerRecordMapper.selectRecommendAwardBill(userId);
    }

    /***
     * 查询当前用户所有算力记录
     * @param userId
     * @return
     */
    @Override
    public List<CalculatePowerRecordDto> listAll(String userId, Integer pageNum, Integer pageSize) {
        if (pageSize == null || pageSize == 0) {
            //默认10条
            pageSize = 10;
        }
        //开始分页
        PageHelper.startPage(pageNum,pageSize);
        return walletCalculatePowerRecordMapper.listAll(userId);
    }

    /**
     * ################huangxl####################
     * ****************后台系统接口*************
     */
    @Override
    public List<CalculatePowerListDto> listByCondition(CalculatePowerListConditionDto condition) {
        return walletCalculatePowerRecordMapper.listByCondition(condition);
    }
}
