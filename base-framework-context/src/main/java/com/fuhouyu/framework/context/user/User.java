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

package com.fuhouyu.framework.context.user;

import java.util.Map;

/**
 * <p>
 * 用户接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 09:38
 */
public interface User {

    /**
     * 用户的主键id
     *
     * @return 主键id
     */
    Long getId();

    /**
     * 登录的用户名
     *
     * @return 用户名
     */
    String getUsername();

    /**
     * 用户的真实姓名，可能为空
     *
     * @return 真实姓名
     */
    String getRealName();

    /**
     * 用户昵称，可能为空
     *
     * @return 用户昵称
     */
    String getNickname();

    /**
     * 用户性别，可能为空
     *
     * @return 用户性别
     */
    String getGender();

    /**
     * 第三方账号id，用户为第三方登录时存在该值
     *
     * @return 第三方账号id
     */
    String getRefAccountId();


    /**
     * 获取用户扩展信息
     */
    Map<String, Object> getAdditionalInformation();


}
