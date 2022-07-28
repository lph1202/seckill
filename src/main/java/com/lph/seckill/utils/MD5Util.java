package com.lph.seckill.utils;


import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;


@Component
public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String SALT="1a2b3c4d";

    public static String inputPassToFromPass(String inputPass){
        String str = ""+SALT.charAt(0)+SALT.charAt(2)+inputPass+SALT.charAt(5)+SALT.charAt(4);
        return md5(str);
    }

    public static String fromPassToDBPass(String fromPass,String salt){
        String str = ""+salt.charAt(0)+salt.charAt(2)+fromPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass,String salt){
        String fromPass = inputPassToFromPass(inputPass);
        String dbPass = fromPassToDBPass(fromPass,salt);
        return dbPass;
    }

    public static void main(String[] args) {
//        ce21b747de5af71ab5c2e20ff0a60eea
        System.out.println(inputPassToFromPass("123456"));
//        0687f9701bca74827fcefcd7e743d179
        System.out.println(fromPassToDBPass("d3b1294a61a07da9b49b6e22b2cbd7f9","1a2b3c4d"));
        System.out.println(inputPassToDBPass("123456","1a2b3c4d"));
    }
}
