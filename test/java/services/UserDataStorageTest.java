package data;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;

public class UserDataStorageTest {

    private static final String TEST_FILE = "users.txt"; // 默认文件

    @Before
    public void setUp() {
        // 每次测试前清空测试文件，避免干扰
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testRegisterAndLoginSuccess() {
        UserDataStorage.registerUser("testuser", "test123");
        boolean result = UserDataStorage.authenticateUser("testuser", "test123");
        assertTrue(result);
    }

    @Test
    public void testLoginFailWrongUsername() {
        UserDataStorage.registerUser("userA", "abc");
        boolean result = UserDataStorage.authenticateUser("wrongUser", "abc");
        assertFalse(result);
    }

    @Test
    public void testLoginFailWrongPassword() {
        UserDataStorage.registerUser("userB", "correct");
        boolean result = UserDataStorage.authenticateUser("userB", "wrong");
        assertFalse(result);
    }
}
