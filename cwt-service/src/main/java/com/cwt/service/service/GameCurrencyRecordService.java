package com.cwt.service.service;

import com.cwt.domain.dto.gameCoin.GameCoinDTO;
import com.cwt.domain.dto.wallet.GameCurrencyRecordDto;

import java.util.List;

public interface GameCurrencyRecordService {

    /***
     * 查询用户游戏币兑换记录
     * @param userId
     * @return
     */
    List<GameCurrencyRecordDto> getGameCoinTransBill(String userId, Integer pageNum, Integer pageSize);


    /********************************后台管理用********************************/

    /**
     * 查询游戏代币记录
     *
     * @param integralDTO 查询条件
     * @return 游戏代币集合
     */
    List<GameCoinDTO> selectGameCoinListForBackend(GameCoinDTO integralDTO);
}
