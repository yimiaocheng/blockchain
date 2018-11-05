package com.cwt.persistent.mapper;

import com.cwt.domain.entity.UserRelationChain;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * UserRelationChainMapper 数据访问类
 * @date 2018-08-25 14:47:39
 * @version 1.0
 */
@Repository
public interface UserRelationChainMapper extends Mapper<UserRelationChain> {
    /**
     * 注册时插入用户关系表
     * @param userId
     * @param inviterId
     * @return
     */
    int initUserRelationChain(@Param("userId") String userId, @Param("inviterId") String inviterId);

    /**
     * 查找自己下级所有好友数量
     * @param userId
     * @return
     */
    int selectCountFriends(@Param("userId") String userId);

    /**
     * 查找指定用户的等级关系
     * @param userId 用户id
     */
    UserRelationChain selectByUserId(String userId);
}