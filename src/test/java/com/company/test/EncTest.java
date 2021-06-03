package com.company.test;

import com.company.EncryptorLogic;
import com.company.ReadException;
import com.company.WriteException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

public class EncTest {
    @Test
    void sizesEq() throws IOException, ReadException, WriteException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        EncryptorLogic.encrypt(true, new ByteArrayInputStream("qwe".getBytes()), output, 3, new char[]{'a', 's', 'd'}, 3, new EncryptorLogic.ProgressUpdateListener() {
            @Override
            public void progressUpdated(int percents) {
            }
        });
        byte[] bytes = output.toByteArray();

        Assertions.assertArrayEquals(new byte[]{-46, -22, -55}, bytes);
    }

    @Test
    void bufSizeNotMultipleOfInp() throws IOException, ReadException, WriteException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        EncryptorLogic.encrypt(true, new ByteArrayInputStream("qwe".getBytes()), output, 3, new char[]{'a', 's', 'd'}, 2, new EncryptorLogic.ProgressUpdateListener() {
            @Override
            public void progressUpdated(int percents) {
            }
        });
        byte[] bytes1 = output.toByteArray();

        Assertions.assertArrayEquals(new byte[]{-46, -22, -55}, bytes1);
    }

    @Test
    void bufSizeMultipleOfInp() throws IOException, ReadException, WriteException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        EncryptorLogic.encrypt(true, new ByteArrayInputStream("qweqwe".getBytes()), output, 6, new char[]{'a', 's', 'd', 'a', 's', 'd'}, 2, new EncryptorLogic.ProgressUpdateListener() {
            @Override
            public void progressUpdated(int percents) {
            }
        });
        byte[] bytes = output.toByteArray();

        Assertions.assertArrayEquals(new byte[]{-46, -22, -55, -46, -22, -55}, bytes);
    }

    @Test
    void bufSizeBiggerThanInp() throws IOException, ReadException, WriteException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        EncryptorLogic.encrypt(true, new ByteArrayInputStream("qwe".getBytes()), output, 3, new char[]{'a', 's', 'd'}, 4, new EncryptorLogic.ProgressUpdateListener() {
            @Override
            public void progressUpdated(int percents) {
            }
        });
        byte[] bytes = output.toByteArray();

        Assertions.assertArrayEquals(new byte[]{-46, -22, -55}, bytes);
    }

    @Test
    void keyIsSmallerInp() throws IOException, ReadException, WriteException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        EncryptorLogic.encrypt(true, new ByteArrayInputStream("qwe".getBytes()), output, 3, new char[]{'a', 's'}, 3, new EncryptorLogic.ProgressUpdateListener() {
            @Override
            public void progressUpdated(int percents) {
            }
        });
        byte[] bytes = output.toByteArray();

        Assertions.assertArrayEquals(new byte[]{-46, -22, -58}, bytes);
    }

    @Test
    void keyIsLongerInp() throws IOException, ReadException, WriteException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        EncryptorLogic.encrypt(true, new ByteArrayInputStream("qwe".getBytes()), output, 3, new char[]{'a', 's', 'd', 'f'}, 3, new EncryptorLogic.ProgressUpdateListener() {
            @Override
            public void progressUpdated(int percents) {
            }
        });
        byte[] bytes = output.toByteArray();

        Assertions.assertArrayEquals(new byte[]{-46, -22, -55}, bytes);
    }
}