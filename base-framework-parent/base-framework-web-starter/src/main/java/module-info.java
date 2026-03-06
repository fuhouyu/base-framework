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
module base.framework.web.starter {
    requires transitive base.framework.cache.starter;
    requires transitive base.framework.context;
    requires base.framework.kms.starter;
    requires transitive jakarta.servlet;
    requires transitive jakarta.validation;
    requires io.swagger.v3.oas.annotations;
    requires org.aspectj.weaver;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires spring.tx;
    requires spring.web;
    requires spring.webmvc;
    requires ip2region;
    requires org.jspecify;
    requires tools.jackson.databind;
    requires spring.webflux;

    exports com.fuhouyu.framework.web;
    exports com.fuhouyu.framework.web.annotaions;
    exports com.fuhouyu.framework.web.constants;
    exports com.fuhouyu.framework.web.controller;
    exports com.fuhouyu.framework.web.components;
    exports com.fuhouyu.framework.web.properties;
    exports com.fuhouyu.framework.web.model;

    opens com.fuhouyu.framework.web to spring.core;
    opens com.fuhouyu.framework.web.controller to spring.core;
    opens com.fuhouyu.framework.web.properties to spring.beans;
}