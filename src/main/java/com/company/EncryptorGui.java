package com.company;

import com.encryptor.EncryptorLogic;
import com.encryptor.ReadException;
import com.encryptor.WriteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Component
public class EncryptorGui extends JFrame {

    public EncryptorGui() {
    }

    public static void main(String[] args) {
        try {
            Class<?> aClass = Class.forName("com.company.SimpleEncBase64");
            System.out.println("class found");
        } catch (ClassNotFoundException e) {
            System.out.println("class not found");
        }
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext("com.company");//new SpringApplicationBuilder(EncryptorGui.class).headless(false).run(args);
    }

    private String input;
    private char[] key;
    private String output;
    private boolean encrypt;
    private EncryptorLogic beanAlgorithm;

    enum FileType {
        INPUT,
        OUTPUT
    }

    private String algorithmName;

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    private void postConstruct() {
        EventQueue.invokeLater(() -> {
            makeFrame();
        });
    }

    private void makeFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLayout(null);
        setVisible(true);
        drawStartScreen();
    }

    private void drawStartScreen() {
        getContentPane().removeAll();
        JButton encryptButton = new JButton("encrypt file");
        JButton decryptButton = new JButton("decrypt file");

        encryptButton.setBounds(100, 100, 200, 50);
        decryptButton.setBounds(400, 100, 200, 50);
        add(encryptButton);
        add(decryptButton);

        Map<String, com.encryptor.EncryptorLogic> algorithms = context.getBeansOfType(com.encryptor.EncryptorLogic.class);

        JComboBox selector = new JComboBox(algorithms.keySet().toArray());

        selector.setBounds(250, 200, 200, 50);
        add(selector);

        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encrypt = true;
                algorithmName = (String) selector.getSelectedItem();
                beanAlgorithm = (EncryptorLogic) context.getBean(algorithmName);
                drawButtonChoose(FileType.INPUT, "choose file");
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encrypt = false;
                algorithmName = (String) selector.getSelectedItem();
                beanAlgorithm = (EncryptorLogic) context.getBean(algorithmName);
                drawButtonChoose(FileType.INPUT, "choose file");
            }
        });
        revalidate();
        repaint();
    }

    private void drawButtonChoose(FileType fileType, String button) {
        if (fileType == null) {
            throw new RuntimeException("fileType is null");
        }
        getContentPane().removeAll();
        JButton b = new JButton(button);
        b.setBounds(150, 100, 300, 50);
        add(b);
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
        revalidate();
        repaint();
    }

    private void drawChooseFileScreen(FileType fileType, String variable, String pathText, String button) {
        if (fileType == null) {
            throw new RuntimeException("fileType is null");
        }
        getContentPane().removeAll();
        JFileChooser fileChooser = new JFileChooser();
        int result;
        JLabel label = new JLabel();

        if (fileType == FileType.INPUT) {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            result = fileChooser.showOpenDialog(this);
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
            result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                output = fileChooser.getSelectedFile().getPath();
                label.setText(pathText + fileChooser.getSelectedFile().getPath());
            }
        }

        label.setBounds(100, 100, 400, 30);
        label.setBackground(null);
        add(label);

        JButton bBack = new JButton(button);
        bBack.setBounds(100, 150, 200, 30);
        add(bBack);
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
        add(bContinue);
        revalidate();
        repaint();
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

    private void drawSetKeyScreen() {
        getContentPane().removeAll();

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

        add(l);
        add(b);
        add(passwordField);
        add(checkBox);
        add(labelKey);

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
                    add(label);
                    revalidate();
                    repaint();
                }
            }
        });
        revalidate();
        repaint();
    }

    private void drawEncryptingScreen() {
        getContentPane().removeAll();
        JLabel labelInProcess = new JLabel("encryption in process");
        labelInProcess.setBounds(250, 150, 300, 30);
        labelInProcess.setBackground(null);
        add(labelInProcess);

        revalidate();
        repaint();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Map.Entry<FileInputStream, Long> inputPair;
                OutputStream outputStream;
                try {
                    inputPair = IOUtils.getFileInputStream(input);
                    outputStream = IOUtils.getFileOutputStream(output);

                    beanAlgorithm.encrypt(encrypt, inputPair.getKey(), outputStream, key);

                    getContentPane().removeAll();

                    JLabel labelFinish = new JLabel("Process finished successfully!");
                    labelFinish.setBounds(150, 150, 300, 30);
                    labelFinish.setBackground(null);
                    add(labelFinish);

                    JButton toStartScreen = new JButton("encrypt something else");
                    toStartScreen.setBounds(170, 250, 250, 30);
                    add(toStartScreen);
                    toStartScreen.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            drawStartScreen();
                        }
                    });
                    revalidate();
                    repaint();
                } catch (FileNotFoundException | ReadException e) {
                    e.printStackTrace();
                    getContentPane().removeAll();
                    JButton b = new JButton("choose another input file");
                    b.setBounds(150, 300, 300, 50);
                    add(b);
                    b.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            drawChooseFileScreen(FileType.INPUT, input, "INPUT PATH: ", "choose another file");
                        }
                    });
                    revalidate();
                    repaint();
                } catch (WriteException e) {
                    e.printStackTrace();
                    getContentPane().removeAll();
                    JButton b = new JButton("choose another directory to save file");
                    b.setBounds(150, 300, 400, 50);
                    add(b);
                    b.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            drawChooseFileScreen(FileType.OUTPUT, output, "OUTPUT PATH: ", "choose another directory");
                        }
                    });
                    revalidate();
                    repaint();

                } catch (Exception e) {
                    e.printStackTrace();
                    getContentPane().removeAll();
                    JButton b = new JButton("Smth gone wrong. Try again");
                    JLabel l = new JLabel(e.getMessage());
                    b.setBounds(150, 150, 300, 50);
                    l.setBounds(50, 200, 600, 100);
                    add(b);
                    add(l);
                    b.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            drawStartScreen();
                        }
                    });
                    revalidate();
                    repaint();
                }
            }
        });
        thread.start();
    }
}
