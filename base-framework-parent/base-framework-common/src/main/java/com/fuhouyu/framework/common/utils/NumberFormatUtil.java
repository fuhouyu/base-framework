/*
 * Copyright 2024-present fuhouyu.
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
package com.fuhouyu.framework.common.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 数据转换工具类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/2 16:09
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class NumberFormatUtil {

    /**
     * 将字符串转换为 Long 类型
     *
     * @param string 要转换的字符串
     * @return 转换后的 Long 值，如果转换失败则返回 0
     */
    public static long toLong(String string) {
        return toLong(string, 0L);
    }

    /**
     * 将字符串转换为 Long 类型
     *
     * @param str          要转换的字符串
     * @param defaultValue 转换失败的默认值
     * @return 转换后的 Long 值，如果转换失败则返回 默认值
     */
    public static long toLong(String str, long defaultValue) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            LoggerUtil.error(log, "无法将字符串转换为 Long: {}, 返回默认值:{} ", str, defaultValue);
            return defaultValue;
        }
    }

    /**
     * 将字符串转换为 int 类型
     *
     * @param str 要转换的字符串
     * @return 转换后的 Integer 值，如果转换失败则返回 0
     */
    public static int toInt(String str) {
        return toInt(str, 0);
    }

    /**
     * 将字符串转换为 int 类型
     *
     * @param str          要转换的字符串
     * @param defaultValue 默认值
     * @return 转换后的 Integer 值，如果转换失败则返回 默认值
     */
    public static int toInt(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            LoggerUtil.error(log, "无法将字符串转换为 int: {}, 返回默认值:{} ", str, defaultValue);
            return defaultValue;
        }
    }

    /**
     * 将字符串转换为 Double 类型
     *
     * @param str 要转换的字符串
     * @return 转换后的 Double 值，如果转换失败则返回 0.0
     */
    public static double toDouble(String str) {
        return toDouble(str, 0.0);
    }

    /**
     * 将字符串转换为 Double 类型
     *
     * @param str          要转换的字符串
     * @param defaultValue 转换失败后返回的默认值
     * @return 转换后的 Double 值，如果转换失败则返回 默认值
     */
    public static double toDouble(String str, double defaultValue) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            LoggerUtil.error(log, "无法将字符串转换为 double: {}, 返回默认值:{} ", str, defaultValue);
            return defaultValue;
        }
    }

    /**
     * 将字符串转换为 Float 类型
     *
     * @param str 要转换的字符串
     * @return 转换后的 Float 值，如果转换失败则返回 0.0
     */
    public static float toFloat(String str) {
        return toFloat(str, 0.0f);
    }

    /**
     * 将字符串转换为 Float 类型
     *
     * @param str          要转换的字符串
     * @param defaultValue 默认值
     * @return 转换后的 Float 值，如果转换失败则返回 默认值
     */
    public static float toFloat(String str, float defaultValue) {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            LoggerUtil.error(log, "无法将字符串转换为 float: {}, 返回默认值:{} ", str, defaultValue);
            return defaultValue;
        }
    }

}
