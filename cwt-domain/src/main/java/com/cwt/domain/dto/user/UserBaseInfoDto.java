package com.cwt.domain.dto.user;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author huangxl
 * 用户基本信息
 */
@Data
public class UserBaseInfoDto {
    private String id;//用户id
    private String userName;//用户手机号
    private String nickName;//昵称
    private String headImg;//头像
    private String paymentMethodBankcard;//银行卡信息
    private String paymentMethodZfb;//支付宝信息
    private String paymentMethodWx;//微信信息
    private Integer invitationCode;//邀请码
    private Date createTime;//创建时间

    private String levelName;//等级名称
    private Integer level;//等级
    private BigDecimal flowBalance;//余额
    private BigDecimal calculationForce;//算力
    private BigDecimal gameCion;//游戏币
    private BigDecimal integral;//积分
}
