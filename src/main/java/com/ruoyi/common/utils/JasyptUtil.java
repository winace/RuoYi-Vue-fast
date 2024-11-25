package com.ruoyi.common.utils;


import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

/**
 * Jasypt 加密工具类
 *
 * @author gluoh
 * @since 2021/03/09 18:05
 */

public class JasyptUtil {

    /**
     * 加密
     *
     * @param plaintext 待加密内容
     * @param salt      加密密码
     * @return 加密值
     */
    public static String encrypt(String plaintext, String salt) {
        /* 加密工具   */
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        /* 加密配置 */
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        /* 加密方式 */
        config.setAlgorithm("PBEWithMD5AndDES");
        /* 加密密码 */
        config.setPassword(salt);
        /* 应用配置 */
        encryptor.setConfig(config);
        return encryptor.encrypt(plaintext);
    }

    /**
     * 解密
     *
     * @param ciphertext 待解密内容
     * @param salt       解密密码
     * @return 解密值
     */
    public static String decrypt(String ciphertext, String salt) {
        /* 加密工具   */
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        /* 加密配置 */
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        /* 加密方式 */
        config.setAlgorithm("PBEWithMD5AndDES");
        /* 加密密码 */
        config.setPassword(salt);
        /* 应用配置 */
        encryptor.setConfig(config);
        return encryptor.decrypt(ciphertext);
    }

}
