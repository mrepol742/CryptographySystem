package com.mrepol742.cryptographysystem;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;

/**
 *
 * @author mrepol742
 */

public class AESDecode {

    public static String get(String str, String key, String saltPhase) {
        try {
            return new String(AESCipher.get(key, saltPhase, Cipher.DECRYPT_MODE).doFinal(Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8))));
        } catch (Exception e) {
        }
        return null;
    }
}