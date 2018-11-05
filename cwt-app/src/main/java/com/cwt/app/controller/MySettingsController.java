package com.cwt.app.controller;

import com.cwt.app.common.dto.UserSessionDto;
import com.cwt.app.common.util.ResultUtils;
import com.cwt.app.common.util.SessionUtils;
import com.cwt.app.controller.api.MySettingsApi;
import com.cwt.domain.dto.ResultDto;
import com.cwt.domain.dto.mySettings.*;
import com.cwt.service.service.IVersionService;
import com.cwt.service.service.MySettingsService;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(MySettingsApi.MYSETTINGS_CONTROLLER_API)
@RestController
@RequestMapping("/mySettings")
public class MySettingsController {

    @Autowired
    private MySettingsService mySettingsService;

    /**
     * 获取用户接收消息列表
     * @param request
     * @return
     */
    @ApiOperation(value = MySettingsApi.ListAcceptMessage.METHOD_API_NAME,
            notes = MySettingsApi.ListAcceptMessage.METHOD_API_NOTE)
    @RequestMapping(value = "/listAcceptMessage", method = RequestMethod.POST)
    public ResultDto listAcceptMessage(HttpServletRequest request,
           @RequestParam(value = "pageNum", required = false) Integer pageNum,
           @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        UserSessionDto userSession = SessionUtils.getSession(request.getSession());  //获取用户session
        PageHelper.startPage(pageNum == null ? 0 : pageNum, pageSize == null ? 10 : pageSize);
        List<AcceptMessageDto> acceptMessageDtoList = mySettingsService.listAcceptMessage(userSession.getUserId());

        return ResultUtils.buildSuccessDto(acceptMessageDtoList);
    }

    /**
     * 已读该消息
     * @param msgId 消息id
     * @return
     */
    @ApiOperation(value = MySettingsApi.ReadAcceptMessage.METHOD_API_NAME,
            notes = MySettingsApi.ReadAcceptMessage.METHOD_API_NOTE)
    @RequestMapping(value = "/readAcceptMessage", method = RequestMethod.POST)
    public ResultDto readAcceptMessage(@RequestParam("msgId") String msgId) {

        Integer countRow = mySettingsService.readAcceptMessage(msgId);

        ResultDto resultDto = ResultUtils.buildSuccessDto(countRow);
        resultDto.setMsg("已读该消息");
        return resultDto;
    }

    /**
     * 获取未读接收消息数量
     * @param request
     * @return
     */
    @ApiOperation(value = MySettingsApi.CountNoReadAcceptMessage.METHOD_API_NAME,
            notes = MySettingsApi.CountNoReadAcceptMessage.METHOD_API_NOTE)
    @RequestMapping(value = "/countNoReadAcceptMessage", method = RequestMethod.POST)
    public ResultDto countNoReadAcceptMessage(HttpServletRequest request) {
        UserSessionDto userSession = SessionUtils.getSession(request.getSession());  //获取用户session
        Integer count = mySettingsService.countNoReadAcceptMessage(userSession.getUserId());

        ResultDto resultDto = ResultUtils.buildSuccessDto(count);
        resultDto.setMsg("未读接收消息数量");
        return resultDto;
    }

    /**
     * 获取所有社区公告列表
     * @return
     */
    @ApiOperation(value = MySettingsApi.ListAllCommunityBulletin.METHOD_API_NAME,
            notes = MySettingsApi.ListAllCommunityBulletin.METHOD_API_NOTE)
    @RequestMapping(value = "/listAllCommunityBulletin", method = RequestMethod.POST)
    public ResultDto listAllCommunityBulletin(@RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize ) {
        PageHelper.startPage(pageNum == null ? 0 : pageNum, pageSize == null ? 10 : pageSize);
        List<CommunityBulletinDto> communityBulletinDtoList = mySettingsService.listAllCommunityBulletin();

        return ResultUtils.buildSuccessDto(communityBulletinDtoList);
    }

    /**
     * 提交投诉建议
     * @param request
     * @param cpContent
     * @return
     */
    @ApiOperation(value = MySettingsApi.InsertComplaintProposal.METHOD_API_NAME,
            notes = MySettingsApi.InsertComplaintProposal.METHOD_API_NOTE)
    @RequestMapping(value = "/insertComplaintProposal", method = RequestMethod.POST)
    public ResultDto insertComplaintProposal(HttpServletRequest request,
             @ApiParam(MySettingsApi.InsertComplaintProposal.METHOD_API_CPCONTENT) @RequestParam("cpContent") String cpContent) {
        UserSessionDto userSession = SessionUtils.getSession(request.getSession());  //获取用户session

        Integer count = mySettingsService.insertComplaintProposal(userSession.getUserId(), cpContent);

        ResultDto resultDto = ResultUtils.buildSuccessDto(count);
        resultDto.setMsg("提交投诉建议成功");
        return resultDto;
    }

    /**
     * 获取所有关于我们列表
     * @return
     */
    @ApiOperation(value = MySettingsApi.ListAllAboutUs.METHOD_API_NAME,
            notes = MySettingsApi.ListAllAboutUs.METHOD_API_NOTE)
    @RequestMapping(value = "/listAllAboutUs", method = RequestMethod.POST)
    public ResultDto listAllAboutUs(@RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        PageHelper.startPage(pageNum == null ? 0 : pageNum, pageSize == null ? 10 : pageSize);
        List<AboutUsDto> aboutUsDtoList = mySettingsService.listAllAboutUs();

        return ResultUtils.buildSuccessDto(aboutUsDtoList);
    }

    /**
     * 获取首页轮播图列表
     * @return
     */
    @ApiOperation(value = MySettingsApi.ListAdSliderForApp.METHOD_API_NAME,
            notes = MySettingsApi.ListAdSliderForApp.METHOD_API_NOTE)
    @RequestMapping(value = "/listAdSliderForApp", method = RequestMethod.POST)
    public ResultDto listAdSliderForApp() {
        List<AdSliderDto> adSliderDtoList = mySettingsService.listAdSliderForApp();

        return ResultUtils.buildSuccessDto(adSliderDtoList);
    }

}

