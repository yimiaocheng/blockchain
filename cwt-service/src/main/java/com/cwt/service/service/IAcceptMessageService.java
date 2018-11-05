package com.cwt.service.service;

import com.cwt.domain.dto.mySettings.AcceptMessageDto;

import java.util.List;

/**
 * 个人接受消息 服务层
 * 
 * @author ruoyi
 * @date 2018-08-27
 */
public interface IAcceptMessageService 
{
	/**
     * 查询个人接受消息信息
     * 
     * @param id 个人接受消息ID
     * @return 个人接受消息信息
     */
	public AcceptMessageDto selectAcceptMessageById(String id);
	
	/**
     * 查询个人接受消息列表
     * 
     * @param acceptMessage 个人接受消息信息
     * @return 个人接受消息集合
     */
	public List<AcceptMessageDto> selectAcceptMessageList(AcceptMessageDto acceptMessage);
	
	/**
     * 新增个人接受消息
     * 
     * @param acceptMessage 个人接受消息信息
     * @return 结果
     */
	public int insertAcceptMessage(AcceptMessageDto acceptMessage);
	
	/**
     * 修改个人接受消息
     * 
     * @param acceptMessage 个人接受消息信息
     * @return 结果
     */
	public int updateAcceptMessage(AcceptMessageDto acceptMessage);
		
	/**
     * 删除个人接受消息信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteAcceptMessageByIds(String ids);
	
}
