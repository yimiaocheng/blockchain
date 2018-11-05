package com.cwt.persistent.mapper;

import com.cwt.domain.dto.integral.IntegralDTO;
import com.cwt.domain.entity.IntegralRecord;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * IntegralRecordMapper 数据访问类
 * @date 2018-10-16 09:44:49
 * @version 1.0
 */
@Repository
public interface IntegralRecordMapper extends Mapper<IntegralRecord> {
    /**
     * 查询商城积分变动记录
     *
     * @param integralDTO 查询条件
     * @return 商城积分集合
     */
    List<IntegralDTO> selectIntegralList(IntegralDTO integralDTO);
}