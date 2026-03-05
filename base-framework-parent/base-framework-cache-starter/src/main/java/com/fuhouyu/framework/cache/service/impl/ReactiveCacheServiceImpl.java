package com.fuhouyu.framework.cache.service.impl;

import com.fuhouyu.framework.cache.service.ReactiveCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 响应式缓存实现
 * </p>
 *
 * @author fuhouyu
 * @since 2026/3/5 21:38
 */
@Slf4j
@RequiredArgsConstructor
public class ReactiveCacheServiceImpl<K, V> implements ReactiveCacheService<K, V> {

    private final ReactiveRedisTemplate<K, V> reactiveRedisTemplate;

    @Override
    public Mono<Void> set(K key, V value) {
        return reactiveRedisTemplate.opsForValue().set(key, value).then();
    }

    @Override
    public Mono<Void> set(K key, V value, Duration timeout) {
        return reactiveRedisTemplate.opsForValue().set(key, value, timeout).then();
    }

    @Override
    public Mono<V> get(K key) {
        return reactiveRedisTemplate.opsForValue().get(key);
    }

    @Override
    public Mono<Boolean> delete(K key) {
        return reactiveRedisTemplate.delete(key).map(l -> l > 0);
    }

    @Override
    public Mono<Long> increment(K key, long delta) {
        return reactiveRedisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public Mono<Boolean> setIfAbsent(K key, V value) {
        return reactiveRedisTemplate.opsForValue().setIfAbsent(key, value);
    }

    @Override
    public Mono<Boolean> setIfAbsent(K key, V value, Duration timeout) {
        return reactiveRedisTemplate.opsForValue().setIfAbsent(key, value, timeout);
    }

    @Override
    public Mono<Boolean> putHash(K key, K hashKey, V value) {
        return reactiveRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<V> getHash(K key, K hashKey) {
        return (Mono<V>) reactiveRedisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Map<K, V>> getHashAll(K key) {
        return reactiveRedisTemplate.opsForHash().entries(key)
                .collectMap(e -> (K) e.getKey(), e -> (V) e.getValue());
    }

    @Override
    @SafeVarargs
    public final Mono<Long> deleteHash(K key, K... hashKeys) {
        return reactiveRedisTemplate.opsForHash().remove(key, (Object[]) hashKeys);
    }

    @Override
    public Mono<Long> pushToList(K key, V value) {
        return reactiveRedisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public Mono<V> popFromList(K key) {
        return reactiveRedisTemplate.opsForList().leftPop(key);
    }

    @Override
    public Flux<V> getList(K key) {
        return reactiveRedisTemplate.opsForList().range(key, 0, -1);
    }

    @Override
    @SafeVarargs
    public final Mono<Long> addToSet(K key, V... values) {
        return reactiveRedisTemplate.opsForSet().add(key, values);
    }

    @Override
    public Flux<V> getSet(K key) {
        return reactiveRedisTemplate.opsForSet().members(key);
    }

    @Override
    @SafeVarargs
    public final Mono<Long> removeFromSet(K key, V... values) {
        return reactiveRedisTemplate.opsForSet().remove(key, (Object[]) values);
    }

    @Override
    public Mono<Boolean> addToZSet(K key, V value, double score) {
        return reactiveRedisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public Flux<V> rangeFromZSet(K key, long start, long end) {
        Range<Long> range = Range.closed(start, end);
        return reactiveRedisTemplate.opsForZSet().range(key, range);
    }

    @Override
    public Mono<Boolean> exists(K key) {
        return reactiveRedisTemplate.hasKey(key);
    }

    @Override
    public Mono<Boolean> expire(K key, Duration timeout) {
        return reactiveRedisTemplate.expire(key, timeout);
    }

    @Override
    public Mono<Long> getExpire(K key) {
        return reactiveRedisTemplate.getExpire(key).map(Duration::getSeconds);
    }

    @Override
    public Mono<Long> deleteMultiple(Collection<K> keys) {
        if (Objects.isNull(keys) || keys.isEmpty()) {
            return Mono.just(0L);
        }
        return reactiveRedisTemplate.delete(Flux.fromIterable(keys));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Flux<K> keys(K keyPrefix) {
        // 注意：生产环境慎用 keys 操作，建议配合 Scan
        return reactiveRedisTemplate.keys((K) (keyPrefix.toString() + "*"));
    }
}
