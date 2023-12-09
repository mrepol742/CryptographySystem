package com.mrepol742.cryptographysystem;

/**
 *
 * @author mrepol742
 */
public class CeasarCipher {
    
    private CeasarCipher() {
        
    }
    
     public static String encode(String text, int s) {
        StringBuilder result = new StringBuilder();
        int length = text.length();
        int i;
        for (i = 0; i < length; i++) {
            if (Character.isUpperCase(text.charAt(i))) {
                char ch = (char) (((int) text.charAt(i) + s - 65) % 26 + 65);
                result.append(ch);
            } else if (Character.isLowerCase(text.charAt(i))) {
                char ch = (char) (((int) text.charAt(i) + s - 97) % 26 + 97);
                result.append(ch);
            } else {
                result.append(text.charAt(i));
            }
        }
        return result.toString();
    }
}
