package com.cwt.service.service;


import com.cwt.domain.dto.grade.GradeDto;
import com.cwt.domain.dto.wallet.WalletDto;
import com.cwt.domain.entity.Grade;

import java.util.List;

public interface GradeService {

    /***
     * 根据用户Id,钱包信息获取等级信息
     * @param userId
     * @param walletDto
     * @return GradeDto
     */
    GradeDto getByUserId(String userId, WalletDto walletDto);
    /***
     * 根据用户Id获取等级信息
     * @param userId
     * @return GradeDto
     */
    GradeDto getByUserId(String userId);

    /**
     * ********************************后台管理用********************************
     */
    /**
     * 查询用户等级信息
     *
     * @param id 用户等级ID
     * @return 用户等级信息
     */
    Grade selectGradeById(Integer id);

    /**
     * 查询用户等级列表
     *
     * @param grade 用户等级信息
     * @return 用户等级集合
     */
    List<Grade> selectGradeList(Grade grade);

    /**
     * 新增用户等级
     *
     * @param grade 用户等级信息
     * @return 结果
     */
    int insertGrade(Grade grade);

    /**
     * 修改用户等级
     *
     * @param grade 用户等级信息
     * @return 结果
     */
    int updateGrade(Grade grade);

    /**
     * 删除用户等级信息
     *
     * @param id 需要删除的数据ID
     * @return 结果
     */
    int deleteGradeById(String id);

}
