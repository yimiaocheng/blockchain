package com.cwt.persistent.mapper;

import com.cwt.domain.dto.wallet.WalletBillDto;
import com.cwt.domain.entity.WalletBill;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * WalletBillMapper 数据访问类
 * @date 2018-08-13
 * @version 1.0
 */
@Repository
@Deprecated
public interface WalletBillMapper extends Mapper<WalletBill> {
    /**
     * 根据转账人id、类型查詢
     *
     * @param userId
     * @return
     */
    List<WalletBillDto> selectByIdAndBillType(@Param("userId") String userId, @Param("billType") String billType);

    /**
     * 根据收款人id、类型查詢
     *
     * @param beUserId
     * @return
     */
    List<WalletBillDto> selectByBeIdAndBillType(@Param("beUserId") String beUserId, @Param("billType") String billType);

    /**
     * 查询当天所有交易记录
     * @return
     */
    Double selectToday();

}