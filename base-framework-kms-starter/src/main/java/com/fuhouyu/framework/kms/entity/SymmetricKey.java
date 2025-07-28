package com.fuhouyu.framework.kms.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 对称密钥实体类，例如 AES、SM4 等
 * </p>
 *
 * @author fuhouyu
 * @since 2025/7/26 19:03
 */
@Getter
@Setter
@ToString
public class SymmetricKey implements Serializable {

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
     * 加密模式，如 ECB、CBC 等
     */
    private String mode;

    /**
     * 填充方式，如 PKCS5Padding、NoPadding 等
     */
    private String padding;

    /**
     * 盐值
     */
    private byte[] secret;

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
