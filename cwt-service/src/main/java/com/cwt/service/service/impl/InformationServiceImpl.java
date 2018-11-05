package com.cwt.service.service.impl;

import com.cwt.domain.constant.InformationConstants;
import com.cwt.domain.dto.information.InformationDto;
import com.cwt.domain.dto.information.InformationForBalanceOrderDto;
import com.cwt.domain.dto.information.InformationForCtRulesDTO;
import com.cwt.domain.entity.Information;
import com.cwt.persistent.mapper.InformationMapper;
import com.cwt.service.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/7/21 16:49
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
@Service("informationService")
public class InformationServiceImpl implements InformationService {

    @Autowired
    private InformationMapper informationMapper;

    /***
     * 根据数据名获取数据
     * @param dateName
     * @return
     */
    @Override
//    @Cacheable(value = "INFORMATION_CACHE",key = "'INFORMATION_CACHE_' + #dateName")
    public InformationDto getByDateName(String dateName) {
        return informationMapper.selectByDateName(dateName, 1);
    }

    @Override
    public List<InformationDto> getLikeDateName(String dateName) {
        return informationMapper.selectLikeDateName(dateName);
    }

    /***
     * 查询加权奖的比率和封顶值
     * @return
     */
    @Override
//    @Cacheable(value = "INFORMATION_RATIO_AND_TOP_CACHE",key = "'INFORMATION_RATIO_AND_TOP_CACHE_' + #result.dataName")
    public List<InformationDto> getWeightingRatioAndTop() {
        return informationMapper.selectLikeDateName("weighting");
    }

    /********************************后台管理用********************************/
    /**
     * 查询自定义信息
     *
     * @param id 自定义ID
     * @return 自定义信息
     */
    @Override
    public Information selectInformationById(Integer id) {
        return informationMapper.selectInformationById(id);
    }

    /**
     * 查询自定义列表
     *
     * @param information 自定义信息
     * @return 自定义集合
     */
    @Override
    public List<Information> selectInformationList(Information information) {
        return informationMapper.selectInformationList(information);
    }

    /**
     * 查询ct兑换规则比例
     *
     * @return 自定义集合
     */
    @Override
    public List<InformationForCtRulesDTO> selectCtRulesList() {
        InformationForCtRulesDTO informationForCtRulesDTO = new InformationForCtRulesDTO();

        List<Information> informationList = informationMapper.selectCtRulesList(InformationConstants.DATA_NAME_CT_RULES_GROUPING);
        for(Information information : informationList){
            if(InformationConstants.DATA_NAME_TRANS_RATE.equals(information.getDataName())){
                informationForCtRulesDTO.setTransRate(information.getDataValue());
            }else if(InformationConstants.DATA_NAME_TRANS_BALANCE_RATE.equals(information.getDataName())){
                informationForCtRulesDTO.setTransBalanceRate(information.getDataValue());
            }else if(InformationConstants.DATA_NAME_CT_TO_INTEGRAL_RATE.equals(information.getDataName())){
                informationForCtRulesDTO.setCtToIntegralRate(information.getDataValue());
            }else if(InformationConstants.DATA_NAME_CT_TO_GAME_COIN_RATE.equals(information.getDataName())){
                informationForCtRulesDTO.setCtToGameCoinRate(information.getDataValue());
            }
        }

        List<InformationForCtRulesDTO> informationForCtRulesDTOList = new ArrayList<InformationForCtRulesDTO>();
        informationForCtRulesDTOList.add(informationForCtRulesDTO);

        return informationForCtRulesDTOList;
    }

    /**
     * ct兑换规则比例--修改
     *
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Integer updateCtRules(InformationForCtRulesDTO informationForCtRulesDTO) {
        int countRows = 0;

        countRows += informationMapper.updateCtRulesByDataName(InformationConstants.DATA_NAME_TRANS_RATE, informationForCtRulesDTO.getTransRate());
        countRows += informationMapper.updateCtRulesByDataName(InformationConstants.DATA_NAME_TRANS_BALANCE_RATE, informationForCtRulesDTO.getTransBalanceRate());
        countRows += informationMapper.updateCtRulesByDataName(InformationConstants.DATA_NAME_CT_TO_INTEGRAL_RATE, informationForCtRulesDTO.getCtToIntegralRate());
        countRows += informationMapper.updateCtRulesByDataName(InformationConstants.DATA_NAME_CT_TO_GAME_COIN_RATE, informationForCtRulesDTO.getCtToGameCoinRate());

        return countRows == 4 ? 1 : 0;
    }

    /**
     * 新增自定义
     *
     * @param information 自定义信息
     * @return 结果
     */
    @Override
    public int insertInformation(Information information) {
        return informationMapper.insertInformation(information);
    }

    /**
     * 修改自定义
     *
     * @param information 自定义信息
     * @return 结果
     */
    @Override
    public int updateInformation(Information information) {
        return informationMapper.updateInformation(information);
    }

    @Override
    public InformationForBalanceOrderDto selectBalanceOrderMsg() {
        InformationForBalanceOrderDto dto = new InformationForBalanceOrderDto();

        InformationDto serviceCharge = informationMapper.selectByDateName(InformationConstants.DATA_NAME_SELL_PLATFORM_FEE_RATIO, InformationConstants.STATUS_SHOW);
        InformationDto subsidy = informationMapper.selectByDateName(InformationConstants.DATA_NAME_SELL_SUBSIDY_RATIO, InformationConstants.STATUS_SHOW);
        InformationDto minNum = informationMapper.selectByDateName(InformationConstants.DATA_NAME_BALANCE_ORDER_MIN_NUM, InformationConstants.STATUS_SHOW);
        dto.setServiceCharge(serviceCharge == null ? "0" : serviceCharge.getDataValue());
        dto.setSubsidy(subsidy == null ? "0" : subsidy.getDataValue());
        dto.setOrderMinNum(minNum == null ? "0" : minNum.getDataValue());
        return dto;
    }

    /**
     * 删除自定义对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
//    @Override
//    public int deleteInformationByIds(String ids)
//    {
//        return informationMapper.deleteInformationByIds(Convert.toStrArray(ids));
//    }

}
