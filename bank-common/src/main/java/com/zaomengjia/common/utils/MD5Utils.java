package com.zaomengjia.common.utils;

import cn.hutool.crypto.digest.MD5;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/6 21:07
 */
public final class MD5Utils {
    public static String toMD5(String str) {
        return MD5.create().digestHex(str, StandardCharsets.UTF_8);
    }

    public static String fromMD5(String md5) {
        char[] a = md5.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        return new String(a);
    }
}
