package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class EncryptorLogic {

    public interface ProgressUpdateListener {
        void progressUpdated(int percents);
    }

    static int percentsFinished = -1;

    public static void encrypt(boolean encrypt,
                               InputStream fileInputStream,
                               OutputStream fileOutputStream,
                               long sourceFileLength,
                               char[] key,
                               int bufSize,
                               ProgressUpdateListener progressUpdateListener) throws ReadException, WriteException {

        byte[] buf;
        if (sourceFileLength > bufSize || sourceFileLength == 0) {
            buf = new byte[bufSize];
        } else {
            buf = new byte[(int) sourceFileLength];
        }
        int step = 0;
        int keyPos = 0;
        long inputFileLength = sourceFileLength;

        while (true) {
            int read = 0;
            try {
                if (!(fileInputStream.available() > 0)) break;
                read = fileInputStream.read(buf, 0, buf.length);
            } catch (IOException e) {
                throw new ReadException("Can't read input", e);
            }


            for (int i = 0; i < read; i++) {
                if (encrypt) {
                    buf[i] = ((byte) (buf[i] + key[keyPos]));
                } else {
                    buf[i] = ((byte) (buf[i] - key[keyPos]));
                }
                keyPos = (keyPos + 1) % key.length;
            }
            step++;
            try {
                fileOutputStream.write(buf, 0, read);
            } catch (IOException e) {
                throw new WriteException("Can't write to output", e);
            }

            int currentPercents = (int) ((long) buf.length * (long) step * 100 / inputFileLength);
            if (currentPercents != percentsFinished) {
                percentsFinished = currentPercents;
                progressUpdateListener.progressUpdated(percentsFinished);
            }
        }
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            throw new WriteException("Can't close outputStream", e);
        }
    }
}
