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
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;

/**
 * <p>
 * 文件操作的工具类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/18 19:12
 */
public class FileUtil {

    private static final int BUFFER_SIZE = 8192;


    private FileUtil() {

    }

    /**
     * 创建文件夹，如果文件夹不存在
     *
     * @param path 文件夹路径地址
     */
    public static void createDirectorIfNotExists(Path path) {
        if (Files.exists(path)) {
            return;
        }
        try {
            Files.createDirectories(path);
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

    /**
     * 复制文件
     *
     * @param sourceInputStream 源数据
     * @param targetPath        需要写入的目标路径
     * @param options           复制选项
     * @throws IOException io异常
     */
    public static void copyFile(InputStream sourceInputStream, Path targetPath, CopyOption... options) throws IOException {
        Files.copy(sourceInputStream, targetPath, options);
    }

    /**
     * 通过字节流写入一个方法
     *
     * @param writePath   要写入的文件路径
     * @param inputStream 输入流
     */
    public static void writeFile(Path writePath, InputStream inputStream) throws IOException {
        Files.copy(inputStream, writePath);
    }

    /**
     * 文件拷贝
     *
     * @param sourcePath 源地址
     * @param targetPath 目标地址
     * @param options    复制选项
     * @throws IOException io异常
     */
    public static void copyFile(Path sourcePath, Path targetPath, CopyOption... options) throws IOException {
        Files.copy(sourcePath, targetPath, options);
    }

    /**
     * 批量设置文件属性
     *
     * @param path          路径地址
     * @param fileAttribute 文件属性map
     * @throws IOException io异常
     */
    public static void setFileAttributeAll(Path path, Map<String, String> fileAttribute) throws IOException {
        Set<Map.Entry<String, String>> entries = fileAttribute.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String key = entry.getKey();
            String value = entry.getValue();
            setFileAttribute(path, key, value);
        }
    }

    /**
     * 批量设置文件属性
     *
     * @param path  路径地址
     * @param key   键
     * @param value value
     * @throws IOException io异常
     */
    public static void setFileAttribute(Path path, String key, String value) throws IOException {
        UserDefinedFileAttributeView view = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        ByteBuffer buffer = Charset.defaultCharset().encode(value);
        view.write(key, buffer);
    }

    /**
     * 读取文件元数据
     *
     * @param path path
     * @return 文件元数据
     * @throws IOException io异常
     */
    public static Map<String, String> readFileAttributes(Path path) throws IOException {
        // 检查文件系统是否支持UserDefinedFileAttributeView
        if (!Files.getFileStore(path).supportsFileAttributeView(UserDefinedFileAttributeView.class)) {
            return Collections.emptyMap();
        }
        UserDefinedFileAttributeView view = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        // 写入自定义属性
        List<String> list = view.list();
        if (list.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> map = new TreeMap<>();
        for (String name : list) {
            ByteBuffer buf = ByteBuffer.allocate(view.size(name));
            view.read(name, buf);
            buf.flip();
            String value = Charset.defaultCharset().decode(buf).toString();
            map.put(name, value);
        }
        return map;
    }


    /**
     * 递归删除所有文件
     *
     * @param directPath 文件夹路径
     * @throws IOException io异常
     */
    public static void deleteDirect(Path directPath) throws IOException {
        if (!Files.exists(directPath)) {
            return;
        }
        if (!Files.isDirectory(directPath)) {
            Files.delete(directPath);
            return;
        }
        Files.walkFileTree(directPath, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }


            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
                throw exc;
            }
        });
    }

    /**
     * 读取文件属性
     *
     * @param path 路径
     * @param key  属性key
     * @return 自定义的属性值
     * @throws IOException 可能存在的io异常
     */
    public static String readFileAttribute(Path path, String key) throws IOException {
        UserDefinedFileAttributeView view = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
        ByteBuffer buf = ByteBuffer.allocate(view.size(key));
        view.read(key, buf);
        buf.flip();
        return Charset.defaultCharset().decode(buf).toString();
    }

    /**
     * 设置文件最后修改时间，并返回时间
     *
     * @param path 路径
     * @throws IOException io异常
     */
    public static Date setFileLastModifiedTime(Path path) throws IOException {
        FileTime fileTime = FileTime.fromMillis(System.currentTimeMillis());
        Files.setLastModifiedTime(path, fileTime);
        return Date.from(fileTime.toInstant());
    }

    /**
     * 获取文件最后的更新时间
     *
     * @param path 文件路径
     * @return 文件最后的修改时间
     * @throws IOException io异常
     */
    public static Date getFileLastModifiedTime(Path path) throws IOException {
        FileTime fileTime = Files.getLastModifiedTime(path);
        // 将FileTime转换为Instant
        Instant instant = fileTime.toInstant();
        // 将Instant转换为Date
        return Date.from(instant);
    }


    /**
     * 计算文件摘要
     *
     * @param path   path
     * @param digest 摘要的算法
     * @return 计算后的结果
     */
    public static String calculateFileDigest(Path path,
                                             MessageDigest digest) {
        try (FileChannel fileChannel = FileChannel.open(path)) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            while (fileChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                digest.update(byteBuffer);
                byteBuffer.clear();
            }
            return HexUtil.toHexString(digest.digest());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算文件摘要
     *
     * @param inputStream 输入流
     * @param digest      摘要的算法
     * @return 计算后的结果
     */
    public static String calculateFileDigest(InputStream inputStream,
                                             MessageDigest digest) throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        while (inputStream.read(bytes) != -1) {
            digest.update(bytes);
        }
        return HexUtil.toHexString(digest.digest());
    }

}
