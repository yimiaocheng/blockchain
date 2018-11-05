package com.cwt.common.enums;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/7/9 18:30
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
public enum UserResultEnums {
    //----------------------------------------------------------------
    NULL_USER("1000","用户不存在"),
    NULL_ID("1001","id不能为空"),
    NULL_TELEPHONE("1002","手机号不能为空"),
    ALREADY_REGISTERED("1003", "该用户已经注册"),
    PASSWORD_NULL("1004", "密码不能为空"),
    LOGIN_ERR("1005", "手机号或密码错误"),
    SMSG_OUTTIME("1006", "短信验证码失效"),
    SMSG_FALSE("1007", "短信验证码错误"),
    SMSGCODE_NULL("1008", "验证码不能为空"),
    UPDATE_FAILED("1009", "修改失败"),
    INVITERID_NULL("1101", "邀请人id不能为空"),
    INSERT_ERR("1102", "注册失败"),
    SMSGCODE_ERR("1103", "短信验证码获取失败"),
    INVITATIONCODE_NULL("1104", "自己邀请码不能为空"),
    SMSGCODE_OVERFLOW("1105", "每个手机号每天只能发送10条验证码"),
    USER_NO_REGISTERED("1106", "该用户尚未注册"),
    INVITER_NULL("1107", "邀请人不存在"),
    USER_DISABLE("1110", "用户不可用"),
    USER_FORBIDDEN("1111", "用户不可用"),
    USERBANK_NULL("1108", "银行卡信息不能为空"),
    BANK_ERR("1109", "银行卡信息更新失败"),
    BANKINFO_NULL("1110", "暂无银行卡信息，请添加"),
    USER_INCONFORMITY("1111","当前修改的账号跟登录账号不一致！"),
    //***************************************************************************
    USER_NULL("1000","用户不存在"),
    USERID_NULL("1001","用户ID不存在"),
    NICK_NAME_NULL("1010","用户昵称不能为空"),
    NICK_NAME_REPEAT("1011","用户昵称没有修改"),
    USER_NAME_NULL("1012","账号不能为空"),
    USER_NAME_REPEAT("1013","账号没有修改"),
    USER_NAME_EXISTENCE("1036","该账号已注册"),
    IS_NOT_PASSWORD("1014","密码输入有误"),
    OLD_IS_NOT_PASSWORD("1015","旧密码输入有误，请重试"),
    OLD_PASSWORD_NULL("1016","旧密码为空"),
    PASSWORD_REPEAT("1017","密码没有修改"),
    PAYMENT_PASSWORD_NULL("1018","支付密码不能为空"),
    PAYMENT_PASSWORD_INPUT_ERROR("10190","支付密码修改失败，密码仅支持纯数字，请重试！"),
    PAYMENT_PASSWORD_INPUT_LENGTH_ERROR("10191","支付密码修改失败，密码长度在6~16位"),
    OLD_PAYMENT_PASSWORD_NULL("1019","旧支付密码为空"),
    PAYMENT_PASSWORD_REPEAT("1020","支付密码没有修改"),
    IS_NOT_OLD_PAYMENT_PASSWORD("1021","旧支付密码输入有误，请重试"),
    IS_NOT_PAYMENT_PASSWORD("1022","支付密码输入有误"),
    PAYMENT_METHOD_BANKCARD_ERROR("1023","该卡号暂时不能支持，请使用其他卡片"),
    PAYMENT_METHOD_BANKCARD_NULL("1024","银行卡号不能为空"),
    PAYMENT_METHOD_BANKCARD_REPEAT("1025","银行卡号没有修改"),
    PAYMENT_METHOD_ZFB_NULL("1026","支付宝账号不能为空"),
    PAYMENT_METHOD_ZFB_REPEAT("1027","支付宝账号没有修改"),
    PAYMENT_METHOD_ZFB_ERROR("1028","该支付宝账号不能识别，请使用其他支付宝账号"),
    PAYMENT_METHOD_WX_NULL("1029","微信号不能为空"),
    PAYMENT_METHOD_WX_REPEAT("1030","微信号没有修改"),
    PAYMENT_METHOD_WX_ERROR("1031","该微信账号不能识别，请使用其他微信账号"),
    FILE_NULL("1032","没有获取到上传的文件，请重新上传"),
    FILE_ERROR("1033","文件上传失败"),
    FILE_TYPE_NULL("1034","文件类型无法识别"),
    FILE_IMG_TYPE_ERROR("1035","文件类型暂时不支持，请使用“jpg”、“jpeg”、“png”格式的图片"),

//    -------------------------------------------------------
    IS_PASSWORD_SUCCESSFUL("200","密码验证成功"),
    IS_PAYMENT_PASSWORD_SUCCESSFUL("200","支付密码验证成功"),
    PASSWORD_SUCCESSFUL("200","密码修改成功"),
    PAYMENT_PASSWORD_SUCCESSFUL("200","支付密码修改成功"),
    NICK_NAME_SUCCESSFUL("200","昵称修改成功"),
    USER_NAME_SUCCESSFUL("200","账号修改成功"),
    PAYMENT_METHOD_BANKCARD_SUCCESSFUL("200","银行卡号修改成功"),
    PAYMENT_METHOD_WX_SUCCESSFUL("200","微信号修改成功"),
    PAYMENT_METHOD_ZFB_SUCCESSFUL("200","支付宝账号修改成功");



    private String code;
    private String msg;

    UserResultEnums(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
