package views;

import constants.AppConstants;
import components.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.w3c.dom.events.MouseEvent; // 此导入未使用，后续可考虑移除
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import javax.swing.*; // 重复导入，可移除
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*; // 重复导入，可移除

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
/**
 * TransactionsView 类继承自 BaseView，用于展示交易视图界面。
 * 该界面包含标题、卡片面板、费用图表和交易表格，并且支持分页查看交易记录。
 */
public class TransactionsView extends BaseView {
    /**
     * 获取视图的名称，用于标识该视图。
     *
     * @return 视图名称 "Transactions"
     */
    @Override
    public String getViewName() {
        return "Transactions";
    }

    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JTable incomeTable;
    private JTable expenseTable;
    private DefaultTableModel incomeTableModel;
    private DefaultTableModel expenseTableModel;

    // 添加卡片列表和卡片面板
    private java.util.List<CardInfo> cards;
    private JPanel cardsContainer;
    private JScrollPane cardsScrollPane;
    
    // 添加卡片信息类
    private static class CardInfo {
        Color backgroundColor;
        String balance;
        String cardholder;
        String validThru;
        String cardNumber1;
        String cardNumber2;
        String cardNumber3;
        String cardNumber4;
        
        public CardInfo(Color backgroundColor, String balance, String cardholder, String validThru,
                       String cardNumber1, String cardNumber2, String cardNumber3, String cardNumber4) {
            this.backgroundColor = backgroundColor;
            this.balance = balance;
            this.cardholder = cardholder;
            this.validThru = validThru;
            this.cardNumber1 = cardNumber1;
            this.cardNumber2 = cardNumber2;
            this.cardNumber3 = cardNumber3;
            this.cardNumber4 = cardNumber4;
        }
    }

    /**
     * 初始化用户界面，设置布局、添加标题、卡片面板、费用图表和交易表格。
     */
    @Override
    public void initUI() {
        // 初始化卡片列表
        cards = new java.util.ArrayList<>();
        
        // 设置布局管理器，组件间水平和垂直间距为 15 像素
        setLayout(new BorderLayout(15, 15));
        // 设置面板的内边距，上、左、下、右均为 20 像素
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 创建顶部标题标签
        JLabel titleLabel = new JLabel("Transactions", SwingConstants.LEFT);
        // 设置标题字体为全局定义的标题字体
        titleLabel.setFont(AppConstants.TITLE_FONT);
        // 设置标题标签的底部内边距为 15 像素
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        // 将标题标签添加到面板的北部位置
        add(titleLabel, BorderLayout.NORTH);

        // 初始化表格模型 - 移到这里，确保在创建图表前初始化
        initializeTableModels();

        // 创建主内容面板，使用 1 行 2 列的网格布局，组件间水平和垂直间距为 15 像素
        RoundedPanel gridPanel = new RoundedPanel(new GridLayout(1, 2, 15, 15));
        // 添加卡片面板并包装上标题
        gridPanel.add(wrapComponent(createCardPanel(), "My Cards"));
        // 添加费用图表并包装上标题
        gridPanel.add(wrapComponent(createExpenseChart(), "My Expenses"));
        // 将主内容面板添加到面板的中心位置
        add(gridPanel, BorderLayout.CENTER);

        // 将交易表格面板添加到面板的南部位置
        add(createTransactionPanel(), BorderLayout.SOUTH);
    }

    /**
     * 初始化所有表格模型
     */
    private void initializeTableModels() {
        // 定义表格的列名
        String[] columnNames = {"Description", "Transaction ID", "Type", "Card", "Date", "Amount"};
        
        // 创建主表格模型
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        // 创建收入表格模型
        incomeTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        // 创建支出表格模型
        expenseTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        // 创建表格
        transactionTable = new JTable(tableModel);
        incomeTable = new JTable(incomeTableModel);
        expenseTable = new JTable(expenseTableModel);

        // 设置表格样式
        transactionTable.setFont(new Font("Arial", Font.PLAIN, 14));
        transactionTable.setRowHeight(25);
        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        incomeTable.setFont(new Font("Arial", Font.PLAIN, 14));
        incomeTable.setRowHeight(25);
        incomeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        expenseTable.setFont(new Font("Arial", Font.PLAIN, 14));
        expenseTable.setRowHeight(25);
        expenseTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        Object[][] data = {
            {"Spotify Subscription", "123456", "Shopping", "1234******", "28 Mar, 12.30 AM", "-$2,500"},
            {"Freepik Sales", "789012", "Transfer", "5678******", "25 Jan, 10.40 PM", "+$750"},
            {"Mobile Service", "345678", "Service", "9012******", "20 Jan, 10.40 PM", "-$150"},
            {"Wilson", "901234", "Transfer", "3456******", "15 Jan, 03.29 PM", "-$1,050"},
            {"Emily", "567890", "Transfer", "7890******", "14 Jan, 10.40 PM", "+$840"},
            {"Amy", "567891", "Transfer", "7890******", "14 Feb, 10.40 PM", "-$4378"}
        };
        
        // 将初始数据添加到相应的表格中
        for (Object[] row : data) {
            // 添加到主表格
            tableModel.addRow(row);
            
            // 根据金额判断类型并添加到相应表格
            String amount = (String) row[5]; // 金额在第6列
            if (amount.startsWith("-")) {
                expenseTableModel.addRow(row);
            } else {
                incomeTableModel.addRow(row);
            }
        }
    }

    /**
     * 创建交易表格面板，包含标题、选项卡和分页控件。
     *
     * @return 包含交易表格和分页控件的面板
     */
    private JPanel createTransactionPanel() {
        // 创建主面板，使用边界布局
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 顶部：标题
        JLabel titleLabel = new JLabel("Recent Transactions", SwingConstants.CENTER);
        // 设置标题字体为 Arial 加粗，字号 18
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        // 设置标题标签的上下内边距为 10 像素
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        // 将标题标签添加到主面板的北部位置
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 中部：选项卡和交易表格
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // 添加"所有交易"选项卡
        tabbedPane.addTab("All Transactions", new JScrollPane(transactionTable));
        
        // 添加"收入"选项卡
        tabbedPane.addTab("Income", new JScrollPane(incomeTable));
        
        // 添加"支出"选项卡
        tabbedPane.addTab("Expense", new JScrollPane(expenseTable));
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // 底部：分页控件
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prevButton = new JButton("<Previous");
        JButton nextButton = new JButton("Next>");
        JLabel pageLabel = new JLabel("1");
        // 将上一页按钮添加到分页面板
        paginationPanel.add(prevButton);
        // 将页码标签添加到分页面板
        paginationPanel.add(pageLabel);
        // 将下一页按钮添加到分页面板
        paginationPanel.add(nextButton);
        // 将分页面板添加到主面板的南部位置
        mainPanel.add(paginationPanel, BorderLayout.SOUTH);

        // 分页按钮事件
        prevButton.addActionListener(new ActionListener() {
            /**
             * 上一页按钮点击事件处理方法，显示提示信息。
             *
             * @param e 动作事件对象
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // 上一页逻辑
                JOptionPane.showMessageDialog(null, "上一页");
            }
        });
        nextButton.addActionListener(new ActionListener() {
            /**
             * 下一页按钮点击事件处理方法，显示提示信息。
             *
             * @param e 动作事件对象
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // 下一页逻辑
                JOptionPane.showMessageDialog(null, "下一页");
            }
        });

        // 设置主面板的首选大小为 800x350 像素
        mainPanel.setPreferredSize(new Dimension(800, 350));

        return mainPanel;
    }

    /**
     * 创建卡片面板，包含卡片列表和添加卡片按钮。
     * 
     * @return 包含卡片列表和添加卡片按钮的面板
     */
    private JPanel createCardPanel() {
        // 创建主面板，使用边界布局
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        
        // 创建顶部面板，包含标题和添加卡片按钮
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        // 创建添加卡片按钮
        JButton addCardButton = new JButton("+ Add Card");
        addCardButton.setFont(new Font("Arial", Font.BOLD, 12));
        addCardButton.setBackground(AppConstants.PRIMARY_COLOR);
        addCardButton.setForeground(Color.WHITE);
        addCardButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        addCardButton.setFocusPainted(false);
        
        // 添加卡片按钮点击事件
        addCardButton.addActionListener(e -> showAddCardDialog());
        
        // 将按钮添加到顶部面板的右侧
        topPanel.add(addCardButton, BorderLayout.EAST);
        
        // 创建卡片容器面板，使用水平流式布局
        cardsContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        cardsContainer.setOpaque(false);
        
        // 创建滚动面板，设置水平滚动条
        cardsScrollPane = new JScrollPane(cardsContainer);
        cardsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        cardsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        cardsScrollPane.setBorder(null);
        cardsScrollPane.setOpaque(false);
        
        // 添加初始卡片
        addInitialCards();
        
        // 组装面板
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(cardsScrollPane, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    /**
     * 添加初始卡片
     */
    private void addInitialCards() {
        // 添加默认卡片
        cards.add(new CardInfo(
            new Color(40, 80, 150), 
            "$5,756", 
            "Eddy Cusuma", 
            "12/22", 
            "3778", "****", "****", "1234"
        ));
        
        // 刷新卡片显示
        refreshCardsDisplay();
    }
    
    /**
     * 刷新卡片显示
     */
    private void refreshCardsDisplay() {
        // 清空卡片容器
        cardsContainer.removeAll();
        
        // 添加所有卡片
        for (CardInfo card : cards) {
            cardsContainer.add(createCard(
                card.backgroundColor,
                card.balance,
                card.cardholder,
                card.validThru,
                card.cardNumber1,
                card.cardNumber2,
                card.cardNumber3,
                card.cardNumber4
            ));
        }
        
        // 重新验证和重绘
        cardsContainer.revalidate();
        cardsContainer.repaint();
    }
    
    /**
     * 显示添加卡片对话框
     */
    private void showAddCardDialog() {
        // 创建对话框
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Card", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        // 创建表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 添加表单字段
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Cardholder Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Card Number:"), gbc);
        gbc.gridx = 1;
        JTextField cardNumberField = new JTextField(20);
        formPanel.add(cardNumberField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Valid Thru (MM/YY):"), gbc);
        gbc.gridx = 1;
        JTextField validThruField = new JTextField(20);
        formPanel.add(validThruField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Balance:"), gbc);
        gbc.gridx = 1;
        JTextField balanceField = new JTextField(20);
        formPanel.add(balanceField, gbc);
        
        // 添加按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancel");
        JButton saveButton = new JButton("Save");
        
        cancelButton.addActionListener(e -> dialog.dispose());
        saveButton.addActionListener(e -> {
            // 获取表单数据
            String cardholder = nameField.getText().trim();
            String cardNumber = cardNumberField.getText().trim();
            String validThru = validThruField.getText().trim();
            String balance = balanceField.getText().trim();
            
            // 验证数据
            if (cardholder.isEmpty() || cardNumber.isEmpty() || validThru.isEmpty() || balance.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "Please fill in all fields", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 验证卡号格式
            if (cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
                JOptionPane.showMessageDialog(dialog, 
                    "Card number must be 16 digits", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 验证有效期格式
            if (!validThru.matches("\\d{2}/\\d{2}")) {
                JOptionPane.showMessageDialog(dialog, 
                    "Valid thru must be in MM/YY format", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 验证余额格式
            if (!balance.matches("\\$?\\d+(,\\d{3})*(\\.\\d{2})?")) {
                JOptionPane.showMessageDialog(dialog, 
                    "Balance must be a valid currency amount", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 格式化余额
            if (!balance.startsWith("$")) {
                balance = "$" + balance;
            }
            
            // 生成随机颜色
            Color[] colors = {
                new Color(40, 80, 150),  // 蓝色
                new Color(80, 40, 150),  // 紫色
                new Color(150, 40, 80),  // 红色
                new Color(40, 150, 80),  // 绿色
                new Color(150, 80, 40)   // 橙色
            };
            Color backgroundColor = colors[cards.size() % colors.length];
            
            // 分割卡号
            String cardNumber1 = cardNumber.substring(0, 4);
            String cardNumber2 = "****";
            String cardNumber3 = "****";
            String cardNumber4 = cardNumber.substring(12, 16);
            
            // 创建新卡片
            CardInfo newCard = new CardInfo(
                backgroundColor,
                balance,
                cardholder,
                validThru,
                cardNumber1,
                cardNumber2,
                cardNumber3,
                cardNumber4
            );
            
            // 添加到卡片列表
            cards.add(newCard);
            
            // 刷新卡片显示
            refreshCardsDisplay();
            
            // 关闭对话框
            dialog.dispose();
            
            // 显示成功消息
            JOptionPane.showMessageDialog(this, 
                "Card added successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        // 组装对话框
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // 显示对话框
        dialog.setVisible(true);
    }
    
    /**
     * 创建单个银行卡界面。
     * 
     * @param backgroundColor 卡片背景颜色
     * @param balance 余额
     * @param cardholder 持卡人姓名
     * @param validThru 有效期
     * @param cardNumber1 卡号第一段
     * @param cardNumber2 卡号第二段
     * @param cardNumber3 卡号第三段
     * @param cardNumber4 卡号第四段
     * @return 包含银行卡信息的面板
     */
    private JPanel createCard(Color backgroundColor, String balance, String cardholder, String validThru,
                            String cardNumber1, String cardNumber2, String cardNumber3, String cardNumber4) {
        // 创建带圆角的卡片容器
        RoundedPanel panel = new RoundedPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(backgroundColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setPreferredSize(new Dimension(320, 180));

        // 主内容容器
        JPanel content = new JPanel(new GridBagLayout()) {
            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // 余额区域（左上）
        JPanel balancePanel = new JPanel(new BorderLayout());
        balancePanel.setOpaque(false);
        JLabel balanceLabel = new JLabel("BALANCE");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 10));
        balanceLabel.setForeground(new Color(180, 180, 220));
        
        JLabel amountLabel = new JLabel(balance);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 18));
        amountLabel.setForeground(Color.WHITE);
        
        balancePanel.add(balanceLabel, BorderLayout.NORTH);
        balancePanel.add(amountLabel, BorderLayout.CENTER);

        // 卡号区域（中间偏上）
        JPanel numberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        numberPanel.setOpaque(false);
        numberPanel.add(createCardSegment(cardNumber1, 22));
        numberPanel.add(createCardSegment(cardNumber2, 18));
        numberPanel.add(createCardSegment(cardNumber3, 18));
        numberPanel.add(createCardSegment(cardNumber4, 22));

        // 底部信息区域
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.setOpaque(false);
        
        // 左侧持卡人信息
        JPanel holderPanel = new JPanel(new BorderLayout());
        holderPanel.setOpaque(false);
        JLabel holderLabel = new JLabel("CARDHOLDER");
        holderLabel.setFont(new Font("Arial", Font.BOLD, 10));
        holderLabel.setForeground(new Color(180, 180, 220));
        
        JLabel nameLabel = new JLabel(cardholder);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(Color.WHITE);
        
        holderPanel.add(holderLabel, BorderLayout.NORTH);
        holderPanel.add(nameLabel, BorderLayout.CENTER);

        // 右侧有效期信息
        JPanel validPanel = new JPanel(new BorderLayout());
        validPanel.setOpaque(false);
        JLabel validLabel = new JLabel("VALID THRU");
        validLabel.setFont(new Font("Arial", Font.BOLD, 10));
        validLabel.setForeground(new Color(180, 180, 220));
        
        JLabel dateLabel = new JLabel(validThru);
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dateLabel.setForeground(Color.WHITE);
        
        validPanel.add(validLabel, BorderLayout.NORTH);
        validPanel.add(dateLabel, BorderLayout.CENTER);

        // 布局组合
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        content.add(balancePanel, gbc);

        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        content.add(numberPanel, gbc);

        bottomPanel.add(holderPanel);
        bottomPanel.add(validPanel);

        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(bottomPanel, gbc);

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    /**
     * 创建卡号分段组件，用于显示银行卡号的分段信息。
     *
     * @param text     卡号分段的文本内容
     * @param fontSize 卡号分段文本的字体大小
     * @return 包含卡号分段信息的 JLabel 组件
     */
    private JLabel createCardSegment(String text, int fontSize) {
        JLabel segment = new JLabel(text);
        // 设置卡号分段标签字体为 Arial 加粗，指定字号
        segment.setFont(new Font("Arial", Font.BOLD, fontSize));
        // 设置卡号分段标签字体颜色为白色
        segment.setForeground(Color.WHITE);
        // 设置卡号分段标签的左右内边距为 5 像素
        segment.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        return segment;
    }

    /**
     * 创建费用图表，显示每月的费用支出情况。
     *
     * @return 包含 3D 柱状图的图表面板
     */
    private ChartPanel createExpenseChart() {
        // 创建默认的分类数据集
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // 从支出表格中获取数据并按月份统计
        java.util.Map<String, Double> monthlyExpenses = new java.util.HashMap<>();
        
        // 检查expenseTableModel是否已初始化
        if (expenseTableModel != null) {
            // 遍历支出表格中的所有行
            for (int i = 0; i < expenseTableModel.getRowCount(); i++) {
                // 获取日期和金额
                String dateStr = (String) expenseTableModel.getValueAt(i, 4); // 日期在第5列
                String amountStr = (String) expenseTableModel.getValueAt(i, 5); // 金额在第6列
                
                // 从日期中提取月份 (格式: "28 Jan, 12.30 AM")
                String month = dateStr.split(" ")[1]; // 提取月份名称
                
                // 处理金额字符串，移除负号和美元符号，并转换为数值
                double amount = 0.0;
                try {
                    // 移除负号和美元符号，并处理千位分隔符
                    String cleanAmount = amountStr.replace("$", "").replace(",", "");
                    if (cleanAmount.startsWith("-")) {
                        cleanAmount = cleanAmount.substring(1);
                    }
                    amount = Double.parseDouble(cleanAmount);
                } catch (NumberFormatException e) {
                    // 如果解析失败，记录错误并继续
                    System.err.println("无法解析金额: " + amountStr);
                }
                
                // 累加该月份的支出
                monthlyExpenses.put(month, monthlyExpenses.getOrDefault(month, 0.0) + amount);
            }
            
            // 将统计结果添加到数据集
            for (java.util.Map.Entry<String, Double> entry : monthlyExpenses.entrySet()) {
                dataset.addValue(entry.getValue(), "Expenses", entry.getKey());
            }
        } else {
            // 如果expenseTableModel为空，添加一些默认数据
            dataset.addValue(0, "Expenses", "Jan");
            dataset.addValue(0, "Expenses", "Feb");
            dataset.addValue(0, "Expenses", "Mar");
            dataset.addValue(0, "Expenses", "Apr");
            dataset.addValue(0, "Expenses", "May");
        }

        // 创建 3D 柱状图
        JFreeChart chart = ChartFactory.createBarChart3D(
                "My Expenses", // 图表标题
                "Month",       // 分类轴标签
                "Amount",      // 数值轴标签
                dataset,       // 数据集
                PlotOrientation.VERTICAL, // 图表方向
                true,          // 是否显示图例
                false,         // 是否生成工具提示
                false          // 是否生成网址链接
        );
        
        // 获取柱状图对象
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        
        // 设置柱状图渲染器，添加数值标签
        BarRenderer3D renderer = new BarRenderer3D();
        
        // 使用自定义标签生成器，只显示纯数字
        renderer.setBaseItemLabelGenerator(new CategoryItemLabelGenerator() {
            @Override
            public String generateLabel(CategoryDataset dataset, int row, int column) {
                Number value = dataset.getValue(row, column);
                // 使用整数格式，不显示小数部分
                return String.format("%.2f", value.doubleValue());
            }
            
            @Override
            public String generateRowLabel(CategoryDataset dataset, int row) {
                return dataset.getRowKey(row).toString();
            }
            
            @Override
            public String generateColumnLabel(CategoryDataset dataset, int column) {
                return dataset.getColumnKey(column).toString();
            }
        });
        
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, org.jfree.ui.TextAnchor.BOTTOM_CENTER));
        
        // 应用渲染器
        plot.setRenderer(renderer);

        // 返回包含图表的图表面板
        return new ChartPanel(chart);
    }

    /**
     * 包装组件，为组件添加标题边框并设置背景颜色。
     *
     * @param comp  需要包装的组件
     * @param title 包装组件的标题
     * @return 包装后的面板
     */
    private JPanel wrapComponent(Component comp, String title) {
        // 创建使用边界布局的面板
        JPanel wrapper = new JPanel(new BorderLayout());
        // 为面板设置标题边框
        wrapper.setBorder(BorderFactory.createTitledBorder(title));
        // 设置面板的背景颜色为白色
        wrapper.setBackground(Color.WHITE);
        // 将组件添加到面板的中心位置
        wrapper.add(comp, BorderLayout.CENTER);
        return wrapper;
    }


    public void addTransaction(String date, String description, String amount, String type) {
        // 生成随机交易ID
        String transactionId = String.format("%06d", (int)(Math.random() * 1000000));
        // 生成随机卡号
        String cardNumber = String.format("%04d******", (int)(Math.random() * 10000));

        // 根据金额判断交易类型
        String actualType = amount.startsWith("-") ? "Expense" : "Income";

        // 添加新行到表格
        Object[] rowData = {
            description,
            transactionId,
            type,
            cardNumber,
            date,
            amount
        };

        tableModel.addRow(rowData);

        // 根据实际类型添加到相应的表格
        if (actualType.equals("Income")) {
            incomeTableModel.addRow(rowData);
        } else {
            expenseTableModel.addRow(rowData);
        }
    }
    
    /**
     * 删除交易记录
     * 
     * @param date 交易日期
     * @param description 交易描述
     * @param amount 交易金额
     * @param type 交易类型
     */
    public void removeTransaction(String date, String description, String amount, String type) {
        // 在主表格中查找并删除匹配的行
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            if (tableModel.getValueAt(i, 0).equals(description) && 
                tableModel.getValueAt(i, 4).equals(date) && 
                tableModel.getValueAt(i, 5).equals(amount)) {
                tableModel.removeRow(i);
                break;
            }
        }
        
        // 根据交易类型在相应的表格中查找并删除匹配的行
        String actualType = amount.startsWith("-") ? "Expense" : "Income";
        DefaultTableModel targetModel = actualType.equals("Income") ? incomeTableModel : expenseTableModel;
        
        for (int i = targetModel.getRowCount() - 1; i >= 0; i--) {
            if (targetModel.getValueAt(i, 0).equals(description) && 
                targetModel.getValueAt(i, 4).equals(date) && 
                targetModel.getValueAt(i, 5).equals(amount)) {
                targetModel.removeRow(i);
                break;
            }
        }
    }
}