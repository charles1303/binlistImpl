package com.projects.binlist.utils.security;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESEncryption {

	private static final byte[] SALT = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35,
			(byte) 0xE3, (byte) 0x03 };
	private static final int ITERATION_COUNT = 65536;
	private static final int KEY_LENGTH = 256;
	private Cipher cipher;
	private Cipher decipher;
	private SecretKey secret;

	public AESEncryption(String passPhrase) throws Exception {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH);
		SecretKey tmp = factory.generateSecret(spec);
		this.secret = new SecretKeySpec(tmp.getEncoded(), "AES");

		this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		this.decipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

	}

	public String encrypt(String encrypt) throws Exception {
		byte[] bytes = encrypt.getBytes(UTF_8);
		byte[] encrypted = encrypt(bytes);
		return Base64.getEncoder().encodeToString(encrypted);
	}

	public byte[] encrypt(byte[] plain) throws Exception {
		this.cipher.init(Cipher.ENCRYPT_MODE, this.secret);
		return this.cipher.doFinal(plain);
	}

	public String decrypt(String encrypt) throws Exception {
		byte[] bytes = Base64.getDecoder().decode(encrypt);
		byte[] decrypted = decrypt(bytes);
		return new String(decrypted, UTF_8);
	}

	public byte[] decrypt(byte[] encrypt) throws Exception {
		byte[] iv = this.cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
		this.decipher.init(Cipher.DECRYPT_MODE, this.secret, new IvParameterSpec(iv));
		return this.decipher.doFinal(encrypt);
	}
	
	public File writeToFile(File output, byte[] toWrite)
			throws IllegalBlockSizeException, BadPaddingException, IOException {
		FileOutputStream fos = new FileOutputStream(output);
		fos.write(toWrite);
		fos.flush();
		fos.close();
		return output;
	}
}
