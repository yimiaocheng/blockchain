package com.cwt.persistent.mapper;

import com.cwt.domain.dto.store.UserPayInfoDTO;
import com.cwt.domain.dto.store.UserPayInfoPO;
import com.cwt.domain.entity.UserPay;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

@Repository
public interface UserPayMapper extends Mapper<UserPay>{

    UserPay findUserPayInfo(@Param("id") String id);

    int updateUserPayStatus(@Param("id")String id ,@Param("status") String status,@Param("modifyTime")Date modifyTime);

    UserPay findPayResultByOrderId(@Param("orderNo") String orderNo);

    List<UserPayInfoPO> selectUserPayInfoList(UserPayInfoDTO userPayInfoDTO);
}
