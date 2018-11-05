package com.cwt.persistent.mapper;

import com.cwt.domain.dto.mySettings.ComplaintProposalDto;
import com.cwt.domain.entity.ComplaintProposal;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * ComplaintProposalMapper 数据访问类
 * @date 2018-08-27 14:24:47
 * @version 1.0
 */
@Repository
public interface ComplaintProposalMapper extends Mapper<ComplaintProposal> {

    /******************************************************后台管理用******************************************************/
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
     * 删除投诉建议
     *
     * @param id 投诉建议ID
     * @return 结果
     */
    public int deleteComplaintProposalById(String id);

    /**
     * 批量已读投诉建议
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int readComplaintProposalByIds(String[] ids);

    /**
     * 批量忽略投诉建议
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int ignoreComplaintProposalByIds(String[] ids);
}