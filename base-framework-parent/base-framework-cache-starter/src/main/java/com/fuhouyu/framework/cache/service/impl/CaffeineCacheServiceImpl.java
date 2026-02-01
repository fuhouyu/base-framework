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

package com.fuhouyu.framework.cache.service.impl;


import com.fuhouyu.framework.cache.service.CacheService;
import com.fuhouyu.framework.common.utils.NumberFormatUtil;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * Caffeine 缓存实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 13:31
 */
public class CaffeineCacheServiceImpl<K, V> implements CacheService<K, V> {

    private final Cache<K, V> cache;

    public CaffeineCacheServiceImpl(Cache<K, V> cache) {
        this.cache = cache;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(byte[] key, byte[] value) {
        cache.put((K) new String(key),
                (V) value);
    }

    @Override
    public void set(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void set(K key, V value, long timeout, TimeUnit unit) {
        this.addPolicyExpireTime(key, value, timeout, unit);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(byte[] key, byte[] value, long timeout, TimeUnit unit) {
        this.addPolicyExpireTime((K) new String(key), (V) value, timeout, unit);
    }

    @Override
    public V get(K key) {
        return cache.getIfPresent(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public byte[] get(byte[] key) {
        return (byte[]) cache.getIfPresent((K) new String(key));
    }

    @Override
    public void delete(K key) {
        cache.invalidate(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(byte[] key) {
        cache.invalidate((K) new String(key));
    }

    @Override
    public void putHash(K key, K hashKey, V value) {
        Map<K, V> v = this.getHashByCache(key);
        v.put(hashKey, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void putHash(K key, K hashKey, V value, long timeout, TimeUnit unit) {
        Map<K, V> v = this.getHashByCache(key);
        v.put(hashKey, value);
        this.addPolicyExpireTime(key, (V) v, timeout, unit);
    }

    @Override
    public V getHash(K key, K hashKey) {
        Map<K, V> v = this.getHashByCache(key);
        return v.get(hashKey);
    }


    @Override
    public void deleteHash(K key, K hashKey) {
        this.getHashByCache(key).remove(hashKey);
    }

    @Override
    public Map<K, V> getHashAll(K key) {
        return this.getHashByCache(key);
    }

    @Override
    public void putHashAll(K key, Map<K, V> map) {
        Map<K, V> v = this.getHashByCache(key);
        v.putAll(map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void putHashAll(K key, Map<K, V> map, long timeout, TimeUnit unit) {
        Map<K, V> v = this.getHashByCache(key);
        v.putAll(map);
        this.addPolicyExpireTime(key, (V) v, timeout, unit);


    }

    @Override
    public void pushToList(K key, V value) {
        this.getListByCache(key).add(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void pushToList(K key, V value, long timeout, TimeUnit unit) {
        List<V> v = this.getListByCache(key);
        this.addPolicyExpireTime(key, (V) v, timeout, unit);

    }

    @Override
    public V popFromList(K key) {
        List<V> v = this.getListByCache(key);
        return v.removeFirst();
    }

    @Override
    public List<V> getList(K key) {
        return this.getListByCache(key);
    }

    @Override
    public void addToSet(K key, V value) {
        this.getSetByCache(key).add(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addToSet(K key, V value, long timeout, TimeUnit unit) {
        Set<V> v = this.getSetByCache(key);
        this.addPolicyExpireTime(key, (V) v, timeout, unit);
    }

    @Override
    public Set<V> getSet(K key) {
        return this.getSetByCache(key);
    }

    @Override
    public void removeFromSet(K key, V value) {
        this.getSetByCache(key).remove(value);
    }

    @Override
    public boolean exists(K key) {
        return !this.getSetByCache(key).isEmpty();
    }

    @Override
    public void expire(K key, long timeout) {
        this.expire(key, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void expire(K key, long timeout, TimeUnit unit) {
        cache.policy().expireVariably()
                .ifPresent(e -> e.setExpiresAfter(key, timeout, unit));
    }

    @Override
    public long size(K key) {
        throw new UnsupportedOperationException("当前缓存不支持");
    }

    @Override
    public void deleteMultiple(Set<K> keys) {
        cache.invalidateAll(keys);
    }

    /**
     * 从缓存中获取hash
     *
     * @param key 键
     * @return hash
     */
    @SuppressWarnings("unchecked")
    private Map<K, V> getHashByCache(K key) {
        V v = cache.getIfPresent(key);
        if (Objects.isNull(v)) {
            Map<K, V> map = new HashMap<>();
            cache.put(key, (V) map);
            return map;
        }
        return (Map<K, V>) v;
    }

    /**
     * 从缓存中获取List
     *
     * @param key 键
     * @return hash
     */
    @SuppressWarnings("unchecked")
    private List<V> getListByCache(K key) {
        V v = cache.getIfPresent(key);
        if (Objects.isNull(v)) {
            List<V> list = new LinkedList<>();
            cache.put(key, (V) list);
            return list;
        }
        return (List<V>) v;
    }

    /**
     * 从缓存中获取Set
     *
     * @param key 键
     * @return hash
     */
    @SuppressWarnings("unchecked")
    private Set<V> getSetByCache(K key) {
        V v = cache.getIfPresent(key);
        if (Objects.isNull(v)) {
            Set<V> set = new HashSet<>();
            cache.put(key, (V) set);
            return set;
        }
        return (Set<V>) v;
    }

    @Override
    public Set<K> keys(K keyPrefix) {
        return cache.asMap().keySet().stream()
                .filter(k -> ((String) k).startsWith((String) keyPrefix))
                .collect(Collectors.toSet());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<byte[]> keys(byte[] keyPrefix) {
        return (Set<byte[]>) cache.asMap().keySet().stream()
                .filter(key -> startsWith((byte[]) key, keyPrefix))
                .collect(Collectors.toSet());
    }

    @Override
    @SuppressWarnings("unchecked")
    public long increment(K key, long delta) {
        Object result = cache.asMap().compute(key, (k, v) -> {
            long currentValue = (v instanceof Number) ? ((Number) v).longValue() : 0L;
            return (V) Long.valueOf(currentValue + delta);
        });
        return ((Number) result).longValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public long decrement(K key, long delta) {
        Object result = cache.asMap().compute(key, (k, v) -> {
            long currentValue = 0L;
            if (v instanceof Number) {
                currentValue = ((Number) v).longValue();
            } else if (v instanceof String numberStr) {
                currentValue = NumberFormatUtil.toLong(numberStr, 0L);
            }
            long newValue = currentValue - delta;

            return (V) Long.valueOf(newValue);
        });

        return ((Number) result).longValue();
    }

    @Override
    public boolean setIfAbsent(K key, V value) {
        V existing = cache.getIfPresent(key);
        if (existing == null) {
            cache.put(key, value);
            return true;
        }
        return false;
    }

    @Override
    public boolean setIfAbsent(K key, V value, long timeout, TimeUnit unit) {
        V existing = cache.getIfPresent(key);
        if (existing == null) {
            this.addPolicyExpireTime(key, value, timeout, unit);
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addToZSet(K key, V value, double score) {
        NavigableSet<ZSetEntry<V>> zset = (NavigableSet<ZSetEntry<V>>) cache.asMap()
                .computeIfAbsent(key, k -> (V) new ConcurrentSkipListSet<ZSetEntry<V>>());
        ZSetEntry<V> entry = new ZSetEntry<>(value, score);
        zset.remove(entry);
        zset.add(entry);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<V> rangeFromZSet(K key, long start, long end) {
        V v = cache.getIfPresent(key);
        if (!(v instanceof NavigableSet)) {
            return Collections.emptySet();
        }

        NavigableSet<ZSetEntry<V>> zSet = (NavigableSet<ZSetEntry<V>>) v;
        // 分页截取
        return zSet.stream()
                .skip(Math.max(0, start))
                .limit(end < 0 ? Long.MAX_VALUE : (end - start + 1))
                .map(ZSetEntry::getValue)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeFromZSet(K key, V... values) {
        V v = cache.getIfPresent(key);
        if (v instanceof NavigableSet && values != null) {
            NavigableSet<ZSetEntry<V>> zSet = (NavigableSet<ZSetEntry<V>>) v;
            for (V val : values) {
                zSet.removeIf(entry -> Objects.equals(entry.getValue(), val));
            }
        }
    }

    @Override
    public long getExpire(K key, TimeUnit unit) {
        return cache.policy().expireVariably()
                // 2. map 返回 Optional<OptionalLong>
                .map(policy -> policy.getExpiresAfter(key, unit))
                // 3. 如果 policy 存在，处理 OptionalLong；否则返回 -2
                .map(optionalLong -> optionalLong.orElse(-1L))
                .orElse(-2L);
    }

    /**
     * 添加键值过期时间
     *
     * @param key     键
     * @param value   过期
     * @param timeout 过期时间
     * @param unit    时间格式
     */
    private void addPolicyExpireTime(K key, V value, long timeout, TimeUnit unit) {
        cache.policy().expireVariably()
                .ifPresent(e -> {
                            V ignored = e.put(key, value, timeout, unit);
                        }
                );
    }

    /**
     * 判断一个 byte[] 是否以另一个 byte[] 为前缀
     */
    private boolean startsWith(byte[] full, byte[] prefix) {
        if (full == null || prefix == null || full.length < prefix.length) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (full[i] != prefix[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * ZSet 内部存储条目
     */
    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode(of = "value")
    private static class ZSetEntry<V> implements Comparable<ZSetEntry<V>> {
        private final V value;
        private final double score;

        @Override
        public int compareTo(ZSetEntry<V> o) {
            // 先按分数排序
            int comp = Double.compare(this.score, o.score);
            if (comp != 0) {
                return comp;
            }
            // 分数相同时，按值的 toString 排序（保证唯一性）
            return Integer.compare(System.identityHashCode(this.value), System.identityHashCode(o.value));
        }
    }
}
