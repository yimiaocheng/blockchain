package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 退款记录表
 */
@Table(name = "app_user_refunds")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReFund implements Serializable {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "userId")
    private String userId;
    @Column(name = "businessId")
    private String businessId;
    @Column(name = "orderId")
    private String orderId;
    @Column(name = "fee_ct")
    private String fee_ct;
    @Column(name = "fee_integral")
    private String fee_integral;
    @Column(name = "refund_status")
    private String refund_status;
    @Column(name = "remark")
    private String remark;
    @Column(name = "create_time")
    private java.util.Date createTime;
    @Column(name = "modify_time")
    private java.util.Date modifyTime;


}
