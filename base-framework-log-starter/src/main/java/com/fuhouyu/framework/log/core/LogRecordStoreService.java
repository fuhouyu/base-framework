/*
 * Copyright 2012-2020 the original author or authors.
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

package com.fuhouyu.framework.log.core;

import com.fuhouyu.framework.log.model.LogRecord;

/**
 * <p>
 * 日志存储接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 11:56
 */
public interface LogRecordStoreService {

    /**
     * 保存日志记录
     *
     * @param logRecord 日志记录
     */
    void saveLogRecord(LogRecord logRecord);

}
