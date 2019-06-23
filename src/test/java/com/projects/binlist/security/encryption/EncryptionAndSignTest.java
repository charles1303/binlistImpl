package com.projects.binlist.security.encryption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.security.KeyPair;
import java.security.PublicKey;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.projects.binlist.utils.security.AESEncryption;
import com.projects.binlist.utils.security.RSAEncryption;

@RunWith(MockitoJUnitRunner.class)
public class EncryptionAndSignTest {
	
	@Test
	public void testRSA() throws Exception {
		RSAEncryption rsa = new RSAEncryption();
        KeyPair pair = RSAEncryption.generateKeyPair();
        
        //Get public key as string
        String pubKeyStr = rsa.getPublicKeyString(pair);
        
        PublicKey pubKey = rsa.getPublicKeyFromString(pubKeyStr);
        
        //Our secret message
        String message = "this is the message to encrypt";
        
      //Let's sign the message
        String signatureOne = rsa.sign(message, pair.getPrivate());

        //Encrypt the message
        String cipherText = rsa.encryptText(message, pair.getPublic());
        
      //Now decrypt it
        String decipheredMessage = rsa.decryptText(cipherText, pair.getPrivate());
        assertEquals(message, decipheredMessage);
        
      //Encrypt the message using the public key gotten from the string
        String cipherText2 = rsa.encryptText(message, pubKey);
        
      //Now decrypt second cipherText encrypted using public key gotten from string
        String decipheredMessage2 = rsa.decryptText(cipherText2, pair.getPrivate());
        assertEquals(message, decipheredMessage2);
        assertEquals(decipheredMessage, decipheredMessage2);
        
      //Let's check the signature
        boolean isValid = rsa.verify(decipheredMessage, signatureOne, pair.getPublic());
        
        assertTrue(isValid);

        //Let's sign another message
        String signatureTwo = rsa.sign("sign", pair.getPrivate());

        //Let's check the signature
        assertTrue(rsa.verify("sign", signatureTwo, pair.getPublic()));
        
        
      //Let's check the first message with the new signature
        boolean isFalse = rsa.verify(decipheredMessage, signatureTwo, pair.getPublic());
        
        assertFalse(isFalse);
	}
	
	@Test
	public void testAES () throws Exception {
		
		String message = "this is the message to encrypt";
        String passPhrase = "secret";
 
        AESEncryption encrypter = new AESEncryption(passPhrase);
        String encrypted = encrypter.encrypt(message);
        String decrypted = encrypter.decrypt(encrypted);
        
        assertNotEquals(message, encrypted);
        assertNotEquals(decrypted, encrypted);
        assertEquals(message, decrypted);
		
	}

}
