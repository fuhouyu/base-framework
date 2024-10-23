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
module base.framework.kms.starter {
    requires transitive base.framework.common;
    requires transitive org.bouncycastle.provider;

    requires cn.hutool.crypto;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;

    exports com.fuhouyu.framework.kms;
    exports com.fuhouyu.framework.kms.exception;
    exports com.fuhouyu.framework.kms.properties;
    exports com.fuhouyu.framework.kms.service;

    uses com.fuhouyu.framework.kms.service.KmsService;
    // spring 需要反射访问当前包
    opens com.fuhouyu.framework.kms to spring.core;
}