package com.cwt.persistent.mapper;

import com.cwt.domain.dto.store.UserReceiveDTO;
import com.cwt.domain.dto.store.UserReceiveListPO;
import com.cwt.domain.dto.store.UserRefundDTO;
import com.cwt.domain.dto.store.UserRefundListPO;
import com.cwt.domain.entity.UserReceive;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@Repository
public interface UserReceiveMapper extends Mapper<UserReceive>{

    UserReceive selectUserReceiveInfo(@Param("orderId") String orderId, @Param("userId")String userId);

    List<UserReceiveListPO> selectUserReceiveList(UserReceiveDTO userReceiveDTO);

}
