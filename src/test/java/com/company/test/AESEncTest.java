package com.company.test;

import com.company.AESEncryptorLogic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AESEncTest {
    @Test
    void sizesEq() {
        AESEncryptorLogic aesEncryptorLogic = new AESEncryptorLogic();

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        char[] key = {'a', 's', 'd'};

        aesEncryptorLogic.encrypt(true, new ByteArrayInputStream("qwe".getBytes()), output, key);
        byte[] bytesEnc = output.toByteArray();
        InputStream inputEnc = new ByteArrayInputStream(bytesEnc);

        ByteArrayOutputStream output1 = new ByteArrayOutputStream();

        aesEncryptorLogic.encrypt(false, inputEnc, output1, key);
        byte[] bytesDec = output1.toByteArray();

        byte[] exp = "qwe".getBytes();
        Assertions.assertArrayEquals(exp, bytesDec);
    }
}
