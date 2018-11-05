package com.cwt.service.service;


import com.cwt.domain.dto.information.InformationDto;
import com.cwt.domain.dto.information.InformationForBalanceOrderDto;
import com.cwt.domain.dto.information.InformationForCtRulesDTO;
import com.cwt.domain.dto.integral.IntegralDTO;
import com.cwt.domain.entity.Information;

import java.util.List;

public interface IntegralService {



    /********************************后台管理用********************************/

    /**
     * 查询商城积分变动记录
     *
     * @param integralDTO 查询条件
     * @return 商城积分集合
     */
    List<IntegralDTO> selectIntegralList(IntegralDTO integralDTO);


}
