/*
 * Copyright 2024-2034 the original author or authors.
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

package com.fuhouyu.framework.security.authentication;

import com.fuhouyu.framework.cache.CacheAutoConfigure;
import com.fuhouyu.framework.security.SecurityAutoConfigure;
import com.fuhouyu.framework.security.core.authentication.wechat.WechatAppletsPlatformProvider;
import com.fuhouyu.framework.security.service.UserAuthService;
import com.fuhouyu.framework.security.token.DefaultOAuth2Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.Assert;

/**
 * <p>
 * 微信登录测试
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/24 22:08
 */
@SpringBootTest(classes = {
        CacheAutoConfigure.class,
        SecurityAutoConfigure.class,
})
@TestPropertySource(locations = {"classpath:application.yaml"})
@EnabledIfSystemProperty(named = "run.tests", matches = "true")
class OpenPlatformAuthenticationTest {

    private static final String USER_LOGIN_CODE = "0c1Vx30w3Yp4n33ZSJ1w3yuRMr1Vx30I";

    @Autowired
    private UserAuthService userAuthService;

    @Test
    void testWechatAppletLoginByCode() throws Exception {
        WechatAppletsPlatformProvider.WechatAppletsAuthenticationToken wechatAppletsAuthenticationToken = new WechatAppletsPlatformProvider.WechatAppletsAuthenticationToken(USER_LOGIN_CODE);
        DefaultOAuth2Token defaultOAuth2Token = userAuthService.userLogin(wechatAppletsAuthenticationToken);
        Assert.notNull(defaultOAuth2Token, "微信小程序登录失败");
    }
}
