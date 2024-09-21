/*
 * Copyright 2024-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fuhouyu.framework.web.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * <p>
 * bean转换工具类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/19 18:27
 */
public class BeanConvertUtil {

    private BeanConvertUtil() {

    }

    /**
     * 转换对象
     *
     * @param source         源数据
     * @param targetSupplier 目标对象
     * @param <S>            源类型
     * @param <T>            目标类型
     * @return 转换后的对象
     */
    public static <S, T> T convertTo(S source, Supplier<T> targetSupplier) {
        return convertTo(source, targetSupplier, null);
    }

    /**
     * 转换对象
     *
     * @param source         源数据
     * @param targetSupplier 目标对象
     * @param callback       回调消费
     * @param <S>            源类型
     * @param <T>            目标类型
     * @return 转换后的对象
     */
    public static <S, T> T convertTo(S source, Supplier<T> targetSupplier,
                                     BiConsumer<S, T> callback) {
        if (null == source || null == targetSupplier) {
            return null;
        }
        T target = targetSupplier.get();
        BeanUtils.copyProperties(source, target);
        if (Objects.nonNull(callback)) {
            callback.accept(source, target);
        }
        return target;
    }

    /**
     * 转换对象
     *
     * @param sources        源对象list
     * @param targetSupplier 目标对象供应方
     * @param <S>            源对象类型
     * @param <T>            目标对象类型
     * @return 目标对象list
     */
    public static <S, T> List<T> convertListTo(List<S> sources, Supplier<T> targetSupplier) {
        return convertListTo(sources, targetSupplier, null);
    }

    /**
     * 转换对象
     *
     * @param sources        源对象list
     * @param targetSupplier 目标对象供应方
     * @param callBack       回调方法
     * @param <S>            源对象类型
     * @param <T>            目标对象类型
     * @return 目标对象list
     */
    @SuppressWarnings("all")
    public static <S, T> List<T> convertListTo(List<S> sources, Supplier<T> targetSupplier,
                                               BiConsumer<S, T> callBack) {
        if (null == sources || null == targetSupplier) {
            return Collections.emptyList();
        }
        Class<? extends List> listClazz = sources.getClass();
        List<T> list;
        try {
            Constructor<? extends List> constructor = ReflectionUtils.accessibleConstructor(listClazz);
            list = constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for (S source : sources) {
            T target = targetSupplier.get();
            BeanUtils.copyProperties(source, target);
            if (callBack != null) {
                callBack.accept(source, target);
            }
            list.add(target);
        }
        return list;
    }

}
