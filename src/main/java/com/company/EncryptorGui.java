package com.company;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Map;


public class EncryptorGui {

    public static void main(String[] args) {
        EncryptorGui encryptorGui = new EncryptorGui();
        encryptorGui.makeFrame();
    }

    private EncryptorLogic simpleEncryptorLogic = new SimpleEncryptorLogic();
    private EncryptorLogic aesEncryptorLogic = new AESEncryptorLogic();

    private JFrame f;
    private String input;
    private char[] key;
    private String output;
    private boolean encrypt;
    static JLabel percentsLabel = new JLabel("0 % finished");

    enum FileType {
        INPUT,
        OUTPUT
    }

    void makeFrame() {
        f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(700, 500);
        f.setLayout(null);
        f.setVisible(true);
        drawStartScreen();
    }

    void drawStartScreen() {
        f.getContentPane().removeAll();
        JButton encryptButton = new JButton("encrypt file");
        JButton decryptButton = new JButton("decrypt file");

        encryptButton.setBounds(100, 100, 200, 50);
        decryptButton.setBounds(400, 100, 200, 50);
        f.add(encryptButton);
        f.add(decryptButton);

        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encrypt = true;
                drawButtonChoose(FileType.INPUT, "choose file");
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encrypt = false;
                drawButtonChoose(FileType.INPUT, "choose file");
            }
        });
        f.revalidate();
        f.repaint();
    }

    void drawButtonChoose(FileType fileType, String button) {
        if (fileType == null) {
            throw new RuntimeException("fileType is null");
        }
        f.getContentPane().removeAll();
        JButton b = new JButton(button);
        b.setBounds(150, 100, 300, 50);
        f.add(b);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileType == FileType.INPUT) {
                    drawChooseFileScreen(FileType.INPUT, input, "INPUT PATH: ", "choose another input file");
                } else {
                    drawChooseFileScreen(FileType.OUTPUT, output, "OUTPUT PATH: ", "choose another location");
                }
            }
        });
        f.revalidate();
        f.repaint();
    }

    void drawChooseFileScreen(FileType fileType, String variable, String pathText, String button) {
        if (fileType == null) {
            throw new RuntimeException("fileType is null");
        }
        f.getContentPane().removeAll();
        JFileChooser fileChooser = new JFileChooser();
        int result;
        JLabel label = new JLabel();

        if (fileType == FileType.INPUT) {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            result = fileChooser.showOpenDialog(f);
            if (result == JFileChooser.APPROVE_OPTION) {
                input = fileChooser.getSelectedFile().getPath();
                label.setText(pathText + fileChooser.getSelectedFile().getPath());
            }
        }

        if (fileType == FileType.OUTPUT) {
            if (encrypt == true) {
                fileChooser.setSelectedFile(new File(input + ".enc"));
            } else {
                fileChooser.setSelectedFile(new File(input + ".dec"));
            }
            result = fileChooser.showSaveDialog(f);
            if (result == JFileChooser.APPROVE_OPTION) {
                output = fileChooser.getSelectedFile().getPath();
                label.setText(pathText + fileChooser.getSelectedFile().getPath());
            }
        }

        label.setBounds(100, 100, 400, 30);
        label.setBackground(null);
        f.add(label);

        JButton bBack = new JButton(button);
        bBack.setBounds(100, 150, 200, 30);
        f.add(bBack);
        bBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileType == FileType.INPUT) {
                    drawChooseFileScreen(FileType.INPUT, input, "INPUT PATH: ", "choose another input file");
                } else {
                    drawChooseFileScreen(FileType.OUTPUT, output, "OUTPUT PATH: ", "choose another location");
                }
            }
        });

        JButton bContinue = new JButton("continue");
        bContinue.setBounds(350, 150, 100, 30);
        f.add(bContinue);
        f.revalidate();
        f.repaint();
        bContinue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileType == FileType.INPUT) {
                    drawSetKeyScreen();
                } else {
                    drawEncryptingScreen();
                }
            }
        });
    }

    void drawSetKeyScreen() {
        f.getContentPane().removeAll();

        JLabel l = new JLabel("key:");
        l.setBounds(50, 100, 50, 30);

        JButton b = new JButton("submit");
        b.setBounds(350, 100, 80, 30);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setEchoChar('*');
        passwordField.setBounds(100, 100, 200, 30);
        Border border = BorderFactory.createLineBorder(Color.black, 1);
        passwordField.setBorder(border);

        JCheckBox checkBox = new JCheckBox();
        JLabel labelKey = new JLabel("make visible/invisible");
        labelKey.setBackground(null);
        checkBox.setBounds(100, 150, 20, 20);
        labelKey.setBounds(130, 150, 240, 20);
        checkBox.setBorderPaintedFlat(true);

        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox d = (JCheckBox) e.getSource();
                if (d.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('*');
                }
            }
        });

        f.add(l);
        f.add(b);
        f.add(passwordField);
        f.add(checkBox);
        f.add(labelKey);

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (passwordField.getPassword().length != 0) {
                    key = passwordField.getPassword();

                    drawButtonChoose(FileType.OUTPUT, "choose where to save encrypted file");
                } else {
                    JLabel label = new JLabel("nothing entered!");
                    label.setBounds(100, 130, 100, 30);
                    label.setBackground(null);
                    f.add(label);
                    f.revalidate();
                    f.repaint();
                }
            }
        });
        f.revalidate();
        f.repaint();
    }

    void drawEncryptingScreen() {
        f.getContentPane().removeAll();
        JLabel labelInProcess = new JLabel("encryption in process");
        labelInProcess.setBounds(250, 150, 300, 30);
        labelInProcess.setBackground(null);
        f.add(labelInProcess);

        percentsLabel.setBounds(250, 200, 500, 30);
        percentsLabel.setBackground(null);
//        f.add(percentsLabel);
        f.revalidate();
        f.repaint();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Map.Entry<FileInputStream, Long> inputPair;
                OutputStream outputStream;
                try {
                    inputPair = IOUtils.getFileInputStream(input);
                    outputStream = IOUtils.getFileOutputStream(output);

//                    simpleEncryptorLogic.encrypt(encrypt, inputPair.getKey(), outputStream, key);
                    aesEncryptorLogic.encrypt(encrypt, inputPair.getKey(), outputStream, key);

//                    AESEncryptorLogic.aESEncrypt(encrypt, 128, inputPair.getKey(), outputStream, key);
//                    simpleEncryptorLogic.encrypt(encrypt, inputPair.getKey(), outputStream, inputPair.getValue(), key, 200 * 1024 * 1024, new SimpleEncryptorLogic.ProgressUpdateListener() {
//                        @Override
//                        public void progressUpdated(int percents) {
//                            percentsLabel.setText(SimpleEncryptorLogic.percentsFinished + "% finished");
//                        }
//                    });
                    f.getContentPane().removeAll();

                    JLabel labelFinish = new JLabel("Process finished successfully!");
                    labelFinish.setBounds(150, 150, 300, 30);
                    labelFinish.setBackground(null);
                    f.add(labelFinish);

                    JButton toStartScreen = new JButton("encrypt something else");
                    toStartScreen.setBounds(170, 250, 250, 30);
                    f.add(toStartScreen);
                    toStartScreen.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            drawStartScreen();
                        }
                    });
                    f.revalidate();
                    f.repaint();
                } catch (FileNotFoundException | ReadException e) {
                    e.printStackTrace();
                    f.getContentPane().removeAll();
//                    f.add(percentsLabel);
//                    percentsLabel.setText(e.getMessage());
                    JButton b = new JButton("choose another input file");
                    b.setBounds(150, 300, 300, 50);
                    f.add(b);
                    b.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            drawChooseFileScreen(FileType.INPUT, input, "INPUT PATH: ", "choose another file");
                        }
                    });
                    f.revalidate();
                    f.repaint();
                } catch (WriteException e) {
                    e.printStackTrace();
                    f.getContentPane().removeAll();
//                    f.add(percentsLabel);
//                    percentsLabel.setText(e.getMessage());
                    JButton b = new JButton("choose another directory to save file");
                    b.setBounds(150, 300, 400, 50);
                    f.add(b);
                    b.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            drawChooseFileScreen(FileType.OUTPUT, output, "OUTPUT PATH: ", "choose another directory");
                        }
                    });
                    f.revalidate();
                    f.repaint();

                } catch (Exception e) {
                    e.printStackTrace();
                    f.getContentPane().removeAll();
                    JButton b = new JButton("Smth gone wrong. Try again");
                    JLabel l = new JLabel(e.getMessage());
                    b.setBounds(150, 150, 300, 50);
                    l.setBounds(50, 200, 600, 100);
                    f.add(b);
                    f.add(l);
                    b.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            drawStartScreen();
                        }
                    });
                    f.revalidate();
                    f.repaint();
                }
            }
        });
        thread.start();
    }
}
