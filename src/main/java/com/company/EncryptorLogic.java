package com.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EncryptorLogic {

    interface ProgressUpdateListener {
        void progressUpdated(int percents);
    }
    static int percentsFinished = -1;

    static void encrypt(boolean encrypt, String inputPathname, String outputPathname, String key, int bufSize, ProgressUpdateListener progressUpdateListener) {
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
                    progressUpdateListener.progressUpdated(percentsFinished);
                }
            }
            fileOutputStream.close();

        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        System.out.println("finish");
    }
}