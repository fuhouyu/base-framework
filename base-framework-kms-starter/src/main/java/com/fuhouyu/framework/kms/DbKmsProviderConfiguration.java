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
package com.fuhouyu.framework.kms;

import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.kms.exception.KmsException;
import com.fuhouyu.framework.kms.properties.KeyProperties;
import com.fuhouyu.framework.kms.provider.DbKeyProvider;
import com.fuhouyu.framework.kms.provider.KeyProvider;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * <p>
 * db kms 配置
 * </p>
 *
 * @author fuhouyu
 * @since 2025/7/27 10:30
 */
@Slf4j
@ConditionalOnProperty(prefix = KeyProperties.PREFIX,
        name = "key-provider", havingValue = "db")
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class DbKmsProviderConfiguration {

    private final ResourceLoader resourceLoader;

    private final JdbcTemplate jdbcTemplate;

    private final KeyProperties keyProperties;

    @PostConstruct
    public void init() {
        KeyProperties.DbKeyProperties db = this.keyProperties.getDb();
        this.doInitSql(db.getSchemaInitPath());
        this.doInitSql(db.getDataInitPath());
    }


    @Bean
    @ConditionalOnMissingBean(KeyProvider.class)
    public KeyProvider keyProvider() {
        return new DbKeyProvider(jdbcTemplate);
    }

    private void doInitSql(String path) {
        Resource resource = resourceLoader.getResource(path);
        if (!resource.exists()) {
            throw new IllegalStateException("初始化 SQL 文件不存在: " + path);
        }
        try (InputStream is = resource.getInputStream()) {
            String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Pattern multiLineComment = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
            sql = multiLineComment.matcher(sql).replaceAll("");
            sql = sql.replaceAll("--.*?\\n", "");
            String[] sqlStatements = sql.split(";");

            for (String statement : sqlStatements) {
                String s = statement.trim();
                if (!s.isEmpty()) {
                    jdbcTemplate.execute(s);
                }
            }
            // 执行sql
        } catch (Exception e) {
            LoggerUtil.error(log, "sql 初始化文件: [{}] 执行失败:  [{}]", path, e.getMessage(), e);
            throw new KmsException(e);
        }
    }
}
