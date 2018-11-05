package com.cwt.service.service;


import com.cwt.domain.dto.grade.GradeDto;
import com.cwt.domain.dto.user.BackendUserDTO;
import com.cwt.domain.dto.wallet.WalletDto;
import com.cwt.domain.entity.UserBalance;
import com.cwt.domain.entity.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    /***
     * 根据用户Id获取钱包信息
     * @param userId
     * @return WalletDto
     */
    WalletDto getByUserId(String userId);

    /***
     * 根据钱包Id(主键)获取钱包信息
     * @param walletId
     * @return WalletDto
     */
    WalletDto getByWalletId(String walletId);

    /***
     * 查询所有钱包信息
     * @return List<WalletDto>
     */
    List<WalletDto> selectAllWallet();

    /***
     * 查询所有矿工等级信息
     * @return List<GradeDto>
     */
    List<GradeDto> selectAllGrade();

    /***
     * 更新钱包信息
     * @param wallet
     * @return
     */
    WalletDto updateWallet(Wallet wallet);

    /***
     * 余额转换算力功能
     *
     * @param transGameCoin 转换的游戏币
       @param userId  用户Id
     * @return 更新后的钱包对象
     */
    WalletDto gameCointransform(BigDecimal transGameCoin, String password, String userId);


    /***
     * 余额转换算力功能
     *
     * @param transBalance 转换的余额
     * @param userId
     * @return 更新后的钱包对象
     */
    WalletDto transform(Double transBalance, String password, String userId);

    /***
     * 节点转账功能(转出)
     * @param userId 转账人ID
     * @param telephone 收款人账号
     * @param balance 交易余额
     * @param password 交易密码
     * @return
     */
    @Deprecated
    WalletDto transferAccounts(String userId, String telephone, Double balance, String password);

    /***
     * 新建/保存钱包
     * @param userId
     * @return WalletDto
     */
    WalletDto saveByUserId(String userId);

    /***
     * 获取每天的智能释放奖励
     * @param userId
     */
    void headerUpdateBalance(String userId);

    /***
     * 查询智能释放奖励的值
     * @param userId
     * @return
     */
    BigDecimal selectUpdateBalance(String userId);


    /**
     * huangxl**************
     */

    /**
     * 处理点对点节点转账
     * @param userId 转账人id
     * @param telephone 收款人手机号
     * @param balance 余额
     * @param password 支付密码
     */
    void handleC2CTransfer(String userId, String telephone, Double balance, String password);

    /**
     * 处理扫码转账
     * @param userId 转账人id
     * @param telephone 收款人手机号
     * @param balance 余额
     * @param password 支付密码
     */
    void handleScanQRCodeTransfer(String userId, String telephone, Double balance, String password);


    /******************************************************************后台管理用，改变算力、余额 hugq******************************************************************/
    /**
     * 增加、减少智能算力
     * @param backendUserDTO  改变条件
     * @return
     */
    Integer updateCalculationForceForBackend(BackendUserDTO backendUserDTO);

    /**
     * 增加、减少流动余额
     * @param backendUserDTO  改变条件
     * @return
     */
    Integer updateFlowBalanceForBackend(BackendUserDTO backendUserDTO);


    /**
     * yimiao start
     */
    /**
     * 根据用户id查询用户余额
     * @param userId
     * @return
     */
    UserBalance findUserBalance(String userId);
}
