/*
 * Copyright 2024-2025 fuhouyu.
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
import com.fuhouyu.framework.cache.RedisCacheConfiguration;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 基类测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/5 20:51
 */
@SpringBootTest(classes = {
        RedisCacheConfiguration.class,
        BaseTest.BaseComponent.class,
        CacheAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        OAuth2ClientAutoConfiguration.class,
        AuthenticationAutoConfiguration.class
})
@ActiveProfiles("test")
@EnableWebSecurity
abstract class BaseTest {

    @SuppressWarnings("resource")
    static final GenericContainer<?> REDIS_GENERIC_CONTAINER =
            new GenericContainer<>(DockerImageName.parse("redis:7.2-alpine"))
                    .withCommand("redis-server --requirepass password")
                    .withExposedPorts(6379);

    static {
        REDIS_GENERIC_CONTAINER.start();
        System.setProperty("spring.data.redis.host", REDIS_GENERIC_CONTAINER.getHost());
        System.setProperty("spring.data.redis.port", REDIS_GENERIC_CONTAINER.getMappedPort(6379).toString());
    }

    @TestComponent
    static class BaseComponent {

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        public UserDetailsService userDetailsService() {
            UserDetails userDetails = new UserDetails() {
                @Override
                public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
                    return List.of();
                }

                @Override
                public String getPassword() {
                    return "{sm3}$3mb29qZzcuSEhKSnU1LkpRbgQk6/3N6wriraK7V5V0SE74tuRB7TVNRiigXOiMu3JNE";
                }

                @Override
                public @NonNull String getUsername() {
                    return "admin";
                }
            };
            return new InMemoryUserDetailsManager(userDetails);
        }
    }
}
