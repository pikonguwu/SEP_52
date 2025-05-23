package views;

import services.CurrencyConverterService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class CurrencySettingViewPanel extends JPanel {
    private JComboBox<String> fromCurrencySelector;
    private JComboBox<String> toCurrencySelector;
    private JTextField amountInput;
    private JLabel resultLabel;
    private Image backgroundImage;  // 用于存储背景图

    public CurrencySettingViewPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 加载背景图片
        try {
            backgroundImage = ImageIO.read(new File("C:\\Users\\马家悦\\Desktop\\study\\Java\\SEP_52\\src\\main\\java\\photo\\currencybackground.png")); // 使用绝对路径加载图片
            System.out.println("背景图片加载成功！");  // 打印日志，确认图片是否加载成功
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("图片加载失败！请检查路径。");
        }

        // 标题区域
        JLabel title = new JLabel("Currency Converter");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // 中央表单区域
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());  // 使用 GridBagLayout 布局
        GridBagConstraints gbc = new GridBagConstraints();
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        formPanel.setBackground(new Color(255, 255, 255, 0)); // 使背景透明

        String[] currencies = { "CNY", "USD", "EUR" };

        // From Currency
        JLabel fromLabel = new JLabel("From Currency:");
        fromLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;  // 设置标签左对齐
        formPanel.add(fromLabel, gbc);

        fromCurrencySelector = new JComboBox<>(currencies);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // 使组合框填满
        fromCurrencySelector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        formPanel.add(fromCurrencySelector, gbc);

        // Amount
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;  // 重置为不填充
        formPanel.add(amountLabel, gbc);

        amountInput = new JTextField("100");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // 使输入框填满
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

        // Convert Button: 设置按钮宽度与上面的框相同并居中
        JButton convertButton = new JButton("Convert");
        convertButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        convertButton.setBackground(new Color(59, 130, 246));  // 保持原来的蓝色
        convertButton.setForeground(Color.WHITE);
        convertButton.setFocusPainted(false);
        convertButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        convertButton.setPreferredSize(new Dimension(300, 40));  // 使按钮宽度与输入框一致
        convertButton.setMaximumSize(new Dimension(300, 40));  // 使按钮宽度与输入框一致
        convertButton.addActionListener(new ConvertListener());

        // Result Label: 确保与按钮一致的宽度并居中
        resultLabel = new JLabel("Result will appear here");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultLabel.setForeground(new Color(33, 33, 33));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        // 设置背景颜色为蓝色，和按钮颜色一致
        resultLabel.setBackground(new Color(59, 130, 246)); // 与按钮相同的蓝色
        resultLabel.setOpaque(true); // 设置为不透明，以显示背景色
        resultLabel.setPreferredSize(new Dimension(300, 40));  // 设置与按钮相同的宽度
        resultLabel.setMaximumSize(new Dimension(300, 40));  // 设置与按钮相同的宽度

        // 添加组件到表单
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;  // 设置按钮跨越两列
        formPanel.add(convertButton, gbc);

        // 组装页面
        add(formPanel, BorderLayout.CENTER);
        add(resultLabel, BorderLayout.SOUTH);
    }

    // 重写 paintComponent 方法来绘制背景图片
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // 绘制背景图片，确保背景图在最底层
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
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
