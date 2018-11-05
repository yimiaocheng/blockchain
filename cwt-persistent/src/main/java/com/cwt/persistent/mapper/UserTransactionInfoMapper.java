package com.cwt.persistent.mapper;

import com.cwt.domain.dto.user.UserTransactionInfoDto;
import com.cwt.domain.entity.UserTransactionInfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * UserTransactionInfoMapper 数据访问类
 * @date 2018-08-24 14:18:17
 * @version 1.0
 */
@Repository
public interface UserTransactionInfoMapper extends Mapper<UserTransactionInfo> {

    //根据用户id查询余额交易记录
    UserTransactionInfoDto selectByUserId(String userId);
}