package com.cwt.service.service.impl;

import com.cwt.common.util.BeanUtils;
import com.cwt.domain.dto.mySettings.AgreementDto;
import com.cwt.domain.entity.Agreement;
import com.cwt.persistent.mapper.AgreementMapper;
import com.cwt.service.service.AgreementService;
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
public class AgreementServiceImpl implements AgreementService {
    @Autowired
    AgreementMapper agreementMapper;

    @Override
    public List<AgreementDto> selectAgreement() {
        return agreementMapper.stlectAgreement();
    }

    @Override
    public Integer updateAgreement(AgreementDto agreementDto) {
        Agreement agreement = new Agreement();
        BeanUtils.copySamePropertyValue(agreementDto, agreement);
        return agreementMapper.updateByPrimaryKeySelective(agreement);
    }
}
