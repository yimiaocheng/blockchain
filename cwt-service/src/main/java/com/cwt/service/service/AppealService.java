package com.cwt.service.service;

import com.cwt.domain.dto.appeal.AppealListConditionDto;
import com.cwt.domain.dto.appeal.AppealListDto;

import java.util.List;

public interface AppealService {
    /**
     * 后台查询申诉列表
     * @param condition 筛选条件
     */
    List<AppealListDto> listAppealByCondition(AppealListConditionDto condition);
}
