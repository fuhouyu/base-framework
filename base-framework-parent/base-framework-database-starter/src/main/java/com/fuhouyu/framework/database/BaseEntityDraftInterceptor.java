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

import com.fuhouyu.framework.context.ContextHolder;
import org.babyfish.jimmer.ImmutableObjects;
import org.babyfish.jimmer.sql.DraftInterceptor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

/**
 * <p>
 * 基类实体草稿拦截器
 * </p>
 *
 * @author fuhouyu
 * @since 2026/2/4 20:09
 */
@Component
public class BaseEntityDraftInterceptor implements DraftInterceptor<BaseEntity, BaseEntityDraft> {

    @Override
    public void beforeSave(@NonNull BaseEntityDraft draft, @Nullable BaseEntity original) {
        String username = ContextHolder.getContext().getUser().getUsername();
        if (!ImmutableObjects.isLoaded(draft, BaseEntityProps.UPDATED_AT)) {
            draft.setUpdatedAt(OffsetDateTime.now());
        }
        if (!ImmutableObjects.isLoaded(draft, BaseEntityProps.UPDATED_BY)) {
            draft.setUpdatedBy(username);
        }

        if (original == null) {
            if (!ImmutableObjects.isLoaded(draft, BaseEntityProps.CREATED_AT)) {
                draft.setCreatedAt(OffsetDateTime.now());
            }
            if (!ImmutableObjects.isLoaded(draft, BaseEntityProps.CREATED_BY)) {
                draft.setCreatedBy(username);
            }
            if (!ImmutableObjects.isLoaded(draft, BaseEntityProps.IS_DELETED)) {
                draft.setIsDeleted(false);
            }
        }
    }
}
