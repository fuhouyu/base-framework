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

package com.fuhouyu.framework.cache.service.impl;

import com.fuhouyu.framework.cache.service.CacheService;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.types.Expiration;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 基于 RedisTemplate 的缓存服务实现。
 *
 * @param <K> 缓存键的类型
 * @param <V> 缓存值的类型
 * @author fuhouyu
 */
public class RedisCacheService<K, V> implements CacheService<K, V> {

    private final RedisTemplate<K, V> redisTemplate;

    private final HashOperations<K, K, V> hashOperations;

    private final ListOperations<K, V> listOperations;

    private final SetOperations<K, V> setOperations;

    public RedisCacheService(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.listOperations = redisTemplate.opsForList();
        this.setOperations = redisTemplate.opsForSet();
    }

    // ===== String Operations =====

    @Override
    public void set(byte[] key, byte[] value) {
        this.doExecute(redisConnection -> {
            redisConnection.stringCommands().set(key, value);
            return value;
        });
    }


    @Override
    public void set(K key, V value) {

        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(K key, V value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public void set(byte[] key, byte[] value, long timeout, TimeUnit unit) {
        this.doExecute(redisConnection -> {
            redisConnection.stringCommands().set(key, value, Expiration.from(timeout, unit), RedisStringCommands.SetOption.UPSERT);
            return null;
        });
    }

    @Override
    public V get(K key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public byte[] get(byte[] key) {
        return this.doExecute(redisConnection -> redisConnection.stringCommands().get(key));
    }

    @Override
    public void delete(K key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delete(byte[] key) {
        this.doExecute(redisConnection ->
                redisConnection.stringCommands().getDel(key)
        );

    }

    // ===== Hash Operations =====

    @Override
    public void putHash(K key, K hashKey, V value) {
        hashOperations.put(key, hashKey, value);
    }

    @Override
    public void putHash(K key, K hashKey, V value, long timeout, TimeUnit unit) {
        hashOperations.put(key, hashKey, value);
        redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public V getHash(K key, K hashKey) {
        return hashOperations.get(key, hashKey);
    }

    @Override
    public void deleteHash(K key, K hashKey) {
        hashOperations.delete(key, hashKey);
    }

    @Override
    public Map<K, V> getHashAll(K key) {
        return hashOperations.entries(key);
    }

    @Override
    public void putHashAll(K key, Map<K, V> map) {
        hashOperations.putAll(key, map);
    }

    @Override
    public void putHashAll(K key, Map<K, V> map, long timeout, TimeUnit unit) {
        hashOperations.putAll(key, map);
        redisTemplate.expire(key, timeout, unit);
    }

    // ===== List Operations =====

    @Override
    public void pushToList(K key, V value) {
        listOperations.rightPush(key, value);
    }

    @Override
    public void pushToList(K key, V value, long timeout, TimeUnit unit) {
        listOperations.rightPush(key, value);
        redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public V popFromList(K key) {
        return listOperations.leftPop(key);
    }

    @Override
    public List<V> getList(K key) {
        return listOperations.range(key, 0, -1);
    }

    // ===== Set Operations =====

    @Override
    @SuppressWarnings("unchecked")
    public void addToSet(K key, V value) {
        setOperations.add(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addToSet(K key, V value, long timeout, TimeUnit unit) {
        setOperations.add(key, value);
        redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public Set<V> getSet(K key) {
        return setOperations.members(key);
    }

    @Override
    public void removeFromSet(K key, V value) {
        setOperations.remove(key, value);
    }

    // ===== Common Operations =====

    @Override
    public boolean exists(K key) {
        return Objects.isNull(redisTemplate.hasKey(key));
    }

    @Override
    public void expire(K key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void expire(K key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public long size(K key) {
        Long result = redisTemplate.opsForValue().size(key);
        return Objects.isNull(result) ? 0 : result;
    }

    @Override
    public void deleteMultiple(Set<K> keys) {
        redisTemplate.delete(keys);
    }

    private byte[] doExecute(Function<RedisConnection, byte[]> redisConnectionFunction) {
        return redisTemplate.opsForValue().getOperations()
                .execute((RedisCallback<byte[]>) connection -> {
                    try (connection) {
                        return redisConnectionFunction.apply(connection);
                    }
                });
    }
}
