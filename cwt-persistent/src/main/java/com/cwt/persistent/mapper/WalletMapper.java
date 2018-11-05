package com.cwt.persistent.mapper;

import com.cwt.domain.dto.user.BackendUserDTO;
import com.cwt.domain.dto.wallet.WalletDto;
import com.cwt.domain.dto.wallet.transfor.TransForDto;
import com.cwt.domain.entity.UserAward;
import com.cwt.domain.entity.UserBalance;
import com.cwt.domain.entity.Wallet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * WalletMapper 数据访问类
 *
 * @version 1.0
 * @date 2018-08-09 17:22:37
 */
@Repository
public interface WalletMapper extends Mapper<Wallet> {
    /***
     * 根据用户id获取钱包信息
     * @param userId
     * @return WalletDto
     */
    WalletDto selectByUserId(@Param("userId") String userId);

    /***
     * 根据钱包id(主键)获取钱包信息
     * @param walletId
     * @return WalletDto
     */
    WalletDto selectByWalletId(@Param("walletId") String walletId);

    /***
     * 查询所有钱包信息
     * @return List<WalletDto>
     */
    List<WalletDto> selectAllWallet();

    /**
     * 查询社区矿管总人数
     *
     * @return
     */
    Integer selectHeadCount();

    /***
     * 根据钱包id(主键)更新钱包信息
     * @param
     */
    void updateByWalletId(Wallet wallet);

    /***
     * 通过SQL语句完成算力智能释放功能
     */
    void autoUpdateBalance();

    /***
     * 计算推荐人奖励
     */
    void updateAward(@Param("userId") String userId, @Param("force") BigDecimal force);

    /**
     * 更新加权奖
     */
    void updateWeightingAward(@Param("award") BigDecimal award);

    /***
     * 根据钱包id更新数据
     * 智能释放功能----锁行操作
     * @param id
     * @param autoTrans
     */
    void updateBalacneById(@Param("id") String id, @Param("autoTrans") BigDecimal autoTrans);


    /***
     * 转换算力第二期
     * @param transForDto
     * 2018/10/16 10:23
     */
    void handleTransForm(TransForDto transForDto);

    /***
     * 第二期新需求
     * 游戏币转换算力
     * @param transForDto
     * 2018/10/16 11:02
     */
    void handleGameCoinTransForm(TransForDto transForDto);

    /******************************************************************后台管理用，改变算力、余额 hugq******************************************************************/
    /**
     * 增加、减少智能算力
     *
     * @param backendUserDTO 改变条件
     * @return
     */
    Integer updateCalculationForceForBackend(BackendUserDTO backendUserDTO);

    /**
     * 增加、减少流动余额
     *
     * @param backendUserDTO 改变条件
     * @return
     */
    Integer updateFlowBalanceForBackend(BackendUserDTO backendUserDTO);

    /**
     * yimiao start
     */

    /**
     *  根据用户id查询用户积分
     * @param userId
     * @return
     */
    UserBalance findUserBalance(@Param("userId") String userId);

    /**
     * ct扣款
     * @param fromId
     * @param orderFee
     * @param modifyTime
     */
    int paymentByCT(@Param("fromId")String fromId, @Param("orderFee")BigDecimal orderFee ,@Param("modifyTime")Date modifyTime);

    /**
     * 积分扣款
     * @param fromId
     * @param orderFee
     * @param modifyTime
     */
    int paymentByintegral(@Param("fromId")String fromId, @Param("orderFee")BigDecimal orderFee,@Param("modifyTime")Date modifyTime);

    /**
     * ct收款
     * @param userId
     * @param orderFee
     * @param modifyTime
     * @return
     */
    int updateFlowBalance(@Param("userId")String userId, @Param("orderFee")BigDecimal orderFee, @Param("modifyTime")Date modifyTime);

    /**
     * 积分收款
     * @param userId
     * @param orderFee
     * @param modifyTime
     * @return
     */
    int updateIntegral(@Param("userId")String userId, @Param("orderFee")BigDecimal orderFee, @Param("modifyTime")Date modifyTime);

    /**
     *ct收款 用户增加算力
     * @param fromId
     * @param force
     * @param modifyTime
     */
    int updateCalculationForce(@Param("fromId")String fromId, @Param("force")BigDecimal force, @Param("modifyTime")Date modifyTime);

    /**
     * 给用户账户发放奖金
     * @param userId
     * @param award_ct
     * @param award_integral
     * @return
     */
    int updateFlowBalanceAndIntegral(@Param("userId")String userId, @Param("award_ct")BigDecimal award_ct, @Param("award_integral")BigDecimal award_integral,@Param("modifyTime")Date modifyTime);

}