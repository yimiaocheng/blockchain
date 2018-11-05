package com.cwt.service.service;


import com.cwt.domain.dto.user.*;
import com.cwt.domain.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/8/10 10:16
 * \* User: YeHao
 * \* Version: V1.0
 * \
 */
public interface UserService {

    //***************
    //  User:HuGanQiang
    //***************

    /**
     * 用户注册
     *
     * @param telephone
     * @param password
     * @param code
     * @return
     */
    UserDto register(String telephone, String password, String code, Integer inviterCode);

    /**
     * 用户登录
     *
     * @param telephone
     * @param password
     * @return
     */
    UserDto login(String telephone, String password);


    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    UserDto getById(String id);

    /**
     * 根据电话号码获取用户信息
     *
     * @param telephone
     * @return
     */
    UserDto getByTelephone(String telephone);

    /**
     * 根据自己的邀请码获取用户信息
     *
     * @param invitationCode
     * @return
     */
    UserDto getUserByInvitationCode(Integer invitationCode);

    /**
     * 获得伞下第一级好友信息列表
     *
     * @param id
     * @return
     */
    List<UserFriendsDto> getFriendsList(String id);

    /**
     * 获得伞下好友总数,数额 = 总好友数 - 直接下级好友数。
     *
     * @param id
     * @return
     */
    UserCountFriendsDto selectCoutFriends(String id);

    /**
     * 修改客户信息
     *
     * @param field
     * @param userDto
     * @return
     */
    UserDto updateUserInfo(String field, UserDto userDto);

//***************
//  User:YeHao
//***************

    /**
     * 验证登录密码
     *
     * @param password 登录密码
     * @return
     */
    boolean isPassword(String id, String password);

    /**
     * 验证支付密码
     *
     * @param paymentPassword 支付密码
     * @return
     */
    boolean isPaymentPassword(String id, String paymentPassword);

    /**
     * 昵称修改
     *
     * @param id       用户ID
     * @param nickName 更新的昵称
     * @return
     */
    UserDto updateNickName(String id, String nickName);

    /**
     * 账号（手机号）修改
     *
     * @param id       用户ID
     * @param userName 更新的账号（手机号）
     * @return
     */
    UserDto updateUserName(String id, String userName);

    /**
     * 登录密码修改
     *
     * @param id       用户ID
     * @param password 更新的登录密码
     * @return
     */
    UserDto updatePassword(String id, String password);

    /**
     * 支付密码修改
     *
     * @param id              用户ID
     * @param paymentPassword 更新的支付密码
     * @return
     */
    UserDto updatePaymentPassword(String id, String paymentPassword);

    /**
     * 银行卡号修改
     *
     * @param id                    用户ID
     * @param paymentMethodBankcard 更新的银行卡号
     * @return
     */
    UserDto updatePaymentMethodBankcard(String id, String paymentMethodBankcard);

    /**
     * 支付宝账号修改
     *
     * @param id               用户ID
     * @param paymentMethodZfb 更新的支付宝账号
     * @return
     */
    UserDto updatePaymentMethodZfb(String id, String paymentMethodZfb);

    /**
     * 微信号修改
     *
     * @param id              用户ID
     * @param paymentMethodWx 更新的微信号
     * @return
     */
    UserDto updatePaymentMethodWx(String id, String paymentMethodWx);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return
     */
    UserDto updateUserInfo(User user);

    /**
     * 更新用户图片信息
     *
     * @param id      用户ID
     * @param headImg 图片名称
     * @return
     */
    UserDto updateHeadImg(String id, String headImg);

    /**
     * 查询用户基本信息
     *
     * @param id 用户id
     */
    UserBaseInfoDto selectBaseInfoByUserId(@Param("id") String id);

//***************
//  User:lzf
//***************

    /***
     * 根据用户id查询等级和用户基本信息
     * @param userId
     * @return
     */
    UserTransferInfoDto selectUserTransferInfoByUserId( String userId);

    /***
     * 根据电话号查询用户支付账号信息
     * @param userName
     * @return
     */
    UserPayInfoDto selectPayInfoByUserTel(String userName);

    /***
     * 重置用户支付密码
     * @param userName
     * @param tel
     */
    void updateResetPayPassWord(String tel,String userName);


/******************************************************************后台管理用******************************************************************/
    /**
     * 查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    public BackendUserDTO selectUserWalletById(String id);

    /**
     * 查询用户列表
     *
     * @param User 用户信息
     * @return 用户集合
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
     * 修改用户状态，后台管理用
     *
     * @param User 用户信息
     * @return 结果
     */
    public int updateUserStatusForBackend(BackendUserDTO User);

    /**
     * 通过用户id查询用户信息
     * @param userId
     * @return
     */
    UserInfo findUserInfo(String userId);

//    /**
//     * 删除用户信息
//     *
//     * @param ids 需要删除的数据ID
//     * @return 结果
//     */
////    public int deleteUserByIds(String ids);

}