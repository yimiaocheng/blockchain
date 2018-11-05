package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 商户收款表
 */
@Table(name = "app_user_receive")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReceive implements Serializable{

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "userId")
    private String userId;
    @Column(name = "order_no")
    private String orderNo;
    @Column(name = "fee_ct")
    private String fee_ct;
    @Column(name = "fee_integral")
    private String fee_integral;
    @Column(name = "status")
    private String status;
    @Column(name = "create_time")
    private java.util.Date createTime;
    @Column(name = "modify_time")
    private java.util.Date modifyTime;
}
