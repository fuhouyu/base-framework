/*
 * Copyright 2024-2025 fuhouyu.
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
package com.fuhouyu.framework.s3.service;

import com.fuhouyu.framework.s3.enums.StsActionEnum;
import com.fuhouyu.framework.s3.model.StsPolicy;
import com.fuhouyu.framework.s3.model.StsStatement;
import com.fuhouyu.framework.s3.model.StsTokenResponse;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * sts 操作接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/18 21:04
 */
public interface StsOperation {


    /**
     * 生成临时的token
     *
     * @param bucket      桶名
     * @param actionEnums 操作枚举
     * @return 临时token响应
     */
    StsTokenResponse generateStsToken(@NonNull String bucket,
                                      @NonNull StsActionEnum... actionEnums);

    /**
     * 生成临时的token
     *
     * @param bucket      桶名
     * @param objectKey   对象key
     * @param actionEnums 操作枚举
     * @return 临时token响应
     */
    StsTokenResponse generateStsToken(@NonNull String bucket,
                                      @NonNull String objectKey,
                                      @NonNull StsActionEnum... actionEnums);


    /**
     * 生成临时的token
     *
     * @param bucket      桶名
     * @param objectKeys  对象keys
     * @param actionEnums 操作枚举
     * @return 临时token响应
     */
    StsTokenResponse generateStsToken(@NonNull String bucket,
                                      @NonNull Collection<String> objectKeys,
                                      @NonNull StsActionEnum... actionEnums);


    /**
     * 获取sts 的版本
     *
     * @return sts version
     */
    String getStsVersion();

    /**
     * policy action 前缀
     *
     * @return 前缀
     */
    String getPolicyActionPrefix();


    /**
     * policy resource 前缀
     *
     * @return policy 资源前缀
     */
    String getPolicyResourcePrefix();


    /**
     * 生成策略
     *
     * @param actionEnums 操作枚举
     * @return stsPolicy
     */
    default StsPolicy generatePolicy(@NonNull String bucket,
                                     @NonNull Collection<String> objectKeys,
                                     @NonNull StsActionEnum... actionEnums) {
        List<String> resources = new ArrayList<>(objectKeys.size());
        for (String objectKey : objectKeys) {
            if (Arrays.stream(actionEnums).anyMatch(actionEnum -> actionEnum == StsActionEnum.ListBucket)) {
                resources.add(this.getPolicyResourcePrefix() + bucket + "/*");
                break;
            }
            resources.add(this.getPolicyResourcePrefix() + bucket + "/" + objectKey);
        }
        Collection<StsStatement> statements = new ArrayList<>(actionEnums.length);
        Collection<String> actions = new ArrayList<>(actionEnums.length);
        for (StsActionEnum actionEnum : actionEnums) {
            actions.add(this.getPolicyActionPrefix() + actionEnum.name());
        }
        statements.add(StsStatement.builder().action(actions).resource(resources).build());
        return StsPolicy.builder()
                .version(this.getStsVersion())
                .statements(statements).build();
    }

}
