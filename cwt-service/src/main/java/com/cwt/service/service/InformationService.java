package com.cwt.service.service;


import com.cwt.domain.dto.information.InformationDto;
import com.cwt.domain.dto.information.InformationForBalanceOrderDto;
import com.cwt.domain.dto.information.InformationForCtRulesDTO;
import com.cwt.domain.entity.Information;

import java.util.List;

public interface InformationService {
    /***
     * 根据数据名获取数据
     * @param dateName
     * @return
     */
    InformationDto getByDateName(String dateName);

    /***
     * 模糊查询配置信息
     * @param dateName
     * @return
     */
    List<InformationDto> getLikeDateName(String dateName);

    /***
     * 查询加权奖的比率和封顶值
     * @return
     */
    List<InformationDto> getWeightingRatioAndTop();


    /********************************后台管理用********************************/
    /**
     * 查询自定义信息
     *
     * @param id 自定义ID
     * @return 自定义信息
     */
    Information selectInformationById(Integer id);

    /**
     * 查询自定义列表
     *
     * @param information 自定义信息
     * @return 自定义集合
     */
    List<Information> selectInformationList(Information information);

    /**
     * 查询ct兑换规则比例
     *
     * @return 自定义集合
     */
    List<InformationForCtRulesDTO> selectCtRulesList();

    /**
     * ct兑换规则比例--修改
     *
     * @return
     */
    Integer updateCtRules(InformationForCtRulesDTO informationForCtRulesDTO);

    /**
     * 新增自定义
     *
     * @param information 自定义信息
     * @return 结果
     */
    int insertInformation(Information information);

    /**
     * 修改自定义
     *
     * @param information 自定义信息
     * @return 结果
     */
    int updateInformation(Information information);

    /**
     * 查询余额交易相关的平台手续费和买家补贴信息
     */
    InformationForBalanceOrderDto selectBalanceOrderMsg();

    /**
     * 删除自定义信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
//    public int deleteInformationByIds(String ids);

}
