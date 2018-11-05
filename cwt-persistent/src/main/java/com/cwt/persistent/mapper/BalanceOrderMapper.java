package com.cwt.persistent.mapper;

import com.cwt.domain.dto.balance.*;
import com.cwt.domain.entity.BalanceOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * BalanceOrderMapper 数据访问类
 *
 * @version 1.0
 * @date 2018-08-22 17:28:41
 */
@Repository
public interface BalanceOrderMapper extends Mapper<BalanceOrder> {

    /***
     * 根据筛选条件查询订单
     * @param userId
     * @param orderType 买/卖单
     * @param payType 支付方式
     * @param orderNum 交易数量
     * @return
     */
    List<GetBalanceOrderByConditionDto> listByCondition
    (@Param("userId") String userId,
     @Param("orderType") Integer orderType,
     @Param("payType") Integer payType,
     @Param("orderNum") BigDecimal orderNum,
     @Param("orderByType") String orderByType,
     @Param("status") int... status);

    /***
     * 查询用户的发布记录
     *
     * @param userId    用户ID
     * @param orderStatus    订单状态（不想展示的订单状态）
     * @return
     */
    List<GetBalanceOrderByConditionDto> listByReleaseRecords
    (@Param("userId") String userId,
     @Param("fieldOrderStatus") String fieldOrderStatus,
     @Param("orderStatus") int... orderStatus);

    /**
     * 根据ID查询订单的详细信息
     *
     * @param id 订单ID
     * @return BalanceOrderDto
     */
    BalanceOrderDto selectById(@Param("id") String id);

    /**
     * 根据ID查询订单的基本信息
     *
     * @param id 订单ID
     * @return BalanceOrderUpdateDto
     */
    BalanceOrderUpdateDto selectBalanceOrderUpdateById(@Param("id") String id);

    /**
     * ###################huangxl##############
     * 后台系统查看交易总订单列表信息
     */
    List<BalanceOrderOfAllListDto> listForAll(BalanceOrderOfAllConditionDto condition);
}





