package com.mrepol742.cryptographysystem;

/**
 *
 * @author mrepol742
 */
public class CryptoraphySystem {

    public static void main(String[] args) {
        sleepThread();

        java.awt.EventQueue.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }

    private static void sleepThread() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }
    }
}
