package com.cwt.service.service.impl;

import com.cwt.domain.dto.mySettings.AboutUsDto;
import com.cwt.domain.entity.AboutUs;
import com.cwt.persistent.mapper.AboutUsMapper;
import com.cwt.service.service.IAboutUsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 关于我们 服务层实现
 *
 * @author ruoyi
 * @date 2018-08-27
 */
@Service
public class AboutUsServiceImpl implements IAboutUsService {
    @Autowired
    private AboutUsMapper aboutUsMapper;

    /**
     * 查询关于我们信息
     *
     * @param id 关于我们ID
     * @return 关于我们信息
     */
    @Override
    public AboutUsDto selectAboutUsById(String id) {
        return aboutUsMapper.selectAboutUsById(id);
    }

    /**
     * 查询关于我们列表
     *
     * @param aboutUs 关于我们信息
     * @return 关于我们集合
     */
    @Override
    public List<AboutUsDto> selectAboutUsList(AboutUsDto aboutUs) {
        return aboutUsMapper.selectAboutUsList(aboutUs);
    }

    /**
     * 新增关于我们
     *
     * @param aboutUs 关于我们信息
     * @return 结果
     */
    @Override
    public int insertAboutUs(AboutUsDto aboutUs) {
        return aboutUsMapper.insertAboutUs(aboutUs);
    }

    /**
     * 修改关于我们
     *
     * @param aboutUs 关于我们信息
     * @return 结果
     */
    @Override
    public int updateAboutUs(AboutUsDto aboutUs) {
        return aboutUsMapper.updateAboutUs(aboutUs);
    }

    /**
     * 删除关于我们对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAboutUsByIds(String ids) {
        return aboutUsMapper.deleteAboutUsByIds(ids.split(","));
    }

}
