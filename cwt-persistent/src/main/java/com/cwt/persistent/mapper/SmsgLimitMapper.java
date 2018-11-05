package com.cwt.persistent.mapper;

import com.cwt.domain.dto.user.SmsgLimitDto;
import com.cwt.domain.entity.SmsgLimit;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * SmsgLimitMapper 数据访问类
 * @date 2018-08-07 19:52:54
 * @version 1.0
 */
@Repository
public interface SmsgLimitMapper extends Mapper<SmsgLimit> {


    /**
     * 通过手机号获取今日已发送短信验证码条数
     * @param mobilephone  手机号
     * @return
     */
    SmsgLimitDto selectCountByMobilePhone(@Param("mobilephone") String mobilephone);

    /**
     * 发送成功记录条数加1
     */
    void addSmsgCount(@Param("mobilephone") String mobilephone);

}