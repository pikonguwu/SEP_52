package ui;

import javax.swing.*;
import java.awt.*;
import components.RoundedButton;
import components.RoundedPanel;
import constants.AppConstants;

/**
 * A custom dialog window used for adding details of a new card.
 * It provides input fields for card number, cardholder name, expiry date,
 * and initial balance. It includes input validation and confirms
 * whether the user successfully added the card or cancelled.
 */
public class AddCardDialog extends JDialog {
    
    /**
     * Text field for entering the card number.
     */
    private JTextField cardNumberField;
    
    /**
     * Text field for entering the cardholder name.
     */
    private JTextField cardholderField;
    
    /**
     * Text field for entering the card expiry date (MM/YY).
     */
    private JTextField expiryDateField;
    
    /**
     * Text field for entering the initial balance.
     */
    private JTextField balanceField;
    
    /**
     * Flag indicating whether the dialog was confirmed by the user
     * (i.e., the "Add Card" button was clicked after successful validation).
     */
    private boolean confirmed = false;

    /**
     * Constructs a new AddCardDialog with the specified parent frame.
     * The dialog is modal and titled "Add New Card".
     *
     * @param parent The parent frame for this dialog.
     */
    public AddCardDialog(Frame parent) {
        super(parent, "Add New Card", true);
        initUI();
        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Initializes the user interface components and layout for the dialog.
     * Sets up the form fields, labels, panels, and buttons.
     */
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
    
    /**
     * Validates the input fields entered by the user.
     * Checks for card number format (16 digits), non-empty cardholder name,
     * expiry date format (MM/YY), and valid numeric balance.
     * Displays error messages using JOptionPane if validation fails.
     *
     * @return true if all input fields are valid, false otherwise.
     */
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
    
    /**
     * Checks if the dialog was confirmed by the user.
     * This method should be called after the dialog is closed.
     *
     * @return true if the user clicked the "Add Card" button after
     *         successful validation, false if they clicked "Cancel"
     *         or closed the dialog.
     */
    public boolean isConfirmed() {
        return confirmed;
    }
    
    /**
     * Retrieves the card number entered in the text field.
     * Should only be called if {@link #isConfirmed()} returns true.
     *
     * @return The entered card number string.
     */
    public String getCardNumber() {
        return cardNumberField.getText();
    }
    
    /**
     * Retrieves the cardholder name entered in the text field.
     * Should only be called if {@link #isConfirmed()} returns true.
     *
     * @return The entered cardholder name string.
     */
    public String getCardholderName() {
        return cardholderField.getText();
    }
    
    /**
     * Retrieves the expiry date entered in the text field.
     * Should only be called if {@link #isConfirmed()} returns true.
     *
     * @return The entered expiry date string (MM/YY format).
     */
    public String getExpiryDate() {
        return expiryDateField.getText();
    }
    
    /**
     * Retrieves the initial balance entered in the text field as a double.
     * Should only be called if {@link #isConfirmed()} returns true.
     *
     * @return The entered initial balance as a double.
     */
    public double getBalance() {
        return Double.parseDouble(balanceField.getText());
    }
}