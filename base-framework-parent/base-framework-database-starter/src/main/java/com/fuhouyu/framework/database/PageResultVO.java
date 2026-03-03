package com.fuhouyu.framework.database;

import lombok.Builder;
import lombok.Data;
import org.babyfish.jimmer.Page;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 分页结果vo对象
 *
 * @param <V> 返回的视图对象
 *            </p>
 * @author fuhouyu
 * @since 2026/3/2 21:09
 */
@Builder
@Data
public class PageResultVO<V> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private int pageNum;

    /**
     * 每页数量
     */
    private int pageSize;

    /**
     * 总数
     */
    private long totalCount;

    /**
     * 数据列表
     */
    private List<V> list;

    public static <T> PageResultVO<T> of(int pageNum, int pageSize, long totalCount, List<T> list) {
        return PageResultVO.<T>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalCount(totalCount)
                .list(list)
                .build();
    }

    public static <T> PageResultVO<T> empty() {
        return PageResultVO.<T>builder()
                .pageNum(0)
                .pageSize(0)
                .totalCount(0)
                .list(Collections.emptyList())
                .build();
    }

    public static <V> PageResultVO<V> of(Page<V> page, int pageNum, int pageSize) {
        return PageResultVO.<V>builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .totalCount(page.getTotalRowCount())
                .list(page.getRows())
                .build();
    }
}
