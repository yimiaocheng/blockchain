package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "app_wallet")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBalance implements Serializable{
    //@Id
    //@Column(name = "id")
    //private String id;
    @Column(name = "user_id")
    private String userId;
    /**
     * 流动余额ct
     */
    @Column(name = "flow_balance")
    private BigDecimal flowBalance;
    /**
     * 智能算力
     */
    @Column(name = "calculation_force")
    private BigDecimal calculationForce;
    /**
     * 游戏币
     */
    @Column(name = "game_cion")
    private BigDecimal gameCion;
    /**
     * 积分
     */
    @Column(name = "integral")
    private BigDecimal integral;
    //@Column(name = "version")
    //private Long version;
    /*@Column(name = "create_time")
    private java.util.Date createTime;
    @Column(name = "modify_time")
    private java.util.Date modifyTime;*/


}
