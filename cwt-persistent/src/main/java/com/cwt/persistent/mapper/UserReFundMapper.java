package com.cwt.persistent.mapper;

import com.cwt.domain.dto.store.UserRefundDTO;
import com.cwt.domain.dto.store.UserRefundListPO;
import com.cwt.domain.entity.UserReFund;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface UserReFundMapper extends Mapper<UserReFund>{

    UserReFund selectUserReFundInfo(@Param("orderId") String orderId, @Param("fromId")String fromId, @Param("toId")String toId);

    List<UserRefundListPO> selectUserRefundList(UserRefundDTO userRefundDTO);
}
