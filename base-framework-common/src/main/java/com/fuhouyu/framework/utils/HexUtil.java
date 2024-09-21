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

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 十六进制工具类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/21 18:35
 */
public class HexUtil {


    private static final char[] DIGITS_LOWER = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    private HexUtil() {
    }

    /**
     * 转换为十六进制字符串
     *
     * @param str 需要转换的字符串
     * @return 十六进制字符串
     */
    public static String encodeToHexString(String str) {
        return encodeToHexString(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 转换为十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    public static String encodeToHexString(final byte[] bytes) {

        final char[] out = new char[bytes.length * 2];

        encodeHex(bytes, bytes.length, out);
        return new String(out);
    }

    /**
     * 进行十六进制的解码
     *
     * @param hexStr 十六进制字符串
     * @return 字节数组对象
     */
    public static byte[] decodeHex(String hexStr) {
        int len = hexStr.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("Hex string must have an even length");
        }
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hexStr.charAt(i), 16) << 4)
                    + Character.digit(hexStr.charAt(i + 1), 16));
        }
        return bytes;
    }

    private static void encodeHex(final byte[] data, final int dataLen,
                                  final char[] out) {
        for (int i = 0, j = 0; i < dataLen; i++) {
            out[j++] = HexUtil.DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = HexUtil.DIGITS_LOWER[0x0F & data[i]];
        }
    }


}
