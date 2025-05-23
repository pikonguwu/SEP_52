package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A JPanel that provides user interface components for configuring privacy settings.
 * It includes checkboxes for controlling transaction history storage, AI recommendations,
 * and security notifications, along with a save button.
 */
public class PrivacySettingsPanel extends JPanel {
    private JCheckBox allowTransactionStorageCheckBox;
    private JCheckBox enablePersonalizedAIBox;
    private JCheckBox enableSecurityNotificationsBox;
    private JButton saveButton;

    /**
     * Constructs a new PrivacySettingsPanel.
     * Initializes the layout, creates and adds the privacy setting checkboxes,
     * the save button, and sets up the action listener for the save button.
     */
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

    /**
     * Checks if storing transaction history is allowed based on the checkbox selection.
     *
     * @return {@code true} if the "Allow storing my transaction history" checkbox is selected, {@code false} otherwise.
     */
    public boolean isTransactionStorageAllowed() {
        return allowTransactionStorageCheckBox.isSelected();
    }

    /**
     * Checks if personalized AI recommendations are enabled based on the checkbox selection.
     *
     * @return {@code true} if the "Enable personalized AI recommendations" checkbox is selected, {@code false} otherwise.
     */
    public boolean isAIEnabled() {
        return enablePersonalizedAIBox.isSelected();
    }

    /**
     * Checks if security notifications are enabled based on the checkbox selection.
     *
     * @return {@code true} if the "Receive security notifications" checkbox is selected, {@code false} otherwise.
     */
    public boolean isSecurityNotificationsEnabled() {
        return enableSecurityNotificationsBox.isSelected();
    }
}