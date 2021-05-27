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
                drawDownloadScreen();
            }
        });
    }

    void drawDownloadScreen() {
        f.getContentPane().removeAll();
        f.revalidate();
        f.repaint();
        JButton b = new JButton("choose file");
        b.setBounds(200, 100, 200, 50);
        f.add(b);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawChooseFileScreen();
            }
        });
    }

    void drawChooseFileScreen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(f);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            input = selectedFile.getPath();
            f.getContentPane().removeAll();
            f.revalidate();
            f.repaint();

            JTextArea textArea = new JTextArea("INPUT PATH: " + input);
            textArea.setBounds(100, 100, 400, 30);
            textArea.setBackground(null);
            f.add(textArea);

            JButton bBack = new JButton("choose another file");
            bBack.setBounds(100, 150, 150, 30);
            f.add(bBack);
            bBack.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    drawChooseFileScreen();
                }
            });

            JButton bContinue = new JButton("continue");
            bContinue.setBounds(300, 150, 100, 30);
            f.add(bContinue);
            bContinue.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    drawSetKeyScreen();
                }
            });
            f.revalidate();
            f.repaint();
        }
    }

    void drawChooseDestinationLocationScreen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(input + ".enc"));
        int result = fileChooser.showSaveDialog(f);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            output = selectedFile.getPath();
            f.getContentPane().removeAll();
            f.revalidate();
            f.repaint();

            JTextArea textArea = new JTextArea("OUTPUT PATH: " + output);
            textArea.setBounds(100, 100, 400, 30);
            textArea.setBackground(null);
            f.add(textArea);

            JButton bBack = new JButton("choose another location");
            bBack.setBounds(100, 150, 200, 30);
            f.add(bBack);
            bBack.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    drawChooseDestinationLocationScreen();
                }
            });

            JButton bContinue = new JButton("continue");
            bContinue.setBounds(350, 150, 100, 30);
            f.add(bContinue);
            bContinue.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    drawEncryptingScreen();
                }
            });
            f.revalidate();
            f.repaint();
        }
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

                    drawChooseDestinationScreen();
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

    void drawChooseDestinationScreen() {
        f.getContentPane().removeAll();
        f.revalidate();
        f.repaint();

        JButton d = new JButton("choose location to save encrypted file");

        d.setBounds(200, 100, 290, 50);

        f.add(d);

        d.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawChooseDestinationLocationScreen();
            }
        });
    }

    void drawEncryptingScreen() {
        f.getContentPane().removeAll();
        f.revalidate();
        f.repaint();
        JTextArea textArea = new JTextArea("encryption in process");
        textArea.setBounds(250, 150, 150, 30);
        textArea.setBackground(null);
        f.add(textArea);
    }
}

