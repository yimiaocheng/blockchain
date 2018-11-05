package com.cwt.app.controller.api;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/8/10 9:29
 * \* User: YeHao
 * \* Version: V1.0
 * \
 */
public class LoginApi {
    public static final String USER_CONTROLLER_API = "用户控制器";

    public static class Register{
        public static final String METHOD_API_NAME = "注册";
        public static final String METHOD_API_NOTE = "注册接口";
        public static final String METHOD_API_TELEPHONE = "手机号";
        public static final String METHOD_API_PSSWORD = "密码";
        public static final String METHOD_API_CODE = "验证码";
        public static final String METHOD_API_INVITERID = "邀请人id";
        public static final String METHOD_API_INVITER_CODE = "邀请码";
    }

    public static class Login{
        public static final String METHOD_API_NAME = "登录";
        public static final String METHOD_API_NOTE = "登录接口";
        public static final String METHOD_API_TELEPHONE = "手机号";
        public static final String METHOD_API_PSSWORD = "密码";
    }

    public static class LoginOut{
        public static final String METHOD_API_NAME = "退出登录";
        public static final String METHOD_API_NOTE = "退出登录接口";
    }

    public static class Smsg{
        public static final String METHOD_API_NAME = "短信验证码";
        public static final String METHOD_API_NOTE = "通过手机号获取短信验证码接口";
        public static final String METHOD_API_TELEPHONE = "手机号";
    }

    public static class FindPassword{
        public static final String METHOD_API_NAME = "找回密码";
        public static final String METHOD_API_NOTE = "找回密码接口";
        public static final String METHOD_API_TELEPHONE = "手机号";
        public static final String METHOD_API_NEWPASSWORD = "新密码";
        public static final String METHOD_API_SMSG_CODE = "验证码";
    }

    public static class SelectCountFriends{
        public static final String METHOD_API_NAME = "获取用户伞下所有好友数量";
        public static final String METHOD_API_NOTE = "获取用户伞下所有好友数量接口";
    }

    public static class Agreement{
        public static final String METHOD_API_NAME = "获取平台服务协议";
        public static final String METHOD_API_NOTE = "获取平台服务协议接口";
    }

}
