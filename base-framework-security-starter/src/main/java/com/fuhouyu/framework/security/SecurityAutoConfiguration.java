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

package com.fuhouyu.framework.security;

import com.fuhouyu.framework.cache.CacheAutoConfiguration;
import com.fuhouyu.framework.cache.service.CacheService;
import com.fuhouyu.framework.security.token.TokenStore;
import com.fuhouyu.framework.security.token.TokenStoreCache;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * 自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 16:22
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({
        CacheAutoConfiguration.class,
        OAuth2ClientAutoConfiguration.class
})
@Import({AuthenticationConfiguration.class})
public class SecurityAutoConfiguration {

    /**
     * redisToken存储.
     *
     * @param cacheService 缓存对象
     * @return token存储.
     */
    @Bean
    @ConditionalOnMissingBean(TokenStore.class)
    @ConditionalOnBean(CacheService.class)
    public TokenStore tokenStore(CacheService<String, Object> cacheService) {
        return new TokenStoreCache("user", cacheService);
    }

}
