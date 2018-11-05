package com.cwt.persistent.mapper;

import com.cwt.domain.dto.user.*;
import com.cwt.domain.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * UserMapper 数据访问类
 * @date 2018-08-07 19:52:54
 * @version 1.0
 */
@Repository
public interface UserMapper extends Mapper<User> {

    /**
     * 用户登录
     * @param userName
     * @param password
     * @return
     */
    UserDto selectByUsernameAndPwd(@Param("userName") String userName, @Param("password") String password);

    /**
     * 根据id查詢
     *
     * @param id
     * @return
     */
    UserDto selectById(@Param("id") String id);

    /**
     * 根据telephone查詢
     *
     * @param telephone
     * @return
     */
    UserDto selectByTelephone(@Param("telephone") String telephone);

    /**
     * 根据用户自己的邀请码获取信息
     * @param invitationCode
     * @return
     */
    UserDto selectByInvitationCode(@Param("invitationCode") Integer invitationCode);

    /**
     * 获得伞下第一级好友信息列表
     * @param id id
     * @return
     */
    List<UserFriendsDto> selectFriendsList(@Param("id") String id);

    /**
     *  获得伞下第一级好友总数
     * @param id id
     * @return
     */
    Integer selectCoutFriends(@Param("id") String id);

    /**
     *
     * 查询用户基本信息
     * @param id 用户id
     */
    UserBaseInfoDto selectBaseInfoByUserId(@Param("id") String id);

    /***
     * 根据用户id查询等级和用户基本信息
     * @param userId
     * @return
     */
    UserTransferInfoDto selectUserTransferInfoByUserId(@Param("userId") String userId);

    /**
     * 查询余额 >= minBalance 的用户数
     * @param minBalance 至少含有的余额数
     * @return
     */
    int countByMinBalance(@Param("minBalance") int minBalance);

    /***
     * 根据电话号查询用户支付账号信息
     * @param userTel
     * @return
     */
    UserPayInfoDto selectPayInfoByUserTel(@Param("userTel")String userTel);

    /***
     * 查询该用户是否是当天创建的新用户
     * @param id
     * @return
     */
    UserDto selectUserNewToday(@Param("id") String id);

    /***
     * 根据用户id查询账号、余额、算力、等级奖励信息
     * @param userId
     * @return
     */
    UserGradeWalletDto selectUserGradeWalletByUid(@Param("userId") String userId);
    
    /******************************************************后台管理用******************************************************/
    /**
     * 查询包含用户等级、算力余额的用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    public BackendUserDTO selectUserWalletById(String id);

//    /**
//     * 查询用户列表
//     *
//     * @param User 用户信息
//     * @return 用户集合
//     */
//    public List<User> selectUserList(User User);

    /**
     * 查询包含用户等级、算力余额等信息的用户列表
     * @param User
     * @return
     */
    public List<BackendUserDTO> selectUserWalletList(BackendUserDTO User);

//    /**
//     * 新增用户
//     *
//     * @param User 用户信息
//     * @return 结果
//     */
//    public int insertUser(User User);

    /**
     * 修改用户
     *
     * @param User 用户信息
     * @return 结果
     */
    public int updateUserStatusForBackend(BackendUserDTO User);


}