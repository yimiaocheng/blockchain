package com.cwt.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo implements Serializable{

    private String id;
    private String userName;//手机号
    private String nickName;
    private String headImg;

}
