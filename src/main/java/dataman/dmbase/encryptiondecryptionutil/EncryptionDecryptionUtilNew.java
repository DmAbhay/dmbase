package dataman.dmbase.encryptiondecryptionutil;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import dataman.dmbase.exception.EncryptionDecryptionException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.security.GeneralSecurityException;
import java.util.Base64;

@Getter
@Component
@Setter
@Data
public class EncryptionDecryptionUtilNew {

    // AES Secret Key
    private SecretKey secretKey;

    /**
     * This method is called automatically after the server starts,
     * generating the AES Secret Key.
     */
    @PostConstruct
    private void initializeKey() {
        try {
            this.secretKey = generateAESKey();
        } catch (Exception e) {
            throw new RuntimeException("Error initializing AES key", e);
        }
    }
    
    
//    @PostConstruct
//    private void initializeKey() {
//        try {
//            // Hardcoded AES Key (Base64 decoded to RAW format)
//            String base64Key = "Wn9GWIOZInqJ3pQtfyjB9H/32kBVAxYC6GanZr/Mlbs=";
//            byte[] decodedKey = Base64.getDecoder().decode(base64Key);
//            this.secretKey = new SecretKeySpec(decodedKey, "AES"); // Create AES Key from raw bytes
//        } catch (Exception e) {
//            throw new RuntimeException("Error initializing AES key", e);
//        }
//    }

    /**
     * Method to generate an AES secret key.
     *
     * @return a newly generated AES Secret Key.
     */
    private SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // AES 256-bit key
        return keyGenerator.generateKey();
    }

//    /**
//     * Method to decrypt an encrypted response using the AES secret key.
//     *
//     * @param encryptedResponse The encrypted response in Base64.
//     * @return The decrypted plain text.
//     */
//    public String decrypt(String encryptedResponse) throws Exception {
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.DECRYPT_MODE, secretKey);
//
//        byte[] decodedResponse = Base64.getDecoder().decode(encryptedResponse);
//        byte[] decryptedBytes = cipher.doFinal(decodedResponse);
//        return new String(decryptedBytes);
//    }
//
//    /**
//     * Method to encrypt a message using the AES secret key.
//     *
//     * @param message The plain text message.
//     * @return The encrypted message in Base64.
//     */
//    public String encrypt(String message) throws Exception {
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//
//        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
//        return Base64.getEncoder().encodeToString(encryptedBytes);
//    }
    
    
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
     * Encrypt a message using the provided AES key.
     *
     * @param message The plain text message.
     * @return The encrypted message in Base64 format.
     * @throws Exception If encryption fails.
     */
    public String encryptWithKey(String message, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypt a message using the provided AES key.
     *
     * @param encryptedMessage The encrypted message in Base64 format.
     * @return The decrypted plain text message.
     * @throws Exception If decryption fails.
     */
    public String decryptWithKey(String encryptedMessage, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decodedMessage = Base64.getDecoder().decode(encryptedMessage);
        byte[] decryptedBytes = cipher.doFinal(decodedMessage);
        return new String(decryptedBytes);
    }


}
