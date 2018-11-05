package com.cwt.app.controller;

import com.cwt.app.common.dto.UserSessionDto;
import com.cwt.app.common.util.RedisUtils;
import com.cwt.app.common.util.ResultUtils;
import com.cwt.app.common.util.SendSmsgCode;
import com.cwt.app.common.util.SessionUtils;
import com.cwt.app.controller.api.LoginApi;
import com.cwt.common.constant.BaseConstants;
import com.cwt.common.enums.UserResultEnums;
import com.cwt.common.exception.UserExeption;
import com.cwt.common.util.BeanUtils;
import com.cwt.common.util.ExceptionPreconditionUtils;
import com.cwt.domain.dto.ResultDto;
import com.cwt.domain.dto.mySettings.AgreementDto;
import com.cwt.domain.dto.user.SmsgLimitDto;
import com.cwt.domain.dto.user.UserDto;
import com.cwt.domain.dto.user.UserLoginReturnDto;
import com.cwt.service.service.AgreementService;
import com.cwt.service.service.SmsgLimitService;
import com.cwt.service.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Api(LoginApi.USER_CONTROLLER_API)
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private SmsgLimitService smsgLimitService;
    @Autowired
    private SendSmsgCode sendSmsgCode;
    @Value("${OPEN_SSM}")
    private boolean openSms;

    /**
     *注册接口
     * @param userName 手机
     * @param password 密码
     * @param smsgCode 验证码
     * @return
     */
    @ApiOperation(value = LoginApi.Register.METHOD_API_NAME,
            notes = LoginApi.Register.METHOD_API_NOTE)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResultDto register(
            @ApiParam(LoginApi.Register.METHOD_API_TELEPHONE) @RequestParam("userName") String userName,
            @ApiParam(LoginApi.Register.METHOD_API_PSSWORD) @RequestParam("password") String password,
            @ApiParam(LoginApi.Register.METHOD_API_CODE) @RequestParam("phoneCode") String smsgCode,
            @ApiParam(LoginApi.Register.METHOD_API_INVITERID) @RequestParam("inviterCode") Integer inviterCode) {

        //检查参数
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(smsgCode, new UserExeption(UserResultEnums.SMSGCODE_NULL));
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(userName, new UserExeption(UserResultEnums.NULL_TELEPHONE));

        UserDto userDto = null;
        UserDto returnDto = new UserDto();

        String smsgCodeInRedis = (String) redisUtils.getValue(userName+"SMsg");
        //检查能否从缓存中获取之前发送的验证码
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(smsgCodeInRedis, new UserExeption(UserResultEnums.SMSG_OUTTIME)); //验证码过期

        if(smsgCode.equals(smsgCodeInRedis)){  //验证码正确
            userDto = userService.register(userName, password, smsgCode, inviterCode);  //注册
            returnDto = new UserDto();
            returnDto.setId(userDto.getId());
            redisUtils.deleteByKey(userName+"SMsg");  //注册成功，删除缓存验证码
        }else{
            throw new UserExeption(UserResultEnums.SMSG_FALSE);
        }
        ResultDto resultDto = ResultUtils.buildSuccessDto("注册");
        resultDto.setMsg("注册成功");

        return resultDto;
    }

    /**
     * 登录接口
     * @param request 请求参数
     * @param userName 手机号
     * @param password 密码
     * @return
     */
    @ApiOperation(value = LoginApi.Login.METHOD_API_NAME,
            notes = LoginApi.Login.METHOD_API_NOTE)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultDto login(HttpServletRequest request,
           @ApiParam(LoginApi.Login.METHOD_API_TELEPHONE) @RequestParam("userName") String userName,
           @ApiParam(LoginApi.Login.METHOD_API_PSSWORD) @RequestParam("password") String password) {

        UserDto userDto = userService.login(userName, password);  //用户登录，查询用户信息

        UserSessionDto userSession = new UserSessionDto();
        userSession.setUserId(userDto.getId());
        userSession.setTelephone(userDto.getUserName());
        userSession.setToken(UUID.randomUUID().toString());

        SessionUtils.setSession(request.getSession(), userSession);  //设置用户session

        redisUtils.setKeyValue(BaseConstants.LOGIN_USER_REDIS_KEY  + userSession.getToken(), userSession, BaseConstants.LOGIN_CACHE_TIME_DEFAULT);  //设置用户session到redis缓存


        UserLoginReturnDto userLoginReturnDto = new UserLoginReturnDto();  //登陆后返回一个携带token的用户对象
        BeanUtils.copySamePropertyValue(userDto, userLoginReturnDto);
        userLoginReturnDto.setToken(userSession.getToken());

        return ResultUtils.buildSuccessDto(userLoginReturnDto);
    }

    /**
     * 退出登录
     * @param request 请求
     * @return
     */
    @ApiOperation(value = LoginApi.LoginOut.METHOD_API_NAME,
            notes = LoginApi.LoginOut.METHOD_API_NOTE)
    @RequestMapping(value = "/loginOut", method = RequestMethod.POST)
    public ResultDto loginOut(HttpServletRequest request) {

        UserSessionDto userSession = SessionUtils.getSession(request.getSession());  //获取用户session

        SessionUtils.removeSession(request.getSession());  //删除用户登录session

        redisUtils.deleteByKey(BaseConstants.LOGIN_USER_REDIS_KEY  + userSession.getToken());  //删除redis中缓存的userToken

        return ResultUtils.buildSuccessDto(new UserDto());
    }

    /**
     * 找回密码
     * @param mobilePhone 手机号
     * @param smsgCode 验证码
     * @param newPassword 新密码
     * @return
     */
    @ApiOperation(value = LoginApi.FindPassword.METHOD_API_NAME,
            notes = LoginApi.FindPassword.METHOD_API_NOTE)
    @RequestMapping(value = "/findPassword", method = RequestMethod.POST)
    public ResultDto findPassword(
              @ApiParam(LoginApi.FindPassword.METHOD_API_TELEPHONE) @RequestParam("mobilePhone") String mobilePhone,
              @ApiParam(LoginApi.FindPassword.METHOD_API_SMSG_CODE) @RequestParam("smsgCode") String smsgCode,
              @ApiParam(LoginApi.FindPassword.METHOD_API_NEWPASSWORD) @RequestParam("newPassword") String newPassword) {

        //检查参数
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(smsgCode, new UserExeption(UserResultEnums.SMSGCODE_NULL));
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(mobilePhone, new UserExeption(UserResultEnums.NULL_TELEPHONE));

        UserDto userDto = new UserDto();
        userDto.setPassword(newPassword);
        userDto.setUserName(mobilePhone);

        String smsgCodeInRedis = (String) redisUtils.getValue(mobilePhone+"SMsg");  //从缓存中获取之前发送的验证码
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(smsgCodeInRedis, new UserExeption(UserResultEnums.SMSG_OUTTIME)); //验证码过期
        if(smsgCode.equals(smsgCodeInRedis)){  //验证码正确
            userService.updateUserInfo("password", userDto);  //更新新密码

            redisUtils.deleteByKey(mobilePhone+"SMsg");  //找回密码成功，删除缓存验证码
        }else{
            throw new UserExeption(UserResultEnums.SMSG_FALSE);  //验证码错误
        }

        return ResultUtils.buildSuccessDto(userDto);
    }

    /**
     * 发送手机短信验证码
     * @param mobilePhone 手机号
     */
    @ApiOperation(value = LoginApi.Smsg.METHOD_API_NAME,
            notes = LoginApi.Smsg.METHOD_API_NOTE)
    @RequestMapping(value = "/getSmsgCode", method = RequestMethod.POST)
    public ResultDto getSmsgCode(
            @ApiParam(LoginApi.Smsg.METHOD_API_TELEPHONE) @RequestParam("mobilePhone") String mobilePhone) {
        //检查手机号不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(mobilePhone, new UserExeption(UserResultEnums.NULL_TELEPHONE));

        SmsgLimitDto smsgLimitDto = smsgLimitService.selectCountByMobilePhone(mobilePhone);  //查询今天验证码条数
        if(smsgLimitDto.getSmsgCount() < 10){  //小于十条
            String smsgCode;
            if(openSms){//是否开启短信认证
                smsgCode = RandomStringUtils.random(6, false, true);  //随机六位数字验证码
                sendSmsgCode.sendSmsg(mobilePhone, smsgCode,restTemplate);  //发送验证码
            }else{
                smsgCode = "888888";
            }
            redisUtils.setKeyValue(mobilePhone+"SMsg", smsgCode, 5*60);  //短信验证码5分钟内有效
            smsgLimitService.addSmsgCount(mobilePhone);  //记录条数加1
        }else{  //大于十条
            throw new UserExeption(UserResultEnums.SMSGCODE_OVERFLOW);  //验证码超过10条
        }

        return ResultUtils.buildSuccessDto(smsgLimitDto);
    }


    @Autowired
    private AgreementService agreementService;

    /**
     * 获取平台服务协议
     * @return
     */
    @ApiOperation(value = LoginApi.Agreement.METHOD_API_NAME,
            notes = LoginApi.Agreement.METHOD_API_NOTE)
    @RequestMapping(value = "/agreement", method = RequestMethod.POST)
    public ResultDto agreement() {

        AgreementDto agreementDto = agreementService.selectAgreement().get(0);

        return  ResultUtils.buildSuccessDto(agreementDto);
    }
}

