package com.cwt.app.common.util;


import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 *短信API服务调用 － 聚合数据
 **/
@Component
public  class SendSmsgCode {
    private static final Logger LOG = LoggerFactory.getLogger(SendSmsgCode.class);
    //配置发送短信接口申请的KEY
    private static final String SMS_APPKEY = "fec1b4d1f1fe3ccac6fbbcee239264d9";
    //配置申请的短信模板id
    private static final String SMS_TPL_ID = "100151"; //CT模板

    private static final String SMS_URL = "http://v.juhe.cn/sms/send";
    /**
     * 请求聚合数据接口，发送短信验证码
     * @param mobile 手机号
     * @param smsCode 短信验证码
     * @return
     */
    @Async
    public  void sendSmsg(String mobile, String smsCode, RestTemplate restTemplate){
        LOG.info("mobile:"+mobile+",smsgCode:"+smsCode);
        String url = SMS_URL;//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("mobile", mobile);//接收短信的手机号码
        params.put("tpl_id", SMS_TPL_ID);//短信模板ID，请参考个人中心短信模板设置
        params.put("tpl_value", "#code#=" + smsCode);//变量名和变量值对
        params.put("key", SMS_APPKEY);//应用APPKEY

        url = url + "?" + urlencode(params);
        ResponseEntity<JSONObject> json = restTemplate.getForEntity(url, JSONObject.class);
        if(200 != json.getStatusCodeValue()){
            LOG.error(json.toString());
        }
        LOG.info("===send sms end====");
    }

    //将map型转为请求参数型
    private static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            }catch(UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}