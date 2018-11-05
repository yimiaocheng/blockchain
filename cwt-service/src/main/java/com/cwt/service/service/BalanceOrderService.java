package com.cwt.service.service;


import com.cwt.domain.dto.balance.*;

import java.math.BigDecimal;
import java.util.List;

public interface BalanceOrderService {

    /***
     * 2018.8.29 09:07
     * lzf
     *
     *
     *
     *
     */

    /***
     * 根据筛选条件查询订单
     * @param userId
     * @param orderType 买/卖单
     * @param payType 支付方式
     * @param orderNum 交易数量
     * @param pageNum 起始行号
     * @param pageSize 总行数
     * @return
     */
    List<GetBalanceOrderByConditionDto> listByCondition(String userId, Integer orderType, Integer payType, BigDecimal orderNum, Integer pageNum, Integer pageSize,String orderByType);

    /***
     * 根据订单id查询订单
     * @param id
     * @return
     */
    BalanceOrderDto getById(String id);

    /***
     * 强制撤销总单
     * @param orderId
     * @param userId
     */
    void cancelOrder(String orderId,String userId);

    /***
     * 2018.8.29 09:07
     * lzf
     *
     *
     *
     *
     */

    /**
     * 返回余额交易买单信息
     *
     * @param orderTotalNum 交易总额
     * @param limitNumMin   交易最小限制
     * @param limitNumMax   交易上限
     * @param payType       支付方式
     * @param balanceConvertPercent     汇率
     * @return
     */
    BalanceOrderInfoDto headerBalanceOrderBuyConfirm(BigDecimal orderTotalNum, BigDecimal limitNumMin, BigDecimal limitNumMax, Integer payType, BigDecimal balanceConvertPercent);

    /**
     * 返回余额交易卖单信息
     *
     * @param orderTotalNum 交易总额
     * @param limitNumMin   交易最小限制
     * @param limitNumMax   交易上限
     * @param payType       支付方式
     * @param balanceConvertPercent     汇率
     * @return
     */
    BalanceOrderInfoDto headerBalanceOrderSellConfirm(BigDecimal orderTotalNum, BigDecimal limitNumMin, BigDecimal limitNumMax, Integer payType, BigDecimal balanceConvertPercent);

    /**
     * 创建一个余额交易的买单
     *
     * @param userId              用户id
     * @param balanceOrderInfoDto 买单信息
     * @return
     */
    boolean insertBalanceOrderBuy(String userId, BalanceOrderInfoDto balanceOrderInfoDto);

    /**
     * 创建一个余额交易的卖单
     *
     * @param userId              用户id
     * @param telephone           手机号
     * @param balanceOrderInfoDto 卖单信息
     * @return
     */
    boolean insertBalanceOrderSell(String userId, String telephone, BalanceOrderInfoDto balanceOrderInfoDto);

    /**
     * 根据 ID 获取交易订单的基本信息
     *
     * @param id 交易订单ID
     * @return
     */
    BalanceOrderUpdateDto selectBalanceOrderUpdateById(String id);

    /**
     * 返回余额交易买单修改后的信息
     *
     * @param balanceOrderUpdateDto 买单的基本信息
     * @return
     */
    BalanceOrderInfoDto headerBalanceOrderBuyUpdateConfirm(BalanceOrderUpdateDto balanceOrderUpdateDto);

    /**
     * 返回余额交易卖单修改后的信息
     *
     * @param balanceOrderUpdateDto 卖单的基本信息
     * @return
     */
    BalanceOrderInfoDto headerBalanceOrderSellUpdateConfirm(BalanceOrderUpdateDto balanceOrderUpdateDto);

    /**
     * 修改一个余额交易的买单
     *
     * @param balanceOrderInfoDto 买单信息
     * @return
     */
    boolean updateBalanceOrderBuy(BalanceOrderInfoDto balanceOrderInfoDto);

    /**
     * 修改一个余额交易的卖单
     *
     * @param userId              用户id
     * @param telephone           手机号
     * @param balanceOrderInfoDto 卖单信息
     * @return
     */
    boolean updateBalanceOrderSell(String userId, String telephone, BalanceOrderInfoDto balanceOrderInfoDto);


    /**
     * ####################huangxl###############
     * **************后台系统接口*************
     */
    /**
     * 后台查询所有总订单信息列表
     *
     * @param condition 查询条件封装类
     * @return 列表
     */
    List<BalanceOrderOfAllListDto> listForAll(BalanceOrderOfAllConditionDto condition);

    /**
     * YEHAO
     *
     *
     *
     *
     *
     */
    /**
     * 查询用户的发布记录
     *
     * @param userId   用户Id
     * @param pageNum  起始行号
     * @param pageSize 总行数
     * @return
     */
    List<GetBalanceOrderByConditionDto> listByReleaseRecords(String userId, Integer pageNum, Integer pageSize);
    /**
     *
     *
     * YEHAO
     *
     *
     *
     *
     */
    /**
     * 撤单操作
     *
     * @param id 买单ID
     * @return
     */
    boolean updateBalanceBuyOrderInvalid(String id);

    /**
     * @param id        订单id
     * @param telephone 当前用户手机号
     * @param userId    当前用户ID
     * @return
     */
    boolean updateBalanceSellOrderInvalid(String id, String userId, String telephone);

}
