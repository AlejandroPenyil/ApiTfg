package TFG.Terranaturale.Util;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for password encryption and verification.
 */
@Component
public class PasswordUtils {

    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    private static final String DELIMITER = ":";

    /**
     * Encrypts a password using SHA-256 with a random salt.
     *
     * @param rawPassword the raw password to encrypt
     * @return the encrypted password with salt (format: "salt:hash")
     */
    public String encryptPassword(String rawPassword) {
        if (rawPassword == null) {
            return null;
        }

        try {
            // Generate a random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            // Hash the password with the salt
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hashedPassword = md.digest(rawPassword.getBytes(StandardCharsets.UTF_8));

            // Convert to Base64 strings
            String saltStr = Base64.getEncoder().encodeToString(salt);
            String hashStr = Base64.getEncoder().encodeToString(hashedPassword);

            // Return salt and hash combined
            return saltStr + DELIMITER + hashStr;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error encrypting password", e);
        }
    }

    /**
     * Verifies if a raw password matches an encrypted password.
     *
     * @param rawPassword the raw password to check
     * @param storedPassword the encrypted password to check against (format: "salt:hash")
     * @return true if the passwords match, false otherwise
     */
    public boolean verifyPassword(String rawPassword, String storedPassword) {
        if (rawPassword == null || storedPassword == null) {
            return false;
        }

        try {
            // Split the stored password into salt and hash
            String[] parts = storedPassword.split(DELIMITER);
            if (parts.length != 2) {
                return false;
            }

            String saltStr = parts[0];
            String hashStr = parts[1];

            // Decode from Base64
            byte[] salt = Base64.getDecoder().decode(saltStr);
            byte[] storedHash = Base64.getDecoder().decode(hashStr);

            // Hash the input password with the same salt
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] newHash = md.digest(rawPassword.getBytes(StandardCharsets.UTF_8));

            // Compare the hashes
            if (storedHash.length != newHash.length) {
                return false;
            }

            // Time-constant comparison to prevent timing attacks
            int diff = 0;
            for (int i = 0; i < storedHash.length; i++) {
                diff |= storedHash[i] ^ newHash[i];
            }
            return diff == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
