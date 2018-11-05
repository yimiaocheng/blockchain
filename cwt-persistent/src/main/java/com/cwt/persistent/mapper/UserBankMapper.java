package com.cwt.persistent.mapper;

import com.cwt.domain.dto.user.UserBankDto;
import com.cwt.domain.entity.UserBank;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * UserBankMapper 数据访问类
 * @date 2018-08-22 10:10:40
 * @version 1.0
 */
@Repository
public interface UserBankMapper extends Mapper<UserBank> {

    /**
     * 根据用户ID查询银行卡信息
     *
     * @param userid 用户ID
     * @return
     */
    UserBankDto selectByUserid(@Param("userid") String userid);

}