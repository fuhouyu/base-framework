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
package com.fuhouyu.framework.s3.service.impl;

import com.fuhouyu.framework.common.utils.JacksonUtil;
import com.fuhouyu.framework.s3.enums.StsActionEnum;
import com.fuhouyu.framework.s3.model.StsPolicy;
import com.fuhouyu.framework.s3.model.StsTokenResponse;
import com.fuhouyu.framework.s3.properties.S3Properties;
import com.fuhouyu.framework.s3.properties.StsProperties;
import com.fuhouyu.framework.s3.service.StsOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
import software.amazon.awssdk.services.sts.model.Credentials;

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
public class S3StsOperationImpl implements StsOperation {

    private static final String VERSION = "2012-10-17";

    private static final String POLICY_ACTION_PREFIX = "s3:";

    private static final String POLICY_RESOURCE_PREFIX = "arn:aws:s3:::";

    private final StsClient stsClient;

    private final StsProperties stsProperties;

    private final S3Properties s3Properties;

    /**
     * 生成临时的token
     *
     * @param bucket      桶名
     * @param actionEnums 操作枚举
     * @return 临时token响应
     */
    @Override
    public StsTokenResponse generateStsToken(@NonNull String bucket,
                                             @NonNull StsActionEnum... actionEnums) {
        return this.generateStsToken(bucket, List.of(this.getPolicyResourcePrefix() + bucket + "/*"), actionEnums);
    }

    /**
     * 生成临时的token
     *
     * @param bucket      桶名
     * @param objectKey   对象key
     * @param actionEnums 操作枚举
     * @return 临时token响应
     */
    @Override
    public StsTokenResponse generateStsToken(@NonNull String bucket,
                                             @NonNull String objectKey,
                                             @NonNull StsActionEnum... actionEnums) {
        return this.generateStsToken(bucket, List.of(objectKey), actionEnums);
    }


    /**
     * 生成临时的token
     *
     * @param bucket      桶名
     * @param objectKeys  对象key集合
     * @param actionEnums 操作枚举
     * @return 临时token响应
     */
    @Override
    public StsTokenResponse generateStsToken(@NonNull String bucket,
                                             @NonNull Collection<String> objectKeys,
                                             @NonNull StsActionEnum... actionEnums) {
        StsPolicy stsPolicy = this.generatePolicy(bucket, objectKeys, actionEnums);
        AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
                .roleArn(stsProperties.getRoleArn())
                .roleSessionName(UUID.randomUUID().toString().replace("-", "").substring(16))
                .policy(JacksonUtil.toJsonString(stsPolicy))
                .durationSeconds(stsProperties.getDurationSeconds())
                .build();
        AssumeRoleResponse assumeRoleResponse = this.stsClient.assumeRole(assumeRoleRequest);
        Credentials credentials = assumeRoleResponse.credentials();
        return StsTokenResponse.builder()
                .region(s3Properties.getRegion().id())
                .endpoint(s3Properties.getEndpoint())
                .enablePathStyle(s3Properties.getPathStyleEnabled())
                .accessKey(credentials.accessKeyId())
                .secretAccessKey(credentials.secretAccessKey())
                .sessionToken(credentials.sessionToken())
                .build();
    }


    @Override
    public String getStsVersion() {
        return VERSION;
    }

    @Override
    public String getPolicyActionPrefix() {
        return POLICY_ACTION_PREFIX;
    }


    @Override
    public String getPolicyResourcePrefix() {
        return POLICY_RESOURCE_PREFIX;
    }


}

