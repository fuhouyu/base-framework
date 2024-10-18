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

package com.fuhouyu.framework.security.token;

import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.Serializer;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import com.esotericsoftware.kryo.kryo5.objenesis.strategy.StdInstantiatorStrategy;
import com.esotericsoftware.kryo.kryo5.util.Pool;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * <p>
 * kryo序列化
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 21:33
 */
public class KryoTokenSerializer implements TokenStoreSerializationStrategy {

    /**
     * 空白数组，如果序列化对象为空时，则返回该值
     */
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    private final Pool<Kryo> kryoPool;

    /**
     * 初始化kryo
     */
    public KryoTokenSerializer() {
        this.kryoPool = new Pool<>(true, false, 8) {
            @Override
            protected Kryo create() {
                Kryo kryo = new Kryo();
                // Configure the Kryo instance.
                kryo.setRegistrationRequired(false);
                // 设置初始化策略，如果没有默认无参构造器，那么就需要设置此项,使用此策略构造一个无参构造器
                Set<?> unmodifiableSet = Collections.unmodifiableSet(new HashSet<>(0));
                List<?> unmodifiableList = Collections.unmodifiableList(new ArrayList<>(0));
                kryo.register(unmodifiableSet.getClass(),
                        new CollectionSerializer(unmodifiableSet));
                kryo.register(unmodifiableList.getClass(),
                        new CollectionSerializer(unmodifiableList));
                kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

                return kryo;
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] bytes) {
        if (Objects.isNull(bytes)) {
            return null;
        }
        Kryo kryo = kryoPool.obtain();
        try (Input input = new Input(new ByteArrayInputStream(bytes))) {
            return (T) kryo.readClassAndObject(input);
        } finally {
            kryoPool.free(kryo);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (Objects.isNull(bytes)) {
            return null;
        }
        Kryo kryo = kryoPool.obtain();
        try (Input input = new Input(new ByteArrayInputStream(bytes))) {
            return kryo.readObject(input, clazz);
        } finally {
            kryoPool.free(kryo);
        }
    }

    @Override
    public String deserializeString(byte[] bytes) {
        if (Objects.isNull(bytes)) {
            return null;
        }
        Kryo kryo = kryoPool.obtain();
        try (Input input = new Input(new ByteArrayInputStream(bytes))) {
            return kryo.readObject(input, String.class);
        } finally {
            kryoPool.free(kryo);
        }
    }

    @Override
    public byte[] serialize(Object object) {
        if (Objects.isNull(object)) {
            return EMPTY_BYTE_ARRAY;
        }
        Kryo kryo = kryoPool.obtain();
        try (Output output = new Output(new ByteArrayOutputStream())) {
            kryo.writeClassAndObject(output, object);
            return output.toBytes();
        } finally {
            kryoPool.free(kryo);
        }
    }

    @Override
    public byte[] serialize(String data) {
        if (!StringUtils.hasLength(data)) {
            return EMPTY_BYTE_ARRAY;
        }
        Kryo kryo = kryoPool.obtain();
        try (Output output = new Output(new ByteArrayOutputStream())) {
            kryo.writeClassAndObject(output, String.class);
            return output.toBytes();
        } finally {
            kryoPool.free(kryo);
        }
    }


    /**
     * 集合序列化处理
     */
    static class CollectionSerializer extends Serializer<Collection<?>> {

        private final Collection<?> defaultCollection;

        public CollectionSerializer(Collection<?> defaultCollection) {
            this.defaultCollection = defaultCollection;
        }

        @Override
        public void write(Kryo kryo, Output output, Collection<?> object) {
            if (CollectionUtils.isEmpty(object)) {
                kryo.writeObjectOrNull(output, null, object.getClass());
            } else {
                kryo.writeObject(output, object);
            }
        }

        @Override
        public Collection<?> read(Kryo kryo, Input input, Class<? extends Collection<?>> type) {
            // 在这里处理空集合的反序列化
            // 其他属性的反序列化...
            Collection<?> collection = kryo.readObjectOrNull(input, type);
            return collection == null ? defaultCollection : collection;
        }
    }

}
