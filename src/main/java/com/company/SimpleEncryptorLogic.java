package com.company;

import com.encryptor.ReadException;
import com.encryptor.WriteException;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class SimpleEncryptorLogic implements com.encryptor.EncryptorLogic {

    @Override
    public void encrypt(boolean encrypt, InputStream inputStream, OutputStream outputStream, char[] password) throws ReadException, WriteException {
        long lengthInputStream = 0;
        try {
            lengthInputStream = inputStream.available();
        } catch (IOException e) {
            throw new ReadException("something wrong with InputStream");
        }

        simpleEncrypt(encrypt, inputStream, outputStream, lengthInputStream, password, 1024);
    }

    public void simpleEncrypt(boolean encrypt,
                              InputStream fileInputStream,
                              OutputStream fileOutputStream,
                              long sourceFileLength,
                              char[] key,
                              int bufSize) throws ReadException, WriteException {

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
        }
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            throw new WriteException("Can't close outputStream", e);
        }
    }
}
