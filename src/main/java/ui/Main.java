package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) {
        // 设置全局字体为 Segoe UI
        setUIFont(new Font("Segoe UI", Font.PLAIN, 14));

        // 启动登录页面（放在 UI 线程中更安全）
        SwingUtilities.invokeLater(() -> new LoginPage());
    }

    // 工具方法：遍历 UIManager 的所有键，统一设置字体
    public static void setUIFont(Font font) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof Font) {
                UIManager.put(key, font);
            }
        }
    }
}
