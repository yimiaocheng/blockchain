package com.cwt.service.service;

import com.cwt.domain.dto.mySettings.AboutUsDto;
import com.cwt.domain.entity.AboutUs;

import java.util.List;

/**
 * 关于我们 服务层
 * 
 * @author ruoyi
 * @date 2018-08-27
 */
public interface IAboutUsService 
{
	/**
     * 查询关于我们信息
     * 
     * @param id 关于我们ID
     * @return 关于我们信息
     */
	public AboutUsDto selectAboutUsById(String id);
	
	/**
     * 查询关于我们列表
     * 
     * @param aboutUs 关于我们信息
     * @return 关于我们集合
     */
	public List<AboutUsDto> selectAboutUsList(AboutUsDto aboutUs);
	
	/**
     * 新增关于我们
     * 
     * @param aboutUs 关于我们信息
     * @return 结果
     */
	public int insertAboutUs(AboutUsDto aboutUs);
	
	/**
     * 修改关于我们
     * 
     * @param aboutUs 关于我们信息
     * @return 结果
     */
	public int updateAboutUs(AboutUsDto aboutUs);
		
	/**
     * 删除关于我们信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteAboutUsByIds(String ids);
	
}
