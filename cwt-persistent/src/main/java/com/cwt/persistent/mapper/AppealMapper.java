package com.cwt.persistent.mapper;

import com.cwt.domain.dto.appeal.AppealListConditionDto;
import com.cwt.domain.dto.appeal.AppealListDto;
import com.cwt.domain.entity.Appeal;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * AppealMapper 数据访问类
 * @date 2018-08-30 10:44:52
 * @version 1.0
 */
@Repository
public interface AppealMapper extends Mapper<Appeal> {

    /**
     * 后台查询申诉列表
     * @param condition 筛选条件
     */
    List<AppealListDto> listAppealByCondition(AppealListConditionDto condition);
}