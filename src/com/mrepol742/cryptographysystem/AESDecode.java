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

public class AESDecode {
    
    private AESDecode() {
    }

    public static String get(String str, String key, String saltPhase) {
        try {
            return new String(AESCipher.get(key, saltPhase, Cipher.DECRYPT_MODE).doFinal(Base64.getDecoder().decode(str)));
        } catch (Exception e) {
              Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}