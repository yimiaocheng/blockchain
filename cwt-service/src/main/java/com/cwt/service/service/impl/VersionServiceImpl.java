package com.cwt.service.service.impl;

import com.cwt.domain.constant.VersionConstants;
import com.cwt.domain.dto.mySettings.VersionDto;
import com.cwt.domain.entity.Version;
import com.cwt.persistent.mapper.VersionMapper;
import com.cwt.service.service.IVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用版本 服务层实现
 *
 * @author ruoyi
 * @date 2018-09-06
 */
@Service("iVersionService")
public class VersionServiceImpl implements IVersionService {
    @Autowired
    private VersionMapper versionMapper;

    /**
     * 查询应用版本信息
     *
     * @param id 应用版本ID
     * @return 应用版本信息
     */
    @Override
    public VersionDto selectVersionById(Integer id) {
        return versionMapper.selectVersionById(id);
    }

    /***
     * lzf
     * 查询最新的版本信息
     * @return
     */
    @Override
    public VersionDto selectNewestVersion() {
        return versionMapper.selectNewestVersion();
    }

    @Override
    public VersionDto selectNewestVersionV2(String systemType) {
        if (VersionConstants.SYSTEMTYPE_IOS.equalsIgnoreCase(systemType)) {
            return versionMapper.selectNewestVersionV2(VersionConstants.SYSTEMTYPE_IOS);
        } else {
            return versionMapper.selectNewestVersionV2(VersionConstants.SYSTEMTYPE_ANDROID);
        }
    }

    @Override
    public Map<String, VersionDto> selectNewestVersionAll() {
        Map<String, VersionDto> VersionMap = new HashMap();
        VersionDto versionIos = versionMapper.selectNewestVersionV2(VersionConstants.SYSTEMTYPE_IOS);
        VersionDto versionAndroid = versionMapper.selectNewestVersionV2(VersionConstants.SYSTEMTYPE_ANDROID);
        VersionMap.put(VersionConstants.SYSTEMTYPE_IOS,versionIos);
        VersionMap.put(VersionConstants.SYSTEMTYPE_ANDROID,versionAndroid);
        return VersionMap;
    }

    /**
     * 查询应用版本列表
     *
     * @param version 应用版本信息
     * @return 应用版本集合
     */
    @Override
    public List<VersionDto> selectVersionList(VersionDto version) {
        return versionMapper.selectVersionList(version);
    }

    /**
     * 新增应用版本
     *
     * @param version 应用版本信息
     * @return 结果
     */
    @Override
    public int insertVersion(VersionDto version) {
        return versionMapper.insertVersion(version);
    }

    /**
     * 修改应用版本
     *
     * @param version 应用版本信息
     * @return 结果
     */
    @Override
    public int updateVersion(VersionDto version) {
        return versionMapper.updateVersion(version);
    }

    /**
     * 删除应用版本对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteVersionByIds(String ids) {
        return versionMapper.deleteVersionByIds(ids.split(","));
    }

}
