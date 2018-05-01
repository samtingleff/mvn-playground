package com.playground.ortb;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionVsSha1 {
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

	public static byte[] hash(String storageId) throws NoSuchAlgorithmException {
		MessageDigest sha1 = java.security.MessageDigest.getInstance("SHA1");
		sha1.reset();
		sha1.update(storageId.getBytes());
		byte[] result = sha1.digest();
		return result;
	}

	public static void main(String[] args) throws Exception {
		String secret = "Bar12345Bar12345"; // 128 bit key
		byte[] iv = new byte[16];
		random.nextBytes(iv);
		int count = 10000000;
		long start = System.nanoTime();
		for (int i = 0; i < count; ++i) {
			byte[] bytes = new byte[16];
			String storageId = new String(bytes);
			byte[] encrypted = encrypt(secret, iv, storageId);
			int l1 = bytes.length, l2 = encrypted.length;
		}
		System.err.println("AES duration:  " + (System.nanoTime() - start));
		double throughput = ((double) count) / ((double) ((System.nanoTime() - start) / (1000d * 1000d * 1000d)));
		System.err.println("AES throughput:  " + throughput);
		start = System.nanoTime();
		for (int i = 0; i < count; ++i) {
			byte[] bytes = new byte[16];
			String storageId = new String(bytes);
			byte[] hashed = hash(storageId);
			int l1 = bytes.length, l2 = hashed.length;
		}
		System.err.println("SHA1 duration: " + (System.nanoTime() - start));
		throughput = ((double) count) / ((double) ((System.nanoTime() - start) / (1000d * 1000d * 1000d)));
		System.err.println("SHA1 throughput:   " + throughput);
	}
}
