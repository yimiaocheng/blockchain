package com.cwt.persistent.mapper;

import com.cwt.domain.dto.store.UserAwardDTO;
import com.cwt.domain.dto.store.UserAwardListPO;
import com.cwt.domain.entity.UserAward;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

@Repository
public interface UserAwardMapper extends Mapper<UserAward>{

    int updateUserAwardStatus(@Param("userId") String userId,@Param("award_orderNo") String award_orderNo,@Param("awardType")  String awardType,@Param("status") String status,@Param("modifyTime")Date modifyTime);

    List<UserAward> selectUserAwardByStatus(@Param("status") String status,@Param("awardType_weighted")  String awardType_weighted,@Param("awardType_sales")  String awardType_sales);

    int updateUserAwardStatusById(@Param("id") String id, @Param("status") String status, @Param("modifyTime")Date modifyTime);

    List<UserAward> selectUserAward(@Param("status") String status, @Param("awardType") String awardType);

    List<UserAwardListPO> selectUserAwardList(UserAwardDTO userAwardDTO);
}
