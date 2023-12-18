package com.mrepol742.cryptographysystem;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author mrepol742
 */
public class RSAEncode {

    private RSAEncode() {
    }

    public static RSACipher get(String plainText) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {

        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        SecretKey secKey = generator.generateKey();
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        String encryptedCipherText = Base64.getEncoder().withoutPadding().encodeToString(byteCipherText);

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
         Logger.getLogger(Main.class.getName()).log(Level.SEVERE, kpg.getProvider().getName());
  
        KeyPair keyPair = kpg.generateKeyPair();
        PublicKey puKey = keyPair.getPublic();
        PrivateKey prKey = keyPair.getPrivate();
        
        String privateKeyBase64 = Base64.getEncoder().withoutPadding().encodeToString(prKey.getEncoded());
        
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.PUBLIC_KEY, puKey);
        byte[] encryptedKey = cipher.doFinal(secKey.getEncoded());
        String encryptedKe = Base64.getEncoder().withoutPadding().encodeToString(encryptedKey);

          Logger.getLogger(Main.class.getName()).log(Level.SEVERE, String.valueOf(prKey.getEncoded()));
        return new RSACipher(privateKeyBase64,encryptedKe, encryptedCipherText);

        /*
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        String privateKeyBase64 = Base64.getEncoder().withoutPadding().encodeToString(privateKey.getEncoded());
        String publicKeyBase64 = Base64.getEncoder().withoutPadding().encodeToString(publicKey.getEncoded());
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] secretMessageBytes = AESEncode.get(plainText, "a", "a").getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
        String encryptedBytes = Base64.getEncoder().withoutPadding().encodeToString(encryptedMessageBytes);
        return new RSACipher(privateKeyBase64, publicKeyBase64, encryptedBytes);
         */
    }
}