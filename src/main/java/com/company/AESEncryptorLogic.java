package com.company;

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

public class AESEncryptorLogic {
    static int iterationsEncrypt = 32768;
    static byte[] salt;
    static int bufSize = 1024;

    public static byte[] generateSalt(int length) {
        Random r = new SecureRandom();
        byte[] salt = new byte[length];
        r.nextBytes(salt);
        return salt;
    }

    static class Keys {
        public final SecretKey encryption, authentication;

        public Keys(SecretKey encryption, SecretKey authentication) {
            this.encryption = encryption;
            this.authentication = authentication;
        }
    }

    public static Keys getKey(int keyLength, char[] password, byte[] salt) {
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password, salt, iterationsEncrypt, keyLength + 8 * 8);
            SecretKey tmp = null;
            tmp = factory.generateSecret(spec);
            byte[] fullKey = tmp.getEncoded();
            SecretKey authKey = new SecretKeySpec(
                    Arrays.copyOfRange(fullKey, 0, 8), "AES");
            SecretKey encKey = new SecretKeySpec(
                    Arrays.copyOfRange(fullKey, 8, fullKey.length), "AES");
            return new Keys(encKey, authKey);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void encrypt(int keyLength,
                               InputStream fileInputStream,
                               OutputStream fileOutputStream,
                               char[] password
    ) {
        byte[] salt = generateSalt(16);

        Keys keys = getKey(keyLength, password, salt);

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keys.encryption);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] iv = null;
        try {
            iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        }

        try {
            fileOutputStream.write(keyLength / 8);
            fileOutputStream.write(salt);
            fileOutputStream.write(keys.authentication.getEncoded());
            fileOutputStream.write(iv);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buf = new byte[bufSize]; //1024
        int bytesReadNum = 0;
        byte[] encrypted = null;
        while (true) {
            try {
                if (!((bytesReadNum = fileInputStream.read(buf)) > 0)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                encrypted = cipher.update(buf, 0, bytesReadNum);
                fileOutputStream.write(encrypted);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            encrypted = cipher.doFinal();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        if (encrypted != null) {
            try {
                fileOutputStream.write(encrypted);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void decrypt(InputStream fileInputStream,
                               OutputStream fileOutputStream,
                               char[] password
    ) {int keyLength=0;
        try {
            keyLength = fileInputStream.read() * 8;
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] salt = new byte[16];
        try {
            fileInputStream.read(salt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Keys keys = getKey(keyLength, password, salt);
        byte[] authRead = new byte[8];
        try {
            fileInputStream.read(authRead);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!Arrays.equals(keys.authentication.getEncoded(), authRead)) {
            try {
                throw new InvalidPasswordException();
            } catch (InvalidPasswordException e) {
                e.printStackTrace();
            }
        }
        byte[] iv = new byte[16];
        try {
            fileInputStream.read(iv);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keys.encryption, new IvParameterSpec(iv));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        byte[] buf = new byte[bufSize]; //1024
        int bytesReadNum = 0;
        byte[] decrypted = null;
        while (true) {
            try {
                if (!((bytesReadNum = fileInputStream.read(buf)) > 0)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            decrypted = cipher.update(buf, 0, bytesReadNum);
            if (decrypted != null) {
                try {
                    fileOutputStream.write(decrypted);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                decrypted = cipher.doFinal();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            if (decrypted != null) {
                try {
                    fileOutputStream.write(decrypted);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class InvalidPasswordException extends Exception {
    }
}