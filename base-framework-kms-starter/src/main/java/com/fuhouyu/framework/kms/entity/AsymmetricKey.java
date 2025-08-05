package com.fuhouyu.framework.kms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 非对称密钥实体类，例如 RSA、SM2 等
 * </p>
 *
 * @author fuhouyu
 * @since 2025/7/26 19:03
 */
@Getter
@Setter
@ToString
public class AsymmetricKey implements Serializable {

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
     * 公钥内容（Base64 编码）
     */
    private byte[] publicKey;

    /**
     * 私钥内容（Base64 编码）
     */
    private byte[]  privateKey;

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
