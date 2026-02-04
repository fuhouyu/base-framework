package com.fuhouyu.framework.database;

import com.fuhouyu.framework.context.ContextHolder;
import org.babyfish.jimmer.ImmutableObjects;
import org.babyfish.jimmer.sql.DraftInterceptor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
            draft.setUpdatedAt(LocalDateTime.now());
        }
        if (!ImmutableObjects.isLoaded(draft, BaseEntityProps.UPDATED_BY)) {
            draft.setUpdatedBy(username);
        }
        if (original == null) {
            if (!ImmutableObjects.isLoaded(draft, BaseEntityProps.CREATED_AT)) {
                draft.setCreatedAt(LocalDateTime.now());
            }
            if (!ImmutableObjects.isLoaded(draft, BaseEntityProps.CREATED_BY)) {
                draft.setCreatedBy(username);
            }
        }
    }
}
