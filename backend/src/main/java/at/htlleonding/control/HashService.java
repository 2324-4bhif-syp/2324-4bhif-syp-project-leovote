package at.htlleonding.control;

import jakarta.enterprise.context.ApplicationScoped;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@ApplicationScoped
public class HashService {
    public String calculateSHA256Hash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
