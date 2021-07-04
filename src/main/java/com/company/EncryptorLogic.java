package com.company;

import java.io.InputStream;
import java.io.OutputStream;

public interface EncryptorLogic {
    void encrypt(boolean encrypt, InputStream inputStream, OutputStream outputStream, char[] password) throws ReadException, WriteException;
}
