package com.cwt.app.common.handle;

import com.cwt.common.enums.BaseResultEnums;
import com.cwt.common.exception.BaseException;
import com.cwt.domain.dto.ResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/3/18 11:50
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
@ControllerAdvice
@RestController
public class ExceptionControllerHandler<T extends BaseException> {
    private Logger logger = LoggerFactory.getLogger(ExceptionControllerHandler.class);

    /**
     * 处理系统出现的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultDto commonException(T e) {
        logger.info("{}业务异常，异常类型：{}，异常信息：{}", new Date(), e.getClass().getName(), e.getMessage());
        e.printStackTrace();
        return new ResultDto(e.getCode(),e.getMsg());
    }

    /**
     * 处理未知错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public ResultDto otherEx(T e) {
        logger.error("{}系统抛出异常，异常是：{}，异常信息是：{}", new Date(), e.getClass().getName(), e.getMessage());
        e.printStackTrace();
        return new ResultDto(BaseResultEnums.DEFAULT.getCode(), BaseResultEnums.DEFAULT.getMsg());
    }

}
