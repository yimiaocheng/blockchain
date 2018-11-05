package com.cwt.service.service.impl;

import com.cwt.domain.dto.appeal.AppealListConditionDto;
import com.cwt.domain.dto.appeal.AppealListDto;
import com.cwt.persistent.mapper.AppealMapper;
import com.cwt.service.service.AppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppealServiceImpl implements AppealService {
    @Autowired
    private AppealMapper appealMapper;
    @Override
    public List<AppealListDto> listAppealByCondition(AppealListConditionDto condition) {
        return appealMapper.listAppealByCondition(condition);
    }
}
