package com.cwt.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/9/6 0006 14:38
 * \* User: YeHao
 * \
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCountFriendsDto {
    String id;  //用户 ID
    Integer directNumber; //直接好友数（一级好友数）
    Integer indirectNumber; //间接好友数 （二级好友数）
    Integer countNumver; //总好友数
}
