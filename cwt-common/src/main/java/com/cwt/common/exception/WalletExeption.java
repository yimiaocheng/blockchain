package com.cwt.common.exception;


import com.cwt.common.enums.WalletResultEnums;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/7/9 18:29
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
public class WalletExeption extends BaseException {
    public WalletExeption(WalletResultEnums code) {
        super(code.getCode(), code.getMsg());
    }
}
