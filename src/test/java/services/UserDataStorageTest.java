package data;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;

/**
 * Unit test class for {@link UserDataStorage}.
 * This class tests user registration and login authentication functionalities.
 * Each test ensures that the user data file is reset before execution.
 */
public class UserDataStorageTest {

    /** Path to the test user data file. */
    private static final String TEST_FILE = "users.txt";

    /**
     * Sets up the test environment by deleting the test user file
     * to avoid interference between test cases.
     */
    @Before
    public void setUp() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Tests successful user registration and login.
     * Ensures that the registered user can log in with correct credentials.
     */
    @Test
    public void testRegisterAndLoginSuccess() {
        UserDataStorage.registerUser("testuser", "test123");
        boolean result = UserDataStorage.authenticateUser("testuser", "test123");
        assertTrue(result);
    }

    /**
     * Tests login failure due to an incorrect username.
     * Ensures that authentication fails when the username is not registered.
     */
    @Test
    public void testLoginFailWrongUsername() {
        UserDataStorage.registerUser("userA", "abc");
        boolean result = UserDataStorage.authenticateUser("wrongUser", "abc");
        assertFalse(result);
    }

    /**
     * Tests login failure due to an incorrect password.
     * Ensures that authentication fails when the password is incorrect.
     */
    @Test
    public void testLoginFailWrongPassword() {
        UserDataStorage.registerUser("userB", "correct");
        boolean result = UserDataStorage.authenticateUser("userB", "wrong");
        assertFalse(result);
    }
}
