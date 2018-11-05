package com.cwt.service.service.impl;

import com.cwt.common.enums.BalanceOrderResultEnums;
import com.cwt.common.enums.WalletResultEnums;
import com.cwt.common.exception.BalanceOrderException;
import com.cwt.common.exception.WalletExeption;
import com.cwt.common.util.BalanceComputeUtils;
import com.cwt.common.util.ExceptionPreconditionUtils;
import com.cwt.domain.constant.BalanceOrderBillConstants;
import com.cwt.domain.constant.InformationConstants;
import com.cwt.domain.constant.WalletBillConstants;
import com.cwt.domain.dto.balance.*;

import com.cwt.domain.dto.user.UserDto;
import com.cwt.domain.dto.wallet.WalletDto;
import com.cwt.domain.entity.*;
import com.cwt.persistent.mapper.*;
import com.cwt.service.service.BalanceOrderService;
import com.cwt.service.service.UserService;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("balanceOrderService")
public class BalanceOrderServiceImpl implements BalanceOrderService {

    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private BalanceOrderMapper balanceOrderMapper;
    @Autowired
    private BalanceOrderBillMapper balanceOrderBillMapper;
    @Autowired
    private InformationMapper informationMapper;
    @Autowired
    private WalletBalanceRecordMapper walletBalanceRecordMapper;
    @Autowired
    private UserService userService;
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
     * @param userId 用户Id
     * @param orderType 买/卖单
     * @param payType 支付方式
     * @param orderNum 交易数量
     * @param pageNum 起始行号
     * @param pageSize 总行数
     * @param orderByType 分组类型
     * @return
     */
    @Override
    public List<GetBalanceOrderByConditionDto> listByCondition(String userId, Integer orderType, Integer payType, BigDecimal orderNum, Integer pageNum, Integer pageSize, String orderByType) {
        if (pageSize == null || pageSize == 0) {
            //默认10条
            pageSize = 10;
        }
        //开始分页
        PageHelper.startPage(pageNum, pageSize);

        //分组类型判断
        String orderByTypeSql = "";
        if ("dateType".equals(orderByType)) {
            orderByTypeSql = " b.modify_time DESC ";
        } else {
            orderByTypeSql = " b.balance_convert_percent ASC , b.modify_time DESC ";
        }

        return balanceOrderMapper.listByCondition(userId, orderType, payType, orderNum, orderByTypeSql, BalanceOrderBillConstants.ORDER_STATUS_NEW, BalanceOrderBillConstants.ORDER_STATUS_UNDERWAY, BalanceOrderBillConstants.ORDER_STATUS_TRADING);
    }

    /**
     * 查询用户的发布记录
     *
     * @param userId   用户Id
     * @param pageNum  起始行号
     * @param pageSize 总行数
     * @return
     */
    @Override
    public List<GetBalanceOrderByConditionDto> listByReleaseRecords(String userId, Integer pageNum, Integer pageSize) {
        if (pageSize == null || pageSize == 0) {
            //默认10条
            pageSize = 10;
        }
        StringBuffer orderBySql = new StringBuffer();
        orderBySql.append("ORDER BY FIELD(app_balance_order.order_status,");
        orderBySql.append(BalanceOrderBillConstants.ORDER_STATUS_NEW).append(",");
        orderBySql.append(BalanceOrderBillConstants.ORDER_STATUS_UNDERWAY).append(",");
        orderBySql.append(BalanceOrderBillConstants.ORDER_STATUS_TRADING).append(",");
        orderBySql.append(BalanceOrderBillConstants.ORDER_STATUS_INVALID).append(",");
        orderBySql.append(BalanceOrderBillConstants.ORDER_STATUS_FINISH).append(")");
        orderBySql.append(",app_balance_order.modify_time DESC");
        //开始分页
        PageHelper.startPage(pageNum, pageSize);
        return balanceOrderMapper.listByReleaseRecords(userId, orderBySql.toString(), BalanceOrderBillConstants.ORDER_STATUS_INVALID);
    }

    /**
     * 撤单操作
     *
     * @param id 买单ID
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public boolean updateBalanceBuyOrderInvalid(String id) {
        //查询买单的信息
        BalanceOrder balanceOrder = new BalanceOrder();
        balanceOrder.setId(id);
        BalanceOrder balanceOrderNew = balanceOrderMapper.selectOne(balanceOrder);
        // 获取订单状态
        int orderStatus = balanceOrderNew.getOrderStatus();
        // 订单只有在新建状态和挂单状态下，才可以撤单
        if (orderStatus == BalanceOrderBillConstants.ORDER_STATUS_NEW || orderStatus == BalanceOrderBillConstants.ORDER_STATUS_UNDERWAY) {

            balanceOrderNew.setOrderStatus(BalanceOrderBillConstants.ORDER_STATUS_INVALID);
            balanceOrderNew.setModifyTime(new Date());
            int updateInt = balanceOrderMapper.updateByPrimaryKey(balanceOrderNew);
            return updateInt > 0 ? true : false;

        } else if (orderStatus == BalanceOrderBillConstants.ORDER_STATUS_FINISH) {
            throw new BalanceOrderException(BalanceOrderResultEnums.TRANSACTION_COMPLETE_ERROR);
        } else {
            throw new BalanceOrderException(BalanceOrderResultEnums.TRANSACTION_PRODUCTION_INVALID);
        }

    }

    /**
     * 撤单操作
     *
     * @param id 卖单ID
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public boolean updateBalanceSellOrderInvalid(String id, String userId, String telephone) {
        //查询卖单的信息
        BalanceOrder balanceOrder = new BalanceOrder();
        balanceOrder.setId(id);
        BalanceOrder balanceOrderNew = balanceOrderMapper.selectOne(balanceOrder);
        // 获取订单状态
        int orderStatus = balanceOrderNew.getOrderStatus();
        // 需要返回的余额数目
        BigDecimal amount = BigDecimal.valueOf(1);

        // 订单只有在新建状态和挂单状态下，才可以撤单
        if (orderStatus == BalanceOrderBillConstants.ORDER_STATUS_NEW || orderStatus == BalanceOrderBillConstants.ORDER_STATUS_UNDERWAY) {
            // 计算返回的余额数目
            amount = BalanceComputeUtils.freeBack(balanceOrderNew.getOrderNum(), balanceOrderNew.getOrderTotalNum(), balanceOrderNew.getServerCharge(), balanceOrderNew.getSubsidy());
        } else if (orderStatus == BalanceOrderBillConstants.ORDER_STATUS_FINISH) {
            throw new BalanceOrderException(BalanceOrderResultEnums.TRANSACTION_COMPLETE_ERROR);
        } else {
            throw new BalanceOrderException(BalanceOrderResultEnums.TRANSACTION_PRODUCTION_INVALID);
        }
        //获取用户的钱包信息
        WalletDto walletDto = walletMapper.selectByUserId(balanceOrderNew.getUserId());
        //返回客户的数字资产
        walletDto.setFlowBalance(walletDto.getFlowBalance().add(balanceOrderNew.getOrderTotalNum()));
        walletDto.setModifyTime(new Date());
        Wallet wallet = new Wallet();
        BeanUtils.copyProperties(walletDto, wallet);// 类型转换
        walletMapper.updateByPrimaryKey(wallet);

        // 添加余额变动记录
        WalletBalanceRecord walletBalanceRecord = new WalletBalanceRecord();
        walletBalanceRecord.setId(UUID.randomUUID().toString());    //余额变得记录ID
        walletBalanceRecord.setTargetUserId(balanceOrderNew.getUserId());     // 用户的ID
        walletBalanceRecord.setOptUserId(userId);       //操作用户的ID
        walletBalanceRecord.setOptUserShowMsg(telephone);   //当前用户手机号
        walletBalanceRecord.setAmount(amount);    //余额变得的数量
        walletBalanceRecord.setCreateTime(new Date());  //创建时间
        walletBalanceRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD); //余额变动的类型：增加
        walletBalanceRecord.setServiceCharge(balanceOrderNew.getOrderTotalNum().multiply(balanceOrderNew.getServerCharge())); //  平台手续费
        walletBalanceRecord.setSubsidy(balanceOrderNew.getOrderTotalNum().multiply(balanceOrderNew.getSubsidy()));  //补贴费
        walletBalanceRecord.setChangeType(WalletBillConstants.CHANGE_TYPE_REVOCATION_SELL_ORDER);   //撤销卖单状态
        walletBalanceRecordMapper.insert(walletBalanceRecord);

        balanceOrderNew.setOrderStatus(BalanceOrderBillConstants.ORDER_STATUS_INVALID);
        int updateInt = balanceOrderMapper.updateByPrimaryKey(balanceOrderNew);
        return updateInt > 0 ? true : false;
    }


    /***
     * 根据订单id查询订单
     * @param id
     * @return
     */
    @Override
    public BalanceOrderDto getById(String id) {
        BalanceOrderDto balanceOrderDto = new BalanceOrderDto();
        BeanUtils.copyProperties(balanceOrderMapper.selectByPrimaryKey(id), balanceOrderDto);
        return balanceOrderDto;
    }

    /***
     * 强制撤销总单
     * @param orderId 总单id
     * @param userId 操作该功能的用户
     */
    @Override
    public void cancelOrder(String orderId, String userId) {
        BalanceOrder balanceOrder = balanceOrderMapper.selectByPrimaryKey(orderId);
        WalletDto walletDto = walletMapper.selectByUserId(userId);
        //判断发布订单是否存在
        ExceptionPreconditionUtils.checkNotNull(balanceOrder,
                new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER));
        //判断钱包是否存在
        ExceptionPreconditionUtils.checkNotNull(walletDto,
                new WalletExeption(WalletResultEnums.NO_WALLET));
        //判断撤单操作者是否是订单发布者
        if (!balanceOrder.getUserId().equals(userId)) {
            throw new BalanceOrderException(BalanceOrderResultEnums.USER_ERROR);
        }
        //判断订单状态，如果不是进行中，则不能执行撤销功能
        if (balanceOrder.getOrderStatus() != BalanceOrderBillConstants.ORDER_STATUS_UNDERWAY) {
            throw new BalanceOrderException(BalanceOrderResultEnums.ORDER_TYPE_CHANGE);
        }
        //操作用户信息
        UserDto optUser = userService.getById(userId);
        //如果是卖单 返回订单剩余余额
        if (balanceOrder.getOrderType() == BalanceOrderBillConstants.ORDER_TYPE_SELL) {
            //实际返回的余额
            BigDecimal freeBack = BalanceComputeUtils.freeBack(balanceOrder.getOrderNum(), null, balanceOrder.getServerCharge(), balanceOrder.getSubsidy());
            //更新发布者钱包
            Wallet wallet = new Wallet();
            wallet.setUserId(userId);
            wallet.setId(walletDto.getId());
            wallet.setModifyTime(new Date());
            wallet.setVersion(walletDto.getVersion() + 1);
            wallet.setFlowBalance(walletDto.getFlowBalance().add(freeBack));

            //更新余额变更记录
            WalletBalanceRecord walletRecord = new WalletBalanceRecord();
            walletRecord.setId(UUID.randomUUID().toString());
            walletRecord.setTargetUserId(userId);
            walletRecord.setOptUserId(userId);
            walletRecord.setOptUserShowMsg(optUser.getUserName());
            walletRecord.setAmount(freeBack);
            walletRecord.setServiceCharge(freeBack.multiply(balanceOrder.getServerCharge()));
            walletRecord.setSubsidy(freeBack.multiply(balanceOrder.getSubsidy()));
            walletRecord.setCreateTime(new Date());
            walletRecord.setChangeType(WalletBillConstants.CHANGE_TYPE_REVOCATION_SELL_ORDER);
            walletRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);

            //更新数据
            walletBalanceRecordMapper.insertSelective(walletRecord);
            walletMapper.updateByPrimaryKeySelective(wallet);
        }
        //将订单状态改变为已失效
        balanceOrder.setOrderStatus(BalanceOrderBillConstants.ORDER_STATUS_INVALID);
        balanceOrder.setModifyTime(new Date());
        //更新数据
        balanceOrderMapper.updateByPrimaryKeySelective(balanceOrder);
    }

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
     * @param orderTotalNum 交易总额
     * @return
     */
    @Override
    public BalanceOrderInfoDto headerBalanceOrderBuyConfirm(BigDecimal orderTotalNum, BigDecimal limitNumMin, BigDecimal limitNumMax, Integer payType, BigDecimal balanceConvertPercent) {
        judgeCreateOrder(orderTotalNum, limitNumMax, limitNumMin, balanceConvertPercent);
        BalanceOrderInfoDto balanceOrderInfoDto = new BalanceOrderInfoDto();
        try {
            //获取余额交易比例----余额：现金
//            BigDecimal balanceConvertPercent = new BigDecimal(informationMapper.selectByDateName(InformationConstants.DATA_NAME_BALANCE_CONVERT_PERCENT, 1).getDataValue());
            //获取平台扣除卖方的手续费
            BigDecimal serverCharge = new BigDecimal(informationMapper.selectByDateName(InformationConstants.DATA_NAME_SELL_PLATFORM_FEE_RATIO, 1).getDataValue());
            // 获取卖方补贴给买方的手续费
            BigDecimal subsidy = new BigDecimal(informationMapper.selectByDateName(InformationConstants.DATA_NAME_SELL_SUBSIDY_RATIO, 1).getDataValue());
            balanceOrderInfoDto.setOrderTotalNum(orderTotalNum);//交易总数
            balanceOrderInfoDto.setLimitNumMin(limitNumMin);//交易最小限制
            balanceOrderInfoDto.setLimitNumMax(limitNumMax);//交易上限
            balanceOrderInfoDto.setPayType(payType);//支付方式
            balanceOrderInfoDto.setBalanceConvertPercent(balanceConvertPercent);//余额：现金比例
            balanceOrderInfoDto.setServerCharge(serverCharge);//平台手续费比例
            balanceOrderInfoDto.setSubsidy(subsidy);//补贴比例
            balanceOrderInfoDto.setOrderNum(orderTotalNum);////实际交易总额（剩余交易额）

            //卖家实际燃烧的资产
            BigDecimal deduction = BigDecimal.valueOf(1).subtract(serverCharge).subtract(subsidy);
            BigDecimal sellOrderTotalNum = orderTotalNum.divide(deduction, 4, RoundingMode.HALF_UP);
            //平台手续费用
            BigDecimal serverChargeFee = sellOrderTotalNum.multiply(serverCharge);
            //买家补贴费用
            BigDecimal subsidyFee = sellOrderTotalNum.multiply(subsidy);

            balanceOrderInfoDto.setServerChargeFee(serverChargeFee);//平台手续费用
            balanceOrderInfoDto.setSubsidyFee(subsidyFee);//买家补贴费用
            balanceOrderInfoDto.setRealCash(orderTotalNum.multiply(balanceConvertPercent));//实际支出的现金
            balanceOrderInfoDto.setFlowBalanceCash(BalanceComputeUtils.buyFlowBalance(orderTotalNum, serverCharge, subsidy));//实际获得的流动余额

        } catch (Exception e) {
            //比例数值 String 类型转 BigDecimal 类型失败，预计为配置表数据有误
            throw new BalanceOrderException(BalanceOrderResultEnums.BIGDECIMAL_ERROR);
        }
        return balanceOrderInfoDto;
    }

    /**
     * 返回余额交易卖单信息
     *
     * @param orderTotalNum 交易总额
     * @return
     */
    @Override
    public BalanceOrderInfoDto headerBalanceOrderSellConfirm(BigDecimal orderTotalNum, BigDecimal limitNumMin, BigDecimal limitNumMax, Integer payType, BigDecimal balanceConvertPercent) {
        BalanceOrderInfoDto balanceOrderInfoDto = new BalanceOrderInfoDto();
//        BigDecimal balanceConvertPercent;
        BigDecimal serverCharge;
        BigDecimal subsidy;
        try {
            //获取余额交易比例----余额：现金
//            balanceConvertPercent = new BigDecimal(informationMapper.selectByDateName(InformationConstants.DATA_NAME_BALANCE_CONVERT_PERCENT, 1).getDataValue());
            //获取平台抽取手续费比例
            serverCharge = new BigDecimal(informationMapper.selectByDateName(InformationConstants.DATA_NAME_SELL_PLATFORM_FEE_RATIO, 1).getDataValue());
            // 获取补贴给交易对方的比例
            subsidy = new BigDecimal(informationMapper.selectByDateName(InformationConstants.DATA_NAME_SELL_SUBSIDY_RATIO, 1).getDataValue());

        } catch (Exception e) {
            //比例数值 String 类型转 BigDecimal 类型失败，预计为配置表数据有误
            throw new BalanceOrderException(BalanceOrderResultEnums.BIGDECIMAL_ERROR);
        }
        judgeCreateOrder(orderTotalNum, limitNumMax, limitNumMin, balanceConvertPercent);
        BigDecimal orderNum = BalanceComputeUtils.sellOrderNum(orderTotalNum, serverCharge, subsidy);
        if (limitNumMax.compareTo(orderNum) > 0) {
            BalanceOrderResultEnums balanceOrderResultEnums = BalanceOrderResultEnums.FLOW_BALANCE_MAX_ERROR_TWO;
            balanceOrderResultEnums.setMsg("交易限额的 '最大限额' 不能大于'" + orderNum.toString() + "'");
            throw new BalanceOrderException(balanceOrderResultEnums);
        }

        balanceOrderInfoDto.setOrderTotalNum(orderTotalNum);//交易总数
        balanceOrderInfoDto.setLimitNumMin(limitNumMin);//交易最小限制
        balanceOrderInfoDto.setLimitNumMax(limitNumMax);//交易上限
        balanceOrderInfoDto.setPayType(payType);//支付方式
        balanceOrderInfoDto.setBalanceConvertPercent(balanceConvertPercent);//余额：现金比例
        balanceOrderInfoDto.setServerCharge(serverCharge);//平台手术费比例
        balanceOrderInfoDto.setSubsidy(subsidy);//补贴比例
        balanceOrderInfoDto.setOrderNum(orderNum);//实际交易总额（剩余交易额）

        balanceOrderInfoDto.setServerChargeFee(orderTotalNum.multiply(serverCharge));//平台手续费用
        balanceOrderInfoDto.setSubsidyFee(orderTotalNum.multiply(subsidy));//买家补贴费用
        balanceOrderInfoDto.setRealCash(balanceOrderInfoDto.getOrderNum().multiply(balanceConvertPercent));//实际收入的现金
        balanceOrderInfoDto.setFlowBalanceCash(orderTotalNum);//实际减少的流动余额

        return balanceOrderInfoDto;
    }

    /**
     * 创建一个余额交易的买单
     *
     * @param userId              用户Id
     * @param balanceOrderInfoDto 买单信息
     * @return
     */
    @Override
    public boolean insertBalanceOrderBuy(String userId, BalanceOrderInfoDto balanceOrderInfoDto) {
        judgeCreateOrder(balanceOrderInfoDto.getOrderTotalNum(), balanceOrderInfoDto.getLimitNumMax(), balanceOrderInfoDto.getLimitNumMin(), balanceOrderInfoDto.getBalanceConvertPercent());
        BalanceOrder balanceOrder = new BalanceOrder();
        balanceOrder.setId(UUID.randomUUID().toString()); //卖单ID
        balanceOrder.setUserId(userId);//用户ID

        balanceOrder.setOrderTotalNum(balanceOrderInfoDto.getOrderTotalNum());//交易总数
        balanceOrder.setLimitNumMin(balanceOrderInfoDto.getLimitNumMin());//交易最小限制
        balanceOrder.setLimitNumMax(balanceOrderInfoDto.getLimitNumMax());//交易上限
        balanceOrder.setPayType(balanceOrderInfoDto.getPayType());//支付方式
        balanceOrder.setBalanceConvertPercent(balanceOrderInfoDto.getBalanceConvertPercent());//余额：现金比例
        balanceOrder.setServerCharge(balanceOrderInfoDto.getServerCharge());//平台手术费比例
        balanceOrder.setSubsidy(balanceOrderInfoDto.getSubsidy());//补贴比例
        balanceOrder.setOrderNum(balanceOrderInfoDto.getOrderNum());//实际交易总额（剩余交易额）

        balanceOrder.setOrderType(BalanceOrderBillConstants.ORDER_TYPE_BUY);//买单类型
        balanceOrder.setOrderStatus(BalanceOrderBillConstants.ORDER_STATUS_NEW);//订单状态
        Date date = new Date();
        balanceOrder.setCreateTime(date); //创建时间
        balanceOrder.setModifyTime(date);  //修改时间
        int insertNumer = balanceOrderMapper.insert(balanceOrder);
        return insertNumer > 0 ? true : false;
    }

    /**
     * 创建一个余额交易的卖单
     *
     * @param userId              用户id
     * @param balanceOrderInfoDto 卖单信息
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public boolean insertBalanceOrderSell(String userId, String telephone, BalanceOrderInfoDto balanceOrderInfoDto) {
        judgeCreateOrder(balanceOrderInfoDto.getOrderTotalNum(), balanceOrderInfoDto.getLimitNumMax(), balanceOrderInfoDto.getLimitNumMin(), balanceOrderInfoDto.getBalanceConvertPercent());

        if (balanceOrderInfoDto.getLimitNumMax().compareTo(balanceOrderInfoDto.getOrderNum()) > 0) {
            BalanceOrderResultEnums balanceOrderResultEnums = BalanceOrderResultEnums.FLOW_BALANCE_MAX_ERROR_TWO;
            balanceOrderResultEnums.setMsg("交易限额的 '最大限额' 不能大于'" + balanceOrderInfoDto.getOrderNum().toString() + "'");
            throw new BalanceOrderException(balanceOrderResultEnums);
        }

        //获取用户的钱包信息
        WalletDto walletDto = walletMapper.selectByUserId(userId);
        //判断用户的流动余额是否小于交易总额
        if (walletDto.getFlowBalance().compareTo(balanceOrderInfoDto.getOrderTotalNum()) < 0) {
            throw new BalanceOrderException(BalanceOrderResultEnums.FLOW_BALANCE_INSUFFICIENT);
        }

        BalanceOrder balanceOrder = new BalanceOrder();
        balanceOrder.setId(UUID.randomUUID().toString()); //卖单ID
        balanceOrder.setUserId(userId);//用户ID

        balanceOrder.setOrderTotalNum(balanceOrderInfoDto.getOrderTotalNum());//交易总数
        balanceOrder.setLimitNumMin(balanceOrderInfoDto.getLimitNumMin());//交易最小限制
        balanceOrder.setLimitNumMax(balanceOrderInfoDto.getLimitNumMax());//交易上限
        balanceOrder.setPayType(balanceOrderInfoDto.getPayType());//支付方式
        balanceOrder.setBalanceConvertPercent(balanceOrderInfoDto.getBalanceConvertPercent());//余额：现金比例
        balanceOrder.setServerCharge(balanceOrderInfoDto.getServerCharge());//平台手术费比例
        balanceOrder.setSubsidy(balanceOrderInfoDto.getSubsidy());//补贴比例
        balanceOrder.setOrderNum(balanceOrderInfoDto.getOrderNum());//实际交易总额（剩余交易额）

        balanceOrder.setOrderType(BalanceOrderBillConstants.ORDER_TYPE_SELL);//买单类型
        balanceOrder.setOrderStatus(BalanceOrderBillConstants.ORDER_STATUS_NEW);//订单状态
        Date date = new Date();
        balanceOrder.setCreateTime(date); //创建时间
        balanceOrder.setModifyTime(date);  //修改时间

        // 减少钱包的流动余额
        walletDto.setFlowBalance(walletDto.getFlowBalance().subtract(balanceOrderInfoDto.getOrderTotalNum()));
        walletDto.setModifyTime(new Date());
        Wallet wallet = new Wallet();
        BeanUtils.copyProperties(walletDto, wallet);// 类型转换
        walletMapper.updateByPrimaryKey(wallet);

        // 添加余额变动记录
        WalletBalanceRecord walletBalanceRecord = new WalletBalanceRecord();
        walletBalanceRecord.setId(UUID.randomUUID().toString());    //余额变得记录ID
        walletBalanceRecord.setTargetUserId(userId);     // 用户的ID
        walletBalanceRecord.setOptUserId(userId);       //操作用户的ID
        walletBalanceRecord.setOptUserShowMsg(telephone);   //用户手机号
        walletBalanceRecord.setAmount(balanceOrderInfoDto.getOrderTotalNum());    //余额变得的数量
        walletBalanceRecord.setCreateTime(new Date());  //创建时间
        walletBalanceRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_SUB); //余额变动的类型：减少
        walletBalanceRecord.setServiceCharge(balanceOrderInfoDto.getServerChargeFee()); //  平台手续费
        walletBalanceRecord.setSubsidy(balanceOrderInfoDto.getSubsidyFee());  //补贴费
        walletBalanceRecord.setChangeType(WalletBillConstants.CHANGE_TYPE_CREATE_BALANCE_ORDER);
        walletBalanceRecordMapper.insert(walletBalanceRecord);

        // 创建卖单
        int insertNumer = balanceOrderMapper.insert(balanceOrder);
        return insertNumer > 0 ? true : false;
    }

    /**
     * 根据 ID 获取交易订单的基本信息
     *
     * @param id 交易订单ID
     * @return
     */
    @Override
    public BalanceOrderUpdateDto selectBalanceOrderUpdateById(String id) {
        //判断订单 ID 是否为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(id, new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER));
        return balanceOrderMapper.selectBalanceOrderUpdateById(id);
    }

    /**
     * 返回余额交易买单修改后的信息
     *
     * @param balanceOrderUpdateDto 买单的基本信息
     * @return
     */
    @Override
    public BalanceOrderInfoDto headerBalanceOrderBuyUpdateConfirm(BalanceOrderUpdateDto balanceOrderUpdateDto) {
        judgeCreateOrder(balanceOrderUpdateDto.getOrderTotalNum(), balanceOrderUpdateDto.getLimitNumMax(), balanceOrderUpdateDto.getLimitNumMin(), balanceOrderUpdateDto.getBalanceConvertPercent());
//        查询交易记录是否产生
        BalanceOrder order = new BalanceOrder();
        order.setId(balanceOrderUpdateDto.getId());
        BalanceOrder balanceOrderNew = balanceOrderMapper.selectOne(order);
        if(balanceOrderNew.getOrderStatus() != BalanceOrderBillConstants.ORDER_STATUS_NEW){
            throw new BalanceOrderException(BalanceOrderResultEnums.TRANSACTION_PRODUCTION);
        }
        //确认信息对象
        BalanceOrderInfoDto balanceOrderInfoDto = new BalanceOrderInfoDto();
        try {
            //获取余额交易比例----余额：现金
//            BigDecimal balanceConvertPercent = new BigDecimal(informationMapper.selectByDateName(InformationConstants.DATA_NAME_BALANCE_CONVERT_PERCENT, 1).getDataValue());
            BigDecimal balanceConvertPercent = balanceOrderUpdateDto.getBalanceConvertPercent();
            //获取平台抽取手续费比例
            BigDecimal serverCharge = new BigDecimal(informationMapper.selectByDateName(InformationConstants.DATA_NAME_SELL_PLATFORM_FEE_RATIO, 1).getDataValue());
            // 获取补贴给交易对方的比例
            BigDecimal subsidy = new BigDecimal(informationMapper.selectByDateName(InformationConstants.DATA_NAME_SELL_SUBSIDY_RATIO, 1).getDataValue());


            balanceOrderInfoDto.setId(balanceOrderUpdateDto.getId());   //订单 ID
            balanceOrderInfoDto.setOrderTotalNum(balanceOrderUpdateDto.getOrderTotalNum());//交易总数
            balanceOrderInfoDto.setLimitNumMin(balanceOrderUpdateDto.getLimitNumMin());//交易最小限制
            balanceOrderInfoDto.setLimitNumMax(balanceOrderUpdateDto.getLimitNumMax());//交易上限
            balanceOrderInfoDto.setPayType(balanceOrderUpdateDto.getPayType());//支付方式
            balanceOrderInfoDto.setBalanceConvertPercent(balanceConvertPercent);//余额：现金比例
            balanceOrderInfoDto.setServerCharge(serverCharge);//平台手续费比例
            balanceOrderInfoDto.setSubsidy(subsidy);//补贴比例
            balanceOrderInfoDto.setOrderNum(balanceOrderUpdateDto.getOrderTotalNum());////实际交易总额（剩余交易额）

            //卖家实际燃烧的资产
            BigDecimal deduction = BigDecimal.valueOf(1).subtract(serverCharge).subtract(subsidy);
            BigDecimal sellOrderTotalNum = balanceOrderUpdateDto.getOrderTotalNum().divide(deduction, 4, RoundingMode.HALF_UP);
            //平台手续费用
            BigDecimal serverChargeFee = sellOrderTotalNum.multiply(serverCharge);
            //买家补贴费用
            BigDecimal subsidyFee = sellOrderTotalNum.multiply(subsidy);

            balanceOrderInfoDto.setServerChargeFee(serverChargeFee);//平台手续费用
            balanceOrderInfoDto.setSubsidyFee(subsidyFee);//买家补贴费用

            balanceOrderInfoDto.setRealCash(balanceOrderUpdateDto.getOrderTotalNum().multiply(balanceConvertPercent));//实际支出的现金
            balanceOrderInfoDto.setFlowBalanceCash(BalanceComputeUtils.buyFlowBalance(balanceOrderUpdateDto.getOrderTotalNum(), serverCharge, subsidy));//实际获得的流动余额
        } catch (Exception e) {
            //比例数值 String 类型转 BigDecimal 类型失败，预计为配置表数据有误
            throw new BalanceOrderException(BalanceOrderResultEnums.BIGDECIMAL_ERROR);
        }
        return balanceOrderInfoDto;
    }

    /**
     * 返回余额交易卖单修改后的信息
     *
     * @param balanceOrderUpdateDto 卖单的基本信息
     * @return
     */
    @Override
    public BalanceOrderInfoDto headerBalanceOrderSellUpdateConfirm(BalanceOrderUpdateDto balanceOrderUpdateDto) {
        judgeCreateOrder(balanceOrderUpdateDto.getOrderTotalNum(), balanceOrderUpdateDto.getLimitNumMax(), balanceOrderUpdateDto.getLimitNumMin(), balanceOrderUpdateDto.getBalanceConvertPercent());

        //查询交易记录是否产生
        BalanceOrder order = new BalanceOrder();
        order.setId(balanceOrderUpdateDto.getId());
        BalanceOrder balanceOrderNew = balanceOrderMapper.selectOne(order);
        if(balanceOrderNew.getOrderStatus() != BalanceOrderBillConstants.ORDER_STATUS_NEW){
            throw new BalanceOrderException(BalanceOrderResultEnums.TRANSACTION_PRODUCTION);
        }

        //查询卖单的信息详情
        BalanceOrderDto balanceOrderDto = balanceOrderMapper.selectById(balanceOrderUpdateDto.getId());
        //获取用户的钱包信息
        WalletDto walletDto = walletMapper.selectByUserId(balanceOrderDto.getUserId());
        //流动余额的数目
        BigDecimal flowBalance = walletDto.getFlowBalance();
        //修改前的交易总额
        BigDecimal orderTotalNumOld = balanceOrderDto.getOrderTotalNum();
        //修改后的交易总额

        BigDecimal orderTotalNumNew = balanceOrderUpdateDto.getOrderTotalNum();
        if (orderTotalNumNew.compareTo(orderTotalNumOld.add(flowBalance)) > 0) { //修改后的交易总额大于总流动余额
            throw new BalanceOrderException(BalanceOrderResultEnums.UPDATE_FLOW_BALANCE_INSUFFICIENT);
        }

        //确认信息对象
        BalanceOrderInfoDto balanceOrderInfoDto = new BalanceOrderInfoDto();
        BigDecimal balanceConvertPercent;
        BigDecimal serverCharge;
        BigDecimal subsidy;
        try {
            //获取余额交易比例----余额：现金
//            balanceConvertPercent = new BigDecimal(informationMapper.selectByDateName(InformationConstants.DATA_NAME_BALANCE_CONVERT_PERCENT, 1).getDataValue());
            balanceConvertPercent = balanceOrderUpdateDto.getBalanceConvertPercent();
            //获取平台抽取手续费比例
            serverCharge = new BigDecimal(informationMapper.selectByDateName(InformationConstants.DATA_NAME_SELL_PLATFORM_FEE_RATIO, 1).getDataValue());
            // 获取补贴给交易对方的比例
            subsidy = new BigDecimal(informationMapper.selectByDateName(InformationConstants.DATA_NAME_SELL_SUBSIDY_RATIO, 1).getDataValue());

        } catch (Exception e) {
            //比例数值 String 类型转 BigDecimal 类型失败，预计为配置表数据有误
            throw new BalanceOrderException(BalanceOrderResultEnums.BIGDECIMAL_ERROR);
        }

        BigDecimal orderNum = BalanceComputeUtils.sellOrderNum(balanceOrderUpdateDto.getOrderTotalNum(), serverCharge, subsidy);
        if (balanceOrderUpdateDto.getLimitNumMax().compareTo(orderNum) > 0) {
            BalanceOrderResultEnums balanceOrderResultEnums = BalanceOrderResultEnums.FLOW_BALANCE_MAX_ERROR_TWO;
            balanceOrderResultEnums.setMsg("交易限额的 '最大限额' 不能大于'" + orderNum.toString() + "'");
            throw new BalanceOrderException(balanceOrderResultEnums);
        }

        balanceOrderInfoDto.setId(balanceOrderUpdateDto.getId());   //订单 ID
        balanceOrderInfoDto.setOrderTotalNum(balanceOrderUpdateDto.getOrderTotalNum());//交易总数
        balanceOrderInfoDto.setLimitNumMin(balanceOrderUpdateDto.getLimitNumMin());//交易最小限制
        balanceOrderInfoDto.setLimitNumMax(balanceOrderUpdateDto.getLimitNumMax());//交易上限
        balanceOrderInfoDto.setPayType(balanceOrderUpdateDto.getPayType());//支付方式
        balanceOrderInfoDto.setBalanceConvertPercent(balanceConvertPercent);//余额：现金比例
        balanceOrderInfoDto.setServerCharge(serverCharge);//平台手术费比例
        balanceOrderInfoDto.setSubsidy(subsidy);//补贴比例

        balanceOrderInfoDto.setOrderNum(orderNum);//实际交易总额（剩余交易额）
        balanceOrderInfoDto.setServerChargeFee(balanceOrderInfoDto.getOrderTotalNum().multiply(serverCharge));//平台手续费用
        balanceOrderInfoDto.setSubsidyFee(balanceOrderInfoDto.getOrderTotalNum().multiply(subsidy));//买家补贴费用
        balanceOrderInfoDto.setRealCash(balanceOrderInfoDto.getOrderNum().multiply(balanceConvertPercent));//实际收入的现金
        balanceOrderInfoDto.setFlowBalanceCash(balanceOrderInfoDto.getOrderTotalNum());//实际减少的流动余额

        return balanceOrderInfoDto;
    }

    /**
     * 修改买单
     *
     * @param balanceOrderInfoDto 买单信息
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public boolean updateBalanceOrderBuy(BalanceOrderInfoDto balanceOrderInfoDto) {
        judgeCreateOrder(balanceOrderInfoDto.getOrderTotalNum(), balanceOrderInfoDto.getLimitNumMax(), balanceOrderInfoDto.getLimitNumMin(), balanceOrderInfoDto.getBalanceConvertPercent());
        //查询交易记录是否产生
        BalanceOrder order = new BalanceOrder();
        order.setId(balanceOrderInfoDto.getId());
        BalanceOrder balanceOrderNew = balanceOrderMapper.selectOne(order);
        if(balanceOrderNew.getOrderStatus() != BalanceOrderBillConstants.ORDER_STATUS_NEW){
            throw new BalanceOrderException(BalanceOrderResultEnums.TRANSACTION_PRODUCTION);
        }

        BalanceOrder balanceOrder = new BalanceOrder();
        balanceOrder.setId(balanceOrderInfoDto.getId()); //买单ID
        balanceOrder.setOrderTotalNum(balanceOrderInfoDto.getOrderTotalNum());//交易总数
        balanceOrder.setLimitNumMin(balanceOrderInfoDto.getLimitNumMin());//交易最小限制
        balanceOrder.setLimitNumMax(balanceOrderInfoDto.getLimitNumMax());//交易上限
        balanceOrder.setPayType(balanceOrderInfoDto.getPayType());//支付方式
        balanceOrder.setBalanceConvertPercent(balanceOrderInfoDto.getBalanceConvertPercent());//余额：现金比例
        balanceOrder.setServerCharge(balanceOrderInfoDto.getServerCharge());//平台手术费比例
        balanceOrder.setSubsidy(balanceOrderInfoDto.getSubsidy());//补贴比例
        balanceOrder.setOrderNum(balanceOrderInfoDto.getOrderNum());//实际交易总额（剩余交易额）
        balanceOrder.setOrderType(BalanceOrderBillConstants.ORDER_TYPE_BUY);//买单类型
        balanceOrder.setOrderStatus(BalanceOrderBillConstants.ORDER_STATUS_NEW);//订单状态
        balanceOrder.setModifyTime(new Date()); //修改时间
        int updateNumer = balanceOrderMapper.updateByPrimaryKeySelective(balanceOrder);
        return updateNumer > 0 ? true : false;
    }

    /**
     * 修改卖单
     *
     * @param balanceOrderInfoDto 买单信息
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public boolean updateBalanceOrderSell(String userId, String telephone, BalanceOrderInfoDto balanceOrderInfoDto) {
        judgeCreateOrder(balanceOrderInfoDto.getOrderTotalNum(), balanceOrderInfoDto.getLimitNumMax(), balanceOrderInfoDto.getLimitNumMin(), balanceOrderInfoDto.getBalanceConvertPercent());

        if (balanceOrderInfoDto.getLimitNumMax().compareTo(balanceOrderInfoDto.getOrderNum()) > 0) {
            BalanceOrderResultEnums balanceOrderResultEnums = BalanceOrderResultEnums.FLOW_BALANCE_MAX_ERROR_TWO;
            balanceOrderResultEnums.setMsg("交易限额的 '最大限额' 不能大于'" + balanceOrderInfoDto.getOrderNum().toString() + "'");
            throw new BalanceOrderException(balanceOrderResultEnums);
        }

        BalanceOrder order = new BalanceOrder();
        order.setId(balanceOrderInfoDto.getId());
        BalanceOrder balanceOrderNew = balanceOrderMapper.selectOne(order);
        if(balanceOrderNew.getOrderStatus() != BalanceOrderBillConstants.ORDER_STATUS_NEW){
            throw new BalanceOrderException(BalanceOrderResultEnums.TRANSACTION_PRODUCTION);
        }

        //获取用户的钱包信息
        WalletDto walletDto = walletMapper.selectByUserId(userId);
        //查询卖单的信息详情
        BalanceOrderDto balanceOrderDto = balanceOrderMapper.selectById(balanceOrderInfoDto.getId());

        //流动余额的数目
        BigDecimal flowBalance = walletDto.getFlowBalance();
        //修改前的交易总额
        BigDecimal orderTotalNumOld = balanceOrderDto.getOrderTotalNum();
        //修改后的交易总额
        BigDecimal orderTotalNumNew = balanceOrderInfoDto.getOrderTotalNum();

        if (orderTotalNumNew.compareTo(orderTotalNumOld) != 0) { //交易总额不变

            if (orderTotalNumNew.compareTo(orderTotalNumOld.add(flowBalance)) > 0) { //修改后的交易总额大于总流动余额
                throw new BalanceOrderException(BalanceOrderResultEnums.UPDATE_FLOW_BALANCE_INSUFFICIENT);
            }
            // 钱包的流动余额变动
            walletDto.setFlowBalance(orderTotalNumOld.add(flowBalance).subtract(orderTotalNumNew));
            walletDto.setModifyTime(new Date());
            // 修改钱包的流动余额
            Wallet wallet = new Wallet();
            BeanUtils.copyProperties(walletDto, wallet);// 类型转换
            walletMapper.updateByPrimaryKey(wallet);

            // 添加余额变动记录
            WalletBalanceRecord walletBalanceRecord = new WalletBalanceRecord();
            walletBalanceRecord.setId(UUID.randomUUID().toString());    //余额变得记录ID
            walletBalanceRecord.setTargetUserId(userId);     // 用户的ID
            walletBalanceRecord.setOptUserId(userId);       //操作用户的ID
            walletBalanceRecord.setOptUserShowMsg(telephone);   //用户手机号
            walletBalanceRecord.setAmount((orderTotalNumNew.subtract(orderTotalNumOld)).abs());    //余额变得的数量
            walletBalanceRecord.setCreateTime(new Date());  //创建时间
            //判断余额变动的类型
            int arithmeticType = orderTotalNumNew.compareTo(orderTotalNumOld) > 0 ? WalletBillConstants.ARITHMETIC_TYPE_SUB : WalletBillConstants.ARITHMETIC_TYPE_ADD;
            walletBalanceRecord.setArithmeticType(arithmeticType); //余额变动的类型：减少
            walletBalanceRecord.setServiceCharge(balanceOrderInfoDto.getServerChargeFee()); //  平台手续费
            walletBalanceRecord.setSubsidy(balanceOrderInfoDto.getSubsidyFee());  //补贴费
            walletBalanceRecord.setChangeType(WalletBillConstants.CHANGE_TYPE_UPDATE_BALANCE_ORDER);
            //添加余额变动记录
            walletBalanceRecordMapper.insert(walletBalanceRecord);
        }

        //修改卖单
        balanceOrderDto.setId(balanceOrderInfoDto.getId()); //卖单ID
        balanceOrderDto.setOrderTotalNum(balanceOrderInfoDto.getOrderTotalNum());//交易总数
        balanceOrderDto.setLimitNumMin(balanceOrderInfoDto.getLimitNumMin());//交易最小限制
        balanceOrderDto.setLimitNumMax(balanceOrderInfoDto.getLimitNumMax());//交易上限
        balanceOrderDto.setPayType(balanceOrderInfoDto.getPayType());//支付方式
        balanceOrderDto.setBalanceConvertPercent(balanceOrderInfoDto.getBalanceConvertPercent());//余额：现金比例
        balanceOrderDto.setServerCharge(balanceOrderInfoDto.getServerCharge());//平台手术费比例
        balanceOrderDto.setSubsidy(balanceOrderInfoDto.getSubsidy());//补贴比例
        balanceOrderDto.setOrderNum(balanceOrderInfoDto.getOrderNum());//实际交易总额（剩余交易额）
        balanceOrderDto.setOrderType(BalanceOrderBillConstants.ORDER_TYPE_SELL);//卖单类型
        balanceOrderDto.setOrderStatus(BalanceOrderBillConstants.ORDER_STATUS_NEW);//订单状态
        balanceOrderDto.setModifyTime(new Date()); //修改时间
        // 修改卖单
        BalanceOrder balanceOrder = new BalanceOrder();
        BeanUtils.copyProperties(balanceOrderDto, balanceOrder);
        int insertNumer = balanceOrderMapper.updateByPrimaryKeySelective(balanceOrder);
        return insertNumer > 0 ? true : false;
    }

    /**
     * ####################huangxl###############
     * **************后台系统接口*************
     */
    @Override
    public List<BalanceOrderOfAllListDto> listForAll(BalanceOrderOfAllConditionDto condition) {
        return balanceOrderMapper.listForAll(condition);
    }


    /**
     * YEHAO
     *
     *
     */
    /**
     * 创建订单的资产参数判断
     *
     * @param orderTotalNum 交易数量
     * @param limitNumMax   交易上限
     * @param limitNumMin   交易下限
     */
    private void judgeCreateOrder(BigDecimal orderTotalNum, BigDecimal limitNumMax, BigDecimal limitNumMin, BigDecimal balanceConvertPercent) {
        BigDecimal limitNumMinNumMin = BigDecimal.valueOf(1);
        ;
        BigDecimal balanceConvertPercentMin = BigDecimal.valueOf(0.5);
        try {
            limitNumMinNumMin = new BigDecimal(informationMapper.selectByDateName(InformationConstants.DATA_NAME_BALANCE_ORDER_MIN_NUM, 1).getDataValue());
//            balanceConvertPercentMin = new BigDecimal(0.5);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (limitNumMin.compareTo(limitNumMinNumMin) == -1) {  //判断 交易下限 不能小于 limitNumMinMinNum
            BalanceOrderResultEnums balanceOrderResultEnums = BalanceOrderResultEnums.FLOW_BALANCE_MIN_ERROR;
            balanceOrderResultEnums.setMsg("交易限额的 '最小限额' 不能小于 " + limitNumMinNumMin);
            throw new BalanceOrderException(balanceOrderResultEnums);

        } else if (limitNumMin.compareTo(limitNumMax) == 1) {  //判断 交易下限 是否大于  交易上限

            throw new BalanceOrderException(BalanceOrderResultEnums.FLOW_BALANCE_MAX_ERROR);

        } else if (limitNumMax.compareTo(orderTotalNum) == 1) { //判断 交易上限 是否大于 交易总额

            throw new BalanceOrderException(BalanceOrderResultEnums.FLOW_BALANCE_ORDERNUM_ERROR);

        } else if (balanceConvertPercent.compareTo(balanceConvertPercentMin) == -1) { //判断 交易单价 是否小于 交易单价最低限价

            BalanceOrderResultEnums balanceOrderResultEnums = BalanceOrderResultEnums.BALANCE_CONVERT_PERCENT_ERROR;
            balanceOrderResultEnums.setMsg("交易单价最低限价：" + balanceConvertPercentMin + " CNY");
            throw new BalanceOrderException(balanceOrderResultEnums);

        }
    }
}
