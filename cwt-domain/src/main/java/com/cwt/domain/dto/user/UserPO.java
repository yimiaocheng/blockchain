package com.cwt.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPO implements Serializable{

    private String uid;//用户id
    private String telephone;//手机号
    private String invitation_code;//推荐人邀请码
    private String new_code;//我的邀请码

}
