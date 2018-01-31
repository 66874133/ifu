package com.funnel.security.symmetric;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DES {

    private String password;

    public DES(String password) {
        this.password = password;
    }

    /**
    * 加密
    * @param datasource byte[]
    * @param password String
    * @return byte[]
    */
    public byte[] encrypt(byte[] datasource) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
    * 解密
    * @param src byte[]
    * @param password String
    * @return byte[]
    * @throws Exception
    */
    public byte[] decrypt(byte[] src) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }

    /** 
     * 
     * <p> 
     * Des加密strMing，然后base64转换 
     * @param strMing 
     * @param md5key 
     * @return 
     * @return String    
     * author: Heweipo 
     */
    public String encryptString(String strMing) {
        byte[] byteMi = null;
        byte[] byteMing = null;
        String strMi = "";
        try {
            byteMing = strMing.getBytes("utf-8");
            byteMi = encrypt(byteMing);
            BASE64Encoder base64Encoder = new BASE64Encoder();
            strMi = base64Encoder.encode(byteMi);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            byteMing = null;
            byteMi = null;
        }
        return strMi;
    }

    /** 
     * 
     * <p> 
     * Base64转换strMi,然后进行des解密 
     * @param strMi 
     * @param md5key 
     * @return 
     * @return String    
     * author: Heweipo 
     */
    public String decryptString(String strMi) {
        byte[] byteMing = null;
        String strMing = "";
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byteMing = decoder.decodeBuffer(strMi);
            byteMing = decrypt(byteMing);
            strMing = new String(byteMing,"utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            byteMing = null;
        }
        return strMing;
    }

    //测试
    public static void main(String args[]) throws UnsupportedEncodingException {
        //待加密内容
        String str = "111_" + System.currentTimeMillis();
        //密码，长度要是8的倍数
        String password = "6200616411960574122434059469100235892702736860872901247123456958802882010913257074332531189842634785729877354946875887501857953775777216308447887369944730603446";

        DES des = new DES(password);
        byte[] result = des.encrypt(str.getBytes());
        System.out.println("加密后：" + new String(result, "utf-8"));

        //直接将如上内容解密
        try {
            byte[] decryResult = des.decrypt(result);
            System.out.println("解密后：" + new String(decryResult));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        
        String resultStr = des.encryptString(str);
        System.out.println("加密后：" + resultStr);

        //直接将如上内容解密
        try {
            String decryResultStr = des.decryptString(resultStr);
            System.out.println("解密后：" + decryResultStr);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }
}
