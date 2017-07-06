package com.coder.kzxt.utils;

import android.util.Base64;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Decrypt_Utils {
	
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
		'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	 /**
	  * des解密
	  * @param key
	  * @param data
	  * @return
	  * @throws Exception
	  */
     public static String decode(String key, String data) throws Exception {
            try {
            	byte b[] = Base64.decode(data, Base64.DEFAULT); //必须先用base64解密 否则数据丢失
                DESKeySpec dks = new DESKeySpec(key.getBytes());
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
               //key的长度不能够小于8位字节  
                Key secretKey = keyFactory.generateSecret(dks);
                Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
                IvParameterSpec iv = new IvParameterSpec(key.getBytes());
//              AlgorithmParameterSpec paramSpec = iv;  
                cipher.init(Cipher.DECRYPT_MODE, secretKey,iv);
                return new String(cipher.doFinal(b));
             } catch (Exception e){
               throw new Exception(e);
         }  
      }  

     
     /**
      * des加密
      * @param s
      * @return
      */
     public static String encode(String key, byte[] data)throws Exception {
    	
    	 try{
    		 
    		 DESKeySpec dks = new DESKeySpec(key.getBytes());
    		 SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    		 //key的长度不能够小于8位字节          
    		 Key secretKey = keyFactory.generateSecret(dks);
    		 Cipher cipher = Cipher.getInstance("DES");
    		 IvParameterSpec iv = new IvParameterSpec(key.getBytes());
    		 cipher.init(Cipher.ENCRYPT_MODE, secretKey,iv);
    		 byte[] bytes = cipher.doFinal(data);
    		 return Base64.encodeToString(bytes, Base64.DEFAULT);
    		 
    	 }catch (Exception e) {
    		 throw new Exception(e);
    	 }
    	 
     }
     
     
     public static String encodes(String key, String data) throws Exception {
         return encode(key, data.getBytes());
     }
     
     
     /**
      * md5加密
      * @param s
      * @return
      */
     public static String md5(String s) {
 		try {
 			// Create MD5 Hash
 			MessageDigest digest = MessageDigest.getInstance("MD5");
 			digest.update(s.getBytes());
 			byte messageDigest[] = digest.digest();

 			return toHexString(messageDigest);
 		} catch (NoSuchAlgorithmException e) {
 			e.printStackTrace();
 		}
 		return "";
 	}

 	private static String toHexString(byte[] b) { // String to byte
 		StringBuilder sb = new StringBuilder(b.length * 2);
 		for (int i = 0; i < b.length; i++) {
 			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
 			sb.append(HEX_DIGITS[b[i] & 0x0f]);
 		}
 		return sb.toString();
 	}

}
