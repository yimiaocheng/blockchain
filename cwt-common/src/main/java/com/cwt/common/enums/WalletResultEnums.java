package com.cwt.common.enums;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/7/9 18:30
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
public enum WalletResultEnums {
    NO_WALLET("2000","钱包不存在"),
    WALLETID_NULL("2001","钱包id不能为空"),
    WALLETTYPE_NULL("2001","钱包类型不能为空"),
    WALLET_BALANCE_NULL("2001","数字资产不足"),

    NO_WALLETBILL("2000","钱包不存在"),
    WALLETBILLID_NULL("2001","账单id不能为空"),

    AUTOTRANS_AGAIN("2002","今天已领过智能释放奖励！"),
    NOT_UP_TO_STANDARD("2002","当前等级不能获得智能释放奖励！"),
    TRANSFER_AMOUNT_NULL("2002","转账金额不能为空"),
    TRANSFER_AMOUNT_ZERO("2002","转账金额不能为0或小于0"),

    TARANSFORM_TRANSBALANCE_NULL("2002","兑换的数字资产不能为空"),
    TARANSFORM_TRANSBALANCE_ZERO("2002","兑换的数字资产不能为0或小于0"),
    TARANSFORM_TRANSBALANCE_BEYOND("2002","兑换的数字资产不能超过当前资产数量"),
    TARANSFORM_TRANSBALANCE_LESSTHAN("2002","兑换的数字资产不能小于100"),
    TARANSFORM_TRANSBALANCE_EXACTDIVISION("2002","兑换的数字资产必须是100的倍数"),

    TARANSFORM_TRANSGAMECOIN_NULL("2002","兑换的游戏币不能为空"),
    TARANSFORM_TRANSGAMECOIN_ZERO("2002","兑换的游戏币不能为0或小于0"),
    TARANSFORM_TRANSGAMECOIN_BEYOND("2002","兑换的游戏币不能超过当前资产数量"),

    SAVA_WALLET_BY_USER("2003","用户已存在钱包！"),
    USER_REPETITION("2003","收款用户跟转账用户重复！");

    private String code;
    private String msg;

    WalletResultEnums(String code, String msg) {
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
