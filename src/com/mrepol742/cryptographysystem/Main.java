package com.mrepol742.cryptographysystem;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
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
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

/**
 *
 * @author mrepol742
 */
public class Main extends javax.swing.JFrame {

    private boolean isEncode = true;
    private Color blue = new Color(66, 133, 244);
    private Color defaultBackground = new javax.swing.JButton().getBackground();
    private Color defaultForeground = new javax.swing.JButton().getForeground();
    private Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png"));
    private Icon icon = new ImageIcon(image);

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        setResizable(false);
        setIconImage(image);
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
        asymmetricPaste.setFocusPainted(false);
        asymmetricCopyCipher.setFocusPainted(false);
        symmetricPrimaryActionButton.setFocusPainted(false);
        ceasarPaddingInput.setText("3");
        /*
        * DISABLE OUTPUT TEXT AREA
         */
        ceasarOutputTextArea.setEditable(false);
        symmetricOutputTextArea.setEditable(false);
        asymmetricOutputTextArea.setEditable(false);
        asymmetricPrivateKeyTextArea.setEditable(false);
        asymmetricPublicKeyTextArea.setEditable(false);
        /*
        * ENABLE LINE WRAPPING
         */
        ceasarInputTextArea.setLineWrap(true);
        ceasarOutputTextArea.setLineWrap(true);
        symmetricInputTextArea.setLineWrap(true);
        symmetricOutputTextArea.setLineWrap(true);
        asymmetricInputTextArea.setLineWrap(true);
        asymmetricOutputTextArea.setLineWrap(true);
        asymmetricPrivateKeyTextArea.setLineWrap(true);
        asymmetricPublicKeyTextArea.setLineWrap(true);
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
                    } else if (input > 26) {
                        showErrorMessage("Please enter number not bigger than 26!");
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

    private void asymmetricRSA() {
        if (asymmetricInputTextArea.getText().trim().equals("")) {
            asymmetricOutputTextArea.setText("");
            asymmetricPrivateKeyTextArea.setText("");
            asymmetricPublicKeyTextArea.setText("");
            return;
        }

        if (isEncode) {
            try {
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
                generator.initialize(2048);
                KeyPair pair = generator.generateKeyPair();
                PrivateKey privateKey = pair.getPrivate();
                PublicKey publicKey = pair.getPublic();
                asymmetricPrivateKeyTextArea.setText(Base64.getEncoder().withoutPadding().encodeToString(privateKey.getEncoded()));
                asymmetricPublicKeyTextArea.setText(Base64.getEncoder().withoutPadding().encodeToString(publicKey.getEncoded()));

                Cipher encryptCipher = Cipher.getInstance("RSA");
                encryptCipher.init(Cipher.ENCRYPT_MODE, privateKey);
                byte[] secretMessageBytes = asymmetricInputTextArea.getText().getBytes(StandardCharsets.UTF_8);
                byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
                asymmetricOutputTextArea.setText(Base64.getEncoder().withoutPadding().encodeToString(encryptedMessageBytes));
            } catch (NoSuchPaddingException ex) {
                showErrorMessage("Unable to encrypt the input due to padding issues!");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeyException ex) {
                showErrorMessage("Unable to encrypt the input due to invalid private key!");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException ex) {
                showErrorMessage("Unable to decrypt the provided input!");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                byte[] publicKeyBytes = Base64.getDecoder().decode(asymmetricPublicKeyTextArea.getText().getBytes(StandardCharsets.UTF_8));
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
                Cipher decryptCipher = Cipher.getInstance("RSA");
                decryptCipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePublic(publicKeySpec));
                byte[] decryptedMessageBytes = decryptCipher.doFinal(Base64.getDecoder().decode(asymmetricInputTextArea.getText()));
                String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
                asymmetricOutputTextArea.setText(decryptedMessage);
            } catch (NoSuchPaddingException ex) {
                showErrorMessage("Unable to decrypt the input due to padding issues!");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeySpecException | InvalidKeyException ex) {
                showErrorMessage("Unable to decrypt the input due to invalid private key!");
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException ex) {
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
        } else if (saltPhrase.trim().isEmpty()) {
            showErrorMessage("Invalid salt phrase value!");
        } else {
            if (isEncode) {
                String encode = AESEncode.get(plain, secretKey, saltPhrase);
                if (encode == null) {
                    showErrorMessage("Unable to encode the provided input!");
                } else {
                    symmetricOutputTextArea.setText(encode);
                }
            } else {
                String decode = AESDecode.get(plain, secretKey, saltPhrase);
                if (decode == null) {
                    showErrorMessage("Unable to decode the provided input!");
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
        asymmetric = new javax.swing.JPanel();
        ceasar2 = new javax.swing.JPanel();
        asymmetricOutputLabel = new javax.swing.JLabel();
        asymmetricInputLabel = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        asymmetricInputTextArea = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        asymmetricOutputTextArea = new javax.swing.JTextArea();
        asymmetricPrivateKeyLabel = new javax.swing.JLabel();
        ceasarInputPaste2 = new javax.swing.JButton();
        asymmetricPaste = new javax.swing.JButton();
        asymmetricCopyCipher = new javax.swing.JButton();
        asymmetricPublicKeyLabel = new javax.swing.JLabel();
        asymmetricPrimaryActionButton = new javax.swing.JButton();
        asymmetricCopyPrivateKey = new javax.swing.JButton();
        asymmetricCopyPublicKey = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        asymmetricPrivateKeyTextArea = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        asymmetricPublicKeyTextArea = new javax.swing.JTextArea();
        hybrid = new javax.swing.JPanel();
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
                .addContainerGap(39, Short.MAX_VALUE))
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

        javax.swing.GroupLayout ceasar1Layout = new javax.swing.GroupLayout(ceasar1);
        ceasar1.setLayout(ceasar1Layout);
        ceasar1Layout.setHorizontalGroup(
            ceasar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ceasar1Layout.createSequentialGroup()
                .addGroup(ceasar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ceasar1Layout.createSequentialGroup()
                        .addGap(53, 53, 53)
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
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(symmetricSecretKeyRandom))
                                    .addGroup(ceasar1Layout.createSequentialGroup()
                                        .addGap(26, 26, 26)
                                        .addComponent(symmetricPaste)))))
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
                        .addGap(282, 282, 282)
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
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                    .addComponent(jScrollPane4))
                .addGap(18, 18, 18)
                .addComponent(symmetricPrimaryActionButton)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout symmetricLayout = new javax.swing.GroupLayout(symmetric);
        symmetric.setLayout(symmetricLayout);
        symmetricLayout.setHorizontalGroup(
            symmetricLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 778, Short.MAX_VALUE)
            .addGroup(symmetricLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(symmetricLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(ceasar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        symmetricLayout.setVerticalGroup(
            symmetricLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 379, Short.MAX_VALUE)
            .addGroup(symmetricLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(symmetricLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(ceasar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        crypto.addTab("Symmetric", symmetric);

        ceasar2.setInheritsPopupMenu(true);
        ceasar2.setPreferredSize(new java.awt.Dimension(778, 379));
        ceasar2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                ceasar2MouseMoved(evt);
            }
        });

        asymmetricOutputLabel.setText("Cipher Text:");

        asymmetricInputLabel.setText("Plain Text:");

        asymmetricInputTextArea.setColumns(20);
        asymmetricInputTextArea.setRows(5);
        asymmetricInputTextArea.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                asymmetricInputTextAreaInputMethodTextChanged(evt);
            }
        });
        jScrollPane5.setViewportView(asymmetricInputTextArea);

        asymmetricOutputTextArea.setColumns(20);
        asymmetricOutputTextArea.setRows(5);
        jScrollPane6.setViewportView(asymmetricOutputTextArea);

        asymmetricPrivateKeyLabel.setText("Private Key:");

        ceasarInputPaste2.setText("Paste");
        ceasarInputPaste2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ceasarInputPaste2ActionPerformed(evt);
            }
        });

        asymmetricPaste.setText("Paste");
        asymmetricPaste.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                asymmetricPasteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                asymmetricPasteMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                asymmetricPasteMousePressed(evt);
            }
        });
        asymmetricPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                asymmetricPasteActionPerformed(evt);
            }
        });

        asymmetricCopyCipher.setText("Copy");
        asymmetricCopyCipher.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                asymmetricCopyCipherMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                asymmetricCopyCipherMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                asymmetricCopyCipherMousePressed(evt);
            }
        });
        asymmetricCopyCipher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                asymmetricCopyCipherActionPerformed(evt);
            }
        });

        asymmetricPublicKeyLabel.setText("Public Key:");

        asymmetricPrimaryActionButton.setText("Encrypt");
        asymmetricPrimaryActionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                asymmetricPrimaryActionButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                asymmetricPrimaryActionButtonMouseExited(evt);
            }
        });
        asymmetricPrimaryActionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                asymmetricPrimaryActionButtonActionPerformed(evt);
            }
        });

        asymmetricCopyPrivateKey.setText("Copy");
        asymmetricCopyPrivateKey.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                asymmetricCopyPrivateKeyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                asymmetricCopyPrivateKeyMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                asymmetricCopyPrivateKeyMousePressed(evt);
            }
        });
        asymmetricCopyPrivateKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                asymmetricCopyPrivateKeyActionPerformed(evt);
            }
        });

        asymmetricCopyPublicKey.setText("Copy");
        asymmetricCopyPublicKey.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                asymmetricCopyPublicKeyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                asymmetricCopyPublicKeyMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                asymmetricCopyPublicKeyMousePressed(evt);
            }
        });
        asymmetricCopyPublicKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                asymmetricCopyPublicKeyActionPerformed(evt);
            }
        });

        asymmetricPrivateKeyTextArea.setColumns(20);
        asymmetricPrivateKeyTextArea.setRows(5);
        jScrollPane7.setViewportView(asymmetricPrivateKeyTextArea);

        asymmetricPublicKeyTextArea.setColumns(20);
        asymmetricPublicKeyTextArea.setRows(5);
        jScrollPane8.setViewportView(asymmetricPublicKeyTextArea);

        javax.swing.GroupLayout ceasar2Layout = new javax.swing.GroupLayout(ceasar2);
        ceasar2.setLayout(ceasar2Layout);
        ceasar2Layout.setHorizontalGroup(
            ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ceasar2Layout.createSequentialGroup()
                .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ceasar2Layout.createSequentialGroup()
                        .addGap(164, 164, 164)
                        .addComponent(ceasarInputPaste2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ceasar2Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(asymmetricPrivateKeyLabel)
                            .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(ceasar2Layout.createSequentialGroup()
                                    .addComponent(asymmetricInputLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(asymmetricPaste)))
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE))))
                .addGap(29, 29, 29)
                .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(ceasar2Layout.createSequentialGroup()
                        .addComponent(asymmetricOutputLabel)
                        .addGap(134, 134, 134)
                        .addComponent(asymmetricCopyCipher, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane6)
                    .addGroup(ceasar2Layout.createSequentialGroup()
                        .addComponent(asymmetricPublicKeyLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(asymmetricCopyPublicKey, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane8))
                .addGap(24, 24, 24))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ceasar2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(asymmetricCopyPrivateKey, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(asymmetricPrimaryActionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(226, 226, 226))
        );
        ceasar2Layout.setVerticalGroup(
            ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ceasar2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(asymmetricOutputLabel)
                        .addComponent(asymmetricCopyCipher))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(asymmetricInputLabel)
                        .addComponent(asymmetricPaste)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(ceasarInputPaste2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                    .addComponent(jScrollPane5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(asymmetricPrimaryActionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(asymmetricPublicKeyLabel)
                    .addComponent(asymmetricPrivateKeyLabel)
                    .addComponent(asymmetricCopyPrivateKey)
                    .addComponent(asymmetricCopyPublicKey))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ceasar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
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

        crypto.addTab("Asymmetric", asymmetric);

        javax.swing.GroupLayout hybridLayout = new javax.swing.GroupLayout(hybrid);
        hybrid.setLayout(hybridLayout);
        hybridLayout.setHorizontalGroup(
            hybridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 678, Short.MAX_VALUE)
        );
        hybridLayout.setVerticalGroup(
            hybridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 367, Short.MAX_VALUE)
        );

        crypto.addTab("Hybrid", hybrid);

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
                .addComponent(crypto, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        this.setLocationRelativeTo(null);
    }//GEN-LAST:event_formWindowOpened

    private void decodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decodeActionPerformed
        if (!isEncode) {
            return;
        }
        isEncode = false;

        decode.setBackground(blue);
        decode.setForeground(Color.WHITE);

        encode.setBackground(defaultBackground);
        encode.setForeground(defaultForeground);

        int index = crypto.getSelectedIndex();
        switch (index) {
            case 0 -> {
                ceasarInputText.setText("Cipher Text:");
                ceasarOutputLabel.setText("Plain Text:");
                ceasarInputTextArea.setText("");
                ceasarOutputTextArea.setText("");
            }
            case 1 -> {
                symmetricInputLabel.setText("Cipher Text:");
                symmetricOutputLabel.setText("Plain Text:");
                symmetricInputTextArea.setText("");
                symmetricOutputTextArea.setText("");
                symmetricSecretKeyRandom.setVisible(false);
                symmetricSaltPhraseRandom.setVisible(false);
                symmetricPrimaryActionButton.setText("Decrypt");
            }
            case 2 -> {
                asymmetricInputLabel.setText("Cipher Text:");
                asymmetricOutputLabel.setText("Plain Text:");
                asymmetricPrimaryActionButton.setText("Decrypt");
                asymmetricPublicKeyTextArea.setEditable(true);
                asymmetricPrivateKeyLabel.setVisible(false);
                asymmetricCopyPrivateKey.setVisible(false);
                asymmetricPrivateKeyTextArea.setVisible(false);
                asymmetricInputTextArea.setText("");
                asymmetricOutputTextArea.setText("");
                asymmetricPrivateKeyTextArea.setText("");
            }
            default -> {
            }
        }
    }//GEN-LAST:event_decodeActionPerformed

    private void encodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encodeActionPerformed
        if (isEncode) {
            return;
        }
        isEncode = true;

        encode.setBackground(blue);
        encode.setForeground(Color.WHITE);

        decode.setBackground(defaultBackground);
        decode.setForeground(defaultForeground);

        int index = crypto.getSelectedIndex();
        switch (index) {
            case 0 -> {
                ceasarInputText.setText("Plain Text:");
                ceasarOutputLabel.setText("Cipher Text:");
                ceasarInputTextArea.setText("");
                ceasarOutputTextArea.setText("");
            }
            case 1 -> {
                symmetricInputLabel.setText("Plain Text:");
                symmetricOutputLabel.setText("Cipher Text:");
                symmetricInputTextArea.setText("");
                symmetricOutputTextArea.setText("");
                symmetricSecretKeyRandom.setVisible(true);
                symmetricSaltPhraseRandom.setVisible(true);
                symmetricPrimaryActionButton.setText("Encrypt");
            }
            case 2 -> {
                asymmetricInputLabel.setText("Plain Text:");
                asymmetricOutputLabel.setText("Cipher Text:");
                asymmetricPrimaryActionButton.setText("Encrypt");
                asymmetricPublicKeyTextArea.setEditable(false);
                asymmetricPrivateKeyLabel.setVisible(true);
                asymmetricCopyPrivateKey.setVisible(true);
                asymmetricPrivateKeyTextArea.setVisible(true);
                asymmetricInputTextArea.setText("");
                asymmetricOutputTextArea.setText("");
                asymmetricPrivateKeyTextArea.setText("");
            }
            default -> {
            }
        }
    }//GEN-LAST:event_encodeActionPerformed

    private void settingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsActionPerformed
        // TODO add your handling code here:
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

    private void asymmetricInputTextAreaInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_asymmetricInputTextAreaInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricInputTextAreaInputMethodTextChanged

    private void ceasarInputPaste2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ceasarInputPaste2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ceasarInputPaste2ActionPerformed

    private void asymmetricPasteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricPasteMouseEntered
        asymmetricPaste.setBackground(blue);
        asymmetricPaste.setForeground(Color.WHITE);
    }//GEN-LAST:event_asymmetricPasteMouseEntered

    private void asymmetricPasteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricPasteMouseExited
        asymmetricPaste.setBackground(defaultBackground);
        asymmetricPaste.setForeground(defaultForeground);
    }//GEN-LAST:event_asymmetricPasteMouseExited

    private void asymmetricPasteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricPasteMousePressed
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = c.getContents(this);
        if (t == null) {
            return;
        }
        try {
            asymmetricInputTextArea.setText((String) t.getTransferData(DataFlavor.stringFlavor));
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_asymmetricPasteMousePressed

    private void asymmetricPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asymmetricPasteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricPasteActionPerformed

    private void asymmetricCopyCipherMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricCopyCipherMouseEntered
        asymmetricCopyCipher.setBackground(blue);
        asymmetricCopyCipher.setForeground(Color.WHITE);
    }//GEN-LAST:event_asymmetricCopyCipherMouseEntered

    private void asymmetricCopyCipherMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricCopyCipherMouseExited
        asymmetricCopyCipher.setBackground(defaultBackground);
        asymmetricCopyCipher.setForeground(defaultForeground);
    }//GEN-LAST:event_asymmetricCopyCipherMouseExited

    private void asymmetricCopyCipherMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricCopyCipherMousePressed
        StringSelection stringSelection = new StringSelection(asymmetricOutputTextArea.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_asymmetricCopyCipherMousePressed

    private void asymmetricCopyCipherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asymmetricCopyCipherActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricCopyCipherActionPerformed

    private void ceasar2MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ceasar2MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_ceasar2MouseMoved

    private void asymmetricCopyPrivateKeyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricCopyPrivateKeyMouseEntered
        asymmetricCopyPrivateKey.setBackground(blue);
        asymmetricCopyPrivateKey.setForeground(Color.WHITE);
    }//GEN-LAST:event_asymmetricCopyPrivateKeyMouseEntered

    private void asymmetricCopyPrivateKeyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricCopyPrivateKeyMouseExited
        asymmetricCopyPrivateKey.setBackground(defaultBackground);
        asymmetricCopyPrivateKey.setForeground(defaultForeground);
    }//GEN-LAST:event_asymmetricCopyPrivateKeyMouseExited

    private void asymmetricCopyPrivateKeyMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricCopyPrivateKeyMousePressed
        StringSelection stringSelection = new StringSelection(asymmetricPrivateKeyTextArea.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_asymmetricCopyPrivateKeyMousePressed

    private void asymmetricCopyPrivateKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asymmetricCopyPrivateKeyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricCopyPrivateKeyActionPerformed

    private void asymmetricCopyPublicKeyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricCopyPublicKeyMouseEntered
        asymmetricCopyPublicKey.setBackground(blue);
        asymmetricCopyPublicKey.setForeground(Color.WHITE);
    }//GEN-LAST:event_asymmetricCopyPublicKeyMouseEntered

    private void asymmetricCopyPublicKeyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricCopyPublicKeyMouseExited
        asymmetricCopyPublicKey.setBackground(defaultBackground);
        asymmetricCopyPublicKey.setForeground(defaultForeground);
    }//GEN-LAST:event_asymmetricCopyPublicKeyMouseExited

    private void asymmetricCopyPublicKeyMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricCopyPublicKeyMousePressed
        StringSelection stringSelection = new StringSelection(asymmetricPublicKeyTextArea.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_asymmetricCopyPublicKeyMousePressed

    private void asymmetricCopyPublicKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asymmetricCopyPublicKeyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_asymmetricCopyPublicKeyActionPerformed

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

    private void asymmetricPrimaryActionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asymmetricPrimaryActionButtonActionPerformed
        asymmetricRSA();
    }//GEN-LAST:event_asymmetricPrimaryActionButtonActionPerformed

    private void asymmetricPrimaryActionButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricPrimaryActionButtonMouseEntered
        asymmetricPrimaryActionButton.setBackground(blue);
        asymmetricPrimaryActionButton.setForeground(Color.WHITE);
    }//GEN-LAST:event_asymmetricPrimaryActionButtonMouseEntered

    private void asymmetricPrimaryActionButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_asymmetricPrimaryActionButtonMouseExited
        asymmetricPrimaryActionButton.setBackground(defaultBackground);
        asymmetricPrimaryActionButton.setForeground(defaultForeground);
    }//GEN-LAST:event_asymmetricPrimaryActionButtonMouseExited

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
    private javax.swing.JButton asymmetricCopyCipher;
    private javax.swing.JButton asymmetricCopyPrivateKey;
    private javax.swing.JButton asymmetricCopyPublicKey;
    private javax.swing.JLabel asymmetricInputLabel;
    private javax.swing.JTextArea asymmetricInputTextArea;
    private javax.swing.JLabel asymmetricOutputLabel;
    private javax.swing.JTextArea asymmetricOutputTextArea;
    private javax.swing.JButton asymmetricPaste;
    private javax.swing.JButton asymmetricPrimaryActionButton;
    private javax.swing.JLabel asymmetricPrivateKeyLabel;
    private javax.swing.JTextArea asymmetricPrivateKeyTextArea;
    private javax.swing.JLabel asymmetricPublicKeyLabel;
    private javax.swing.JTextArea asymmetricPublicKeyTextArea;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel ceasar;
    private javax.swing.JPanel ceasar1;
    private javax.swing.JPanel ceasar2;
    private javax.swing.JButton ceasarCopy;
    private javax.swing.JButton ceasarInputPaste;
    private javax.swing.JButton ceasarInputPaste1;
    private javax.swing.JButton ceasarInputPaste2;
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JButton settings;
    private javax.swing.JPanel symmetric;
    private javax.swing.JButton symmetricCopy;
    private javax.swing.JLabel symmetricInputLabel;
    private javax.swing.JTextArea symmetricInputTextArea;
    private javax.swing.JLabel symmetricOutputLabel;
    private javax.swing.JTextArea symmetricOutputTextArea;
    private javax.swing.JButton symmetricPaste;
    private javax.swing.JButton symmetricPrimaryActionButton;
    private javax.swing.JTextField symmetricSaltPhraseInput;
    private javax.swing.JLabel symmetricSaltPhraseLabel;
    private javax.swing.JButton symmetricSaltPhraseRandom;
    private javax.swing.JTextField symmetricSecretKeyInput;
    private javax.swing.JLabel symmetricSecretKeyLabel;
    private javax.swing.JButton symmetricSecretKeyRandom;
    // End of variables declaration//GEN-END:variables
}
