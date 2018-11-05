package com.cwt.app.controller.api;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/8/10 9:29
 * \* User: YeHao
 * \* Version: V1.0
 * \
 */
public class UserApi {
    public static final String USER_CONTROLLER_API = "用户控制器";

//    ******************************
//    User:XianChaoWei
//    ******************************

    public static class LoginOut {
        public static final String METHOD_API_NAME = "退出登录";
        public static final String METHOD_API_NOTE = "退出登录接口";
    }

    public static class GetUserById {
        public static final String METHOD_API_NAME = "通过id获取用户信息";
        public static final String METHOD_API_NOTE = "通过id获取用户信息接口";
        public static final String METHOD_API_ID = "id";
    }

    public static class GetUserByInvitationCode {
        public static final String METHOD_API_NAME = "通过自己的邀请码获取用户信息";
        public static final String METHOD_API_NOTE = "通过自己的邀请码获取用户信息接口";
        public static final String METHOD_API_INVITATIONCODE = "自己的邀请码";
    }

    public static class GetFriends {
        public static final String METHOD_API_NAME = "获得伞下第一级好友信息列表";
        public static final String METHOD_API_NOTE = "获得伞下第一级好友信息列表接口";
    }

    public static class SelectCoutFriends {
        public static final String METHOD_API_NAME = "获得伞下第一、二级好友总数";
        public static final String METHOD_API_NOTE = "获得伞下第一、二级好友总数接口";
    }

    public static class GetUserBankInfo {
        public static final String METHOD_API_NAME = "获取用户银行卡信息";
        public static final String METHOD_API_NOTE = "获取用户银行卡信息接口";
    }

    public static class SaveOrUpdateUserBankInfo {
        public static final String METHOD_API_NAME = "保存或修改用户银行卡信息";
        public static final String METHOD_API_NOTE = "保存或修改用户银行卡信息接口";
        public static final String METHOD_API_USERNAME = "持卡人姓名";
        public static final String METHOD_API_BANKLOCATION = "银行开户地";
        public static final String METHOD_API_BANKNAME = "开卡银行";
        public static final String METHOD_API_BRANCHNAME = "支行名称";
        public static final String METHOD_API_BANKNUMBER = "银行卡号";

    }

    public static class GetUserByTelephone {
        public static final String METHOD_API_NAME = "通过手机号获取用户信息";
        public static final String METHOD_API_NOTE = "通过手机号获取用户信息接口";
        public static final String METHOD_API_TELEPHONE = "手机号";
    }

    public static class SetPaymentPassword {
        public static final String METHOD_API_NAME = "设置初始支付密码";
        public static final String METHOD_API_NOTE = "设置初始支付密码接口";
        public static final String METHOD_API_PaymentPassword = "支付密码";
    }

    public static class GetUserBaseInfo {
        public static final String METHOD_API_NAME = "查询用户基本信息";
        public static final String METHOD_API_NOTE = "查询用户基本信息接口";
    }

    //    ******************************
//    User:YeHao
//    ******************************
    public static class GetSessionUser {
        public static final String METHOD_API_NAME = "获取缓存的用户信息";
        public static final String METHOD_API_NOTE = "获取缓存的用户信息接口";
    }

    public static class GetSessionUserPayType {
        public static final String METHOD_API_NAME = "获取缓存中的用户支付方式的绑定信息";
        public static final String METHOD_API_NOTE = "获取缓存中的用户支付方式的绑定信息";
    }

    public static class UpdateNickName {
        public static final String METHOD_API_NAME = "修改昵称";
        public static final String METHOD_API_NOTE = "修改昵称接口";
        public static final String METHOD_API_NICK_NAME = "昵称";
    }

    public static class UpdateUserName {
        public static final String METHOD_API_NAME = "修改账号";
        public static final String METHOD_API_NOTE = "修改账号接口";
        public static final String METHOD_API_USER_NAME = "账号（手机号）";
        public static final String METHOD_API_PHONE_CODE = "验证码";
    }

    public static class UpdatePassword {
        public static final String METHOD_API_NAME = "修改登录密码";
        public static final String METHOD_API_NOTE = "修改登录密码接口";
        public static final String METHOD_API_PASSWORD = "登录密码";
    }

    public static class UpdatePaymentPassword {
        public static final String METHOD_API_NAME = "修改支付密码";
        public static final String METHOD_API_NOTE = "修改支付密码接口";
        public static final String METHOD_API_PAYMENT_PASSWORD = "支付密码";
    }

    public static class UpdatePaymentMethodBankcard {
        public static final String METHOD_API_NAME = "修改银行卡号";
        public static final String METHOD_API_NOTE = "修改银行卡号接口";
        public static final String METHOD_API_PAYMENT_METHOD_BANKCARD = "银行卡号";
    }

    public static class UpdatePaymentMethodZfb {
        public static final String METHOD_API_NAME = "修改支付宝账号";
        public static final String METHOD_API_NOTE = "修改支付宝账号接口";
        public static final String METHOD_API_PAYMENT_METHOD_ZFB = "支付宝账号";
    }

    public static class UpdatePaymentMethodWx {
        public static final String METHOD_API_NAME = "修改微信号";
        public static final String METHOD_API_NOTE = "修改微信号接口";
        public static final String METHOD_API_PAYMENT_METHOD_WX = "微信号";
    }

    public static class IsPassword {
        public static final String METHOD_API_NAME = "登录密码验证";
        public static final String METHOD_API_NOTE = "登录密码验证接口";
        public static final String METHOD_API_PASSWORD = "登录密码";
    }

    public static class isPaymentPassword {
        public static final String METHOD_API_NAME = "支付密码验证";
        public static final String METHOD_API_NOTE = "支付密码验证接口";
        public static final String METHOD_API_PAYMENT_PASSWORD = "支付密码";
    }

    public static class updateUser {
        public static final String METHOD_API_NAME = "更新用户信息";
        public static final String METHOD_API_NOTE = "更新用户信息接口";
        public static final String METHOD_API_USER_INFO = "用户信息";
    }

    public static class updateHeadImg {
        public static final String METHOD_API_NAME = "更新用户头像";
        public static final String METHOD_API_NOTE = "更新用户头像接口";
        public static final String METHOD_API_IMG = "base64字符串(图片转换得来)";
    }

    /****lzf****/
    public static class getUserTransferInfo {
        public static final String METHOD_API_NAME = "根据用户id查询等级和用户基本信息";
        public static final String METHOD_API_NOTE = "根据用户id查询等级和用户基本信息";
        public static final String METHOD_USERID = "收款用户的id";
    }

    public static class getPayInfo {
        public static final String METHOD_API_NAME = "根据电话号查询用户支付账号信息";
        public static final String METHOD_API_NOTE = "根据电话号查询用户支付账号信息";
        public static final String METHOD_USERNAME = "用户名/电话";
    }

    public static class updateResetPayPassWord{
        public static final String METHOD_API_NAME = "重置支付密码";
        public static final String METHOD_API_NOTE = "重置支付密码";
        public static final String METHOD_API_USER_NAME = "账号（手机号）";
        public static final String METHOD_API_PHONE_CODE = "验证码";
    }
}
