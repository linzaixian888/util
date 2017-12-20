package com.linzaixian.util.encrypt;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URLDecoder;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Hex;

/**
 * 
 * @author lzx
 * 
 */
public class RSAUtil {
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private KeyPair keyPair;
	private final static int KEY_SIZE = 1024;
	public RSAUtil(KeyPair keyPair) {
		try {
			init(keyPair);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public RSAUtil() {
		try {
			init(generateKeyPair());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init2() throws Exception {
		FileInputStream fis = new FileInputStream("c:/RSAKey.txt");
		ObjectInputStream oos = new ObjectInputStream(fis);
		keyPair = (KeyPair) oos.readObject();
		privateKey = keyPair.getPrivate();
		publicKey = keyPair.getPublic();
		oos.close();
		fis.close();
	}

	/**
	 * 生成密钥对
	 * 
	 * @return
	 * @throws Exception
	 */
	private KeyPair generateKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA",
				new org.bouncycastle.jce.provider.BouncyCastleProvider());
		keyPairGen.initialize(KEY_SIZE, new SecureRandom());
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
	 * 
	 * @param pk
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private byte[] encrypt(PublicKey pk, byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA",
				new org.bouncycastle.jce.provider.BouncyCastleProvider());
		cipher.init(Cipher.ENCRYPT_MODE, pk);
		int blockSize = cipher.getBlockSize();// 获得加密块大小，如：加密前数据为128个byte，而key_size=1024
		// 加密块大小为127
		// byte,加密后为128个byte;因此共有2个加密块，第一个127
		// byte第二个为1个byte
		int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
		int leavedSize = data.length % blockSize;
		int blocksSize = leavedSize != 0 ? data.length / blockSize + 1
				: data.length / blockSize;
		byte[] raw = new byte[outputSize * blocksSize];
		int i = 0;
		while (data.length - i * blockSize > 0) {
			if (data.length - i * blockSize > blockSize) {
				cipher.doFinal(data, i * blockSize, blockSize, raw, i
						* outputSize);
			}
				
			else {
				cipher.doFinal(data, i * blockSize,
						data.length - i * blockSize, raw, i * outputSize);
			// 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到
			// ByteArrayOutputStream中，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了
			// OutputSize所以只好用dofinal方法。
			}
			i++;
		}
		return raw;

	}

	/**
	 * rsa加密
	 * 
	 * @param data
	 *            明文
	 * @return 密文
	 * @throws Exception
	 */
	public String encrypt(String data) throws Exception {
		byte[] byteArray = encrypt(this.publicKey, data.getBytes());
		return new String(Hex.encodeHex(byteArray));
	}

	private byte[] decrypt(PrivateKey pk, byte[] raw) throws Exception {

		Cipher cipher = Cipher.getInstance("RSA",
				new org.bouncycastle.jce.provider.BouncyCastleProvider());
		cipher.init(cipher.DECRYPT_MODE, pk);
		int blockSize = cipher.getBlockSize();
		ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
		int j = 0;

		while (raw.length - j * blockSize > 0) {
			bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
			j++;
		}
		bout.close();
		return bout.toByteArray();
	}

	/**
	 * rsa解密
	 * 
	 * @param raw
	 *            密文
	 * @return 明文
	 * @throws Exception
	 */
	private String decrypt(String miwen) throws Exception {
		byte[] byteArray = Hex.decodeHex(miwen.toCharArray());
		return new String(decrypt(this.privateKey, byteArray));
	}

	public String decryptByJs(String miwen) throws Exception {
		//byte[] byteArray = new BigInteger(miwen, 16).toByteArray();
		//有时会出现数组长度为129,第一个元素为0，这是错误的，所以重写转换
		byte[] byteArray =hexStringToBytes(miwen);
		System.out.println(byteArray.length);
		String result = new String(decrypt(this.privateKey, byteArray));
		//用js加密的是倒序的且进行了编码，且进行解码
		return URLDecoder.decode(new StringBuilder(result).reverse().toString(), "utf-8");
	}

	/**
	 * 16进制 To byte[]
	 * 
	 * @param hexString
	 * @return byte[]
	 */
	private static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public KeyPair getKeyPair() {
		return keyPair;
	}

	public void setKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
	}

	public static void main(String[] args) {
		RSAUtil u = new RSAUtil();
		try {
			System.out.println(u.decrypt(u.encrypt("中国人")));;
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	
	
	}

}
