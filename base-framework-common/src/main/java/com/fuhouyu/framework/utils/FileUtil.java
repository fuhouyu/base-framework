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

package com.fuhouyu.framework.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * <p>
 * 文件操作的工具类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/18 19:12
 */
public class FileUtil {


    private FileUtil() {

    }

    /**
     * 创建文件夹，如果文件夹不存在
     *
     * @param path 文件夹路径地址
     */
    public static void createDirectorIfNotExists(Path path) {
        if (Files.exists(path.getParent())) {
            return;
        }
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 文件删除，如果存在
     *
     * @param path 路径已存在
     */
    public static void deleteFileIfExists(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
