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
module base.framework.security.starter {
    requires transitive base.framework.cache.starter;
    requires transitive base.framework.context;
    requires transitive com.fasterxml.jackson.annotation;
    requires cn.hutool.crypto;
    requires com.esotericsoftware.kryo.kryo5;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires spring.security.core;
    requires spring.security.crypto;
    requires spring.security.oauth2.core;
    requires spring.data.redis;
    requires spring.web;
    opens com.fuhouyu.framework.security to spring.core;
    opens com.fuhouyu.framework.security.entity to com.esotericsoftware.kryo.kryo5;
    exports com.fuhouyu.framework.security.core.authentication.refreshtoken to com.fasterxml.jackson.databind;

    exports com.fuhouyu.framework.security;
    exports com.fuhouyu.framework.security.core;
    exports com.fuhouyu.framework.security.entity;
    exports com.fuhouyu.framework.security.properties;
    exports com.fuhouyu.framework.security.serializer;
    exports com.fuhouyu.framework.security.token;
}