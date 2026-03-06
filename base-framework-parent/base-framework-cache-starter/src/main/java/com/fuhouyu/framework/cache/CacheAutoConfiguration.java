package com.fuhouyu.framework.cache;

import com.fuhouyu.framework.cache.properties.CacheServiceProperties;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * <p>
 * 缓存自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2026/3/6 20:19
 */
@ConfigurationPropertiesScan(value = "com.fuhouyu.framework.cache.properties")
@Slf4j
@RequiredArgsConstructor
public class CacheAutoConfiguration implements InitializingBean {

    private final CacheServiceProperties cacheServiceProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        LoggerUtil.info(log, "缓存类型: {} ", cacheServiceProperties.getType());
    }
}
