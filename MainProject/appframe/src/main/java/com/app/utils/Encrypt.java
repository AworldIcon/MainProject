package com.app.utils;

import android.util.Base64;

import com.app.beans.MyJSONObject;

import org.json.JSONException;

import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;


/**
 * Created by MaShiZhao on 2017/3/17
 */

public class Encrypt
{


    // 密钥
//    private static String YAN = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC08LiBOhOUG1ak69k7gON3cfmKKUiK9BVv9RCaftu/ZFfyktQB67nBsgWc/RMO+2TmF7phRIliJbRyF140gq4d07LysxS7/jcqr0856GdEXcjNsySRmgNPdSB6wDwFYxdSE9gKvbSERJo+25K44JsP7zZaa46fT7w94Vv91kK3fQIDAQAB";
    static String YAN = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC08LiBOhOUG1ak69k7gON3cfmK" + "\r" +
            "KUiK9BVv9RCaftu/ZFfyktQB67nBsgWc/RMO+2TmF7phRIliJbRyF140gq4d07Ly" + "\r" +
            "sxS7/jcqr0856GdEXcjNsySRmgNPdSB6wDwFYxdSE9gKvbSERJo+25K44JsP7zZa" + "\r" +
            "a46fT7w94Vv91kK3fQIDAQAB";

    public static String desCode(Map<String, String> strings)
    {
        // 获取keys值
        List<String> keys = new ArrayList<>();
        Iterator<Map.Entry<String, String>> stringHeaders = strings.entrySet().iterator();
        while (stringHeaders.hasNext())
        {
            keys.add(stringHeaders.next().getKey());
        }
        // 字典排序
        Collections.sort(keys);
        // 封装成object
        MyJSONObject jsonObject = new MyJSONObject();
        for (String key : keys)
        {
            try
            {
                jsonObject.put(key, strings.get(key));
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        //把 body层的 / 的转义字符去除掉
        String deSString = md5Encrypt(jsonObject.toString());
//        L.d("第一次加密：" + deSString);

        try
        {
            deSString = encryptWithRSA(deSString);
//            L.d(deSString.length() + " 第二次加密：" + deSString);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return deSString;
    }

    //对string进行MD5加密
    private static String md5Encrypt(String string)
    {
//        L.d("加密之前字符串：" + string);
        try
        {
            StringBuffer sb = new StringBuffer();

            MessageDigest digest = MessageDigest.getInstance("md5");// algorithm

            byte[] bytes = digest.digest(string.getBytes()); // 参数是，明文字节数组，返回的就是加密后的结果，字节数组

            for (byte b : bytes)
            { // 数byte 类型转换为无符号的整数

                int n = b & 0XFF; // 将整数转换为16进制

                String s = Integer.toHexString(n); // 如果16进制字符串是一位，那么前面补0

                if (s.length() == 1)
                {

                    sb.append("0" + s);

                } else
                {

                    sb.append(s);
                }

            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e)
        {

            e.printStackTrace();
        }
        return null;
    }


    /**
     * 从字符串中加载公钥,从服务端获取
     *
     * @param pubKey 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    private static PublicKey loadPublicKey(String pubKey)
    {
        try
        {
            byte[] buffer = Base64.decode(pubKey, Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return keyFactory.generatePublic(keySpec);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 公钥加密过程  publicKey
     *
     * @param plainData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public static String encryptWithRSA(String plainData) throws Exception
    {
        PublicKey publicKey = loadPublicKey(YAN);
        if (publicKey == null)
        {
            throw new NullPointerException("encrypt PublicKey is null !");
        }

        Cipher cipher = null;
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");// 此处如果写成"RSA"加密出来的信息JAVA服务器无法解析

        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] output = cipher.doFinal(plainData.getBytes("UTF-8"));
//        // 必须先encode成 byte[]，再转成encodeToString，否则服务器解密会失败 php 不识别这种方式
//        byte[] encode = Base64.encode(output, Base64.DEFAULT);
//        String s = Base64.encodeToString(encode, Base64.DEFAULT);
        return new String(Base64.encode(output, Base64.DEFAULT)).replace("\n", "").trim();
    }
//
//    解密
//    public static String decryptWithRSA(String encryedData) throws Exception
//    {
//        if (publicKey == null)
//        {
//            throw new NullPointerException("decrypt PublicKey is null !");
//        }
//
//        Cipher cipher = null;
//        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");// 此处如果写成"RSA"解析的数据前多出来些乱码
//        cipher.init(Cipher.DECRYPT_MODE, publicKey);
//        byte[] output = cipher.doFinal(Base64.decode(encryedData, Base64.DEFAULT));
//        String s = new String(output);
//        L.d(s);
//        return s;
//    }


}
