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
module base.framework.s3.starter {
    requires transitive base.framework.common;
    requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.annotation;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires software.amazon.awssdk.services.s3;
    requires software.amazon.awssdk.auth;
    requires software.amazon.awssdk.regions;
    requires software.amazon.awssdk.services.sts;
    requires aliyun.java.sdk.core;

    opens com.fuhouyu.framework.s3 to spring.core, com.fasterxml.jackson.databind;
    opens com.fuhouyu.framework.s3.properties to spring.beans;

    exports com.fuhouyu.framework.s3;
    exports com.fuhouyu.framework.s3.properties;
    exports com.fuhouyu.framework.s3.enums;
    exports com.fuhouyu.framework.s3.service.impl;
    exports com.fuhouyu.framework.s3.model;
    opens com.fuhouyu.framework.s3.service.impl to com.fasterxml.jackson.databind, spring.core;

}