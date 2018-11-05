package com.cwt.domain.dto.user;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackendUserDTO extends BaseDto {

    private String id;
    private String invitationCode;
    private String userName;
    private String nickName;
    private String status;
    private String paymentMethodBankcard;
    private String paymentMethodZfb;
    private String paymentMethodWx;
    private java.util.Date createTime;
    private Double flowBalance;
    private Double calculationForce;
    private BigDecimal gameCion;//游戏代币
    private BigDecimal integral;//商城积分
    private String levelName;
    private String level;
    private Date beginTime;//开始时间
    private Date endTime;//结束时间

    private Integer cpArithmeticType;//智能算力修改类型
    private BigDecimal cpAmount;//智能算力修改数值
    private Integer fbArithmeticType;//流动余额修改类型
    private BigDecimal fbAmount;//流动余额修改数值


}
