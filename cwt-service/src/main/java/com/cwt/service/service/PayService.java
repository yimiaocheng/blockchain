package com.cwt.service.service;

import com.cwt.domain.dto.request.PrePayReq;
import com.cwt.domain.dto.request.UserAwardReq;
import com.cwt.domain.dto.store.*;
import com.cwt.domain.entity.UserPay;

import java.util.List;

public interface PayService {
    /**
     * 预支付
     * @param prePayReq
     * @return
     */
    UserPay prepay(PrePayReq prePayReq);

    /**
     * 支付
     * @param userPay
     * @param password
     */
    UserPay payment(UserPay userPay, String password);

    /**
     * 查询订单支付结果
     * @param orderNo
     */
    String findPayResultByOrderId(String orderNo);


    /**
     * 确认收款
     * @param orderId 订单id
     * @param fromId 用户id
     * @param toId 商户id
     * @param fee_ct
     * @param fee_integral
     */
    void receive(String orderId, String fromId,String toId,String fee_ct,String fee_integral);

    /**
     * 退款（多商户）
     * @param fromId
     * @param toId
     * @param orderId
     * @param fee_ct
     * @param fee_integral
     */
    void refund(String fromId, String toId, String orderId, String fee_ct,String fee_integral);

    /**
     * 退款（单商户）
     * @param orderNo
     * @param fromId
     * @param fee_ct
     * @param fee_integral
     */
    void refund(String orderNo, String fromId, String fee_ct, String fee_integral);

    /**
     * 奖金发放
     * @param userAwardReq
     */
    void award(UserAwardReq userAwardReq);

    /**
     * 将奖金发放信息储存到数据库表中 状态为 待发放
     * @param userAwardReq
     */
    void saveUserAward(UserAwardReq userAwardReq);

    /**
     * 查询用户支付信息记录
     * @param userPayInfoDTO
     * @return
     */
    List<UserPayInfoPO> selectUserPayInfoList(UserPayInfoDTO userPayInfoDTO);
    /**
     * 查询奖金发放记录
     * @param userAwardDTO
     * @return
     */
    List<UserAwardListPO> selectUserAwardList(UserAwardDTO userAwardDTO);

    /**
     * 查询用户确认收款记录
     * @param userReceiveDTO
     * @return
     */
    List<UserReceiveListPO> selectUserReceiveList(UserReceiveDTO userReceiveDTO);

    /**
     * 查询用户退款记录
     * @param userRefundDTO
     * @return
     */
    List<UserRefundListPO> selectUserRefundList(UserRefundDTO userRefundDTO);
}
