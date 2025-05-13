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
        setLayout(new GridLayout(5, 1, 10, 10));

        String[] currencies = { "CNY", "USD", "EUR" };

        // 原货币选择
        fromCurrencySelector = new JComboBox<>(currencies);
        add(new JLabel("From Currency:"));
        add(fromCurrencySelector);

        // 金额输入
        amountInput = new JTextField("100");
        add(new JLabel("Amount:"));
        add(amountInput);

        // 目标货币选择
        toCurrencySelector = new JComboBox<>(currencies);
        add(new JLabel("To Currency:"));
        add(toCurrencySelector);

        // 转换按钮
        JButton convertButton = new JButton("Convert");
        convertButton.addActionListener(new ConvertListener());
        add(convertButton);

        // 结果显示
        resultLabel = new JLabel("Result will appear here");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(resultLabel);
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
                resultLabel.setText("Invalid amount!");
            }
        }
    }
}
