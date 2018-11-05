package com.cwt.common.util;

import java.lang.annotation.*;

/**
 * \* <p>Desciption:针对BeanUtils的字段值复制，表示不参与复制的字段</p>
 * \* CreateTime: 2018/3/12 12:20
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BeanUtilsNoCopyValue {
}
