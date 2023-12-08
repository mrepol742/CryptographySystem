package com.mrepol742.cryptographysystem;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
/**
 *
 * @author mrepol742
 */
public class AESEncode {
    public static String get(String str, String key, String saltPhase) {
        try {
            byte[] str1 = AESCipher.get(key, saltPhase, Cipher.ENCRYPT_MODE).doFinal(str.getBytes(StandardCharsets.UTF_8));
           return Base64.getEncoder().withoutPadding().encodeToString(str1);
        } catch (Exception e) {
              Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}