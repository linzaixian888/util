package com.linzaixian.util.encrypt;

import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;

public class RSAJdkUtil {
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private KeyPair keyPair;
	private final static int KEY_SIZE = 1024;
	public RSAJdkUtil(KeyPair keyPair) {
		try {
			init(keyPair);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public RSAJdkUtil() {
		try {
			init(generateKeyPair());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 生成密钥对
	 * 
	 * @return
	 * @throws Exception
	 */
	private KeyPair generateKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(KEY_SIZE);
		keyPair = keyPairGen.generateKeyPair();
		return keyPair;
	}
	/**
	 * 公钥密钥的初始化
	 * 
	 * @throws Exception
	 */
	private void init(KeyPair keyPair) throws Exception {
		this.keyPair = keyPair;
		this.privateKey = keyPair.getPrivate();
		this.publicKey = keyPair.getPublic();

	}

	/**
	 * rsa加密
	 * @param mingwen
	 * @return
	 * @throws Exception
	 */
	private String encrypt(String mingwen) throws Exception {
		RSAPublicKey puk = (RSAPublicKey) publicKey;
		BigInteger e = puk.getPublicExponent();
		BigInteger n = puk.getModulus();
		mingwen=URLEncoder.encode(mingwen, "UTF-8");
		byte[] ptext = mingwen.getBytes("UTF-8"); // 获取明文的大整数
		BigInteger m = new BigInteger(ptext);
		BigInteger c = m.modPow(e, n);
		String miwen = c.toString();
		return miwen;
	}

	/**
	 * rsa解密
	 * @param miwen
	 * @return
	 * @throws Exception
	 */
	private String decrypt(String miwen)throws Exception {
		StringBuffer mingwen = new StringBuffer();
		RSAPublicKey puk = (RSAPublicKey) publicKey;
		RSAPrivateCrtKey prk = (RSAPrivateCrtKey) privateKey;
		BigInteger n = puk.getModulus();
		BigInteger d = prk.getPrivateExponent();
		BigInteger c = new BigInteger(miwen);
		BigInteger m = c.modPow(d, n);// 解密明文
		byte[] mt = m.toByteArray();// 计算明文对应的字符串并输出
		for (int i = 0; i < mt.length; i++) {
			mingwen.append((char) mt[i]);
		}
		return URLDecoder.decode(mingwen.toString(), "UTF-8");
	}
	public static void main(String[] args) throws Exception {
		RSAJdkUtil u=new RSAJdkUtil();
		System.out.println(u.decrypt(u.encrypt("中国人")));
	}
}
