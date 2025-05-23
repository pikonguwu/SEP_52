package data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import services.SecurityService;

/**
 * Handles user data storage and authentication operations.
 * This class provides methods for user registration and authentication,
 * storing user credentials in an encrypted format in a file.
 */
public class UserDataStorage {

    /** File path for storing user information */
    private static final String FILE_PATH = "users.txt"; // 文件路径，保存用户信息

    /**
     * Registers a new user by saving their encrypted credentials to the file.
     * The username and password are encrypted before being stored.
     *
     * @param username The username of the new user
     * @param password The password of the new user
     * @throws IOException If an I/O error occurs while writing to the file
     */
    public static void registerUser(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String encrypted = SecurityService.encrypt(username + ":" + password);
            writer.write(encrypted);// 修改后的加密方式
            // writer.write(username + ":" + password); // 使用冒号分隔用户名和密码
            writer.newLine(); // 换行
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Authenticates a user by checking their credentials against stored data.
     * Reads the encrypted user data from file and verifies the username and password.
     *
     * @param username The username to authenticate
     * @param password The password to verify
     * @return true if the credentials are valid, false otherwise
     * @throws IOException If an I/O error occurs while reading the file
     */
    public static boolean authenticateUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String decrypted = SecurityService.decrypt(line);
                if (decrypted == null)
                    continue;
                String[] user = decrypted.split(":");
                if (user.length == 2 && user[0].equals(username) && user[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}