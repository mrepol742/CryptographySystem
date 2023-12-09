package com.mrepol742.cryptographysystem;

/**
 *
 * @author mrepol742
 */
public class RSACipher {
    private String privateKey;
    private String publicKey;
    private String content;
    
    private RSACipher() {
    }
    
    public RSACipher(String privateKey, String publicKey, String content) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.content = content;
    }
    
    public String getPrivateKey() {
        return privateKey;
    }
    
    public String getPublickKey() {
        return publicKey;
    }
    
    public String getContent() {
        return content;
    }
}
