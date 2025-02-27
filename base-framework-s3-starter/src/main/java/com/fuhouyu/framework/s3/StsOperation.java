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
package com.fuhouyu.framework.s3;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuhouyu.framework.common.utils.JacksonUtil;
import com.fuhouyu.framework.s3.enums.StsActionEnum;
import com.fuhouyu.framework.s3.properties.StsProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * s3 sts 操作类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/22 19:06
 */
@Slf4j
@RequiredArgsConstructor
public class StsOperation {

    private final StsClient stsClient;

    private final StsProperties stsProperties;


    /**
     * 生成临时的token
     *
     * @param bucket      桶名
     * @param objectKey   对象key
     * @param actionEnums 操作枚举
     * @return 临时token响应
     */
    public AssumeRoleResponse generateStsToken(@NonNull String bucket,
                                               @NonNull String objectKey,
                                               @NonNull StsActionEnum... actionEnums) {
        return this.generateStsToken(bucket, List.of(objectKey), actionEnums);
    }

    /**
     * 生成临时的token
     *
     * @param bucket      桶名
     * @param actionEnums 操作枚举
     * @return 临时token响应
     */
    public AssumeRoleResponse generateStsToken(@NonNull String bucket,
                                               @NonNull StsActionEnum... actionEnums) {
        return this.generateStsToken(bucket, List.of(stsProperties.getPolicyResourcePrefix() + bucket + "/*"), actionEnums);
    }

    /**
     * 生成临时的token
     *
     * @param policy policy
     * @return 临时token响应
     */

    private AssumeRoleResponse doGenerateStsToken(Policy policy) {
        AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
                .roleArn(stsProperties.getRoleArn())
                .roleSessionName(UUID.randomUUID().toString().replace("-", "").substring(16))
                .policy(JacksonUtil.writeValueAsString(policy))
                .durationSeconds(stsProperties.getDurationSeconds())
                .build();
        return this.stsClient.assumeRole(assumeRoleRequest);
    }

    /**
     * 生成临时的token
     *
     * @param bucket      桶名
     * @param objectKeys  对象key集合
     * @param actionEnums 操作枚举
     * @return 临时token响应
     */
    public AssumeRoleResponse generateStsToken(@NonNull String bucket,
                                               @NonNull Collection<String> objectKeys,
                                               @NonNull StsActionEnum... actionEnums) {
        List<String> resources = new ArrayList<>(objectKeys.size());
        for (String objectKey : objectKeys) {
            resources.add(stsProperties.getPolicyResourcePrefix() + bucket + "/" + objectKey);
        }
        Policy policy = this.generatePolicy(resources, actionEnums);
        return this.doGenerateStsToken(policy);
    }

    /**
     * policy
     *
     * @param resources   资源
     * @param actionEnums 枚举
     * @return policy
     */
    private Policy generatePolicy(List<String> resources, StsActionEnum... actionEnums) {
        Collection<Statement> statements = new ArrayList<>(actionEnums.length);
        Collection<String> actions = new ArrayList<>(actionEnums.length);
        for (StsActionEnum actionEnum : actionEnums) {
            actions.add(stsProperties.getPolicyActionPrefix() + actionEnum.name());
        }
        statements.add(Statement.builder().action(actions).resource(resources).build());
        return Policy.builder().statements(statements).build();
    }

}


@Builder
@Getter
class Policy {

    @JsonProperty("Version")
    private final String version = "2012-10-17";

    @JsonProperty("Statement")
    private final Collection<Statement> statements;
}

@Builder
@Getter
class Statement {

    @JsonProperty("Effect")
    private final String effect = "Allow";

    @JsonProperty("Action")
    private Collection<String> action;

    @JsonProperty("Resource")
    private Collection<String> resource;

}
