package com.cwt.service.service.impl;

import com.cwt.domain.dto.integral.IntegralDTO;
import com.cwt.persistent.mapper.IntegralRecordMapper;
import com.cwt.service.service.IntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("integralService")
public class IntegralServiceImpl implements IntegralService {
    @Autowired
    private IntegralRecordMapper integralRecordMapper;

    @Override
    public List<IntegralDTO> selectIntegralList(IntegralDTO integralDTO) {

        return integralRecordMapper.selectIntegralList(integralDTO);
    }
}
