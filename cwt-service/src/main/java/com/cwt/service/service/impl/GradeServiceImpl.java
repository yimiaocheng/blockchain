package com.cwt.service.service.impl;

import com.cwt.domain.dto.grade.GradeDto;
import com.cwt.domain.entity.Grade;
import com.cwt.domain.dto.wallet.WalletDto;
import com.cwt.persistent.mapper.GradeMapper;
import com.cwt.service.service.GradeService;
import com.cwt.service.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("gradeService")
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private WalletService walletService;

    /***
     * 根据用户Id,钱包信息获取等级信息
     * @param userId
     * @param walletDto
     * @return GradeDto
     */
    @Override
    public GradeDto getByUserId(String userId, WalletDto walletDto) {
        BigDecimal force = walletDto.getCalculationForce();
        return gradeMapper.selectByUserId(userId,force);
    }

    /**
     * 根据用户Id获取等级信息
     * @param userId
     * @return
     */
    @Override
//    @Cacheable(value = "GRADE_USERID_CACHE",key = "'GRADE_USERID_CACHE_' + #userId")
    public GradeDto getByUserId(String userId) {
        WalletDto walletDto = walletService.getByUserId(userId);
        return gradeMapper.selectByUserId(userId,walletDto.getCalculationForce());
    }


/**
 * ********************************后台管理用********************************
 */
    /**
     * 查询用户等级信息
     *
     * @param id 用户等级ID
     * @return 用户等级信息
     */
    @Override
    public Grade selectGradeById(Integer id) {
        return gradeMapper.selectGradeById(id);
    }

    /**
     * 查询用户等级列表
     *
     * @param grade 用户等级信息
     * @return 用户等级集合
     */
    @Override
    public List<Grade> selectGradeList(Grade grade) {
        return gradeMapper.selectGradeList(grade);
    }

    /**
     * 新增用户等级
     *
     * @param grade 用户等级信息
     * @return 结果
     */
    @Override
    public int insertGrade(Grade grade) {
        return gradeMapper.insertGrade(grade);
    }

    /**
     * 修改用户等级
     *
     * @param grade 用户等级信息
     * @return 结果
     */
    @Override
    public int updateGrade(Grade grade) {
        return gradeMapper.updateGrade(grade);
    }

    /**
     * 删除用户等级对象
     *
     * @param id 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteGradeById(String id) {
        return gradeMapper.deleteGradeById(id);
    }

}
