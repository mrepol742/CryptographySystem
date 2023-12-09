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
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author mrepol742
 */
public class RSAEncode {
    
    private RSAEncode() {
    }

    public static RSACipher get(String plainText) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        String privateKeyBase64 = Base64.getEncoder().withoutPadding().encodeToString(privateKey.getEncoded());
        String publicKeyBase64 = Base64.getEncoder().withoutPadding().encodeToString(publicKey.getEncoded());
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] secretMessageBytes = plainText.getBytes();
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
        String encryptedBytes = Base64.getEncoder().withoutPadding().encodeToString(encryptedMessageBytes);
        return new RSACipher(privateKeyBase64, publicKeyBase64, encryptedBytes);
    }
}