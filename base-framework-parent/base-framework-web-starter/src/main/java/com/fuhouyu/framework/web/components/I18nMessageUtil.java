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
package com.fuhouyu.framework.web.components;

import com.fuhouyu.framework.common.utils.LoggerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * <p>
 * i18n消息工具类
 * </p>
 *
 * @author fuhouyu
 * @since 2026/2/1 18:10
 */
@Component
@Slf4j
public class I18nMessageUtil {

    private static MessageSource messageSource;

    public I18nMessageUtil(MessageSource messageSource) {
        I18nMessageUtil.messageSource = messageSource;
    }

    /**
     * 获取翻译后的文案
     *
     * @param msgKey I18n 文件中的 Key (即 ResponseStatusEnum 里的 message)
     * @param args   动态参数（用于替换 {0} {1} 等占位符）
     * @return 翻译后的字符串
     */
    public static String getMessage(String msgKey, Object... args) {
        try {
            LocaleContextHolder.setDefaultLocale(Locale.CHINA);
            return messageSource.getMessage(msgKey, args, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            // 如果找不到 Key，直接返回 Key 本身，避免报错
            LoggerUtil.error(log, "找不到 Key: {}, 错误信息:{}", msgKey, e.getMessage());
            return msgKey;
        }
    }
}
