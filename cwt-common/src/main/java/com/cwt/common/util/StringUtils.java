package com.cwt.common.util;

import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * \* <p>Desciption:数据串工具类</p>
 * \* CreateTime: 2018/3/12 12:31
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
public class StringUtils {

    /**
     * 驼峰法转(大写字母)下划线
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String getCamelUnderline(String line){
        if(line==null||"".equals(line)){
            return "";
        }
        line=String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb=new StringBuffer();
        Pattern pattern=Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end()==line.length()?"":"_");
        }
        return sb.toString();
    }

    /**
     * 把驼峰实体类转成大写加下划线实体类
     * @param JsonObject 实体类
     * @return JSONObject
     */
    public static Map<String, String> getCamelUnderlineJsonObject(Object JsonObject){

        String orgExtendJson = JsonUtils.objectToJson(JsonObject);
        Map<String, String> jsonToHashMap = JsonUtils.jsonToHashMap(orgExtendJson, String.class, String.class);
        Map<String, String> newMap = Maps.newHashMapWithExpectedSize(jsonToHashMap.size());
        Iterator<Map.Entry<String, String>> iterator = jsonToHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String key = StringUtils.getCamelUnderline(next.getKey());
            String value = next.getValue();
            newMap.put(key, value);
        }
        return newMap;
    }
}
