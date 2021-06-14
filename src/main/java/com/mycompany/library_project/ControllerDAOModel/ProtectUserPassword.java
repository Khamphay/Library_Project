package com.mycompany.library_project.ControllerDAOModel;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class ProtectUserPassword {
    private static final Random randKey = new SecureRandom();
    private static final String encryptedKey = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int ITERATIONS = 100000;
    private static final int key_length = 256;

    public static String getSalt(int length) {
        StringBuilder salt = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            salt.append(encryptedKey.charAt(randKey.nextInt(encryptedKey.length())));
        }
        return new String(salt);
    }

    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, ITERATIONS, key_length);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory sckeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return sckeyFactory.generateSecret(keySpec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            keySpec.clearPassword();
        }
    }

    public static String generateSecurePassword(String password, String salt) {
        String encodeKey = null;
        byte[] scrPassword = hash(password.toCharArray(), salt.getBytes());
        encodeKey = Base64.getEncoder().encodeToString(scrPassword);
        return encodeKey;
    }

    public static boolean verifyPassword(String providePassword, String sucurePassword, String salt) {
        boolean result = false;

        // Todo: Generate New secure password with the same salt
        String newScrPassword = generateSecurePassword(providePassword, salt);

        // Todo: check two passwords with the same salt
        result = newScrPassword.equalsIgnoreCase(sucurePassword);
        return result;
    }
}
