package com.cwt.service.service;

import com.cwt.domain.dto.mySettings.CommunityBulletinDto;

import java.util.List;

/**
 * 社区公告 服务层
 * 
 * @author ruoyi
 * @date 2018-08-27
 */
public interface ICommunityBulletinService 
{
	/**
     * 查询社区公告信息
     * 
     * @param id 社区公告ID
     * @return 社区公告信息
     */
	public CommunityBulletinDto selectCommunityBulletinById(String id);
	
	/**
     * 查询社区公告列表
     * 
     * @param communityBulletin 社区公告信息
     * @return 社区公告集合
     */
	public List<CommunityBulletinDto> selectCommunityBulletinList(CommunityBulletinDto communityBulletin);
	
	/**
     * 新增社区公告
     * 
     * @param communityBulletin 社区公告信息
     * @return 结果
     */
	public int insertCommunityBulletin(CommunityBulletinDto communityBulletin);
	
	/**
     * 修改社区公告
     * 
     * @param communityBulletin 社区公告信息
     * @return 结果
     */
	public int updateCommunityBulletin(CommunityBulletinDto communityBulletin);
		
	/**
     * 删除社区公告信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteCommunityBulletinByIds(String ids);
	
}
