package com.cwt.domain.dto.user;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author huangxl
 * 用户基本信息
 */
@Data
public class UserTransferInfoDto {
    private String id;//用户id
    private String userName;//用户手机号
    private String nickName;//昵称
    private String headImg;//头像
    private Integer invitationCode;//邀请码
    private String levelName;//等级名称
    private Integer level;//等级
}
