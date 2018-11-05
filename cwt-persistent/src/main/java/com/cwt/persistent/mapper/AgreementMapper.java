package com.cwt.persistent.mapper;

import com.cwt.domain.dto.mySettings.AgreementDto;
import com.cwt.domain.entity.Agreement;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * AboutUsMapper 数据访问类
 * @date 2018-08-27 14:24:47
 * @version 1.0
 */
@Repository
public interface AgreementMapper extends Mapper<Agreement> {

    List<AgreementDto> stlectAgreement();
}