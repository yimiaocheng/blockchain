package com.cwt.app.common.interceptor;

import com.cwt.app.common.dto.UserSessionDto;
import com.cwt.app.common.util.RedisUtils;
import com.cwt.app.common.util.SessionUtils;
import com.cwt.common.constant.BaseConstants;
import com.cwt.common.enums.BaseResultEnums;
import com.cwt.common.exception.BaseException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Enumeration;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/5/4 11:55
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private RedisUtils redisUtils;

    private static Logger LOG = LoggerFactory.getLogger(TokenInterceptor.class);

//    private static List<String> uncheckUrls = new ArrayList();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            LOG.info("=======URI IS ==========="+request.getRequestURI());
            UserSessionDto sessionDto = SessionUtils.getSession(request.getSession());
            if (sessionDto == null) {//用户未登陆
                /*System.out.println("第一次登陆");
                sessionDto = new UserSessionDto();
                sessionDto.setUserId("5a65e899-81c1-4866-b08d-0b9415d32dc1");
                SessionUtils.setSession(request.getSession(), sessionDto);*/

                //从请求的header中获取token
                Enumeration e = request.getHeaders("X-CSRF-TOKEN");
                String token = "";
                while (e.hasMoreElements()) {
                    token = (String) e.nextElement();
                }
                //判断是否有token
                System.out.println("sjgdjshgdksk:"+token);
                if (!StringUtils.isNotBlank(token)) {
                    throwNoLoginError(request, response);
                }
                sessionDto = (UserSessionDto) redisUtils.getValue(BaseConstants.LOGIN_USER_REDIS_KEY + token);
                //判断token是否已过期
                if (sessionDto == null) {
                    throwNoLoginError(request, response);
                }
                SessionUtils.setSession(request.getSession(), sessionDto);
                return true;
            }
        }
        return super.preHandle(request, response, handler);
    }

    /**
     * 改变跨域请求的respone请求头
     *
     * @param request
     * @param response
     */
    private void throwNoLoginError(HttpServletRequest request, HttpServletResponse response) {
        String XRequested = request.getHeader("X-Requested-With");
        if (XRequested != null && "XMLHttpRequest".equals(XRequested)) {
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Content-Type", "application/json;charset=UTF-8");
            response.addHeader("Date", new Date().toString());
            response.addHeader("Transfer-Encoding", "chunked");
            response.addHeader("Vary", "Origin");
        }
        throw new BaseException(BaseResultEnums.NO_LOGIN);
    }
}
