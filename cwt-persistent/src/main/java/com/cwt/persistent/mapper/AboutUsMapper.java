package com.cwt.persistent.mapper;

import com.cwt.domain.dto.mySettings.AboutUsDto;
import com.cwt.domain.entity.AboutUs;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * AboutUsMapper 数据访问类
 * @date 2018-08-27 14:24:47
 * @version 1.0
 */
@Repository
public interface AboutUsMapper extends Mapper<AboutUs> {

    /**
     * 获取所有关于我们
     * @return
     */
    List<AboutUsDto> listAllAboutUs();

    /******************************************************后台管理用******************************************************/
    /**
     * 查询关于我们信息
     *
     * @param id 关于我们ID
     * @return 关于我们信息
     */
    public AboutUsDto selectAboutUsById(String id);

    /**
     * 查询关于我们列表
     *
     * @param aboutUs 关于我们信息
     * @return 关于我们集合
     */
    public List<AboutUsDto> selectAboutUsList(AboutUsDto aboutUs);

    /**
     * 新增关于我们
     *
     * @param aboutUs 关于我们信息
     * @return 结果
     */
    public int insertAboutUs(AboutUsDto aboutUs);

    /**
     * 修改关于我们
     *
     * @param aboutUs 关于我们信息
     * @return 结果
     */
    public int updateAboutUs(AboutUsDto aboutUs);

    /**
     * 删除关于我们
     *
     * @param id 关于我们ID
     * @return 结果
     */
    public int deleteAboutUsById(String id);

    /**
     * 批量删除关于我们
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAboutUsByIds(String[] ids);
}