package com.cwt.persistent.mapper;

import com.cwt.domain.dto.mySettings.VersionDto;
import com.cwt.domain.entity.Version;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 应用版本 数据层
 *
 * @author ruoyi
 * @date 2018-09-06
 */
@Repository
public interface VersionMapper extends Mapper<Version> {
    /**
     * 查询应用版本信息
     *
     * @param id 应用版本ID
     * @return 应用版本信息
     */
    VersionDto selectVersionById(Integer id);

    /***
     * lzf
     * 查询最新的版本信息
     * @return
     */
    VersionDto selectNewestVersion();
    VersionDto selectNewestVersionV2(String systemType);

    /**
     * 查询应用版本列表
     *
     * @param version 应用版本信息
     * @return 应用版本集合
     */
    List<VersionDto> selectVersionList(VersionDto version);

    /**
     * 新增应用版本
     *
     * @param version 应用版本信息
     * @return 结果
     */
    int insertVersion(VersionDto version);

    /**
     * 修改应用版本
     *
     * @param version 应用版本信息
     * @return 结果
     */
    int updateVersion(VersionDto version);

    /**
     * 删除应用版本
     *
     * @param id 应用版本ID
     * @return 结果
     */
    int deleteVersionById(Integer id);

    /**
     * 批量删除应用版本
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteVersionByIds(String[] ids);

}