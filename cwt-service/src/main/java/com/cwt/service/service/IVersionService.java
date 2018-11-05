package com.cwt.service.service;

import com.cwt.domain.dto.mySettings.VersionDto;

import java.util.List;
import java.util.Map;

/**
 * 应用版本 服务层
 *
 * @author ruoyi
 * @date 2018-09-06
 */
public interface IVersionService {
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
    /***
     * yh
     * 根据系统类型查询最新的版本信息
     * @param systemType
     * @return
     */
    VersionDto selectNewestVersionV2(String systemType);
    /***
     * yh
     * 查询所有系统类型最新的版本信息
     * @return
     */
    Map<String,VersionDto> selectNewestVersionAll();

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
     * 删除应用版本信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteVersionByIds(String ids);

}
