package com.cwt.app.common.util;


import com.cwt.common.enums.BaseResultEnums;
import com.cwt.domain.dto.ResultDto;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/7/4 11:15
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
public class ResultUtils {

    /**
     * 创建请求成功返回数据
     * @param result
     * @return
     */
    public static <T extends Object>ResultDto buildSuccessDto(T result) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(BaseResultEnums.SUCCESS.getCode());
        resultDto.setMsg(BaseResultEnums.SUCCESS.getMsg());
        resultDto.setData(result);
        return resultDto;
    }

}
