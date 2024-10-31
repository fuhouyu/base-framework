/*
 * Copyright 2024-2024 the original author or authors.
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
package com.fuhouyu.framework.resource.assembler;

import com.aliyun.oss.model.*;
import com.fuhouyu.framework.resource.model.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 阿里云资源转换
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/31 20:31
 */
@Mapper
public interface AliOssResourceAssembler {

    AliOssResourceAssembler INSTANCE = Mappers.getMapper(AliOssResourceAssembler.class);


    @Mapping(source = "bucketName", target = "bucketName")
    @Mapping(source = "prefix", target = "prefix")
    @Mapping(source = "delimiter", target = "delimiter")
    @Mapping(source = "encodingType", target = "encodingType")
    @Mapping(source = "maxKeys", target = "maxKeys")
    @Mapping(source = "nextMarker", target = "continuationToken")
    @Mapping(source = "startAfter", target = "startAfter")
    @Mapping(source = "objectKey", target = "key")
    ListObjectsV2Request toOssListObjectsRequest(ListResourceRequest listResourceRequest);

    @Mapping(source = "bucketName", target = "bucketName")
    @Mapping(source = "objectKey", target = "key")
    @Mapping(source = "inputStream", target = "inputStream")
    PutObjectRequest toOssPutObjectRequest(PutResourceRequest putResourceRequest);

    @ObjectFactory
    default PutObjectRequest createPutObjectRequest(PutResourceRequest putResourceRequest) {
        // 根据实际需求选择合适的构造函数
        return new PutObjectRequest(putResourceRequest.getBucketName(), putResourceRequest.getObjectKey(),
                putResourceRequest.getFile());
    }

    @Mapping(source = "userMetadata", target = "userMetadata")
    ObjectMetadata toObjectMetadata(ResourceMetadata resourceMetadata);

    ResourceMetadata toResourceMetadata(ObjectMetadata objectMetadata);

    ListMultipartResult toListMultipartResult(PartListing partListing);

    @ObjectFactory
    default ListMultipartResult createListMultipartResult(PartListing partListing) {
        return new ListMultipartResult(partListing.getBucketName(), partListing.getKey(),
                partListing.getUploadId());
    }


    @AfterMapping
    default void toOssPutObjectRequest(PutResourceRequest putResourceRequest, @MappingTarget PutObjectRequest target) {
        PutObjectRequest objectRequest = new PutObjectRequest(putResourceRequest.getBucketName(),
                putResourceRequest.getObjectKey(),
                putResourceRequest.getFile());
        objectRequest.setInputStream(putResourceRequest.getInputStream());
        objectRequest.setMetadata(toObjectMetadata(putResourceRequest.getMetadata()));
    }

    @AfterMapping
    default void toObjectMetadata(ResourceMetadata resourceMetadata,
                                  @MappingTarget ObjectMetadata objectMetadata) {
        Map<String, Object> metadata = resourceMetadata.getMetadata();
        if (Objects.nonNull(metadata)) {
            for (Map.Entry<String, Object> entry : metadata.entrySet()) {
                objectMetadata.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }


    @AfterMapping
    default void toListMultipartResult(PartListing partListing, @MappingTarget ListMultipartResult listMultipartResult) {
        List<PartInfoResult> list = new ArrayList<>(partListing.getParts().size());
        listMultipartResult.setNextPartNumberMaker(partListing.getNextPartNumberMarker());
        listMultipartResult.setTruncated(partListing.isTruncated());
        for (PartSummary part : partListing.getParts()) {
            PartInfoResult partInfoResponse = new PartInfoResult(part.getPartNumber(),
                    part.getLastModified(), part.getETag(), part.getSize());
            list.add(partInfoResponse);
        }
        listMultipartResult.setPartInfoResult(list);
    }

    @AfterMapping
    default void toResourceMetadata(ObjectMetadata objectMetadata,
                                    @MappingTarget ResourceMetadata resourceMetadata) {
        resourceMetadata.addUserMetadataAll(objectMetadata.getUserMetadata());
        resourceMetadata.addHeaderAll(objectMetadata.getRawMetadata());
    }

}
