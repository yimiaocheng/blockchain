package com.cwt.service.service;


import com.cwt.domain.dto.user.UserBankDto;
import com.cwt.domain.entity.UserBank;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/8/10 10:16
 * \* User: YeHao
 * \* Version: V1.0
 * \
 */
public interface UserBankService {


    /**
     * 根据userid查询用户银行卡信息
     *
     * @param userid 用户ID
     * @return
     */
    UserBankDto getByUserid(String userid);

    /**
     * 保存或修改用户银行卡信息
     * @param userBank 用户银行卡信息类
     * @return
     */
    UserBankDto saveOrUpdateBankinfo(UserBank userBank);

}