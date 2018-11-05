package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发放奖金记录表
 */
@Table(name = "app_user_award")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAward implements Serializable{
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "userId")
    private String userId;
    @Column(name = "award_ct")
    private BigDecimal award_ct;
    @Column(name = "award_integral")
    private BigDecimal award_integral;
    @Column(name = "award_type")
    private String award_type;
    @Column(name = "award_remark")
    private String award_remark;
    @Column(name = "award_orderNo")
    private String award_orderNo;
    @Column(name = "award_status")
    private String award_status;
    @Column(name = "create_time")
    private java.util.Date createTime;
    @Column(name = "modify_time")
    private java.util.Date modifyTime;


}
