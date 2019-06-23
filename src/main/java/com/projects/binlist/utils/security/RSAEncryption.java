package com.projects.binlist.utils.security;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAEncryption {

	private Cipher cipher;
	private Signature publicSignature;
	private String passPhrase = "terces";
	private String alias = "yek";
	private String keyStoreFile = "keystore.jks";
	KeyFactory keyFactory;

	public RSAEncryption() throws NoSuchAlgorithmException, NoSuchPaddingException {
		this.cipher = Cipher.getInstance("RSA");
		this.publicSignature = Signature.getInstance("SHA256withRSA");
		this.keyFactory = KeyFactory.getInstance("RSA");
	}
	
	public RSAEncryption(String passPhrase, String alias, String keyStoreFile) throws NoSuchAlgorithmException, NoSuchPaddingException {
		this.cipher = Cipher.getInstance("RSA");
		this.publicSignature = Signature.getInstance("SHA256withRSA");
		this.passPhrase = passPhrase;
		this.alias = alias;
		this.keyStoreFile = keyStoreFile;
	}

	public static KeyPair generateKeyPair() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048, new SecureRandom());
		KeyPair pair = generator.generateKeyPair();

		return pair;
	}

	public KeyPair getKeyPairFromKeyStore() throws Exception {
		// Generated with:
		// keytool -genkeypair -alias yek -storepass terces -keypass terces -keyalg RSA
		// -keystore keystore.jks

		InputStream ins = RSAEncryption.class.getResourceAsStream(File.separator + this.keyStoreFile);

		KeyStore keyStore = KeyStore.getInstance("JCEKS");
		keyStore.load(ins, this.passPhrase.toCharArray()); // Keystore password
		KeyStore.PasswordProtection keyPassword = // Key password
				new KeyStore.PasswordProtection(this.passPhrase.toCharArray());

		KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(this.alias, keyPassword);

		java.security.cert.Certificate cert = keyStore.getCertificate(this.alias);
		PublicKey publicKey = cert.getPublicKey();
		PrivateKey privateKey = privateKeyEntry.getPrivateKey();

		return new KeyPair(publicKey, privateKey);
	}

	public String getPublicKeyString(KeyPair keyPair) {
		byte[] pubKey = keyPair.getPublic().getEncoded();
		return Base64.getEncoder().encodeToString(pubKey);
		
	}
	
	public PublicKey getPublicKeyFromString(String publicKeyString) throws InvalidKeySpecException, NoSuchAlgorithmException {
		byte[] data = Base64.getDecoder().decode(publicKeyString);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
	    return this.keyFactory.generatePublic(spec);
		
	}
	
	public PrivateKey getPrivateKeyFromString(String privateStringKey) throws GeneralSecurityException {
	    byte[] clear = Base64.getDecoder().decode(privateStringKey);
	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
	    PrivateKey privateKey = this.keyFactory.generatePrivate(keySpec);
	    Arrays.fill(clear, (byte) 0);
	    return privateKey;
	}
	
	private File writeToFile(File output, byte[] toWrite)
			throws IllegalBlockSizeException, BadPaddingException, IOException {
		FileOutputStream fos = new FileOutputStream(output);
		fos.write(toWrite);
		fos.flush();
		fos.close();
		return output;
	}

	public File encryptFile(byte[] input, File output, PrivateKey privateKey)
			throws IOException, GeneralSecurityException {

		this.cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return writeToFile(output, this.cipher.doFinal(input));
	}

	public File decryptFile(byte[] input, File output, PublicKey publicKey)
			throws IOException, GeneralSecurityException {
		this.cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return writeToFile(output, this.cipher.doFinal(input));
	}

	public String encryptText(String msg, PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException,
			UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
		this.cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return Base64.getEncoder().encodeToString(cipher.doFinal(msg.getBytes("UTF-8")));
	}

	public String decryptText(String msg, PrivateKey privateKey)
			throws InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		byte[] bytes = Base64.getDecoder().decode(msg);

		this.cipher.init(Cipher.DECRYPT_MODE, privateKey);

		return new String(this.cipher.doFinal(bytes), UTF_8);
	}

	public String sign(String plainText, PrivateKey privateKey) throws Exception {
		this.publicSignature.initSign(privateKey);
		this.publicSignature.update(plainText.getBytes(UTF_8));

		byte[] signature = this.publicSignature.sign();

		return Base64.getEncoder().encodeToString(signature);
	}
	
	public  boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
        
        this.publicSignature.initVerify(publicKey);
        this.publicSignature.update(plainText.getBytes(UTF_8));

        byte[] signatureBytes = Base64.getDecoder().decode(signature);

        return this.publicSignature.verify(signatureBytes);
	}
	
}
