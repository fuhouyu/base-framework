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
module base.framework.resource.starter {
    requires aliyun.java.sdk.core;
    requires aliyun.sdk.oss;
    requires transitive base.framework.common;
    requires transitive com.fasterxml.jackson.core;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;

    opens com.fuhouyu.framework.resource to spring.core;

    exports com.fuhouyu.framework.resource;
    exports com.fuhouyu.framework.resource.constants;
    exports com.fuhouyu.framework.resource.enums;
    exports com.fuhouyu.framework.resource.exception;
    exports com.fuhouyu.framework.resource.model;
    exports com.fuhouyu.framework.resource.properties;
    exports com.fuhouyu.framework.resource.service;
}