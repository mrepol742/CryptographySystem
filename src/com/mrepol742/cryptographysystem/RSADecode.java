package com.mrepol742.cryptographysystem;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author mrepol742
 */
public class RSADecode {

    private RSADecode() {
    }

    public static String get(String secretKey, String privateKey, String encryptedText) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {

        byte[] encryptedPrivateKey = Base64.getDecoder().decode(privateKey.getBytes(StandardCharsets.UTF_8));
        byte[] encryptedSecretKey = Base64.getDecoder().decode(secretKey.getBytes(StandardCharsets.UTF_8));
        
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpecPv = new PKCS8EncodedKeySpec(encryptedPrivateKey);
        PrivateKey privateKey1 = (RSAPrivateKey) kf.generatePrivate(keySpecPv);
  
        Cipher aesCipher = Cipher.getInstance("RSA");
        aesCipher.init(Cipher.PRIVATE_KEY, privateKey1);
        byte[] decryptedKey = aesCipher.doFinal(encryptedSecretKey);

        SecretKey originalKey = new SecretKeySpec(decryptedKey, 0, decryptedKey.length, "AES");
        Cipher aesCipher1 = Cipher.getInstance("AES");
        aesCipher1.init(Cipher.DECRYPT_MODE, originalKey);
        byte[] decodeStr = Base64.getDecoder().decode(encryptedText.getBytes(StandardCharsets.UTF_8));
        byte[] bytePlainText = aesCipher1.doFinal(decodeStr);
        String plainText = new String(bytePlainText);
        return plainText;
       
        /*
        byte[] publicKeyBytes = Base64.getDecoder().decode(secretKey.getBytes(StandardCharsets.UTF_8));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        Cipher decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePublic(publicKeySpec));
        byte[] decryptedMessageBytes = decryptCipher.doFinal(Base64.getDecoder().decode(encryptedText));
        String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
        return AESDecode.get(decryptedMessage, "a", "a");
         */
    }
}