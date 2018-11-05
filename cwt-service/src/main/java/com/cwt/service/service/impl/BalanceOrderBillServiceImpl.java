package com.cwt.service.service.impl;

import com.cwt.common.enums.BaseResultEnums;
import com.cwt.common.enums.WalletResultEnums;
import com.cwt.common.exception.*;
import com.cwt.common.enums.BalanceOrderResultEnums;
import com.cwt.common.util.BalanceComputeUtils;
import com.cwt.common.util.ExceptionPreconditionUtils;
import com.cwt.common.util.MD5Utils;
import com.cwt.domain.constant.BalanceOrderBillConstants;
import com.cwt.domain.constant.WalletBillConstants;
import com.cwt.domain.dto.balance.*;
import com.cwt.domain.dto.user.UserDto;
import com.cwt.domain.dto.user.UserTransactionInfoDto;
import com.cwt.domain.dto.wallet.WalletBalanceRecordDto;
import com.cwt.domain.dto.wallet.WalletDto;
import com.cwt.domain.entity.*;
import com.cwt.persistent.mapper.*;
import com.cwt.service.service.*;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("BalanceOrderBillServiceBill")
public class BalanceOrderBillServiceImpl implements BalanceOrderBillService {

    @Autowired
    private BalanceOrderBillMapper balanceOrderBillMapper;
    @Autowired
    private BalanceOrderMapper balanceOrderMapper;
    @Autowired
    private BalanceOrderService balanceOrderService;
    @Autowired
    private UserService userService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private UserTransactionInfoMapper userTransactionInfoMapper;
    @Autowired
    private WalletBalanceRecordService walletBalanceRecordService;
    @Autowired
    private WalletBalanceRecordMapper walletBalanceRecordMapper;
    @Autowired
    private AppealMapper appealMapper;

    /***
     * 根据交易类型、交易状态分页查询交易信息
     * @param userId
     * @param orderType
     * @param billStatus
     * @return
     */
    @Override
    public List<GetBalanceOrderBillByConditionDto> listByOrderTypeAndBillStatus(String userId, Integer orderType, Integer billStatus, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<GetBalanceOrderBillByConditionDto> getBalanceOrderByConditionDtos = balanceOrderBillMapper.listByOrderTypeAndBillStatus(userId, orderType, billStatus);
        for (GetBalanceOrderBillByConditionDto getBalanceOrderByConditionDto : getBalanceOrderByConditionDtos) {
            //如果当前记录中，用户是发布者角色，那么返回的将返回的订单类型取反  例如（买 返回 卖）
            if (getBalanceOrderByConditionDto.getOrderUserId().equals(userId)) {
                //取反
                getBalanceOrderByConditionDto.setOrderType(-1 * getBalanceOrderByConditionDto.getOrderType());
            }
        }
        return getBalanceOrderByConditionDtos;
    }

    //卖家点击下一步，将用户状态和订单状态设置为进行中
    //已不需要该功能
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void updateOrderBillStatus(String id, String userId) {
//        //订单信息
//        BalanceOrderBill balanceOrderBill = balanceOrderBillMapper.selectByPrimaryKey(id);
//        //交易者信息
//        UserDto user = userService.getById(userId);
//        //判断交易订单是否存在
//        ExceptionPreconditionUtils.checkNotNull(balanceOrderBill,
//                new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER));
//        //判断发布者是否存在
//        ExceptionPreconditionUtils.checkNotNull(user,
//                new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER_USER));
//        //如果当前订单状态等于进行中，提示是买家点击了确认付款改变了状态
//        if (balanceOrderBill.getBillStatus() == BalanceOrderBillConstants.BILL_STATUS_AFFIRM) {
//            throw new BalanceOrderException(BalanceOrderResultEnums.BUYER_NEXT);
//        }
//        //判断当前操作用户是发布者还是交易者
//        if (balanceOrderBill.getOrderUserId().equals(userId)) {
//            //将发布者状态设置为进行中
//            balanceOrderBill.setOptUserStatus(BalanceOrderBillConstants.USER_STATUS_AFFIRM);//将交易用户状态变成进行中
//        } else if (balanceOrderBill.getOptUserId().equals(userId)) {
//            //将交易者者状态设置为进行中
//            balanceOrderBill.setOrderUserStatus(BalanceOrderBillConstants.USER_STATUS_AFFIRM);//将发布用户状态变成进行中
//        } else {
//            //如果操作用户不是交易者或者发布者，抛出异常
//            throw new BalanceOrderException(BalanceOrderResultEnums.USER_APPEAL_ERROR);
//        }
//        //将订单状态设置为进行中
//        balanceOrderBill.setBillStatus(BalanceOrderBillConstants.BILL_STATUS_AFFIRM);
//        balanceOrderBill.setModifyTime(new Date());//设置修改时间
//        //更新订单状态
//        balanceOrderBillMapper.updateByPrimaryKeySelective(balanceOrderBill);
    }

    /***
     * 下单确认操作
     * @param id 发布订单id
     * @param orderUserId 发布订单用户id
     * @param optUserId 操作订单用户id
     * @param moneyAmount 金额数量
     * @param amount 余额数量
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public BalanceOrderBill headerOrderConfirmation(String id, String orderUserId, String optUserId, BigDecimal moneyAmount, BigDecimal amount) {
        //判断发布订单是否为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(id,
                new BalanceOrderException(BalanceOrderResultEnums.ORDER_ID_NULL));
        //判断发布用户id是否为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(orderUserId,
                new BalanceOrderException(BalanceOrderResultEnums.ORDER_USERID_NULL));
        //判断操作用户id是否为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(orderUserId,
                new BalanceOrderException(BalanceOrderResultEnums.OPT_USERID_NULL));
        //判断金额数量是否为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(orderUserId,
                new BalanceOrderException(BalanceOrderResultEnums.MONEY_AMOUNT_NULL));
        //判断余额数量是否为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(orderUserId,
                new BalanceOrderException(BalanceOrderResultEnums.AMOUNT_NULL));

        //发布订单信息
        BalanceOrderDto orderDto = balanceOrderService.getById(id);
        //判断发布订单是否存在
        ExceptionPreconditionUtils.checkNotNull(orderDto,
                new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER));
        //判断发布订单状态是否为进行中
        if (orderDto.getOrderStatus() != BalanceOrderBillConstants.ORDER_STATUS_UNDERWAY
                && orderDto.getOrderStatus() != BalanceOrderBillConstants.ORDER_STATUS_NEW
                && orderDto.getOrderStatus() != BalanceOrderBillConstants.ORDER_STATUS_TRADING) {
            throw new BalanceOrderException(BalanceOrderResultEnums.ORDER_NO_AFFIRM);
        }
        //判断剩余的余额是否充足
        if (orderDto.getOrderNum().compareTo(orderDto.getLimitNumMin()) < 0) {
            throw new BalanceOrderException(BalanceOrderResultEnums.AMOUNT_INSUFFICIENT);
        }
        //判断购买/卖出余额是否超过当前剩余余额
        if (amount.compareTo(orderDto.getOrderNum()) > 0) {
            throw new BalanceOrderException(BalanceOrderResultEnums.AMOUNT_MORE_THAN_BALANCE);
        }
        //判断购买/卖出余额是否不符合交易限额
        if (amount.compareTo(orderDto.getLimitNumMin()) < 0 || amount.compareTo(orderDto.getLimitNumMax()) > 0) {
            throw new BalanceOrderException(BalanceOrderResultEnums.AMOUNT_INCONFORMITY);
        }
        //发布用户信息
        UserDto orderUser = userService.getById(orderUserId);
        //判断发起用户是否存在
        ExceptionPreconditionUtils.checkNotNull(orderUser,
                new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER_USER));
        //操作用户信息
        UserDto optUser = userService.getById(optUserId);
        //判断操作用户是否存在
        ExceptionPreconditionUtils.checkNotNull(optUser,
                new BalanceOrderException(BalanceOrderResultEnums.NO_OPT_USER));

        //创建交易订单信息实例
        BalanceOrderBill bill = new BalanceOrderBill();
        bill.setId(UUID.randomUUID().toString());//交易订单id
        bill.setOrderId(id);//发布订单id
        bill.setOrderUserId(orderUserId);//发布用户id
        bill.setOrderUserStatus(BalanceOrderBillConstants.USER_STATUS_PENDING);//设置发布用户状态为待处理
        bill.setOptUserId(optUserId);//操作用户id
        bill.setOptUserStatus(BalanceOrderBillConstants.USER_STATUS_PENDING);//设置操作用户状态为待处理

        //如果发布订单类型是买单
        if (orderDto.getOrderType() == BalanceOrderBillConstants.ORDER_TYPE_BUY) {
            //判断交易者是否绑定了总单对应的交易方式
            int payType = orderDto.getPayType();//总单的交易方式
            if (payType == BalanceOrderBillConstants.PAY_TYPE_WECHAT) {
                if (StringUtils.isEmpty(optUser.getPaymentMethodWx())) {
                    throw new BaseException("500", "你还没用绑定微信支付方式，无法出售");
                }
            }
            if (payType == BalanceOrderBillConstants.PAY_TYPE_BANK) {
                if (StringUtils.isEmpty(optUser.getPaymentMethodBankcard())) {
                    throw new BaseException("500", "你还没用绑定银行卡支付方式，无法出售");
                }
            }
            if (payType == BalanceOrderBillConstants.PAY_TYPE_ALIPAY) {
                if (StringUtils.isEmpty(optUser.getPaymentMethodZfb())) {
                    throw new BaseException("500", "你还没用绑定支付宝支付方式，无法出售");
                }
            }
            //设置交易订单交易类型 卖
            bill.setOrderType(BalanceOrderBillConstants.ORDER_TYPE_SELL);
            WalletDto walletDto = walletService.getByUserId(optUserId);
            //卖出，先扣钱
            Wallet wallet = new Wallet();
            wallet.setId(walletDto.getId());//设置钱包id
            wallet.setUserId(optUserId);//设置用户id
            //检查钱包是否为空
            ExceptionPreconditionUtils.checkNotNull(wallet,
                    new WalletExeption(WalletResultEnums.NO_WALLET));
            //实际扣除余额 = 余额/(1-补贴-手续费)
            BigDecimal sellTotal = amount.divide(BigDecimal.valueOf(1).subtract(orderDto.getSubsidy()).subtract(orderDto.getServerCharge()), 4, BigDecimal.ROUND_HALF_UP);
            //检测钱包余额是否足够出售
            WalletExceptionPreconditionUtils.checkNotBeyond(sellTotal, walletDto.getFlowBalance(),
                    new WalletExeption(WalletResultEnums.WALLET_BALANCE_NULL));
            wallet.setFlowBalance(walletDto.getFlowBalance().subtract(sellTotal));//减去卖出的余额，更新钱包余额
            walletMapper.updateByPrimaryKeySelective(wallet);
            //添加余额变动记录
            WalletBalanceRecord walletBalanceRecord = new WalletBalanceRecord();
            walletBalanceRecord.setId(MD5Utils.MD5(UUID.randomUUID().toString()));//记录id
            walletBalanceRecord.setTargetUserId(optUserId);//余额变更用户id
            walletBalanceRecord.setOptUserId(optUserId);//操作的用户id
            walletBalanceRecord.setOptUserShowMsg(optUser.getUserName());
            walletBalanceRecord.setAmount(sellTotal);//设置变动金额
            walletBalanceRecord.setServiceCharge(sellTotal.multiply(orderDto.getServerCharge()));//手续费=实际扣除余额 * 手续费比率
            walletBalanceRecord.setSubsidy(sellTotal.multiply(orderDto.getSubsidy()));//补贴=实际扣除余额 * 手续费比率
            walletBalanceRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_SUB);//变动类型 减少
            walletBalanceRecord.setChangeType(WalletBillConstants.CHANGE_TYPE_DEAL);//途径：余额交易
            walletBalanceRecord.setCreateTime(new Date());//创建时间
            walletBalanceRecordMapper.insertSelective(walletBalanceRecord);//保存余额变更记录
        } else {
            //设置交易订单交易类型 买
            bill.setOrderType(BalanceOrderBillConstants.ORDER_TYPE_BUY);
        }
        //计算购买实际获得的收益
        BigDecimal amountReal = BalanceComputeUtils.actualIncome(amount, orderDto.getOrderTotalNum(), orderDto.getServerCharge(), orderDto.getSubsidy());
        //设置实际获得收益
        bill.setAmountReal(amountReal);
        bill.setMoneyAmount(moneyAmount);//设置金额数量
        bill.setAmount(amount);//设置余额数量
        bill.setBillStatus(BalanceOrderBillConstants.BILL_STATUS_PENDING);//设置交易订单状态为待处理
        bill.setCreateTime(new Date());//设置创建时间
        //保存订单
        balanceOrderBillMapper.insertSelective(bill);

        //更新发布订单的数据
        BalanceOrder order = new BalanceOrder();
        order.setId(orderDto.getId());//发布订单id
        order.setOrderNum(orderDto.getOrderNum().subtract(amount));//订单剩余余额
        order.setOrderStatus(BalanceOrderBillConstants.ORDER_STATUS_TRADING);//将订单设置为交易中
        order.setModifyTime(new Date());//修改时间
        balanceOrderMapper.updateByPrimaryKeySelective(order);
        return bill;
    }

    /***
     * 放弃买单
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void headerAbandonBuy(String id) {
        //交易订单信息
        BalanceOrderBill balanceOrderBill = balanceOrderBillMapper.selectByPrimaryKey(id);
        //发布订单信息
        BalanceOrder balanceOrder = balanceOrderMapper.selectByPrimaryKey(balanceOrderBill.getOrderId());
        //判断交易订单是否存在
        ExceptionPreconditionUtils.checkNotNull(balanceOrderBill,
                new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER));
        //判断发布订单是否存在
        ExceptionPreconditionUtils.checkNotNull(balanceOrder,
                new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER));
        //如果订单状态不是新建状态，则无法放弃
        if (balanceOrderBill.getBillStatus() != BalanceOrderBillConstants.BILL_STATUS_PENDING) {
            throw new BalanceOrderException(BalanceOrderResultEnums.ORDER_TYPE_CHANGE);
        }
        //设置交易订单为失效
        balanceOrderBill.setBillStatus(BalanceOrderBillConstants.BILL_STATUS_LOSE_EFFICACY);
        balanceOrderBill.setModifyTime(new Date());
        balanceOrderBillMapper.updateByPrimaryKeySelective(balanceOrderBill);
        //将交易订单的 余额数量 返回到发布订单的 剩余余额中
        balanceOrder.setOrderNum(balanceOrder.getOrderNum().add(balanceOrderBill.getAmount()));
        BigDecimal total = balanceOrder.getOrderTotalNum();
        //如果总单是卖单，需要比较的总额从 totalNum 变成 totalNum * 1-手续费-补贴
        if (balanceOrder.getOrderType() == BalanceOrderBillConstants.ORDER_TYPE_SELL) {
            total = total.multiply(BigDecimal.valueOf(1).subtract(balanceOrder.getServerCharge()).subtract(balanceOrder.getSubsidy()));
        }

        //查询发布订单伞下的所有交易订单的状态，返回有多少订单不是已完成或者失效状态
        int count = balanceOrderBillMapper.countNoInStatusByOrderId(
                balanceOrder.getId(),
                BalanceOrderBillConstants.BILL_STATUS_FINISH,
                BalanceOrderBillConstants.BILL_STATUS_LOSE_EFFICACY);
        //如果不等于1，代表当前总单伞下还有未结束的订单
        if (count != 1) {//这里不为0 的原因是此时放弃的这条订单到这步还没有更新
            balanceOrder.setOrderStatus(BalanceOrderBillConstants.ORDER_STATUS_TRADING);//将订单设置成交易中
        } else {
            //如果撤单后总单剩余等于总额，设置总单状态为待处理否则为进行中
            if (balanceOrder.getOrderNum().compareTo(total) == 0) {
                balanceOrder.setOrderStatus(BalanceOrderBillConstants.ORDER_STATUS_NEW);//将订单设置成待处理
            } else {
                balanceOrder.setOrderStatus(BalanceOrderBillConstants.ORDER_STATUS_UNDERWAY);//将订单设置成进行中
            }
        }
        balanceOrder.setModifyTime(new Date());//设置修改时间
        balanceOrderMapper.updateByPrimaryKeySelective(balanceOrder);
    }

    /***
     * 放弃卖单
     * @param id
     */
    @Override
    public void headerAbandonSell(String id) {
        //交易订单信息
        BalanceOrderBill balanceOrderBill = balanceOrderBillMapper.selectByPrimaryKey(id);
        //发布订单信息
        BalanceOrder balanceOrder = balanceOrderMapper.selectByPrimaryKey(balanceOrderBill.getOrderId());
        //判断交易订单是否存在
        ExceptionPreconditionUtils.checkNotNull(balanceOrderBill,
                new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER));
        //判断发布订单是否存在
        ExceptionPreconditionUtils.checkNotNull(balanceOrder,
                new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER));
        //如果订单状态不是新建状态，则无法放弃
        if (balanceOrderBill.getBillStatus() != BalanceOrderBillConstants.BILL_STATUS_PENDING) {
            throw new BalanceOrderException(BalanceOrderResultEnums.ORDER_TYPE_CHANGE);
        }
        //当前时间
        Date now = new Date();
        //返回给卖家的实际余额
        BigDecimal freeBack = BalanceComputeUtils.freeBack(
                balanceOrderBill.getAmount(), balanceOrder.getOrderTotalNum(), balanceOrder.getServerCharge(), balanceOrder.getSubsidy());

        //交易订单是卖单时，撤单后获得余额的用户
        String optUserId = balanceOrderBill.getOptUserId();

        //操作用户信息
        UserDto optUser = userService.getById(optUserId);

        //当前用户钱包
        WalletDto walletDto = walletMapper.selectByUserId(optUserId);
        Wallet wallet = new Wallet();
        wallet.setId(walletDto.getId());
        wallet.setModifyTime(now);//修改时间
        wallet.setVersion(walletDto.getVersion() + 1);//版本
        wallet.setFlowBalance(walletDto.getFlowBalance().add(freeBack));//更新返回余额

        //添加余额变动记录
        WalletBalanceRecord walletBalanceRecord = new WalletBalanceRecord();
        walletBalanceRecord.setId(MD5Utils.MD5(UUID.randomUUID().toString()));//记录id
        walletBalanceRecord.setTargetUserId(optUserId);//余额变更用户id
        walletBalanceRecord.setOptUserId(optUserId);//操作的用户id
        walletBalanceRecord.setOptUserShowMsg(optUser.getUserName());
        walletBalanceRecord.setAmount(freeBack);//设置变动金额
        walletBalanceRecord.setServiceCharge(freeBack.multiply(balanceOrder.getServerCharge()));//手续费=实际扣除余额 * 手续费比率
        walletBalanceRecord.setSubsidy(freeBack.multiply(balanceOrder.getSubsidy()));//补贴=实际扣除余额 * 手续费比率
        walletBalanceRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//变动类型 增加
        walletBalanceRecord.setChangeType(WalletBillConstants.CHANGE_TYPE_ROLL_BACK);//途径：订单失效时返回余额
        walletBalanceRecord.setCreateTime(now);//创建时间

        //将出售的数量返回到总单剩余数量中
        balanceOrder.setOrderNum(balanceOrder.getOrderNum().add(balanceOrderBill.getAmount()));
        balanceOrder.setModifyTime(now);

        //查询发布订单伞下的所有交易订单的状态，返回有多少订单不是已完成或者失效状态
        int count = balanceOrderBillMapper.countNoInStatusByOrderId(
                balanceOrder.getId(),
                BalanceOrderBillConstants.BILL_STATUS_FINISH,
                BalanceOrderBillConstants.BILL_STATUS_LOSE_EFFICACY);
        //如果不等于1，代表当前总单伞下还有未结束的订单
        if (count != 1) {//这里不为0 的原因是此时放弃的这条订单到这步还没有更新
            balanceOrder.setOrderStatus(BalanceOrderBillConstants.ORDER_STATUS_TRADING);//将订单设置成交易中
        } else {
            //如果撤单后总单剩余等于总额，设置总单状态为待处理否则为进行中
            if (balanceOrder.getOrderNum().compareTo(balanceOrder.getOrderTotalNum()) == 0) {
                balanceOrder.setOrderStatus(BalanceOrderBillConstants.ORDER_STATUS_NEW);//将订单设置成待处理
            } else {
                balanceOrder.setOrderStatus(BalanceOrderBillConstants.ORDER_STATUS_UNDERWAY);//将订单设置成进行中
            }
        }

        //设置交易订单为失效
        balanceOrderBill.setBillStatus(BalanceOrderBillConstants.BILL_STATUS_LOSE_EFFICACY);
        balanceOrderBill.setModifyTime(now);

        //持久化数据
        walletMapper.updateByPrimaryKeySelective(wallet);//更新钱包
        walletBalanceRecordMapper.insertSelective(walletBalanceRecord);//保存余额变更记录
        balanceOrderBillMapper.updateByPrimaryKeySelective(balanceOrderBill);//更新交易订单数据
        balanceOrderMapper.updateByPrimaryKeySelective(balanceOrder);//更新总单信息
    }

    /***
     * 订单自动失效时根据订单类型调用相应接口
     * @param orderBillId
     */
    @Override
    public void headerAutoFail(String orderBillId) {
        BalanceOrderBill balanceOrder = balanceOrderBillMapper.selectByPrimaryKey(orderBillId);
        Integer orderType = balanceOrder.getOrderType();
        //判断交易订单是买/卖单，调用相应的接口
        if (orderType == BalanceOrderBillConstants.ORDER_TYPE_BUY) {
            headerAbandonBuy(orderBillId);//调用放弃买单接口
        } else if (orderType == BalanceOrderBillConstants.ORDER_TYPE_SELL) {
            headerAbandonSell(orderBillId);//调用放弃卖单接口
        }
    }

    //买单确认结算
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void headerBuyAffirm(String id, String passWord, String userId) {
        //判断支付密码是否为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(passWord,
                new BaseException(BaseResultEnums.PAYPASSWORD_NULL));
        //判断id是否为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(id,
                new BalanceOrderException(BalanceOrderResultEnums.ORDER_ID_NULL));
        //将密码加密后再跟数据库的M5D密码比较
        String md5Password = MD5Utils.MD5(passWord);
        //订单信息
        BalanceOrderBill balanceOrderBill = balanceOrderBillMapper.selectByPrimaryKey(id);
        //发布订单信息
        BalanceOrderDto orderDto = balanceOrderService.getById(balanceOrderBill.getOrderId());
        //发布者信息
        UserDto orderUser = userService.getById(balanceOrderBill.getOrderUserId());
        //交易者信息
        UserDto optUser = userService.getById(balanceOrderBill.getOptUserId());

        /************校验信息************/
        //判断交易订单是否存在
        ExceptionPreconditionUtils.checkNotNull(balanceOrderBill,
                new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER));
        //判断发布订单是否存在
        ExceptionPreconditionUtils.checkNotNull(orderDto,
                new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER));
        //判断发布者是否存在
        ExceptionPreconditionUtils.checkNotNull(orderUser,
                new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER_USER));
        //判断交易者是否存在
        ExceptionPreconditionUtils.checkNotNull(optUser,
                new BalanceOrderException(BalanceOrderResultEnums.NO_OPT_USER));
        //如果订单不等于待处理状态，则显示提示信息
        if (balanceOrderBill.getBillStatus() != BalanceOrderBillConstants.BILL_STATUS_PENDING) {
            throw new BalanceOrderException(BalanceOrderResultEnums.BILL_CHANGE);
        }
        /************校验信息************/

        //判断是发布者还是交易者确认付款
        if (balanceOrderBill.getOptUserId().equals(userId)) {
            //判断opt用户账号是否设置了支付密码
            ExceptionPreconditionUtils.checkStringArgumentsNotBlank(optUser.getPaymentPassword(),
                    new BaseException(BaseResultEnums.NO_PAYPASSWORD));
            //判断opt用户支付密码是否正确
            ExceptionPreconditionUtils.checkArguments(optUser.getPaymentPassword().equals(md5Password),
                    new BaseException(BaseResultEnums.PAYPASSWORD_ERROR));
            //如果交易订单交易用户是当前user，那么确认付款的是opt用户
            balanceOrderBill.setOptUserStatus(BalanceOrderBillConstants.USER_STATUS_AFFIRM);
        } else if (balanceOrderBill.getOrderUserId().equals(userId)) {
            //判断order用户账号是否设置了支付密码
            ExceptionPreconditionUtils.checkStringArgumentsNotBlank(orderUser.getPaymentPassword(),
                    new BaseException(BaseResultEnums.NO_PAYPASSWORD));
            //判断order用户支付密码是否正确
            ExceptionPreconditionUtils.checkArguments(orderUser.getPaymentPassword().equals(md5Password),
                    new BaseException(BaseResultEnums.PAYPASSWORD_ERROR));
            //如果交易订单发布用户是当前user，那么确认付款的是order用户
            balanceOrderBill.setOrderUserStatus(BalanceOrderBillConstants.USER_STATUS_AFFIRM);
        } else {
            //如果发布者和交易者id都不是当前用户，说明该订单与当前用户没有关联，抛出异常
            throw new BalanceOrderException(BalanceOrderResultEnums.USER_ERROR);
        }
        //设置订单状态为进行中
        balanceOrderBill.setBillStatus(BalanceOrderBillConstants.BILL_STATUS_AFFIRM);
        //设置修改订单时间
        balanceOrderBill.setModifyTime(new Date());
        //更新订单信息
        balanceOrderBillMapper.updateByPrimaryKeySelective(balanceOrderBill);
    }

    //卖单确认结算
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void headerSellAffirm(String id, String passWord, String userId) {
        //判断支付密码是否为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(passWord,
                new BaseException(BaseResultEnums.PAYPASSWORD_NULL));
        //判断id是否为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(id,
                new BalanceOrderException(BalanceOrderResultEnums.ORDER_ID_NULL));
        //将密码加密后再跟数据库的M5D密码比较
        String md5Password = MD5Utils.MD5(passWord);
        //订单信息
        BalanceOrderBill balanceOrderBill = balanceOrderBillMapper.selectByPrimaryKey(id);
        //发布者信息
        UserDto orderUser = userService.getById(balanceOrderBill.getOrderUserId());
        //交易者信息
        UserDto optUser = userService.getById(balanceOrderBill.getOptUserId());
        //发布订单信息
        BalanceOrderDto orderDto = balanceOrderService.getById(balanceOrderBill.getOrderId());

        /************校验信息************/
        //判断交易订单是否存在
        ExceptionPreconditionUtils.checkNotNull(balanceOrderBill,
                new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER));
        //判断发布者是否存在
        ExceptionPreconditionUtils.checkNotNull(orderUser,
                new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER_USER));
        //判断交易者是否存在
        ExceptionPreconditionUtils.checkNotNull(optUser,
                new BalanceOrderException(BalanceOrderResultEnums.NO_OPT_USER));
        //判断发布订单是否存在
        ExceptionPreconditionUtils.checkNotNull(orderDto,
                new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER));
        //如果订单不等于进行中状态，则显示提示信息
        if (balanceOrderBill.getBillStatus() != BalanceOrderBillConstants.BILL_STATUS_AFFIRM) {
            throw new BalanceOrderException(BalanceOrderResultEnums.BILL_CHANGE);
        }
        /************校验信息************/

        //判断是发布者还是交易者确认收款
        if (balanceOrderBill.getOrderUserId().equals(userId)) {
            //判断order用户账号是否设置了支付密码
            ExceptionPreconditionUtils.checkStringArgumentsNotBlank(orderUser.getPaymentPassword(),
                    new BaseException(BaseResultEnums.NO_PAYPASSWORD));
            //判断order用户支付密码是否正确
            ExceptionPreconditionUtils.checkArguments(orderUser.getPaymentPassword().equals(md5Password),
                    new BaseException(BaseResultEnums.PAYPASSWORD_ERROR));
            //如果交易订单发布用户是当前user，那么确认收款的是order用户
            balanceOrderBill.setOrderUserStatus(BalanceOrderBillConstants.USER_STATUS_AFFIRM);
            //检查双方是否确认交易
            doubleCheck(balanceOrderBill, balanceOrderBill.getOptUserId());
        } else if (balanceOrderBill.getOptUserId().equals(userId)) {
            //判断opt用户账号是否设置了支付密码
            ExceptionPreconditionUtils.checkStringArgumentsNotBlank(optUser.getPaymentPassword(),
                    new BaseException(BaseResultEnums.NO_PAYPASSWORD));
            //判断opt用户支付密码是否正确
            ExceptionPreconditionUtils.checkArguments(optUser.getPaymentPassword().equals(md5Password),
                    new BaseException(BaseResultEnums.PAYPASSWORD_ERROR));
            //如果交易订单交易用户是当前user，那么确认收款的是opt用户
            balanceOrderBill.setOptUserStatus(BalanceOrderBillConstants.USER_STATUS_AFFIRM);
            //检查双方是否确认交易
            doubleCheck(balanceOrderBill, balanceOrderBill.getOrderUserId());
        } else {
            //如果发布者和交易者id都不是当前用户，说明该订单与当前用户没有关联，抛出异常
            throw new BalanceOrderException(BalanceOrderResultEnums.USER_ERROR);
        }
    }

    //检查双方是否确认交易
    private void doubleCheck(BalanceOrderBill balanceOrderBill, String buyUserId) {
        //判断发布者和交易者双方是否确认收款
        boolean order = balanceOrderBill.getOrderUserStatus() == BalanceOrderBillConstants.USER_STATUS_AFFIRM;
        boolean opt = balanceOrderBill.getOptUserStatus() == BalanceOrderBillConstants.USER_STATUS_AFFIRM;
        if (order == opt) {
            //发布订单信息
            BalanceOrder balanceOrder = balanceOrderMapper.selectByPrimaryKey(balanceOrderBill.getOrderId());
            //判断发布订单是否存在
            ExceptionPreconditionUtils.checkNotNull(balanceOrder,
                    new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER));

            //操作用户信息
            UserDto optUser = userService.getById(buyUserId);
            //判断操作用户是否存在
            ExceptionPreconditionUtils.checkNotNull(optUser,
                    new BalanceOrderException(BalanceOrderResultEnums.NO_OPT_USER));

            //获取钱包信息
            WalletDto walletDto = walletMapper.selectByUserId(buyUserId);
            Wallet wallet = new Wallet();
            //判断钱包是否存在
            ExceptionPreconditionUtils.checkNotNull(walletDto,
                    new WalletExeption(WalletResultEnums.NO_WALLET));
            wallet.setId(walletDto.getId());//设置钱包id
            wallet.setUserId(buyUserId);//设置用户id
            wallet.setModifyTime(new Date());//设置修改时间
            wallet.setVersion(walletDto.getVersion() + 1);//版本加一
            wallet.setFlowBalance(walletDto.getFlowBalance().add(balanceOrderBill.getAmountReal()));//将实际买入加到用户钱包中

            //插入余额变更记录
            WalletBalanceRecord walletBalanceRecord = new WalletBalanceRecord();
            walletBalanceRecord.setId(MD5Utils.MD5(UUID.randomUUID().toString()));//记录id
            walletBalanceRecord.setChangeType(WalletBillConstants.CHANGE_TYPE_DEAL);//途径：余额交易
            walletBalanceRecord.setCreateTime(new Date());//创建时间
            walletBalanceRecord.setTargetUserId(buyUserId);//余额变更用户id
            walletBalanceRecord.setOptUserId(buyUserId);//操作的用户id
            walletBalanceRecord.setOptUserShowMsg(optUser.getUserName());
            walletBalanceRecord.setAmount(balanceOrderBill.getAmountReal());//变更的数量
            walletBalanceRecord.setSubsidy(balanceOrderBill.getAmountReal().subtract(balanceOrderBill.getAmount()));//发布者补贴
            walletBalanceRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加

            //交易订单状态设置为已完成
            balanceOrderBill.setBillStatus(BalanceOrderBillConstants.BILL_STATUS_FINISH);
            //设置修改时间
            balanceOrderBill.setModifyTime(new Date());
            //保存余额变更记录
            walletBalanceRecordMapper.insertSelective(walletBalanceRecord);
            //保存订单记录
            balanceOrderBillMapper.updateByPrimaryKeySelective(balanceOrderBill);
            //设置余额交易记录
            UserTransactionInfoDto userTransactionInfoDto = userTransactionInfoMapper.selectByUserId(balanceOrderBill.getOrderUserId());
            UserTransactionInfo userTransactionInfo = new UserTransactionInfo();
            //交易量加一
            userTransactionInfo.setTransationTotal(userTransactionInfoDto.getTransationTotal() + 1);
            //更新余额交易记录
            userTransactionInfo.setId(userTransactionInfoDto.getId());//设置记录id
            userTransactionInfo.setUserId(balanceOrderBill.getOrderUserId());//设置用户id
            userTransactionInfo.setModifyTime(new Date());//设置修改时间
            userTransactionInfoMapper.updateByPrimaryKeySelective(userTransactionInfo);
            //更新钱包
            walletMapper.updateByPrimaryKeySelective(wallet);
            //判断发布订单总额是否充足
            checkTotalNumInsufficient(balanceOrder);
        }
    }

    //判断发布订单总额是否充足
    private void checkTotalNumInsufficient(BalanceOrder balanceOrder) {

        /***判断伞下所有订单的状态是否都已结束***/
        //查询发布订单伞下的所有交易订单的状态，返回有多少订单不是已完成或者失效状态
        int count = balanceOrderBillMapper.countNoInStatusByOrderId(
                balanceOrder.getId(),
                BalanceOrderBillConstants.BILL_STATUS_FINISH,
                BalanceOrderBillConstants.BILL_STATUS_LOSE_EFFICACY);
        //如果不等于0，代表当前总单伞下还有未结束的订单，返回false结束当前方法
        if (count != 0) {
            return;
        }
        //如果剩余的余额不足以交易    ->>>>>   如果小于最小限额，防止空指针或者算数异常
        if (balanceOrder.getOrderNum().compareTo(balanceOrder.getLimitNumMin()) < 0) {
            if (balanceOrder.getOrderType() == BalanceOrderBillConstants.ORDER_TYPE_BUY) {//买单
                balanceOrder.setOrderStatus(BalanceOrderBillConstants.ORDER_STATUS_FINISH);//设置订单状态已完成
                balanceOrderMapper.updateByPrimaryKeySelective(balanceOrder);//更新订单状态
                return;
            } else {//卖单
                if (balanceOrder.getOrderNum().compareTo(new BigDecimal("0.0001")) < 0) {
                    balanceOrder.setOrderStatus(BalanceOrderBillConstants.ORDER_STATUS_FINISH);//设置订单状态已完成
                    balanceOrderMapper.updateByPrimaryKeySelective(balanceOrder);//更新订单状态
                    return;
                }
                //将订单剩下的不能交易的余额返回到发布用户的账户中
                WalletDto walletDto = walletService.getByUserId(balanceOrder.getUserId());
                //判断钱包是否存在
                ExceptionPreconditionUtils.checkNotNull(walletDto,
                        new WalletExeption(WalletResultEnums.NO_WALLET));
                Wallet wallet = new Wallet();
                wallet.setId(walletDto.getId());//设置钱包id
                wallet.setUserId(walletDto.getUserId());//设置用户id
                //计算需要返回的余额
                BigDecimal freeBackBalance = BalanceComputeUtils.freeBack(balanceOrder.getOrderNum(), balanceOrder.getOrderTotalNum(), balanceOrder.getServerCharge(), balanceOrder.getSubsidy());
                //计算手续费
                BigDecimal freeBackServiceCharge = BalanceComputeUtils.realServiceCharge(balanceOrder.getOrderNum(), balanceOrder.getServerCharge(), balanceOrder.getSubsidy());
                //计算补贴
                BigDecimal freeBackSubsidy = BalanceComputeUtils.realSubsidy(balanceOrder.getOrderNum(), balanceOrder.getServerCharge(), balanceOrder.getSubsidy());
                wallet.setFlowBalance(freeBackBalance.add(walletDto.getFlowBalance()));//将返回的余额加到钱包

                //操作用户信息
                UserDto optUser = userService.getById(balanceOrder.getUserId());

                //余额变更记录
                WalletBalanceRecord balanceRecord = new WalletBalanceRecord();
                balanceRecord.setId(UUID.randomUUID().toString());//记录id
                balanceRecord.setTargetUserId(balanceOrder.getUserId());//变更用户
                balanceRecord.setOptUserId(balanceOrder.getUserId());
                balanceRecord.setOptUserShowMsg(optUser.getUserName());
                balanceRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//变更类型：增加
                balanceRecord.setCreateTime(new Date());//创建时间
                balanceRecord.setAmount(freeBackBalance);//实际返回的余额
                balanceRecord.setSubsidy(freeBackSubsidy);//平台扣除给买家的补贴
                balanceRecord.setServiceCharge(freeBackServiceCharge);//平台扣除的手续费
                balanceRecord.setChangeType(WalletBillConstants.CHANGE_TYPE_ROLL_BACK);//余额变更触发事件
                walletBalanceRecordMapper.insertSelective(balanceRecord);//插入余额变更记录
                balanceOrder.setOrderStatus(BalanceOrderBillConstants.ORDER_STATUS_FINISH);//设置订单状态已完成
                balanceOrderMapper.updateByPrimaryKeySelective(balanceOrder);//更新订单状态
                walletMapper.updateByPrimaryKeySelective(wallet);//更新钱包数据
            }
        } else {
            //代表还有足够的余额进行下一步的交易
            balanceOrder.setOrderStatus(BalanceOrderBillConstants.ORDER_STATUS_UNDERWAY);//设置订单状态进行中
            balanceOrderMapper.updateByPrimaryKeySelective(balanceOrder);//更新订单状态
        }
    }

    /***
     * 申诉
     * @param id
     * @param userId
     * @param appealFileName
     */
    @Override
    @Transactional
    public void headerAppeal(String id, String userId, String appealFileName, String appealText) {
        //交易订单信息
        BalanceOrderBill balanceOrderBill = balanceOrderBillMapper.selectByPrimaryKey(id);
        //判断交易订单是否存在
        ExceptionPreconditionUtils.checkNotNull(balanceOrderBill,
                new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER));
        //如果当前订单状态等于申诉中 则不能再次申诉
        if (balanceOrderBill.getBillStatus() == BalanceOrderBillConstants.BILL_STATUS_APPEAL) {
            throw new BalanceOrderException(BalanceOrderResultEnums.BIll_APPEAL_AGAIN);
        }
        //如果当前订单状态等于已完成，则不能点击申诉
        if (balanceOrderBill.getBillStatus() == BalanceOrderBillConstants.BILL_STATUS_FINISH) {
            throw new BalanceOrderException(BalanceOrderResultEnums.BILL_FINNSH);
        }
        //判断是发布者还是交易者申诉
        if (balanceOrderBill.getOrderUserId().equals(userId)) {
            //如果是发布者申诉,则改变发布者状态
            balanceOrderBill.setOrderUserStatus(BalanceOrderBillConstants.USER_STATUS_APPEAL);
        } else if (balanceOrderBill.getOptUserId().equals(userId)) {
            //如果是交易者申诉,则改变交易者状态
            balanceOrderBill.setOptUserStatus(BalanceOrderBillConstants.USER_STATUS_APPEAL);
        } else {
            //如果发布者和交易者id都不是当前用户，说明该订单与当前用户没有关联，抛出异常
            throw new BalanceOrderException(BalanceOrderResultEnums.USER_ERROR);
        }
        //将订单状态设置为申诉
        balanceOrderBill.setBillStatus(BalanceOrderBillConstants.BILL_STATUS_APPEAL);
        //设置修改时间
        balanceOrderBill.setModifyTime(new Date());
        balanceOrderBillMapper.updateByPrimaryKeySelective(balanceOrderBill);

        Appeal appeal = new Appeal();
        appeal.setId(UUID.randomUUID().toString());//申诉记录id
        appeal.setAppealFile(appealFileName);//申诉文件名
        appeal.setAppealExplain(appealText);//申诉描述
        appeal.setCreatDatetime(new Date());//创建时间
        appeal.setUserId(userId);//申诉用户id
        appeal.setBalanceBillId(id);//交易订单记录id
        appeal.setStatus(BalanceOrderBillConstants.APPEAL_STATUS_NEW);//设置订单为新建状态
        appealMapper.insert(appeal);//插入一条新记录
    }


    /***
     * 同意申诉
     * @param appealId 申诉id
     */
    @Override
    @Transactional
    public void headerAgreed2Appeal(String appealId) {
        //申诉记录信息
        Appeal appeal = appealMapper.selectByPrimaryKey(appealId);
        //交易订单信息
        BalanceOrderBill balanceOrderBill = balanceOrderBillMapper.selectByPrimaryKey(appeal.getBalanceBillId());
        //发布订单信息
        BalanceOrder balanceOrder = balanceOrderMapper.selectByPrimaryKey(balanceOrderBill.getOrderId());
        //状态
        String orderUId = balanceOrderBill.getOrderUserId();
        String optUId = balanceOrderBill.getOptUserId();
        //判断交易买单还是交易卖单
        if (balanceOrderBill.getOrderType() == BalanceOrderBillConstants.ORDER_TYPE_BUY) {
            if (balanceOrderBill.getOrderUserStatus() == BalanceOrderBillConstants.USER_STATUS_APPEAL) {
                //发布者->同意申诉->订单失效(更新)
                updatesUserTransationInfoOfAppealForBillFail(balanceOrderBill.getOrderType(), orderUId, optUId);
                updateBillBuyBalanceOfAppealForBillFail(balanceOrderBill, orderUId);
            } else if (balanceOrderBill.getOptUserStatus() == BalanceOrderBillConstants.USER_STATUS_APPEAL) {
                //操作者->同意申诉->订单成功
                updatesUserTransationInfoOfAppealForBillSuccess(balanceOrderBill.getOrderType(), orderUId, optUId);
                updateBillBuyBalanceOfAppealForBillSuccess(balanceOrderBill, optUId);
            }
        } else if (balanceOrderBill.getOrderType() == BalanceOrderBillConstants.ORDER_TYPE_SELL) {
            if (balanceOrderBill.getOrderUserStatus() == BalanceOrderBillConstants.USER_STATUS_APPEAL) {
                //发布者->同意申诉->订单成功
                updatesUserTransationInfoOfAppealForBillSuccess(balanceOrderBill.getOrderType(), orderUId, optUId);
                updateBillBuyBalanceOfAppealForBillSuccess(balanceOrderBill, orderUId);
            } else if (balanceOrderBill.getOptUserStatus() == BalanceOrderBillConstants.USER_STATUS_APPEAL) {
                //操作者->同意申诉->订单失效
                updatesUserTransationInfoOfAppealForBillFail(balanceOrderBill.getOrderType(), orderUId, optUId);
                updateBillBuyBalanceOfAppealForBillFail(balanceOrderBill, optUId);
            }
        }
        //更新申诉记录
        appeal.setStatus(BalanceOrderBillConstants.APPEAL_STATUS_AGREED);
        appeal.setModifyTime(new Date());
        appealMapper.updateByPrimaryKey(appeal);
        //发布订单可以设置为已完成
        //判断发布订单总额是否充足
        //如果为true代表发布订单还有余额可以继续交易
        //如果为false代表发布订单余额不足以交易
        checkTotalNumInsufficient(balanceOrder);
    }

    /***
     * 驳回申诉
     * @param appealId 申诉id
     */
    @Override
    @Transactional
    public void headerReject2Appeal(String appealId) {
        //申诉记录信息
        Appeal appeal = appealMapper.selectByPrimaryKey(appealId);
        //交易订单信息
        BalanceOrderBill balanceOrderBill = balanceOrderBillMapper.selectByPrimaryKey(appeal.getBalanceBillId());
        //发布订单信息
        BalanceOrder balanceOrder = balanceOrderMapper.selectByPrimaryKey(balanceOrderBill.getOrderId());
        //状态
        String orderUId = balanceOrderBill.getOrderUserId();
        String optUId = balanceOrderBill.getOptUserId();
        //判断交易买单还是交易卖单
        if (balanceOrderBill.getOrderType() == BalanceOrderBillConstants.ORDER_TYPE_BUY) {
            if (balanceOrderBill.getOrderUserStatus() == BalanceOrderBillConstants.USER_STATUS_APPEAL) {
                //发布者->驳回申诉->操作者胜诉->订单成功
                updatesUserTransationInfoOfAppealForBillSuccess(balanceOrderBill.getOrderType(), orderUId, optUId);
                updateBillBuyBalanceOfAppealForBillSuccess(balanceOrderBill, optUId);
            } else if (balanceOrderBill.getOptUserStatus() == BalanceOrderBillConstants.USER_STATUS_APPEAL) {
                //操作者->驳回申诉->发布者胜诉->订单失效
                updatesUserTransationInfoOfAppealForBillFail(balanceOrderBill.getOrderType(), orderUId, optUId);
                updateBillBuyBalanceOfAppealForBillFail(balanceOrderBill, orderUId);
            }
        } else if (balanceOrderBill.getOrderType() == BalanceOrderBillConstants.ORDER_TYPE_SELL) {
            if (balanceOrderBill.getOrderUserStatus() == BalanceOrderBillConstants.USER_STATUS_APPEAL) {
                //发布者->驳回申诉->操作者胜诉->订单失效
                updatesUserTransationInfoOfAppealForBillFail(balanceOrderBill.getOrderType(), orderUId, optUId);
                updateBillBuyBalanceOfAppealForBillFail(balanceOrderBill, optUId);
            } else if (balanceOrderBill.getOptUserStatus() == BalanceOrderBillConstants.USER_STATUS_APPEAL) {
                //操作者->驳回申诉->发布者胜诉->订单成功
                updatesUserTransationInfoOfAppealForBillSuccess(balanceOrderBill.getOrderType(), orderUId, optUId);
                updateBillBuyBalanceOfAppealForBillSuccess(balanceOrderBill, orderUId);
            }
        }
        //更新申诉记录
        appeal.setStatus(BalanceOrderBillConstants.APPEAL_STATUS_AGREED);
        appeal.setModifyTime(new Date());
        appealMapper.updateByPrimaryKey(appeal);
        //发布订单可以设置为已完成
        //判断发布订单总额是否充足
        //如果为true代表发布订单还有余额可以继续交易
        //如果为false代表发布订单余额不足以交易
        checkTotalNumInsufficient(balanceOrder);
    }

    /**
     * 通过申诉导致订单状态变为成功时，更新用户交易信息表
     *
     * @param orderType   买单(1)  卖单(-1)
     * @param orderUserId 发布者用户id
     * @param optUserId   操作者用户id
     */
    private void updatesUserTransationInfoOfAppealForBillSuccess(int orderType, String orderUserId, String optUserId) {
        UserTransactionInfoDto orderUserDto = userTransactionInfoMapper.selectByUserId(orderUserId);
        UserTransactionInfoDto optUserDto = userTransactionInfoMapper.selectByUserId(optUserId);

        UserTransactionInfo orderUser = new UserTransactionInfo();//发布者
        orderUser.setId(orderUserDto.getId());
        orderUser.setTransationTotal(orderUserDto.getTransationTotal() + 1);//交易量+1
        orderUser.setAppealTotal(orderUserDto.getAppealTotal() + 1);//申诉量+1

        UserTransactionInfo optUser = new UserTransactionInfo();//操作者
        optUser.setId(optUserDto.getId());

        if (orderType == BalanceOrderBillConstants.ORDER_TYPE_BUY) {//如果是买单
            orderUser.setAppealFail(orderUserDto.getAppealFail() + 1);//发布者败诉+1
            optUser.setAppealSuccess(optUserDto.getAppealSuccess() + 1);//操作者胜诉+1
        } else {//如果是卖单
            optUser.setAppealFail(optUserDto.getAppealFail() + 1);//操作者败诉+1
        }

        userTransactionInfoMapper.updateByPrimaryKeySelective(orderUser);
        userTransactionInfoMapper.updateByPrimaryKeySelective(optUser);
    }

    /**
     * 通过申诉导致订单状态变为失败时，更新用户交易信息表
     *
     * @param orderType   买单(1)  卖单(-1)
     * @param orderUserId 发布者用户id
     * @param optUserId   操作者用户id
     */
    private void updatesUserTransationInfoOfAppealForBillFail(int orderType, String orderUserId, String optUserId) {
        UserTransactionInfoDto orderUserDto = userTransactionInfoMapper.selectByUserId(orderUserId);
        UserTransactionInfoDto optUserDto = userTransactionInfoMapper.selectByUserId(optUserId);

        UserTransactionInfo orderUser = new UserTransactionInfo();
        orderUser.setId(orderUserDto.getId());

        UserTransactionInfo optUser = new UserTransactionInfo();
        optUser.setId(optUserDto.getId());

        if (orderType == BalanceOrderBillConstants.ORDER_TYPE_BUY) {//如果是买单
            orderUser.setAppealSuccess(orderUserDto.getAppealSuccess() + 1);//发布者胜诉+1
            optUser.setAppealFail(optUserDto.getAppealFail() + 1);//操作者败诉+1
        } else {//如果是卖单
            orderUser.setAppealFail(orderUserDto.getAppealFail() + 1);//发布者败诉+1
            optUser.setAppealSuccess(optUserDto.getAppealSuccess() + 1);//操作者胜诉+1
        }

        userTransactionInfoMapper.updateByPrimaryKeySelective(orderUser);
        userTransactionInfoMapper.updateByPrimaryKeySelective(optUser);
    }

    /**
     * 作废申诉，如果作废前bill单是成功
     *
     * @param orderType   买单(1)  卖单(-1)
     * @param orderUserId 发布者用户id
     * @param optUserId   操作者用户id
     */
    private void updatesUserTransationInfoOfRollbackForBillSuccessBefore(int orderType, String orderUserId, String optUserId) {
        UserTransactionInfoDto orderUserDto = userTransactionInfoMapper.selectByUserId(orderUserId);
        UserTransactionInfoDto optUserDto = userTransactionInfoMapper.selectByUserId(optUserId);

        UserTransactionInfo orderUser = new UserTransactionInfo();//发布者
        orderUser.setId(orderUserDto.getId());
        orderUser.setTransationTotal(orderUserDto.getTransationTotal() - 1);//交易量-1
        orderUser.setAppealTotal(orderUserDto.getAppealTotal() - 1);//申诉量-1

        UserTransactionInfo optUser = new UserTransactionInfo();//操作者
        optUser.setId(optUserDto.getId());

        if (orderType == BalanceOrderBillConstants.ORDER_TYPE_BUY) {//如果是买单
            orderUser.setAppealFail(orderUserDto.getAppealFail() - 1);//发布者败诉-1
            orderUser.setAppealSuccess(orderUserDto.getAppealSuccess() + 1);//发布胜诉+1
            optUser.setAppealSuccess(optUserDto.getAppealSuccess() - 1);//操作者胜诉-1
            optUser.setAppealFail(optUserDto.getAppealFail() + 1);//操作者败诉+1
        } else {//如果是卖单
            orderUser.setAppealFail(orderUserDto.getAppealFail() + 1);//发布者败诉+1
            optUser.setAppealFail(optUserDto.getAppealFail() - 1);//操作者败诉-1
            optUser.setAppealSuccess(optUserDto.getAppealSuccess() + 1);//操作者胜诉+1
        }

        userTransactionInfoMapper.updateByPrimaryKeySelective(orderUser);
        userTransactionInfoMapper.updateByPrimaryKeySelective(optUser);
    }

    /**
     * 作废申诉，如果作废前bill单是失败
     *
     * @param orderType   买单(1)  卖单(-1)
     * @param orderUserId 发布者用户id
     * @param optUserId   操作者用户id
     */
    private void updatesUserTransationInfoOfRollbackForBillFailBefore(int orderType, String orderUserId, String optUserId) {
        UserTransactionInfoDto orderUserDto = userTransactionInfoMapper.selectByUserId(orderUserId);
        UserTransactionInfoDto optUserDto = userTransactionInfoMapper.selectByUserId(optUserId);

        UserTransactionInfo orderUser = new UserTransactionInfo();
        orderUser.setId(orderUserDto.getId());
        orderUser.setTransationTotal(orderUserDto.getTransationTotal() + 1);//交易量+1
        orderUser.setAppealTotal(orderUserDto.getAppealTotal() + 1);//申诉数+1

        UserTransactionInfo optUser = new UserTransactionInfo();
        optUser.setId(optUserDto.getId());

        if (orderType == BalanceOrderBillConstants.ORDER_TYPE_BUY) {//如果是买单
            orderUser.setAppealSuccess(orderUserDto.getAppealSuccess() - 1);//发布者胜诉-1
            orderUser.setAppealFail(orderUserDto.getAppealFail() + 1);//发布者败诉+1

            optUser.setAppealFail(optUserDto.getAppealFail() - 1);//操作者败诉-1
            optUser.setAppealSuccess(optUserDto.getAppealSuccess() + 1);//操作者胜诉+1
        } else {//如果是卖单
            orderUser.setAppealFail(orderUserDto.getAppealFail() - 1);//发布者败诉-1
            optUser.setAppealSuccess(optUserDto.getAppealSuccess() - 1);//操作者胜诉-1
            optUser.setAppealFail(optUserDto.getAppealFail() + 1);//操作者败诉+1
        }

        userTransactionInfoMapper.updateByPrimaryKeySelective(orderUser);
        userTransactionInfoMapper.updateByPrimaryKeySelective(optUser);
    }

    /**
     * 作废申诉
     *
     * @param appealId 申诉id
     */
    @Override
    @Transactional
    public void headerRepeal2Appeal(String appealId) {
        Date now = new Date();
        Appeal appeal = appealMapper.selectByPrimaryKey(appealId);
        //如果申诉记录状态不等于同意/驳回，抛出异常
        if (appeal.getStatus() != BalanceOrderBillConstants.APPEAL_STATUS_AGREED &&
                appeal.getStatus() != BalanceOrderBillConstants.APPEAL_STATUS_REJECT) {
            throw new BaseException("500", "申诉状态更新，请刷新重试！");
        }
        appeal.setStatus(BalanceOrderBillConstants.APPEAL_STATUS_REPEAL);//设置申诉状态->作废
        appeal.setModifyTime(now);//设置修改时间
        appealMapper.updateByPrimaryKeySelective(appeal);

        BalanceOrderBill balanceOrderBill = balanceOrderBillMapper.selectByPrimaryKey(appeal.getBalanceBillId());
        BalanceOrder order = balanceOrderMapper.selectByPrimaryKey(balanceOrderBill.getOrderId());
        String orderUserId = balanceOrderBill.getOrderUserId();
        String optUserId = balanceOrderBill.getOptUserId();

        BigDecimal amount = balanceOrderBill.getAmount();//交易数量
        BigDecimal amountReal = balanceOrderBill.getAmountReal();//实际成交数量
        //卖家实际支出数量
        BigDecimal freeBack = BalanceComputeUtils.freeBack(amount, null, order.getServerCharge(), order.getSubsidy());
        BigDecimal serverCharge = order.getServerCharge();//手续费
        BigDecimal subsidy = order.getSubsidy();//买家补贴
        if (balanceOrderBill.getBillStatus() == BalanceOrderBillConstants.BILL_STATUS_FINISH) {
            //如果之前是成功状态
            updatesUserTransationInfoOfRollbackForBillSuccessBefore(balanceOrderBill.getOrderType(), orderUserId, optUserId);//更新交易信息(信任、好评)

            //更新订单状态为失效
            balanceOrderBill.setBillStatus(BalanceOrderBillConstants.BILL_STATUS_LOSE_EFFICACY);
            balanceOrderBill.setModifyTime(now);
            balanceOrderBillMapper.updateByPrimaryKeySelective(balanceOrderBill);

            if (balanceOrderBill.getOrderType() == BalanceOrderBillConstants.ORDER_TYPE_BUY) {
                //如果是买单，操作者减去实际获得的余额，发布者返回全部余额
                updateWalletFlowBalance(orderUserId, WalletBillConstants.ARITHMETIC_TYPE_ADD, freeBack);//发布者钱包变动
                updateWalletFlowBalance(optUserId, WalletBillConstants.ARITHMETIC_TYPE_SUB, amountReal);//操作者钱包变动

                insertBalanceRecord(orderUserId, freeBack, WalletBillConstants.ARITHMETIC_TYPE_ADD, serverCharge, subsidy);//发布者余额变动记录
                insertBalanceRecord(optUserId, amountReal, WalletBillConstants.ARITHMETIC_TYPE_SUB, null, subsidy);//操作者余额变动记录
            } else {
                //如果是卖单，发布者减去实际获得的余额，操作者返还全部余额
                updateWalletFlowBalance(orderUserId, WalletBillConstants.ARITHMETIC_TYPE_SUB, amountReal);//发布者钱包变动
                updateWalletFlowBalance(optUserId, WalletBillConstants.ARITHMETIC_TYPE_ADD, freeBack);//操作者钱包变动

                insertBalanceRecord(orderUserId, amountReal, WalletBillConstants.ARITHMETIC_TYPE_SUB, null, subsidy);//发布者余额变动记录
                insertBalanceRecord(optUserId, freeBack, WalletBillConstants.ARITHMETIC_TYPE_ADD, serverCharge, subsidy);//操作者余额变动记录
            }
        } else {
            //如果之前是失效状态
            updatesUserTransationInfoOfRollbackForBillFailBefore(balanceOrderBill.getOrderType(), orderUserId, optUserId);//更新交易信息(信任、好评)

            //更新订单状态为成功
            balanceOrderBill.setBillStatus(BalanceOrderBillConstants.BILL_STATUS_FINISH);
            balanceOrderBill.setModifyTime(now);
            balanceOrderBillMapper.updateByPrimaryKeySelective(balanceOrderBill);

            if (balanceOrderBill.getOrderType() == BalanceOrderBillConstants.ORDER_TYPE_BUY) {
                //如果是买单，操作者加上实际获得的余额，发布者减去返还的全部余额

                updateWalletFlowBalance(orderUserId, WalletBillConstants.ARITHMETIC_TYPE_SUB, freeBack);//发布者钱包变动
                updateWalletFlowBalance(optUserId, WalletBillConstants.ARITHMETIC_TYPE_ADD, amountReal);//操作者钱包变动

                insertBalanceRecord(orderUserId, freeBack, WalletBillConstants.ARITHMETIC_TYPE_SUB, serverCharge, subsidy);//发布者余额变动记录
                insertBalanceRecord(optUserId, amountReal, WalletBillConstants.ARITHMETIC_TYPE_ADD, null, subsidy);//操作者余额变动记录
            } else {
                //如果是卖单，操作者减去之前返还的全部余额，发布者加上实际获得的余额

                updateWalletFlowBalance(orderUserId, WalletBillConstants.ARITHMETIC_TYPE_ADD, amountReal);//发布者钱包变动
                updateWalletFlowBalance(optUserId, WalletBillConstants.ARITHMETIC_TYPE_SUB, freeBack);//操作者钱包变动

                insertBalanceRecord(orderUserId, amountReal, WalletBillConstants.ARITHMETIC_TYPE_ADD, null, subsidy);//发布者余额变动记录
                insertBalanceRecord(optUserId, freeBack, WalletBillConstants.ARITHMETIC_TYPE_SUB, serverCharge, subsidy);//操作者余额变动记录
            }
        }
    }

    /**
     * 更新用户钱包信息
     *
     * @param userId         用户id
     * @param arithmeticType 类型，加(1)，减(-1)
     * @param amount         总数
     */
    private void updateWalletFlowBalance(String userId, int arithmeticType, BigDecimal amount) {
        WalletDto walletDto = walletMapper.selectByUserId(userId);
        Wallet wallet = new Wallet();
        wallet.setId(walletDto.getId());
        //增加
        if (arithmeticType == WalletBillConstants.ARITHMETIC_TYPE_ADD) {
            wallet.setFlowBalance(walletDto.getFlowBalance().add(amount));
        } else {
            //减少
            wallet.setFlowBalance(walletDto.getFlowBalance().subtract(amount));
        }
        wallet.setVersion(walletDto.getVersion() + 1);
        wallet.setModifyTime(new Date());
        walletMapper.updateByPrimaryKeySelective(wallet);
    }

    /**
     * 申诉作废操作，插入余额变动记录
     *
     * @param targetUserId   变更的钱包用户id
     * @param amount         总数
     * @param arithmeticType 类型，加(1)，减(-1)
     * @param serverCharge   平台收取的服务费
     * @param subsidy        补贴
     */
    private void insertBalanceRecord(String targetUserId, BigDecimal amount, int arithmeticType, BigDecimal serverCharge, BigDecimal subsidy) {
        //操作用户信息
        UserDto optUser = userService.getById(targetUserId);
        //操作者变动记录
        WalletBalanceRecord balanceRecord = new WalletBalanceRecord();
        balanceRecord.setId(UUID.randomUUID().toString());
        balanceRecord.setAmount(amount);
        //变更状态->增加
        if (arithmeticType == WalletBillConstants.ARITHMETIC_TYPE_ADD) {
            balanceRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);
        } else {
            //变更状态->减少
            balanceRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_SUB);
        }
        //变更类型->申诉作废
        balanceRecord.setChangeType(WalletBillConstants.CHANGE_TYPE_APPEAL_ROLL_BACK);
        if (serverCharge != null) {
            balanceRecord.setServiceCharge(amount.multiply(serverCharge));
        }
        if (subsidy != null) {
            balanceRecord.setSubsidy(amount.multiply(subsidy));
        }
        balanceRecord.setTargetUserId(targetUserId);
        balanceRecord.setOptUserId(targetUserId);
        balanceRecord.setOptUserShowMsg(optUser.getUserName());
        balanceRecord.setCreateTime(new Date());
        walletBalanceRecordMapper.insertSelective(balanceRecord);
    }


    /***lzf***/
    /***
     *
     * 订单状态改变为成功时，对应用户获得的余额
     *
     * @param balanceOrderBill 交易订单信息
     * @param successUserid 胜诉用户id
     */
    private void updateBillBuyBalanceOfAppealForBillSuccess(BalanceOrderBill balanceOrderBill, String successUserid) {
        WalletDto walletDto = walletMapper.selectByUserId(successUserid);
        Wallet wallet = new Wallet();
        WalletBalanceRecord walletBalanceRecord = new WalletBalanceRecord();

        //操作用户信息
        UserDto optUser = userService.getById(successUserid);

        BigDecimal amount = balanceOrderBill.getAmount();//交易的余额
        BigDecimal buyAmount = balanceOrderBill.getAmountReal();//实际处理的余额
        BigDecimal buySubsidy = buyAmount.subtract(amount);//交易补贴

        wallet.setId(walletDto.getId());
        wallet.setVersion(walletDto.getVersion() + 1);
        wallet.setFlowBalance(walletDto.getFlowBalance().add(buyAmount));
        wallet.setModifyTime(new Date());

        walletBalanceRecord.setId(UUID.randomUUID().toString());
        walletBalanceRecord.setTargetUserId(successUserid);
        walletBalanceRecord.setOptUserId(successUserid);
        walletBalanceRecord.setOptUserShowMsg(optUser.getUserName());
        walletBalanceRecord.setAmount(buyAmount);
        walletBalanceRecord.setSubsidy(buySubsidy);
        walletBalanceRecord.setServiceCharge(null);
        walletBalanceRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);
        walletBalanceRecord.setChangeType(WalletBillConstants.CHANGE_TYPE_DEAL);
        walletBalanceRecord.setCreateTime(new Date());

        balanceOrderBill.setBillStatus(BalanceOrderBillConstants.BILL_STATUS_FINISH);
        balanceOrderBillMapper.updateByPrimaryKeySelective(balanceOrderBill);
        walletMapper.updateByPrimaryKeySelective(wallet);
        walletBalanceRecordMapper.insertSelective(walletBalanceRecord);
    }

    /***
     *
     * 订单状态改变为失效时，对应用户获得的余额
     *
     * @param balanceOrderBill 交易订单信息
     * @param failUserid 胜诉用户id
     */
    private void updateBillBuyBalanceOfAppealForBillFail(BalanceOrderBill balanceOrderBill, String failUserid) {
        BalanceOrderDto balanceOrder = balanceOrderMapper.selectById(balanceOrderBill.getOrderId());
        WalletDto walletDto = walletMapper.selectByUserId(failUserid);
        //操作用户信息
        UserDto optUser = userService.getById(failUserid);
        Wallet wallet = new Wallet();
        WalletBalanceRecord walletBalanceRecord = new WalletBalanceRecord();

        BigDecimal amount = balanceOrderBill.getAmount();
        BigDecimal serviceCharge = BalanceComputeUtils.realServiceCharge(amount, balanceOrder.getServerCharge(), balanceOrder.getSubsidy());
        BigDecimal sellSubsidy = BalanceComputeUtils.realSubsidy(amount, balanceOrder.getServerCharge(), balanceOrder.getSubsidy());
        BigDecimal sellAmount = amount.add(serviceCharge).add(sellSubsidy);

        wallet.setId(walletDto.getId());
        wallet.setVersion(walletDto.getVersion() + 1);
        wallet.setFlowBalance(walletDto.getFlowBalance().add(sellAmount));
        wallet.setModifyTime(new Date());

        walletBalanceRecord.setId(UUID.randomUUID().toString());
        walletBalanceRecord.setTargetUserId(failUserid);
        walletBalanceRecord.setOptUserId(failUserid);
        walletBalanceRecord.setOptUserShowMsg(optUser.getUserName());
        walletBalanceRecord.setAmount(sellAmount);
        walletBalanceRecord.setSubsidy(sellSubsidy);
        walletBalanceRecord.setServiceCharge(sellSubsidy);
        walletBalanceRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);
        walletBalanceRecord.setChangeType(WalletBillConstants.CHANGE_TYPE_DEAL);
        walletBalanceRecord.setCreateTime(new Date());

        balanceOrderBill.setBillStatus(BalanceOrderBillConstants.BILL_STATUS_LOSE_EFFICACY);
        balanceOrderBillMapper.updateByPrimaryKeySelective(balanceOrderBill);
        walletMapper.updateByPrimaryKeySelective(wallet);
        walletBalanceRecordMapper.insertSelective(walletBalanceRecord);
    }

    /***lzf***/

    /**
     * #################huangxl############
     * 后台系统接口
     */
    @Override
    public List<BalanceOrderBillOfAllListDto> listForAll(BalanceOrderBillOfAllConditionDto condition) {
        return balanceOrderBillMapper.listForAll(condition);
    }
}
