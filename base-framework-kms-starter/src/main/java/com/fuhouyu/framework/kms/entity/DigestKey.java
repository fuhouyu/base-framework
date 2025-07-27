package com.fuhouyu.framework.kms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("digest_key")
public class DigestKey implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 密钥唯一标识
     */
    @TableId(type = IdType.ASSIGN_UUID, value = "key_id")
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
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 更新人
     */
    @TableField("updated_by")
    private String updatedBy;
}
