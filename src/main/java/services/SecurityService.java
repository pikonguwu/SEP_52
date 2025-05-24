package services;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
/**
 * A service class that provides encryption and decryption functionality using AES (Advanced Encryption Standard).
 * This class implements symmetric encryption/decryption using a 128-bit AES key.
 * 
 * <p>The service uses a default secret key for encryption and decryption operations.
 * For production use, it is recommended to store the secret key in a configuration file
 * or environment variable rather than hardcoding it.
 * 
 * <p>Example usage:
 * <pre>
 * String encrypted = SecurityService.encrypt("sensitive data");
 * String decrypted = SecurityService.decrypt(encrypted);
 * </pre>
 * 
 * <p>Note: This implementation uses AES encryption in ECB mode. For production use,
 * consider using more secure modes like CBC or GCM with proper initialization vectors.
 * 
 * @author System
 * @version 1.0
 */
public class SecurityService {
    private static final String AES = "AES";
    // 你可以将这个密钥存在配置文件或从环境变量读取
    private static final String DEFAULT_SECRET = "BuckBrainSuperKey"; // 应该至少16位
    /**
     * Generates an AES secret key from the provided seed string.
     * Uses SHA1PRNG for secure random number generation.
     * 
     * @param seed the seed string used to generate the secret key
     * @return a SecretKeySpec instance for AES encryption
     * @throws Exception if key generation fails
     */
    // 根据密钥生成 AES 对称加密密钥
    private static SecretKeySpec getSecretKey(String seed) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(AES);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(seed.getBytes(StandardCharsets.UTF_8));
        keyGen.init(128, random);
        SecretKey secretKey = keyGen.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), AES);
    }
    /**
     * Encrypts a string using AES encryption.
     * The encrypted result is returned as a Base64 encoded string.
     * 
     * @param content the string to be encrypted
     * @return Base64 encoded encrypted string, or null if encryption fails
     */
    public static String encrypt(String content) {
        try {
            SecretKeySpec secretKey = getSecretKey(DEFAULT_SECRET);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            System.err.println("加密失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * Decrypts a Base64 encoded encrypted string using AES decryption.
     * 
     * @param encryptedContent the Base64 encoded encrypted string to be decrypted
     * @return the decrypted string, or null if decryption fails
     */
    public static String decrypt(String encryptedContent) {
        try {
            SecretKeySpec secretKey = getSecretKey(DEFAULT_SECRET);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedContent);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("解密失败: " + e.getMessage());
            return null;
        }
    }
}
