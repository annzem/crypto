package com.company;

import java.io.*;

public class Encryptor {
    public static void main(String[] args) {

        String input = "/home/anna/IdeaProjects/crypto/src/main/resources/1.txt";
        String encrypted = "/home/anna/IdeaProjects/crypto/src/main/resources/1.encrypted.txt";
        String decrypted = "/home/anna/IdeaProjects/crypto/src/main/resources/1.decrypted.txt";

        String key = "abcdef";

//        (true, encrypted, key, 5);
//        (false, decrypted, key, 5);

        encrypt1_decrypt0(false, encrypted, decrypted, key, 1);
    }

    static void encrypt1_decrypt0(boolean encrypt, String inputPathname, String outputPathname, String key, int bufSize) {
        try {
            File sourceFile = new File(inputPathname);
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            File resultFile = new File(outputPathname);
            FileOutputStream fileOutputStream = new FileOutputStream(resultFile);

            byte[] buf;
            if (sourceFile.length() > bufSize) {
                buf = new byte[bufSize];
            } else {
                buf = new byte[(int) sourceFile.length()];
            }

            int percentsFinished = -1;
            int step = 0;
            int keyPos = 0;
            long inputFileLength = sourceFile.length();

            while (fileInputStream.available() > 0) {
                int read = fileInputStream.read(buf, 0, buf.length);
                for (int i = 0; i < read; i++) {
                    if (encrypt) {
                        buf[i] = ((byte) (buf[i] + key.charAt(keyPos)));
                    } else {
                        buf[i] = (byte) (buf[i] - key.charAt(keyPos));
                    }
                    keyPos = (keyPos + 1) % key.length();
                }
                step++;
                fileOutputStream.write(buf);
                int currentPercents = (int) ((long) buf.length * (long) step * 100 / inputFileLength);
                if (currentPercents != percentsFinished) {
                    percentsFinished = currentPercents;
                    System.out.println(percentsFinished + "%");
                }
            }
            fileOutputStream.close();

        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        System.out.println("finish");
    }
}
