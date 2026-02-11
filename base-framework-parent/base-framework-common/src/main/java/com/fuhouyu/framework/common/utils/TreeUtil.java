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
package com.fuhouyu.framework.common.utils;


import com.fuhouyu.framework.common.BaseTree;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 构建树对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 17:39
 */
public class TreeUtil {


    private TreeUtil() {
    }


    /**
     * 构建树对象
     *
     * @param treeList 树集合
     * @param <T>      树泛型
     * @return 树集合
     */
    public static <T extends BaseTree<T>> List<T> buildTree(List<T> treeList) {
        Map<Long, List<T>> groupedByParent = treeList.stream().collect(Collectors.groupingBy(BaseTree::getParentId));
        return buildSubTree(-1L, groupedByParent);
    }


    private static <T extends BaseTree<T>> List<T> buildSubTree(Long parentId, Map<Long, List<T>> groupedByParent) {
        // 获取当前父节点的子节点列表
        List<T> subTree = groupedByParent.getOrDefault(parentId, Collections.emptyList());

        // 对每个子节点递归调用，构建其子树
        subTree.forEach(node -> node.setChildren(buildSubTree(node.getId(), groupedByParent)));

        return subTree;
    }

}
