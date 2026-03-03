/*
 * Copyright 2024-present fuhouyu.
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
module base.framework.cache.starter {
    // 透传
    requires static spring.data.redis;
    requires transitive base.framework.common;
    requires transitive com.fasterxml.jackson.annotation;
    requires transitive com.fasterxml.jackson.databind;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.tx;
    requires org.jspecify;
    requires spring.boot.data.redis;
    requires tools.jackson.databind;
    requires spring.beans;
    requires spring.security.core;

    // 需要导出的类
    exports com.fuhouyu.framework.cache;
    exports com.fuhouyu.framework.cache.enums;
    exports com.fuhouyu.framework.cache.properties;
    exports com.fuhouyu.framework.cache.service;
    exports com.fuhouyu.framework.cache.service.impl;

    uses com.fuhouyu.framework.cache.service.CacheService;
    opens com.fuhouyu.framework.cache to spring.core;

}