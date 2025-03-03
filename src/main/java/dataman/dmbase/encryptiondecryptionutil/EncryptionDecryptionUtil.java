package dataman.dmbase.encryptiondecryptionutil;

import java.security.GeneralSecurityException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import dataman.dmbase.exception.EncryptionDecryptionException;


@Component
public class EncryptionDecryptionUtil {

    private SecretKey secretKey;

    /**
     * Constructor that allows setting a predefined AES key.
     */
    public EncryptionDecryptionUtil(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * Default constructor - Generates a new AES key.
     */
    public EncryptionDecryptionUtil() {
        try {
            this.secretKey = generateAESKey();
        } catch (Exception e) {
            throw new RuntimeException("Error generating AES key", e);
        }
    }

    /**
     * Generates an AES secret key.
     */
    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    /**
     * Allows manually setting an existing AES key.
     */
    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * Returns the AES secret key.
     */
    public SecretKey getSecretKey() {
        return secretKey;
    }

    /**
     * Encrypts a plain text message.
     */
	public String encrypt(String message) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedBytes = cipher.doFinal(message.getBytes());
			return Base64.getEncoder().encodeToString(encryptedBytes);

		} catch (GeneralSecurityException e) {
			throw new EncryptionDecryptionException("Encryption failed: " + e.getMessage());
		} catch (Exception e) {
			throw new EncryptionDecryptionException("Unexpected error during encryption: " + e.getMessage());
		}
	}

    /**
     * Decrypts an encrypted message.
     */
    public String decrypt(String encryptedMessage) {
        try {
        	Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedMessage);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
            
        }catch (GeneralSecurityException e) {
			throw new EncryptionDecryptionException("Encryption failed: " + e.getMessage());
		} catch (Exception e) {
			throw new EncryptionDecryptionException("Unexpected error during encryption: " + e.getMessage());
		}
    }

    /**
     * Encrypts a message using a provided AES key.
     */
    public String encryptWithKey(String message, SecretKey secretKey)  {
        try {
        	Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }catch (GeneralSecurityException e) {
			throw new EncryptionDecryptionException("Encryption failed: " + e.getMessage());
		} catch (Exception e) {
			throw new EncryptionDecryptionException("Unexpected error during encryption: " + e.getMessage());
		}
    }

    /**
     * Decrypts a message using a provided AES key.
     */
    public String decryptWithKey(String encryptedMessage, SecretKey secretKey) {
        try {
        	Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedMessage);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        }catch (GeneralSecurityException e) {
			throw new EncryptionDecryptionException("Encryption failed: " + e.getMessage());
		} catch (Exception e) {
			throw new EncryptionDecryptionException("Unexpected error during encryption: " + e.getMessage());
		}
    }
}





