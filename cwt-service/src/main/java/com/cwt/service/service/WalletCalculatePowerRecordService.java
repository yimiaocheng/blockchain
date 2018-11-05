package com.cwt.service.service;

import com.cwt.domain.dto.calculate.CalculatePowerListConditionDto;
import com.cwt.domain.dto.calculate.CalculatePowerListDto;
import com.cwt.domain.dto.wallet.BalanceRecordListAllDto;
import com.cwt.domain.dto.wallet.CalculatePowerRecordDto;
import com.cwt.domain.dto.wallet.GetBillDto;
import com.cwt.domain.dto.wallet.WalletCalculatePowerRecordDto;

import java.util.List;

public interface WalletCalculatePowerRecordService {

    //保存算力变更记录
    void saveForce(WalletCalculatePowerRecordDto walletCalculatePowerRecordDto);

    /***
     * 查询用户推荐奖记录
     * @param userId
     * @return
     */
    List<GetBillDto> getRecommendAwardBill(String userId);

    /***
     * 查询当前用户所有算力记录
     * @param userId
     * @return
     */
    List<CalculatePowerRecordDto> listAll(String userId, Integer pageNum, Integer pageSize);

    /**
     * ################huangxl#########
     * ***************后台接口*************
     */

    /**
     *  后台查询所有算力记录
     * @param condition 查询条件
     * @return 算力记录列表
     */
    List<CalculatePowerListDto> listByCondition(CalculatePowerListConditionDto condition);
}
