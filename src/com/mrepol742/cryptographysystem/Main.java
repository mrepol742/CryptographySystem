package com.mrepol742.cryptographysystem;

import java.awt.Color;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author mrepol742
 */
public class Main extends javax.swing.JFrame {

    private int index = 0;
    private boolean isEncode = true;
    private String actionType = null;
    private Color blue = new Color(66, 133, 244);
    private Color defaultBackground = new javax.swing.JButton().getBackground();
    private Color defaultForeground = new javax.swing.JButton().getForeground();
    private Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png"));
    private Icon icon = new ImageIcon(image);
    private EncryptedFile encryptedFile = null;

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        setResizable(false);
        setIconImage(image);
        setTitle("Cryptography System - @mrepol742");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        encode.setBackground(blue);
        encode.setForeground(Color.WHITE);
        encode.setOpaque(true);
        decode.setOpaque(true);
        /*
        * DISABLE BUTTONS LABEL OUTLINE ON FOCUSED
         */
        encode.setFocusPainted(false);
        decode.setFocusPainted(false);
        settings.setFocusPainted(false);
        ceasarPaste.setFocusPainted(false);
        ceasarCopy.setFocusPainted(false);
        symmetricPaste.setFocusPainted(false);
        symmetricCopy.setFocusPainted(false);
        symmetricSecretKeyRandom.setFocusPainted(false);
        symmetricSaltPhraseRandom.setFocusPainted(false);
        hybridPaste.setFocusPainted(false);
        hybridCopyCipher.setFocusPainted(false);
        symmetricPrimaryActionButton.setFocusPainted(false);
        ceasarPaddingInput.setText("3");
        /*
        * DISABLE OUTPUT TEXT AREA
         */
        ceasarOutputTextArea.setEditable(false);
        symmetricOutputTextArea.setEditable(false);
        hybridOutputTextArea.setEditable(false);
        hybridPrivateKeyTextArea.setEditable(false);
        hybridPublicKeyTextArea.setEditable(false);
        symmetricFileChooser.setEditable(false);
        symmetricSaveFileTextField.setEditable(false);
        /*
        * ENABLE LINE WRAPPING
         */
        ceasarInputTextArea.setLineWrap(true);
        ceasarOutputTextArea.setLineWrap(true);
        symmetricInputTextArea.setLineWrap(true);
        symmetricOutputTextArea.setLineWrap(true);
        hybridInputTextArea.setLineWrap(true);
        hybridOutputTextArea.setLineWrap(true);
        hybridPrivateKeyTextArea.setLineWrap(true);
        hybridPublicKeyTextArea.setLineWrap(true);
        /*
        * TEXTAREA's ON TEXT CHANGED LISTENERS
         */
        ceasarPaddingInput.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                warn();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                warn();
            }

            public void warn() {
                try {
                    if (ceasarPaddingInput.getText().trim().equals("")) {
                        return;
                    }

                    int input = Integer.parseInt(ceasarPaddingInput.getText());

                    if (input <= 0) {
                        showErrorMessage("Please enter number bigger than 0!");
                        Logger.getLogger(Main.class.getName()).log(Level.INFO, "Invalid padding value: {0}", input);
                    } else if (input > 26) {
                        showErrorMessage("Please enter number not bigger than 26!");
                        Logger.getLogger(Main.class.getName()).log(Level.INFO, "Invalid padding value: {0}", input);
                    }
                } catch (HeadlessException | NumberFormatException ex) {
                    showErrorMessage("Please enter only number!");
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        ceasarInputTextArea.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                caesarCipher();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                caesarCipher();
            }
        });
    }

    private void hybridRSA() {
        if (hybridInputTextArea.getText().trim().equals("")) {
            hybridOutputTextArea.setText("");
            hybridPrivateKeyTextArea.setText("");
            hybridPublicKeyTextArea.setText("");
            return;
        }

        if (isEncode) {
            try {
                RSACipher encryptMode = RSAEncode.get(hybridInputTextArea.getText());

                hybridPrivateKeyTextArea.setText(encryptMode.getPrivateKey());
                hybridPublicKeyTextArea.setText(encryptMode.getPublickKey());
                hybridOutputTextArea.setText(encryptMode.getContent());

            } catch (NoSuchPaddingException ex) {
                showErrorMessage("Unable to encrypt the input due to padding issues!");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeyException ex) {
                showErrorMessage("Unable to encrypt the input due to invalid key!");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalBlockSizeException ex) {
                showErrorMessage("Maximum block size of 256! Please reduce the input text or use Hybrid!");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException | BadPaddingException ex) {
                showErrorMessage("Unable to encrypt the provided input!");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                String decryptMode = RSADecode.get(hybridPublicKeyTextArea.getText(),
                                hybridPrivateKeyTextArea.getText(),
                                hybridInputTextArea.getText());
                hybridOutputTextArea.setText(decryptMode);
            } catch (NoSuchPaddingException ex) {
                showErrorMessage("Unable to decrypt the input due to padding issues!");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeySpecException | InvalidKeyException ex) {
                showErrorMessage("Unable to decrypt the input due to invalid key!");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalBlockSizeException ex) {
                showErrorMessage("Maximum block size of 256! Please reduce the input text or use Hybrid!");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException | BadPaddingException ex) {
                showErrorMessage("Unable to decrypt the provided input!");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void symmetricAES() {
        if (symmetricInputTextArea.getText().trim().equals("")) {
            symmetricOutputTextArea.setText("");
            return;
        }
        String plain = symmetricInputTextArea.getText();
        String secretKey = symmetricSecretKeyInput.getText();
        String saltPhrase = symmetricSaltPhraseInput.getText();
        if (secretKey.trim().isEmpty()) {
            showErrorMessage("Invalid secret key value!");
            Logger.getLogger(Main.class.getName()).log(Level.INFO, "Invalid secret key value");
        } else if (saltPhrase.trim().isEmpty()) {
            showErrorMessage("Invalid salt phrase value!");
            Logger.getLogger(Main.class.getName()).log(Level.INFO, "Invalid salt phrase value");
        } else {
            if (isEncode) {
                String encode = AESEncode.get(plain, secretKey, saltPhrase);
                if (encode == null) {
                    showErrorMessage("Unable to encode the provided input!");
                    Logger.getLogger(Main.class.getName()).log(Level.INFO, "Unable to encode!");
                } else {
                    symmetricOutputTextArea.setText(encode);
                }
            } else {
                String decode = AESDecode.get(plain, secretKey, saltPhrase);
                if (decode == null) {
                    showErrorMessage("Unable to decode the provided input!");
                    Logger.getLogger(Main.class.getName()).log(Level.INFO, "Unable to decode!");
                } else {
                    symmetricOutputTextArea.setText(decode);
                }
            }
        }
    }

    private void caesarCipher() {
        if (ceasarInputTextArea.getText().trim().equals("")) {
            ceasarOutputTextArea.setText("");
            return;
        }
        String plain = ceasarInputTextArea.getText();
        try {
            int padding = Integer.parseInt(ceasarPaddingInput.getText());
            if (padding <= 0 || padding > 26) {
                showErrorMessage("Invalid padding value!");
                Logger.getLogger(Main.class.getName()).log(Level.INFO, "Invalid padding value: {0}", padding);
            } else {
                if (isEncode) {
                    ceasarOutputTextArea.setText(CeasarCipher.encode(plain, padding));
                } else {
                    ceasarOutputTextArea.setText(CeasarCipher.encode(plain, -padding));
                }
            }
        } catch (Exception ex) {
            showErrorMessage("Invalid padding value!");
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showErrorMessage(String message) {
        JOptionPane jp = new JOptionPane(message,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                icon);
        JDialog dialog = jp.createDialog(null, "Error");
        ((Frame) dialog.getParent()).setIconImage(image);
        dialog.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        crypto = new javax.swing.JTabbedPane();
        ceasar = new javax.swing.JPanel();
        ceasarOutputLabel = new javax.swing.JLabel();
        ceasarInputText = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ceasarInputTextArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        ceasarOutputTextArea = new javax.swing.JTextArea();
        ceasarPaddingLabel = new javax.swing.JLabel();
        ceasarPaddingInput = new javax.swing.JTextField();
        ceasarInputPaste = new javax.swing.JButton();
        ceasarPaste = new javax.swing.JButton();
        ceasarCopy = new javax.swing.JButton();
        symmetric = new javax.swing.JPanel();
        ceasar1 = new javax.swing.JPanel();
        symmetricOutputLabel = new javax.swing.JLabel();
        symmetricInputLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        symmetricInputTextArea = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        symmetricOutputTextArea = new javax.swing.JTextArea();
        symmetricSecretKeyLabel = new javax.swing.JLabel();
        symmetricSecretKeyInput = new javax.swing.JTextField();
        ceasarInputPaste1 = new javax.swing.JButton();
        symmetricPaste = new javax.swing.JButton();
        symmetricCopy = new javax.swing.JButton();
        symmetricSaltPhraseLabel = new javax.swing.JLabel();
        symmetricSaltPhraseInput = new javax.swing.JTextField();
        symmetricSecretKeyRandom = new javax.swing.JButton();
        symmetricSaltPhraseRandom = new javax.swing.JButton();
        symmetricPrimaryActionButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        symmetricFileChooser = new javax.swing.JTextField();
        symmetricMainActionFile = new javax.swing.JButton();
        symmetricSaveFileTextField = new javax.swing.JTextField();
        symmetricSaveFile = new javax.swing.JButton();
        hybrid = new javax.swing.JPanel();
        ceasar3 = new javax.swing.JPanel();
        asymmetricOutputLabel1 = new javax.swing.JLabel();
        asymmetricInputLabel1 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        asymmetricInputTextArea1 = new javax.swing.JTextArea();
        jScrollPane10 = new javax.swing.JScrollPane();
        asymmetricOutputTextArea1 = new javax.swing.JTextArea();
        asymmetricPrivateKeyLabel1 = new javax.swing.JLabel();
        ceasarInputPaste3 = new javax.swing.JButton();
        asymmetricPaste1 = new javax.swing.JButton();
        asymmetricCopyCipher1 = new javax.swing.JButton();
        asymmetricPublicKeyLabel1 = new javax.swing.JLabel();
        asymmetricPrimaryActionButton1 = new javax.swing.JButton();
        asymmetricCopyPrivateKey1 = new javax.swing.JButton();
        asymmetricCopyPublicKey1 = new javax.swing.JButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        asymmetricPrivateKeyTextArea1 = new javax.swing.JTextArea();
        jScrollPane12 = new javax.swing.JScrollPane();
        asymmetricPublicKeyTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        asymmetric = new javax.swing.JPanel();
        ceasar2 = new javax.swing.JPanel();
        hybridOutputLabel = new javax.swing.JLabel();
        hybridInputLabel = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        hybridInputTextArea = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        hybridOutputTextArea = new javax.swing.JTextArea();
        hybridPrivateKeyLabel = new javax.swing.JLabel();
        ceasarInputPaste2 = new javax.swing.JButton();
        hybridPaste = new javax.swing.JButton();
        hybridCopyCipher = new javax.swing.JButton();
        hybridPublicKeyLabel = new javax.swing.JLabel();
        hybridPrimaryActionButton = new javax.swing.JButton();
        hybridCopyPrivateKey = new javax.swing.JButton();
        hybridCopyPublicKey = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        hybridPrivateKeyTextArea = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        hybridPublicKeyTextArea = new javax.swing.JTextArea();
        hybridMainActionFile1 = new javax.swing.JButton();
        hybridSaveFileTextField1 = new javax.swing.JTextField();
        hybridSaveFile1 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        hybridFileChooser1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        decode = new javax.swing.JButton();
        encode = new javax.swing.JButton();
        settings = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(790, 500));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Source Code Pro Black", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CRYPTOGRAPHY SYSTEM");
        jLabel1.setFocusable(false);
        jPanel1.add(jLabel1, java.awt.BorderLayout.CENTER);
        jLabel1.getAccessibleContext().setAccessibleDescription("");

        crypto.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        crypto.setToolTipText("");
        crypto.setFocusable(false);
        crypto.setFont(new java.awt.Font("Noto Sans", 1, 12)); // NOI18N
        crypto.setName(""); // NOI18N
        crypto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cryptoFocusGained(evt);
            }
        });
        crypto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cryptoMousePressed(evt);
            }
        });

        ceasar.setInheritsPopupMenu(true);
        ceasar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                ceasarMouseMoved(evt);
            }
        });

        ceasarOutputLabel.setText("Cipher Text:");

        ceasarInputText.setText("Plain Text:");

        ceasarInputTextArea.setColumns(20);
        ceasarInputTextArea.setRows(5);
        ceasarInputTextArea.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                ceasarInputTextAreaInputMethodTextChanged(evt);
            }
        });
        jScrollPane1.setViewportView(ceasarInputTextArea);

        ceasarOutputTextArea.setColumns(20);
        ceasarOutputTextArea.setRows(5);
        jScrollPane2.setViewportView(ceasarOutputTextArea);

        ceasarPaddingLabel.setText("Padding:");

        ceasarPaddingInput.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                ceasarPaddingInputInputMethodTextChanged(evt);
            }
        });
        ceasarPaddingInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ceasarPaddingInputActionPerformed(evt);
            }
        });

        ceasarInputPaste.setText("Paste");
        ceasarInputPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ceasarInputPasteActionPerformed(evt);
            }
        });

        ceasarPaste.setText("Paste");
        ceasarPaste.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ceasarPasteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ceasarPasteMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ceasarPasteMousePressed(evt);
            }
        });
        ceasarPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ceasarPasteActionPerformed(evt);
            }
        });

        ceasarCopy.setText("Copy");
        ceasarCopy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ceasarCopyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ceasarCopyMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ceasarCopyMousePressed(evt);
            }
        });
        ceasarCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ceasarCopyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ceasarLayout = new javax.swing.GroupLayout(ceasar);
        ceasar.setLayout(ceasarLayout);
        ceasarLayout.setHorizontalGroup(
            ceasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ceasarLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(ceasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(ceasarLayout.createSequentialGroup()
                        .addComponent(ceasarPaddingLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ceasarPaddingInput, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ceasarLayout.createSequentialGroup()
                        .addComponent(ceasarInputText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ceasarInputPaste, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ceasarPaste, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14)
                .addGroup(ceasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(ceasarLayout.createSequentialGroup()
                        .addComponent(ceasarOutputLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ceasarCopy, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        ceasarLayout.setVerticalGroup(
            ceasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ceasarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ceasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ceasarPaddingLabel)
                    .addComponent(ceasarPaddingInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ceasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ceasarInputPaste, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ceasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ceasarInputText)
                        .addComponent(ceasarOutputLabel)
                        .addComponent(ceasarPaste, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ceasarCopy)))
                .addGap(9, 9, 9)
                .addGroup(ceasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        crypto.addTab("Ceasar", ceasar);

        ceasar1.setInheritsPopupMenu(true);
        ceasar1.setPreferredSize(new java.awt.Dimension(778, 379));
        ceasar1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                ceasar1MouseMoved(evt);
            }
        });

        symmetricOutputLabel.setText("Cipher Text:");

        symmetricInputLabel.setText("Plain Text:");

        symmetricInputTextArea.setColumns(20);
        symmetricInputTextArea.setRows(5);
        symmetricInputTextArea.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                symmetricInputTextAreaInputMethodTextChanged(evt);
            }
        });
        jScrollPane3.setViewportView(symmetricInputTextArea);

        symmetricOutputTextArea.setColumns(20);
        symmetricOutputTextArea.setRows(5);
        jScrollPane4.setViewportView(symmetricOutputTextArea);

        symmetricSecretKeyLabel.setText("Secret Key:");

        symmetricSecretKeyInput.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                symmetricSecretKeyInputInputMethodTextChanged(evt);
            }
        });
        symmetricSecretKeyInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                symmetricSecretKeyInputActionPerformed(evt);
            }
        });

        ceasarInputPaste1.setText("Paste");
        ceasarInputPaste1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ceasarInputPaste1ActionPerformed(evt);
            }
        });

        symmetricPaste.setText("Paste");
        symmetricPaste.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                symmetricPasteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                symmetricPasteMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                symmetricPasteMousePressed(evt);
            }
        });
        symmetricPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                symmetricPasteActionPerformed(evt);
            }
        });

        symmetricCopy.setText("Copy");
        symmetricCopy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                symmetricCopyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                symmetricCopyMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                symmetricCopyMousePressed(evt);
            }
        });
        symmetricCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                symmetricCopyActionPerformed(evt);
            }
        });

        symmetricSaltPhraseLabel.setText("Salt Phrase:");

        symmetricSecretKeyRandom.setText("Random");
        symmetricSecretKeyRandom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                symmetricSecretKeyRandomMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                symmetricSecretKeyRandomMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                symmetricSecretKeyRandomMousePressed(evt);
            }
        });
        symmetricSecretKeyRandom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                symmetricSecretKeyRandomActionPerformed(evt);
            }
        });

        symmetricSaltPhraseRandom.setText("Random");
        symmetricSaltPhraseRandom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                symmetricSaltPhraseRandomMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                symmetricSaltPhraseRandomMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                symmetricSaltPhraseRandomMousePressed(evt);
            }
        });

        symmetricPrimaryActionButton.setText("Encrypt");
        symmetricPrimaryActionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                symmetricPrimaryActionButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                symmetricPrimaryActionButtonMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                symmetricPrimaryActionButtonMousePressed(evt);
            }
        });

        jLabel2.setText("OR");

        symmetricFileChooser.setText("Select file...");
        symmetricFileChooser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                symmetricFileChooserMousePressed(evt);
            }
        });
        symmetricFileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                symmetricFileChooserActionPerformed(evt);
            }
        });

        symmetricMainActionFile.setText("Encrypt File >");
        symmetricMainActionFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                symmetricMainActionFileActionPerformed(evt);
            }
        });

        symmetricSaveFileTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                symmetricSaveFileTextFieldMousePressed(evt);
            }
        });

        symmetricSaveFile.setText("Save File");
        symmetricSaveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                symmetricSaveFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ceasar1Layout = new javax.swing.GroupLayout(ceasar1);
        ceasar1.setLayout(ceasar1Layout);
        ceasar1Layout.setHorizontalGroup(
            ceasar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ceasar1Layout.createSequentialGroup()
                .addGroup(ceasar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ceasar1Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(ceasar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(ceasar1Layout.createSequentialGroup()
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(ceasar1Layout.createSequentialGroup()
                                .addGroup(ceasar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ceasar1Layout.createSequentialGroup()
                                        .addGroup(ceasar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ceasar1Layout.createSequentialGroup()
                                                .addComponent(symmetricSecretKeyLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(symmetricSecretKeyInput))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ceasar1Layout.createSequentialGroup()
                                                .addComponent(symmetricInputLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(ceasarInputPaste1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(ceasar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(ceasar1Layout.createSequentialGroup()
                                                .addGap(26, 26, 26)
                                                .addComponent(symmetricPaste))
                                            .addGroup(ceasar1Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(symmetricSecretKeyRandom)))))
                                .addGroup(ceasar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(ceasar1Layout.createSequentialGroup()
                                        .addGap(15, 15, 15)
                                        .addComponent(symmetricSaltPhraseLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(symmetricSaltPhraseInput)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(symmetricSaltPhraseRandom))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ceasar1Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(symmetricOutputLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(symmetricCopy, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(ceasar1Layout.createSequentialGroup()
                                        .addGap(14, 14, 14)
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(ceasar1Layout.createSequentialGroup()
                                .addComponent(symmetricFileChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(symmetricMainActionFile, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(symmetricSaveFileTextField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(symmetricSaveFile, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(ceasar1Layout.createSequentialGroup()
                        .addGap(283, 283, 283)
                        .addComponent(symmetricPrimaryActionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(151, Short.MAX_VALUE))
        );
        ceasar1Layout.setVerticalGroup(
            ceasar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ceasar1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ceasar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(symmetricSecretKeyLabel)
                    .addComponent(symmetricSecretKeyInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(symmetricSaltPhraseLabel)
                    .addComponent(symmetricSaltPhraseInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(symmetricSecretKeyRandom)
                    .addComponent(symmetricSaltPhraseRandom))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ceasar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ceasarInputPaste1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ceasar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(symmetricInputLabel)
                        .addComponent(symmetricPaste, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(symmetricCopy)
                        .addComponent(symmetricOutputLabel)))
                .addGap(9, 9, 9)
                .addGroup(ceasar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .addComponent(jScrollPane4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(symmetricPrimaryActionButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ceasar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ceasar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(symmetricFileChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(symmetricMainActionFile)
                    .addComponent(symmetricSaveFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(symmetricSaveFile))
                .addContainerGap(68, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout symmetricLayout = new javax.swing.GroupLayout(symmetric);
        symmetric.setLayout(symmetricLayout);
        symmetricLayout.setHorizontalGroup(
            symmetricLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, symmetricLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ceasar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        symmetricLayout.setVerticalGroup(
            symmetricLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, symmetricLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ceasar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        crypto.addTab("Symmetric", symmetric);

        ceasar3.setInheritsPopupMenu(true);
        ceasar3.setPreferredSize(new java.awt.Dimension(778, 379));
        ceasar3.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                ceasar3MouseMoved(evt);
            }
        });

        asymmetricOutputLabel1.setText("Cipher Text:");

        asymmetricInputLabel1.setText("Plain Text:");

        asymmetricInputTextArea1.setColumns(20);
        asymmetricInputTextArea1.setRows(5);
        asymmetricInputTextArea1.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                asymmetricInputTextArea1InputMethodTextChanged(evt);
            }
        });
        jScrollPane9.setViewportView(asymmetricInputTextArea1);

        asymmetricOutputTextArea1.setColumns(20);
        asymmetricOutputTextArea1.setRows(5);
        jScrollPane10.setViewportView(asymmetricOutputTextArea1);

        asymmetricPrivateKeyLabel1.setText("Secret Key:");

        ceasarInputPaste3.setText("Paste");
        ceasarInputPaste3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ceasarInputPaste3ActionPerformed(evt);
            }
        });

        asymmetricPaste1.setText("Paste");
        asymmetricPaste1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                asymmetricPaste1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                asymmetricPaste1MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                asymmetricPaste1MousePressed(evt);
            }
        });
        asymmetricPaste1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                asymmetricPaste1ActionPerformed(evt);
            }
        });

        asymmetricCopyCipher1.setText("Copy");
        asymmetricCopyCipher1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                asymmetricCopyCipher1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                asymmetricCopyCipher1MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                asymmetricCopyCipher1MousePressed(evt);
            }
        });
        asymmetricCopyCipher1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                asymmetricCopyCipher1ActionPerformed(evt);
            }
        });

        asymmetricPublicKeyLabel1.setText("Public Key:");

        asymmetricPrimaryActionButton1.setText("Encrypt");
        asymmetricPrimaryActionButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                asymmetricPrimaryActionButton1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                asymmetricPrimaryActionButton1MouseExited(evt);
            }
        });
        asymmetricPrimaryActionButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                asymmetricPrimaryActionButton1ActionPerformed(evt);
            }
        });

        asymmetricCopyPrivateKey1.setText("Copy");
        asymmetricCopyPrivateKey1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                asymmetricCopyPrivateKey1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                asymmetricCopyPrivateKey1MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                asymmetricCopyPrivateKey1MousePressed(evt);
            }
        });
        asymmetricCopyPrivateKey1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                asymmetricCopyPrivateKey1ActionPerformed(evt);
            }
        });

        asymmetricCopyPublicKey1.setText("Copy");
        asymmetricCopyPublicKey1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                asymmetricCopyPublicKey1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                asymmetricCopyPublicKey1MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                asymmetricCopyPublicKey1MousePressed(evt);
            }
        });
        asymmetricCopyPublicKey1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                asymmetricCopyPublicKey1ActionPerformed(evt);
            }
        });

        asymmetricPrivateKeyTextArea1.setColumns(20);
        asymmetricPrivateKeyTextArea1.setRows(5);
        jScrollPane11.setViewportView(asymmetricPrivateKeyTextArea1);

        asymmetricPublicKeyTextArea1.setColumns(20);
        asymmetricPublicKeyTextArea1.setRows(5);
        jScrollPane12.setViewportView(asymmetricPublicKeyTextArea1);

        jButton1.setText("Random");

        javax.swing.GroupLayout ceasar3Layout = new javax.swing.GroupLayout(ceasar3);
        ceasar3.setLayout(ceasar3Layout);
        ceasar3Layout.setHorizontalGroup(
            ceasar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ceasar3Layout.createSequentialGroup()
                .addGroup(ceasar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ceasar3Layout.createSequentialGroup()
                        .addGap(164, 164, 164)
                        .addComponent(ceasarInputPaste3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ceasar3Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(ceasar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ceasar3Layout.createSequentialGroup()
                                .addComponent(asymmetricPrivateKeyLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(asymmetricCopyPrivateKey1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(ceasar3Layout.createSequentialGroup()
                                .addGroup(ceasar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(ceasar3Layout.createSequentialGroup()
                                        .addComponent(asymmetricInputLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(asymmetricPaste1)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane11))))
                .addGap(29, 29, 29)
                .addGroup(ceasar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(ceasar3Layout.createSequentialGroup()
                        .addComponent(asymmetricOutputLabel1)
                        .addGap(134, 134, 134)
                        .addComponent(asymmetricCopyCipher1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane10)
                    .addGroup(ceasar3Layout.createSequentialGroup()
                        .addComponent(asymmetricPublicKeyLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(asymmetricCopyPublicKey1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane12))
                .addGap(24, 24, 24))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ceasar3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(asymmetricPrimaryActionButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(226, 226, 226))
        );
        ceasar3Layout.setVerticalGroup(
            ceasar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ceasar3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ceasar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ceasar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(asymmetricOutputLabel1)
                        .addComponent(asymmetricCopyCipher1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ceasar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(asymmetricInputLabel1)
                        .addComponent(asymmetricPaste1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(ceasarInputPaste3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ceasar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                    .addComponent(jScrollPane9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(asymmetricPrimaryActionButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ceasar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(asymmetricPublicKeyLabel1)
                    .addComponent(asymmetricPrivateKeyLabel1)
                    .addComponent(asymmetricCopyPrivateKey1)
                    .addComponent(asymmetricCopyPublicKey1)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ceasar3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout hybridLayout = new javax.swing.GroupLayout(hybrid);
        hybrid.setLayout(hybridLayout);
        hybridLayout.setHorizontalGroup(
            hybridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hybridLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ceasar3, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        hybridLayout.setVerticalGroup(
            hybridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hybridLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ceasar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        crypto.addTab("Asymmetric", hybrid);

        ceasar2.setInheritsPopupMenu(true);
        ceasar2.setPreferredSize(new java.awt.Dimension(778, 379));
        ceasar2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                ceasar2MouseMoved(evt);
            }
        });

        hybridOutputLabel.setText("Cipher Text:");

        hybridInputLabel.setText("Plain Text:");

        hybridInputTextArea.setColumns(20);
        hybridInputTextArea.setRows(5);
        hybridInputTextArea.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                hybridInputTextAreaInputMethodTextChanged(evt);
            }
        });
        jScrollPane5.setViewportView(hybridInputTextArea);

        hybridOutputTextArea.setColumns(20);
        hybridOutputTextArea.setRows(5);
        jScrollPane6.setViewportView(hybridOutputTextArea);

        hybridPrivateKeyLabel.setText("Private Key:");

        ceasarInputPaste2.setText("Paste");
        ceasarInputPaste2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ceasarInputPaste2ActionPerformed(evt);
            }
        });

        hybridPaste.setText("Paste");
        hybridPaste.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hybridPasteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                hybridPasteMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                hybridPasteMousePressed(evt);
            }
        });
        hybridPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hybridPasteActionPerformed(evt);
            }
        });

        hybridCopyCipher.setText("Copy");
        hybridCopyCipher.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hybridCopyCipherMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                hybridCopyCipherMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                hybridCopyCipherMousePressed(evt);
            }
        });
        hybridCopyCipher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hybridCopyCipherActionPerformed(evt);
            }
        });

        hybridPublicKeyLabel.setText("Secret Key:");

        hybridPrimaryActionButton.setText("Encrypt");
        hybridPrimaryActionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hybridPrimaryActionButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                hybridPrimaryActionButtonMouseExited(evt);
            }
        });
        hybridPrimaryActionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hybridPrimaryActionButtonActionPerformed(evt);
            }
        });

        hybridCopyPrivateKey.setText("Copy");
        hybridCopyPrivateKey.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hybridCopyPrivateKeyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                hybridCopyPrivateKeyMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                hybridCopyPrivateKeyMousePressed(evt);
            }
        });
        hybridCopyPrivateKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hybridCopyPrivateKeyActionPerformed(evt);
            }
        });

        hybridCopyPublicKey.setText("Copy");
        hybridCopyPublicKey.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hybridCopyPublicKeyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                hybridCopyPublicKeyMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                hybridCopyPublicKeyMousePressed(evt);
            }
        });
        hybridCopyPublicKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hybridCopyPublicKeyActionPerformed(evt);
            }
        });

        hybridPrivateKeyTextArea.setColumns(20);
        hybridPrivateKeyTextArea.setRows(5);
        jScrollPane7.setViewportView(hybridPrivateKeyTextArea);

        hybridPublicKeyTextArea.setColumns(20);
        hybridPublicKeyTextArea.setRows(5);
        jScrollPane8.setViewportView(hybridPublicKeyTextArea);

        hybridMainActionFile1.setText("Encrypt File >");
        hybridMainActionFile1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hybridMainActionFile1ActionPerformed(evt);
            }
        });

        hybridSaveFile1.setText("Save File");
        hybridSaveFile1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hybridSaveFile1ActionPerformed(evt);
            }
        });

        hybridFileChooser1.setText("Select file...");
        hybridFileChooser1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                hybridFileChooser1MousePressed(evt);
            }
        });
        hybridFileChooser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hybridFileChooser1ActionPerformed(evt);
            }
        });

        jLabel3.setText("OR");

        javax.swing.GroupLayout ceasar2Layout = new javax.swing.GroupLayout(ceasar2);
        ceasar2.setLayout(ceasar2Layout);
        ceasar2Layout.setHorizontalGroup(
            ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ceasar2Layout.createSequentialGroup()
                .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ceasar2Layout.createSequentialGroup()
                        .addGap(164, 164, 164)
                        .addComponent(ceasarInputPaste2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(ceasar2Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ceasar2Layout.createSequentialGroup()
                                .addComponent(hybridPrivateKeyLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(hybridCopyPrivateKey, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane7)
                            .addGroup(ceasar2Layout.createSequentialGroup()
                                .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(ceasar2Layout.createSequentialGroup()
                                        .addComponent(hybridInputLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(hybridPaste)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(37, 37, 37)
                .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(ceasar2Layout.createSequentialGroup()
                        .addComponent(hybridOutputLabel)
                        .addGap(134, 134, 134)
                        .addComponent(hybridCopyCipher, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane6)
                    .addGroup(ceasar2Layout.createSequentialGroup()
                        .addComponent(hybridPublicKeyLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(hybridCopyPublicKey, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane8))
                .addGap(24, 24, 24))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ceasar2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ceasar2Layout.createSequentialGroup()
                        .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(ceasar2Layout.createSequentialGroup()
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(ceasar2Layout.createSequentialGroup()
                                .addComponent(hybridFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hybridMainActionFile1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hybridSaveFileTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hybridSaveFile1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(33, 33, 33))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ceasar2Layout.createSequentialGroup()
                        .addComponent(hybridPrimaryActionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(256, 256, 256))))
        );
        ceasar2Layout.setVerticalGroup(
            ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ceasar2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(hybridOutputLabel)
                        .addComponent(hybridCopyCipher))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(hybridInputLabel)
                        .addComponent(hybridPaste)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ceasarInputPaste2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hybridPrimaryActionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hybridCopyPrivateKey)
                    .addComponent(hybridPrivateKeyLabel)
                    .addComponent(hybridPublicKeyLabel)
                    .addComponent(hybridCopyPublicKey))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hybridFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hybridMainActionFile1)
                    .addComponent(hybridSaveFileTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hybridSaveFile1))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout asymmetricLayout = new javax.swing.GroupLayout(asymmetric);
        asymmetric.setLayout(asymmetricLayout);
        asymmetricLayout.setHorizontalGroup(
            asymmetricLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(asymmetricLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ceasar2, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        asymmetricLayout.setVerticalGroup(
            asymmetricLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(asymmetricLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ceasar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        crypto.addTab("Hybrid", asymmetric);

        decode.setText("Decode");
        buttonGroup1.add(decode);
        decode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decodeActionPerformed(evt);
            }
        });

        encode.setText("Encode");
        buttonGroup1.add(encode);
        encode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                encodeActionPerformed(evt);
            }
        });

        settings.setText("Settings");
        settings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                settingsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                settingsMouseExited(evt);
            }
        });
        settings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addComponent(encode)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(decode)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(settings))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(crypto, javax.swing.GroupLayout.PREFERRED_SIZE, 775, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(9, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(encode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(decode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(settings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(crypto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        this.setLocationRelativeTo(null);
    }//GEN-LAST:event_formWindowOpened


    private void decodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decodeActionPerformed

        isEncode = false;

        decode.setBackground(blue);
        decode.setForeground(Color.WHITE);

        encode.setBackground(defaultBackground);
        encode.setForeground(defaultForeground);

        int index = crypto.getSelectedIndex();
        this.index = index;
        switch (index) {
            case 0 -> {
                ceasarInputText.setText("Cipher Text:");
                ceasarOutputLabel.setText("Plain Text:");
                ceasarInputTextArea.setText("");
                ceasarOutputTextArea.setText("");
                actionType = null;
                Logger.getLogger(Main.class.getName()).log(Level.INFO, "Decode Action Peformed: CEASAR");
            }
            case 1 -> {
                symmetricInputLabel.setText("Cipher Text:");
                symmetricOutputLabel.setText("Plain Text:");
                symmetricInputTextArea.setText("");
                symmetricOutputTextArea.setText("");
                symmetricSecretKeyRandom.setVisible(false);
                symmetricSaltPhraseRandom.setVisible(false);
                symmetricPrimaryActionButton.setText("Decrypt");
                symmetricMainActionFile.setText("Decrypt >");
                symmetricFileChooser.setText("");
                symmetricSaveFileTextField.setText("");
                encryptedFile = null;
                actionType = "SYMMETRIC";
                Logger.getLogger(Main.class.getName()).log(Level.INFO, "Decode Action Peformed: SYMMETRIC");
            }
            case 2 -> {

                encryptedFile = null;
                actionType = "ASYMMETRIC";
                Logger.getLogger(Main.class.getName()).log(Level.INFO, "Decode Action Peformed: ASYMMETRIC");
            }
            case 3 -> {
                hybridInputLabel.setText("Cipher Text:");
                hybridOutputLabel.setText("Plain Text:");
                hybridPrimaryActionButton.setText("Decrypt");
                hybridInputTextArea.setText("");
                hybridOutputTextArea.setText("");
                hybridFileChooser1.setText("");
                hybridSaveFileTextField1.setText("");
                hybridMainActionFile1.setText("Decrypt >");
                encryptedFile = null;
                actionType = "HYBRID";
                Logger.getLogger(Main.class.getName()).log(Level.INFO, "Decode Action Peformed: HYBRID");
            }
            default -> {
            }
        }
    }//GEN-LAST:event_decodeActionPerformed

    private void encodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encodeActionPerformed

        isEncode = true;

        encode.setBackground(blue);
        encode.setForeground(Color.WHITE);

        decode.setBackground(defaultBackground);
        decode.setForeground(defaultForeground);

        int index = crypto.getSelectedIndex();
        this.index = index;
        switch (index) {
            case 0 -> {
                ceasarInputText.setText("Plain Text:");
                ceasarOutputLabel.setText("Cipher Text:");
                ceasarInputTextArea.setText("");
                ceasarOutputTextArea.setText("");
                actionType = null;
                Logger.getLogger(Main.class.getName()).log(Level.INFO, "Encode Action Peformed: CEASAR");
            }
            case 1 -> {
                symmetricInputLabel.setText("Plain Text:");
                symmetricOutputLabel.setText("Cipher Text:");
                symmetricInputTextArea.setText("");
                symmetricOutputTextArea.setText("");
                symmetricSecretKeyRandom.setVisible(true);
                symmetricSaltPhraseRandom.setVisible(true);
                symmetricPrimaryActionButton.setText("Encrypt");
                symmetricMainActionFile.setText("Encrypt >");
                symmetricFileChooser.setText("");
                symmetricSaveFileTextField.setText("");
                encryptedFile = null;
                actionType = "SYMMETRIC";
                Logger.getLogger(Main.class.getName()).log(Level.INFO, "Encode Action Peformed: SYMMETRIC");
            }
            case 2 -> {

                encryptedFile = null;
                actionType = "ASYMMETRIC";
                Logger.getLogger(Main.class.getName()).log(Level.INFO, "Encode Action Peformed: ASYMMETRIC");
            }
            case 3 -> {
                hybridInputLabel.setText("Plain Text:");
                hybridOutputLabel.setText("Cipher Text:");
                hybridPrimaryActionButton.setText("Encrypt");
                hybridInputTextArea.setText("");
                hybridOutputTextArea.setText("");
                hybridFileChooser1.setText("");
                hybridSaveFileTextField1.setText("");
                hybridMainActionFile1.setText("Encrypt >");
                encryptedFile = null;
                actionType = "HYBRID";
                Logger.getLogger(Main.class.getName()).log(Level.INFO, "Encode Action Peformed: HYBRID");
            }
            default -> {
            }
        }
    }//GEN-LAST:event_encodeActionPerformed

    private void settingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsActionPerformed
        new Settings().setVisible(true);
    }//GEN-LAST:event_settingsActionPerformed

    private void settingsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsMouseEntered
        settings.setBackground(blue);
        settings.setForeground(Color.WHITE);
    }//GEN-LAST:event_settingsMouseEntered

    private void settingsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsMouseExited
        settings.setBackground(defaultBackground);
        settings.setForeground(defaultForeground);
    }//GEN-LAST:event_settingsMouseExited

    private void cryptoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cryptoFocusGained

    }//GEN-LAST:event_cryptoFocusGained

    private void ceasarMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ceasarMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_ceasarMouseMoved

    private void ceasarCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ceasarCopyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ceasarCopyActionPerformed

    private void ceasarCopyMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ceasarCopyMousePressed
        StringSelection stringSelection = new StringSelection(ceasarOutputTextArea.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_ceasarCopyMousePressed

    private void ceasarCopyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ceasarCopyMouseExited
        ceasarCopy.setBackground(defaultBackground);
        ceasarCopy.setForeground(defaultForeground);
    }//GEN-LAST:event_ceasarCopyMouseExited

    private void ceasarCopyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ceasarCopyMouseEntered
        ceasarCopy.setBackground(blue);
        ceasarCopy.setForeground(Color.WHITE);
    }//GEN-LAST:event_ceasarCopyMouseEntered

    private void ceasarPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ceasarPasteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ceasarPasteActionPerformed

    private void ceasarPasteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ceasarPasteMousePressed
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = c.getContents(this);
        if (t == null) {
            return;
        }
        try {
            ceasarInputTextArea.setText((String) t.getTransferData(DataFlavor.stringFlavor));
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ceasarPasteMousePressed

    private void ceasarPasteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ceasarPasteMouseExited
        ceasarPaste.setBackground(defaultBackground);
        ceasarPaste.setForeground(defaultForeground);
    }//GEN-LAST:event_ceasarPasteMouseExited

    private void ceasarPasteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ceasarPasteMouseEntered
        ceasarPaste.setBackground(blue);
        ceasarPaste.setForeground(Color.WHITE);
    }//GEN-LAST:event_ceasarPasteMouseEntered

    private void ceasarInputPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ceasarInputPasteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ceasarInputPasteActionPerformed

    private void ceasarPaddingInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ceasarPaddingInputActionPerformed

    }//GEN-LAST:event_ceasarPaddingInputActionPerformed

    private void ceasarPaddingInputInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_ceasarPaddingInputInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_ceasarPaddingInputInputMethodTextChanged

    private void ceasarInputTextAreaInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_ceasarInputTextAreaInputMethodTextChanged

    }//GEN-LAST:event_ceasarInputTextAreaInputMethodTextChanged

    private void ceasar1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ceasar1MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_ceasar1MouseMoved

    private void symmetricSaltPhraseRandomMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_symmetricSaltPhraseRandomMousePressed
        String randomString = RandomKey.get(10);
        symmetricSaltPhraseInput.setText(randomString);
    }//GEN-LAST:event_symmetricSaltPhraseRandomMousePressed

    private void symmetricSaltPhraseRandomMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_symmetricSaltPhraseRandomMouseExited
        symmetricSaltPhraseRandom.setBackground(defaultBackground);
        symmetricSaltPhraseRandom.setForeground(defaultForeground);
    }//GEN-LAST:event_symmetricSaltPhraseRandomMouseExited

    private void symmetricSaltPhraseRandomMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_symmetricSaltPhraseRandomMouseEntered
        symmetricSaltPhraseRandom.setBackground(blue);
        symmetricSaltPhraseRandom.setForeground(Color.WHITE);
    }//GEN-LAST:event_symmetricSaltPhraseRandomMouseEntered

    private void symmetricSecretKeyRandomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_symmetricSecretKeyRandomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_symmetricSecretKeyRandomActionPerformed

    private void symmetricSecretKeyRandomMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_symmetricSecretKeyRandomMousePressed
        String randomString = RandomKey.get(10);
        symmetricSecretKeyInput.setText(randomString);
    }//GEN-LAST:event_symmetricSecretKeyRandomMousePressed

    private void symmetricSecretKeyRandomMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_symmetricSecretKeyRandomMouseExited
        symmetricSecretKeyRandom.setBackground(defaultBackground);
        symmetricSecretKeyRandom.setForeground(defaultForeground);
    }//GEN-LAST:event_symmetricSecretKeyRandomMouseExited

    private void symmetricSecretKeyRandomMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_symmetricSecretKeyRandomMouseEntered
        symmetricSecretKeyRandom.setBackground(blue);
        symmetricSecretKeyRandom.setForeground(Color.WHITE);
    }//GEN-LAST:event_symmetricSecretKeyRandomMouseEntered

    private void symmetricCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_symmetricCopyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_symmetricCopyActionPerformed

    private void symmetricCopyMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_symmetricCopyMousePressed
        StringSelection stringSelection = new StringSelection(symmetricOutputTextArea.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_symmetricCopyMousePressed

    private void symmetricCopyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_symmetricCopyMouseExited
        symmetricCopy.setBackground(defaultBackground);
        symmetricCopy.setForeground(defaultForeground);
    }//GEN-LAST:event_symmetricCopyMouseExited

    private void symmetricCopyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_symmetricCopyMouseEntered
        symmetricCopy.setBackground(blue);
        symmetricCopy.setForeground(Color.WHITE);
    }//GEN-LAST:event_symmetricCopyMouseEntered

    private void symmetricPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_symmetricPasteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_symmetricPasteActionPerformed

    private void symmetricPasteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_symmetricPasteMousePressed
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = c.getContents(this);
        if (t == null) {
            return;
        }
        try {
            symmetricInputTextArea.setText((String) t.getTransferData(DataFlavor.stringFlavor));
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_symmetricPasteMousePressed

    private void symmetricPasteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_symmetricPasteMouseExited
        symmetricPaste.setBackground(defaultBackground);
        symmetricPaste.setForeground(defaultForeground);
    }//GEN-LAST:event_symmetricPasteMouseExited

    private void symmetricPasteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_symmetricPasteMouseEntered
        symmetricPaste.setBackground(blue);
        symmetricPaste.setForeground(Color.WHITE);
    }//GEN-LAST:event_symmetricPasteMouseEntered

    private void ceasarInputPaste1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ceasarInputPaste1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ceasarInputPaste1ActionPerformed

    private void symmetricSecretKeyInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_symmetricSecretKeyInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_symmetricSecretKeyInputActionPerformed

    private void symmetricSecretKeyInputInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_symmetricSecretKeyInputInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_symmetricSecretKeyInputInputMethodTextChanged

    private void symmetricInputTextAreaInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_symmetricInputTextAreaInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_symmetricInputTextAreaInputMethodTextChanged

    private void symmetricPrimaryActionButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_symmetricPrimaryActionButtonMouseEntered
        symmetricPrimaryActionButton.setBackground(blue);
        symmetricPrimaryActionButton.setForeground(Color.WHITE);
    }//GEN-LAST:event_symmetricPrimaryActionButtonMouseEntered

    private void symmetricPrimaryActionButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_symmetricPrimaryActionButtonMouseExited
        symmetricPrimaryActionButton.setBackground(defaultBackground);
        symmetricPrimaryActionButton.setForeground(defaultForeground);
    }//GEN-LAST:event_symmetricPrimaryActionButtonMouseExited

    private void symmetricPrimaryActionButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_symmetricPrimaryActionButtonMousePressed
        symmetricAES();
    }//GEN-LAST:event_symmetricPrimaryActionButtonMousePressed

    private void cryptoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cryptoMousePressed
        if (isEncode) {
            encodeActionPerformed(null);
        } else {
            decodeActionPerformed(null);
        }
        int index = crypto.getSelectedIndex();

        switch (index) {
            case 0 -> {
                ceasarPaddingInput.setText("3");
                encryptedFile = null;
                actionType = null;
            }
            case 1 -> {
                symmetricSecretKeyInput.setText("");
                symmetricSaltPhraseInput.setText("");
                encryptedFile = null;
                actionType = "SYMMETRIC";
            }
            case 2 -> {

                encryptedFile = null;
                actionType = "ASYMMETRIC";
            }
            case 3 -> {
                encryptedFile = null;
                actionType = "HYBRID";
            }
            default -> {
            }
        }
    }//GEN-LAST:event_cryptoMousePressed

    private void asymmetricInputTextArea1InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_asymmetricInputTextArea1InputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricInputTextArea1InputMethodTextChanged

    private void ceasarInputPaste3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ceasarInputPaste3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ceasarInputPaste3ActionPerformed

    private void asymmetricPaste1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricPaste1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricPaste1MouseEntered

    private void asymmetricPaste1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricPaste1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricPaste1MouseExited

    private void asymmetricPaste1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricPaste1MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricPaste1MousePressed

    private void asymmetricPaste1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asymmetricPaste1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricPaste1ActionPerformed

    private void asymmetricCopyCipher1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricCopyCipher1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricCopyCipher1MouseEntered

    private void asymmetricCopyCipher1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricCopyCipher1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricCopyCipher1MouseExited

    private void asymmetricCopyCipher1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricCopyCipher1MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricCopyCipher1MousePressed

    private void asymmetricCopyCipher1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asymmetricCopyCipher1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricCopyCipher1ActionPerformed

    private void asymmetricPrimaryActionButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricPrimaryActionButton1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricPrimaryActionButton1MouseEntered

    private void asymmetricPrimaryActionButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricPrimaryActionButton1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricPrimaryActionButton1MouseExited

    private void asymmetricPrimaryActionButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asymmetricPrimaryActionButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricPrimaryActionButton1ActionPerformed

    private void asymmetricCopyPrivateKey1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricCopyPrivateKey1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricCopyPrivateKey1MouseEntered

    private void asymmetricCopyPrivateKey1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricCopyPrivateKey1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricCopyPrivateKey1MouseExited

    private void asymmetricCopyPrivateKey1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricCopyPrivateKey1MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricCopyPrivateKey1MousePressed

    private void asymmetricCopyPrivateKey1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asymmetricCopyPrivateKey1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricCopyPrivateKey1ActionPerformed

    private void asymmetricCopyPublicKey1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricCopyPublicKey1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricCopyPublicKey1MouseEntered

    private void asymmetricCopyPublicKey1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricCopyPublicKey1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricCopyPublicKey1MouseExited

    private void asymmetricCopyPublicKey1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricCopyPublicKey1MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricCopyPublicKey1MousePressed

    private void asymmetricCopyPublicKey1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asymmetricCopyPublicKey1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricCopyPublicKey1ActionPerformed

    private void ceasar3MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ceasar3MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_ceasar3MouseMoved

    private void symmetricFileChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_symmetricFileChooserActionPerformed

    }//GEN-LAST:event_symmetricFileChooserActionPerformed

    private void symmetricFileChooserMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_symmetricFileChooserMousePressed
        JFileChooser jfileChooser = new JFileChooser();
        int result = jfileChooser.showOpenDialog(this);
        jfileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        encryptedFile = new EncryptedFile(jfileChooser.getSelectedFile(), null);
        if (actionType.equals("SYMMETRIC")) {
            symmetricFileChooser.setText(encryptedFile.getFile().getName());
        } else if (actionType.equals("ASYMMETRIC")) {
            hybridFileChooser1.setText(encryptedFile.getFile().getName());
        }
    }//GEN-LAST:event_symmetricFileChooserMousePressed

    private void symmetricMainActionFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_symmetricMainActionFileActionPerformed
        if (encryptedFile == null) {
            showErrorMessage("Please choose a file!");
            return;
        }

        try {
            StringBuilder sb = new StringBuilder();
            if (encryptedFile.getMimetype().equals("image")) {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(encryptedFile.getFile());
                } catch (IOException e) {
                }
                if (isEncode) {
                    sb.append(ImageUtil.imgToBase64String(img, encryptedFile.getFileFormat(true)));
                } else {
                    sb.append(ImageUtil.imgToBase64String(img, encryptedFile.getFileFormat(false)));
                }
            } else {
                FileReader fr = new FileReader(encryptedFile.getFile());
                BufferedReader br = new BufferedReader(fr);
                String ln;
                while ((ln = br.readLine()) != null) {
                    sb.append(ln);
                    if (isEncode) {
                        sb.append("\n");
                    }
                }
                fr.close();
                br.close();
            }
            String plain = sb.toString();

            if (isEncode) {
                if (actionType.equals("SYMMETRIC")) {
                    String secretKey = symmetricSecretKeyInput.getText();
                    String saltPhrase = symmetricSaltPhraseInput.getText();
                    if (secretKey.trim().isEmpty()) {
                        showErrorMessage("Invalid secret key value!");
                    } else if (saltPhrase.trim().isEmpty()) {
                        showErrorMessage("Invalid salt phrase value!");
                    } else {
                        String encode1 = AESEncode.get(plain, secretKey, saltPhrase);
                        //   String encode1 = plain;
                        if (encode1 == null) {
                            showErrorMessage("Unable to encode the provided input!");
                        } else {
                            symmetricSaveFileTextField.setText(encryptedFile.getFile().getName() + ".enc");
                            encryptedFile.setContent(encode1);
                        }
                    }
                } else if (actionType.equals("ASYMMETRIC")) {
                    try {
                        RSACipher encryptMode = RSAEncode.get(plain);

                        hybridPrivateKeyTextArea.setText(encryptMode.getPrivateKey());
                        hybridPublicKeyTextArea.setText(encryptMode.getPublickKey());

                        hybridSaveFileTextField1.setText(encryptedFile.getFile().getName() + ".enc");
                        encryptedFile.setContent(encryptMode.getContent());

                    } catch (NoSuchPaddingException ex) {
                        showErrorMessage("Unable to encrypt the input due to padding issues!");
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvalidKeyException ex) {
                        showErrorMessage("Unable to encrypt the input due to invalid key!");
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalBlockSizeException ex) {
                        showErrorMessage("Maximum block size of 256! Please reduce the input text or use Hybrid!");
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchAlgorithmException | BadPaddingException ex) {
                        showErrorMessage("Unable to encrypt the provided input!");
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                if (actionType.equals("SYMMETRIC")) {
                    String secretKey = symmetricSecretKeyInput.getText();
                    String saltPhrase = symmetricSaltPhraseInput.getText();
                    if (secretKey.trim().isEmpty()) {
                        showErrorMessage("Invalid secret key value!");
                    } else if (saltPhrase.trim().isEmpty()) {
                        showErrorMessage("Invalid salt phrase value!");
                    } else {
                        String decode1 = AESDecode.get(plain, secretKey, saltPhrase);
                        // String decode1 = plain;
                        if (decode1 == null) {
                            showErrorMessage("Unable to decode the provided input!");
                        } else {
                            symmetricSaveFileTextField.setText(encryptedFile.getFile().getName().replace(".enc", ""));
                            encryptedFile.setContent(decode1);
                        }
                    }
                } else if (actionType.equals("ASYMMETRIC")) {
                    try {
                        String decryptMode = RSADecode.get(hybridPublicKeyTextArea.getText(),
                                hybridPrivateKeyTextArea.getText(),
                                plain);
                        hybridSaveFileTextField1.setText(encryptedFile.getFile().getName().replace(".enc", ""));
                        encryptedFile.setContent(decryptMode);
                    } catch (NoSuchPaddingException ex) {
                        showErrorMessage("Unable to decrypt the input due to padding issues!");
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvalidKeySpecException | InvalidKeyException ex) {
                        showErrorMessage("Unable to decrypt the input due to invalid key!");
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalBlockSizeException ex) {
                        showErrorMessage("Maximum block size of 256! Please reduce the input text or use Hybrid!");
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoSuchAlgorithmException | BadPaddingException ex) {
                        showErrorMessage("Unable to decrypt the provided input!");
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_symmetricMainActionFileActionPerformed

    private void symmetricSaveFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_symmetricSaveFileActionPerformed
        JFileChooser chooser = new JFileChooser();
        File file1 = new File(encryptedFile.getFile().getAbsolutePath());
        chooser.setCurrentDirectory(file1);
        File file;
        if (!isEncode) {
            file = new File(encryptedFile.getFile().getName().replace(".enc", ""));
        } else {
            file = new File(encryptedFile.getFile().getName() + ".enc");
        }

        chooser.setSelectedFile(file);

        int retrival = chooser.showSaveDialog(this);
        if (retrival == JFileChooser.APPROVE_OPTION) {
            try {
                if (encryptedFile.getMimetype().equals("image")) {
                    if (!isEncode) {
                        ImageIO.write(ImageUtil.base64StringToImg(encryptedFile.getContent()), encryptedFile.getFileFormat(false), chooser.getSelectedFile());
                    } else {
                        ImageIO.write(ImageUtil.base64StringToImg(encryptedFile.getContent()), encryptedFile.getFileFormat(true), chooser.getSelectedFile());
                    }
                } else {
                    FileWriter fw = new FileWriter(chooser.getSelectedFile());
                    fw.write(encryptedFile.getContent());
                    fw.close();
                }
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_symmetricSaveFileActionPerformed

    private void ceasar2MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ceasar2MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_ceasar2MouseMoved

    private void hybridFileChooser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hybridFileChooser1ActionPerformed

    }//GEN-LAST:event_hybridFileChooser1ActionPerformed

    private void hybridFileChooser1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hybridFileChooser1MousePressed
        symmetricFileChooserMousePressed(evt);
    }//GEN-LAST:event_hybridFileChooser1MousePressed

    private void hybridSaveFile1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hybridSaveFile1ActionPerformed
        symmetricSaveFileActionPerformed(evt);
    }//GEN-LAST:event_hybridSaveFile1ActionPerformed

    private void hybridMainActionFile1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hybridMainActionFile1ActionPerformed
        symmetricMainActionFileActionPerformed(evt);
    }//GEN-LAST:event_hybridMainActionFile1ActionPerformed

    private void hybridPrimaryActionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hybridPrimaryActionButtonActionPerformed
        hybridRSA();
    }//GEN-LAST:event_hybridPrimaryActionButtonActionPerformed

    private void hybridPrimaryActionButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hybridPrimaryActionButtonMouseExited
        hybridPrimaryActionButton.setBackground(defaultBackground);
        hybridPrimaryActionButton.setForeground(defaultForeground);
    }//GEN-LAST:event_hybridPrimaryActionButtonMouseExited

    private void hybridPrimaryActionButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hybridPrimaryActionButtonMouseEntered
        hybridPrimaryActionButton.setBackground(blue);
        hybridPrimaryActionButton.setForeground(Color.WHITE);
    }//GEN-LAST:event_hybridPrimaryActionButtonMouseEntered

    private void hybridCopyCipherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hybridCopyCipherActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hybridCopyCipherActionPerformed

    private void hybridCopyCipherMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hybridCopyCipherMousePressed
        StringSelection stringSelection = new StringSelection(hybridOutputTextArea.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_hybridCopyCipherMousePressed

    private void hybridCopyCipherMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hybridCopyCipherMouseExited
        hybridCopyCipher.setBackground(defaultBackground);
        hybridCopyCipher.setForeground(defaultForeground);
    }//GEN-LAST:event_hybridCopyCipherMouseExited

    private void hybridCopyCipherMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hybridCopyCipherMouseEntered
        hybridCopyCipher.setBackground(blue);
        hybridCopyCipher.setForeground(Color.WHITE);
    }//GEN-LAST:event_hybridCopyCipherMouseEntered

    private void hybridPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hybridPasteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hybridPasteActionPerformed

    private void hybridPasteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hybridPasteMousePressed
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = c.getContents(this);
        if (t == null) {
            return;
        }
        try {
            hybridInputTextArea.setText((String) t.getTransferData(DataFlavor.stringFlavor));
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_hybridPasteMousePressed

    private void hybridPasteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hybridPasteMouseExited
        hybridPaste.setBackground(defaultBackground);
        hybridPaste.setForeground(defaultForeground);
    }//GEN-LAST:event_hybridPasteMouseExited

    private void hybridPasteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hybridPasteMouseEntered
        hybridPaste.setBackground(blue);
        hybridPaste.setForeground(Color.WHITE);
    }//GEN-LAST:event_hybridPasteMouseEntered

    private void ceasarInputPaste2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ceasarInputPaste2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ceasarInputPaste2ActionPerformed

    private void hybridInputTextAreaInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_hybridInputTextAreaInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_hybridInputTextAreaInputMethodTextChanged

    private void symmetricSaveFileTextFieldMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_symmetricSaveFileTextFieldMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_symmetricSaveFileTextFieldMousePressed

    private void hybridCopyPrivateKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hybridCopyPrivateKeyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hybridCopyPrivateKeyActionPerformed

    private void hybridCopyPrivateKeyMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hybridCopyPrivateKeyMousePressed
        StringSelection stringSelection = new StringSelection(hybridPrivateKeyTextArea.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_hybridCopyPrivateKeyMousePressed

    private void hybridCopyPrivateKeyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hybridCopyPrivateKeyMouseExited
        hybridCopyPrivateKey.setBackground(defaultBackground);
        hybridCopyPrivateKey.setForeground(defaultForeground);
    }//GEN-LAST:event_hybridCopyPrivateKeyMouseExited

    private void hybridCopyPrivateKeyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hybridCopyPrivateKeyMouseEntered
        hybridCopyPrivateKey.setBackground(blue);
        hybridCopyPrivateKey.setForeground(Color.WHITE);
    }//GEN-LAST:event_hybridCopyPrivateKeyMouseEntered

    private void hybridCopyPublicKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hybridCopyPublicKeyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hybridCopyPublicKeyActionPerformed

    private void hybridCopyPublicKeyMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hybridCopyPublicKeyMousePressed
        StringSelection stringSelection = new StringSelection(hybridPublicKeyTextArea.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_hybridCopyPublicKeyMousePressed

    private void hybridCopyPublicKeyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hybridCopyPublicKeyMouseExited
        hybridCopyPublicKey.setBackground(defaultBackground);
        hybridCopyPublicKey.setForeground(defaultForeground);
    }//GEN-LAST:event_hybridCopyPublicKeyMouseExited

    private void hybridCopyPublicKeyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hybridCopyPublicKeyMouseEntered
        hybridCopyPublicKey.setBackground(blue);
        hybridCopyPublicKey.setForeground(Color.WHITE);
    }//GEN-LAST:event_hybridCopyPublicKeyMouseEntered

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel asymmetric;
    private javax.swing.JButton asymmetricCopyCipher1;
    private javax.swing.JButton asymmetricCopyPrivateKey1;
    private javax.swing.JButton asymmetricCopyPublicKey1;
    private javax.swing.JLabel asymmetricInputLabel1;
    private javax.swing.JTextArea asymmetricInputTextArea1;
    private javax.swing.JLabel asymmetricOutputLabel1;
    private javax.swing.JTextArea asymmetricOutputTextArea1;
    private javax.swing.JButton asymmetricPaste1;
    private javax.swing.JButton asymmetricPrimaryActionButton1;
    private javax.swing.JLabel asymmetricPrivateKeyLabel1;
    private javax.swing.JTextArea asymmetricPrivateKeyTextArea1;
    private javax.swing.JLabel asymmetricPublicKeyLabel1;
    private javax.swing.JTextArea asymmetricPublicKeyTextArea1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel ceasar;
    private javax.swing.JPanel ceasar1;
    private javax.swing.JPanel ceasar2;
    private javax.swing.JPanel ceasar3;
    private javax.swing.JButton ceasarCopy;
    private javax.swing.JButton ceasarInputPaste;
    private javax.swing.JButton ceasarInputPaste1;
    private javax.swing.JButton ceasarInputPaste2;
    private javax.swing.JButton ceasarInputPaste3;
    private javax.swing.JLabel ceasarInputText;
    private javax.swing.JTextArea ceasarInputTextArea;
    private javax.swing.JLabel ceasarOutputLabel;
    private javax.swing.JTextArea ceasarOutputTextArea;
    private javax.swing.JTextField ceasarPaddingInput;
    private javax.swing.JLabel ceasarPaddingLabel;
    private javax.swing.JButton ceasarPaste;
    private javax.swing.JTabbedPane crypto;
    private javax.swing.JButton decode;
    private javax.swing.JButton encode;
    private javax.swing.JPanel hybrid;
    private javax.swing.JButton hybridCopyCipher;
    private javax.swing.JButton hybridCopyPrivateKey;
    private javax.swing.JButton hybridCopyPublicKey;
    private javax.swing.JTextField hybridFileChooser1;
    private javax.swing.JLabel hybridInputLabel;
    private javax.swing.JTextArea hybridInputTextArea;
    private javax.swing.JButton hybridMainActionFile1;
    private javax.swing.JLabel hybridOutputLabel;
    private javax.swing.JTextArea hybridOutputTextArea;
    private javax.swing.JButton hybridPaste;
    private javax.swing.JButton hybridPrimaryActionButton;
    private javax.swing.JLabel hybridPrivateKeyLabel;
    private javax.swing.JTextArea hybridPrivateKeyTextArea;
    private javax.swing.JLabel hybridPublicKeyLabel;
    private javax.swing.JTextArea hybridPublicKeyTextArea;
    private javax.swing.JButton hybridSaveFile1;
    private javax.swing.JTextField hybridSaveFileTextField1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JButton settings;
    private javax.swing.JPanel symmetric;
    private javax.swing.JButton symmetricCopy;
    private javax.swing.JTextField symmetricFileChooser;
    private javax.swing.JLabel symmetricInputLabel;
    private javax.swing.JTextArea symmetricInputTextArea;
    private javax.swing.JButton symmetricMainActionFile;
    private javax.swing.JLabel symmetricOutputLabel;
    private javax.swing.JTextArea symmetricOutputTextArea;
    private javax.swing.JButton symmetricPaste;
    private javax.swing.JButton symmetricPrimaryActionButton;
    private javax.swing.JTextField symmetricSaltPhraseInput;
    private javax.swing.JLabel symmetricSaltPhraseLabel;
    private javax.swing.JButton symmetricSaltPhraseRandom;
    private javax.swing.JButton symmetricSaveFile;
    private javax.swing.JTextField symmetricSaveFileTextField;
    private javax.swing.JTextField symmetricSecretKeyInput;
    private javax.swing.JLabel symmetricSecretKeyLabel;
    private javax.swing.JButton symmetricSecretKeyRandom;
    // End of variables declaration//GEN-END:variables
}
