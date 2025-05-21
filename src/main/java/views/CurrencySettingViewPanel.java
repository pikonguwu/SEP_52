package views;

import services.CurrencyConverterService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CurrencySettingViewPanel extends JPanel {
    private JComboBox<String> fromCurrencySelector;
    private JComboBox<String> toCurrencySelector;
    private JTextField amountInput;
    private JLabel resultLabel;

    public CurrencySettingViewPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 标题区域
        JLabel title = new JLabel("Currency Converter");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // 中央表单区域
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        formPanel.setBackground(Color.WHITE);

        String[] currencies = { "CNY", "USD", "EUR", "HKD" };

        // From Currency
        JLabel fromLabel = new JLabel("From Currency:");
        fromLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fromCurrencySelector = new JComboBox<>(currencies);
        fromCurrencySelector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Amount
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        amountInput = new JTextField("100");
        amountInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // To Currency
        JLabel toLabel = new JLabel("To Currency:");
        toLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        toCurrencySelector = new JComboBox<>(currencies);
        toCurrencySelector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Convert Button
        JButton convertButton = new JButton("Convert");
        convertButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        convertButton.setBackground(new Color(59, 130, 246));
        convertButton.setForeground(Color.WHITE);
        convertButton.setFocusPainted(false);
        convertButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        convertButton.setPreferredSize(new Dimension(100, 40));
        convertButton.setMaximumSize(new Dimension(200, 40));
        convertButton.addActionListener(new ConvertListener());

        // Result Label
        resultLabel = new JLabel("Result will appear here");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultLabel.setForeground(new Color(33, 33, 33));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        // 添加组件到表单
        formPanel.add(fromLabel);
        formPanel.add(fromCurrencySelector);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(amountLabel);
        formPanel.add(amountInput);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(toLabel);
        formPanel.add(toCurrencySelector);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(convertButton);

        // 组装页面
        add(formPanel, BorderLayout.CENTER);
        add(resultLabel, BorderLayout.SOUTH);
    }

    private class ConvertListener implements ActionListener {
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
