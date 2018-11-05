package com.cwt.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cwt.app.common.dto.ResultDTO;
import com.cwt.app.common.dto.UserSessionDto;
import com.cwt.app.common.util.Public;
import com.cwt.app.common.util.RedisUtils;
import com.cwt.app.common.util.SessionUtils;
import com.cwt.common.enums.BaseResultEnums;
import com.cwt.common.enums.PayResultEnums;
import com.cwt.common.exception.BaseException;
import com.cwt.common.exception.PayException;
import com.cwt.domain.constant.UserAwardConstants;
import com.cwt.domain.constant.UserPayConstants;
import com.cwt.domain.dto.request.PrePayReq;
import com.cwt.domain.dto.request.UserAwardReq;
import com.cwt.domain.dto.user.UserInfo;
import com.cwt.domain.entity.UserBalance;
import com.cwt.domain.entity.UserPay;
import com.cwt.service.service.PayService;
import com.cwt.service.service.UserService;
import com.cwt.service.service.WalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;

/**
 * yimiao
 */
@Api("php商城接口控制器")
@RestController
@RequestMapping("/php/a6shop")
public class ShopController {

    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private WalletService walletService;

    @Autowired
    private PayService payService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    /**
     * 通过用户id查询用户信息
     * @param jsonStr
     * @return
     */
    @ApiOperation(value = "php对接接口", notes = "通过用户id查询用户信息")
    @RequestMapping(value="/findUserInfo",method = RequestMethod.POST)
    public ResultDTO findUserInfo(@RequestBody String jsonStr){
        logger.info("通过用户id查询用户信息：请求参数{}", jsonStr);
        //将json字符串转化为json对象
        String userId = JSONObject.parseObject(jsonStr).getString("userId");
        UserInfo userInfo = userService.findUserInfo(userId);
        ResultDTO resultDTO = ResultDTO.requstSuccess(userInfo);
        logger.info("通过用户id查询用户信息：返回结果{}", JSON.toJSON(resultDTO));
        return resultDTO;
    }

    /**
     * 查询用户积分，ct余额信息
     * @param jsonStr
     * @return
     */
    @ApiOperation(value = "php对接接口", notes = "查询用户积分，ct余额信息")
    @RequestMapping(value="/findUserBalance",method = RequestMethod.POST)
    public ResultDTO findUserBalance(@RequestBody String jsonStr){
        logger.info("获取查询用户余额信息：请求参数{}", jsonStr);
        //将json字符串转化为json对象
        String userId = JSONObject.parseObject(jsonStr).getString("userId");
        UserBalance userBalance = walletService.findUserBalance(userId);
        ResultDTO resultDTO = ResultDTO.requstSuccess(userBalance);
        logger.info("获取查询用户余额信息：返回结果{}", JSON.toJSON(resultDTO));
        return resultDTO;
    }

    /**
     * 预支付
     * @param prePayReq
     * @return
     */
    @ApiOperation(value = "php对接接口", notes = "预支付")
    @RequestMapping(value="/prepay",method = RequestMethod.POST)
    public ResultDTO prepay(@RequestBody PrePayReq prePayReq){
        logger.info("获取预支付信息：请求参数{}", JSON.toJSON(prePayReq));
        UserPay userPay = payService.prepay(prePayReq);
        //将订单信息保存到redis中（设置过期时间3-5分钟）
        redisUtils.setKeyValue(userPay.getId(),userPay,10*60);//4分钟缓存失效
        ResultDTO resultDTO = ResultDTO.requstSuccess(userPay.getId());
        logger.info("获取预支付信息：返回结果{}", JSON.toJSON(resultDTO));
        return resultDTO;
    }

    /**
     * 支付
     * @param
     * @return
     */
    @ApiOperation(value = "php对接接口", notes = "支付")
    @RequestMapping(value="/payment",method = RequestMethod.POST)
    public void payment(@RequestBody String jsonStr)throws Exception{
        logger.info("获取支付信息：请求参数{}", jsonStr);
        JSONObject json = JSONObject.parseObject(jsonStr);
        String prepayId = json.getString("prepayId");//预支付id
        String password = json.getString("password");//支付密码
        //通过redis获取订单相关信息
        UserPay userPay = (UserPay)redisUtils.getValue(prepayId);
        if(userPay==null){
            throw new PayException(PayResultEnums.PAYMENT_TOO_LONG);//预支付id过期 支付超时
        }
        UserPay payInfo = payService.payment(userPay,password);
        ResultDTO resultDTO = ResultDTO.requstSuccess(payInfo);
        logger.info("获取支付信息：返回结果{}",  JSON.toJSON(resultDTO));
        //调用支付回调接口，将信息传过去
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(resultDTO);
        HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString() , headers) ;//参数
        restTemplate.postForEntity(URLDecoder.decode(payInfo.getNotifyUrl(),"UTF-8"), entity,String.class);
    }


    /**
     * 查询订单支付结果
     * @param jsonStr
     * @return
     */
    @ApiOperation(value = "php对接接口", notes = "查询订单支付结果")
    @RequestMapping(value="/findPayResultByOrderId",method = RequestMethod.POST)
    public ResultDTO findPayResultByOrderId(@RequestBody String jsonStr){
        logger.info("获取查询订单支付结果：请求参数{}", jsonStr);
        //订单号
        String orderNo = JSONObject.parseObject(jsonStr).getString("orderNo");
        String status = payService.findPayResultByOrderId(orderNo);
        ResultDTO resultDTO = ResultDTO.requstSuccess(status);
        logger.info("获取查询订单支付结果：返回结果{}",  JSON.toJSON(resultDTO));
        return resultDTO;
    }

    /**
     * 确认收款（用户确认收货时触发）
     * @param jsonStr
     * @return
     */
    @ApiOperation(value = "php对接接口", notes = "确认收款")
    @RequestMapping(value="/receive",method = RequestMethod.POST)
    public ResultDTO receive(@RequestBody String jsonStr){
        logger.info("获取确认收款信息：请求参数{}", jsonStr);
        JSONObject json = JSONObject.parseObject(jsonStr);
        String orderNo = json.getString("orderNo");//订单号
        String fromId = json.getString("fromId");//用户id
        String toId = json.getString("toId");//商户id
        String fee_ct = json.getString("fee_ct");//订单费用ct
        String fee_integral = json.getString("fee_integral");//订单费用integral
        String type = json.getString("type");//商户类型
        //多商户 单商户没有收款
        if(UserPayConstants.USER_TYPE_1.equals(type)){
            payService.receive(orderNo,fromId,toId,fee_ct,fee_integral);
        }
        ResultDTO resultDTO = ResultDTO.requstSuccess();
        logger.info("获取确认收款信息：返回结果{}",  JSON.toJSON(resultDTO));
        return resultDTO;
    }

    /**
     * 退款
     * @param jsonStr
     * @return
     */
    @ApiOperation(value = "php对接接口", notes = "退款")
    @RequestMapping(value="/refund",method = RequestMethod.POST)
    public ResultDTO refund(@RequestBody String jsonStr){
        logger.info("获取用户退款信息：请求参数{}", jsonStr);
        JSONObject json = JSONObject.parseObject(jsonStr);
        String orderNo = json.getString("orderNo");//订单号(子订单号)
        String fromId = json.getString("fromId");//用户id
        String toId = json.getString("toId");//商户id
        String fee_ct = json.getString("fee_ct");//订单费用ct
        String fee_integral = json.getString("fee_integral");//订单费用integral
        String type = json.getString("type");//商户类型
        //退款只是单方面的对用户操作
        if(UserPayConstants.USER_TYPE_1.equals(type)){
            payService.refund(fromId,toId,orderNo,fee_ct,fee_integral);
        }else{
            payService.refund(orderNo,fromId,fee_ct,fee_integral);
        }
        ResultDTO resultDTO = ResultDTO.requstSuccess();
        logger.info("获取用户退款信息：返回结果{}", JSON.toJSON(resultDTO));
        return resultDTO;
    }


    /**
     * 奖金发放（1.销售提成 2.差额利润 3.服务补贴 4.加权分红（月结或者周结））
     * @param userAwardReq
     */
    @ApiOperation(value = "php对接接口", notes = "奖金发放")
    @RequestMapping(value="/award",method = RequestMethod.POST)
    public ResultDTO award(@RequestBody UserAwardReq userAwardReq){
        logger.info("获取奖金发放信息：请求参数{}", JSON.toJSON(userAwardReq));
        String startTime = Public.DateTOString(Public.GetNewDate());
        System.out.println("开始时间："+ startTime);
        if(userAwardReq!=null){
            //1.将奖金发放信息储存到数据库表中 状态为 待发放
            payService.saveUserAward(userAwardReq);
            if(!UserAwardConstants.AWARD_TYPE_WEIGHTED.equals(userAwardReq.getAward_type())){
                payService.award(userAwardReq);
            }
        }
        ResultDTO resultDTO = ResultDTO.requstSuccess();
        logger.info("获取奖金发放信息：返回结果{}", JSON.toJSON(resultDTO));
        System.out.println("结束时间："+ Public.DateTOString(Public.GetNewDate()));
        logger.info("耗时："+Public.getLogStr(startTime, Public.DateTOString(Public.GetNewDate())));
        return resultDTO;
    }



}
