package com.cwt.service.service.impl;

import com.cwt.domain.constant.InformationConstants;
import com.cwt.domain.constant.WalletBillConstants;
import com.cwt.domain.dto.balance.BalanceRecordListConditionDto;
import com.cwt.domain.dto.balance.BalanceRecordListDto;
import com.cwt.domain.dto.balance.CenterAccountConditionDto;
import com.cwt.domain.dto.information.InformationDto;
import com.cwt.domain.dto.wallet.*;
import com.cwt.domain.entity.WalletBalanceRecord;
import com.cwt.persistent.mapper.InformationMapper;
import com.cwt.persistent.mapper.UserMapper;
import com.cwt.persistent.mapper.WalletBalanceRecordMapper;
import com.cwt.persistent.mapper.WalletMapper;
import com.cwt.service.service.WalletBalanceRecordService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service("walletBalanceRecordService")
public class WalletBalanceRecordServiceImpl implements WalletBalanceRecordService {

    @Autowired
    private WalletBalanceRecordMapper walletBalanceRecordMapper;
    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private InformationMapper informationMapper;

    //保存余额变更记录
    @Override
    public void saveBalance(WalletBalanceRecordDto walletBalanceRecordDto) {
        WalletBalanceRecord walletBalanceRecord = new WalletBalanceRecord();
        BeanUtils.copyProperties(walletBalanceRecordDto, walletBalanceRecord);
        walletBalanceRecordMapper.insert(walletBalanceRecord);
    }

    /***
     * 查询用户转账记录
     * @param userId
     * @return
     */
    @Override
    public List<WalletBalanceTransferOutDto> getTransferBill(String userId, Integer pageNum, Integer pageSize) {
        if (pageSize == null || pageSize == 0) {
            //默认10条
            pageSize = 10;
        }
        //开始分页
        PageHelper.startPage(pageNum, pageSize);
        return walletBalanceRecordMapper.selectTransferBill(userId, WalletBillConstants.CHANGE_TYPE_TRANSFER_SCAN_IN, WalletBillConstants.CHANGE_TYPE_TRANSFER);
    }

    /***
     * 查询用户收款记录
     * @param userId
     * @return
     */
    @Override
    public List<WalletBalanceTransferInDto> getCollectingBill(String userId, Integer pageNum, Integer pageSize) {
        if (pageSize == null || pageSize == 0) {
            //默认10条
            pageSize = 10;
        }
        //开始分页
        PageHelper.startPage(pageNum, pageSize);
        return walletBalanceRecordMapper.selectCollectingBill(userId, WalletBillConstants.CHANGE_TYPE_TRANSFER_SCAN_IN, WalletBillConstants.CHANGE_TYPE_TRANSFER);
    }

    /***
     * 查询用户余额兑换记录
     * @param userId
     * @return
     */
    @Override
    public List<GetBillDto> getTransBill(String userId, Integer pageNum, Integer pageSize) {
        if (pageSize == null || pageSize == 0) {
            //默认10条
            pageSize = 10;
        }
        //开始分页
        PageHelper.startPage(pageNum, pageSize);
        return walletBalanceRecordMapper.selectTransBill(userId);
    }

    /***
     * 查询用户余额释放记录
     * @param userId
     * @return
     */
    @Override
    public List<GetBillDto> getAutoUpdateBalanceBill(String userId) {
        return walletBalanceRecordMapper.selectAutoUpdateBalanceBill(userId);
    }

    /***
     * 查询当前用户所有余额记录
     * @param userId
     * @return
     */
    @Override
    public List<BalanceRecordListAllDto> listAll(String userId, Integer pageNum, Integer pageSize) {
        if (pageSize == null || pageSize == 0) {
            //默认10条
            pageSize = 10;
        }
        //开始分页
        PageHelper.startPage(pageNum, pageSize);
        return walletBalanceRecordMapper.listAll(userId);
    }

    /***
     * 判断用户是否有资格获得奖励和当天是否有获得过奖励
     * @param userId 用户id
     * @return
     */
    @Override
    public boolean countByFreeBalanceRecordOfToday(String userId) {
        //用户钱包
        WalletDto walletDto = walletMapper.selectByUserId(userId);
        //从数据库中读取：算力最少是多少才可以获得智能释放奖励
        InformationDto informationDto = informationMapper.selectByDateName(InformationConstants.DATA_NAME_LEAST_FORCE, InformationConstants.STATUS_SHOW);
        //判断当前用户的算力是否达标
        if (walletDto.getCalculationForce().compareTo(new BigDecimal(informationDto.getDataValue())) > 0) {
            //判断当前用户当天有没有获得过智能释放奖励
            int flag = walletBalanceRecordMapper.countByChangeTypeOfToday(userId, WalletBillConstants.CHANGE_TYPE_AUTO_TRANSFER);
            if (flag > 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * ####################huangxl##############
     * 后台系统功能
     */
    @Override
    public List<BalanceRecordListDto> listByCondition(BalanceRecordListConditionDto condition) {
        return walletBalanceRecordMapper.listByCondition(condition);
    }

    @Override
    public List<CenterAccountDto> centerAccountCount() {
        return walletBalanceRecordMapper.centerAccountList(new CenterAccountConditionDto());
    }

    @Override
    public List<CenterAccountDto> centerAccountDetail(CenterAccountConditionDto centerAccountConditionDto) {
        return walletBalanceRecordMapper.centerAccountDetail(centerAccountConditionDto);
    }

    @Override
    public BigDecimal getSurplusCTAccount() {
        BigDecimal nowCTAccountTotal = walletBalanceRecordMapper.nowCTAccountTotal(new CenterAccountConditionDto());
        InformationDto informationDto = informationMapper.selectByDateName(InformationConstants.DATA_NAME_CENTER_ACCOUNT, InformationConstants.STATUS_SHOW);

        return new BigDecimal(informationDto.getDataValue()).subtract(nowCTAccountTotal);
    }
}
