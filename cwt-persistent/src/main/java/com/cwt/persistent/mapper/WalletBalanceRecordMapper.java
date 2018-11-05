package com.cwt.persistent.mapper;

import com.cwt.domain.dto.balance.BalanceRecordListConditionDto;
import com.cwt.domain.dto.balance.BalanceRecordListDto;
import com.cwt.domain.dto.balance.CenterAccountConditionDto;
import com.cwt.domain.dto.wallet.*;
import com.cwt.domain.entity.WalletBalanceRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * WalletBalanceRecordMapper 数据访问类
 *
 * @version 1.0
 * @date 2018-08-23 14:41:59
 */
@Repository
public interface WalletBalanceRecordMapper extends Mapper<WalletBalanceRecord> {

    /***
     * 保存每天智能释放记录
     */
    void savaAutoUpdateBalanceBill();

    /***
     * 查询用户转账记录
     * @param userId 用户id
     * @param changeType  收款类型集合
     * @return
     */
    List<WalletBalanceTransferOutDto> selectTransferBill(@Param("userId") String userId, @Param("changeType") int... changeType);

    /***
     * 查询用户收款记录
     * @param userId
     * @return
     */
    List<WalletBalanceTransferInDto> selectCollectingBill(@Param("userId") String userId, @Param("changeType") int ...changeType);

    /***
     * 查询用户余额兑换记录
     * @param userId
     * @return
     */
    List<GetBillDto> selectTransBill(@Param("userId") String userId);

    /***
     * 查询用户智能释放记录
     * @param userId
     * @return
     */
    List<GetBillDto> selectAutoUpdateBalanceBill(@Param("userId") String userId);

    /***
     * 查询当前用户所有余额记录
     * @param userId
     * @return
     */
    List<BalanceRecordListAllDto> listAll(@Param("userId") String userId);

    /**
     * 查询今日指定类型的变动记录总数
     * @return
     */
    int countByChangeTypeOfToday(@Param("userId") String userId,@Param("changeType")Integer changeType);

    /**
     * #############huangxl#############
     * 后台管理模块
     */
    /**
     * 查询所有余额流水记录
     *
     * @param condition 筛选条件
     */
    List<BalanceRecordListDto> listByCondition(BalanceRecordListConditionDto condition);

    /**
     * 查询后台统计数据
     * @return
     */
    List<CenterAccountDto> centerAccountList(CenterAccountConditionDto centerAccountConditionDto);
    /**
     * 查询资产收支统计详细
     * @return
     */
    List<CenterAccountDto> centerAccountDetail(CenterAccountConditionDto centerAccountConditionDto);

    /**
     * 获取现有资产总额
     * @return
     */
    BigDecimal nowCTAccountTotal(CenterAccountConditionDto centerAccountConditionDto);
}