package com.mrepol742.cryptographysystem;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
/**
 *
 * @author mrepol742
 */
public class AESEncode {
    public static String get(String str, String key, String saltPhase) {
        try {
           return Base64.getEncoder().encodeToString(AESCipher.get(key, saltPhase, Cipher.ENCRYPT_MODE).doFinal(str.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
        }
        return null;
    }
}