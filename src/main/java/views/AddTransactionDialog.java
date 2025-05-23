package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A custom dialog window used for adding new transaction details.
 * It provides input fields for the transaction date, description, amount,
 * and type (Income/Expense). It includes basic input validation.
 */
public class AddTransactionDialog extends JDialog {
    
    /**
     * Text field for entering the transaction date.
     */
    private JTextField dateField;
    
    /**
     * Text field for entering the transaction description.
     */
    private JTextField descriptionField;
    
    /**
     * Text field for entering the transaction amount.
     */
    private JTextField amountField;
    
    /**
     * Combo box for selecting the transaction type (Income or Expense).
     */
    private JComboBox<String> typeComboBox;
    
    /**
     * Flag indicating whether the dialog was confirmed by the user
     * (i.e., the "Confirm" button was clicked after successful validation).
     */
    private boolean confirmed = false;

    /**
     * Constructs a new AddTransactionDialog with the specified owner frame.
     * The dialog is modal and titled "Add Transaction". It initializes the UI,
     * sets the size, location relative to the owner, and makes it non-resizable.
     *
     * @param owner The frame that is the owner of this dialog.
     */
    public AddTransactionDialog(Frame owner) {
        super(owner, "Add Transaction", true); // true makes the dialog modal
        setSize(400, 300);
        setLocationRelativeTo(owner); // Center the dialog relative to the owner frame
        setLayout(new BorderLayout(10, 10)); // Use BorderLayout with spacing
        setResizable(false); // Prevent resizing

        // 创建主面板
        JPanel mainPanel = new JPanel(new GridBagLayout()); // Panel using GridBagLayout for form fields
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding around the form
        GridBagConstraints gbc = new GridBagConstraints(); // Constraints for GridBagLayout
        gbc.fill = GridBagConstraints.HORIZONTAL; // Components fill their display area horizontally
        gbc.insets = new Insets(5, 5, 5, 5); // Padding between components

        // 日期输入
        gbc.gridx = 0; // Column 0
        gbc.gridy = 0; // Row 0
        mainPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1; // Column 1
        // Initialize date field with current date in dd/MM/yyyy format
        dateField = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        mainPanel.add(dateField, gbc);

        // 描述输入
        gbc.gridx = 0; // Column 0
        gbc.gridy = 1; // Row 1
        mainPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; // Column 1
        descriptionField = new JTextField();
        mainPanel.add(descriptionField, gbc);

        // 金额输入
        gbc.gridx = 0; // Column 0
        gbc.gridy = 2; // Row 2
        mainPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1; // Column 1
        amountField = new JTextField();
        mainPanel.add(amountField, gbc);

        // 交易类型选择
        gbc.gridx = 0; // Column 0
        gbc.gridy = 3; // Row 3
        mainPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1; // Column 1
        typeComboBox = new JComboBox<>(new String[]{"Income", "Expense"}); // Combo box for type selection
        mainPanel.add(typeComboBox, gbc);

        add(mainPanel, BorderLayout.CENTER); // Add the form panel to the center of the dialog

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Panel for buttons, right-aligned
        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");

        // Action listener for Confirm button
        confirmButton.addActionListener(e -> {
            if (validateInput()) { // Validate input before confirming
                confirmed = true; // Set confirmed flag to true
                dispose(); // Close the dialog
            }
        });

        // Action listener for Cancel button
        cancelButton.addActionListener(e -> dispose()); // Close the dialog

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH); // Add the button panel to the south of the dialog
    }

    /**
     * Validates the input fields entered by the user.
     * Checks that all fields are non-empty and that the amount is a valid number.
     * Displays error messages using JOptionPane if validation fails.
     *
     * @return true if all input fields are valid, false otherwise.
     */
    private boolean validateInput() {
        // Check for empty fields
        if (dateField.getText().trim().isEmpty() ||
            descriptionField.getText().trim().isEmpty() ||
            amountField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate amount as a number
        try {
            Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true; // All validations passed
    }

    /**
     * Checks if the dialog was confirmed by the user.
     * This method should be called after the dialog is closed.
     *
     * @return true if the user clicked the "Confirm" button after
     *         successful validation, false if they clicked "Cancel"
     *         or closed the dialog.
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Retrieves the transaction date entered in the text field.
     * Should only be called if {@link #isConfirmed()} returns true.
     *
     * @return The entered date string.
     */
    public String getDate() {
        return dateField.getText();
    }

    /**
     * Retrieves the transaction description entered in the text field.
     * Should only be called if {@link #isConfirmed()} returns true.
     *
     * @return The entered description string.
     */
    public String getDescription() {
        return descriptionField.getText();
    }

    /**
     * Retrieves the transaction amount entered in the text field as a double.
     * Should only be called if {@link #isConfirmed()} returns true.
     * Assumes {@link #validateInput()} has confirmed it's a valid number.
     *
     * @return The entered amount as a double.
     */
    public double getAmount() {
        return Double.parseDouble(amountField.getText());
    }

    /**
     * Retrieves the selected transaction type from the combo box.
     * Should only be called if {@link #isConfirmed()} returns true.
     *
     * @return The selected transaction type string ("Income" or "Expense").
     */
    public String getTransactionType() {
        return (String) typeComboBox.getSelectedItem();
    }
}