package com.cwt.app.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/8/9 11:30
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSessionDto implements Serializable {
    private String userId;
    private String telephone;
    private String token;
}
