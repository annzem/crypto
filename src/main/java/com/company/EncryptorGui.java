package com.company;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class EncryptorGui {

    public static void main(String[] args) {
        EncryptorGui encryptorGui = new EncryptorGui();
        encryptorGui.makeFrame();
    }

    private JFrame f;

    private String input;
    private String key;
    private String output;
    private boolean encrypt;

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
        JButton e = new JButton("encrypt file");
        JButton d = new JButton("decrypt file");

        e.setBounds(100, 100, 200, 50);
        d.setBounds(400, 100, 200, 50);
        f.add(e);
        f.add(d);

        e.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encrypt = true;
                drawButtonChoose(FileType.INPUT, "choose file");
            }
        });
    }

    void drawButtonChoose(FileType fileType, String button) {
        if (fileType == null) {
            throw new RuntimeException("fileType is null");
        }
        f.getContentPane().removeAll();
        f.revalidate();
        f.repaint();
        JButton b = new JButton(button);
        b.setBounds(150, 100, 300, 50);
        f.add(b);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileType == FileType.INPUT) {
                    drawChooseFileScreen(FileType.INPUT, input, "INPUT PATH: ", "choose another file");
                } else {
                    drawChooseFileScreen(FileType.OUTPUT, output, "OUTPUT PATH: ", "choose another location");
                }
            }
        });
    }

    void drawChooseFileScreen(FileType fileType, String variable, String pathText, String button) {
        if (fileType == null) {
            throw new RuntimeException("fileType is null");
        }
        JFileChooser fileChooser = new JFileChooser();
        int result;

        if (fileType == FileType.INPUT) {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            result = fileChooser.showOpenDialog(f);
            if (result == JFileChooser.APPROVE_OPTION) {
                input = fileChooser.getSelectedFile().getPath();
            }
        }

        if (fileType == FileType.OUTPUT) {
            fileChooser.setSelectedFile(new File(input + ".enc"));
            result = fileChooser.showSaveDialog(f);
            if (result == JFileChooser.APPROVE_OPTION) {
                output = fileChooser.getSelectedFile().getPath();
            }
        }

            f.getContentPane().removeAll();
            f.revalidate();
            f.repaint();

            JTextArea textArea = new JTextArea(pathText + fileChooser.getSelectedFile().getPath());
            textArea.setBounds(100, 100, 400, 30);
            textArea.setBackground(null);
            f.add(textArea);

            JButton bBack = new JButton(button);
            bBack.setBounds(100, 150, 200, 30);
            f.add(bBack);
            bBack.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (fileType == FileType.INPUT) {
                        drawChooseFileScreen(FileType.INPUT, input, "INPUT PATH: ", "choose another file");
                    } else {
                        drawChooseFileScreen(FileType.OUTPUT, output, "OUTPUT PATH: ", "choose another location");
                    }
                }
            });

            JButton bContinue = new JButton("continue");
            bContinue.setBounds(350, 150, 100, 30);
            f.add(bContinue);
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
            f.revalidate();
            f.repaint();

    }

    void drawSetKeyScreen() {
        f.getContentPane().removeAll();
        f.revalidate();
        f.repaint();

        JLabel l = new JLabel("key:");
        l.setBounds(50, 100, 50, 30);

        JButton b = new JButton("submit");
        b.setBounds(350, 100, 80, 30);

        JTextField t = new JTextField(20);
        t.setBounds(100, 100, 200, 30);
        Border border = BorderFactory.createLineBorder(Color.black, 1);
        t.setBorder(border);

        f.add(l);
        f.add(b);
        f.add(t);

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!t.getText().isEmpty()) {
                    key = t.getText();

                    drawButtonChoose(FileType.OUTPUT, "choose where to save encrypted file");
                } else {
                    JTextArea textArea = new JTextArea("nothing entered!");
                    textArea.setBounds(100, 130, 100, 30);
                    textArea.setBackground(null);
                    f.add(textArea);
                    f.revalidate();
                    f.repaint();
                }
            }
        });
    }

    void drawEncryptingScreen() {
        f.getContentPane().removeAll();
        f.revalidate();
        f.repaint();
        JTextArea textArea = new JTextArea("encryption in process");
        textArea.setBounds(250, 150, 300, 30);
        textArea.setBackground(null);
        f.add(textArea);
    }
}
