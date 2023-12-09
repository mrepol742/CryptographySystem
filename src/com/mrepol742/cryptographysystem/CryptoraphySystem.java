package com.mrepol742.cryptographysystem;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author mrepol742
 */
public class CryptoraphySystem {

    public static void main(String[] args) {

        sleepThread();

        java.awt.EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatDarkLaf());
                new Main().setVisible(true);
                Logger.getLogger(Main.class.getName()).log(Level.INFO, "APPLICATION STARTED");
            } catch (UnsupportedLookAndFeelException ex) {
            }
        });
    }

    private static void sleepThread() {
        try {
             Logger.getLogger(Main.class.getName()).log(Level.INFO, "APPLICATION INIT");
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }
    }
}
