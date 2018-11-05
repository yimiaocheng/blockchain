package com.cwt.persistent.mapper;

import com.cwt.domain.dto.grade.GradeDto;
import com.cwt.domain.entity.Grade;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * GradeMapper 数据访问类
 * @date 2018-08-10 13:40:28
 * @version 1.0
 */
@Repository
public interface GradeMapper extends Mapper<Grade> {
    /***
     * 查询所有矿工等级信息
     * @return
     */
    List<GradeDto> selectAllGrade();

    /***
     * 根据用户id、算力查询等级信息
     * @param userId
     * @param force
     * @return
     */
    GradeDto selectByUserId(@Param("userId") String userId, @Param("force") BigDecimal force);

    /**
     * ********************************后台管理用********************************
     */
    /**
     * 查询用户等级信息
     *
     * @param id 用户等级ID
     * @return 用户等级信息
     */
    public Grade selectGradeById(Integer id);

    /**
     * 查询用户等级列表
     *
     * @param grade 用户等级信息
     * @return 用户等级集合
     */
    public List<Grade> selectGradeList(Grade grade);

    /**
     * 新增用户等级
     *
     * @param grade 用户等级信息
     * @return 结果
     */
    public int insertGrade(Grade grade);

    /**
     * 修改用户等级
     *
     * @param grade 用户等级信息
     * @return 结果
     */
    public int updateGrade(Grade grade);

    /**
     * 删除用户等级
     *
     * @param id 用户等级ID
     * @return 结果
     */
    public int deleteGradeById(Integer id);

    /**
     * 批量删除用户等级
     *
     * @param id 需要删除的数据ID
     * @return 结果
     */
    public int deleteGradeById(String id);

}