package com.cwt.persistent.mapper;

import com.cwt.domain.dto.mySettings.CommunityBulletinDto;
import com.cwt.domain.entity.CommunityBulletin;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * CommunityBulletinMapper 数据访问类
 * @date 2018-08-27 14:24:47
 * @version 1.0
 */
@Repository
public interface CommunityBulletinMapper extends Mapper<CommunityBulletin> {
    /**
     * 获取所有有效社区公告列表
     * @return
     */
    List<CommunityBulletinDto> listAllCommunityBulletin();

    /******************************************************后台管理用******************************************************/
    /**
     * 查询社区公告信息
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
     * 删除社区公告
     *
     * @param id 社区公告ID
     * @return 结果
     */
    public int deleteCommunityBulletinById(String id);

    /**
     * 批量删除社区公告
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCommunityBulletinByIds(String[] ids);
}