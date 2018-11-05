package com.cwt.service.service.impl;

import com.cwt.domain.dto.mySettings.ComplaintProposalDto;
import com.cwt.persistent.mapper.ComplaintProposalMapper;
import com.cwt.service.service.IComplaintProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 投诉建议 服务层实现
 * 
 * @author ruoyi
 * @date 2018-08-27
 */
@Service
public class ComplaintProposalServiceImpl implements IComplaintProposalService
{
	@Autowired
	private ComplaintProposalMapper complaintProposalMapper;

	/**
     * 查询投诉建议信息
     * 
     * @param id 投诉建议ID
     * @return 投诉建议信息
     */
    @Override
	public ComplaintProposalDto selectComplaintProposalById(String id)
	{
	    return complaintProposalMapper.selectComplaintProposalById(id);
	}
	
	/**
     * 查询投诉建议列表
     * 
     * @param complaintProposal 投诉建议信息
     * @return 投诉建议集合
     */
	@Override
	public List<ComplaintProposalDto> selectComplaintProposalList(ComplaintProposalDto complaintProposal)
	{
	    return complaintProposalMapper.selectComplaintProposalList(complaintProposal);
	}
	
    /**
     * 新增投诉建议
     * 
     * @param complaintProposal 投诉建议信息
     * @return 结果
     */
	@Override
	public int insertComplaintProposal(ComplaintProposalDto complaintProposal)
	{
	    return complaintProposalMapper.insertComplaintProposal(complaintProposal);
	}
	
	/**
     * 修改投诉建议
     * 
     * @param complaintProposal 投诉建议信息
     * @return 结果
     */
	@Override
	public int updateComplaintProposal(ComplaintProposalDto complaintProposal)
	{
	    return complaintProposalMapper.updateComplaintProposal(complaintProposal);
	}

	/**
     * 已读投诉建议对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int readComplaintProposalByIds(String ids)
	{
		return complaintProposalMapper.readComplaintProposalByIds(ids.split(","));
	}

	/**
     * 忽略投诉建议对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int ignoreComplaintProposalByIds(String ids)
	{
		return complaintProposalMapper.ignoreComplaintProposalByIds(ids.split(","));
	}

}
