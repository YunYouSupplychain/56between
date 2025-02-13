package com.yunyou.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SignatureUtil {

    private static Logger log = LoggerFactory.getLogger(SignatureUtil.class);
    private final static String SIGN_TYPE_RSA = "RSA";
    private final static String SIGN_ALGORITHMS = "SHA1WithRSA";
    private final static String CHARSET = "UTF-8";

    /**
     * 获取私钥PKCS8格式（需base64）
     */
    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, String priKey) throws Exception {
        if (algorithm == null || "".equals(algorithm) || priKey == null || "".equals(priKey)) {
            return null;
        }

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] encodedKey = Base64.decodeBase64(priKey.getBytes());

        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

    public static PrivateKey getPrivateKeyFromPKCS1(String algorithm, String priKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        DerValue[] seq = new DerInputStream(Base64.decodeBase64(priKey.getBytes())).getSequence(0);
        BigInteger modulus = seq[1].getBigInteger();
        BigInteger publicExp = seq[2].getBigInteger();
        BigInteger privateExp = seq[3].getBigInteger();
        BigInteger prime1 = seq[4].getBigInteger();
        BigInteger prime2 = seq[5].getBigInteger();
        BigInteger exp1 = seq[6].getBigInteger();
        BigInteger exp2 = seq[7].getBigInteger();
        BigInteger crtCoef = seq[8].getBigInteger();

        RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2, exp1, exp2, crtCoef);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 通过证书获取公钥（需BASE64，X509为通用证书标准）
     */
    public static PublicKey getPublicKeyFromX509(String algorithm, String pubKey) throws Exception {
        if (algorithm == null || "".equals(algorithm) || pubKey == null || "".equals(pubKey)) {
            return null;
        }

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        StringWriter writer = new StringWriter();
        StreamUtil.io(new InputStreamReader(new ByteArrayInputStream(pubKey.getBytes())), writer);
        byte[] encodeByte = Base64.decodeBase64(pubKey.getBytes());

        return keyFactory.generatePublic(new X509EncodedKeySpec(encodeByte));
    }

    /**
     * 使用私钥对字符进行签名
     */
    public static String sign(String plain, String prikey) throws Exception {
        if (plain == null || "".equals(plain) || prikey == null || "".equals(prikey)) {
            return null;
        }

        PrivateKey privatekey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA, prikey);
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
        signature.initSign(privatekey);
        signature.update(plain.getBytes(CHARSET));
        byte[] signed = signature.sign();

        return new String(Base64.encodeBase64(signed));
    }

    /**
     * 将内容体、签名信息、及对方公钥进行验签
     */
    public static boolean verify(String plain, String sign, String pubKey) {
        if (StringUtils.isBlank(plain) || StringUtils.isBlank(sign) || StringUtils.isBlank(pubKey)) {
            return false;
        }

        try {
            PublicKey publicKey = getPublicKeyFromX509(SIGN_TYPE_RSA, pubKey);
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(publicKey);
            signature.update(plain.getBytes(CHARSET));
            return signature.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }


}