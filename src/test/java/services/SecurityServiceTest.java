package services;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link SecurityService}.
 * <p>
 * Verifies that the encryption and decryption methods behave correctly under valid
 * and invalid inputs.
 */
public class SecurityServiceTest {

    /**
     * Tests that invoking {@link SecurityService#encrypt(String)} followed by
     * {@link SecurityService#decrypt(String)} on a valid plaintext string
     * returns the original string.
     */
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

    /**
     * Tests that invoking {@link SecurityService#encrypt(String)} on any non-null
     * input always produces a non-null ciphertext.
     */
    @Test
    public void testEncryptProducesNonNull() {
        String encrypted = SecurityService.encrypt("hello");
        assertNotNull(encrypted);
    }

    /**
     * Tests that invoking {@link SecurityService#decrypt(String)} on a clearly invalid
     * ciphertext (containing illegal Base64 characters) returns null rather than throwing
     * an exception.
     */
    @Test
    public void testDecryptInvalidStringReturnsNull() {
        String result = SecurityService.decrypt("not_valid_ciphertext");
        assertNull(result); // 解密失败应返回 null，而不是抛异常
    }
}
