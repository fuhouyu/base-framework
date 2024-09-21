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

package com.fuhouyu.framework.web.filter;

/**
 * <p>
 * http body 过滤器接口，
 * 针对http post 发起的请求, 对body进行加解密的处理操作
 * 方法上需要有{@link org.springframework.web.bind.annotation.PostMapping}
 * 和{@link com.fuhouyu.framework.web.annotaions.PrepareHttpBody}注解
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/22 11:20
 */
public interface HttpBodyFilter {


    /**
     * 对已经加密过的字节数组进行解密
     *
     * @param encryptBodyBytes 加密的body字节数组
     * @return 解密后的字节数组对象
     */
    byte[] decryptionBody(byte[] encryptBodyBytes);


    /**
     * 对原始数据进行加密
     *
     * @param originBody 原始body
     * @return 加密后的数据
     */
    byte[] encryptionBody(byte[] originBody);
}
