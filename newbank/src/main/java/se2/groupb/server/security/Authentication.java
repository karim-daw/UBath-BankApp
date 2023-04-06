package se2.groupb.server.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Authentication {

    public static boolean authenticatePassword(String plainPassword, String hashedPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(plainPassword.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            String hashedPlainPassword = sb.toString();
            return hashedPlainPassword.equals(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            // handle exception
            return false;
        }
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // handle exception
            return null;
        }
    }
}
