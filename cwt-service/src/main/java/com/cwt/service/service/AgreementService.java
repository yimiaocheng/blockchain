package com.cwt.service.service;

import com.cwt.domain.dto.mySettings.AgreementDto;

import java.util.List;

/**
 * 关于我们 服务层
 * 
 * @author ruoyi
 * @date 2018-08-27
 */
public interface AgreementService {

	List<AgreementDto> selectAgreement();
	
	Integer updateAgreement(AgreementDto aboutUs);
		
}
