package views;

import constants.AppConstants;
import components.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A dialog for editing transaction information.
 * This dialog is used to modify details of an existing transaction.
 */
public class EditTransactionDialog extends JDialog {
    private JTextField dateField;
    private JTextField descriptionField;
    private JTextField amountField;
    private JComboBox<String> typeComboBox;
    private boolean confirmed = false;

    /**
     * Constructs a new EditTransactionDialog.
     *
     * @param parent The parent Frame for this dialog.
     * @param date The current date string to pre-populate the date field.
     * @param description The current description string to pre-populate the description field.
     * @param amount The current amount string to pre-populate the amount field.
     * @param type The current transaction type string (e.g., "Income", "Expense") to pre-select in the type combo box.
     */
    public EditTransactionDialog(Frame parent, String date, String description, String amount, String type) {
        super(parent, "编辑交易信息", true); // Title remains in Chinese as per original code

        // 设置对话框大小和位置
        setSize(400, 300);
        setLocationRelativeTo(parent);

        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 创建表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 日期字段
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("日期:"), gbc); // Label remains in Chinese

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        dateField = new JTextField(date, 20);
        formPanel.add(dateField, gbc);

        // 描述字段
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("描述:"), gbc); // Label remains in Chinese

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        descriptionField = new JTextField(description, 20);
        formPanel.add(descriptionField, gbc);

        // 金额字段
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("金额:"), gbc); // Label remains in Chinese

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        amountField = new JTextField(amount, 20);
        formPanel.add(amountField, gbc);

        // 类型下拉框
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("类型:"), gbc); // Label remains in Chinese

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        String[] types = {"Income", "Expense"};
        typeComboBox = new JComboBox<>(types);
        typeComboBox.setSelectedItem(type);
        formPanel.add(typeComboBox, gbc);

        // 添加表单面板到主面板
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // 取消按钮
        JButton cancelButton = new JButton("取消"); // Button text remains in Chinese
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        // 确认按钮
        JButton confirmButton = new JButton("确认"); // Button text remains in Chinese
        confirmButton.setBackground(AppConstants.PRIMARY_COLOR);
        confirmButton.setForeground(Color.WHITE);
        confirmButton.addActionListener(e -> {
            // 验证输入
            if (validateInput()) {
                confirmed = true;
                dispose();
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);

        // 添加按钮面板到主面板
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 添加主面板到对话框
        add(mainPanel);
    }

    /**
     * Validates the input data entered by the user.
     * Checks if date and description are not empty and if the amount is a valid number.
     *
     * @return {@code true} if the input is valid; {@code false} otherwise.
     */
    private boolean validateInput() {
        // 验证日期不为空
        if (dateField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "日期不能为空", // Message remains in Chinese
                "输入错误",   // Title remains in Chinese
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 验证描述不为空
        if (descriptionField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "描述不能为空", // Message remains in Chinese
                "输入错误",   // Title remains in Chinese
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 验证金额为数字
        try {
            Double.parseDouble(amountField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "金额必须是有效的数字", // Message remains in Chinese
                "输入错误",   // Title remains in Chinese
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Gets the date entered in the date field.
     *
     * @return The date as a String.
     */
    public String getDate() {
        return dateField.getText();
    }

    /**
     * Gets the description entered in the description field.
     *
     * @return The description as a String.
     */
    public String getDescription() {
        return descriptionField.getText();
    }

    /**
     * Gets the amount entered in the amount field.
     *
     * @return The amount as a String.
     */
    public String getAmount() {
        return amountField.getText();
    }

    /**
     * Gets the selected transaction type.
     *
     * @return The selected transaction type as a String (e.g., "Income", "Expense").
     */
    public String getTransactionType() {
        return (String) typeComboBox.getSelectedItem();
    }

    /**
     * Checks if the dialog was closed by confirming the changes.
     *
     * @return {@code true} if the "确认" (Confirm) button was clicked and input was valid;
     *         {@code false} if the "取消" (Cancel) button was clicked or the dialog was closed otherwise.
     */
    public boolean isConfirmed() {
        return confirmed;
    }
}