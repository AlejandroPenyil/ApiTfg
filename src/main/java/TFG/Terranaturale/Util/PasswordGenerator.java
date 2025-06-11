package TFG.Terranaturale.Util;

/**
 * Utility class to generate encrypted passwords for initial data loading.
 * This class is meant to be run once to generate the encrypted passwords
 * and then the output can be copied to the Liquibase changelog file.
 */
public class PasswordGenerator {
    public static void main(String[] args) {
        PasswordUtils passwordUtils = new PasswordUtils();
        
        // Generate encrypted password for "root"
        String encryptedRoot = passwordUtils.encryptPassword("root");
        System.out.println("Encrypted password for 'root': " + encryptedRoot);
        
        // You can add more passwords here if needed
    }
}