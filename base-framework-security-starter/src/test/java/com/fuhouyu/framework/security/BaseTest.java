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
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

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
        BaseTest.BaseComponent.class,
        CacheAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        OAuth2ClientAutoConfiguration.class,
        AuthenticationAutoConfiguration.class
})
@TestPropertySource(locations = {"classpath:application.yaml"})
@EnableWebSecurity
abstract class BaseTest {


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
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return List.of();
                }

                @Override
                public String getPassword() {
                    return "{noop}admin";
                }

                @Override
                public String getUsername() {
                    return "admin";
                }
            };
            return new InMemoryUserDetailsManager(userDetails);
        }
    }
}
