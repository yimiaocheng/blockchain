package com.cwt.persistent.mapper;

import com.cwt.domain.dto.mySettings.AcceptMessageDto;
import com.cwt.domain.entity.AcceptMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * AcceptMessageMapper 数据访问类
 * @date 2018-08-27 14:24:47
 * @version 1.0
 */
@Repository
public interface AcceptMessageMapper extends Mapper<AcceptMessage> {

    /**
     * 通过用户id 获取接收消息
     * @param userId
     * @return
     */
    List<AcceptMessageDto> selectByUserId(@Param("userId") String userId);

    /**
     * 获取未读信息数量
     * @param userId 用户id
     * @return
     */
    Integer countNoReadAcceptMessage(@Param("userId") String userId);

    /**
     * 用户已读信息
     * @param userId 用户id
     * @return
     */
    Integer readAcceptMessage(@Param("userId") String userId);

    /******************************************************后台管理用******************************************************/
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
     * 删除个人接受消息
     *
     * @param id 个人接受消息ID
     * @return 结果
     */
    public int deleteAcceptMessageById(String id);

    /**
     * 批量删除个人接受消息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAcceptMessageByIds(String[] ids);
}