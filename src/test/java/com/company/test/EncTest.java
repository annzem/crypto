package com.company.test;

import com.company.EncryptorLogic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

public class EncTest {
    @Test
    void sizesEq() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        EncryptorLogic.encrypt(true, new ByteArrayInputStream("qwe".getBytes()), output, 3, "asd", 3);
        byte[] bytes = output.toByteArray();

        Assertions.assertArrayEquals(new byte[]{-46, -22, -55}, bytes);
    }

    @Test
    void bufSizeNotMultipleOfInp() {
        ByteArrayOutputStream output1 = new ByteArrayOutputStream();

        EncryptorLogic.encrypt(true, new ByteArrayInputStream("qwe".getBytes()), output1, 3, "asd", 2);
        byte[] bytes1 = output1.toByteArray();

        Assertions.assertArrayEquals(new byte[]{-46, -22, -55}, bytes1);
    }

    @Test
    void bufSizeMultipleOfInp() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        EncryptorLogic.encrypt(true, new ByteArrayInputStream("qweqwe".getBytes()), output, 6, "asdasd", 2);
        byte[] bytes = output.toByteArray();

        Assertions.assertArrayEquals(new byte[]{-46, -22, -55, -46, -22, -55}, bytes);
    }

    @Test
    void bufSizeBiggerThanInp() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        EncryptorLogic.encrypt(true, new ByteArrayInputStream("qwe".getBytes()), output, 3, "asd", 4);
        byte[] bytes = output.toByteArray();

        Assertions.assertArrayEquals(new byte[]{-46, -22, -55}, bytes);
    }

    @Test
    void keyIsSmallerInp() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        EncryptorLogic.encrypt(true, new ByteArrayInputStream("qwe".getBytes()), output, 3, "as", 3);
        byte[] bytes = output.toByteArray();

        Assertions.assertArrayEquals(new byte[]{-46, -22, -58}, bytes);
    }

    @Test
    void keyIsLongerInp() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        EncryptorLogic.encrypt(true, new ByteArrayInputStream("qwe".getBytes()), output, 3, "asdf", 3);
        byte[] bytes = output.toByteArray();

        Assertions.assertArrayEquals(new byte[]{-46, -22, -55}, bytes);
    }
}