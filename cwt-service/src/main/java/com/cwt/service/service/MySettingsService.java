package com.cwt.service.service;


import com.cwt.domain.dto.mySettings.AboutUsDto;
import com.cwt.domain.dto.mySettings.AcceptMessageDto;
import com.cwt.domain.dto.mySettings.AdSliderDto;
import com.cwt.domain.dto.mySettings.CommunityBulletinDto;

import java.util.List;


public interface MySettingsService {

    /**
     * 获得个人接受消息列表
     * @param userId
     * @return
     */
    List<AcceptMessageDto> listAcceptMessage(String userId);

    /**
     * 获取未读信息数量
     * @param userId 用户id
     * @return
     */
    Integer countNoReadAcceptMessage(String userId);

    /**
     * 已读信息
     * @param msgId
     * @return
     */
    Integer readAcceptMessage(String msgId);

    /**
     * 获取所有有效社区公告列表
     * @return
     */
    List<CommunityBulletinDto> listAllCommunityBulletin();

    /**
     * 提交投诉建议
     * @param userId
     * @param cpContent
     * @return
     */
    Integer insertComplaintProposal(String userId, String cpContent);

    /**
     * 获取所有关于我们列表
     * @return
     */
    List<AboutUsDto> listAllAboutUs();

    /**
     * 获取首页轮播广告图片
     * @return
     */
    List<AdSliderDto> listAdSliderForApp();

}