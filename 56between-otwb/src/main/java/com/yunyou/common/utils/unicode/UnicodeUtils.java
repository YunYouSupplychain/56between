package com.yunyou.common.utils.unicode;

public class UnicodeUtils {
    //解码
    public static String unicodeToString(String unicode) {
        if (unicode == null || "".equals(unicode)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;
        while ((i = unicode.indexOf("\\u", pos)) != -1) {
            sb.append(unicode.substring(pos, i));
            if (i + 5 < unicode.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16));
            }
        }
        if (pos < unicode.length()) {
            sb.append(unicode.substring(pos));
        }
        return sb.toString();
    }

    //编码
    public static String stringtoUnicode(String string) {
        if (string == null || "".equals(string)) {
            return null;
        }
        StringBuilder unicode = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            unicode.append("\\u").append(Integer.toHexString(c));
        }
        return unicode.toString();
    }
}
