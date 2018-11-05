package com.cwt.common.exception;

import java.math.BigDecimal;

/**
 * \* <p>Desciption:用于判断类并返回错误信息</p>
 * \* CreateTime: 2018/3/18 16:09
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
public class WalletExceptionPreconditionUtils {
    /***
     * 比较转换/转账的余额是否超出当前余额
     *
     * @param transBalance 需要比较的转换/转账余额
     * @param balance 需要比较的当前钱包的余额
     * @param e 返回的业务错误
     */
    public static void checkNotBeyond(Double transBalance, BigDecimal balance, BaseException e) {
        if (new BigDecimal(transBalance+"").compareTo(balance) > 0) {
            throw e;
        }
    }

    /***
     * 判断钱包余额是否足够出售
     *
     * @param amount 需要出售的余额
     * @param balance 需要比较的当前钱包的余额
     * @param e 返回的业务错误
     */
    public static void checkNotBeyond(BigDecimal amount, BigDecimal balance, BaseException e) {
        if (balance.compareTo(amount) < 0) {
            throw e;
        }
    }

    /**
     * 判断一个Double对象不可以小于或等于零
     *
     * @param dou 需要判断的对象
     * @param e 如果对象小于等于0(0.01精度范围)，平台需要返回的业务错误代码
     */
    public static void checkNotZero(Double dou, BaseException e) {
        if (dou <= 0.1) {
            throw e;
        }
    }

    /**
     * 判断一个Double对象不可以小于100
     *
     * @param dou 需要判断的对象
     * @param e 如果对象小于100(余额转换最少100起步)，平台需要返回的业务错误代码
     */
    public static void checkLessThan(Double dou, BaseException e) {
        if (dou <100) {
            throw e;
        }
    }

    /**
     * 判断一个Double对象不可以整除100
     *
     * @param dou 需要判断的对象
     * @param e 如果对象不能整除100，平台需要返回的业务错误代码
     */
    public static void checkExactDivision(Double dou, BaseException e) {
        if ((dou % 100) != 0.0) {
            throw e;
        }
    }

    /***
     * 判断用户是否重复
     * @param obj 需要判断的对象
     * @param e 如果对象不为null，平台需要返回的业务错误代码
     */
    public static void checkUserNoRepetition(Object obj,BaseException e){
        if(obj != null){
            throw e;
        }
    }

    /***
     * 判断用户是否相同
     * @param obj 需要判断的对象
     * @param obj2 需要判断的对象
     * @param e 如果对象不为null，平台需要返回的业务错误代码
     */
    public static void checkUserNoRepetition(Object obj,Object obj2,BaseException e){
        if(obj == obj2){
            throw e;
        }
    }

}
