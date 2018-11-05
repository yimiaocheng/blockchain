package com.cwt.app.common.util;


import com.cwt.app.common.dto.UserSessionDto;
import com.cwt.common.constant.BaseConstants;

import javax.servlet.http.HttpSession;

/**
 * \* <p>Desciption:用户session工具类</p>
 * \* CreateTime: 2018/5/3 17:02
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
public class SessionUtils {



    public static UserSessionDto getSession(HttpSession session) {
        UserSessionDto user = null;
        if (null != session) {
            try {
                user = (UserSessionDto) session.getAttribute(BaseConstants.USER_LOGIN_SESSION);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public static void setSession(HttpSession session, UserSessionDto token) {
        if (null != session) {
            try {
                session.setAttribute(BaseConstants.USER_LOGIN_SESSION, token);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeSession(HttpSession session) {
        if (null != session) {
            try {
                session.removeAttribute(BaseConstants.USER_LOGIN_SESSION);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
