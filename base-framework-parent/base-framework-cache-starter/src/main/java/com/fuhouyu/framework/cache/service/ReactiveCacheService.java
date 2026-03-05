package com.fuhouyu.framework.cache.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 响应式通用缓存服务接口，支持全链路非阻塞操作。
 * 适用于 Spring Cloud Gateway 或 WebFlux 环境。
 * </p>
 *
 * @author fuhouyu
 * @since 2026/3/5 21:36
 */
public interface ReactiveCacheService<K, V> {

    /**
     * 设置缓存值
     *
     * @param key   缓存键
     * @param value 缓存值
     * @return 异步完成信号
     */
    Mono<Void> set(K key, V value);

    /**
     * 设置缓存值并指定过期时间
     *
     * @param key     缓存键
     * @param value   缓存值
     * @param timeout 过期时间（Duration 表达）
     * @return 异步完成信号
     */
    Mono<Void> set(K key, V value, Duration timeout);

    /**
     * 获取缓存值
     *
     * @param key 缓存键
     * @return 包含缓存值的 Mono，如果键不存在则返回 Mono.empty()
     */
    Mono<V> get(K key);

    /**
     * 删除指定的缓存键
     *
     * @param key 缓存键
     * @return 如果删除成功返回 true 的 Mono
     */
    Mono<Boolean> delete(K key);

    /**
     * 原子自增操作
     *
     * @param key   缓存键
     * @param delta 增量
     * @return 自增后的新值
     */
    Mono<Long> increment(K key, long delta);

    /**
     * 只有当键不存在时才设置值 (SET IF NOT EXISTS)
     *
     * @param key   缓存键
     * @param value 缓存值
     * @return 如果设置成功返回 true 的 Mono
     */
    Mono<Boolean> setIfAbsent(K key, V value);

    /**
     * 只有当键不存在时才设置值，并指定过期时间
     *
     * @param key     缓存键
     * @param value   缓存值
     * @param timeout 过期时间
     * @return 如果设置成功返回 true 的 Mono
     */
    Mono<Boolean> setIfAbsent(K key, V value, Duration timeout);

    /**
     * 将哈希值放入缓存中
     *
     * @param key     缓存键
     * @param hashKey 哈希内部键
     * @param value   哈希值
     * @return 是否成功放入
     */
    Mono<Boolean> putHash(K key, K hashKey, V value);

    /**
     * 获取哈希表中的特定值
     *
     * @param key     缓存键
     * @param hashKey 哈希内部键
     * @return 哈希值
     */
    Mono<V> getHash(K key, K hashKey);

    /**
     * 获取哈希表中的所有键值对
     *
     * @param key 缓存键
     * @return 包含所有键值对的 Map
     */
    Mono<Map<K, V>> getHashAll(K key);

    /**
     * 批量删除哈希表中的指定键
     *
     * @param key      缓存键
     * @param hashKeys 要删除的哈希内部键集合
     * @return 已删除的数量
     */
    @SuppressWarnings("unchecked")
    Mono<Long> deleteHash(K key, K... hashKeys);

    /**
     * 将值从右侧推入列表
     *
     * @param key   缓存键
     * @param value 元素值
     * @return 推入后的列表长度
     */
    Mono<Long> pushToList(K key, V value);

    /**
     * 从列表左侧弹出一个值
     *
     * @param key 缓存键
     * @return 弹出的元素
     */
    Mono<V> popFromList(K key);

    /**
     * 获取列表中的所有值
     *
     * @param key 缓存键
     * @return 元素流
     */
    Flux<V> getList(K key);

    /**
     * 将元素添加到集合中
     *
     * @param key    缓存键
     * @param values 要添加的元素
     * @return 添加成功的元素个数
     */
    @SuppressWarnings("unchecked")
    Mono<Long> addToSet(K key, V... values);

    /**
     * 获取集合中的所有成员
     *
     * @param key 缓存键
     * @return 成员流
     */
    Flux<V> getSet(K key);

    /**
     * 移除集合中的指定成员
     *
     * @param key    缓存键
     * @param values 要移除的成员
     * @return 移除成功的数量
     */
    @SuppressWarnings("unchecked")
    Mono<Long> removeFromSet(K key, V... values);

    /**
     * 向有序集合中添加元素
     *
     * @param key   缓存键
     * @param value 成员
     * @param score 分数
     * @return 是否成功添加
     */
    Mono<Boolean> addToZSet(K key, V value, double score);

    /**
     * 获取有序集合中指定排名范围的成员
     *
     * @param key   缓存键
     * @param start 开始排名
     * @param end   结束排名
     * @return 成员流
     */
    Flux<V> rangeFromZSet(K key, long start, long end);

    /**
     * 检查键是否存在
     *
     * @param key 缓存键
     * @return 存在的布尔值 Mono
     */
    Mono<Boolean> exists(K key);

    /**
     * 设置键的过期时间
     *
     * @param key     缓存键
     * @param timeout 过期时间
     * @return 设置成功返回 true
     */
    Mono<Boolean> expire(K key, Duration timeout);

    /**
     * 获取剩余生存时间（TTL）
     *
     * @param key 缓存键
     * @return 生存时间（单位：秒）
     */
    Mono<Long> getExpire(K key);

    /**
     * 批量删除缓存键
     *
     * @param keys 键集合
     * @return 已删除的数量
     */
    Mono<Long> deleteMultiple(Collection<K> keys);

    /**
     * 根据前缀模糊搜索键
     *
     * @param keyPrefix 键前缀
     * @return 匹配的键流
     */
    Flux<K> keys(K keyPrefix);
}
