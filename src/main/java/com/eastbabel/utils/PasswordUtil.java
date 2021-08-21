package com.eastbabel.utils;

import com.eastbabel.constant.Constant;
import com.eastbabel.exception.CustomException;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

public final class PasswordUtil {
    private static final String ALGORITHM_NAME = "MD5";
    private static final Integer HASH_ITERATIONS = 5;
    private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();

    private PasswordUtil() {
    }

    public static String encryption(@NotBlank(message = "密码不能为空") String password,
                                    @NotBlank(message = "盐不能为空") String salt) {
        for (int i = 0; i < HASH_ITERATIONS; i++) {
            password = md5(password + salt);
        }
        return password;
    }

    private static String md5(String s) {
        try {
            //MessageDigest是封装md5算法的工具对象还支持SHA算法
            MessageDigest md = MessageDigest.getInstance(ALGORITHM_NAME);
            //通过digest拿到的任意字符串,得到的bates都是等长的
            byte[] bytes = md.digest(s.getBytes(StandardCharsets.UTF_8));
            //返回的toHex通过下面方法再处理
            return toHex(bytes);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        //把toHex的字符串把二进制转换成十六进制
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        //循环判断是为了补位操作
        for (byte aByte : bytes) {
            ret.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return ret.toString();
    }

    public static String genSalt() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    public static boolean validatePassword(String password) {
        return StringUtils.isNotBlank(password) && StringUtils.length(password) >= Constant.PASSWORD_MIN_LENGTH;
    }
}
