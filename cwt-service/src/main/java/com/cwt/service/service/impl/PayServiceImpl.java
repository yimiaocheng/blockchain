package com.cwt.service.service.impl;

import com.cwt.common.enums.PayResultEnums;
import com.cwt.common.exception.PayException;
import com.cwt.common.util.MD5Utils;
import com.cwt.domain.constant.IntegralConstants;
import com.cwt.domain.constant.UserAwardConstants;
import com.cwt.domain.constant.UserPayConstants;
import com.cwt.domain.constant.WalletBillConstants;
import com.cwt.domain.dto.request.PrePayReq;
import com.cwt.domain.dto.request.UserAwardPO;
import com.cwt.domain.dto.request.UserAwardReq;
import com.cwt.domain.dto.store.*;
import com.cwt.domain.dto.user.UserDto;
import com.cwt.domain.dto.wallet.WalletBalanceRecordDto;
import com.cwt.domain.entity.*;
import com.cwt.persistent.mapper.*;
import com.cwt.service.service.PayService;
import com.cwt.service.service.UserService;
import com.cwt.service.service.WalletBalanceRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("payService")
public class PayServiceImpl implements PayService {

    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private UserPayMapper userPayMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserReceiveMapper userReceiveMapper;
    @Autowired
    private UserReFundMapper userReFundMapper;
    @Autowired
    private IntegralRecordMapper integralRecordMapper;
    @Autowired
    private WalletBalanceRecordService walletBalanceRecordService;
    @Autowired
    private WalletCalculatePowerRecordMapper walletCalculatePowerRecordMapper;
    @Autowired
    private UserAwardMapper userAwardMapper;
    @Autowired
    private UserService userService;
    @Override
    @Transactional
    public UserPay prepay(PrePayReq prePayReq) {
        if(prePayReq==null){
            throw new PayException(PayResultEnums.HTTP_PARAM_NULL);
        }
        //判断请求参数是否为空和签名是否正确
        checkPramAndSign(prePayReq);
        //查询用户余额信息
        UserBalance userBalance = walletMapper.findUserBalance(prePayReq.getFromId());
        //判断用户是否有余额
        checkBalanceAndIntegral(userBalance,prePayReq);

        //生成一条支付信息并保存到数据库表
        UserPay userPay = new UserPay();
        String prepayId = UUID.randomUUID().toString();

        userPay.setId(prepayId);
        userPay.setFromId(prePayReq.getFromId());
        userPay.setOrderNo(prePayReq.getOrder_no());
        userPay.setFee_ct(prePayReq.getFee_ct());
        userPay.setFee_integral(prePayReq.getFee_integral());
        userPay.setBody(prePayReq.getBody());
        userPay.setNotifyUrl(prePayReq.getNotify_url());
        userPay.setState(prePayReq.getState());
        userPay.setTimesiamp(prePayReq.getTimesiamp());
        userPay.setSign(prePayReq.getSign());
        userPay.setStatus(UserPayConstants.PAY_STATUS_UNPAID);//支付状态  1.待支付 2.已支付
        userPay.setCreateTime(new Date());
        userPay.setModifyTime(new Date());

        //将订单信息保存到数据库
        userPayMapper.insert(userPay);

        return userPay;
    }

    @Override
    @Transactional
    public UserPay payment(UserPay userPay, String password) {
        if(StringUtils.isBlank(password)){
            throw new PayException(PayResultEnums.PAYPASSWORD_NULL);//支付密码为空
        }
        //判断密码是否正确
        UserDto userDto = userMapper.selectById(userPay.getFromId());
        System.out.println(MD5Utils.MD5(password));
        if(!userDto.getPaymentPassword().equals(MD5Utils.MD5(password))){
            throw new PayException(PayResultEnums.PAYPASSWORD_ERROR);//支付密码错误
        }
        //再次判断用户余额是否充足
        //查询用户余额信息
        UserBalance userBalance = walletMapper.findUserBalance(userPay.getFromId());
        //判断用户是否有余额
        BigDecimal flowBalance = userBalance.getFlowBalance();//CT
        int a = flowBalance.compareTo(new BigDecimal(Double.parseDouble(userPay.getFee_ct())));
        /**
         * a = -1,表示flowBalance小于order_fee；
         a = 0,表示flowBalance等于order_fee；
         a = 1,表示flowBalance大于order_fee；
         */
        if(a==-1){
            throw new PayException(PayResultEnums.FLOWBALANCE_NOT_ENOUGH);//ct余额不足
        }
        BigDecimal integral = userBalance.getIntegral();//积分
        int m = integral.compareTo(new BigDecimal(Double.parseDouble(userPay.getFee_integral())));
        /**
         * a = -1,表示integral小于order_fee；
         a = 0,表示integral等于order_fee；
         a = 1,表示integral大于order_fee；
         */
        if(m==-1){
            throw new PayException(PayResultEnums.INTEGRAL_NOT_ENOUGH);//积分余额不足
        }
        //判断订单是否已经支付（防止订单重复支付）
        UserPay payInfo = userPayMapper.findUserPayInfo(userPay.getId());
        if(UserPayConstants.PAY_STATUS_PAID.equals(payInfo.getStatus())){//已支付状态
            throw new PayException(PayResultEnums.ORDER_PAYPAL);//订单重复支付
        }
        //用户扣款
        int i = 0 ;
        if(!"0".equals(userPay.getFee_ct())){
            //ct扣款
            i = walletMapper.paymentByCT(userPay.getFromId(),new BigDecimal(Double.parseDouble(userPay.getFee_ct())),new Date());
            //保存ct变更记录
            WalletBalanceRecordDto payeeBalanceBill = new WalletBalanceRecordDto();
            payeeBalanceBill.setId(UUID.randomUUID().toString());//记录id
            payeeBalanceBill.setTargetUserId(userPay.getFromId());//余额变更用户id
            payeeBalanceBill.setOptUserId(userPay.getFromId());//操作的用户id
            payeeBalanceBill.setOptUserShowMsg(userDto.getUserName());//操作用户name
            payeeBalanceBill.setAmount(new BigDecimal(Double.parseDouble(userPay.getFee_ct())));//变更的数量
            payeeBalanceBill.setServiceCharge(new BigDecimal("0.00"));
            payeeBalanceBill.setSubsidy(new BigDecimal("0.00"));
            payeeBalanceBill.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_SUB);//该记录的类型是：减少
            payeeBalanceBill.setChangeType(WalletBillConstants.PAY_CT_ONPAID_DELETE);//途径：用户支付时，用户减少ct
            payeeBalanceBill.setCreateTime(new Date());//设置创建时间

            walletBalanceRecordService.saveBalance(payeeBalanceBill);
        }
        if(!"0".equals(userPay.getFee_integral())){
            //积分扣款
            i = walletMapper.paymentByintegral(userPay.getFromId(),new BigDecimal(Double.parseDouble(userPay.getFee_integral())),new Date());
            //保存积分变更记录
            IntegralRecord integralRecord = new IntegralRecord();
            integralRecord.setUserId(userPay.getFromId());
            integralRecord.setId(UUID.randomUUID().toString());
            integralRecord.setTotalNum(new BigDecimal(Double.parseDouble(userPay.getFee_integral())));//扣掉的积分
            integralRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_SUB);//该记录的类型是：减少
            integralRecord.setChangeType(IntegralConstants.PAY_INTEGRAL_ONPAID_DELETE);//途径：用户支付时，用户减少积分
            integralRecord.setCreateTime(new Date());
            integralRecord.setModifyTime(new Date());
            //保存积分记录
            integralRecordMapper.insertSelective(integralRecord);
        }
        if(i<=0){
            throw new PayException(PayResultEnums.PAY_FAIL);//用户扣款失败
        }
        //修改支付状态 已支付
        userPay.setStatus(UserPayConstants.PAY_STATUS_PAID);
        userPay.setModifyTime(new Date());
        int n = userPayMapper.updateUserPayStatus(userPay.getId(),UserPayConstants.PAY_STATUS_PAID,new Date());
        if(n<=0){
            throw new PayException(PayResultEnums.UPDATE_PAY_STAYUS);//修改支付状态失败
        }
        return userPay;
    }

    @Override
    @Transactional
    public String findPayResultByOrderId(String orderNo) {
        if(StringUtils.isBlank(orderNo)){
            throw new PayException(PayResultEnums.ORDER_NO_NULL);//订单id不能为空
        }
        UserPay payInfo = userPayMapper.findPayResultByOrderId(orderNo);
        if(payInfo==null){
            throw new PayException(PayResultEnums.ORDER_NOT_EXISTENT);//订单不存在
        }
        return payInfo.getStatus();


    }

    @Override
    @Transactional
    public void receive(String orderId, String fromId,String toId,String fee_ct,String fee_integral) {
        if(StringUtils.isBlank(orderId)){
            throw new PayException(PayResultEnums.ORDER_NO_NULL);//订单id不能为空
        }
        if(StringUtils.isBlank(fromId)){
            throw new PayException(PayResultEnums.FROM_ID_NULL);//用户id不能为空
        }
        if(StringUtils.isBlank(toId)){
            throw new PayException(PayResultEnums.TO_ID_NULL);//商户id不能为空
        }
        if(StringUtils.isBlank(fee_ct)){
            throw new PayException(PayResultEnums.ORDER_FEE_NULL);
        }
        if(StringUtils.isBlank(fee_integral)){
            throw new PayException(PayResultEnums.ORDER_FEE_NULL);
        }
        //判断是否已经收款
        UserReceive receive = userReceiveMapper.selectUserReceiveInfo(orderId,toId);
        //用户信息
        UserDto user = userService.getById(fromId);
        //操作用户信息
        UserDto optUser = userService.getById(toId);
        if(receive!=null){
            if(UserPayConstants.RECEIVE_TYPE_2.equals(receive.getStatus())){
                //已经收款 不做任何处理  防止多次点击确认收款
                System.out.println("已经收款");
            }
        }else{
            if(Double.parseDouble(fee_ct)>0){
                //ct
                int i = walletMapper.updateFlowBalance(toId, new BigDecimal(Double.parseDouble(fee_ct)), new Date());
                if(i<=0){
                    throw new PayException(PayResultEnums.RECEIVE_FAIL);//商户收款失败
                }
                //保存ct变更记录
                WalletBalanceRecordDto payeeBalanceBill = new WalletBalanceRecordDto();
                payeeBalanceBill.setId(UUID.randomUUID().toString());//记录id
                payeeBalanceBill.setTargetUserId(toId);//余额变更用户id
                payeeBalanceBill.setOptUserId(toId);//操作的用户id
                payeeBalanceBill.setOptUserShowMsg(optUser.getUserName());
                payeeBalanceBill.setAmount(new BigDecimal(Double.parseDouble(fee_ct)));//变更的数量
                payeeBalanceBill.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
                payeeBalanceBill.setChangeType(WalletBillConstants.RECEIVE_CT_BUSINESS_TO_ADD);//途径：确认收款时，商户增加ct
                payeeBalanceBill.setServiceCharge(new BigDecimal("0.00"));
                payeeBalanceBill.setSubsidy(new BigDecimal("0.00"));
                payeeBalanceBill.setCreateTime(new Date());//设置创建时间

                walletBalanceRecordService.saveBalance(payeeBalanceBill);

                //用户获取的算力 支付的ct*90%
                BigDecimal force = new BigDecimal(Double.parseDouble(fee_ct)*0.9);
                walletMapper.updateCalculationForce(fromId,force, new Date());
                //保存用户算力变更记录
                WalletCalculatePowerRecord userPowerRecord = new WalletCalculatePowerRecord();
                userPowerRecord.setId(UUID.randomUUID().toString());
                userPowerRecord.setTargetUserId(fromId);//算力变更用户id
                userPowerRecord.setOptUserId(fromId);//操作的用户id
                userPowerRecord.setOptUserShowMsg(user.getUserName());//操作用户的账号
                userPowerRecord.setAmount(force);//变更的数量
                userPowerRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
                userPowerRecord.setChangeType(WalletBillConstants.RECEIVE_CALCULATIONFORCE_BUSINESS_FROM_ADD);//途径：确认收款时，用户增加相应的算力
                userPowerRecord.setCreateTime(new Date());//创建时间

                //保存算力记录
                walletCalculatePowerRecordMapper.insertSelective(userPowerRecord);
            }

            if(Double.parseDouble(fee_integral)>0){
                //积分
                int m = walletMapper.updateIntegral(toId,new BigDecimal(Double.parseDouble(fee_integral)),new Date());
                if(m<=0){
                    throw new PayException(PayResultEnums.RECEIVE_FAIL);//商户收款失败
                }
                //保存积分变更记录
                IntegralRecord integralRecord = new IntegralRecord();
                integralRecord.setUserId(toId);
                integralRecord.setId(UUID.randomUUID().toString());
                integralRecord.setTotalNum(new BigDecimal(Double.parseDouble(fee_integral)));//收款的积分
                integralRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
                integralRecord.setChangeType(IntegralConstants.RECEIVE_INTEGRAL_BUSINESS_TO_ADD);//途径：确认收款时，商户增加积分
                integralRecord.setCreateTime(new Date());
                integralRecord.setModifyTime(new Date());
                //保存积分记录
                integralRecordMapper.insertSelective(integralRecord);
            }
            //生成一条收款记录保存到数据库
            UserReceive userReceive = new UserReceive();
            userReceive.setId(UUID.randomUUID().toString());
            userReceive.setUserId(toId);
            userReceive.setOrderNo(orderId);
            userReceive.setFee_ct(fee_ct);
            userReceive.setFee_integral(fee_integral);
            userReceive.setStatus(UserPayConstants.RECEIVE_TYPE_2);//已收款
            userReceive.setCreateTime(new Date());
            userReceive.setModifyTime(new Date());

            userReceiveMapper.insert(userReceive);
        }

    }

    @Override
    @Transactional
    public void refund(String fromId, String toId, String orderId, String fee_ct,String fee_integral) {
        if(StringUtils.isBlank(fromId)){
            throw new PayException(PayResultEnums.FROM_ID_NULL);//用户id不能为空
        }
        if(StringUtils.isBlank(toId)){
            throw new PayException(PayResultEnums.TO_ID_NULL);//商户id不能为空
        }
        if(StringUtils.isBlank(orderId)){
            throw new PayException(PayResultEnums.ORDER_NO_NULL);//订单id不能为空
        }
        if(StringUtils.isBlank(fee_ct)){
            throw new PayException(PayResultEnums.REFUNDFEE_NONULL);//退款金额不能为空
        }
        if(StringUtils.isBlank(fee_integral)){
            throw new PayException(PayResultEnums.REFUNDFEE_NONULL);//退款金额不能为空
        }
        //操作用户信息
        UserDto optUser = userService.getById(fromId);
        //判断是否已经执行退款
        UserReFund refund = userReFundMapper.selectUserReFundInfo(orderId,fromId,toId);
        if(refund!=null){
            if(UserPayConstants.REFUND_TYPE_2.equals(refund.getRefund_status())){
                //已经退款 防止多次点击退款
                System.out.println("已经退款");
            }
        }else{
            //1.用户收款 2.商户扣款
            if(Double.parseDouble(fee_ct)>0){
                //ct
                int i = walletMapper.updateFlowBalance(fromId, new BigDecimal(Double.parseDouble(fee_ct)), new Date());//用户加ct
                if(i<=0){
                    throw new PayException(PayResultEnums.REFUNDFEE_FAIL);
                }
                //保存ct变更记录
                WalletBalanceRecordDto payeeBalanceBill = new WalletBalanceRecordDto();
                payeeBalanceBill.setId(UUID.randomUUID().toString());//记录id
                payeeBalanceBill.setTargetUserId(fromId);//余额变更用户id
                payeeBalanceBill.setOptUserId(fromId);//操作的用户id
                payeeBalanceBill.setOptUserShowMsg(optUser.getUserName());
                payeeBalanceBill.setAmount(new BigDecimal(Double.parseDouble(fee_ct)));//变更的数量
                payeeBalanceBill.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
                payeeBalanceBill.setChangeType(WalletBillConstants.REFUND_CT_BUSINESS_FROM_ADD);//途径：多商户退款时，用户增加ct
                payeeBalanceBill.setServiceCharge(new BigDecimal("0.00"));
                payeeBalanceBill.setSubsidy(new BigDecimal("0.00"));
                payeeBalanceBill.setCreateTime(new Date());//设置创建时间

                walletBalanceRecordService.saveBalance(payeeBalanceBill);

                /*int m = walletMapper.paymentByCT(toId, new BigDecimal(Double.parseDouble(fee_ct)), new Date());//商户扣ct
                if(m<=0){
                    throw new PayException(PayResultEnums.RECEIVE_FAIL);
                }
                //保存ct变更记录
                WalletBalanceRecordDto walletBalanceRecordDto = new WalletBalanceRecordDto();
                walletBalanceRecordDto.setId(UUID.randomUUID().toString());//记录id
                walletBalanceRecordDto.setTargetUserId(toId);//余额变更用户id
                walletBalanceRecordDto.setOptUserId(toId);//操作的用户id
                walletBalanceRecordDto.setOptUserShowMsg(toId);//操作用户name
                walletBalanceRecordDto.setAmount(new BigDecimal(Double.parseDouble(fee_ct)));//变更的数量
                walletBalanceRecordDto.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_SUB);//该记录的类型是：减少
                walletBalanceRecordDto.setChangeType(WalletBillConstants.REFUND_CT_BUSINESS_TO_DELETE);//途径：多商户退款时，商户减少ct
                walletBalanceRecordDto.setServiceCharge(new BigDecimal("0"));
                walletBalanceRecordDto.setSubsidy(new BigDecimal("0"));
                walletBalanceRecordDto.setCreateTime(new Date());//设置创建时间

                walletBalanceRecordService.saveBalance(walletBalanceRecordDto);*/
            }
            if(Double.parseDouble(fee_integral)>0){
                //积分
                int m = walletMapper.updateIntegral(fromId,new BigDecimal(Double.parseDouble(fee_integral)),new Date());//用户增加积分
                if(m<=0){
                    throw new PayException(PayResultEnums.REFUNDFEE_FAIL);
                }
                //保存积分变更记录
                IntegralRecord integralRecord = new IntegralRecord();
                integralRecord.setId(UUID.randomUUID().toString());
                integralRecord.setUserId(fromId);
                integralRecord.setTotalNum(new BigDecimal(Double.parseDouble(fee_integral)));//收款的积分
                integralRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
                integralRecord.setChangeType(IntegralConstants.REFUND_INTEGRAL_BUSINESS_FROM_ADD);//多商户退款时，用户增加积分
                integralRecord.setCreateTime(new Date());
                integralRecord.setModifyTime(new Date());

                integralRecordMapper.insertSelective(integralRecord);

                /*int n = walletMapper.paymentByintegral(toId, new BigDecimal(Double.parseDouble(fee_ct)), new Date());//商户扣积分
                if(n<=0){
                    throw new PayException(PayResultEnums.RECEIVE_FAIL);
                }
                //保存积分变更记录
                IntegralRecord integral = new IntegralRecord();
                integral.setUserId(toId);
                integral.setId(UUID.randomUUID().toString());
                integral.setTotalNum(new BigDecimal(Double.parseDouble(fee_integral)));//扣掉的积分
                integral.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_SUB);//该记录的类型是：减少
                integral.setChangeType(IntegralConstants.REFUND_INTEGRAL_BUSINESS_TO_DELETE);//多商户退款时，商户减少积分
                integral.setCreateTime(new Date());
                integral.setModifyTime(new Date());

                integralRecordMapper.insertSelective(integral);*/
            }
            //生成退款记录
            UserReFund userReFund = new UserReFund();
            userReFund.setId(UUID.randomUUID().toString());
            userReFund.setUserId(fromId);
            userReFund.setBusinessId(toId);
            userReFund.setOrderId(orderId);
            userReFund.setFee_ct(fee_ct);
            userReFund.setFee_integral(fee_integral);
            userReFund.setRefund_status(UserPayConstants.REFUND_TYPE_2);
            userReFund.setRemark("多商户退款");
            userReFund.setCreateTime(new Date());
            userReFund.setModifyTime(new Date());
            userReFundMapper.insert(userReFund);
        }

    }

    @Override
    @Transactional
    public void refund(String orderNo, String fromId, String fee_ct, String fee_integral) {
        if(StringUtils.isBlank(fromId)){
            throw new PayException(PayResultEnums.FROM_ID_NULL);//用户id不能为空
        }
        if(StringUtils.isBlank(orderNo)){
            throw new PayException(PayResultEnums.ORDER_NO_NULL);//订单id不能为空
        }
        if(StringUtils.isBlank(fee_ct)){
            throw new PayException(PayResultEnums.REFUNDFEE_NONULL);//退款金额不能为空
        }
        if(StringUtils.isBlank(fee_integral)){
            throw new PayException(PayResultEnums.REFUNDFEE_NONULL);//退款金额不能为空
        }
        //操作用户信息
        UserDto optUser = userService.getById(fromId);
        //单商户 退款
        if(Double.parseDouble(fee_ct)>0){
            //ct 用户加ct
            int i = walletMapper.updateFlowBalance(fromId, new BigDecimal(Double.parseDouble(fee_ct)), new Date());
            if(i<=0){
                throw new PayException(PayResultEnums.REFUNDFEE_FAIL);
            }
            //保存ct变更记录
            WalletBalanceRecordDto payeeBalanceBill = new WalletBalanceRecordDto();
            payeeBalanceBill.setId(UUID.randomUUID().toString());//记录id
            payeeBalanceBill.setTargetUserId(fromId);//余额变更用户id
            payeeBalanceBill.setOptUserId(fromId);//操作的用户id
            payeeBalanceBill.setOptUserShowMsg(optUser.getUserName());//操作用户name
            payeeBalanceBill.setAmount(new BigDecimal(Double.parseDouble(fee_ct)));//变更的数量
            payeeBalanceBill.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
            payeeBalanceBill.setChangeType(WalletBillConstants.REFUND_CT_PERSON_FROM_ADD);//途径：单商户退款时，用户增加ct
            payeeBalanceBill.setServiceCharge(new BigDecimal("0.00"));
            payeeBalanceBill.setSubsidy(new BigDecimal("0.00"));
            payeeBalanceBill.setCreateTime(new Date());//设置创建时间

            walletBalanceRecordService.saveBalance(payeeBalanceBill);
        }
        if(Double.parseDouble(fee_integral)>0){
            //积分 用户加积分
            //积分
            int m = walletMapper.updateIntegral(fromId,new BigDecimal(Double.parseDouble(fee_integral)),new Date());
            if(m<=0){
                throw new PayException(PayResultEnums.REFUNDFEE_FAIL);
            }
            //保存积分变更记录
            IntegralRecord integralRecord = new IntegralRecord();
            integralRecord.setId(UUID.randomUUID().toString());
            integralRecord.setUserId(fromId);
            integralRecord.setTotalNum(new BigDecimal(Double.parseDouble(fee_integral)));//扣掉的积分
            integralRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
            integralRecord.setChangeType(IntegralConstants.REFUND_INTEGRAL_PERSON_FROM_ADD);//途径：单商户退款时，用户增加积分
            integralRecord.setCreateTime(new Date());
            integralRecord.setModifyTime(new Date());

            integralRecordMapper.insertSelective(integralRecord);
        }

        //生成退款记录
        UserReFund userReFund = new UserReFund();
        userReFund.setId(UUID.randomUUID().toString());
        userReFund.setUserId(fromId);
        userReFund.setBusinessId("平台");
        userReFund.setOrderId(orderNo);
        userReFund.setFee_ct(fee_ct);
        userReFund.setFee_integral(fee_integral);
        userReFund.setRefund_status(UserPayConstants.REFUND_TYPE_2);
        userReFund.setRemark("单商户退款");
        userReFund.setCreateTime(new Date());
        userReFund.setModifyTime(new Date());
        userReFundMapper.insert(userReFund);

    }

    @Override
    @Transactional
    public void award(UserAwardReq userAwardReq) {
        String award_type = userAwardReq.getAward_type();
        if(StringUtils.isBlank(award_type)){
            throw new PayException(PayResultEnums.AWARD_TYPE_NULL);
        }
        Integer changeType = 0;
        if(UserAwardConstants.AWARD_TYPE_SALES.equals(award_type)){
            changeType = WalletBillConstants.USER_AWARD_FROM_ADD_1;//1.销售提成
        }else if(UserAwardConstants.AWARD_TYPE_PROFIT.equals(award_type)){
            changeType = WalletBillConstants.USER_AWARD_FROM_ADD_2;//2.差额利润
        }else if(UserAwardConstants.AWARD_TYPE_SUBSIDY.equals(award_type)){
            changeType = WalletBillConstants.USER_AWARD_FROM_ADD_3;//3.服务补贴
        }else if(UserAwardConstants.AWARD_TYPE_WEIGHTED.equals(award_type)){
            changeType = WalletBillConstants.USER_AWARD_FROM_ADD_4;//4.加权分红
        }
        List<UserAwardPO> AwardList = userAwardReq.getAward();
        if(AwardList!=null&&AwardList.size()>0){
            for(UserAwardPO userAwardPO:AwardList){
                //获取用户发放奖金信息
                String userId = userAwardPO.getUserId();
                //操作用户信息
                UserDto optUser = userService.getById(userId);
                BigDecimal award_ct = new BigDecimal(userAwardPO.getAward_ct());
                BigDecimal award_integral = new BigDecimal(userAwardPO.getAward_integral());
                String award_orderNo = userAwardReq.getAward_orderNo();
                //给用户账户发放奖金
                int count1 = walletMapper.updateFlowBalanceAndIntegral(userId,award_ct,award_integral,new Date());
                if(count1!=1){
                    throw new PayException(PayResultEnums.AWARD_FAIL);
                }
                //更新奖金发放状态
                int count2 = userAwardMapper.updateUserAwardStatus(userId,award_orderNo,award_type,UserAwardConstants.AWARD_STATUS_ISSUED,new Date());//已发放
                if(count2!=1){
                    throw new PayException(PayResultEnums.AWARD_FAIL);
                }
                if(!"0".equals(userAwardPO.getAward_ct())){
                    //保存ct变更记录
                    WalletBalanceRecordDto payeeBalanceBill = new WalletBalanceRecordDto();
                    payeeBalanceBill.setId(UUID.randomUUID().toString());//记录id
                    payeeBalanceBill.setTargetUserId(userId);//余额变更用户id
                    payeeBalanceBill.setOptUserId(userId);//操作的用户id
                    payeeBalanceBill.setOptUserShowMsg(optUser.getUserName());//操作用户name
                    payeeBalanceBill.setAmount(award_ct);//变更的数量
                    payeeBalanceBill.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
                    payeeBalanceBill.setChangeType(changeType);//途径：奖金发放时，用户增加ct
                    payeeBalanceBill.setServiceCharge(new BigDecimal("0.00"));
                    payeeBalanceBill.setSubsidy(new BigDecimal("0.00"));
                    payeeBalanceBill.setCreateTime(new Date());//设置创建时间

                    walletBalanceRecordService.saveBalance(payeeBalanceBill);
                }
                if(!"0".equals(userAwardPO.getAward_integral())){
                    //保存积分变更记录
                    IntegralRecord integralRecord = new IntegralRecord();
                    integralRecord.setId(UUID.randomUUID().toString());
                    integralRecord.setUserId(userId);
                    integralRecord.setTotalNum(award_integral);//变更的数量
                    integralRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
                    integralRecord.setChangeType(changeType);//途径：奖金发放时，用户增加积分
                    integralRecord.setCreateTime(new Date());
                    integralRecord.setModifyTime(new Date());

                    integralRecordMapper.insertSelective(integralRecord);
                }
            }
        }
    }

    @Override
    public void saveUserAward(UserAwardReq userAwardReq) {
        String award_type = userAwardReq.getAward_type();
        if(StringUtils.isBlank(award_type)){
            throw new PayException(PayResultEnums.AWARD_TYPE_NULL);
        }
        List<UserAwardPO> AwardList = userAwardReq.getAward();
        if(AwardList!=null&&AwardList.size()>0) {
            for (UserAwardPO userAwardPO : AwardList) {
                //获取用户发放奖金信息
                String userId = userAwardPO.getUserId();
                String award_ct = userAwardPO.getAward_ct();
                String award_integral = userAwardPO.getAward_integral();
                String award_orderNo = userAwardReq.getAward_orderNo();
                String award_remark = userAwardPO.getAward_remark();

                UserAward userAward = new UserAward();
                userAward.setId(UUID.randomUUID().toString());
                userAward.setUserId(userId);
                userAward.setAward_ct(new BigDecimal(award_ct));
                userAward.setAward_integral(new BigDecimal(award_integral));
                userAward.setAward_type(award_type);
                userAward.setAward_orderNo(award_orderNo);
                userAward.setAward_status(UserAwardConstants.AWARD_STATUS_UNISSUED);//待发放
                userAward.setAward_remark(award_remark);
                userAward.setCreateTime(new Date());
                userAward.setModifyTime(new Date());
                //记录奖金发放
                int count1 = userAwardMapper.insert(userAward);
                if(count1!=1){
                    throw new PayException(PayResultEnums.AWARD_FAIL);
                }
            }
        }
    }

    @Override
    public List<UserPayInfoPO> selectUserPayInfoList(UserPayInfoDTO userPayInfoDTO) {
        return userPayMapper.selectUserPayInfoList(userPayInfoDTO);
    }

    @Override
    public List<UserAwardListPO> selectUserAwardList(UserAwardDTO userAwardDTO) {
        return userAwardMapper.selectUserAwardList(userAwardDTO);
    }

    @Override
    public List<UserReceiveListPO> selectUserReceiveList(UserReceiveDTO userReceiveDTO) {
        return userReceiveMapper.selectUserReceiveList(userReceiveDTO);
    }

    @Override
    public List<UserRefundListPO> selectUserRefundList(UserRefundDTO userRefundDTO) {
        List<UserRefundListPO> list = userReFundMapper.selectUserRefundList(userRefundDTO);
        for(UserRefundListPO userRefundListPO:list){
            String businessId = userRefundListPO.getBusinessId();
            //查询商户信息
            UserDto optUser = userService.getById(businessId);
            userRefundListPO.setBusinessId(optUser.getUserName());
        }
        return list;
    }

    /**
     * 判断用户余额是否充足
     * @param userBalance
     * @param prePayReq
     */
    private void checkBalanceAndIntegral(UserBalance userBalance, PrePayReq prePayReq) {
        BigDecimal flowBalance = userBalance.getFlowBalance();//CT
        //比较大小
        int a = flowBalance.compareTo(BigDecimal.valueOf(Double.parseDouble(prePayReq.getFee_ct())));
        /**
         * a = -1,表示flowBalance小于order_fee；
         a = 0,表示flowBalance等于order_fee；
         a = 1,表示flowBalance大于order_fee；
         */
        if(a==-1){
            throw new PayException(PayResultEnums.FLOWBALANCE_NOT_ENOUGH);//ct余额不足
        }
        BigDecimal integral = userBalance.getIntegral();//积分

        //比较大小
        int m = integral.compareTo(BigDecimal.valueOf(Double.parseDouble(prePayReq.getFee_integral())));
        /**
         * a = -1,表示integral小于order_fee；
         a = 0,表示integral等于order_fee；
         a = 1,表示integral大于order_fee；
         */
        if(m==-1){
            throw new PayException(PayResultEnums.INTEGRAL_NOT_ENOUGH);//积分余额不足
        }
    }

    /**
     * 验证请求参数和签名是否一致
     * @param prePayReq
     */
    private void checkPramAndSign(PrePayReq prePayReq) {
        String fromId = prePayReq.getFromId();//用户id
        if(StringUtils.isBlank(fromId)){
            throw new PayException(PayResultEnums.FROM_ID_NULL);
        }
        String order_no = prePayReq.getOrder_no();//订单id
        if(StringUtils.isBlank(order_no)){
            throw new PayException(PayResultEnums.ORDER_NO_NULL);
        }
        //订单费用  必传
        String fee_ct = prePayReq.getFee_ct();//订单费用ct
        if(StringUtils.isBlank(fee_ct)){
            throw new PayException(PayResultEnums.ORDER_FEE_NULL);
        }
        String fee_integral = prePayReq.getFee_integral();//订单费用ct
        if(StringUtils.isBlank(fee_integral)){
            throw new PayException(PayResultEnums.ORDER_FEE_NULL);
        }
        String body = prePayReq.getBody();//商品描述
        if(StringUtils.isBlank(body)){
            throw new PayException(PayResultEnums.ORDER_BODY_NULL);
        }
        String notify_url = prePayReq.getNotify_url();//支付回调地址
        if(StringUtils.isBlank(notify_url)){
            throw new PayException(PayResultEnums.NOTIFY_URL_NULL);
        }
        String state = prePayReq.getState();//自定义参数（非必传参数）
        String sign = prePayReq.getSign();//签名
        if(StringUtils.isBlank(sign)){
            throw new PayException(PayResultEnums.SIGN_NULL);
        }
        String timesiamp = prePayReq.getTimesiamp();//时间戳
        if(StringUtils.isBlank(timesiamp)){
            throw new PayException(PayResultEnums.TIMESIAMP_NULL);
        }

        //验证签名是否一致
        String value = "fromId="+fromId+"&order_no="+order_no+"&fee_ct="+fee_ct+"&fee_integral="+fee_integral+"&body="+body;
        if(StringUtils.isNotBlank(state)){
            value = value + "&state="+state+"&timesiamp="+timesiamp+"&MD5";
        }else{
            value = value +"&timesiamp="+timesiamp+"&MD5";
        }
        String signs = MD5Utils.MD5(value);

        if(!signs.equals(sign)){
            throw new PayException(PayResultEnums.SIGN_ERROR);//签名错误
        }

    }
}
