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
package com.fuhouyu.framework.s3.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.fuhouyu.framework.common.utils.JacksonUtil;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.s3.enums.StsActionEnum;
import com.fuhouyu.framework.s3.model.StsPolicy;
import com.fuhouyu.framework.s3.model.StsTokenResponse;
import com.fuhouyu.framework.s3.properties.S3Properties;
import com.fuhouyu.framework.s3.properties.StsProperties;
import com.fuhouyu.framework.s3.service.StsOperation;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 阿里云oss sts 操作类实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/18 21:14
 */
@Slf4j
public class AliOssStsOperationImpl implements StsOperation {

    private final StsProperties stsProperties;

    private final S3Properties s3Properties;

    private final DefaultAcsClient client;

    public AliOssStsOperationImpl(StsProperties stsProperties,
                                  S3Properties s3Properties) {
        this.stsProperties = stsProperties;
        DefaultProfile.addEndpoint(stsProperties.getRegion(), "Sts", stsProperties.getEndpoint());
        IClientProfile profile = DefaultProfile.getProfile(stsProperties.getRegion(),
                s3Properties.getAccessKeyId(), s3Properties.getSecretKey());
        this.client = new DefaultAcsClient(profile);
        this.s3Properties = s3Properties;
    }


    @Override
    public StsTokenResponse generateStsToken(@NonNull String bucket, @NonNull StsActionEnum... actionEnums) {
        return this.generateStsToken(bucket, List.of(this.getPolicyResourcePrefix() + bucket + "/*"), actionEnums);
    }


    @Override
    public StsTokenResponse generateStsToken(@NonNull String bucket,
                                             @NonNull String objectKey,
                                             @NonNull StsActionEnum... actionEnums) {
        return this.generateStsToken(bucket, List.of(objectKey), actionEnums);
    }


    @Override
    public StsTokenResponse generateStsToken(@NonNull String bucket,
                                             @NonNull Collection<String> objectKeys,
                                             @NonNull StsActionEnum... actionEnums) {
        StsPolicy policy = this.generatePolicy(bucket, objectKeys, actionEnums);
        AssumeRoleRequest request = new AssumeRoleRequest();
        request.setSysMethod(MethodType.POST);
        request.setRoleArn(this.stsProperties.getRoleArn());
        request.setRoleSessionName(UUID.randomUUID().toString().replace("-", "").substring(16));
        request.setPolicy(JacksonUtil.writeValueAsString(policy));
        request.setDurationSeconds(this.stsProperties.getDurationSeconds().longValue());
        AssumeRoleResponse response;
        try {
            response = client.getAcsResponse(request);
        } catch (ClientException e) {
            LoggerUtil.error(log, "ali yun 生成oss sts token 失败:{}", e.getMessage(), e);
            throw new IllegalArgumentException(e);
        }
        AssumeRoleResponse.Credentials credentials = response.getCredentials();
        return StsTokenResponse.builder()
                .region(s3Properties.getRegion().id())
                .endpoint(s3Properties.getEndpoint())
                .enablePathStyle(s3Properties.getPathStyleEnabled())
                .accessKey(credentials.getAccessKeyId())
                .secretAccessKey(credentials.getAccessKeySecret())
                .sessionToken(credentials.getSecurityToken())
                .build();
    }

    @Override
    public String getStsVersion() {
        return "1";
    }

    @Override
    public String getPolicyActionPrefix() {
        return "oss:";
    }

    @Override
    public String getPolicyResourcePrefix() {
        return "acs:oss:*:*:";
    }

}
