package com.cwt.service.service;


import com.cwt.domain.dto.balance.*;
import com.cwt.domain.entity.BalanceOrderBill;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface BalanceOrderBillService {
    /***
     * 根据交易类型、交易状态分页查询交易信息
     * @param userId
     * @param orderType
     * @param billStatus
     * @return
     */
    List<GetBalanceOrderBillByConditionDto> listByOrderTypeAndBillStatus(String userId, Integer orderType, Integer billStatus, Integer pageNum, Integer pageSize);

    /***
     * 下单确认操作
     * @param id 订单id
     * @param orderUserId 发起订单用户id
     * @param optUserId 操作订单用户id
     * @param moneyAmount 金额数量
     * @param amount 余额数量
     */
    BalanceOrderBill headerOrderConfirmation(String id, String orderUserId, String optUserId, BigDecimal moneyAmount, BigDecimal amount);

    /***
     * 卖家点击下一步，将用户状态和订单状态设置为进行中
     * 已不需要该功能
     * @param id
     * @param userId
     */
    @Deprecated
    void updateOrderBillStatus(String id,String userId);

    /***
     * 买单确认结算
     * @param id
     * @param passWord
     * @param userId
     */
    void headerBuyAffirm(String id,String passWord,String userId);

    /***
     * 卖单确认结算
     * @param id
     * @param passWord
     * @param userId
     */
    void headerSellAffirm(String id,String passWord,String userId);

    /***
     * 放弃买单
     * @param id
     */
    void headerAbandonBuy(String id);

    /***
     * 放弃卖单
     * @param id
     */
    void headerAbandonSell(String id);

    /***
     * 订单自动失效
     * @param orderBillId
     */
    void headerAutoFail(String orderBillId);

    /***
     * 申诉
     * @param id
     * @param userId
     * @param appealFileName
     */
    void headerAppeal(String id, String userId,String appealFileName,String appealText);

    /***
     * 同意申诉
     * @param appealId
     */
    void headerAgreed2Appeal(String appealId);

    /***
     * 驳回申诉
     * @param appealId
     */
    void headerReject2Appeal(String appealId);

    /***
     * 撤销申诉
     * @param appealId
     */
    void headerRepeal2Appeal(String appealId);

    /**
     * ###########huangxl########
     * *********后台系统接口***********
     */

    /**
     * 查询所有的交易明细列表
     * @param condition 查询条件
     */
    List<BalanceOrderBillOfAllListDto> listForAll(BalanceOrderBillOfAllConditionDto condition);




}
