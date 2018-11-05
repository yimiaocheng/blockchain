package com.cwt.service.service.impl;

import com.cwt.domain.dto.mySettings.CommunityBulletinDto;
import com.cwt.persistent.mapper.CommunityBulletinMapper;
import com.cwt.service.service.ICommunityBulletinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 社区公告 服务层实现
 * 
 * @author ruoyi
 * @date 2018-08-27
 */
@Service
public class CommunityBulletinServiceImpl implements ICommunityBulletinService
{
	@Autowired
	private CommunityBulletinMapper communityBulletinMapper;

	/**
     * 查询社区公告信息
     * 
     * @param id 社区公告ID
     * @return 社区公告信息
     */
    @Override
	public CommunityBulletinDto selectCommunityBulletinById(String id)
	{
	    return communityBulletinMapper.selectCommunityBulletinById(id);
	}
	
	/**
     * 查询社区公告列表
     * 
     * @param communityBulletin 社区公告信息
     * @return 社区公告集合
     */
	@Override
	public List<CommunityBulletinDto> selectCommunityBulletinList(CommunityBulletinDto communityBulletin)
	{
	    return communityBulletinMapper.selectCommunityBulletinList(communityBulletin);
	}
	
    /**
     * 新增社区公告
     * 
     * @param communityBulletin 社区公告信息
     * @return 结果
     */
	@Override
	public int insertCommunityBulletin(CommunityBulletinDto communityBulletin)
	{
	    return communityBulletinMapper.insertCommunityBulletin(communityBulletin);
	}
	
	/**
     * 修改社区公告
     * 
     * @param communityBulletin 社区公告信息
     * @return 结果
     */
	@Override
	public int updateCommunityBulletin(CommunityBulletinDto communityBulletin)
	{

	    return communityBulletinMapper.updateCommunityBulletin(communityBulletin);
	}

	/**
     * 删除社区公告对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteCommunityBulletinByIds(String ids)
	{
		return communityBulletinMapper.deleteCommunityBulletinByIds(ids.split(","));
	}
	
}
