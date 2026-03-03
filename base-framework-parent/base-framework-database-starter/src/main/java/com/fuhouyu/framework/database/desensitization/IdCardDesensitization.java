package com.fuhouyu.framework.database.desensitization;

import com.fuhouyu.framework.database.CipherText;
import org.babyfish.jimmer.jackson.Converter;
import org.jspecify.annotations.NonNull;

/**
 * <p>
 * 身份证号脱敏
 * </p>
 *
 * @author fuhouyu
 * @since 2026/3/3 21:05
 */
public class IdCardDesensitization implements Converter<CipherText, String> {
    @Override
    public @NonNull String output(@NonNull CipherText cipherText) {
        String value = cipherText.value();
        if (value.length() < 2) {
            return value;
        }
        return value.replaceAll("^(.).*(.)$", "$1****************$2");
    }
}
