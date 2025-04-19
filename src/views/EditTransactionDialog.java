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
 * 编辑交易对话框，用于修改交易信息
 */
public class EditTransactionDialog extends JDialog {
    private JTextField dateField;
    private JTextField descriptionField;
    private JTextField amountField;
    private JComboBox<String> typeComboBox;
    private boolean confirmed = false;
    
    /**
     * 构造函数
     * 
     * @param parent 父窗口
     * @param date 当前日期
     * @param description 当前描述
     * @param amount 当前金额
     * @param type 当前交易类型
     */
    public EditTransactionDialog(Frame parent, String date, String description, String amount, String type) {
        super(parent, "编辑交易信息", true);
        
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
        formPanel.add(new JLabel("日期:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        dateField = new JTextField(date, 20);
        formPanel.add(dateField, gbc);
        
        // 描述字段
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("描述:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        descriptionField = new JTextField(description, 20);
        formPanel.add(descriptionField, gbc);
        
        // 金额字段
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("金额:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        amountField = new JTextField(amount, 20);
        formPanel.add(amountField, gbc);
        
        // 类型下拉框
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("类型:"), gbc);
        
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
        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
        
        // 确认按钮
        JButton confirmButton = new JButton("确认");
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
     * 验证输入数据
     * 
     * @return 如果输入有效则返回true，否则返回false
     */
    private boolean validateInput() {
        // 验证日期不为空
        if (dateField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "日期不能为空",
                "输入错误",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 验证描述不为空
        if (descriptionField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "描述不能为空",
                "输入错误",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 验证金额为数字
        try {
            Double.parseDouble(amountField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "金额必须是有效的数字",
                "输入错误",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    /**
     * 获取日期
     * 
     * @return 日期字符串
     */
    public String getDate() {
        return dateField.getText();
    }
    
    /**
     * 获取描述
     * 
     * @return 描述字符串
     */
    public String getDescription() {
        return descriptionField.getText();
    }
    
    /**
     * 获取金额
     * 
     * @return 金额字符串
     */
    public String getAmount() {
        return amountField.getText();
    }
    
    /**
     * 获取交易类型
     * 
     * @return 交易类型字符串
     */
    public String getTransactionType() {
        return (String) typeComboBox.getSelectedItem();
    }
    
    /**
     * 检查是否确认了修改
     * 
     * @return 如果确认了修改则返回true，否则返回false
     */
    public boolean isConfirmed() {
        return confirmed;
    }
} 