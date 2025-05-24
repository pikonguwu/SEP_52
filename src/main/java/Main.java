package ui;

import javax.swing.*;

import components.RoundedButton;
import components.RoundedPanel;
import constants.AppConstants;
import views.*;
import data.FileHandler; // ✅ 新加
import Entity.Transaction; // ✅ 还需要加这行
import services.SecurityService;
import ui.LoginPage;

import java.awt.*;
import java.util.Enumeration;
import java.util.List;

/**
 * The main class that launches the application's graphical user interface
 * (GUI).
 * <p>
 * This class serves as the entry point for the Swing application, handling
 * initial setup such as setting the global UI font and starting the primary
 * application window.
 * </p>
 */
public class Main {

    /**
     * The main entry point for the application.
     * <p>
     * This method performs initial setup, including setting the global UI font,
     * and then launches the main application window (the login page) on the
     * Swing Event Dispatch Thread (EDT).
     * </p>
     * 
     * @param args Command-line arguments (not currently used by this application).
     */
    public static void main(String[] args) {
        // 设置全局字体为 Segoe UI
        setUIFont(new Font("Microsoft YaHei", Font.PLAIN, 14));

        // —— 开始加密演示逻辑 ——
        System.out.println("开始读 transaction.txt");
        FileHandler handler = new FileHandler();
        List<Transaction> imported = handler.importTransactions("transaction.txt");
        System.out.println("读取到的交易数量: " + imported.size());

        System.out.println("开始保存 secure_transactions.txt");
        handler.saveTransactions(imported, "secure_transactions.txt");
        System.out.println("已生成 secure_transactions.txt");

        for (Transaction t : imported) {
            String plain = t.getDescription() + "," + t.getAmount() + "," + t.getCategory();
            String encrypted = SecurityService.encrypt(plain);
            System.out.println("  原文: " + plain);
            System.out.println("  密文: " + encrypted);
        }
        System.out.println("—— 演示结束 ——");

        // 启动登录页面（放在 UI 线程中更安全）
        SwingUtilities.invokeLater(() -> new LoginPage());
    }

    /**
     * Sets the default font for all Swing UI components managed by UIManager.
     * <p>
     * This method iterates through all default UI properties available in
     * {@code UIManager.getDefaults()} and applies the specified font
     * to any property whose value is an instance of {@code Font}.
     * </p>
     * 
     * @param font The {@code Font} to set as the default for UI components.
     */
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