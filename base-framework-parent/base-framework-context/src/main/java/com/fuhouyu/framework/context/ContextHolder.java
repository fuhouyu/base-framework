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

package com.fuhouyu.framework.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.fuhouyu.framework.context.user.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 上下文策略类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 10:08
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ContextHolder {

    private static final ThreadLocal<Context> CONTEXT_HOLDER = new TransmittableThreadLocal<>();

    /**
     * 获取用户
     *
     * @return 用户
     */
    public static User getUser() {
        Context ctx = CONTEXT_HOLDER.get();
        return ctx != null ? ctx.getUser() : null;
    }

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    public static Context getContext() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 设置上下文
     *
     * @param context 上下文
     */
    public static void setContext(Context context) {
        CONTEXT_HOLDER.set(context);
    }

    // 记得保留清除方法
    public static void clear() {
        CONTEXT_HOLDER.remove();
    }
}
