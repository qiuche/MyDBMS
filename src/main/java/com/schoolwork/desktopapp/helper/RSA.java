package com.schoolwork.desktopapp.helper;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * 非对称加密 唯一广泛接受并实现 数据加密&数字签名 公钥加密、私钥解密 私钥加密、公钥解密
 * 此处对前端发来的公钥加密字段进行私钥解密
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling
public class RSA {
    private static RSAPublicKey rsaPublicKey;
    private static RSAPrivateKey rsaPrivateKey;
    public static String RSAPublicKey;
    public static String RSAPrivateKey;

    //初始化
    //每天更新一遍公钥私钥
    @Scheduled(cron = "0 0 23 * * ?")
    public static void init() {
        KeyPairGenerator keyPairGenerator;
        try {
            String str= RandomStringUtils.randomAlphanumeric(5);
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);// 64的整倍数
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey = Base64.encodeBase64String(rsaPublicKey.getEncoded()) +"$"+str+"$"+encode(str);
            RSAPrivateKey = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //私钥加密字符
    public static String encode(String key) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(key.getBytes());
        return Base64.encodeBase64String(result);


    }
    //私钥解密
    public static String pubEnPriDe(String PublicKeyjm) throws Exception {
        byte[] result=Base64.decodeBase64(PublicKeyjm);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        result = cipher.doFinal(result);
        return new String(result);
    }

}
