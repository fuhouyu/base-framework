package com.fuhouyu.framework.database.desensitization;

import com.fuhouyu.framework.database.CipherText;
import org.babyfish.jimmer.jackson.Converter;
import org.jspecify.annotations.NonNull;

/**
 * <p>
 * 手机号脱敏
 * </p>
 *
 * @author fuhouyu
 * @since 2026/3/3 21:01
 */
public class PhoneDesensitization implements Converter<CipherText, String> {
    @Override
    public @NonNull String output(@NonNull CipherText cipherText) {
        String value = cipherText.value();
        if (value.length() != 11) {
            // 长度不对，原样返回
            return value;
        }
        return value.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
}
