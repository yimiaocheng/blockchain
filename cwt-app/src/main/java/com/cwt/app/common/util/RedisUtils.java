package com.cwt.app.common.util;

import com.cwt.common.util.JsonUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/3/19 10:08
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
public class RedisUtils {
    private RedisTemplate redisTemplate;

    public RedisUtils(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private HashOperations<String, String, Object> getHashOps() {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations;
    }

    private ListOperations<String, String> getListOps() {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        return listOperations;
    }

    private ValueOperations getValueOps(){
        return redisTemplate.opsForValue();
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * redis-hash数据结构：存入
     *
     * @param key redis-key
     * @param hashKey hash-key
     * @param value hash-value
     */
    public void hashPut(String key, String hashKey, Object value) {
        getHashOps().put(key, hashKey, JsonUtils.objectToJson(value));
    }

    /**
     * redis-hash数据结构：存入
     *
     * @param key redis-key
     * @param hashKey hash-key
     * @param value hash-value
     */
    public void hashPut(String key, String hashKey, String value){
        getHashOps().put(key, hashKey, value);
    }

    /**
     * redis-hash数据结构：判断key是否存在
     *
     * @param key redis-key
     * @param hashKey hash-key
     * @return true：存在 false：不存在
     */
    public boolean hashKeyExist(String key, String hashKey) {
        return getHashOps().hasKey(key, hashKey);
    }

    /**
     * redis-hash数据结构：批量删除hashKey
     *
     * @param key redis-key
     * @param hashKey hash-key
     * @return 删除条目数
     */
    public Long deleteByHashKeys(String key, String... hashKey) {
        return getHashOps().delete(key, hashKey);
    }

    /**
     * redis-hash数据结构：获取存储的值
     *
     * @param key
     * @param hashKey
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getHashValue(String key, String hashKey, Class<T> clazz) {
        String objJson = getHashValue(key, hashKey);
        return JsonUtils.jsonToPojo(objJson, clazz);
    }

    /**
     * 通过rediskey和hashKey获取值
     *
     * @param key
     * @param hashKey
     * @return
     */
    public String getHashValue(String key, String hashKey) {
        return (String) getHashOps().get(key, hashKey);
    }

    /**
     * redis-hash数据结构：获取所有的key
     *
     * @param redisKey redis-key
     * @return
     */
    public Set<String> getHashKeys(String redisKey) {
        return getHashOps().keys(redisKey);
    }

    /**
     * redis-list数据结构：左推进
     *
     * @param key
     * @param value
     * @return
     */
    public Long listLeftPush(String key, String value) {
        return getListOps().leftPush(key, value);
    }

    /**
     * redis-list数据结构：右弹出
     *
     * @param key
     * @return
     */
    public String listRightPop(String key) {
        return getListOps().rightPop(key);
    }

    /**
     * key:value数据结构
     * @param key
     * @param value
     */
    public void setKeyValue(String key, Object value) {
        setKeyValue(key, value, -1);
    }

    /**
     * key:value数据结构，并设置过期时间，单位秒
     * @param key
     * @param value
     * @param expire 过期时间，单位秒
     */
    public void setKeyValue(String key, Object value, long expire){
        getValueOps().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * 通过key获取value值
     * @param key
     * @return
     */
    public Object getValue(String key){
        return getValueOps().get(key);
    }

    /**
     * 删除一个key
     * @param key
     */
    public void deleteByKey(String key){
        redisTemplate.delete(key);
    }
}
