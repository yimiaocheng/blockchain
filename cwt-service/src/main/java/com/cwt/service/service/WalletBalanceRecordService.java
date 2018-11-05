package com.cwt.service.service;


import com.cwt.domain.dto.balance.BalanceRecordListConditionDto;
import com.cwt.domain.dto.balance.BalanceRecordListDto;
import com.cwt.domain.dto.balance.CenterAccountConditionDto;
import com.cwt.domain.dto.wallet.*;

import java.math.BigDecimal;
import java.util.List;

public interface WalletBalanceRecordService {
    //保存余额变更记录
    void saveBalance(WalletBalanceRecordDto walletBalanceRecordDto);

    /***
     * 查询用户转账记录
     * @param userId
     * @param pageNum 起始行号
     * @param pageSize 总行数
     * @return
     */
    List<WalletBalanceTransferOutDto> getTransferBill(String userId,Integer pageNum, Integer pageSize);

    /***
     * 查询用户收款记录
     * @param userId
     * @param pageNum 起始行号
     * @param pageSize 总行数
     * @return
     */
    List<WalletBalanceTransferInDto> getCollectingBill(String userId,Integer pageNum, Integer pageSize);

    /***
     * 查询用户余额兑换记录
     * @param userId
     * @return
     */
    List<GetBillDto> getTransBill(String userId, Integer pageNum, Integer pageSize);

    /***
     * 查询用户余额释放记录
     * @param userId
     * @return
     */
    List<GetBillDto> getAutoUpdateBalanceBill(String userId);

    /***
     * 查询当前用户所有余额记录
     * @param userId
     * @return
     */
    List<BalanceRecordListAllDto> listAll(String userId,Integer pageNum, Integer pageSize);

    /**
     * 判断用户是否有资格获得奖励和当天是否有获得过奖励
     * @param userId 用户id
     * @return
     */
    boolean countByFreeBalanceRecordOfToday(String userId);

    /**
     * #############huangxl#############
     * 后台管理模块
     */
    /**
     * 查询所有余额流水记录
     * @param condition 筛选条件
     */
    List<BalanceRecordListDto> listByCondition(BalanceRecordListConditionDto condition);

    /**
     * 查询后台统计数据
     * @return
     */
    List<CenterAccountDto> centerAccountCount();

    /**
     * 查询资产收支统计详细
     * @return
     */
    List<CenterAccountDto> centerAccountDetail(CenterAccountConditionDto centerAccountConditionDto);

    /**
     * 获取剩余发行总资产
     * @return
     */
    BigDecimal getSurplusCTAccount();
}
