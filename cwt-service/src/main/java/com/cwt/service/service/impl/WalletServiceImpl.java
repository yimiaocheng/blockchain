package com.cwt.service.service.impl;

import com.cwt.common.enums.BaseResultEnums;
import com.cwt.common.enums.PayResultEnums;
import com.cwt.common.enums.UserResultEnums;
import com.cwt.common.enums.WalletResultEnums;
import com.cwt.common.exception.*;
import com.cwt.common.util.BeanUtils;
import com.cwt.common.util.ExceptionPreconditionUtils;
import com.cwt.common.util.MD5Utils;
import com.cwt.domain.constant.GameCoinConstants;
import com.cwt.domain.constant.InformationConstants;
import com.cwt.domain.constant.IntegralConstants;
import com.cwt.domain.constant.WalletBillConstants;
import com.cwt.domain.dto.ResultDto;
import com.cwt.domain.dto.balance.BalanceConverRecordDto;
import com.cwt.domain.dto.grade.GradeDto;
import com.cwt.domain.dto.information.InformationDto;
import com.cwt.domain.dto.user.BackendUserDTO;
import com.cwt.domain.dto.user.UserDto;
import com.cwt.domain.dto.user.UserGradeWalletDto;
import com.cwt.domain.dto.wallet.WalletBalanceRecordDto;
import com.cwt.domain.dto.wallet.WalletCalculatePowerRecordDto;
import com.cwt.domain.dto.wallet.WalletDto;
import com.cwt.domain.dto.wallet.transfor.TransForDto;
import com.cwt.domain.entity.*;
import com.cwt.persistent.mapper.*;
import com.cwt.service.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service("walletService")
public class WalletServiceImpl implements WalletService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private InformationService informationService;
    @Autowired
    private WalletCalculatePowerRecordService walletCalculatePowerRecordService;
    @Autowired
    private WalletBalanceRecordService walletBalanceRecordService;
    @Autowired
    private BalanceConverRecordService balanceConverRecordService;
    @Autowired
    private WalletBalanceRecordMapper walletBalanceRecordMapper;
    @Autowired
    private WalletCalculatePowerRecordMapper walletCalculatePowerRecordMapper;
    @Autowired
    private InformationMapper informationMapper;
    @Autowired
    private GameCurrencyRecordMapper gameCurrencyRecordMapper;
    @Autowired
    private IntegralRecordMapper integralRecordMapper;

    /***
     * 根据用户Id获取钱包信息
     * @param userId
     * @return WalletDto
     */
    @Override
//    @Cacheable(value = "WALLET_USERID_CACHE", key = "'WALLET_USERID_CACHE_' + #userId")
    public WalletDto getByUserId(String userId) {
        //获取钱包信息
        return walletMapper.selectByUserId(userId);
    }

    /***
     * 根据钱包Id(主键)获取钱包信息
     * @param walletId
     * @return WalletDto
     */
    @Override
//    @Cacheable(value = "WALLET_CACHE",key = "'WALLET_CACHE_' + #walletId")
    public WalletDto getByWalletId(String walletId) {
        //获取钱包信息
        return walletMapper.selectByWalletId(walletId);
    }


    /***
     * 根据钱包Id更新钱包信息
     * @param wallet
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
//    @CachePut(value = "WALLET_USERID_CACHE",key = "'WALLET_USERID_CACHE_' + #result.userId")
    public WalletDto updateWallet(Wallet wallet) {
        walletMapper.updateByPrimaryKeySelective(wallet);
        return walletMapper.selectByUserId(wallet.getUserId());
    }

    /***
     * 查询所有钱包信息
     * @return List<WalletDto>
     */
    public List<WalletDto> selectAllWallet() {
        return walletMapper.selectAllWallet();
    }

    /***
     * 查询所有矿工等级信息
     * @return List<GradeDto>
     */
    public List<GradeDto> selectAllGrade() {
        return gradeMapper.selectAllGrade();
    }


    /***
     * 新建/保存钱包
     * @param userId
     * @return WalletDto
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
//    @Caching(put = @CachePut(value = "WALLET_CACHE",key = "'WALLET_CACHE_' + #result.id"))
    public WalletDto saveByUserId(String userId) {
        //检测用户id不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(userId
                , new BaseException(BaseResultEnums.USERID_NULL));
        //检测用户id不能重复(一个用户只能有一个钱包)
        WalletExceptionPreconditionUtils.checkUserNoRepetition(this.getByUserId(userId)
                , new WalletExeption(WalletResultEnums.SAVA_WALLET_BY_USER));

        //从数据库中读取当前平台新建用户的钱包算力
        InformationDto informationDto = informationService.getByDateName("initial_force");
        Double initialForce = Double.parseDouble(informationDto.getDataValue());
        WalletDto walletDto = new WalletDto();
        walletDto.setId(MD5Utils.MD5(UUID.randomUUID().toString()));
        walletDto.setUserId(userId);
        walletDto.setFlowBalance(new BigDecimal(0.0 + ""));
        walletDto.setCalculationForce(new BigDecimal(initialForce + ""));
        Wallet wallet = new Wallet();
        BeanUtils.copySamePropertyValue(walletDto, wallet);
        walletMapper.insert(wallet);
        return walletDto;
    }

    /***
     * 游戏币转换算力功能
     * @param transGameCoin 转换的游戏币
     * @param userId  用户Id
     * @return 更新后的钱包对象
     * 尹总第二期游戏币兑换算力功能
     * 2018/10/16 10:02
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public WalletDto gameCointransform(BigDecimal transGameCoin, String password, String userId) {
        //获取用户钱包
        WalletDto walletDto = this.getByUserId(userId);
        //获取用户
        UserDto user = userService.getById(userId);
        //判断用户不能为Null
        ExceptionPreconditionUtils.checkNotNull(user, new UserExeption(UserResultEnums.USER_NULL));
        //检测转换余额不能为空
        ExceptionPreconditionUtils.checkNotNull(transGameCoin,
                new WalletExeption(WalletResultEnums.TARANSFORM_TRANSGAMECOIN_NULL));
        //检测转换余额不能小于0
        if (transGameCoin.compareTo(BigDecimal.valueOf(0)) < 0) {
            new WalletExeption(WalletResultEnums.TARANSFORM_TRANSGAMECOIN_ZERO);
        }
        //检测转换余额不能大于当前余额
        WalletExceptionPreconditionUtils.checkNotBeyond(transGameCoin, walletDto.getGameCion(),
                new WalletExeption(WalletResultEnums.TARANSFORM_TRANSGAMECOIN_BEYOND));
        //判断支付密码是否为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(password,
                new BaseException(BaseResultEnums.PAYPASSWORD_NULL));
        //判断账号是否设置了支付密码
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(user.getPaymentPassword(),
                new BaseException(BaseResultEnums.NO_PAYPASSWORD));
        //判断支付密码是否正确
        String md5Password = MD5Utils.MD5(password);
        ExceptionPreconditionUtils.checkArguments(user.getPaymentPassword().equals(md5Password),
                new BaseException(BaseResultEnums.PAYPASSWORD_ERROR));

        Date now = new Date();

        //从数据库中读取当前平台转换的游戏币转换算力比率
        InformationDto informationDto = informationService.getByDateName(InformationConstants.DATA_NAME_GAME_COIN_TO_CALCULATION);
        BigDecimal gameCionRate = new BigDecimal(informationDto.getDataValue());//游戏币转换算力比率

        //变动的算力
        BigDecimal force = transGameCoin.multiply(gameCionRate);

        //更新数据库钱包表的参数
        TransForDto transFor = new TransForDto();
        transFor.setId(walletDto.getId());//钱包id
        transFor.setVariationForce(force);//变动的算力
        transFor.setVariationGameCoin(transGameCoin.multiply(BigDecimal.valueOf(-1)));//变动的游戏币

        //游戏币转换算力 更新数据
        walletMapper.handleGameCoinTransForm(transFor);

        //更新数据传输类
        walletDto.setCalculationForce(walletDto.getCalculationForce().add(force));
        walletDto.setGameCion(walletDto.getGameCion().subtract(transGameCoin));
        walletDto.setVersion(walletDto.getVersion() + 1);
        walletDto.setModifyTime(now);

        //保存游戏币变更记录
        String sourceId = UUID.randomUUID().toString();
        GameCurrencyRecord gameCurrencyRecord = new GameCurrencyRecord();
        gameCurrencyRecord.setUserId(userId);
        gameCurrencyRecord.setId(sourceId);
        gameCurrencyRecord.setTotalNum(transGameCoin);//转换得到的游戏币
        gameCurrencyRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_SUB);//该记录的类型是：增加);
        gameCurrencyRecord.setChangeType(GameCoinConstants.CT_TO_GAME_COIN);//资产兑换游戏代币);
        gameCurrencyRecord.setCreateTime(now);
        gameCurrencyRecord.setModifyTime(now);

        //保存算力变更记录
        WalletCalculatePowerRecord walletForceBillDto = new WalletCalculatePowerRecord();
        walletForceBillDto.setId(UUID.randomUUID().toString());//记录id
        walletForceBillDto.setTargetUserId(userId);//算力变更用户id
        walletForceBillDto.setOptUserId(userId);//操作的用户id
        walletForceBillDto.setOptUserShowMsg(user.getUserName());//操作用户的账号
        walletForceBillDto.setAmount(force);//变更的数量
        walletForceBillDto.setEventId(sourceId);//导致算力变更的事件id
        walletForceBillDto.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
        walletForceBillDto.setChangeType(WalletBillConstants.CHANGE_TYPE_TRANS);//途径：余额兑换
        walletForceBillDto.setCreateTime(now);//创建时间

//        //保存余额兑换记录
//        BalanceConverRecordDto converBill = new BalanceConverRecordDto();
//        converBill.setId(UUID.randomUUID().toString());//记录id
//        converBill.setUserId(userId);//操作余额兑换的用户id
//        converBill.setBalanceRecordId(walletForceBillDto.getId());//余额兑换算力后的算力记录id
//        converBill.setConverAmount(force);//转换的算力
//        converBill.setCreateTime(now);//创建时间
//        converBill.setStatus(WalletBillConstants.STATUS_PROCESSED);//状态：1 已处理推荐奖

        //保存算力记录
        walletCalculatePowerRecordMapper.insertSelective(walletForceBillDto);
        //保存游戏币记录
        gameCurrencyRecordMapper.insertSelective(gameCurrencyRecord);

        return walletDto;
    }

    /***
     * 余额转换算力功能
     * @param transBalance 转换的余额
     * @param userId  用户Id
     * @return 更新后的钱包对象
     *
     * 2018/10/16 10:02 尹总第二期兑换算力版本
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public WalletDto transform(Double transBalance, String password, String userId) {
        //获取用户钱包
        WalletDto walletDto = this.getByUserId(userId);
        //获取用户
        UserDto user = userService.getById(userId);
        //判断用户不能为Null
        ExceptionPreconditionUtils.checkNotNull(user, new UserExeption(UserResultEnums.USER_NULL));
        //检测转换余额不能为空
        ExceptionPreconditionUtils.checkNotNull(transBalance,
                new WalletExeption(WalletResultEnums.TARANSFORM_TRANSBALANCE_NULL));
        //检测转换余额不能小于0
        WalletExceptionPreconditionUtils.checkNotZero(transBalance,
                new WalletExeption(WalletResultEnums.TARANSFORM_TRANSBALANCE_ZERO));
        //检测转换余额不能大于当前余额
        WalletExceptionPreconditionUtils.checkNotBeyond(transBalance, walletDto.getFlowBalance(),
                new WalletExeption(WalletResultEnums.TARANSFORM_TRANSBALANCE_BEYOND));
        //检测转换余额不能小于100
        WalletExceptionPreconditionUtils.checkLessThan(transBalance,
                new WalletExeption(WalletResultEnums.TARANSFORM_TRANSBALANCE_LESSTHAN));
        //检测转换余额必须是100的整数倍数
        WalletExceptionPreconditionUtils.checkExactDivision(transBalance,
                new WalletExeption(WalletResultEnums.TARANSFORM_TRANSBALANCE_EXACTDIVISION));
        //判断支付密码是否为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(password,
                new BaseException(BaseResultEnums.PAYPASSWORD_NULL));
        //判断账号是否设置了支付密码
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(user.getPaymentPassword(),
                new BaseException(BaseResultEnums.NO_PAYPASSWORD));
        //判断支付密码是否正确
        String md5Password = MD5Utils.MD5(password);
        ExceptionPreconditionUtils.checkArguments(user.getPaymentPassword().equals(md5Password),
                new BaseException(BaseResultEnums.PAYPASSWORD_ERROR));

        //当前时间
        Date now = new Date();

        //从数据库中读取当前平台转换的比率
        InformationDto informationDto = informationService.getByDateName(InformationConstants.DATA_NAME_TRANS_RATE);
        //从数据库中读取当前平台转换的余额比率
        InformationDto informationDto2 = informationService.getByDateName(InformationConstants.DATA_NAME_TRANS_BALANCE_RATE);
        //从数据库中读取当前平台转换的积分比率
        InformationDto informationDto3 = informationService.getByDateName(InformationConstants.DATA_NAME_CT_TO_INTEGRAL_RATE);
        //从数据库中读取当前平台转换的游戏币比率
        InformationDto informationDto4 = informationService.getByDateName(InformationConstants.DATA_NAME_CT_TO_GAME_COIN_RATE);
        BigDecimal transRate = new BigDecimal(informationDto.getDataValue());//转换倍数杠杆
        BigDecimal transBalanceRate = new BigDecimal(informationDto2.getDataValue());//转换算力比率
        BigDecimal integralRate = new BigDecimal(informationDto3.getDataValue());//转换积分比率
        BigDecimal gameCionRate = new BigDecimal(informationDto4.getDataValue());//转换游戏币比率

        //需要转换的余额
        BigDecimal totalBalance = new BigDecimal(transBalance);
        //变动的算力
        BigDecimal force = totalBalance.multiply(transBalanceRate).multiply(transRate);
        //变动的积分
        BigDecimal integral = totalBalance.multiply(integralRate);
        //变动的游戏币
        BigDecimal gameCion = totalBalance.multiply(gameCionRate);

        //更新数据库钱包表的参数
        TransForDto transFor = new TransForDto();
        transFor.setId(walletDto.getId());//钱包id
        transFor.setVariationBalance(totalBalance.multiply(BigDecimal.valueOf(-1)));//变动的余额
        transFor.setVariationForce(force);//变动的算力
        transFor.setVariationIntegral(integral);//变动的积分
        transFor.setVariationGameCoin(gameCion);//变动的游戏币
        //余额转换算力 更新数据
        walletMapper.handleTransForm(transFor);


        //更新数据传输类
        walletDto.setFlowBalance(walletDto.getFlowBalance().subtract(totalBalance));
        walletDto.setCalculationForce(walletDto.getCalculationForce().add(force));
        walletDto.setIntegral(walletDto.getIntegral().add(integral));
        walletDto.setGameCion(walletDto.getGameCion().add(gameCion));
        walletDto.setVersion(walletDto.getVersion() + 1);
        walletDto.setModifyTime(now);

        //保存余额变更记录
        String sourceId = UUID.randomUUID().toString();
        WalletBalanceRecord walletBalanceBillDto = new WalletBalanceRecord();
        walletBalanceBillDto.setId(sourceId);//记录id
        walletBalanceBillDto.setTargetUserId(userId);//余额变更用户id
        walletBalanceBillDto.setOptUserId(userId);//操作的用户id
        walletBalanceBillDto.setOptUserShowMsg(user.getUserName());//操作用户的账号
        walletBalanceBillDto.setAmount(totalBalance);//变更的数量
        walletBalanceBillDto.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_SUB);//该记录的类型是：减少
        walletBalanceBillDto.setChangeType(WalletBillConstants.CHANGE_TYPE_TRANS);//途径：余额兑换
        walletBalanceBillDto.setCreateTime(now);//创建时间

        //保存算力变更记录
        WalletCalculatePowerRecord walletForceBillDto = new WalletCalculatePowerRecord();
        walletForceBillDto.setId(UUID.randomUUID().toString());//记录id
        walletForceBillDto.setTargetUserId(userId);//算力变更用户id
        walletForceBillDto.setOptUserId(userId);//操作的用户id
        walletForceBillDto.setOptUserShowMsg(user.getUserName());//操作用户的账号
        walletForceBillDto.setAmount(force);//变更的数量
        walletForceBillDto.setEventId(sourceId);//导致算力变更的事件id
        walletForceBillDto.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
        walletForceBillDto.setChangeType(WalletBillConstants.CHANGE_TYPE_TRANS);//途径：余额兑换
        walletForceBillDto.setCreateTime(now);//创建时间

        //保存积分变更记录
        IntegralRecord integralRecord = new IntegralRecord();
        integralRecord.setUserId(userId);
        integralRecord.setId(UUID.randomUUID().toString());
        integralRecord.setTotalNum(integral);//转换得到的积分
        integralRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
        integralRecord.setChangeType(IntegralConstants.CT_TO_INTEGRAL);//资产兑换商城积分
        integralRecord.setCreateTime(now);
        integralRecord.setModifyTime(now);

        //保存游戏币变更记录
        GameCurrencyRecord gameCurrencyRecord = new GameCurrencyRecord();
        gameCurrencyRecord.setUserId(userId);
        gameCurrencyRecord.setId(UUID.randomUUID().toString());
        gameCurrencyRecord.setTotalNum(gameCion);//转换得到的游戏币
        gameCurrencyRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加);
        gameCurrencyRecord.setChangeType(GameCoinConstants.CT_TO_GAME_COIN);//资产兑换游戏代币;
        gameCurrencyRecord.setCreateTime(now);
        gameCurrencyRecord.setModifyTime(now);

//        //保存余额兑换记录
//        BalanceConverRecordDto converBill = new BalanceConverRecordDto();
//        converBill.setId(UUID.randomUUID().toString());//记录id
//        converBill.setUserId(userId);//操作余额兑换的用户id
//        converBill.setBalanceRecordId(walletForceBillDto.getId());//余额兑换算力后的算力记录id
//        converBill.setConverAmount(force);//转换的算力
//        converBill.setCreateTime(now);//创建时间
//        converBill.setStatus(WalletBillConstants.STATUS_PROCESSED);//状态：1 已处理推荐奖

        //保存余额记录
        walletBalanceRecordMapper.insertSelective(walletBalanceBillDto);
        //保存算力记录
        walletCalculatePowerRecordMapper.insertSelective(walletForceBillDto);
        //保存积分记录
        integralRecordMapper.insertSelective(integralRecord);
        //保存游戏币记录
        gameCurrencyRecordMapper.insertSelective(gameCurrencyRecord);
//        //保存余额兑换算力记录，用作后期处理推荐奖
//        balanceConverRecordService.saveConverBill(converBill);

        //开始计算推荐人奖励 ：极差
        startAward(userId, force, walletForceBillDto.getId());

        //返回更新后的钱包对象
        return walletDto;
    }

    /***
     * 开始计算推荐人奖励 ：极差
     * @param userId
     * @param force
     * @param eventId 触发事件Id
     */
    private void startAward(String userId, BigDecimal force, String eventId) {
        /***获取当前用户以及上级用户的信息***/
        //根据id查询 返回：账号 钱包id 算力 余额 钱包版本 等级奖励比率
        //这是当前兑换算力的用户
        UserGradeWalletDto activeUser = userMapper.selectUserGradeWalletByUid(userId);
        //从数据库中读取当前平台可获取奖励的上级人数
        InformationDto informationDto = informationService.getByDateName(InformationConstants.DATA_NAME_AWARD_AMOUNT);
        int awardAmount = Integer.parseInt(informationDto.getDataValue());

        //存储所有上级信息的集合（用户信息、钱包信息、等级信息）
        //数组总长度 = 可获取奖励上级人数
        List<UserGradeWalletDto> ugwDto = new ArrayList<>(awardAmount);
        //上级的用户id
        String pid = activeUser.getInviterId();
        if (pid == null) {
            return;
        }
        //遍历查询出操作算力转换用户的所有上级的信息（包括用户信息、钱包信息、等级信息），并用集合存储
        for (int i = 0; i < awardAmount; i++) {
            if (pid == null) {
                break;
            }
            //上级用户信息
            UserGradeWalletDto parentUser = userMapper.selectUserGradeWalletByUid(pid);
            //如果查询用户是顶级，则parentUser为null
            if (parentUser == null) {
                break;
            }
            pid = parentUser.getInviterId();
            ugwDto.add(parentUser);
        }

        //存储当前极差奖的最大比例值
        BigDecimal maxPercent = new BigDecimal(0);

        for (int i = 0; i < ugwDto.size(); i++) {
            //上级用户信息对象
            UserGradeWalletDto parentUser = ugwDto.get(i);
            if (parentUser.getLevelAward().compareTo(maxPercent) > 0) {
                //设置算力，当前算力加上奖励算力
                BigDecimal currentPercent = parentUser.getLevelAward().subtract(maxPercent);//奖励比例
                BigDecimal addAmount = force.multiply(currentPercent);//实际获得的极差奖

                Wallet wallet = new Wallet();
                wallet.setId(parentUser.getWId());//设置钱包id
                wallet.setCalculationForce(parentUser.getCalculationForce().add(addAmount));//上级获得算力极差奖励
                wallet.setVersion(parentUser.getVersion() + 1);//更新钱包版本
                wallet.setModifyTime(new Date());//设置修改时间

                //保存算力记录
                WalletCalculatePowerRecord walletForceBillDto = new WalletCalculatePowerRecord();
                walletForceBillDto.setId(MD5Utils.MD5(UUID.randomUUID().toString()));//记录id
                walletForceBillDto.setTargetUserId(parentUser.getUId());//算力变更用户id
                walletForceBillDto.setOptUserId(activeUser.getUId());//操作的用户id
                walletForceBillDto.setEventId(eventId);//触发事件id
                walletForceBillDto.setOptUserShowMsg(activeUser.getUserName());//操作用户的账号
                walletForceBillDto.setAmount(addAmount);//变更的数量
                walletForceBillDto.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
                walletForceBillDto.setChangeType(WalletBillConstants.CHANGE_TYPE_RECOMMEND_AWARD);//途径：推荐奖
                walletForceBillDto.setCreateTime(new Date());//创建时间
                //更新钱包数据
                walletMapper.updateByPrimaryKeySelective(wallet);
                //保存算力变更记录
                walletCalculatePowerRecordMapper.insertSelective(walletForceBillDto);
                //记录当前最大等级奖励比率
                maxPercent = maxPercent.max(parentUser.getLevelAward());//计算当前最大的比例值
            } else {
                continue;
            }
        }
    }


    /***
     * 节点转账功能(转出)
     * @param userId 转账人ID
     * @param telephone 收款人账号
     * @param balance 交易余额
     * @param password 交易密码
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public WalletDto transferAccounts(String userId, String telephone, Double balance, String password) {
        //检测转账金额不能为空
        ExceptionPreconditionUtils.checkNotNull(balance,
                new WalletExeption(WalletResultEnums.TRANSFER_AMOUNT_NULL));
        //检测转换余额不能小于0
        WalletExceptionPreconditionUtils.checkNotZero(balance,
                new WalletExeption(WalletResultEnums.TARANSFORM_TRANSBALANCE_ZERO));
        //判断支付密码是否为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(password,
                new BaseException(BaseResultEnums.PAYPASSWORD_NULL));

        /***获取转账人信息***/
        UserDto user = userService.getById(userId);
        //判断转账人是否存在
        ExceptionPreconditionUtils.checkNotNull(user, new UserExeption(UserResultEnums.USER_NULL));
        //获取转账人钱包
        WalletDto userWallet = this.getByUserId(userId);
        //检测转账余额不能大于当前余额
        WalletExceptionPreconditionUtils.checkNotBeyond(balance, userWallet.getFlowBalance(),
                new WalletExeption(WalletResultEnums.TARANSFORM_TRANSBALANCE_BEYOND));
        //检测是否已输入了支付密码
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(user.getPaymentPassword(),
                new BaseException(BaseResultEnums.NO_PAYPASSWORD));
        //判断支付密码是否正确
        String md5Password = MD5Utils.MD5(password);
        ExceptionPreconditionUtils.checkArguments(user.getPaymentPassword().equals(md5Password),
                new BaseException(BaseResultEnums.PAYPASSWORD_ERROR));

        /***获取收款人信息***/
        UserDto payee = userService.getByTelephone(telephone);
        //判断收款人是否存在
        ExceptionPreconditionUtils.checkNotNull(payee, new UserExeption(UserResultEnums.USER_NULL));
        //判断收款人和转账人是否相同（防止自己转给自己）
        WalletExceptionPreconditionUtils.checkUserNoRepetition(user, payee,
                new WalletExeption(WalletResultEnums.USER_REPETITION));
        //获取收款人钱包
        WalletDto payeeWallet = this.getByUserId(payee.getId());
        //若被转账人钱包为空则为其创建钱包
        if (payeeWallet == null || payeeWallet.getId() == null) {
            this.saveByUserId(payee.getId());
        }

        //开始转账
        BigDecimal settleBalance = startTransfer(userWallet.getId(), payeeWallet.getId(), balance, user, payee);

        WalletDto walletDto = new WalletDto();
        walletDto.setFlowBalance(settleBalance);
        return walletDto;
    }


    /***
     * 处理转账功能
     * @param userWalletId
     * @param payyeWalletId
     * @param balance
     * @return
     */
    private BigDecimal startTransfer(String userWalletId, String payyeWalletId, Double balance, UserDto user, UserDto payee) {
        //从数据库中读取当前平台收取的服务费
        InformationDto informationDto = informationService.getByDateName("service_charge");
        Double serviceCharge = Double.parseDouble(informationDto.getDataValue());

        /***转账人数据计算、插入记录***/
        //转账人扣余额
        WalletDto userWalletDto = this.getByWalletId(userWalletId);
        Wallet userWallet = new Wallet();
        userWallet.setId(userWalletDto.getId());
        userWallet.setFlowBalance(userWalletDto.getFlowBalance().subtract(new BigDecimal(balance + "")));
        //保存余额变更记录
        WalletBalanceRecordDto userBalanceBill = new WalletBalanceRecordDto();
        userBalanceBill.setId(UUID.randomUUID().toString());//记录id
        userBalanceBill.setTargetUserId(user.getId());//余额变更用户id
        userBalanceBill.setOptUserId(user.getId());//操作的用户id
        userBalanceBill.setOptUserShowMsg(user.getUserName());//操作用户的账号
        userBalanceBill.setAmount(new BigDecimal(balance + ""));//变更的数量
        userBalanceBill.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_SUB);//该记录的类型是：减少
        userBalanceBill.setChangeType(WalletBillConstants.CHANGE_TYPE_TRANSFER);//途径：节点转账
        userBalanceBill.setCreateTime(new Date());//设置创建时间
        //公式 = 转账额 * 90%
        Double formula = balance * (1 - serviceCharge);
        //转账方获得转账额的90%的算力
        userWallet.setCalculationForce(userWalletDto.getCalculationForce().add(new BigDecimal(formula + "")));
        //保存算力变更记录
        WalletCalculatePowerRecordDto userForceBill = new WalletCalculatePowerRecordDto();
        userForceBill.setId(UUID.randomUUID().toString());//记录id
        userForceBill.setTargetUserId(user.getId());//算力变更用户id
        userForceBill.setOptUserId(user.getId());//操作的用户id
        userForceBill.setOptUserShowMsg(user.getUserName());//操作用户的账号
        userForceBill.setAmount(new BigDecimal(formula + ""));//变更的数量
        userForceBill.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
        userForceBill.setChangeType(WalletBillConstants.CHANGE_TYPE_TRANSFER);//途径：节点转账
        userForceBill.setCreateTime(new Date());//创建时间
        //更新转账人钱包数据
        this.updateWallet(userWallet);
        /***转账人数据计算、插入记录***/

        /***收款人数据计算、插入记录***/
        //收款人加余额
        WalletDto payeeWalletDto = this.getByWalletId(payyeWalletId);
        Wallet payeeWallet = new Wallet();
        payeeWallet.setId(payeeWalletDto.getId());
        //收款方获得90%转账额
        payeeWallet.setFlowBalance(payeeWalletDto.getFlowBalance().add(new BigDecimal(formula + "")));
        //保存余额变更记录
        WalletBalanceRecordDto payeeBalanceBill = new WalletBalanceRecordDto();
        payeeBalanceBill.setId(UUID.randomUUID().toString());//记录id
        payeeBalanceBill.setTargetUserId(payee.getId());//余额变更用户id
        payeeBalanceBill.setOptUserId(user.getId());//操作的用户id
        payeeBalanceBill.setOptUserShowMsg(user.getUserName());//操作用户的账号
        payeeBalanceBill.setAmount(new BigDecimal(formula + ""));//变更的数量
        payeeBalanceBill.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：减少
        payeeBalanceBill.setChangeType(WalletBillConstants.CHANGE_TYPE_TRANSFER);//途径：节点转账
        payeeBalanceBill.setEventId(userBalanceBill.getId());//设置事件触发id
        payeeBalanceBill.setCreateTime(new Date());//设置创建时间
        //公式 = 转账额 * 10%
        Double payeeForce = balance * serviceCharge;
        //收款方获得转账额10%的算力
        payeeWallet.setCalculationForce(payeeWalletDto.getCalculationForce().add(new BigDecimal(payeeForce + "")));
        //保存算力变更记录
        WalletCalculatePowerRecordDto payeeForceBill = new WalletCalculatePowerRecordDto();
        payeeForceBill.setId(UUID.randomUUID().toString());//记录id
        payeeForceBill.setTargetUserId(payee.getId());//算力变更用户id
        payeeForceBill.setOptUserId(user.getId());//操作的用户id
        payeeForceBill.setOptUserShowMsg(user.getUserName());//操作用户的账号
        payeeForceBill.setAmount(new BigDecimal(payeeForce + ""));//变更的数量
        payeeForceBill.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
        payeeForceBill.setChangeType(WalletBillConstants.CHANGE_TYPE_TRANSFER);//途径：节点转账
        payeeForceBill.setCreateTime(new Date());//创建时间
        //更新收款人钱包数据
        this.updateWallet(payeeWallet);
        /***收款人数据计算、插入记录***/

        //保存转账人余额记录
        walletBalanceRecordService.saveBalance(userBalanceBill);
        //保存转账人算力记录
        walletCalculatePowerRecordService.saveForce(userForceBill);
        //保存收款人余额记录
        walletBalanceRecordService.saveBalance(payeeBalanceBill);
        //保存收款人算力记录
        walletCalculatePowerRecordService.saveForce(payeeForceBill);

        //返回转账人余额数据
        return userWallet.getFlowBalance();
    }

    /***
     * 获取每天的智能释放的值
     * @param userId
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void headerUpdateBalance(String userId) {
        //查询用户钱包
        WalletDto walletDto = walletMapper.selectByUserId(userId);
        //操作用户信息
        UserDto optUser = userService.getById(userId);
        //查询用户等级记录
        GradeDto gradeDto = gradeMapper.selectByUserId(userId, walletDto.getCalculationForce());
        //从数据库中读取：算力最少是多少才可以获得智能释放奖励
        InformationDto informationDto = informationMapper.selectByDateName(InformationConstants.DATA_NAME_LEAST_FORCE, InformationConstants.STATUS_SHOW);
        //查询用户当天是否已经获取余额释放奖励
        int count = walletBalanceRecordMapper.countByChangeTypeOfToday(userId, WalletBillConstants.CHANGE_TYPE_AUTO_TRANSFER);
        //如果count大于0代表有领取过的记录，抛出异常
        if (count > 0) {
            throw new WalletExeption(WalletResultEnums.AUTOTRANS_AGAIN);
        }
        //如果当前用户的算力不达标达标
        if (walletDto.getCalculationForce().compareTo(new BigDecimal(informationDto.getDataValue())) < 0) {
            throw new WalletExeption(WalletResultEnums.NOT_UP_TO_STANDARD);
        }
        //用户每天可以获取的智能释放余额和扣减的算力
        BigDecimal transBalance = walletDto.getCalculationForce().multiply(gradeDto.getAutoTransfer());

        //余额变更表记录
        WalletBalanceRecord balanceRecord = new WalletBalanceRecord();
        balanceRecord.setId(UUID.randomUUID().toString());
        balanceRecord.setAmount(transBalance);
        balanceRecord.setTargetUserId(userId);
        balanceRecord.setOptUserId(userId);
        balanceRecord.setOptUserShowMsg(optUser.getUserName());
        balanceRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);
        balanceRecord.setChangeType(WalletBillConstants.CHANGE_TYPE_AUTO_TRANSFER);
        balanceRecord.setCreateTime(new Date());

        //算力变更表记录
        WalletCalculatePowerRecord forceRecord = new WalletCalculatePowerRecord();
        forceRecord.setId(UUID.randomUUID().toString());
        forceRecord.setAmount(transBalance);
        forceRecord.setTargetUserId(userId);
        forceRecord.setOptUserId(userId);
        forceRecord.setOptUserShowMsg(optUser.getUserName());
        forceRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_SUB);
        forceRecord.setChangeType(WalletBillConstants.CHANGE_TYPE_AUTO_TRANSFER);
        forceRecord.setCreateTime(new Date());

        //插入余额变更记录
        walletBalanceRecordMapper.insertSelective(balanceRecord);
        //插入算力变更记录
        walletCalculatePowerRecordMapper.insertSelective(forceRecord);
        //更新钱包数据
        walletMapper.updateBalacneById(walletDto.getId(), transBalance);
    }

    /***
     * 查询智能释放奖励的值
     * @param userId
     * @return
     */
    @Override
    public BigDecimal selectUpdateBalance(String userId) {
        UserDto userDto = userMapper.selectUserNewToday(userId);
        //查询用户是否是当天创建的新用户，如果不等于代表是新用户，则返回0
        if (userDto != null) {
            return new BigDecimal(0 + "");
        }
        //查询用户钱包
        WalletDto walletDto = walletMapper.selectByUserId(userId);
        //查询用户等级记录
        GradeDto gradeDto = gradeMapper.selectByUserId(userId, walletDto.getCalculationForce());
        //用户每天可以获取的智能释放余额和扣减的算力
        BigDecimal transBalance = walletDto.getCalculationForce().multiply(gradeDto.getAutoTransfer());
        //从数据库中读取：算力最少是多少才可以获得智能释放奖励
        InformationDto informationDto = informationMapper.selectByDateName(InformationConstants.DATA_NAME_LEAST_FORCE, InformationConstants.STATUS_SHOW);
        //判断当前用户的算力是否达标
        if (walletDto.getCalculationForce().compareTo(new BigDecimal(informationDto.getDataValue())) > 0) {
            //判断当前用户当天有没有获得过智能释放奖励
            int count = walletBalanceRecordMapper.countByChangeTypeOfToday(userId, WalletBillConstants.CHANGE_TYPE_AUTO_TRANSFER);
            if (count > 0) {
                return new BigDecimal(0 + "");
            } else {
                return transBalance;
            }
        } else {
            return new BigDecimal(0 + "");
        }
    }

    /***
     * map转Dto
     * @param resultDto
     * @return
     */
    @Deprecated
    private UserDto map2Dto(ResultDto resultDto) {
        Object userObj = resultDto.getData();
        Map<String, String> userMap = new HashMap();
        BeanUtils.copySamePropertyValue(userObj, userMap);
        UserDto userDto = BeanUtils.map2Dto(userMap, UserDto.class);
        return userDto;
    }


    /**
     * ##########huangxl###############
     */
    @Override
    @Transactional
    public void handleC2CTransfer(String userId, String telephone, Double balance, String password) {
        transferReady(userId, telephone, balance, password);
        //从数据库中读取当前平台收取的服务费
        InformationDto informationDto = informationService.getByDateName(InformationConstants.DATA_NAME_SERVICE_CHARGE_C2C);
        BigDecimal serviceCharge = new BigDecimal(informationDto.getDataValue());//平台手续费比例
        BigDecimal total = new BigDecimal(balance + "");//转账总额
        BigDecimal realReceive = total.multiply(BigDecimal.valueOf(1).subtract(serviceCharge));//实际收到 = 总额 * (1 - 手续费比例)
        BigDecimal payCharge = total.subtract(realReceive);//手续费
        Date now = new Date();//当前时间

        User user = userMapper.selectByPrimaryKey(userId);//转账人
        UserDto payee = userMapper.selectByTelephone(telephone);//收款人
        //转账人扣余额
        WalletDto userWalletDto = walletMapper.selectByUserId(userId);
        Wallet userWallet = new Wallet();
        userWallet.setId(userWalletDto.getId());
        userWallet.setFlowBalance(userWalletDto.getFlowBalance().subtract(total));
        //保存余额变更记录
        WalletBalanceRecord userBalanceBill = new WalletBalanceRecord();
        userBalanceBill.setId(UUID.randomUUID().toString());//记录id
        userBalanceBill.setTargetUserId(user.getId());//余额变更用户id
        userBalanceBill.setOptUserId(user.getId());//操作的用户id
        userBalanceBill.setOptUserShowMsg(user.getUserName());//操作用户的账号
        userBalanceBill.setAmount(total);//变更的数量
        userBalanceBill.setServiceCharge(payCharge);//手续费
        userBalanceBill.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_SUB);//该记录的类型是：减少
        userBalanceBill.setChangeType(WalletBillConstants.CHANGE_TYPE_TRANSFER);//途径：节点转账
        userBalanceBill.setCreateTime(now);//设置创建时间

        //收款人加余额
        WalletDto payeeWalletDto = walletMapper.selectByUserId(payee.getId());
        Wallet payeeWallet = new Wallet();
        payeeWallet.setId(payeeWalletDto.getId());
        payeeWallet.setFlowBalance(payeeWalletDto.getFlowBalance().add(realReceive));//增加余额
        //保存余额变更记录
        WalletBalanceRecord payeeBalanceBill = new WalletBalanceRecord();
        payeeBalanceBill.setId(UUID.randomUUID().toString());//记录id
        payeeBalanceBill.setTargetUserId(payee.getId());//余额变更用户id
        payeeBalanceBill.setOptUserId(user.getId());//操作的用户id
        payeeBalanceBill.setOptUserShowMsg(user.getUserName());//操作用户的账号
        payeeBalanceBill.setAmount(realReceive);//变更的数量
        payeeBalanceBill.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
        payeeBalanceBill.setChangeType(WalletBillConstants.CHANGE_TYPE_TRANSFER);//途径：节点转账
        payeeBalanceBill.setEventId(userBalanceBill.getId());//设置事件触发id
        payeeBalanceBill.setCreateTime(now);//设置创建时间

        //保存转账人余额记录
        walletMapper.updateByPrimaryKeySelective(userWallet);
        walletMapper.updateByPrimaryKeySelective(payeeWallet);
        //保存余额变更记录
        walletBalanceRecordMapper.insertSelective(userBalanceBill);
        walletBalanceRecordMapper.insertSelective(payeeBalanceBill);
    }

    @Override
    @Transactional
    public void handleScanQRCodeTransfer(String userId, String telephone, Double balance, String password) {
        transferReady(userId, telephone, balance, password);
        //从数据库中读取当前平台收取的服务费
        InformationDto informationDto = informationService.getByDateName(InformationConstants.DATA_NAME_SERVICE_CHARGE);
        BigDecimal serviceCharge = new BigDecimal(informationDto.getDataValue());//平台手续费比例
        BigDecimal total = new BigDecimal(balance + "");//转账总额
        BigDecimal realReceive = total.multiply(BigDecimal.valueOf(1).subtract(serviceCharge));//实际收到 = 总额 * (1 - 手续费比例)
        BigDecimal payCharge = total.subtract(realReceive);//手续费
        Date now = new Date();//当前时间

        User user = userMapper.selectByPrimaryKey(userId);//转账人
        UserDto payee = userMapper.selectByTelephone(telephone);//收款人
        //转账人扣余额
        WalletDto userWalletDto = walletMapper.selectByUserId(userId);
        Wallet userWallet = new Wallet();
        userWallet.setId(userWalletDto.getId());
        userWallet.setFlowBalance(userWalletDto.getFlowBalance().subtract(total));//增加余额
        userWallet.setCalculationForce(userWalletDto.getCalculationForce().add(realReceive));//增加算力
        //保存转账人余额变更记录
        WalletBalanceRecord userBalanceBill = new WalletBalanceRecord();
        userBalanceBill.setId(UUID.randomUUID().toString());//记录id
        userBalanceBill.setTargetUserId(userId);//余额变更用户id
        userBalanceBill.setOptUserId(userId);//操作的用户id
        userBalanceBill.setOptUserShowMsg(user.getUserName());//操作用户的账号
        userBalanceBill.setAmount(total);//变更的数量
        userBalanceBill.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_SUB);//该记录的类型是：减少
        userBalanceBill.setChangeType(WalletBillConstants.CHANGE_TYPE_TRANSFER_SCAN_OUT);//途径：扫码支付
        userBalanceBill.setCreateTime(now);//设置创建时间
        //保存转账人算力变更记录
        WalletCalculatePowerRecord userPowerRecord = new WalletCalculatePowerRecord();
        userPowerRecord.setId(UUID.randomUUID().toString());
        userPowerRecord.setTargetUserId(userId);//算力变更用户id
        userPowerRecord.setOptUserId(userId);//操作的用户id
        userPowerRecord.setOptUserShowMsg(user.getUserName());//操作用户的账号
        userPowerRecord.setAmount(realReceive);//变更的数量,数额=收款人实际收到的余额数
        userPowerRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
        userPowerRecord.setChangeType(WalletBillConstants.CHANGE_TYPE_TRANSFER_SCAN_OUT);//途径：扫码支付
        userPowerRecord.setEventId(userBalanceBill.getId());
        userPowerRecord.setCreateTime(now);//创建时间

        //收款人加余额
        WalletDto payeeWalletDto = walletMapper.selectByUserId(payee.getId());
        Wallet payeeWallet = new Wallet();
        payeeWallet.setId(payeeWalletDto.getId());
        payeeWallet.setFlowBalance(payeeWalletDto.getFlowBalance().add(realReceive));//增加余额
        payeeWallet.setCalculationForce(payeeWalletDto.getCalculationForce().add(payCharge));//增加算力
        //保存收款人余额变更记录
        WalletBalanceRecord payeeBalanceBill = new WalletBalanceRecord();
        payeeBalanceBill.setId(UUID.randomUUID().toString());//记录id
        payeeBalanceBill.setTargetUserId(payee.getId());//余额变更用户id
        payeeBalanceBill.setOptUserId(user.getId());//操作的用户id
        payeeBalanceBill.setOptUserShowMsg(user.getUserName());//操作用户的账号
        payeeBalanceBill.setAmount(realReceive);//变更的数量
        payeeBalanceBill.setServiceCharge(payCharge);//平台收取的手续费
        payeeBalanceBill.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
        payeeBalanceBill.setChangeType(WalletBillConstants.CHANGE_TYPE_TRANSFER_SCAN_IN);//途径：二维码收款
        payeeBalanceBill.setEventId(userBalanceBill.getId());//设置事件触发id
        payeeBalanceBill.setCreateTime(now);//设置创建时间
        //保存收款人算力变更记录
        WalletCalculatePowerRecord payeePowerRecord = new WalletCalculatePowerRecord();
        payeePowerRecord.setId(UUID.randomUUID().toString());
        payeePowerRecord.setTargetUserId(payee.getId());//算力变更用户id
        payeePowerRecord.setOptUserId(userId);//操作的用户id
        payeePowerRecord.setOptUserShowMsg(user.getUserName());//操作用户的账号
        payeePowerRecord.setAmount(payCharge);//变更的数量,数额=平台扣除的余额手续费
        payeePowerRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
        payeePowerRecord.setChangeType(WalletBillConstants.CHANGE_TYPE_TRANSFER_SCAN_IN);//途径：二维码收款
        payeePowerRecord.setEventId(userBalanceBill.getId());//设置触发事件
        payeePowerRecord.setCreateTime(now);//创建时间

        //保存转账人余额记录
        walletMapper.updateByPrimaryKeySelective(userWallet);
        walletMapper.updateByPrimaryKeySelective(payeeWallet);
        //保存余额变更记录
        walletBalanceRecordMapper.insertSelective(userBalanceBill);
        walletBalanceRecordMapper.insertSelective(payeeBalanceBill);
        //保存算力变更记录
        walletCalculatePowerRecordMapper.insertSelective(userPowerRecord);
        walletCalculatePowerRecordMapper.insertSelective(payeePowerRecord);
    }

    private void transferReady(String userId, String telephone, Double balance, String password) {
        //检测转账金额不能为空
        ExceptionPreconditionUtils.checkNotNull(balance,
                new WalletExeption(WalletResultEnums.TRANSFER_AMOUNT_NULL));
        //检测转换余额不能小于0
        WalletExceptionPreconditionUtils.checkNotZero(balance,
                new WalletExeption(WalletResultEnums.TARANSFORM_TRANSBALANCE_ZERO));
        //判断支付密码是否为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(password,
                new BaseException(BaseResultEnums.PAYPASSWORD_NULL));
        //转账手机号不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(password,
                new BaseException(BaseResultEnums.USER_NULL));

        /***获取转账人信息***/
        User user = userMapper.selectByPrimaryKey(userId);
        //判断转账人是否存在
        ExceptionPreconditionUtils.checkNotNull(user, new UserExeption(UserResultEnums.USER_NULL));
        //获取转账人钱包
        WalletDto userWallet = walletMapper.selectByUserId(userId);// this.getByUserId(userId);
        //检测转账余额不能大于当前余额
        WalletExceptionPreconditionUtils.checkNotBeyond(balance, userWallet.getFlowBalance(),
                new WalletExeption(WalletResultEnums.TARANSFORM_TRANSBALANCE_BEYOND));
        //判断支付密码是否正确
        String md5Password = MD5Utils.MD5(password);
        ExceptionPreconditionUtils.checkArguments(user.getPaymentPassword().equals(md5Password),
                new BaseException(BaseResultEnums.PAYPASSWORD_ERROR));

        UserDto payee = userMapper.selectByTelephone(telephone);
        //判断收款人是否存在
        ExceptionPreconditionUtils.checkNotNull(payee, new UserExeption(UserResultEnums.USER_NULL));
        //判断收款人和转账人是否相同（防止自己转给自己）
        if (telephone.equals(user.getUserName())) {
            throw new WalletExeption(WalletResultEnums.USER_REPETITION);
        }
    }


    /******************************************************************后台管理用，改变算力、余额 hugq******************************************************************/
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Integer updateCalculationForceForBackend(BackendUserDTO backendUserDTO) {
        int countU = walletMapper.updateCalculationForceForBackend(backendUserDTO);  //修改算力

        WalletCalculatePowerRecord walletCalculatePowerRecord = new WalletCalculatePowerRecord();
        walletCalculatePowerRecord.setId(UUID.randomUUID().toString());//记录id
        walletCalculatePowerRecord.setTargetUserId(backendUserDTO.getId());//算力变更用户id
        walletCalculatePowerRecord.setOptUserId("后台管理员修改");//操作的用户id
        walletCalculatePowerRecord.setEventId("后台管理员修改");//触发事件id
        walletCalculatePowerRecord.setOptUserShowMsg("后台管理员修改");//操作用户的账号
        walletCalculatePowerRecord.setAmount(backendUserDTO.getCpAmount());//变更的数量
        walletCalculatePowerRecord.setArithmeticType(backendUserDTO.getCpArithmeticType());//该记录的类型是：增加/减少
        walletCalculatePowerRecord.setChangeType(WalletBillConstants.CHANGE_TYPE_UPDATE_BACKEND);//途径：后台修改
        walletCalculatePowerRecord.setCreateTime(new Date());//创建时间
        int countI = walletCalculatePowerRecordMapper.insertSelective(walletCalculatePowerRecord);  //保存智能算力修改记录

        return countU + countI == 2 ? 1 : null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Integer updateFlowBalanceForBackend(BackendUserDTO backendUserDTO) {
        int countU = walletMapper.updateFlowBalanceForBackend(backendUserDTO);  //修改余额

        WalletBalanceRecord walletBalanceRecord = new WalletBalanceRecord();
        walletBalanceRecord.setId(UUID.randomUUID().toString());//记录id
        walletBalanceRecord.setTargetUserId(backendUserDTO.getId());//余额变更用户id
        walletBalanceRecord.setOptUserId("后台管理员修改");//操作的用户id
        walletBalanceRecord.setOptUserShowMsg("后台管理员修改");//操作用户的账号
        walletBalanceRecord.setAmount(backendUserDTO.getFbAmount());//变更的数量
        walletBalanceRecord.setArithmeticType(backendUserDTO.getFbArithmeticType());//该记录的类型是：增加/减少
        walletBalanceRecord.setChangeType(WalletBillConstants.CHANGE_TYPE_UPDATE_BACKEND);//途径：后台管理员修改
        walletBalanceRecord.setCreateTime(new Date());//创建时间
        int countI = walletBalanceRecordMapper.insertSelective(walletBalanceRecord);  //保存流动余额修改记录

        return countU + countI == 2 ? 1 : null;

    }

    @Override
    public UserBalance findUserBalance(String userId) {
        if(StringUtils.isBlank(userId)){
            throw new PayException(PayResultEnums.HTTP_PARAM_NULL);//请求参数不能为空
        }
        UserBalance userBalance = walletMapper.findUserBalance(userId);
        return userBalance;
    }

}
