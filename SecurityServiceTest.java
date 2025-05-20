package services;

import org.junit.Test;
import static org.junit.Assert.*;

public class SecurityServiceTest {

    @Test
    public void testEncryptAndDecrypt() {
        String original = "user:password123";

        String encrypted = SecurityService.encrypt(original);
        String decrypted = SecurityService.decrypt(encrypted);

        // 加密后应不等于原文
        assertNotEquals(original, encrypted);
        // 解密后应等于原文
        assertEquals(original, decrypted);
    }

    @Test
    public void testEncryptProducesNonNull() {
        String encrypted = SecurityService.encrypt("hello");
        assertNotNull(encrypted);
    }

    @Test
    public void testDecryptInvalidStringReturnsNull() {
        String result = SecurityService.decrypt("not_valid_ciphertext");
        assertNull(result); // 解密失败应返回 null，而不是抛异常
    }
}
