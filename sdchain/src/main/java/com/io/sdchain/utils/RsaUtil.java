package com.io.sdchain.utils;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RsaUtil {
	private static final String KEY_ALGORITHM = "RSA";

	/**
	 * Private key to decrypt
	 * @param data Pending data
	 * @param key secret key
	 * @return byte[] Decrypt the data
	 */
	private static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
		// Get the private key
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// Generate the private key
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// data decryption
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}
	
	/**
	 * public key encryption
	 * @param data Data to be encrypted
	 * @param key secret key
	 * @return byte[]  enciphered data
	 */
	public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {
		// Instantiate the key factory
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// Initializing public key
		// Key material conversion
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		// Generate a public key
		PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
		// data encryption
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		return cipher.doFinal(data);
	}
	
	/**
     * decode
     * @param content
     * @param key
     * @return
     * @throws Exception
     */
	public static String decrypt(String content,String key) throws Exception {
		byte[] decontent = Base64.decode(content,Base64.DEFAULT);
		byte[] dekey = Base64.decode(key,Base64.DEFAULT);
		byte[] decode2 = RsaUtil.decryptByPrivateKey(decontent, dekey);
		return new String(decode2);
    }
	
	/**
	 * encrypt
	 * @param content
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String content,String key) throws Exception {
		byte[] dekey = Base64.decode(key,Base64.DEFAULT);
		byte[] code2 = RsaUtil.encryptByPublicKey(content.getBytes(), dekey);
		return Base64.encodeToString(code2,Base64.DEFAULT);
    }
	
	public static void main(String[] args) throws Exception {
		String str = "18525852021";
		String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKi6SwEbhWhb3TZV+MAJ+hQZaIo3yiOOzeek/fIp6jrTSqfo8klmYt1/LJh7FHsxvlPLLlHvO5TWXEspCRjTU5kCAwEAAQ==";
		String privateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAqLpLARuFaFvdNlX4wAn6FBloijfKI47N56T98inqOtNKp+jySWZi3X8smHsUezG+U8suUe87lNZcSykJGNNTmQIDAQABAkBaw/SLQPnQPPLQQwlQjU1pOI3jJMXpwO2FOl2dwuyM/bNgLjq3JLgfA7v3Yy1cF0G9USRiMp65KLD4w/JzkIpxAiEA0qLhXh7ok9OxfXmAXAaR1KbRkgam5GVEWcmZ6T9dRbUCIQDNENjD8XM799yTyc1lljQ+BQyYDQuVzWS6E+fSE4uE1QIhAI0AafsWl7x1hqgnEK0J3M9MiJfHCit0OsgcM4K3XxnNAiBwYacp2wzX9IJBy/pwjdybkJDSU3Ph6Oomv/nvux5e8QIgVZRF/NNHWjMAqYR5zMIBkG0Vb/atARrJSj+iwaPSDR0=";
		String a = RsaUtil.encrypt(str, publicKey);
		System.out.println(a);
		String b = RsaUtil.decrypt(a, privateKey);
		System.out.println(b);
	}
	
}
