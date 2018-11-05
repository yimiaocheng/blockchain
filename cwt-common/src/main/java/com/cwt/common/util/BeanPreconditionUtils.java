package com.cwt.common.util;

import java.util.Collection;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/3/18 16:19
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
public class BeanPreconditionUtils {

    /**
     * 判断字符串不为空或空串
     *
     * @param checkString  需要判断的字符串
     * @return  返回的判断结果
     */
    public static boolean checkStringArgumentsNotBlank(String checkString) {
        if (!StringUtils.isNotBlank(checkString)) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个集合是否为空（包含null和size为0的情况）
     *
     * @param collection 需要判断的集合
     * @return  返回的判断结果
     */
    public static boolean checkCollectionNotEmpty(Collection collection) {
        if (collection == null || collection.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个对象不可以为空
     *
     * @param obj 需要判断的对象
     * @return  返回的判断结果
     */
    public static boolean checkNotNull(Object obj) {
        if (Objects.isNull(obj)) {
            return true;
        }
        return false;
    }
}
