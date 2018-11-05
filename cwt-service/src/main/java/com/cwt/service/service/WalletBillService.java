package com.cwt.service.service;


import com.cwt.domain.dto.wallet.WalletBillDto;

import java.util.List;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/7/21 16:48
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
@Deprecated
public interface WalletBillService {
    /**
     * 根据转账人id、交易记录类型获取记录信息
     *
     * @param userId
     * @param billType
     * @return
     */
    List<WalletBillDto> getByIdAndBillType(String userId, String billType);

    /**
     * 根据收款人id、交易记录类型获取记录信息
     *
     * @param beUserId
     * @param billType
     * @return
     */
    List<WalletBillDto> getByBeIdAndBillType(String beUserId, String billType);

    /**
     * 查询当天所有交易记录
     * @return
     */
    Double getToday();

    /**
     * 保存交易记录
     * @param dto
     * @return
     */
    WalletBillDto save(WalletBillDto dto);
}
