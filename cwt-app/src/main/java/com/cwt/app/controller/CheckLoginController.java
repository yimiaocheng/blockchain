package com.cwt.app.controller;


import com.alibaba.fastjson.JSON;
import com.cwt.app.common.dto.ResultDTO;
import com.cwt.app.common.dto.UserSessionDto;
import com.cwt.app.common.util.SessionUtils;
import com.cwt.common.enums.BaseResultEnums;
import com.cwt.common.exception.BaseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * yimiao
 */
@Api("php商城接口控制器")
@RestController
@RequestMapping("/php/login")
public class CheckLoginController {

    private static final Logger logger = LoggerFactory.getLogger(CheckLoginController.class);
    /**
     * 登录验证
     * @param
     * @return
     */
    @ApiOperation(value = "php对接接口", notes = "登录验证")
    @RequestMapping(value="/checkLogin",method = RequestMethod.POST)
    public ResultDTO checkLogin(HttpServletRequest request){
        UserSessionDto sessionDto = SessionUtils.getSession(request.getSession());
        if(sessionDto==null){
            throw new BaseException(BaseResultEnums.NO_LOGIN);//app未登录
        }
        ResultDTO resultDTO = ResultDTO.requstSuccess(sessionDto);
        logger.info("获取登录验证信息：返回结果{}", JSON.toJSON(resultDTO));
        return resultDTO;
    }
}
