package com.cwt.persistent.mapper;

import com.cwt.domain.dto.gameCoin.GameCoinDTO;
import com.cwt.domain.dto.wallet.GameCurrencyRecordDto;
import com.cwt.domain.entity.GameCurrencyRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * GameCurrencyRecordMapper 数据访问类
 *
 * @version 1.0
 * @date 2018-10-16 09:44:49
 */
@Repository
public interface GameCurrencyRecordMapper extends Mapper<GameCurrencyRecord> {

    /***
     * 根据条件查询游戏币交易记录
     * @param userId
     * @param changeType
     * @param arithmeticType
     * @return
     */
    List<GameCurrencyRecordDto> selectGameCurrencyRecordByCondition(@Param("userId") String userId, @Param("changeType") Integer changeType, @Param("arithmeticType") Integer arithmeticType);

    /********************************后台管理用********************************/

    /**
     * 查询游戏代币记录
     *
     * @param integralDTO 查询条件
     * @return 游戏代币集合
     */
    List<GameCoinDTO> selectGameCoinListForBackend(GameCoinDTO integralDTO);
}