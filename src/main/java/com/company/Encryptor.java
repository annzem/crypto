package com.company;

import java.io.*;

public class Encryptor {
    public static void main(String[] args) {

//        String input = "/home/anna/IdeaProjects/crypto/src/main/resources/007.png";
//        String encrypted = "/home/anna/IdeaProjects/crypto/src/main/resources/007_encoded.png";
//        String decrypted = "/home/anna/IdeaProjects/crypto/src/main/resources/007_decoded.png";

//        String input = "/home/anna/IdeaProjects/crypto/src/main/resources/1.txt";
        String input = "/home/anna/Downloads/m.mkv";
//        String encrypted = "/home/anna/IdeaProjects/crypto/src/main/resources/1.encrypted.txt";
        String encrypted = "/home/anna/Downloads/m-enc.mkv";
//        String decrypted = "/home/anna/IdeaProjects/crypto/src/main/resources/1.decrypted.txt";
        String decrypted = "/home/anna/Downloads/m-dec.mkv";

        String key = "abcdef";

        encrypt(input, encrypted, key, 1024 * 1024 * 100);
        decrypt(encrypted, decrypted, key, 1024 * 1024 * 100);
    }

    static void encrypt(String inputPathname, String outputPathname, String key, int bufSize) {
        File sourceFile = new File(inputPathname);
        byte[] source = new byte[bufSize];
        byte[] buf = new byte[bufSize];

        try {
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            File encodedFile = new File(outputPathname);
            boolean creating = encodedFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(encodedFile);

            long inputFileLength = sourceFile.length();
            int percentsFinished = -1;
            int step = 0;
            int keyPos = 0;
            while (fileInputStream.available() > 0) {
                int read = fileInputStream.read(source, 0, bufSize);
                for (int i = 0; i < read; i++) {
                    buf[i] = ((byte) (source[i] + key.charAt(keyPos)));
                    keyPos = (keyPos + 1) % key.length();
                }
                step++;
                fileOutputStream.write(buf);
                int currentPercents = (int) ((long)bufSize * (long)step * 100 / inputFileLength);
                if (currentPercents != percentsFinished) {
                    percentsFinished = currentPercents;
                    System.out.println(percentsFinished + "%");
                }
            }
            fileOutputStream.close();
            System.out.println("finish");

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();
        } catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }
    }


    static void decrypt(String inputPathname, String outputPathname, String key, int bufSize) {
        File file = new File(inputPathname);

        byte[] source = new byte[bufSize];
        byte[] buf = new byte[bufSize];

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            File decodedFile = new File(outputPathname);
            boolean creating = decodedFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(decodedFile);

            long inputFileLength = file.length();
            int percentsFinished = -1;
            int step = 0;
            int keyPos = 0;
            while (fileInputStream.available() > 0) {
                int read = fileInputStream.read(source, 0, bufSize);
                for (int i = 0; i < read; i++) {
                    buf[i] = (byte) (source[i] - key.charAt(keyPos));
                    keyPos = (keyPos + 1) % key.length();
                }
                step++;
                fileOutputStream.write(buf);
                int currentPercents = (int) ((long)bufSize * (long)step * 100 / inputFileLength);
                if (currentPercents != percentsFinished) {
                    percentsFinished = currentPercents;
                    System.out.println(percentsFinished + "%");
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();
        } catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }
    }
}

