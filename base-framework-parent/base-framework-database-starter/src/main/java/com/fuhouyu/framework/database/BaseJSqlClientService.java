/*
 * Copyright 2024-present fuhouyu.
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

import org.babyfish.jimmer.Input;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.View;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.mutation.BatchSaveResult;
import org.babyfish.jimmer.sql.ast.mutation.DeleteResult;
import org.babyfish.jimmer.sql.ast.mutation.SaveMode;
import org.babyfish.jimmer.sql.ast.mutation.SimpleSaveResult;
import org.babyfish.jimmer.sql.ast.query.MutableRootQuery;
import org.babyfish.jimmer.sql.ast.query.Order;
import org.babyfish.jimmer.sql.ast.table.spi.TableProxy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * <p>
 * 基于 Jimmer JSqlClient 的通用业务基类接口
 * </p>
 * <p>
 * 该接口定义了标准的增删查改（CRUD）契约，通过泛型解耦实体、输入模型与输出视图。
 * </p>
 *
 * @param <T> table（表类型）：Jimmer 生成的 Table 对象，代表数据库表结构，主要用于构建查询条件和执行查询。
 * @param <E> Entity（实体类型）：与数据库表结构一一对应的 Jimmer 实体接口。
 *            主要用于内部查询（Table 对象）和持久化核心逻辑。
 * @param <I> Input（输入类型）：用于保存或更新的 DTO（如 IamTenantsInput）。
 *            通常是 Jimmer 编译生成的 Input 对象，负责承载前端传入的业务数据并
 *            支持深度保存（Save Command）。
 * @param <V> View（视图类型）：用于返回给前端的静态对象视图（如 IamTenantDetailView）。
 *            利用 Jimmer 的 Object Fetcher 功能，实现“按需返回”字段，避免暴露
 *            不必要的敏感数据或产生冗余查询。
 * @author fuhouyu
 * @since 2026/3/3 19:22
 */
public interface BaseJSqlClientService<T extends TableProxy<E>, E, I extends Input<E>, V extends View<E>> {

    /**
     * 获取 JSqlClient 对象
     *
     * @return JSqlClient 对象
     */
    JSqlClient getSqlClient();

    /**
     * 获取视图类
     *
     * @return 视图类
     */
    Class<V> getViewClass();

    /**
     * 获取表对象
     *
     * @return 表对象
     */
    T getTable();

    /**
     * 获取实体类
     *
     * @return 实体类
     */
    Class<E> getEntityClass();

    /**
     * 创建实体
     *
     * @param input 输入模型
     * @return 创建的实体
     */
    @Transactional(rollbackFor = Exception.class)
    default E create(I input) {
        SimpleSaveResult<E> result = this.getSqlClient()
                .saveCommand(input)
                .setMode(SaveMode.INSERT_ONLY)
                .execute();
        return result.getModifiedEntity();
    }

    /**
     * 批量创建实体
     *
     * @param inputs 输入模型集合
     * @return 创建后的实体列表（包含自动生成的 ID 等字段）
     */
    @Transactional(rollbackFor = Exception.class)
    default List<E> createBatch(Collection<I> inputs) {
        if (CollectionUtils.isEmpty(inputs)) {
            return Collections.emptyList();
        }
        BatchSaveResult<I> batchSaveResult = this.getSqlClient()
                .saveEntitiesCommand(inputs)
                .setMode(SaveMode.INSERT_ONLY)
                // 批量操作建议开启，如果某一条失败则全部回滚
                .execute();
        return batchSaveResult.getItems().stream()
                .map(i -> i.getModifiedEntity().toEntity()).toList();
    }

    /**
     * 更新实体
     *
     * @param input 输入模型
     * @return 更新的实体
     */
    @Transactional(rollbackFor = Exception.class)
    default E update(I input) {
        SimpleSaveResult<E> result = this.getSqlClient()
                .saveCommand(input)
                .setMode(SaveMode.UPDATE_ONLY)
                .execute();
        return result.getModifiedEntity();
    }

    /**
     * 批量更新实体
     *
     * @param inputs 输入模型
     * @return 更新的实体
     */
    @Transactional(rollbackFor = Exception.class)
    default List<E> updateBatch(Collection<I> inputs) {
        if (CollectionUtils.isEmpty(inputs)) {
            return Collections.emptyList();
        }
        BatchSaveResult<I> batchSaveResult = this.getSqlClient()
                .saveEntitiesCommand(inputs)
                .setMode(SaveMode.UPDATE_ONLY)
                .execute();
        return batchSaveResult.getItems().stream()
                .map(i -> i.getModifiedEntity().toEntity()).toList();
    }

    /**
     * 创建实体 (直接使用实体对象)
     *
     * @param entity 实体对象
     * @return 保存后的实体（包含自增 ID、默认值等）
     */
    @Transactional(rollbackFor = Exception.class)
    default E createEntity(E entity) {
        return this.getSqlClient()
                .saveCommand(entity)
                .setMode(SaveMode.INSERT_ONLY)
                .execute()
                .getModifiedEntity();
    }

    /**
     * 批量创建实体
     *
     * @param entities 实体对象集合
     * @return 保存后的实体列表
     */
    @Transactional(rollbackFor = Exception.class)
    default List<E> createEntities(Collection<E> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }
        // 注意：保存实体集合时返回的是 BatchSaveResult<E>
        BatchSaveResult<E> result = this.getSqlClient()
                .saveEntitiesCommand(entities)
                .setMode(SaveMode.INSERT_ONLY)
                .execute();
        return result.getItems().stream().map(BatchSaveResult.Item::getModifiedEntity).toList();
    }

    /**
     * 更新实体
     * @param entity 实体对象
     * @return 更新后的实体
     */
    @Transactional(rollbackFor = Exception.class)
    default E updateEntity(E entity) {
        return this.getSqlClient()
                .saveCommand(entity)
                .setMode(SaveMode.UPDATE_ONLY)
                .execute()
                .getModifiedEntity();
    }

    /**
     * 批量更新实体
     * @param entities 实体对象集合
     * @return 更新后的实体
     */
    @Transactional(rollbackFor = Exception.class)
    default List<E> updateEntities(Collection<E> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }
        BatchSaveResult<E> result = this.getSqlClient()
                .saveEntitiesCommand(entities)
                .setMode(SaveMode.UPDATE_ONLY)
                .execute();
        return result.getItems().stream().map(BatchSaveResult.Item::getModifiedEntity).toList();
    }
    
    /**
     * 根据 ID 查询实体
     *
     * @param id 实体 ID
     * @return 实体视图
     */
    default V getById(Object id) {
        return this.getSqlClient()
                .findById(this.getViewClass(), id);
    }

    /**
     * 根据 ID 删除实体
     *
     * @param id 实体 ID
     * @return 删除结果
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean deleteById(Object id) {
        DeleteResult result = this.getSqlClient().deleteById(this.getEntityClass(), id);
        return result.getAffectedRowCount(this.getEntityClass()) > 0;
    }


    /**
     * 根据 ID 集合批量删除实体
     *
     * @param ids 实体 ID 集合
     * @return 删除的记录条数
     */
    @Transactional(rollbackFor = Exception.class)
    default int deleteByIds(Collection<?> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        DeleteResult result = this.getSqlClient().deleteByIds(
                this.getEntityClass(),
                ids
        );
        // 获取当前实体类实际受影响的行数
        return result.getAffectedRowCount(this.getEntityClass());
    }

    /**
     * 分页查询
     *
     * @param pageQueryVO 分页查询的vo对象
     * @return 分页结果
     */
    default PageResultVO<V> page(PageQueryVO pageQueryVO) {
        Page<V> page = this.getSqlClient().createQuery(this.getTable())
                .orderByIf(StringUtils.hasText(pageQueryVO.getOrderBy()), () -> Order.makeOrders(this.getTable(), pageQueryVO.getOrderBy()))
                .select(this.getTable().fetch(this.getViewClass()))
                .fetchPage(pageQueryVO.getPageNum() - 1, pageQueryVO.getPageSize());
        return PageResultVO.of(page, pageQueryVO.getPageNum(), pageQueryVO.getPageSize());
    }


    /**
     * 分页查询（带额外自定义过滤条件）
     *
     * @param pageQueryVO 分页查询对象
     * @param customizer  自定义查询构造器（用于添加额外的 where 条件等）
     * @return 分页结果
     */
    default PageResultVO<V> page(PageQueryVO pageQueryVO, Consumer<MutableRootQuery<T>> customizer) {
        // 创建初始查询
        MutableRootQuery<T> query = this.getSqlClient()
                .createQuery(this.getTable())
                // 应用默认排序逻辑
                .orderByIf(
                        StringUtils.hasText(pageQueryVO.getOrderBy()),
                        () -> Order.makeOrders(this.getTable(), pageQueryVO.getOrderBy())
                );

        // 执行自定义逻辑（在这里可以追加额外的 where）
        if (customizer != null) {
            customizer.accept(query);
        }

        Page<V> page = query.select(this.getTable().fetch(this.getViewClass()))
                .fetchPage(Math.max(0, pageQueryVO.getPageNum() - 1), pageQueryVO.getPageSize());
        return PageResultVO.of(page, pageQueryVO.getPageNum(), pageQueryVO.getPageSize());
    }
}
