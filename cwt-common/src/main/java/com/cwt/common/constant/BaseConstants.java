package com.cwt.common.constant;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/5/2 17:59
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
public class BaseConstants {
    //用户登陆session
    public static final String USER_LOGIN_SESSION = "USER_LOGIN_SESSION";
    //用户登陆redis缓存key
    public static final String LOGIN_USER_REDIS_KEY = "LOGIN:USER:KEY:";
    //用户登陆redis缓存时间
    public static long LOGIN_CACHE_TIME_DEFAULT = 24*60*60;

    //新建订单key的前缀 ，redis.set(key,value), key=设置值+orderbillId,value=currentTimeMillis+失效时间
    public static final String REDIS_KEY_ORDER_NEW_PRE = "order:balance:new:";
    //存放新添加订单id到该key，redis.rightPush(key,orderbillId)
    public static final String REDIS_KEY_ORDER_NEW_LIST = "order-new-list";
    public static final long ORDER_NEW_EXPIRE_MILLISECONDS = 1800000;//30 * 60 * 1000

    //币币行情缓存时间
    public static long COIN_MARKER_CACHE_TIME_DEFAULT = 24*60*60*2;
    //币币行情 比特币 的缓存KEY值
    public  static final String LAST_TIME_BTC_PRICE_KEY = "LAST-TIME-BTC-PRICE-KEY";
    //币币行情 以太坊 的缓存KEY值
    public  static final String LAST_TIME_ETH_PRICE_KEY = "LAST-TIME-ETH-PRICE-KEY";

}
