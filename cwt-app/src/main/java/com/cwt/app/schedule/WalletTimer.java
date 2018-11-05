package com.cwt.app.schedule;

import com.cwt.app.common.util.RedisUtils;
import com.cwt.common.constant.BaseConstants;
import com.cwt.common.constant.UrlConstants;
import com.cwt.common.enums.PayResultEnums;
import com.cwt.common.exception.PayException;
import com.cwt.domain.constant.InformationConstants;
import com.cwt.domain.constant.UserAwardConstants;
import com.cwt.domain.constant.WalletBillConstants;
import com.cwt.domain.dto.information.InformationDto;
import com.cwt.domain.dto.user.UserDto;
import com.cwt.domain.dto.wallet.WalletBalanceRecordDto;
import com.cwt.domain.entity.IntegralRecord;
import com.cwt.domain.entity.UserAward;
import com.cwt.domain.entity.WalletCalculatePowerRecord;
import com.cwt.persistent.mapper.*;
import com.cwt.service.service.UserService;
import com.cwt.service.service.WalletBalanceRecordService;
import com.github.pagehelper.PageHelper;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class WalletTimer {
    private static final Logger LOG = LoggerFactory.getLogger(WalletTimer.class);
    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private WalletBalanceRecordMapper walletBalanceRecordMapper;
    @Autowired
    private WalletCalculatePowerRecordMapper walletCalculatePowerRecordMapper;
    @Autowired
    private InformationMapper informationMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAwardMapper userAwardMapper;
    @Autowired
    private IntegralRecordMapper integralRecordMapper;
    @Autowired
    private WalletBalanceRecordService walletBalanceRecordService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RestTemplate restTemplate;

    /***
     * 算力智能释放余额(每天)
     */
    /*@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Scheduled(cron = " 0 0 0 * * ?")
    public void autoTransfer() {
        int count = userMapper.countByMinBalance(1000);
        if (count < 1) {
            return;
        }
        long start = System.currentTimeMillis();
        //保存智能释放的记录
        walletBalanceRecordMapper.savaAutoUpdateBalanceBill();
        LOG.info("walletBalanceRecordMapper.savaAutoUpdateBalanceBill");
        //保存算力扣减记录
        walletCalculatePowerRecordMapper.insertAutoFreeBalanceRecord();
        LOG.info("walletCalculatePowerRecordMapper.insertAutoFreeBalanceRecord");
        //通过SQL语句完成算力智能释放功能
        walletMapper.autoUpdateBalance();
        LOG.info("walletMapper.autoUpdateBalance");
        LOG.info("======算力释放花费时间===="+(System.currentTimeMillis()-start));
    }*/

    /***
     * 加权奖(每天)
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Scheduled(cron = " 0 0 8 * * ?")
    public void weightingAward() {
        //查询社区矿管的人数
        Integer headCount = walletMapper.selectHeadCount();
        if (headCount == null || headCount == 0) {
            return;
        }
        long start = System.currentTimeMillis();
        LOG.info("社区矿管人数：" + headCount);
        //查询出当天所有的余额兑换的总额
        BigDecimal transToday = walletCalculatePowerRecordMapper.selectToday();
        LOG.info("当天增长的余额数：" + transToday);
        //从数据库中读取当前平台加权奖的比率和封顶值
        InformationDto weightingRatioDto = informationMapper.selectByDateName(InformationConstants.DATA_NAME_WEIGHTING_RATIO, InformationConstants.STATUS_SHOW);//加权奖比例
        InformationDto weightingTopDto = informationMapper.selectByDateName(InformationConstants.DATA_NAME_WEIGHTING_TOP, null);//封顶值
        //比率
        BigDecimal weightingRatio = new BigDecimal(weightingRatioDto.getDataValue());
        //封顶值
        BigDecimal weightingTop = new BigDecimal(weightingTopDto.getDataValue());

        BigDecimal awardToday = null;//奖励总额
        if (weightingTopDto.getStatus() == InformationConstants.STATUS_SHOW) {
            //有封顶值
            awardToday = transToday.min(weightingTop).multiply(weightingRatio);
        } else {
            //无封顶值
            awardToday = transToday.multiply(weightingRatio);
        }
        //计算每位矿管拿多少奖励
        BigDecimal award = awardToday.divide(BigDecimal.valueOf(headCount), 2, BigDecimal.ROUND_HALF_UP);
        //更新加权奖
        walletMapper.updateWeightingAward(award);
        //保存加权奖记录
        walletCalculatePowerRecordMapper.saveWeightingAwardBill(award);
        LOG.info("======加权奖运算时间====" + (System.currentTimeMillis() - start));
    }

    @Scheduled(cron = " 0 0 0 * * ?")
    public void getCoinMarker() {
//        System.out.println("##########");
        ResponseEntity<JSONObject> entityBTC = restTemplate.getForEntity(UrlConstants.BTC_PRICE_URL, JSONObject.class);
        ResponseEntity<JSONObject> entityETH = restTemplate.getForEntity(UrlConstants.ETH_PRICE_URL, JSONObject.class);
        if (200 == (entityBTC.getStatusCodeValue())) {
            JSONObject jsonBTC = entityBTC.getBody();
            Double btcAmount = jsonBTC.getJSONObject("data").getDouble("amount");
            redisUtils.setKeyValue(BaseConstants.LAST_TIME_BTC_PRICE_KEY, btcAmount, BaseConstants.COIN_MARKER_CACHE_TIME_DEFAULT);
        }
        if (200 == (entityETH.getStatusCodeValue())) {
            JSONObject jsonETH = entityETH.getBody();
            Double ethAmount = jsonETH.getJSONObject("data").getDouble("amount");
            redisUtils.setKeyValue(BaseConstants.LAST_TIME_ETH_PRICE_KEY, ethAmount, BaseConstants.COIN_MARKER_CACHE_TIME_DEFAULT);
        }
    }

    /**
     * 发放奖金表进行处理（除了 4加权分红 和 1销售提成） 2分钟处理50条   销售提成是立即处理的
     */
    /*@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Scheduled(cron = "0 0/5 * * * ?")
    public void UserAward(){
        LOG.info("处理发放奖金表开始");
        //首先查询出未处理的数据50条
        PageHelper.startPage(0,50);
        List<UserAward> userAwardList = userAwardMapper.selectUserAwardByStatus(
                UserAwardConstants.AWARD_STATUS_UNISSUED,
                UserAwardConstants.AWARD_TYPE_WEIGHTED,
                UserAwardConstants.AWARD_TYPE_SALES);//状态不为4和1

        if(userAwardList!=null&&userAwardList.size()>0){
            for (UserAward userAward:userAwardList){
                //获取用户发放奖金信息
                String id = userAward.getId();
                String userId = userAward.getUserId();
                BigDecimal award_ct = userAward.getAward_ct();
                BigDecimal award_integral = userAward.getAward_integral();
                String award_type = userAward.getAward_type();
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
                //给用户账户发放奖金
                int count1 = walletMapper.updateFlowBalanceAndIntegral(userId,award_ct,award_integral,new Date());
                if(count1!=1){
                    throw new PayException(PayResultEnums.AWARD_FAIL);
                }
                //更新奖金发放状态
                int count2 = userAwardMapper.updateUserAwardStatusById(id,UserAwardConstants.AWARD_STATUS_ISSUED,new Date());//已发放
                if(count2!=1){
                    throw new PayException(PayResultEnums.AWARD_FAIL);
                }
                if(!"0".equals(award_ct)){
                    //保存ct变更记录
                    WalletBalanceRecordDto payeeBalanceBill = new WalletBalanceRecordDto();
                    payeeBalanceBill.setId(UUID.randomUUID().toString());//记录id
                    payeeBalanceBill.setTargetUserId(userId);//余额变更用户id
                    payeeBalanceBill.setOptUserId(userId);//操作的用户id
                    payeeBalanceBill.setOptUserShowMsg(userId);//操作用户name
                    payeeBalanceBill.setAmount(award_ct);//变更的数量
                    payeeBalanceBill.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
                    payeeBalanceBill.setChangeType(changeType);//途径：奖金发放时，用户增加ct
                    payeeBalanceBill.setServiceCharge(new BigDecimal("0.00"));
                    payeeBalanceBill.setSubsidy(new BigDecimal("0.00"));
                    payeeBalanceBill.setCreateTime(new Date());//设置创建时间

                    walletBalanceRecordService.saveBalance(payeeBalanceBill);
                }
                if(!"0".equals(award_integral)){
                    //保存积分变更记录
                    IntegralRecord integralRecord = new IntegralRecord();
                    integralRecord.setId(UUID.randomUUID().toString());
                    integralRecord.setUserId(userId);
                    integralRecord.setTotalNum(award_integral);//扣掉的积分
                    integralRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
                    integralRecord.setChangeType(changeType);//途径：奖金发放时，用户增加积分
                    integralRecord.setCreateTime(new Date());
                    integralRecord.setModifyTime(new Date());

                    integralRecordMapper.insertSelective(integralRecord);
                }
            }
        }
        LOG.info("处理发放奖金表结束");
    }*/

    /**
     * 加权分红  发放奖金表进行处理  月结
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Scheduled(cron = "0 0/2 * 1 * ?")//每月1号凌晨开始执行  2分钟执行一次
    public void UserAwardToWeighted(){
        LOG.info("处理发放奖金表（加权分红）开始");
        PageHelper.startPage(0,50);
        List<UserAward> userAwardList = userAwardMapper.selectUserAward(UserAwardConstants.AWARD_STATUS_UNISSUED,UserAwardConstants.AWARD_TYPE_WEIGHTED);//状态为4
        if(userAwardList!=null&&userAwardList.size()>0){
            for (UserAward userAward:userAwardList){
                //获取用户发放奖金信息
                String id = userAward.getId();
                String userId = userAward.getUserId();
                //操作用户信息
                UserDto optUser = userService.getById(userId);
                BigDecimal award_ct = userAward.getAward_ct();
                BigDecimal award_integral = userAward.getAward_integral();
                //给用户账户发放奖金
                int count1 = walletMapper.updateFlowBalanceAndIntegral(userId,award_ct,award_integral,new Date());
                if(count1!=1){
                    throw new PayException(PayResultEnums.AWARD_FAIL);
                }
                //更新奖金发放状态
                int count2 = userAwardMapper.updateUserAwardStatusById(id,UserAwardConstants.AWARD_STATUS_ISSUED,new Date());//已发放
                if(count2!=1){
                    throw new PayException(PayResultEnums.AWARD_FAIL);
                }
                if(!"0".equals(award_ct)){
                    //保存ct变更记录
                    WalletBalanceRecordDto payeeBalanceBill = new WalletBalanceRecordDto();
                    payeeBalanceBill.setId(UUID.randomUUID().toString());//记录id
                    payeeBalanceBill.setTargetUserId(userId);//余额变更用户id
                    payeeBalanceBill.setOptUserId(userId);//操作的用户id
                    payeeBalanceBill.setOptUserShowMsg(optUser.getUserName());//操作用户name
                    payeeBalanceBill.setAmount(award_ct);//变更的数量
                    payeeBalanceBill.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
                    payeeBalanceBill.setChangeType(WalletBillConstants.USER_AWARD_FROM_ADD_4);//途径：奖金发放时，用户增加ct
                    payeeBalanceBill.setServiceCharge(new BigDecimal("0.00"));
                    payeeBalanceBill.setSubsidy(new BigDecimal("0.00"));
                    payeeBalanceBill.setCreateTime(new Date());//设置创建时间

                    walletBalanceRecordService.saveBalance(payeeBalanceBill);
                }
                if(!"0".equals(award_integral)){
                    //保存积分变更记录
                    IntegralRecord integralRecord = new IntegralRecord();
                    integralRecord.setId(UUID.randomUUID().toString());
                    integralRecord.setUserId(userId);
                    integralRecord.setTotalNum(award_integral);//扣掉的积分
                    integralRecord.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
                    integralRecord.setChangeType(WalletBillConstants.USER_AWARD_FROM_ADD_4);//途径：奖金发放时，用户增加积分
                    integralRecord.setCreateTime(new Date());
                    integralRecord.setModifyTime(new Date());

                    integralRecordMapper.insertSelective(integralRecord);
                }

            }
        }
        LOG.info("处理发放奖金（加权分红）表结束");
    }

}
