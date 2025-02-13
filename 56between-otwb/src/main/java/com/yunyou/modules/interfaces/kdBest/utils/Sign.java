package com.yunyou.modules.interfaces.kdBest.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sign {
    public static String makeSign(Param param) {
        String signString = makeSignString(param);
        String sign = "";
        try {
            sign = digestEncrypte(signString.getBytes(StandardCharsets.UTF_8), "MD5");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sign;
    }

    public static String makeBase64Sign(Param param) {
        String signString = makeSignString(param);
        String sign = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(signString.getBytes(StandardCharsets.UTF_8));
            sign = Base64.encodeBase64String(md.digest());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return sign;
    }

    private static String makeSignString(Param param) {
        return param.getBizData() + param.getPartnerKey();
    }

    public static String digestEncrypte(byte[] plainText, String algorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(plainText);
        byte[] b = md.digest();
        StringBuilder output = new StringBuilder(32);
        for (byte value : b) {
            String temp = Integer.toHexString(value & 0xff);
            if (temp.length() < 2) {
                output.append("0");
            }
            output.append(temp);
        }
        return output.toString();
    }
}
