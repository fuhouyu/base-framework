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

package com.fuhouyu.framework.security.serializer;

/**
 * <p>
 * token存储序列化接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 21:30
 */
public interface SerializationStrategy {

    /**
     * 从字节数组中进行反序列化
     *
     * @param bytes 字节数组
     * @param <T>   对象类型
     * @return 反序列化获取到的对象
     */
    <T> T deserialize(byte[] bytes);

    /**
     * 从字节数组中进行反序列化
     *
     * @param bytes 字节数组
     * @param clazz 反序列化的类型
     * @param <T>   对象类型
     * @return 反序列化获取到的对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);

    /**
     * 从字节数组中进行反序列化
     *
     * @param bytes 字节数组
     * @return 反序列化成字符串
     */
    String deserializeString(byte[] bytes);

    /**
     * 从对象中进行序列化
     *
     * @param object 对象
     * @return 字节数组
     */
    byte[] serialize(Object object);

    /**
     * 从字符串中进行序列化
     *
     * @param data 字符串
     * @return 字节数组
     */
    byte[] serialize(String data);

}
