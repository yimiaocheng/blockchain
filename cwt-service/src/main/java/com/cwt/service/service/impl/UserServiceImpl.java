package com.cwt.service.service.impl;

import com.cwt.common.enums.PayResultEnums;
import com.cwt.common.enums.UserResultEnums;
import com.cwt.common.exception.PayException;
import com.cwt.common.exception.UserExeption;
import com.cwt.common.util.BeanUtils;
import com.cwt.common.util.CheckUtils;
import com.cwt.common.util.ExceptionPreconditionUtils;
import com.cwt.common.util.MD5Utils;
import com.cwt.domain.constant.InformationConstants;
import com.cwt.domain.constant.UserPayConstants;
import com.cwt.domain.constant.WalletBillConstants;
import com.cwt.domain.dto.information.InformationDto;
import com.cwt.domain.dto.user.*;
import com.cwt.domain.entity.*;
import com.cwt.persistent.mapper.*;
import com.cwt.service.service.UserService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/7/21 10:17
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private UserBankMapper userBankMapper;

    @Autowired
    private UserTransactionInfoMapper userTransactionInfoMapper;

    @Autowired
    private UserRelationChainMapper userRelationChainMapper;

    @Autowired
    private WalletCalculatePowerRecordMapper walletCalculatePowerRecordMapper;
    @Autowired
    private InformationMapper informationMapper;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public UserDto register(String telephone, String password, String code, Integer inviterCode) {
        //检查参数是否为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(telephone, new UserExeption(UserResultEnums.NULL_TELEPHONE));
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(password, new UserExeption(UserResultEnums.PASSWORD_NULL));
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(code, new UserExeption(UserResultEnums.SMSGCODE_NULL));
        ExceptionPreconditionUtils.checkNotNull(inviterCode, new UserExeption(UserResultEnums.INVITERID_NULL));

        //判断手机号，是否已经注册过
        UserDto userDtoVali = userMapper.selectByTelephone(telephone);
        if(userDtoVali != null){
            throw new UserExeption(UserResultEnums.ALREADY_REGISTERED);
        }

        Date nowDate = new Date();  //现在时间

        User user = new User();
        //默认密码
        user.setPaymentPassword(MD5Utils.MD5(telephone));

        UserDto inviter = userMapper.selectByInvitationCode(inviterCode); //通过邀请人的code查询是否为空
        if(inviter != null){
            user.setInviterId(inviter.getId()); //邀请人id
            user.setId(UUID.randomUUID().toString());  //随机一个id
            user.setUserName(telephone);
            user.setPassword(MD5Utils.MD5(password));
            user.setStatus("usable");
            user.setCreateTime(nowDate);
            user.setNickName(telephone);  //随机用户名,以后修改
        }else{
            throw new UserExeption(UserResultEnums.INVITER_NULL);  //邀请人不存在
        }
        int countU = userMapper.insertSelective(user);  //插入数据

        //新建对应钱包
        Wallet wallet = new Wallet();
        wallet.setId(UUID.randomUUID().toString());
        InformationDto informationDto = informationMapper.selectByDateName(InformationConstants.DATA_NAME_INITIAL_FORCE, InformationConstants.STATUS_SHOW);
        BigDecimal present = new BigDecimal(informationDto.getDataValue());//注册赠送算力数额
        wallet.setCalculationForce(present);
        wallet.setFlowBalance(new BigDecimal(0.00));
        wallet.setUserId(user.getId());
        wallet.setCreateTime(nowDate);
        int countW = walletMapper.insertSelective(wallet);

        //新建对应银行卡信息表
        UserBank userBank = new UserBank();
        userBank.setId(UUID.randomUUID().toString());
        userBank.setUserId(user.getId());
        userBank.setCreateTime(nowDate);
        int countUb = userBankMapper.insertSelective(userBank);

        //新建用户交易信息总表
        UserTransactionInfo userTransactionInfo = new UserTransactionInfo();
        userTransactionInfo.setId(UUID.randomUUID().toString());
        userTransactionInfo.setUserId(user.getId());
        userTransactionInfo.setCreateTime(nowDate);
        int countUti = userTransactionInfoMapper.insertSelective(userTransactionInfo);

        //初始化用户关系表
        //int countUrc = userRelationChainMapper.initUserRelationChain(user.getId(), inviterId);
        UserRelationChain parentUserRelation = userRelationChainMapper.selectByUserId(inviter.getId());
        int pTreeDepth = parentUserRelation.getTreeDepth();//父级等级代数
        String pRelationChain = parentUserRelation.getRelationChain();//父级关系链
        UserRelationChain userRelationChain = new UserRelationChain();
        userRelationChain.setId(UUID.randomUUID().toString());
        userRelationChain.setRelationChain(pRelationChain+","+inviter.getId());//设置当前关系链
        userRelationChain.setPid(inviter.getId());//父级ID
        userRelationChain.setUserId(user.getId());//当前用户id
        userRelationChain.setTreeDepth(pTreeDepth+1);//代数深度+1
        userRelationChain.setCreateTime(nowDate);
        int countUrc = userRelationChainMapper.insert(userRelationChain);

        //保存算力变更记录
//      InformationDto informationDto = informationMapper.selectByDateName(InformationConstants.DATA_NAME_INITIAL_FORCE, InformationConstants.STATUS_SHOW);
//      BigDecimal present = new BigDecimal(informationDto.getDataValue());//注册赠送数额
        WalletCalculatePowerRecord userForceBill = new WalletCalculatePowerRecord();
        userForceBill.setId(UUID.randomUUID().toString());//记录id
        userForceBill.setTargetUserId(user.getId());//算力变更用户id
        userForceBill.setOptUserId(user.getId());//操作的用户id
        userForceBill.setOptUserShowMsg(user.getUserName());//操作用户的账号
        userForceBill.setAmount(present);//变更的数量
        userForceBill.setArithmeticType(WalletBillConstants.ARITHMETIC_TYPE_ADD);//该记录的类型是：增加
        userForceBill.setChangeType(WalletBillConstants.CHANGE_TYPE_REGISTER);//途径：注册
        userForceBill.setCreateTime(new Date());//创建时间
        int countCpr = walletCalculatePowerRecordMapper.insert(userForceBill);  //保存记录

        /**调用php注册接口*/
        //通过id查询用户注册的信息
        UserDto u = userMapper.selectById(user.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        UserPO userPO = new UserPO();
        userPO.setUid(u.getId());
        userPO.setTelephone(u.getUserName());
        userPO.setInvitation_code(inviterCode+"");
        userPO.setNew_code(u.getInvitationCode()+"");

        JSONObject jsonObject = JSONObject.fromObject(userPO);
        HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString() , headers) ;//参数
        ResponseEntity<String> dictMap = restTemplate.postForEntity(UserPayConstants.REGISTER_URL, entity,String.class);
        System.out.println(dictMap.getBody());
        //判断php那边是否注册成功 0成功 1失败
        String status = JSONObject.fromObject(dictMap.getBody()).getString("status");
        /**调用php注册接口*/

        if(countU != 1||countW != 1 || countUb != 1 || countUti != 1 || countUrc != 1 || countCpr != 1||!"0".equals(status)){
            throw new UserExeption(UserResultEnums.INSERT_ERR);  //注册失败
        }

        UserDto userDto  = new UserDto();
        BeanUtils.copySamePropertyValue(user, userDto);

        return userDto;
    }

    @Override
//    @CachePut(value = "USER_CACHE", key = "'USER_CACHE_' + #result.id")
    public UserDto login(String userName, String password) {
        //判断手机号，密码是否为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(userName
                , new UserExeption(UserResultEnums.PASSWORD_NULL));
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(password
                , new UserExeption(UserResultEnums.PASSWORD_NULL));

        UserDto userDto = userMapper.selectByUsernameAndPwd(userName, MD5Utils.MD5(password));

        //检测账号、密码是否输入正确
        ExceptionPreconditionUtils.checkNotNull(userDto
                , new UserExeption(UserResultEnums.LOGIN_ERR));

        //检查用户状态
        if("disable".equals(userDto.getStatus())){
            throw new UserExeption(UserResultEnums.USER_DISABLE);
        }else if("forbidden".equals(userDto.getStatus())){
            throw new UserExeption(UserResultEnums.USER_FORBIDDEN);
        }

        return userDto;
    }

    /**
     * 生成6位随机数字字母昵称
     * @return
     */
    public String getStringRandom() {
        String val = "";
        Random random = new Random();

        for(int i = 0; i < 6; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }

        return val;
    }
    @Override
    public UserDto getById(String id) {
        //检测id不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(id
                , new UserExeption(UserResultEnums.NULL_ID));

        UserDto userDto = userMapper.selectById(id);

        //检测账号是否存在
        ExceptionPreconditionUtils.checkNotNull(userDto
                , new UserExeption(UserResultEnums.NULL_USER));

        return userDto;
    }

    @Override
    public UserDto getByTelephone(String telephone) {
        //检测手机号不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(telephone
                , new UserExeption(UserResultEnums.NULL_TELEPHONE));

        UserDto userDto = userMapper.selectByTelephone(telephone);

        //检测账号是否存在
//        ExceptionPreconditionUtils.checkNotNull(userDto
//                , new UserExeption(UserResultEnums.NULL_USER));

        return userDto;
    }

    @Override
    public List<UserFriendsDto> getFriendsList(String id) {
        //检查参数不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(id , new UserExeption(UserResultEnums.NULL_ID));

        List<UserFriendsDto> userDtoList = userMapper.selectFriendsList(id);

        return userDtoList;
    }

    @Override
    public UserCountFriendsDto selectCoutFriends(String id) {
        //数据对象
        UserCountFriendsDto userCountFriendsDto = new UserCountFriendsDto();
        //直接好友数
        Integer directNumber = userMapper.selectCoutFriends(id);
        //总好友数
        Integer countNumver = userRelationChainMapper.selectCountFriends(id);
        if(countNumver != null && directNumber != null){
            userCountFriendsDto.setCountNumver(countNumver);
            userCountFriendsDto.setDirectNumber(directNumber);
            userCountFriendsDto.setIndirectNumber(countNumver - directNumber);
        }else{
            userCountFriendsDto.setCountNumver(0);
            userCountFriendsDto.setDirectNumber(0);
            userCountFriendsDto.setIndirectNumber(0);
        }
        return userCountFriendsDto;
    }

    /*@Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public UserDto updateUserInfo(String field, UserDto userDto) {

        User userForUpdate = new User();
        if("password".equals(field)){
            //检查参数不能为空
            ExceptionPreconditionUtils.checkStringArgumentsNotBlank(userDto.getUserName() , new UserExeption(UserResultEnums.NULL_TELEPHONE));
            ExceptionPreconditionUtils.checkStringArgumentsNotBlank(userDto.getPassword() , new UserExeption(UserResultEnums.PASSWORD_NULL));

            //判断手机号，之前是否注册过
            UserDto userDtoVali = userMapper.selectByTelephone(userDto.getUserName());
            ExceptionPreconditionUtils.checkNotNull(userDtoVali, new UserExeption(UserResultEnums.USER_NO_REGISTERED));  //尚未注册

            userForUpdate.setId(userDtoVali.getId());
            userForUpdate.setPassword(MD5Utils.MD5(userDto.getPassword()));  //修改找回密码
            userForUpdate.setModifyTime(new Date());  //修改时间
        }

        int count = userMapper.updateByPrimaryKeySelective(userForUpdate);  //修改信息，返回影响行数
        if(count != 1){
            throw new UserExeption(UserResultEnums.UPDATE_FAILED);  //修改失败
        }

        return userDto;
    }*/

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public UserDto updateUserInfo(String field, UserDto userDto) {

        User userForUpdate = new User();
        if("password".equals(field)){
            //检查参数不能为空
            ExceptionPreconditionUtils.checkStringArgumentsNotBlank(userDto.getUserName() , new UserExeption(UserResultEnums.NULL_TELEPHONE));
            ExceptionPreconditionUtils.checkStringArgumentsNotBlank(userDto.getPassword() , new UserExeption(UserResultEnums.PASSWORD_NULL));

            //判断手机号，之前是否注册过
            UserDto userDtoVali = userMapper.selectByTelephone(userDto.getUserName());
            ExceptionPreconditionUtils.checkNotNull(userDtoVali, new UserExeption(UserResultEnums.USER_NO_REGISTERED));  //尚未注册

            userForUpdate.setId(userDtoVali.getId());
            userForUpdate.setPassword(MD5Utils.MD5(userDto.getPassword()));  //修改找回密码
        }else if("paymentPassword".equals(field)){
            userForUpdate.setId(userDto.getId());
            userForUpdate.setPaymentPassword(MD5Utils.MD5(userDto.getPaymentPassword()));  //设置支付密码
        }

        int count = userMapper.updateByPrimaryKeySelective(userForUpdate);  //修改信息，返回影响行数
        if(count != 1){
            throw new UserExeption(UserResultEnums.UPDATE_FAILED);  //修改失败
        }

        return userDto;
    }

    @Override
    public UserDto getUserByInvitationCode(Integer invitationCode) {
        //检查参数不能为空
        ExceptionPreconditionUtils.checkNotNull(invitationCode, new UserExeption(UserResultEnums.INVITATIONCODE_NULL));

        UserDto userDto = userMapper.selectByInvitationCode(invitationCode);

        //检测账号是否存在
        ExceptionPreconditionUtils.checkNotNull(userDto, new UserExeption(UserResultEnums.NULL_USER));

        return userDto;
    }

    @Override
    public boolean isPassword(String id, String password) {
        //检测id不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(id
                , new UserExeption(UserResultEnums.USERID_NULL));
        //检测登录密码是否为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(password
                , new UserExeption(UserResultEnums.OLD_PASSWORD_NULL));
        //将数据封装到对象
        User user = new User();
        user.setId(id);
        user.setPassword(MD5Utils.MD5(password));
        //查询验证
        User isUser = userMapper.selectOne(user);
        if (isUser == null) {
            throw new UserExeption(UserResultEnums.OLD_IS_NOT_PASSWORD);
        }
        return true;
    }

    @Override
    public boolean isPaymentPassword(String id, String paymentPassword) {
        //检测id不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(id
                , new UserExeption(UserResultEnums.USERID_NULL));
        //检测支付密码是否为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(paymentPassword
                , new UserExeption(UserResultEnums.OLD_PAYMENT_PASSWORD_NULL));
        //将数据封装到对象
        User user = new User();
        user.setId(id);
        user.setPaymentPassword(MD5Utils.MD5(paymentPassword));
        //查询验证
        User isUser = userMapper.selectOne(user);
        if (isUser == null) {
           throw new UserExeption(UserResultEnums.IS_NOT_OLD_PAYMENT_PASSWORD);
        }
        return true;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public UserDto updateNickName(String id, String nickName) {
        //检测id不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(id
                , new UserExeption(UserResultEnums.USERID_NULL));
        //检测修改的昵称不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(nickName
                , new UserExeption(UserResultEnums.NICK_NAME_NULL));
        //将数据封装到对象
        User user = new User();
        user.setId(id);
        user.setNickName(nickName);
        user.setModifyTime(new Date());
        //更新数据
        userMapper.updateByPrimaryKeySelective(user);
        //查询更新后的对象，返回控制层
        UserDto userDtoNew = userMapper.selectById(id);
        return userDtoNew;
    }

    @Override
    public UserDto updateUserName(String id, String userName) {
        //检测id不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(id
                , new UserExeption(UserResultEnums.USERID_NULL));
        //检测修改的账号不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(userName
                , new UserExeption(UserResultEnums.USER_NAME_NULL));
        UserDto userDto = userMapper.selectByTelephone(userName);
        if (null != userDto){
            throw  new UserExeption(UserResultEnums.USER_NAME_EXISTENCE);
        }
        //将数据封装到对象
        User user = new User();
        user.setId(id);
        user.setUserName(userName);
        user.setModifyTime(new Date());
        //更新数据
        userMapper.updateByPrimaryKeySelective(user);
        //查询更新后的对象，返回控制层
        UserDto userDtoNew = userMapper.selectById(id);
        return userDtoNew;
    }

    @Override
    public UserDto updatePassword(String id, String password) {
        //检测id不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(id
                , new UserExeption(UserResultEnums.USERID_NULL));
        //检测修改的登录密码不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(password
                , new UserExeption(UserResultEnums.PASSWORD_NULL));
        //将数据封装到对象
        User user = new User();
        user.setId(id);
        user.setPassword(MD5Utils.MD5(password));
        user.setModifyTime(new Date());
        //更新数据
        userMapper.updateByPrimaryKeySelective(user);
        //查询更新后的对象，返回控制层
        UserDto userDtoNew = userMapper.selectById(id);
        return userDtoNew;
    }

    @Override
    public UserDto updatePaymentPassword(String id, String paymentPassword) {
        //检测id不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(id
                , new UserExeption(UserResultEnums.USERID_NULL));
        //检测修改的支付密码不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(paymentPassword
                , new UserExeption(UserResultEnums.PAYMENT_PASSWORD_NULL));
        if(paymentPassword.length() < 6 || paymentPassword.length() > 16){
            throw new UserExeption(UserResultEnums.PAYMENT_PASSWORD_INPUT_LENGTH_ERROR);//长度限制
        }

        if(!paymentPassword.matches("[0-9]+")){
            throw new UserExeption(UserResultEnums.PAYMENT_PASSWORD_INPUT_ERROR);//更改密码仅支持纯数字
        }
        //将数据封装到对象
        User user = new User();
        user.setId(id);
        user.setPaymentPassword(MD5Utils.MD5(paymentPassword));
        user.setModifyTime(new Date());
        //更新数据
        userMapper.updateByPrimaryKeySelective(user);
        //查询更新后的对象，返回控制层
        UserDto userDtoNew = userMapper.selectById(id);
        return userDtoNew;
    }

    @Override
    public UserDto updatePaymentMethodBankcard(String id, String paymentMethodBankcard) {
        //检测id不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(id
                , new UserExeption(UserResultEnums.USERID_NULL));
        //检测修改的银行卡号不能为空
        ExceptionPreconditionUtils.checkNotNull(paymentMethodBankcard
                , new UserExeption(UserResultEnums.PAYMENT_METHOD_BANKCARD_NULL));
        //验证是否为银行卡号
        if(!CheckUtils.checkBankCard(paymentMethodBankcard)){
            throw new UserExeption(UserResultEnums.PAYMENT_METHOD_BANKCARD_ERROR);
        };

        //将数据封装到对象
        User user = new User();
        user.setId(id);
        user.setPaymentMethodBankcard(paymentMethodBankcard);
        user.setModifyTime(new Date());
        //更新数据
        userMapper.updateByPrimaryKeySelective(user);
        //查询更新后的对象，返回控制层
        UserDto userDtoNew = userMapper.selectById(id);
        return userDtoNew;
    }

    @Override
    public UserDto updatePaymentMethodZfb(String id, String paymentMethodZfb) {
        //检测id不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(id
                , new UserExeption(UserResultEnums.USERID_NULL));
        //检测修改的支付宝账号不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(paymentMethodZfb
                , new UserExeption(UserResultEnums.PAYMENT_METHOD_ZFB_NULL));
        //将数据封装到对象
        User  user  = new User();
        user.setId(id);
        user.setPaymentMethodZfb(paymentMethodZfb);
        user.setModifyTime(new Date());
        //更新数据
        userMapper.updateByPrimaryKeySelective(user);
        //查询更新后的对象，返回控制层
        UserDto userDtoNew = userMapper.selectById(id);
        return userDtoNew;
    }

    @Override
    public UserDto updatePaymentMethodWx(String id, String paymentMethodWx) {
        //检测id不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(id
                , new UserExeption(UserResultEnums.USERID_NULL));
        //检测修改的微信号不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(paymentMethodWx
                , new UserExeption(UserResultEnums.PAYMENT_METHOD_WX_NULL));
        //将数据封装到对象
        User user = new User();
        user.setId(id);
        user.setPaymentMethodWx(paymentMethodWx);
        user.setModifyTime(new Date());
        //更新数据
        userMapper.updateByPrimaryKeySelective(user);
        //查询更新后的对象，返回控制层
        UserDto userDtoNew = userMapper.selectById(id);
        return userDtoNew;
    }

    @Override
    public UserDto updateUserInfo(User user) {
        //更新数据
        userMapper.updateByPrimaryKeySelective(user);
        UserDto userDtoNew = userMapper.selectById(user.getId());
        return userDtoNew;
    }
    @Override
    public UserDto updateHeadImg(String id, String headImg){
        //将数据封装到对象
        User user = new User();
        user.setId(id);
        user.setHeadImg(headImg);
        user.setModifyTime(new Date());
        //更新数据
        userMapper.updateByPrimaryKeySelective(user);
        UserDto userDtoNew = userMapper.selectById(user.getId());
        return  userDtoNew;
    }

    @Override
    public UserBaseInfoDto selectBaseInfoByUserId(String id){
        return userMapper.selectBaseInfoByUserId(id);
    }

    /***
     * 根据用户id查询等级和用户基本信息
     * @param userId
     * @return
     */
    @Override
    public UserTransferInfoDto selectUserTransferInfoByUserId(String userId) {
        return userMapper.selectUserTransferInfoByUserId(userId);
    }

    /***
     * 根据电话号查询用户支付账号信息
     * @param userName
     * @return
     */
    @Override
    public UserPayInfoDto selectPayInfoByUserTel(String userName) {
        return userMapper.selectPayInfoByUserTel(userName);
    }

    /***
     * lzf
     * 重置支付密码
     * @param userName
     */
    @Override
    public void updateResetPayPassWord(String tel,String userName) {
        //检测电话不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(tel
                , new UserExeption(UserResultEnums.USERID_NULL));
        //检测修改的账号不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(userName
                , new UserExeption(UserResultEnums.USER_NAME_NULL));
        UserDto userDto = userMapper.selectByTelephone(userName);
        //如果登录缓存中的账号和修改的账号不一致，抛出异常
        if (!tel.equals(userName)){
            throw  new UserExeption(UserResultEnums.USER_INCONFORMITY);
        }
        //将数据封装到对象
        User user = new User();
        user.setId(userDto.getId());
        user.setUserName(userName);
        user.setModifyTime(new Date());
        user.setPaymentPassword(MD5Utils.MD5(userName));
        //更新数据
        userMapper.updateByPrimaryKeySelective(user);
    }

    /******************************************************************后台管理用******************************************************************/
    /**
     * 查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public BackendUserDTO selectUserWalletById(String id)
    {
        return userMapper.selectUserWalletById(id);
    }

    /**
     * 查询用户列表
     *
     * @param User 用户信息
     * @return 用户集合
     */
    @Override
    public List<BackendUserDTO> selectUserWalletList(BackendUserDTO User)
    {
        return userMapper.selectUserWalletList(User);
    }

//    /**
//     * 新增用户
//     *
//     * @param User 用户信息
//     * @return 结果
//     */
//    @Override
//    public int insertUser(User User)
//    {
//        return userMapper.insertUser(User);
//    }

    /**
     * 修改用户状态，后台管理用
     *
     * @param User 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatusForBackend(BackendUserDTO User)
    {
        return userMapper.updateUserStatusForBackend(User);
    }

    @Override
    public UserInfo findUserInfo(String userId) {
        if(StringUtils.isBlank(userId)){
            throw new PayException(PayResultEnums.FROM_ID_NULL);
        }
        UserDto userDto = userMapper.selectById(userId);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setUserName(userDto.getUserName());
        userInfo.setNickName(userDto.getNickName());
        String headImg = userDto.getHeadImg();
        userInfo.setHeadImg(headImg);
        return userInfo;
    }

//    /**
//     * 删除用户对象
//     *
//     * @param ids 需要删除的数据ID
//     * @return 结果
//     */
////    @Override
////    public int deleteUserByIds(String ids)
////    {
////        return UserMapper.deleteUserByIds(Convert.toStrArray(ids));
////    }
    
}
