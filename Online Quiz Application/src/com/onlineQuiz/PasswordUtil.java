package com.onlineQuiz;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    // Generate a random salt
    public static String getSalt() throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);  // Convert salt to a Base64 encoded string
    }

    // Hash the password with the salt
    public static String hashPassword(String password, String salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");  // SHA-256 hashing algorithm
        md.update(salt.getBytes());  // Add salt to the password
        byte[] hashedPassword = md.digest(password.getBytes());  // Hash the password + salt

        return Base64.getEncoder().encodeToString(hashedPassword);  // Return Base64 encoded hash
    }

    // Verify the password entered by the user
    public static boolean verifyPassword(String password, String storedHash, String salt) throws Exception {
        // Hash the entered password with the stored salt
        String hashedPassword = hashPassword(password, salt);

        // Compare the hashed password with the stored hash
        return hashedPassword.equals(storedHash);
    }
}
