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
package com.fuhouyu.framework.common.desensitize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.Serial;

/**
 * <p>
 * 数据脱敏
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/30 18:00
 */
@Setter
@Getter
public class ObjectDesensitizeSerializer extends StdSerializer<Object> implements ContextualSerializer {

    @Serial
    private static final long serialVersionUID = -65412353416584123L;

    private transient Desensitization<Object> desensitization;

    protected ObjectDesensitizeSerializer() {
        super(Object.class);
    }


    @Override
    @SuppressWarnings("unchecked")
    public JsonSerializer<Object> createContextual(SerializerProvider prov, BeanProperty property) {
        Desensitize annotation = property.getAnnotation(Desensitize.class);
        ObjectDesensitizeSerializer serializer = new ObjectDesensitizeSerializer();
        Class<? extends Desensitization<?>> clazz = annotation.desensitization();
        if (clazz != DefaultDesensitization.class) {
            serializer.setDesensitization((Desensitization<Object>) DesensitizationFactory.getDesensitization(clazz));
        }
        return serializer;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        Desensitization<Object> objectDesensitization = getDesensitization();
        if (objectDesensitization != null) {
            try {
                gen.writeObject(objectDesensitization.desensitize(value));
            } catch (Exception e) {
                gen.writeObject(value);
            }
        } else if (value instanceof String valueString) {
            gen.writeString("*".repeat(valueString.length()));
        } else {
            gen.writeObject(value);
        }
    }
}
