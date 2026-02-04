package com.fuhouyu.framework.database;

/**
 * CipherText
 *
 * <p>
 * 基础设施层的安全值对象，用于表示「需要被加密存储的文本数据」。
 * </p>
 *
 * <ul>
 *   <li>其存在目的是：
 *     <ul>
 *       <li>在 Jimmer 中作为非标准类型，安全地绑定 ScalarProvider</li>
 *       <li>避免对 String 做全局加解密</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <p>
 * Java 层始终操作明文，数据库中存储密文。
 * </p>
 *
 * @author fuhouyu
 * @since 2026/2/4 21:42
 */
public record CipherText(String value) {
}
