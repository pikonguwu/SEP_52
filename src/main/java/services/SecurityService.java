package services;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class SecurityService {
    private static final String AES = "AES";
    // 你可以将这个密钥存在配置文件或从环境变量读取
    private static final String DEFAULT_SECRET = "BuckBrainSuperKey"; // 应该至少16位

    // 根据密钥生成 AES 对称加密密钥
    private static SecretKeySpec getSecretKey(String seed) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(AES);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(seed.getBytes(StandardCharsets.UTF_8));
        keyGen.init(128, random);
        SecretKey secretKey = keyGen.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), AES);
    }

    // 加密字符串
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

    // 解密字符串
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
