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

module base.framework.common {
    // 以下依赖都需要传递给其依赖项
    requires transitive lombok;
    requires transitive com.fasterxml.jackson.datatype.jsr310;
    requires transitive jakarta.servlet;
    requires transitive org.apache.commons.lang3;
    requires transitive org.slf4j;
    requires transitive com.fasterxml.jackson.databind;

    exports com.fuhouyu.framework.common;
    exports com.fuhouyu.framework.common.constants;
    exports com.fuhouyu.framework.common.exception;
    exports com.fuhouyu.framework.common.function;
    exports com.fuhouyu.framework.common.response;
    exports com.fuhouyu.framework.common.service;
    exports com.fuhouyu.framework.common.utils;
}