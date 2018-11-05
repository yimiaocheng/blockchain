package com.cwt.app.controller;

import com.cwt.app.common.dto.UserSessionDto;
import com.cwt.app.common.util.RedisUtils;
import com.cwt.app.common.util.ResultUtils;
import com.cwt.app.common.util.SessionUtils;
import com.cwt.app.controller.api.UserApi;
import com.cwt.common.constant.BaseConstants;
import com.cwt.common.enums.UserResultEnums;
import com.cwt.common.exception.UserExeption;
import com.cwt.common.util.BeanPreconditionUtils;
import com.cwt.common.util.ExceptionPreconditionUtils;
import com.cwt.common.util.MD5Utils;
import com.cwt.domain.constant.BalanceOrderBillConstants;
import com.cwt.domain.dto.ResultDto;
import com.cwt.domain.dto.user.UserBankDto;
import com.cwt.domain.dto.user.UserBaseInfoDto;
import com.cwt.domain.dto.user.UserDto;
import com.cwt.domain.dto.user.UserFriendsDto;
import com.cwt.domain.entity.User;
import com.cwt.domain.entity.UserBank;
import com.cwt.service.service.UserBankService;
import com.cwt.service.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(UserApi.USER_CONTROLLER_API)
@RestController
@RequestMapping("/user")
public class UserController {

    //    图片路径
    @Value("${FILES_DIR.HEAD_IMG}")
    private String FILE_IMG_PATH;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private UserBankService userBankService;


//    ******************************
//    User:XianChaoWei
//    ******************************

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @ApiOperation(value = UserApi.LoginOut.METHOD_API_NAME,
            notes = UserApi.LoginOut.METHOD_API_NOTE)
    @RequestMapping(value = "/loginOut", method = RequestMethod.POST)
    public ResultDto loginOut(HttpServletRequest request) {

        UserSessionDto userSession = SessionUtils.getSession(request.getSession());  //获取用户session

        SessionUtils.removeSession(request.getSession());  //删除用户登录session

        redisUtils.deleteByKey(BaseConstants.LOGIN_USER_REDIS_KEY + userSession.getToken());  //删除redis中缓存的userToken

        return ResultUtils.buildSuccessDto(new UserDto());
    }

    /**
     * 获得伞下第一级好友信息列表
     *
     * @param request 请求
     * @return
     */
    @ApiOperation(value = UserApi.GetFriends.METHOD_API_NAME,
            notes = UserApi.GetFriends.METHOD_API_NOTE)
    @RequestMapping(value = "/getFriends", method = RequestMethod.POST)
    public ResultDto getFriends(HttpServletRequest request, @RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        UserSessionDto userSession = SessionUtils.getSession(request.getSession());  //获取用户session
        PageHelper.startPage(pageNum == null ? 0 : pageNum, pageSize == null ? 10 : pageSize);
        List<UserFriendsDto> friends = userService.getFriendsList(userSession.getUserId());
//        List<UserFriendsDto> friends = userService.getFriendsList("1");
        return ResultUtils.buildSuccessDto(friends);
    }

    /**
     * 获得伞下好友总数,数额 = 总好友数 - 直接下级好友数。
     */
    @ApiOperation(value = UserApi.SelectCoutFriends.METHOD_API_NAME,
            notes = UserApi.SelectCoutFriends.METHOD_API_NOTE)
    @RequestMapping(value = "/selectCoutFriends", method = RequestMethod.GET)
    public ResultDto selectCoutFriends(HttpServletRequest request) {
        UserSessionDto userSession = SessionUtils.getSession(request.getSession());  //获取用户session
        return ResultUtils.buildSuccessDto(userService.selectCoutFriends(userSession.getUserId()));
    }


    /**
     * 设置初始支付密码
     *
     * @param request
     * @param paymentPassword
     * @return
     */
    @ApiOperation(value = UserApi.SetPaymentPassword.METHOD_API_NAME,
            notes = UserApi.SetPaymentPassword.METHOD_API_NOTE)
    @RequestMapping(value = "/setPaymentPassword", method = RequestMethod.POST)
    public ResultDto setPaymentPassword(HttpServletRequest request,
                                        @ApiParam(UserApi.SetPaymentPassword.METHOD_API_PaymentPassword) @RequestParam("paymentPassword") String paymentPassword) {

        UserSessionDto userSessionDto = SessionUtils.getSession(request.getSession());  //获取session，拿到id
        UserDto userDto = new UserDto();
        userDto.setId(userSessionDto.getUserId());
        userDto.setPaymentPassword(MD5Utils.MD5(paymentPassword));

        userService.updateUserInfo("paymentPassword", userDto);  //设置初始支付密码

        return ResultUtils.buildSuccessDto(userDto);
    }

    /**
     * 通过手机获取用户信息
     *
     * @param telephone
     * @return
     */
    @ApiOperation(value = UserApi.GetUserByTelephone.METHOD_API_NAME,
            notes = UserApi.GetUserByTelephone.METHOD_API_NOTE)
    @RequestMapping(value = "/getUserByTelephone", method = RequestMethod.POST)
    public ResultDto getUserByTelephone(
            @ApiParam(UserApi.GetUserByTelephone.METHOD_API_TELEPHONE) @RequestParam("telephone") String telephone) {

        UserDto userDto = userService.getByTelephone(telephone);

        //检测用户是否存在
        ExceptionPreconditionUtils.checkNotNull(userDto
                , new UserExeption(UserResultEnums.NULL_USER));

        UserDto returnDto = new UserDto();
        returnDto.setId(userDto.getId());

        return ResultUtils.buildSuccessDto(userDto);
    }

    /**
     * 通过id获取用户信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = UserApi.GetUserById.METHOD_API_NAME,
            notes = UserApi.GetUserById.METHOD_API_NOTE)
    @RequestMapping(value = "/getUserById", method = RequestMethod.POST)
    public ResultDto getUserById(
            @ApiParam(UserApi.GetUserById.METHOD_API_ID) @RequestParam("id") String id) {

        UserDto userDto = userService.getById(id);

        return ResultUtils.buildSuccessDto(userDto);
    }

    /**
     * 根据自己邀请码获取信息
     *
     * @param invitationCode
     * @return
     */
    @ApiOperation(value = UserApi.GetUserByInvitationCode.METHOD_API_NAME,
            notes = UserApi.GetUserByInvitationCode.METHOD_API_NOTE)
    @RequestMapping(value = "/getUserByInvitationCode", method = RequestMethod.POST)
    public ResultDto getUserByInvitationCode(
            @ApiParam(UserApi.GetUserByInvitationCode.METHOD_API_INVITATIONCODE) @RequestParam(name = "invitationCode", required = false) Integer invitationCode) {

        UserDto userDto = userService.getUserByInvitationCode(invitationCode);

        return ResultUtils.buildSuccessDto(userDto);
    }

    /**
     * 获取用户银行卡信息
     *
     * @param request 请求
     * @return
     */
    @ApiOperation(value = UserApi.GetUserBankInfo.METHOD_API_NAME,
            notes = UserApi.GetUserBankInfo.METHOD_API_NOTE)
    @RequestMapping(value = "/getUserBankInfo", method = RequestMethod.POST)
    public ResultDto getUserBankInfo(HttpServletRequest request) {
        UserSessionDto userSessionDto = SessionUtils.getSession(request.getSession());  //获取用户session，拿到id

        UserBankDto userBankDto = userBankService.getByUserid(userSessionDto.getUserId());

        if (userBankDto != null) {
            //去掉一些前端不用的数据
            userBankDto.setCreateTime(null);
            userBankDto.setId(null);
            userBankDto.setUserId(null);
            userBankDto.setModifyTime(null);
        } else {
            throw new UserExeption(UserResultEnums.BANKINFO_NULL);
        }

        return ResultUtils.buildSuccessDto(userBankDto);
    }

    /**
     * 保存或修改用户银行卡信息
     *
     * @param request
     * @param userName
     * @param bankLocation
     * @param bankName
     * @param branchName
     * @param bankNumber
     * @return
     */
    @ApiOperation(value = UserApi.SaveOrUpdateUserBankInfo.METHOD_API_NAME,
            notes = UserApi.SaveOrUpdateUserBankInfo.METHOD_API_NOTE)
    @RequestMapping(value = "/saveOrUpdateUserBankInfo", method = RequestMethod.POST)
    public ResultDto saveOrUpdateUserBankInfo(HttpServletRequest request,
                                              @ApiParam(UserApi.SaveOrUpdateUserBankInfo.METHOD_API_USERNAME) @RequestParam(name = "userName", required = false) String userName,
                                              @ApiParam(UserApi.SaveOrUpdateUserBankInfo.METHOD_API_BANKLOCATION) @RequestParam(name = "bankLocation", required = false) String bankLocation,
                                              @ApiParam(UserApi.SaveOrUpdateUserBankInfo.METHOD_API_BANKNAME) @RequestParam(name = "bankName", required = false) String bankName,
                                              @ApiParam(UserApi.SaveOrUpdateUserBankInfo.METHOD_API_BRANCHNAME) @RequestParam(name = "branchName", required = false) String branchName,
                                              @ApiParam(UserApi.SaveOrUpdateUserBankInfo.METHOD_API_BANKNUMBER) @RequestParam(name = "bankNumber", required = false) String bankNumber) {

        UserSessionDto userSessionDto = SessionUtils.getSession(request.getSession());  //获取用户session，拿到id

        UserBank userBank = new UserBank();
        userBank.setUserId(userSessionDto.getUserId());
        userBank.setUserName(userName);
        userBank.setBankLocation(bankLocation);
        userBank.setBankName(bankName);
        userBank.setBranchName(branchName);
        userBank.setBankNumber(bankNumber);

        UserBankDto userBankDto = userBankService.saveOrUpdateBankinfo(userBank);

        return ResultUtils.buildSuccessDto(userBankDto);
    }


//    ******************************
//    User:YeHao
//    ******************************

    /**
     * 获取缓存中的用户信息
     *
     * @return
     */
    @ApiOperation(value = UserApi.GetSessionUser.METHOD_API_NAME,
            notes = UserApi.GetSessionUser.METHOD_API_NOTE)
    @RequestMapping(value = "/getSessionUser", method = RequestMethod.GET)
    public ResultDto getSessionUser(HttpServletRequest request) {
        String id = userSession(request).getUserId();
        UserDto userDto = userService.getById(id);
//        userDto.setId("");
        userDto.setPassword("");
        userDto.setPaymentPassword("");
        return ResultUtils.buildSuccessDto(userDto);
    }

    /**
     * 获取缓存中的用户支付方式的绑定信息
     *
     * @return
     */
    @ApiOperation(value = UserApi.GetSessionUserPayType.METHOD_API_NAME,
            notes = UserApi.GetSessionUserPayType.METHOD_API_NOTE)
    @RequestMapping(value = "/getSessionUserPayType", method = RequestMethod.GET)
    public ResultDto getSessionUserPayType(HttpServletRequest request) {
        String id = userSession(request).getUserId();
        UserDto userDto = userService.getById(id);
        String payTypeValue = "payTypeValue";   //支付方式对应的编码
        String payTypeText = "payTypeText";     //支付方式名称
        List<Map<String, String>> userPayTypeListMap = new ArrayList<>();
        //判断是否绑定银行卡的支付方式
        if (StringUtils.isNotBlank(userDto.getPaymentMethodBankcard())) {
            Map<String, String> mapObj = new HashMap<>();
            mapObj.put(payTypeText, "银行卡");
            mapObj.put(payTypeValue, Integer.toString(BalanceOrderBillConstants.PAY_TYPE_BANK));
            userPayTypeListMap.add(mapObj);
        }
        //判断是否绑定微信的支付方式
        if (StringUtils.isNotBlank(userDto.getPaymentMethodWx())) {
            Map<String, String> mapObj = new HashMap<>();
            mapObj.put(payTypeText, "微信");
            mapObj.put(payTypeValue, Integer.toString(BalanceOrderBillConstants.PAY_TYPE_WECHAT));
            userPayTypeListMap.add(mapObj);
        }
        //判断是否绑定支付宝的支付方式
        if (StringUtils.isNotBlank(userDto.getPaymentMethodZfb())) {
            Map<String, String> mapObj = new HashMap<>();
            mapObj.put(payTypeText, "支付宝");
            mapObj.put(payTypeValue, Integer.toString(BalanceOrderBillConstants.PAY_TYPE_ALIPAY));
            userPayTypeListMap.add(mapObj);
        }
        return ResultUtils.buildSuccessDto(userPayTypeListMap);
    }


    /**
     * 昵称修改接口
     *
     * @param nickName 昵称
     * @return
     */
    @ApiOperation(value = UserApi.UpdateNickName.METHOD_API_NAME,
            notes = UserApi.UpdateNickName.METHOD_API_NOTE)
    @RequestMapping(value = "/updateNickName", method = RequestMethod.POST)
    public ResultDto updateNickName(
            @ApiParam(UserApi.UpdateNickName.METHOD_API_NICK_NAME) @RequestParam(name = "nickName", required = false) String nickName, HttpServletRequest request) {
        String id = userSession(request).getUserId();
        UserDto userDto = userService.updateNickName(id, nickName);

        //操作描述
        ResultDto resultDto = ResultUtils.buildSuccessDto(userDto.getNickName());
        resultDto.setMsg(UserResultEnums.NICK_NAME_SUCCESSFUL.getMsg());
        return resultDto;
    }

    /**
     * 账号修改接口
     *
     * @param userName  账号
     * @param phoneCode 验证码
     * @return
     */
    @ApiOperation(value = UserApi.UpdateUserName.METHOD_API_NAME,
            notes = UserApi.UpdateUserName.METHOD_API_NOTE)
    @RequestMapping(value = "/updateUserName", method = RequestMethod.POST)
    public ResultDto updateUserName(
            @ApiParam(UserApi.UpdateUserName.METHOD_API_USER_NAME) @RequestParam(name = "userName", required = false) String userName,
            @ApiParam(UserApi.UpdateUserName.METHOD_API_PHONE_CODE) @RequestParam(name = "phoneCode", required = false) String phoneCode,
            HttpServletRequest request) {
        //验证码判空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(phoneCode, new UserExeption(UserResultEnums.SMSGCODE_NULL));
        //获取缓存中的验证码
        String smsgCodeInRedis = (String) redisUtils.getValue(userName + "SMsg");
        //检查能否从缓存中获取之前发送的验证码
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(smsgCodeInRedis, new UserExeption(UserResultEnums.SMSG_OUTTIME)); //验证码过期
        if (phoneCode.equals(smsgCodeInRedis)) {  //验证码正确
            redisUtils.deleteByKey(userName + "SMsg");  //注册成功，删除缓存验证码
        } else {
            throw new UserExeption(UserResultEnums.SMSG_FALSE);
        }

        String id = userSession(request).getUserId();
        UserDto userDto = userService.updateUserName(id, userName);

        //操作描述
        ResultDto resultDto = ResultUtils.buildSuccessDto(userDto.getUserName());
        resultDto.setMsg(UserResultEnums.USER_NAME_SUCCESSFUL.getMsg());
        return resultDto;
    }

    /**
     * 登录密码修改接口
     *
     * @param password 登录密码
     * @return
     */
    @ApiOperation(value = UserApi.UpdatePassword.METHOD_API_NAME,
            notes = UserApi.UpdatePassword.METHOD_API_NOTE)
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public ResultDto updatePassword(
            @ApiParam(UserApi.UpdatePassword.METHOD_API_PASSWORD) @RequestParam(name = "password", required = false) String password, HttpServletRequest request) {
        String id = userSession(request).getUserId();
        UserDto userDto = userService.updatePassword(id, password);

        //操作描述
        ResultDto resultDto = ResultUtils.buildSuccessDto(userDto.getPassword());
        resultDto.setMsg(UserResultEnums.PASSWORD_SUCCESSFUL.getMsg());
        return resultDto;
    }

    /**
     * 支付密码修改接口
     *
     * @param paymentPassword 支付密码
     * @return
     */
    @ApiOperation(value = UserApi.UpdatePaymentPassword.METHOD_API_NAME,
            notes = UserApi.UpdatePaymentPassword.METHOD_API_NOTE)
    @RequestMapping(value = "/updatePaymentPassword", method = RequestMethod.POST)
    public ResultDto updatePaymentPassword(
            @ApiParam(UserApi.UpdatePaymentPassword.METHOD_API_PAYMENT_PASSWORD) @RequestParam(name = "paymentPassword", required = false) String paymentPassword, HttpServletRequest request) {
        String id = userSession(request).getUserId();
        UserDto userDto = userService.updatePaymentPassword(id, paymentPassword);

        //操作描述
        ResultDto resultDto = ResultUtils.buildSuccessDto(userDto.getPaymentPassword());
        resultDto.setMsg(UserResultEnums.PAYMENT_PASSWORD_SUCCESSFUL.getMsg());
        return resultDto;
    }

    /**
     * 银行卡号修改接口
     *
     * @param paymentMethodBankcard 银行卡号
     * @return
     */
    @ApiOperation(value = UserApi.UpdatePaymentMethodBankcard.METHOD_API_NAME,
            notes = UserApi.UpdatePaymentMethodBankcard.METHOD_API_NOTE)
    @RequestMapping(value = "/updatePaymentMethodBankcard", method = RequestMethod.POST)
    public ResultDto updatePaymentMethodBankcard(
            @ApiParam(UserApi.UpdatePaymentMethodBankcard.METHOD_API_PAYMENT_METHOD_BANKCARD) @RequestParam(name = "paymentMethodBankcard", required = false) String paymentMethodBankcard, HttpServletRequest request) {
        String id = userSession(request).getUserId();
        UserDto userDto = userService.updatePaymentMethodBankcard(id, paymentMethodBankcard);

        //操作描述
        ResultDto resultDto = ResultUtils.buildSuccessDto(userDto.getPaymentMethodBankcard());
        resultDto.setMsg(UserResultEnums.PAYMENT_METHOD_BANKCARD_SUCCESSFUL.getMsg());
        return resultDto;
    }

    /**
     * 支付宝账号修改接口
     *
     * @param paymentMethodZfb 支付宝账号
     * @return
     */
    @ApiOperation(value = UserApi.UpdatePaymentMethodZfb.METHOD_API_NAME,
            notes = UserApi.UpdatePaymentMethodZfb.METHOD_API_NOTE)
    @RequestMapping(value = "/updatePaymentMethodZfb", method = RequestMethod.POST)
    public ResultDto updatePaymentMethodZfb(
            @ApiParam(UserApi.UpdatePaymentMethodZfb.METHOD_API_PAYMENT_METHOD_ZFB) @RequestParam(name = "paymentMethodZfb", required = false) String paymentMethodZfb, HttpServletRequest request) {
        String id = userSession(request).getUserId();
        UserDto userDto = userService.updatePaymentMethodZfb(id, paymentMethodZfb);

        //操作描述
        ResultDto resultDto = ResultUtils.buildSuccessDto(userDto.getPaymentMethodZfb());
        resultDto.setMsg(UserResultEnums.PAYMENT_METHOD_ZFB_SUCCESSFUL.getMsg());
        return resultDto;
    }

    /**
     * 微信号修改接口
     *
     * @param paymentMethodWx 微信号
     * @return
     */
    @ApiOperation(value = UserApi.UpdatePaymentMethodWx.METHOD_API_NAME,
            notes = UserApi.UpdatePaymentMethodWx.METHOD_API_NOTE)
    @RequestMapping(value = "/updatePaymentMethodWx", method = RequestMethod.POST)
    public ResultDto updatePaymentMethodWx(
            @ApiParam(UserApi.UpdatePaymentMethodWx.METHOD_API_PAYMENT_METHOD_WX) @RequestParam(name = "paymentMethodWx", required = false) String paymentMethodWx, HttpServletRequest request) {
        String id = userSession(request).getUserId();
        UserDto userDto = userService.updatePaymentMethodWx(id, paymentMethodWx);

        //操作描述
        ResultDto resultDto = ResultUtils.buildSuccessDto(userDto.getPaymentMethodWx());
        resultDto.setMsg(UserResultEnums.PAYMENT_METHOD_WX_SUCCESSFUL.getMsg());
        return resultDto;
    }

    /**
     * 登录密码验证接口
     *
     * @param password 登录密码
     * @return
     */
    @ApiOperation(value = UserApi.IsPassword.METHOD_API_NAME,
            notes = UserApi.IsPassword.METHOD_API_NOTE)
    @RequestMapping(value = "/isPassword", method = RequestMethod.POST)
    public ResultDto isPassword(
            @ApiParam(UserApi.IsPassword.METHOD_API_PASSWORD) @RequestParam(name = "password", required = false) String password, HttpServletRequest request) {
        String id = userSession(request).getUserId();
        boolean isPassword = userService.isPassword(id, password);

        //操作描述
        ResultDto resultDto = ResultUtils.buildSuccessDto(isPassword);
        resultDto.setMsg(UserResultEnums.IS_PASSWORD_SUCCESSFUL.getMsg());
        return resultDto;
    }

    /**
     * 支付密码验证接口
     *
     * @param paymentPassword 支付密码
     * @return
     */
    @ApiOperation(value = UserApi.isPaymentPassword.METHOD_API_NAME,
            notes = UserApi.isPaymentPassword.METHOD_API_NOTE)
    @RequestMapping(value = "/isPaymentPassword", method = RequestMethod.POST)
    public ResultDto isPaymentPassword(
            @ApiParam(UserApi.isPaymentPassword.METHOD_API_PAYMENT_PASSWORD) @RequestParam(name = "paymentPassword", required = false) String paymentPassword, HttpServletRequest request) {
        String id = userSession(request).getUserId();
        boolean isPaymentPassword = userService.isPaymentPassword(id, paymentPassword);
        //操作描述
        ResultDto resultDto = ResultUtils.buildSuccessDto(isPaymentPassword);
        resultDto.setMsg(UserResultEnums.IS_PAYMENT_PASSWORD_SUCCESSFUL.getMsg());
        return resultDto;
    }

    /**
     * 支付密码验证接口
     *
     * @param user 用户信息
     * @return
     */
    @ApiOperation(value = UserApi.updateUser.METHOD_API_NAME,
            notes = UserApi.updateUser.METHOD_API_NOTE)
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public ResultDto updateUser(@ApiParam(UserApi.updateUser.METHOD_API_USER_INFO) @RequestBody User user) {
        UserDto userDto = userService.updateUserInfo(user);
        ResultDto resultDto = ResultUtils.buildSuccessDto(userDto);
        return resultDto;
    }

    @ApiOperation(value = UserApi.updateHeadImg.METHOD_API_NAME,
            notes = UserApi.updateHeadImg.METHOD_API_NOTE)
    @RequestMapping(value = "/updateHeadImg", method = RequestMethod.POST)
    public ResultDto updateHeadImg(@ApiParam(UserApi.updateHeadImg.METHOD_API_IMG) @RequestParam(value = "img", required = false) String img,
                                   HttpServletRequest request) {
    //public ResultDto updateHeadImg(@RequestBody String img, HttpServletRequest request) {
        System.out.println(img);
        //返回的状态信息
        ResultDto resultDto = new ResultDto();
        //img文件对象
        File imgFile = null;
        try {
            //图片base64判空
            if (null == img || "".equals(img.trim())) {
                resultDto.setCode(UserResultEnums.FILE_NULL.getCode());
                resultDto.setMsg(UserResultEnums.FILE_NULL.getMsg());
            }
            //获取图片文件
            imgFile = generateImage(img);
            String imgName = imgFile.getName();
            userService.updateHeadImg(userSession(request).getUserId(), imgName);
            resultDto = ResultUtils.buildSuccessDto(imgName);
        } catch (Exception e) {
            resultDto.setCode(UserResultEnums.FILE_ERROR.getCode());
            resultDto.setMsg(UserResultEnums.FILE_ERROR.getMsg());
            if (imgFile != null && imgFile.exists()) {
                imgFile.delete();
            }
            e.printStackTrace();
        } finally {
            return resultDto;
        }
    }

    @ApiOperation(value = UserApi.GetUserBaseInfo.METHOD_API_NAME,
            notes = UserApi.GetUserBaseInfo.METHOD_API_NOTE)
    @RequestMapping(value = "/getUserBaseInfo", method = RequestMethod.POST)
    public ResultDto<UserBaseInfoDto> getUserBaseInfo(HttpServletRequest request) {
        UserSessionDto userDto = SessionUtils.getSession(request.getSession());
        UserBaseInfoDto user = userService.selectBaseInfoByUserId(userDto.getUserId());
        return ResultUtils.buildSuccessDto(user);
    }

    /**
     * 获取缓存中的用户信息
     *
     * @param request
     * @return
     */
    private UserSessionDto userSession(HttpServletRequest request) {
        UserSessionDto sessionDto = SessionUtils.getSession(request.getSession());
        return sessionDto;
    }

    /**
     * 生成随机文件名：当前年月日时分秒+五位随机数
     *
     * @return 文件名
     */
    private String getRandomFileName() {

        SimpleDateFormat simpleDateFormat;

        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        Date date = new Date();

        String str = simpleDateFormat.format(date);

        Random random = new Random();

        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数

        return rannum + str;// 当前时间
    }

    //base64字符串转化成图片
    public File generateImage(String imgStr) throws Exception {   //对字节数组字符串进行Base64解码并生成图片
        BASE64Decoder decoder = new BASE64Decoder();
        //Base64解码
        byte[] b = decoder.decodeBuffer(imgStr);
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {//调整异常数据
                b[i] += 256;
            }
        }
        //自定义文件名
        String fileName = getRandomFileName() + ".jpg";
        //判断文件夹是否存在
        File filePath = new File(FILE_IMG_PATH);
        if (!filePath.exists() && !filePath.isDirectory()) {
            filePath.mkdir();
        }
        //生成jpeg图片
        String imgFilePath = FILE_IMG_PATH + fileName;//新生成的图片
        OutputStream out = new FileOutputStream(imgFilePath);
        out.write(b);
        out.flush();
        out.close();
        return new File(imgFilePath);
    }


    /******lzf******/
    /***
     * 根据用户id查询等级和用户基本信息
     */
    @ApiOperation(value = UserApi.getUserTransferInfo.METHOD_API_NAME,
            notes = UserApi.getUserTransferInfo.METHOD_API_NOTE)
    @RequestMapping(value = "/getUserTransferInfo", method = RequestMethod.GET)
    public ResultDto getUserTransferInfo(
            @ApiParam(UserApi.getUserTransferInfo.METHOD_USERID)@RequestParam("userId") String userId) {
        return ResultUtils.buildSuccessDto(userService.selectUserTransferInfoByUserId(userId));
    }

    /***
     * 根据电话号查询用户支付账号信息
     */
    @ApiOperation(value = UserApi.getPayInfo.METHOD_API_NAME,
            notes = UserApi.getPayInfo.METHOD_API_NOTE)
    @RequestMapping(value = "/getPayInfo", method = RequestMethod.GET)
    public ResultDto getPayInfo(
            @ApiParam(UserApi.getPayInfo.METHOD_USERNAME)@RequestParam("userName") String userName) {
        return ResultUtils.buildSuccessDto(userService.selectPayInfoByUserTel(userName));
    }

    /**
     * 重置支付密码
     *
     * @param userName  账号
     * @param phoneCode 验证码
     * @return
     */
    @ApiOperation(value = UserApi.updateResetPayPassWord.METHOD_API_NAME,
            notes = UserApi.updateResetPayPassWord.METHOD_API_NOTE)
    @RequestMapping(value = "/updateResetPayPassWord", method = RequestMethod.POST)
    public ResultDto updateResetPayPassWord(
            @ApiParam(UserApi.updateResetPayPassWord.METHOD_API_USER_NAME) @RequestParam(name = "userName") String userName,
            @ApiParam(UserApi.updateResetPayPassWord.METHOD_API_PHONE_CODE) @RequestParam(name = "phoneCode") String phoneCode,
            HttpServletRequest request) {
        //验证码判空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(phoneCode, new UserExeption(UserResultEnums.SMSGCODE_NULL));
        //获取缓存中的验证码
        String smsgCodeInRedis = (String) redisUtils.getValue(userName + "SMsg");
        //检查能否从缓存中获取之前发送的验证码
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(smsgCodeInRedis, new UserExeption(UserResultEnums.SMSG_OUTTIME)); //验证码过期
        if (phoneCode.equals(smsgCodeInRedis)) {  //验证码正确
            redisUtils.deleteByKey(userName + "SMsg");  //注册成功，删除缓存验证码
        } else {
            throw new UserExeption(UserResultEnums.SMSG_FALSE);
        }
        //获取缓存中的用户账号
        String tel = userSession(request).getTelephone();
        userService.updateResetPayPassWord(tel, userName);
        //操作描述
        ResultDto resultDto = ResultUtils.buildSuccessDto(null);
        return resultDto;
    }
}

