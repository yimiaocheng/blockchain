package com.cwt.service.service;

import com.cwt.domain.dto.mySettings.ComplaintProposalDto;

import java.util.List;

/**
 * 投诉建议 服务层
 * 
 * @author ruoyi
 * @date 2018-08-27
 */
public interface IComplaintProposalService 
{
	/**
     * 查询投诉建议信息
     * 
     * @param id 投诉建议ID
     * @return 投诉建议信息
     */
	public ComplaintProposalDto selectComplaintProposalById(String id);
	
	/**
     * 查询投诉建议列表
     * 
     * @param complaintProposal 投诉建议信息
     * @return 投诉建议集合
     */
	public List<ComplaintProposalDto> selectComplaintProposalList(ComplaintProposalDto complaintProposal);
	
	/**
     * 新增投诉建议
     * 
     * @param complaintProposal 投诉建议信息
     * @return 结果
     */
	public int insertComplaintProposal(ComplaintProposalDto complaintProposal);
	
	/**
     * 修改投诉建议
     * 
     * @param complaintProposal 投诉建议信息
     * @return 结果
     */
	public int updateComplaintProposal(ComplaintProposalDto complaintProposal);
		
	/**
     * 已读投诉建议信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int readComplaintProposalByIds(String ids);

	/**
     * 忽略投诉建议信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int ignoreComplaintProposalByIds(String ids);

}
