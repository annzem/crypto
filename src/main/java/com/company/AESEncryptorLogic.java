package com.company;

import com.encryptor.EncryptorLogic;
import com.encryptor.ReadException;
import com.encryptor.WriteException;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Random;

@Component
public class AESEncryptorLogic implements EncryptorLogic {

    private static final int ITERATIONS = 32768;
    private static final int BUF_SIZE = 1024;
    private static final int AUTH_KEY = 8;
    private static final int SALT = 16;
    private Cipher cipher;

    @Override
    public void encrypt(boolean encrypt, InputStream inputStream, OutputStream outputStream, char[] password) throws ReadException, WriteException {
        aESEncrypt(encrypt, 128, inputStream, outputStream, password);
    }

    private static byte[] generateSalt(int length) {
        Random r = new SecureRandom();
        byte[] salt = new byte[length];
        r.nextBytes(salt);
        return salt;
    }

    private static class Keys {
        public final SecretKey encryption, authentication;
        public Keys(SecretKey encryption, SecretKey authentication) {
            this.encryption = encryption;
            this.authentication = authentication;
        }
    }

    private Keys getKey(int keyLength, char[] password, byte[] salt) {
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, keyLength + AUTH_KEY * 8);
            SecretKey tmp;
            tmp = factory.generateSecret(spec);
            byte[] fullKey = tmp.getEncoded();
            SecretKey authKey = new SecretKeySpec(
                    Arrays.copyOfRange(fullKey, 0, 8), "AES");
            SecretKey encKey = new SecretKeySpec(
                    Arrays.copyOfRange(fullKey, 8, fullKey.length), "AES");
            return new Keys(encKey, authKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private void aESEncrypt(boolean encrypt,
                              int keyLength,
                              InputStream fileInputStream,
                              OutputStream fileOutputStream,
                              char[] password) throws ReadException, WriteException {
        if (encrypt) {
            byte[] salt = generateSalt(SALT);
            Keys keys = getKey(keyLength, password, salt);

            byte[] iv;
            try {
                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, keys.encryption);
                iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
            } catch (InvalidParameterSpecException |NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
                throw new RuntimeException(e);
            }

            try {
                fileOutputStream.write(keyLength / 8);
                fileOutputStream.write(salt);
                fileOutputStream.write(keys.authentication.getEncoded());
                fileOutputStream.write(iv);
            } catch (IOException e) {
                throw new WriteException(e);
            }
        } else {
            try {
                keyLength = fileInputStream.read() * 8;
                byte[] salt = new byte[16];
                fileInputStream.read(salt);
                Keys keys = getKey(keyLength, password, salt);
                byte[] authRead = new byte[8];
                fileInputStream.read(authRead);
                if (!Arrays.equals(keys.authentication.getEncoded(), authRead)) {
                    throw new RuntimeException("wrong password");
                }
                byte[] iv = new byte[16];
                fileInputStream.read(iv);
                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, keys.encryption, new IvParameterSpec(iv));
            } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new ReadException(e);
            }
        }
            byte[] buf = new byte[BUF_SIZE];
            int bytesReadNum;
            byte[] encrypted;
            while (true) {
                try {
                    if ((bytesReadNum = fileInputStream.read(buf)) < 1) break;
                } catch (IOException e) {
                    throw new ReadException(e);
                }
                encrypted = cipher.update(buf, 0, bytesReadNum);
                if (encrypted != null) {
                    try {
                        fileOutputStream.write(encrypted);
                    } catch (IOException e) {
                        throw new WriteException(e);
                    }
                }
            }
            try {
                encrypted = cipher.doFinal();
            } catch (IllegalBlockSizeException | BadPaddingException e) {
                throw new RuntimeException(e);
            }
            if (encrypted != null) {
                try {
                    fileOutputStream.write(encrypted);
                } catch (IOException e) {
                    throw new WriteException(e);
                }
            }
    }
}
