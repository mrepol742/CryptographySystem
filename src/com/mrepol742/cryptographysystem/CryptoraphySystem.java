package com.mrepol742.cryptographysystem;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

        //  sleepThread();
        java.awt.EventQueue.invokeLater(() -> {
            try {
                StringBuilder sb = new StringBuilder();
                FileReader fr = new FileReader(".theme");
                BufferedReader br = new BufferedReader(fr);
                String ln;
                while ((ln = br.readLine()) != null) {
                    sb.append(ln);
                }
                fr.close();
                br.close();

                if (sb.toString().contains("0")) {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                } else {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                }
                new Main().setVisible(true);
                Logger.getLogger(Main.class.getName()).log(Level.INFO, "APPLICATION STARTED");
            } catch (UnsupportedLookAndFeelException | IOException ex) {
                Logger.getLogger(CryptoraphySystem.class.getName()).log(Level.SEVERE, null, ex);
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
