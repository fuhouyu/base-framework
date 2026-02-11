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
package com.fuhouyu.framework.s3.service;

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>
 * s3Client 基类接口
 * </p>
 *
 * @author fuhouyu
 * @since 2026/1/28 20:48
 */
public interface BaseS3ClientService {

    /**
     * 同一个桶复制对象，并删除源对象
     *
     * @param sourceObjectKey      源对象Key
     * @param destinationObjectKey 目标对象Key
     */
    default void copyObjectAndDeleteSourceObject(String sourceObjectKey, String destinationObjectKey) {
        this.copyObjectAndDeleteSourceObject(this.getBucketName(), sourceObjectKey, this.getBucketName(), destinationObjectKey);
    }

    /**
     * 复制对象并删除源对象
     *
     * @param sourceBucketName      源存储桶名称
     * @param sourceObjectKey       源对象Key
     * @param destinationBucketName 目标存储桶名称
     * @param destinationObjectKey  目标对象Key
     */
    default void copyObjectAndDeleteSourceObject(String sourceBucketName, String sourceObjectKey, String destinationBucketName, String destinationObjectKey) {
        this.copyObject(sourceBucketName, sourceObjectKey, destinationBucketName, destinationObjectKey);
        this.deleteObject(sourceBucketName, sourceObjectKey);
    }


    /**
     * 删除对象
     *
     * @param objectKey 对象Key
     * @return 删除对象的响应结果
     */
    default DeleteObjectResponse deleteObject(String objectKey) {
        return this.deleteObject(this.getBucketName(), objectKey);
    }


    /**
     * 删除对象
     *
     * @param bucketName 桶名
     * @param objectKey  对象Key
     * @return 删除对象的响应结果
     */
    default DeleteObjectResponse deleteObject(String bucketName, String objectKey) {
        return executeS3Action(() -> this.getS3Client().deleteObject(b -> b.bucket(bucketName).key(objectKey)), "删除对象失败");
    }


    /**
     * 同一个桶复制对象
     *
     * @param sourceObjectKey      源对象Key
     * @param destinationObjectKey 目标对象Key
     * @return CopyObjectResponse
     */
    default CopyObjectResponse copyObject(String sourceObjectKey, String destinationObjectKey) {
        return this.copyObject(this.getBucketName(), sourceObjectKey, this.getBucketName(), destinationObjectKey);
    }

    /**
     * 复制对象
     *
     * @param sourceBucketName      源存储桶名称
     * @param sourceObjectKey       源对象Key
     * @param destinationBucketName 目标存储桶名称
     * @param destinationObjectKey  目标对象Key
     * @return CopyObjectResponse
     */
    default CopyObjectResponse copyObject(String sourceBucketName, String sourceObjectKey, String destinationBucketName, String destinationObjectKey) {
        return executeS3Action(() -> this.getS3Client().copyObject(b -> b.sourceBucket(sourceBucketName).sourceKey(sourceObjectKey)
                .destinationBucket(destinationBucketName).destinationKey(destinationObjectKey)), "复制对象失败");
    }

    /**
     * 判断对象是否存在
     *
     * @param objectKey 对象Key
     * @return true 存在，false 不存在
     */
    default boolean doesObjectExist(String objectKey) {
        return this.doesObjectExist(this.getBucketName(), objectKey);
    }

    /**
     * 判断对象是否存在
     *
     * @param bucketName 存储桶名称（建议从配置读取或作为参数）
     * @param objectKey  对象Key
     * @return true 存在，false 不存在
     */
    default boolean doesObjectExist(String bucketName, String objectKey) {
        return executeS3Action(() -> {
            try {
                this.getS3Client().headObject(b -> b.bucket(bucketName).key(objectKey));
                return true;
            } catch (NoSuchKeyException e) {
                // 这是预期的“不存在”情况，直接返回 false，不向上抛出
                return false;
            }
        }, "检查对象是否存在失败");
    }

    /**
     * 上传对象
     *
     * @param putObjectRequest putObject请求参数
     * @param requestBody      上传对象
     * @return HeadObjectResponse  object元数据
     */
    default PutObjectResponse putObject(Consumer<PutObjectRequest.Builder> putObjectRequest, RequestBody requestBody) {
        return this.executeS3Action(() -> this.getS3Client().putObject(putObjectRequest, requestBody),
                "资源上传失败");
    }

    /**
     * 获取对象元数据
     *
     * @param objectKey 对象Key
     * @return HeadObjectResponse  object元数据
     */
    default HeadObjectResponse headObject(String objectKey) {
        return this.headObject(this.getBucketName(), objectKey);
    }

    /**
     * 获取对象元数据
     *
     * @param bucketName 存储桶名称（建议从配置读取或作为参数）
     * @param objectKey  对象Key
     * @return HeadObjectResponse  object元数据
     */
    default HeadObjectResponse headObject(String bucketName, String objectKey) {
        return executeS3Action(() -> this.getS3Client().headObject(b -> b.bucket(bucketName).key(objectKey)), "获取对象元数据失败");
    }

    /**
     * 创建分片上传任务
     *
     * @param objectKey 存储对象Key
     * @return CreateMultipartUploadResponse  分片上传任务信息
     */
    default CreateMultipartUploadResponse createMultipartUpload(String objectKey) {
        return this.createMultipartUpload(this.getBucketName(), objectKey);
    }

    /**
     * 创建分片上传任务
     *
     * @param bucketName 存储桶名称（建议从配置读取或作为参数）
     * @param objectKey  存储对象Key
     * @return CreateMultipartUploadResponse  分片上传任务信息
     */
    default CreateMultipartUploadResponse createMultipartUpload(String bucketName, String objectKey) {
        return executeS3Action(() -> this.getS3Client().createMultipartUpload(b -> b.bucket(bucketName).key(objectKey)), "创建分片上传任务失败");
    }

    /**
     * 上传分片
     *
     * @param uploadPartRequest 上传分片请求参数
     * @param requestBody       上传对象
     * @return UploadPartResponse  分片上传结果
     */
    default UploadPartResponse uploadPart(Consumer<UploadPartRequest.Builder> uploadPartRequest, RequestBody requestBody) {
        return executeS3Action(() -> this.getS3Client().uploadPart(uploadPartRequest, requestBody), "上传分片失败");
    }

    /**
     * 完成分片上传
     *
     * @param completeMultipartUploadRequest 完成分片上传请求参数
     * @return CompleteMultipartUploadResponse  分片上传结果
     */
    default CompleteMultipartUploadResponse completeMultipartUpload(CompleteMultipartUploadRequest completeMultipartUploadRequest) {
        return executeS3Action(() -> this.getS3Client().completeMultipartUpload(completeMultipartUploadRequest), "完成分片上传失败");
    }

    /**
     * 取消分片上传
     *
     * @param abortMultipartUploadRequest 取消分片上传请求参数
     */
    default void abortMultipartUpload(AbortMultipartUploadRequest abortMultipartUploadRequest) {
        executeS3Action(() -> this.getS3Client().abortMultipartUpload(abortMultipartUploadRequest), "取消分片上传失败");
    }

    /**
     * 列出桶中的对象（简单分页查询）
     *
     * @return 对象列表
     */
    default List<S3Object> listObjects() {
        return this.listObjects(this.getBucketName(), null, null);
    }

    /**
     * 列出桶中的对象（简单分页查询）
     *
     * @param maxKeys 最大返回数量（如果不传，S3 默认通常是 1000）
     * @return 对象列表
     */
    default List<S3Object> listObjects(Integer maxKeys) {
        return this.listObjects(this.getBucketName(), null, maxKeys);
    }


    /**
     * 列出桶中的对象（简单分页查询）
     *
     * @param prefix 搜索前缀（即搜索以该字符串开头的对象）
     * @return 对象列表
     */
    default List<S3Object> listObjects(String prefix) {
        return this.listObjects(this.getBucketName(), prefix, null);
    }


    /**
     * 列出桶中的对象（简单分页查询）
     *
     * @param prefix  搜索前缀（即搜索以该字符串开头的对象）
     * @param maxKeys 最大返回数量（如果不传，S3 默认通常是 1000）
     * @return 对象列表
     */
    default List<S3Object> listObjects(String prefix, Integer maxKeys) {
        return this.listObjects(this.getBucketName(), prefix, maxKeys);
    }

    /**
     * 列出桶中的对象
     *
     * @param bucketName 桶名
     * @param prefix     搜索前缀（支持搜索）
     * @return 对象列表
     */
    default List<S3Object> listObjects(String bucketName, String prefix) {
        return this.listObjects(bucketName, prefix, null);
    }


    /**
     * 列出桶中的对象
     *
     * @param bucketName 桶名
     * @param prefix     搜索前缀（支持搜索）
     * @param maxKeys    最大返回数量（如果不传，S3 默认通常是 1000）
     * @return 对象列表
     */
    default List<S3Object> listObjects(String bucketName, String prefix, Integer maxKeys) {
        return executeS3Action(() -> {
            ListObjectsV2Response response = this.getS3Client().listObjectsV2(builder -> {
                builder.bucket(bucketName);
                if (StringUtils.hasText(prefix)) {
                    builder.prefix(prefix); // S3 的“搜索”主要是靠前缀匹配
                }
                if (maxKeys != null) {
                    builder.maxKeys(maxKeys);
                }
            });
            return response.contents();
        }, "列出对象列表失败");
    }

    /**
     * 检查是否存在指定前缀的对象（模糊搜索是否存在）
     *
     * @param prefix 搜索前缀
     * @return true 存在，false 不存在
     */
    default boolean hasObjectsWithPrefix(String prefix) {
        List<S3Object> objects = this.listObjects(this.getBucketName(), prefix, 1);
        return !objects.isEmpty();
    }

    /**
     * 获取对象
     *
     * @param objectKey 存储对象Key
     * @return 响应流
     */
    default ResponseInputStream<GetObjectResponse> getObject(String objectKey) {
        return this.getObject(this.getBucketName(), objectKey, null);
    }

    /**
     * 获取对象
     *
     * @param objectKey 对象Key
     * @return 响应流
     */
    default ResponseInputStream<GetObjectResponse> getObject(String objectKey, String rangeHeader) {
        return this.getObject(this.getBucketName(), objectKey, rangeHeader);
    }

    /**
     * 获取对象
     *
     * @param bucketName 存储桶名称（建议从配置读取或作为参数）
     * @param objectKey  对象Key
     * @return 响应流
     */
    default ResponseInputStream<GetObjectResponse> getObject(String bucketName, String objectKey,
                                                             String rangeHeader) {
        return this.executeS3Action(() -> this.getS3Client().getObject(builder -> {
            builder.bucket(bucketName)
                    .key(objectKey);
            if (StringUtils.hasText(rangeHeader)) {
                builder.range(rangeHeader);
            }
        }));
    }

    /**
     * 统一的 S3 操作执行器（使用默认错误消息）
     *
     * @param action 具体要执行的 S3 调用逻辑
     * @param <T>    返回值的类型
     * @return 执行结果
     */
    default <T> T executeS3Action(Supplier<T> action) {
        // 这里的 "操作失败" 是默认值
        return executeS3Action(action, "存储服务操作失败");
    }

    /**
     * 统一的 S3 操作执行器
     *
     * @param action   具体要执行的 S3 调用逻辑
     * @param errorMsg 日志打印的错误描述前缀
     * @param <T>      返回值的类型
     * @return 执行结果
     */
    default <T> T executeS3Action(Supplier<T> action, String errorMsg) {
        try {
            return action.get();
        } catch (S3Exception e) {
            // 捕获 S3 服务端异常（如权限、Key冲突等）
            LoggerUtil.error(this.getLogger(), "{}: S3服务端错误 - [{}], 状态码: [{}]",
                    errorMsg, e.awsErrorDetails().errorMessage(), e.statusCode());
            throw new ServiceException(ResponseStatusEnum.SERVER_ERROR, errorMsg);
        } catch (SdkClientException e) {
            // 捕获 客户端异常（如网络超时、DNS解析失败）
            LoggerUtil.error(this.getLogger(), "{}: 客户端网络异常 - [{}]", errorMsg, e.getMessage());
            throw new ServiceException(ResponseStatusEnum.SERVER_ERROR, "存储服务连接超时");
        } catch (Exception e) {
            // 捕获 其他未知异常
            LoggerUtil.error(this.getLogger(), "{}: 未知异常 - [{}]", errorMsg, e.getMessage());
            throw new ServiceException(ResponseStatusEnum.SERVER_ERROR, errorMsg);
        }
    }

    /**
     * 获取s3 client
     *
     * @return s3 client
     */
    S3Client getS3Client();

    /**
     * 获取bucket名称
     *
     * @return bucket名称
     */
    String getBucketName();

    /**
     * 获取logger
     *
     * @return logger
     */
    Logger getLogger();
}
