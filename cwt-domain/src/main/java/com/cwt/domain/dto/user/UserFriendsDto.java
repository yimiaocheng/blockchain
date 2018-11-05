package com.cwt.domain.dto.user;

import lombok.Data;

@Data
public class UserFriendsDto {
    private String userName;//用户手机号
    private String nickName;//用户昵称
    private String headImg;//用户头像
    private Integer invitationCode;//邀请码
    private Integer level;//用户等级
    private String levelName;//等级名称
    private java.util.Date createTime;//时间
}
