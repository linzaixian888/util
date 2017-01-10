package com.linzaixian.util.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * 一个MD5算法的压缩类
 * @author lzx
 *
 */
public class MD5Util {
	private final static String[] STRS = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
	
	private static String getStr(byte[] b){
		StringBuffer sb = new StringBuffer();
		String stri = new String();
		for(int i=0;i<b.length;i++){
			stri = byteToString(b[i]);
			sb.append(stri);
		}
		return sb.toString();
	}
	private static String byteToString(byte bt){
		int b = bt;
		if(b < 0){b+=256;}
		int b1 = b/16;
		int b2 = b%16;
		return STRS[b1] + STRS[b2];
	}
	/**
	 * 对字符串进行md5加密
	 * @param sss
	 * @return
	 */
	public static String  encrypt(String sss){
		String str = sss;
		MessageDigest md = null;
		try {
			 md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return getStr(md.digest(str.getBytes()));
	}
	public static void main(String[] args) {
		System.out.println(encrypt("林"));
	}
}
