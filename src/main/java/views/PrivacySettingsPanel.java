package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrivacySettingsPanel extends JPanel {
    private JCheckBox allowTransactionStorageCheckBox;
    private JCheckBox enablePersonalizedAIBox;
    private JCheckBox enableSecurityNotificationsBox;
    private JButton saveButton;

    public PrivacySettingsPanel() {
        setLayout(new GridLayout(5, 1, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Privacy Settings"));

        allowTransactionStorageCheckBox = new JCheckBox("Allow storing my transaction history");
        enablePersonalizedAIBox = new JCheckBox("Enable personalized AI recommendations");
        enableSecurityNotificationsBox = new JCheckBox("Receive security notifications");

        saveButton = new JButton("Save Settings");

        add(allowTransactionStorageCheckBox);
        add(enablePersonalizedAIBox);
        add(enableSecurityNotificationsBox);
        add(new JLabel()); // Spacer
        add(saveButton);

        // 示例保存逻辑（可扩展为写入配置文件）
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean allowTransactions = allowTransactionStorageCheckBox.isSelected();
                boolean enableAI = enablePersonalizedAIBox.isSelected();
                boolean enableNotifications = enableSecurityNotificationsBox.isSelected();

                // 这里可调用服务保存用户设置
                JOptionPane.showMessageDialog(null, "Privacy settings saved successfully.");
            }
        });
    }

    // 可添加 get 方法以供外部调用
    public boolean isTransactionStorageAllowed() {
        return allowTransactionStorageCheckBox.isSelected();
    }

    public boolean isAIEnabled() {
        return enablePersonalizedAIBox.isSelected();
    }

    public boolean isSecurityNotificationsEnabled() {
        return enableSecurityNotificationsBox.isSelected();
    }
}
