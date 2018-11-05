package com.cwt.service.service.impl;

import com.cwt.common.util.BeanUtils;
import com.cwt.common.util.MD5Utils;
import com.cwt.domain.dto.wallet.WalletBillDto;
import com.cwt.domain.entity.WalletBill;
import com.cwt.persistent.mapper.WalletBillMapper;
import com.cwt.service.service.WalletBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/7/21 16:49
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
@Deprecated
@Service("walletBillService")
public class WalletBillServiceImpl implements WalletBillService {

    @Autowired
    private WalletBillMapper walletBillMapper;


    /***
     * 根据用户id、类型查詢
     * @param userId
     * @param billType
     * @return
     */
    @Override
//    @Cacheable(value = "WALLET_BILL_CACHE",key = "'WALLET_BILL_CACHE_' + #userId + #billType")
    public List<WalletBillDto> getByIdAndBillType(String userId, String billType) {
        return walletBillMapper.selectByIdAndBillType(userId, billType);
    }

    /**
     * 根据收款人id、交易记录类型获取记录信息
     *
     * @param beUserId
     * @param billType
     * @return
     */
    @Override
    public List<WalletBillDto> getByBeIdAndBillType(String beUserId, String billType) {
        return walletBillMapper.selectByBeIdAndBillType(beUserId,billType);
    }

    /***
     * 查询当天所有交易记录
     * @return
     */
    @Override
    public Double getToday() {
        return walletBillMapper.selectToday();
    }

    /**
     * 保存交易记录
     * @param dto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
//    @CachePut(value = "WALLET_BILL_CACHE", key = "'WALLET_BILL_CACHE_' + #result.userId + #result.billType")
    public WalletBillDto save(WalletBillDto dto) {
        dto.setId(MD5Utils.MD5(UUID.randomUUID().toString()));
        Date date = new Date();
        dto.setCreateTime(date);
        WalletBill walletBill = new WalletBill();
        BeanUtils.copySamePropertyValue(dto, walletBill);
        walletBillMapper.insert(walletBill);
        return dto;
    }
}
