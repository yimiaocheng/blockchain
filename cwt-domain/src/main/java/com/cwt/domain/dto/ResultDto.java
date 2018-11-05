package com.cwt.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/7/4 9:44
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
@Data
@AllArgsConstructor
public class ResultDto<T extends Object> implements Serializable{
    private String code;//返回码
    private String msg;//返回码描述
    private T data;

    public ResultDto(){

    }

    public ResultDto(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
