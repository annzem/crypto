package com.company;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

//AES шифрование строки целиком

public class EncryptorAESLogic {
    public static String keyStr = "qwe";
    static byte[] encrypted;
    static IvParameterSpec ivParameterSpec;
    static SecretKey secretKeySpec;
    static String saltEncrypt = "salt";
    static int iterationsEncrypt = 65536;
    static  String encryptResult;

    public static void encryptBuf() {
        getIv();
        getKey();
        encryptResult = encryptAES("melocoton", ivParameterSpec, secretKeySpec);

    }

    public static AlgorithmParameterSpec getIv() {
        byte[] iv = new byte[16]; //cipher.getBlockSize()
        new SecureRandom().nextBytes(iv);
        ivParameterSpec = new IvParameterSpec(iv);
        return ivParameterSpec;
    }

    public static SecretKey getKey() {
        SecretKeyFactory factoryKeyEncrypt;
        try {
            factoryKeyEncrypt = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(keyStr.toCharArray(), saltEncrypt.getBytes(), iterationsEncrypt, 128);
            secretKeySpec = new SecretKeySpec(factoryKeyEncrypt.generateSecret(spec).getEncoded(), "AES");
            return secretKeySpec;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptAES(String input, AlgorithmParameterSpec ivParameterSpec, SecretKey secretKeySpec) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            encrypted = cipher.doFinal(input.getBytes());
            String encryptedStr = Base64.getEncoder().encodeToString(encrypted);
            System.out.println("input = " + Arrays.toString(input.getBytes()));
            System.out.println("encrypted = " + Arrays.toString(encrypted));
            return encryptedStr;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptAES(String input, AlgorithmParameterSpec ivParameterSpec, SecretKey secretKeySpec){
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(input));
            String decryptedStr = new String(decrypted);
            return (decryptedStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        EncryptorAESLogic aesEncryptorLogic = new EncryptorAESLogic();
        EncryptorAESLogic.encryptBuf();
        System.out.println("decrypted = " + EncryptorAESLogic.decryptAES(encryptResult, ivParameterSpec, secretKeySpec));
    }
}



