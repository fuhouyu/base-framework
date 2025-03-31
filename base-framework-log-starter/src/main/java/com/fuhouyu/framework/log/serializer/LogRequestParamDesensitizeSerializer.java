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
package com.fuhouyu.framework.log.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * <p>
 * 日志参数脱敏
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/31 23:08
 */
public class LogRequestParamDesensitizeSerializer extends StdSerializer<String> {

    public LogRequestParamDesensitizeSerializer() {
        super(String.class);
    }

    @Override
    public void serialize(String s, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeString("*".repeat(8));

    }
}
