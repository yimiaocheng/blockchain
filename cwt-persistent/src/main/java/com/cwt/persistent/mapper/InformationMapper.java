package com.cwt.persistent.mapper;

import com.cwt.domain.dto.information.InformationDto;
import com.cwt.domain.dto.information.InformationForCtRulesDTO;
import com.cwt.domain.entity.Information;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * InformationMapper 数据访问类
 * @date 2018-08-15 14:13:46
 * @version 1.0
 */
@Repository
public interface InformationMapper extends Mapper<Information> {
    /***
     * 根据数据名获取数据
     * @param dateName
     * @param status status如果为null，则不限制状态
     * @return
     */
    InformationDto selectByDateName(@Param("dateName") String dateName,@Param("status") Integer status);

    /***
     * 模糊查询配置信息
     * @return
     */
    List<InformationDto> selectLikeDateName(@Param("dateName") String dateName);


    /******************************************************后台管理用******************************************************/
    /**
     * 查询自定义信息
     *
     * @param id 自定义ID
     * @return 自定义信息
     */
    public Information selectInformationById(Integer id);

    /**
     * 查询自定义列表 不含分组的信息
     *
     * @param information 自定义信息
     * @return 自定义集合
     */
    public List<Information> selectInformationList(Information information);

    /**
     * 查询自定义列表
     *
     * @param grouping 分组名称
     * @return 自定义集合
     */
    List<Information> selectCtRulesList(@Param("grouping") String grouping);

    /**
     * ct兑换规则比例--修改
     *
     * @return
     */
    Integer updateCtRulesByDataName(@Param("dataName") String dataName, @Param("dataValue") String dataValue);

    /**
     * 新增自定义
     *
     * @param information 自定义信息
     * @return 结果
     */
    public int insertInformation(Information information);

    /**
     * 修改自定义
     *
     * @param information 自定义信息
     * @return 结果
     */
    public int updateInformation(Information information);

    /**
     * 删除自定义
     *
     * @param id 自定义ID
     * @return 结果
     */
    public int deleteInformationById(Integer id);

    /**
     * 批量删除自定义
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteInformationByIds(String[] ids);
}