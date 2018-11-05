package com.cwt.service.service.impl;

import com.cwt.domain.dto.mySettings.AboutUsDto;
import com.cwt.domain.dto.mySettings.AcceptMessageDto;
import com.cwt.domain.dto.mySettings.AdSliderDto;
import com.cwt.domain.dto.mySettings.CommunityBulletinDto;
import com.cwt.domain.entity.AcceptMessage;
import com.cwt.domain.entity.ComplaintProposal;
import com.cwt.persistent.mapper.*;
import com.cwt.service.service.MySettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("mySettingsService")
public class MySettingsServiceImpl implements MySettingsService {
    @Autowired
    AboutUsMapper aboutUsMapper;
    @Autowired
    AcceptMessageMapper acceptMessageMapper;
    @Autowired
    CommunityBulletinMapper communityBulletinMapper;
    @Autowired
    ComplaintProposalMapper complaintProposalMapper;
    @Autowired
    AdSliderMapper adSliderMapper;

    @Override
    public List<AcceptMessageDto> listAcceptMessage(String userId) {
        List<AcceptMessageDto> acceptMessageDtoList = acceptMessageMapper.selectByUserId(userId);

        return acceptMessageDtoList;
    }

    @Override
    public Integer countNoReadAcceptMessage(String userId) {
        return acceptMessageMapper.countNoReadAcceptMessage(userId);
    }

    @Override
    public Integer readAcceptMessage(String msgId) {
        AcceptMessage acceptMessage = new AcceptMessage();
        acceptMessage.setId(msgId);
        acceptMessage.setStatus("1");
        return acceptMessageMapper.updateByPrimaryKeySelective(acceptMessage);
    }

    @Override
    public List<AboutUsDto> listAllAboutUs() {
//        List<AboutUs> aboutUsList = aboutUsMapper.selectAll();
        List<AboutUsDto> aboutUsDtoList = aboutUsMapper.listAllAboutUs();
        return aboutUsDtoList;
    }

    @Override
    public List<CommunityBulletinDto> listAllCommunityBulletin() {

        List<CommunityBulletinDto> communityBulletinDtoList = communityBulletinMapper.listAllCommunityBulletin();

        return communityBulletinDtoList;
    }

    @Override
    public Integer insertComplaintProposal(String userId, String cpContent) {

        ComplaintProposal complaintProposal = new ComplaintProposal();
        complaintProposal.setId(UUID.randomUUID().toString());
        complaintProposal.setUserId(userId);
        complaintProposal.setContent(cpContent);
        complaintProposal.setStatus("0");
        complaintProposal.setCreateTime(new Date());

        Integer count = complaintProposalMapper.insertSelective(complaintProposal);

        return count;
    }

    @Override
    public List<AdSliderDto> listAdSliderForApp(){
        List<AdSliderDto> adSliderDtoList = adSliderMapper.listAdSliderForApp();
        return adSliderDtoList;
    }

}
