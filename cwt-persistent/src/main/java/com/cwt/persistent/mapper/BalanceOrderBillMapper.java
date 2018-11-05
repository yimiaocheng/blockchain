package com.cwt.persistent.mapper;

import com.cwt.domain.dto.balance.*;
import com.cwt.domain.entity.BalanceOrderBill;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * BalanceOrderBillMapper 数据访问类
 * @date 2018-08-22 17:28:41
 * @version 1.0
 */
@Repository
public interface BalanceOrderBillMapper extends Mapper<BalanceOrderBill> {

    /**
     * 根据用户id、交易类型、交易状态查询交易信息
     * @param orderType
     * @param billStatus
     * @param userId
     * @return
     */
    List<GetBalanceOrderBillByConditionDto> listByOrderTypeAndBillStatus(@Param("userId") String userId, @Param("orderType") Integer orderType, @Param("billStatus") Integer billStatus);

    /***
     * 根据发布订单id查询多交易订单
     * @param orderId
     * @return
     */
    List<BalanceOrderBillDto> listByOrderId(@Param("orderId") String orderId);

    /***
     * 根据总单id查询交易订单不等于那些状态中
     * 返回记录总数
     * @param orderId
     * @return
     */
    int countNoInStatusByOrderId(@Param("orderId") String orderId,@Param("status") int ...status);

    /**
     * ###############huangxl###############
     * 后台系统接口
     */
    List<BalanceOrderBillOfAllListDto> listForAll(BalanceOrderBillOfAllConditionDto condition);
}