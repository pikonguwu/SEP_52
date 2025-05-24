package views;

import services.CurrencyConverterService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * A JPanel that provides a user interface for currency conversion settings.
 * Allows users to select source and target currencies, enter an amount,
 * and view the conversion result.
 */
public class CurrencySettingViewPanel extends JPanel {
    /**
     * Selector for the source currency.
     */
    private JComboBox<String> fromCurrencySelector;
    /**
     * Selector for the target currency.
     */
    private JComboBox<String> toCurrencySelector;
    /**
     * Text field for entering the amount to convert.
     */
    private JTextField amountInput;
    /**
     * Label to display the conversion result.
     */
    private JLabel resultLabel;
    /**
     * Image object for the background.
     */
    private Image backgroundImage;

    /**
     * Constructs a new CurrencySettingViewPanel, initializing the UI components
     * and setting up the layout.
     */
    public CurrencySettingViewPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Load background image
        try {
            backgroundImage = ImageIO.read(new File("C:\\Users\\马家悦\\Desktop\\study\\Java\\SEP_52\\src\\main\\java\\photo\\currencybackground.png")); // Load image using an absolute path
            System.out.println("背景图片加载成功！");  // 打印日志，确认图片是否加载成功
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("图片加载失败！请检查路径。");
        }

        // Title area
        JLabel title = new JLabel("Currency Converter");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // Central form area
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());  // Use GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        formPanel.setBackground(new Color(255, 255, 255, 0)); // Make background transparent

        String[] currencies = { "CNY", "USD", "EUR" };

        // From Currency
        JLabel fromLabel = new JLabel("From Currency:");
        fromLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;  // Set label left-aligned
        formPanel.add(fromLabel, gbc);

        fromCurrencySelector = new JComboBox<>(currencies);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Make combo box fill the space
        fromCurrencySelector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        formPanel.add(fromCurrencySelector, gbc);

        // Amount
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;  // Reset to no filling
        formPanel.add(amountLabel, gbc);

        amountInput = new JTextField("100");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Make text field fill the space
        amountInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        formPanel.add(amountInput, gbc);

        // To Currency
        JLabel toLabel = new JLabel("To Currency:");
        toLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(toLabel, gbc);

        toCurrencySelector = new JComboBox<>(currencies);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        toCurrencySelector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        formPanel.add(toCurrencySelector, gbc);

        // Convert Button: Set button width same as fields above and center align
        JButton convertButton = new JButton("Convert");
        convertButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        convertButton.setBackground(new Color(59, 130, 246));  // Keep the original blue color
        convertButton.setForeground(Color.WHITE);
        convertButton.setFocusPainted(false);
        convertButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        convertButton.setPreferredSize(new Dimension(300, 40));  // Make button width consistent with input fields
        convertButton.setMaximumSize(new Dimension(300, 40));  // Make button width consistent with input fields
        convertButton.addActionListener(new ConvertListener());

        // Result Label: Ensure width consistent with button and center align
        resultLabel = new JLabel("Result will appear here");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultLabel.setForeground(new Color(33, 33, 33));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        // Set background color to blue, consistent with button color
        resultLabel.setBackground(new Color(59, 130, 246)); // Same blue as the button
        resultLabel.setOpaque(true); // Set opaque to show background color
        resultLabel.setPreferredSize(new Dimension(300, 40));  // Set width same as the button
        resultLabel.setMaximumSize(new Dimension(300, 40));  // Set width same as the button

        // Add components to the form
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;  // Set button to span two columns
        formPanel.add(convertButton, gbc);

        // Assemble the page
        add(formPanel, BorderLayout.CENTER);
        add(resultLabel, BorderLayout.SOUTH);
    }

    /**
     * Overrides the default paintComponent method to draw a background image
     * before painting the components.
     *
     * @param g The Graphics context.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Draw the background image, ensuring it is at the bottom layer
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Action listener for the convert button.
     * Handles the currency conversion when the button is clicked.
     */
    private class ConvertListener implements ActionListener {
        /**
         * Performs the currency conversion when the button is clicked.
         * It reads the amount, source, and target currencies, performs the conversion
         * using {@link CurrencyConverterService}, and displays the result.
         * Handles {@link NumberFormatException} if the amount is invalid.
         *
         * @param e The action event.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                double amount = Double.parseDouble(amountInput.getText().trim());
                String from = (String) fromCurrencySelector.getSelectedItem();
                String to = (String) toCurrencySelector.getSelectedItem();
                double result = CurrencyConverterService.convert(amount, from, to);
                resultLabel.setText(CurrencyConverterService.format(result, to));
            } catch (NumberFormatException ex) {
                resultLabel.setText("❌ Invalid amount!");
            }
        }
    }
}