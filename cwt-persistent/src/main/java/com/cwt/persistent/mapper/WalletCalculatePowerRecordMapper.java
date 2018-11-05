package com.cwt.persistent.mapper;

import com.cwt.domain.dto.calculate.CalculatePowerListConditionDto;
import com.cwt.domain.dto.calculate.CalculatePowerListDto;
import com.cwt.domain.dto.wallet.CalculatePowerRecordDto;
import com.cwt.domain.dto.wallet.GetBillDto;
import com.cwt.domain.entity.WalletCalculatePowerRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * WalletCalculatePowerRecordMapper 数据访问类
 * @date 2018-08-23 14:41:59
 * @version 1.0
 */
@Repository
public interface WalletCalculatePowerRecordMapper extends Mapper<WalletCalculatePowerRecord> {

    /***
     * 保存加权奖记录
     */
    void saveWeightingAwardBill(@Param("award") BigDecimal award);

    /**
     * 查询当天所有交易记录
     * @return
     */
    BigDecimal selectToday();

    /***
     * 查询用户推荐奖记录
     * @param userId
     * @return
     */
    List<GetBillDto> selectRecommendAwardBill(@Param("userId") String userId);

    /***
     * 查询当前用户所有算力记录
     * @param userId
     * @return
     */
    List<CalculatePowerRecordDto> listAll(@Param("userId") String userId);

    /***************************************
     *           huangxl
     * ********************************
     */
    //智能释放扣减算力
    void insertAutoFreeBalanceRecord();

    /**
     * #################huangxl#############
     * *************后台系统接口***********
     */
    /**
     * 后台查询所有算力记录
     * @param condition
     * @return
     */
    List<CalculatePowerListDto> listByCondition(CalculatePowerListConditionDto condition);


}