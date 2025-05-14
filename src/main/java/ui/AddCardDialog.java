package ui;

import javax.swing.*;
import java.awt.*;
import components.RoundedButton;
import components.RoundedPanel;
import constants.AppConstants;

public class AddCardDialog extends JDialog {
    private JTextField cardNumberField;
    private JTextField cardholderField;
    private JTextField expiryDateField;
    private JTextField balanceField;
    private boolean confirmed = false;

    public AddCardDialog(Frame parent) {
        super(parent, "Add New Card", true);
        initUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        
        // 创建主面板
        RoundedPanel mainPanel = new RoundedPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 创建表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // 添加表单字段
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Card Number:"), gbc);
        gbc.gridx = 1;
        cardNumberField = new JTextField(20);
        formPanel.add(cardNumberField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Cardholder Name:"), gbc);
        gbc.gridx = 1;
        cardholderField = new JTextField(20);
        formPanel.add(cardholderField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Expiry Date (MM/YY):"), gbc);
        gbc.gridx = 1;
        expiryDateField = new JTextField(20);
        formPanel.add(expiryDateField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Initial Balance ($):"), gbc);
        gbc.gridx = 1;
        balanceField = new JTextField(20);
        formPanel.add(balanceField, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        RoundedButton cancelButton = new RoundedButton("Cancel");
        RoundedButton confirmButton = new RoundedButton("Add Card");
        
        cancelButton.setBackground(AppConstants.PRIMARY_COLOR);
        cancelButton.setForeground(Color.WHITE);
        confirmButton.setBackground(AppConstants.PRIMARY_COLOR);
        confirmButton.setForeground(Color.WHITE);
        
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
        
        confirmButton.addActionListener(e -> {
            if (validateInput()) {
                confirmed = true;
                dispose();
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private boolean validateInput() {
        // 验证卡号（16位数字）
        if (!cardNumberField.getText().matches("\\d{16}")) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid 16-digit card number",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 验证持卡人姓名
        if (cardholderField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter the cardholder name",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 验证有效期（MM/YY格式）
        if (!expiryDateField.getText().matches("(0[1-9]|1[0-2])/([0-9]{2})")) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid expiry date in MM/YY format",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 验证余额
        try {
            Double.parseDouble(balanceField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid balance amount",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public String getCardNumber() {
        return cardNumberField.getText();
    }
    
    public String getCardholderName() {
        return cardholderField.getText();
    }
    
    public String getExpiryDate() {
        return expiryDateField.getText();
    }
    
    public double getBalance() {
        return Double.parseDouble(balanceField.getText());
    }
} 