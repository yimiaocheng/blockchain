package com.cwt.service.service.impl;

import com.cwt.domain.constant.GameCoinConstants;
import com.cwt.domain.constant.WalletBillConstants;
import com.cwt.domain.dto.gameCoin.GameCoinDTO;
import com.cwt.domain.dto.wallet.GameCurrencyRecordDto;
import com.cwt.persistent.mapper.GameCurrencyRecordMapper;
import com.cwt.service.service.GameCurrencyRecordService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("gameCurrencyRecordService")
public class GameCurrencyRecordServiceImpl implements GameCurrencyRecordService {

    @Autowired
    private GameCurrencyRecordMapper gameCurrencyRecordMapper;

    /***
     * 查询游戏币转换记录
     * 查询条件 订单状态：减少、变动类型：转换算力
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<GameCurrencyRecordDto> getGameCoinTransBill(String userId, Integer pageNum, Integer pageSize) {
        if (pageSize == null || pageSize == 0) {
            //默认10条
            pageSize = 10;
        }
        //开始分页
        PageHelper.startPage(pageNum, pageSize);
        return gameCurrencyRecordMapper.selectGameCurrencyRecordByCondition(userId, GameCoinConstants.CT_TO_GAME_COIN, WalletBillConstants.ARITHMETIC_TYPE_SUB);
    }

    /**
     * 查询游戏代币记录
     *
     * @param integralDTO 查询条件
     * @return 游戏代币集合
     */
    @Override
    public List<GameCoinDTO> selectGameCoinListForBackend(GameCoinDTO integralDTO) {
        return gameCurrencyRecordMapper.selectGameCoinListForBackend(integralDTO);
    }
}
