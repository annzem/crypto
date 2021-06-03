package com.company;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.Map;
import java.util.function.Consumer;

public class EncryptorGui {

    public static void main(String[] args) {
        EncryptorGui encryptorGui = new EncryptorGui();
        encryptorGui.makeFrame();
    }

    private JFrame f;
    private String input;
    private char[] key;
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
        JButton encryptButton = new JButton("encrypt file");
        JButton decryptFile = new JButton("decrypt file");

        encryptButton.setBounds(100, 100, 200, 50);
        decryptFile.setBounds(400, 100, 200, 50);
        f.add(encryptButton);
        f.add(decryptFile);

        encryptButton.addActionListener(new ActionListener() {
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
        JFileChooser fileChooser = new JFileChooser();
        int result;
        JTextArea textArea = new JTextArea();
        f.getContentPane().removeAll();
        f.revalidate();
        f.repaint();


        if (fileType == FileType.INPUT) {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            result = fileChooser.showOpenDialog(f);
            if (result == JFileChooser.APPROVE_OPTION) {
                input = fileChooser.getSelectedFile().getPath();
                textArea.setText(pathText + fileChooser.getSelectedFile().getPath());
            }
        }

        if (fileType == FileType.OUTPUT) {
            fileChooser.setSelectedFile(new File(input + ".enc"));
            result = fileChooser.showSaveDialog(f);
            if (result == JFileChooser.APPROVE_OPTION) {
                output = fileChooser.getSelectedFile().getPath();
                textArea.setText(pathText + fileChooser.getSelectedFile().getPath());
            }
        }


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
                    drawChooseFileScreen(FileType.INPUT, input, "INPUT PATH: ", "choose another input file");
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

        JPasswordField t = new JPasswordField(20);
        t.setEchoChar('*');
        t.setBounds(100, 100, 200, 30);
        Border border = BorderFactory.createLineBorder(Color.black, 1);
        t.setBorder(border);

        JCheckBox d = new JCheckBox();
        JTextArea d1 = new JTextArea("see key");
        d1.setBackground(null);
        d.setBounds(100, 150, 20, 20);
        d1.setBounds(140, 150, 100, 30);
        d.setBorderPaintedFlat(true);

        d.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox d = (JCheckBox) e.getSource();
                if (d.isSelected()) {
                    t.setEchoChar((char) 0);
                } else {
                    t.setEchoChar('*');
                }
            }
        });

        f.add(l);
        f.add(b);
        f.add(t);
        f.add(d);
        f.add(d1);
        f.revalidate();
        f.repaint();

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (t.getPassword().length != 0) {
                    key = t.getPassword();

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

        JTextArea textAreaPercents = new JTextArea("0 % finished");
        textAreaPercents.setBounds(250, 200, 500, 30);
        textAreaPercents.setBackground(null);
        f.add(textAreaPercents);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Map.Entry<FileInputStream, Long> inputPair;
                OutputStream outputStream;
                try {
                    inputPair = IOUtils.getFileInputStream(input);
                    outputStream = IOUtils.getFileOutputStream(output);
                    EncryptorLogic.encrypt(true, inputPair.getKey(), outputStream, inputPair.getValue(), key, 200 * 1024 * 1024, new EncryptorLogic.ProgressUpdateListener() {
                        @Override
                        public void progressUpdated(int percents) {
                            textAreaPercents.setText(EncryptorLogic.percentsFinished + "% finished");
                        }
                    });
                    f.getContentPane().removeAll();
                    f.revalidate();
                    f.repaint();
                    JTextArea textAreaFinish = new JTextArea("File was encrypted successfully!");
                    textAreaFinish.setBounds(150, 150, 300, 30);
                    textAreaFinish.setBackground(null);
                    f.add(textAreaFinish);

                    JButton toStartScreen = new JButton("encrypt something else");
                    toStartScreen.setBounds(170, 250, 250, 30);
                    f.add(toStartScreen);
                    toStartScreen.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            f.getContentPane().removeAll();
                            f.revalidate();
                            f.repaint();
                            drawStartScreen();
                        }
                    });
                    f.revalidate();
                    f.repaint();
                } catch (FileNotFoundException | ReadException e) {
                    textAreaPercents.setText(e.getMessage());
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
                    textAreaPercents.setText(e.getMessage());
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
                }
            }
        });

        thread.start();
    }
}
