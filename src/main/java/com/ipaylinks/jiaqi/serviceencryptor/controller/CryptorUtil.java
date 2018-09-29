package com.ipaylinks.jiaqi.serviceencryptor.controller;

import com.ipay.api.common.DefaultDecryptor;
import com.ipay.api.common.DefaultEncryptor;
import com.ipay.api.common.IpayApiException;
import com.ipay.api.common.internal.util.IpaySignature;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Nthforever
 * @date 2018-09-28
 */
public class CryptorUtil {

    //todo 添加logger

    private static final String CHAR_SET="UTF-8";
    private static final String ENCRYPT_TYPE="AES";
    private static final char UNDERLINE='_';
    public static final String ERROR_RESPONSE="error_response";
    private static final String DEFAULT_SIGN_TYPE="RSA2";


    /**加密
     * @param source
     * @param key
     * @return
     */
    public static String encryptor(String source,String key){

        DefaultEncryptor defaultEncryptor = new DefaultEncryptor(key);
        String encrypt = defaultEncryptor.encrypt(source, ENCRYPT_TYPE, CHAR_SET);
        System.out.println("encryptor success ==》 "+encrypt);
        return encrypt;
    }

    /**
     * 解密
     * @param source
     * @param key
     * @return
     */
    public static String decryptor(String source,String key){
        DefaultDecryptor defaultDecryptor = new DefaultDecryptor(key);
        String decrypt = defaultDecryptor.decrypt(source, ENCRYPT_TYPE, CHAR_SET);
        System.out.println("decryptor success ==》 "+decrypt);
        return decrypt;
    }


    /**
     * 签名
     * @param source 源字符串
     * @param privateKey 商户私钥
     * @param type 签名类型
     * @return
     */
    public static String sign(String source,String privateKey,String type){
        String res ="";
        try {
            if (DEFAULT_SIGN_TYPE.equals(type)){
                res = IpaySignature.rsa256Sign(source, privateKey, CHAR_SET);
            }else {
                res = IpaySignature.rsaSign(source, privateKey, CHAR_SET);
            }

            System.out.println("sign success ==》 "+res);
        } catch (IpayApiException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 验签
     * @param sign 已签名字符串
     * @param source 源字符串
     * @param publicKey 商户公钥
     * @param type 签名类型
     * @return
     */
    public static boolean check(String sign,String source,String publicKey,String type){
        boolean rsa = false;
        try {
            rsa = IpaySignature.rsaCheck(source, sign, publicKey, CHAR_SET, type);
            System.out.println("sign check success ==》 "+rsa);
        } catch (IpayApiException e) {
            e.printStackTrace();
        }
        return rsa;
    }



    public static String camelToUnderline(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        int len=param.length();
        StringBuilder sb=new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c=param.charAt(i);
            if (Character.isUpperCase(c) && i != 0){
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            }else{
                sb.append(c);
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 下划线转驼峰
     * @param param
     * @return
     */
    public static String underlineToCamel(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        int len=param.length();
        StringBuilder sb=new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c=param.charAt(i);
            if (c==UNDERLINE){
                if (++i<len){
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰转下划线。
     * @param param
     * @return
     */
    public static String underlineToCamel2(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        StringBuilder sb=new StringBuilder(param);
        Matcher mc= Pattern.compile("_").matcher(param);
        int i=0;
        while (mc.find()){
            int position=mc.end()-(i++);
            //String.valueOf(Character.toUpperCase(sb.charAt(position)));
            sb.replace(position-1,position+1,sb.substring(position,position+1).toUpperCase());
        }
        return sb.toString();
    }
}
