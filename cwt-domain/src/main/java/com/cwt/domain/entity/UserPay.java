package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户支付信息表
 */
@Table(name = "app_user_pay")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPay implements Serializable{

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "from_id")
    private String fromId;
    @Column(name = "order_no")
    private String orderNo;
    @Column(name = "fee_ct")
    private String fee_ct;
    @Column(name = "fee_integral")
    private String fee_integral;
    @Column(name = "status")
    private String status;
    @Column(name = "body")
    private String body;
    @Column(name = "notify_url")
    private String notifyUrl;
    @Column(name = "sign")
    private String sign;
    @Column(name = "timesiamp")
    private String timesiamp;
    @Column(name = "state")
    private String state;
    @Column(name = "create_time")
    private java.util.Date createTime;
    @Column(name = "modify_time")
    private java.util.Date modifyTime;


}
