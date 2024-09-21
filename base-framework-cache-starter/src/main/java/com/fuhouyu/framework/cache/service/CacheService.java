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

package com.fuhouyu.framework.cache.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 通用缓存服务接口，支持多种数据类型的缓存操作，适用于不同的缓存实现。
 *
 * @param <K> 缓存键的类型
 * @param <V> 缓存值的类型
 * @author fuhouyu
 */
public interface CacheService<K, V> {

    // ===== String Operations =====

    /**
     * 设置字符串值到缓存中。
     *
     * @param key   缓存键
     * @param value 缓存值
     */
    void set(byte[] key, byte[] value);

    /**
     * 设置字符串值到缓存中。
     *
     * @param key   缓存键
     * @param value 缓存值
     */
    void set(K key, V value);

    /**
     * 设置字符串值到缓存中，并指定过期时间。
     *
     * @param key     缓存键
     * @param value   缓存值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    void set(K key, V value, long timeout, TimeUnit unit);

    /**
     * 设置字符串值到缓存中，并指定过期时间。
     *
     * @param key     缓存键
     * @param value   缓存值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    void set(byte[] key, byte[] value, long timeout, TimeUnit unit);

    /**
     * 获取缓存中的字符串值。
     *
     * @param key 缓存键
     * @return 缓存值
     */
    V get(K key);

    /**
     * 获取缓存中的字节数组。
     *
     * @param key 缓存键
     * @return 缓存值
     */
    byte[] get(byte[] key);

    /**
     * 删除缓存中的字符串值。
     *
     * @param key 缓存键
     */
    void delete(K key);

    /**
     * 删除缓存中的字符串值。
     *
     * @param key 缓存键
     */
    void delete(byte[] key);

    // ===== Hash Operations =====

    /**
     * 将哈希值放入缓存中。
     *
     * @param key     缓存键
     * @param hashKey 哈希键
     * @param value   哈希值
     */
    void putHash(K key, K hashKey, V value);

    /**
     * 将哈希值放入缓存中，并指定过期时间。
     *
     * @param key     缓存键
     * @param hashKey 哈希键
     * @param value   哈希值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    void putHash(K key, K hashKey, V value, long timeout, TimeUnit unit);

    /**
     * 获取缓存中的哈希值。
     *
     * @param key     缓存键
     * @param hashKey 哈希键
     * @return 哈希值
     */
    V getHash(K key, K hashKey);

    /**
     * 删除缓存中的哈希值。
     *
     * @param key     缓存键
     * @param hashKey 哈希键
     */
    void deleteHash(K key, K hashKey);

    /**
     * 批量获取缓存中的哈希值。
     *
     * @param key 缓存键
     * @return 哈希键值对
     */
    Map<K, V> getHashAll(K key);

    /**
     * 批量设置缓存中的哈希值。
     *
     * @param key 缓存键
     * @param map 哈希键值对
     */
    void putHashAll(K key, Map<K, V> map);

    /**
     * 批量设置缓存中的哈希值，并指定过期时间。
     *
     * @param key     缓存键
     * @param map     哈希键值对
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    void putHashAll(K key, Map<K, V> map, long timeout, TimeUnit unit);

    // ===== List Operations =====

    /**
     * 将值加入列表缓存中。
     *
     * @param key   缓存键
     * @param value 列表值
     */
    void pushToList(K key, V value);

    /**
     * 将值加入列表缓存中，并指定过期时间。
     *
     * @param key     缓存键
     * @param value   列表值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    void pushToList(K key, V value, long timeout, TimeUnit unit);

    /**
     * 从列表缓存中弹出一个值。
     *
     * @param key 缓存键
     * @return 列表值
     */
    V popFromList(K key);

    /**
     * 获取列表缓存中的所有值。
     *
     * @param key 缓存键
     * @return 列表值
     */
    List<V> getList(K key);

    // ===== Set Operations =====

    /**
     * 将值加入集合缓存中。
     *
     * @param key   缓存键
     * @param value 集合值
     */
    void addToSet(K key, V value);

    /**
     * 将值加入集合缓存中，并指定过期时间。
     *
     * @param key     缓存键
     * @param value   集合值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    void addToSet(K key, V value, long timeout, TimeUnit unit);

    /**
     * 获取集合缓存中的所有值。
     *
     * @param key 缓存键
     * @return 集合值
     */
    Set<V> getSet(K key);

    /**
     * 从集合缓存中移除指定值。
     *
     * @param key   缓存键
     * @param value 要移除的值
     */
    void removeFromSet(K key, V value);

    // ===== Common Operations =====

    /**
     * 检查缓存中是否存在指定键。
     *
     * @param key 缓存键
     * @return 如果存在则返回 true，否则返回 false
     */
    boolean exists(K key);

    /**
     * 设置缓存键的过期时间。
     *
     * @param key     缓存键
     * @param timeout 过期时间（秒）
     */
    void expire(K key, long timeout);

    /**
     * 设置缓存键的过期时间，并指定时间单位。
     *
     * @param key     缓存键
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    void expire(K key, long timeout, TimeUnit unit);

    /**
     * 获取缓存中的键值对总数。
     *
     * @param key 缓存键
     * @return 键值对总数
     */
    long size(K key);

    /**
     * 删除缓存中的多个键值对。
     *
     * @param keys 要删除的缓存键集合
     */
    void deleteMultiple(Set<K> keys);
}
