package com.cwt.service.service;


import com.cwt.domain.dto.user.SmsgLimitDto;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/8/10 10:16
 * \* User: YeHao
 * \* Version: V1.0
 * \
 */
public interface SmsgLimitService {


    /**
     * 通过手机号获取今日已发送短信验证码条数
     * @param mobilephone  手机号
     * @return
     */
    SmsgLimitDto selectCountByMobilePhone(String mobilephone);

    /**
     * 发送成功，条数加1
     * @return
     */
    void addSmsgCount(String mobilephone);
}
