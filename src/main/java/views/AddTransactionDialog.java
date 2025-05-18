package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddTransactionDialog extends JDialog {
    private JTextField dateField;
    private JTextField descriptionField;
    private JTextField amountField;
    private JComboBox<String> typeComboBox;
    private boolean confirmed = false;

    public AddTransactionDialog(Frame owner) {
        super(owner, "Add Transaction", true);
        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        // 创建主面板
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 日期输入
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        dateField = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        mainPanel.add(dateField, gbc);

        // 描述输入
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descriptionField = new JTextField();
        mainPanel.add(descriptionField, gbc);

        // 金额输入
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        amountField = new JTextField();
        mainPanel.add(amountField, gbc);

        // 交易类型选择
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        typeComboBox = new JComboBox<>(new String[]{"Income", "Expense"});
        mainPanel.add(typeComboBox, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");

        confirmButton.addActionListener(e -> {
            if (validateInput()) {
                confirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private boolean validateInput() {
        if (dateField.getText().trim().isEmpty() ||
            descriptionField.getText().trim().isEmpty() ||
            amountField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getDate() {
        return dateField.getText();
    }

    public String getDescription() {
        return descriptionField.getText();
    }

    public double getAmount() {
        return Double.parseDouble(amountField.getText());
    }

    public String getTransactionType() {
        return (String) typeComboBox.getSelectedItem();
    }
} 