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

package com.fuhouyu.framework.utils;

import org.slf4j.Logger;

/**
 * <p>
 * 日志工具类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 19:39
 */
public class LoggerUtil {

    private LoggerUtil() {

    }


    /**
     * 打印info日志
     *
     * @param logger logger
     * @param msg    日志内容
     * @param args   参数
     */
    public static void info(Logger logger, String msg, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(msg, args);
        }
    }


    /**
     * 打印error日志
     *
     * @param logger logger
     * @param msg    日志内容
     * @param args   参数
     */
    public static void error(Logger logger, String msg, Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(msg, args);
        }
    }

    /**
     * 打印debug日志
     *
     * @param logger logger
     * @param msg    日志内容
     * @param args   参数
     */
    public static void debug(Logger logger, String msg, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.error(msg, args);
        }
    }


    /**
     * 打印warn日志
     *
     * @param logger logger
     * @param msg    日志内容
     * @param args   参数
     */
    public static void warn(Logger logger, String msg, Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(msg, args);
        }
    }

    /**
     * 打印trace日志
     *
     * @param logger logger
     * @param msg    日志内容
     * @param args   参数
     */
    public static void trace(Logger logger, String msg, Object... args) {
        if (logger.isTraceEnabled()) {
            logger.trace(msg, args);
        }
    }
}
