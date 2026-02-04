package com.fuhouyu.framework.database;

import org.babyfish.jimmer.sql.MappedSuperclass;

import java.time.LocalDateTime;

/**
 * <p>
 * 基类
 * </p>
 *
 * @author fuhouyu
 * @since 2026/2/4 20:15
 */
@MappedSuperclass
public interface BaseEntity {

    /**
     * 创建人
     * @return 创建人
     */
    String createdBy();

    /**
     * 创建时间
     * @return 创建时间
     */
    LocalDateTime createdAt();

    /**
     * 更新人
     * @return 更新人
     */
    String updatedBy();

    /**
     * 更新时间
     * @return 更新时间
     */
    LocalDateTime updatedAt();
//
//    /**
//     * 是否删除
//     * @return 是否删除标记
//     */
//    boolean isDeleted();
}
