package com.playground.ortb;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class EncryptedIds {
	private static SecureRandom random = new SecureRandom();

	public static byte[] encrypt(String key, byte[] initVector, String value) throws Exception {
		IvParameterSpec iv = new IvParameterSpec(initVector);
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

		byte[] encrypted = cipher.doFinal(value.getBytes());

		return encrypted;
	}

	public static byte[] decrypt(String key, byte[] initVector, byte[] encrypted) throws Exception {
		IvParameterSpec iv = new IvParameterSpec(initVector);
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

		byte[] original = cipher.doFinal(encrypted);

		return original;
	}

	public static void main(String[] args) throws Exception {
		String storageId = "JAX3DDUH-D-DOML";
		byte[] iv = new byte[16];
		random.nextBytes(iv);
		String secret = "Bar12345Bar12345"; // 128 bit key

		byte[] encrypted = encrypt(secret, iv, storageId);
		byte[] encoded = new byte[iv.length + encrypted.length];
		System.arraycopy(iv, 0, encoded, 0, iv.length);
		System.arraycopy(encrypted, 0, encoded, iv.length, encrypted.length);
		String encryptedId = Base64.encodeBase64String(encoded);
		System.out.println("encrypted id length: " + encoded.length);
		System.out.println("encrypted id: " + encryptedId);
		System.out.println("without iv:   " + Base64.encodeBase64String(encrypted));
		System.out.println("without iv:   " + Base64.encodeBase64String(encrypted).length());

		byte[] cipherText = Base64.decodeBase64(encryptedId);
		byte[] reversedIv = new byte[iv.length];
		byte[] reversedCipherText = new byte[cipherText.length - iv.length];
		System.arraycopy(cipherText, 0, reversedIv, 0, iv.length);
		System.arraycopy(cipherText, iv.length, reversedCipherText, 0, reversedCipherText.length);

		byte[] decrypted = decrypt(secret, reversedIv, reversedCipherText);
		System.out.println("decrypted: " + new String(decrypted));
	}
}
