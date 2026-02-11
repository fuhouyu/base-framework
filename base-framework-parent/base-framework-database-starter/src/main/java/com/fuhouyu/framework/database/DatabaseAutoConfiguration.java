package com.fuhouyu.framework.database;

import com.fuhouyu.framework.kms.KmsAutoConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 数据库自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2026/2/4 20:27
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@AutoConfigureAfter(KmsAutoConfiguration.class)
@ComponentScan(basePackageClasses = DatabaseAutoConfiguration.class)
public class DatabaseAutoConfiguration {
}
