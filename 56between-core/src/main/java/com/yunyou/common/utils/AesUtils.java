package com.yunyou.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AesUtils {
    private static Logger log = LoggerFactory.getLogger(AesUtils.class);
    private static final String ENCODING = "utf-8";
    private static final String KEY_ALGORITHM = "AES";

    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @param key     加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String ecbEncrypt(String content, String key) {
        byte[] data = null;
        try {
            byte[] contentBytes = content.getBytes(ENCODING);
            data = encryptOrDecrypt(Cipher.ENCRYPT_MODE, contentBytes, key, null, EncodeType.AES_ECB_PKCS5Padding);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return data == null ? "" : Base64.encodeBase64String(data);
    }

    /**
     * AES 解密操作
     */
    public static String ecbDecrypt(String content, String key) {
        try {
            byte[] contentBytes = Base64.decodeBase64(content);
            byte[] data = encryptOrDecrypt(Cipher.DECRYPT_MODE, contentBytes, key, null, EncodeType.AES_ECB_PKCS5Padding);
            return new String(data, ENCODING);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    private static byte[] encryptOrDecrypt(int mode, byte[] contentBytes, String key, String iv, String modeAndPadding) throws InvalidKeyException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        byte[] keyBytes = key.getBytes(ENCODING);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        // 创建密码器
        Cipher cipher = Cipher.getInstance(modeAndPadding);
        if (null != iv) {
            // 指定一个初始化向量 (Initialization vector，IV)， IV 必须是16位
            byte[] ivBytes = iv.getBytes(ENCODING);
            cipher.init(mode, keySpec, new IvParameterSpec(ivBytes));
        } else {
            cipher.init(mode, keySpec);
        }
        return cipher.doFinal(contentBytes);
    }

    public static class EncodeType {
        public final static String AES_DEFAULT = "AES";
        public final static String AES_CBC_NoPadding = "AES/CBC/NoPadding";
        public final static String AES_CBC_PKCS5Padding = "AES/CBC/PKCS5Padding";
        public final static String AES_CBC_ISO10126Padding = "AES/CBC/ISO10126Padding";
        public final static String AES_CFB_NoPadding = "AES/CFB/NoPadding";
        public final static String AES_CFB_PKCS5Padding = "AES/CFB/PKCS5Padding";
        public final static String AES_CFB_ISO10126Padding = "AES/CFB/ISO10126Padding";
        public final static String AES_ECB_NoPadding = "AES/ECB/NoPadding";
        public final static String AES_ECB_PKCS5Padding = "AES/ECB/PKCS5Padding";
        public final static String AES_ECB_ISO10126Padding = "AES/ECB/ISO10126Padding";
        public final static String AES_OFB_NoPadding = "AES/OFB/NoPadding";
        public final static String AES_OFB_PKCS5Padding = "AES/OFB/PKCS5Padding";
        public final static String AES_OFB_ISO10126Padding = "AES/OFB/ISO10126Padding";
        public final static String AES_PCBC_NoPadding = "AES/PCBC/NoPadding";
        public final static String AES_PCBC_PKCS5Padding = "AES/PCBC/PKCS5Padding";
        public final static String AES_PCBC_ISO10126Padding = "AES/PCBC/ISO10126Padding";
    }

}