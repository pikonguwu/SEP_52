package data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import services.SecurityService;

public class UserDataStorage {

    private static final String FILE_PATH = "users.txt"; // 文件路径，保存用户信息

    // 注册：将用户信息保存到文件
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

    // 登录：检查用户名和密码是否匹配
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
