package com.company;

import java.io.*;

public class Encryptor {
    public static void main(String[] args) {


//        String input = "/home/anna/IdeaProjects/learnjava1/src/main/resources/007.png";
//        String encrypted = "/home/anna/IdeaProjects/learnjava1/src/main/resources/007_encoded.png";
//        String decrypted = "/home/anna/IdeaProjects/learnjava1/src/main/resources/007_decoded.png";

        String input = "/home/anna/IdeaProjects/learnjava1/src/main/resources/1.txt";
        String encrypted = "/home/anna/IdeaProjects/learnjava1/src/main/resources/1.encrypted.txt";
        String decrypted = "/home/anna/IdeaProjects/learnjava1/src/main/resources/1.decrypted.txt";

        String key = "sECrET";

        encrypt(input, encrypted, key);
        decrypt(encrypted, decrypted, key);
    }

    static void encrypt(String inputPathname, String outputPathname, String key) {
        File sourceFile = new File(inputPathname);
        byte[] source = new byte[(int) sourceFile.length()];
        byte[] res = new byte[source.length];

        try {
            FileInputStream fileInputStream = new FileInputStream(sourceFile);

            BufferedInputStream buf = new BufferedInputStream(fileInputStream, 2);


            fileInputStream.read(source);

            for (int i = 0; i < source.length; i++) {
                int keyPos = i % key.length();
                res[i] = (byte) (source[i] + key.charAt(keyPos));
            }
            for (int i = 0; i < res.length; i++) {
                System.out.println(res[i]);
            }
            System.out.println("finish");

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();
        } catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }

        try {
            File encodedFile = new File(outputPathname);
            boolean creating = encodedFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(encodedFile);
            fileOutputStream.write(res);
            fileOutputStream.close();

        } catch (IOException e) {
            System.out.println("Exception Occurred:");
            e.printStackTrace();
        }
    }


    static void decrypt(String inputPathname, String outputPathname, String key) {
        File file = new File(inputPathname);

        byte[] source = new byte[(int) file.length()];
        byte[] res = new byte[source.length];

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(source);

            for (int i = 0; i < source.length; i++) {
                int a;
                a = i % key.length();
                res[i] = (byte) (source[i] - key.charAt(a));
            }
            for (int i = 0; i < res.length; i++) {
                System.out.println(res[i]);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();
        } catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }

        try {
            File decodedFile = new File(outputPathname);
            boolean creating = decodedFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(decodedFile);
            fileOutputStream.write(res);
        } catch (IOException e) {
            System.out.println("Exception Occurred:");
            e.printStackTrace();
        }
    }
}
