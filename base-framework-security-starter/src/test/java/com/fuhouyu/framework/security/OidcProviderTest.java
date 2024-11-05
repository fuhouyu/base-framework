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

import com.fuhouyu.framework.security.core.provider.oidc.OidcAuthenticationToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

/**
 * <p>
 * 测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/4 22:12
 */
@Disabled
class OidcProviderTest extends BaseTest {

    private static final String CODE = "17896f8f71b37868e2e399d814f53ca06e3c4f6a94dc3132d688f0b8b767c907";

    private static final String STATE = "-02_gJnVuE_n-rsQilIWRM82bBBXxO9z5VdIfSDlfAU=";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    void testOidc() {
        OidcAuthenticationToken oidcAuthenticationToken = new OidcAuthenticationToken(CODE, STATE, "gitlab");
        Authentication authenticate = authenticationManager.authenticate(oidcAuthenticationToken);
        Assertions.assertNotNull(authenticate);
        Assertions.assertTrue(authenticate.isAuthenticated());
    }
}
