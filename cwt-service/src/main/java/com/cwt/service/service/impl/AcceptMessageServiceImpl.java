package com.cwt.service.service.impl;

import com.cwt.domain.dto.mySettings.AcceptMessageDto;
import com.cwt.persistent.mapper.AcceptMessageMapper;
import com.cwt.service.service.IAcceptMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 个人接受消息 服务层实现
 * 
 * @author ruoyi
 * @date 2018-08-27
 */
@Service
public class AcceptMessageServiceImpl implements IAcceptMessageService
{
	@Autowired
	private AcceptMessageMapper acceptMessageMapper;

	/**
     * 查询个人接受消息信息
     * 
     * @param id 个人接受消息ID
     * @return 个人接受消息信息
     */
    @Override
	public AcceptMessageDto selectAcceptMessageById(String id)
	{
	    return acceptMessageMapper.selectAcceptMessageById(id);
	}
	
	/**
     * 查询个人接受消息列表
     * 
     * @param acceptMessage 个人接受消息信息
     * @return 个人接受消息集合
     */
	@Override
	public List<AcceptMessageDto> selectAcceptMessageList(AcceptMessageDto acceptMessage)
	{
	    return acceptMessageMapper.selectAcceptMessageList(acceptMessage);
	}
	
    /**
     * 新增个人接受消息
     * 
     * @param acceptMessage 个人接受消息信息
     * @return 结果
     */
	@Override
	public int insertAcceptMessage(AcceptMessageDto acceptMessage)
	{
	    return acceptMessageMapper.insertAcceptMessage(acceptMessage);
	}
	
	/**
     * 修改个人接受消息
     * 
     * @param acceptMessage 个人接受消息信息
     * @return 结果
     */
	@Override
	public int updateAcceptMessage(AcceptMessageDto acceptMessage)
	{
	    return acceptMessageMapper.updateAcceptMessage(acceptMessage);
	}

	/**
     * 删除个人接受消息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteAcceptMessageByIds(String ids)
	{
		return acceptMessageMapper.deleteAcceptMessageByIds(ids.split(","));
	}
	
}
