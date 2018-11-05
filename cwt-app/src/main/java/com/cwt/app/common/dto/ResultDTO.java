package com.cwt.app.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDTO<T extends Object> implements Serializable {
    private int code;//返回码
    private String msg;//返回码描述
    private T data;

    public static final int SUCCESS = 200;//成功
    public static final int FAIL = 300;//失败

    public static ResultDTO requstSuccess() {
        return requstSuccess(null);
    }

    public static ResultDTO requstSuccess(Object data) {
        return new ResultDTO(SUCCESS, "", data);
    }

    public static ResultDTO requstFail(String msg) {
        return new ResultDTO(FAIL, msg, null);
    }
}