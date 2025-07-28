package com.fuhouyu.framework.kms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 摘要签名实体类，用于签名和验签
 * </p>
 *
 * @author fuhouyu
 * @since 2025/7/26 19:03
 */
@Getter
@Setter
@ToString
public class DigestKey implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 密钥唯一标识
     */
    private String keyId;

    /**
     * 密钥名称
     */
    private String keyName;

    /**
     * 盐值
     */
    private byte[] salt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新人
     */
    private String updatedBy;
}
