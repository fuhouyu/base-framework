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
package com.fuhouyu.framework.database;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <p>
 * mapped工具类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/18 22:31
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MappedStatementUtil {

    public static final String PAGE_COUNT = "_COUNT";

    /**
     * 根据 MappedStatement 的 id 解析出对应的 Method 对象。
     *
     * @param mappedStatement MyBatis 的 MappedStatement 对象
     * @return 对应的 Method 对象
     */
    public static Method resolveMethodFromMappedStatement(MappedStatement mappedStatement) {
        if (mappedStatement == null) {
            throw new IllegalArgumentException("MappedStatement 不能为空");
        }

        // 获取 MappedStatement 的 id，格式如：com.fuhouyu.framework.database.mapper.UserMapper.findUserById
        String statementId = mappedStatement.getId();

        // 分离类名和方法名
        int lastDotIndex = statementId.lastIndexOf('.');
        if (lastDotIndex == -1) {
            throw new IllegalArgumentException("无效的 MappedStatement id: " + statementId);
        }

        String className = statementId.substring(0, lastDotIndex);
        String methodName = statementId.substring(lastDotIndex + 1);
        methodName = methodName.endsWith(PAGE_COUNT) ? methodName.substring(0, methodName.length() - PAGE_COUNT.length()) : methodName;
        // 反射获取类和方法
        Class<?> mapperClass;
        try {
            mapperClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.format("%s 类未找到", className));
        }
        for (Method method : mapperClass.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        // 获取父类接口方法
        Method method = getInterfaceMethods(mapperClass, methodName);
        if (Objects.nonNull(method)) {
            return method;
        }
        throw new IllegalArgumentException(String.format("方法未找到: %s in class %s", methodName, className));
    }

    /**
     * 获取父级的接口
     *
     * @param clazz      class
     * @param methodName 方法名称
     */
    private static Method getInterfaceMethods(Class<?> clazz, String methodName) {
        if (Objects.isNull(clazz)) {
            return null;
        }
        // 获取当前类实现的接口
        for (Class<?> superInterface : clazz.getInterfaces()) {
            for (Method method : superInterface.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    return method;
                }
            }
            // 递归处理父接口
            MappedStatementUtil.getInterfaceMethods(superInterface, methodName);
        }

        // 递归处理父类的接口
        Class<?> superClass = clazz.getSuperclass();
        if (Objects.nonNull(superClass) && superClass != Object.class) {
            return MappedStatementUtil.getInterfaceMethods(superClass, methodName);
        }
        return null;
    }
}
