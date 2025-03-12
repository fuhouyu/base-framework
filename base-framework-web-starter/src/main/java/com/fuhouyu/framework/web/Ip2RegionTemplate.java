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
package com.fuhouyu.framework.web;

import com.fuhouyu.framework.web.model.Ip2Region;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * <p>
 * ip2regionTemplate
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/11 22:22
 */
public class Ip2RegionTemplate implements AutoCloseable {

    private static final String PATH_START = "classpath:";

    private final Searcher searcher;

    public Ip2RegionTemplate(String dbPath) {
        try {
            this.searcher = this.initDbPath(dbPath);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("%s 从路径中加载db失败: %s", dbPath, e.getMessage()));
        }
    }

    private Searcher initDbPath(String dbPath) throws IOException {
        byte[] dbBuffer;
        if (dbPath.startsWith(PATH_START)) {
            // 从classpath下加载资源
            String classpathResource = dbPath.substring(PATH_START.length());
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(classpathResource)) {
                if (inputStream == null) {
                    throw new FileNotFoundException("Resource not found: " + classpathResource);
                }
                dbBuffer = inputStream.readAllBytes();
            }
        } else {
            // 从宿主机文件系统加载
            dbBuffer = Searcher.loadVectorIndexFromFile(dbPath);
        }
        return Searcher.newWithBuffer(dbBuffer);
    }


    /**
     * 搜索ip
     *
     * @param ip ip
     * @return 地址信息
     */
    public Ip2Region searchIp(String ip) {
        try {
            return new Ip2Region(searcher.search(ip));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }


    @Override
    public void close() throws Exception {
        if (Objects.nonNull(searcher)) {
            searcher.close();
        }
    }
}
