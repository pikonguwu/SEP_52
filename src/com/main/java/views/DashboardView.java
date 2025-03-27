// src/views/DashboardView.java
package com.main.java.views;

import com.main.java.constants.AppConstants;
import com.main.java.components.*;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.data.general.DefaultPieDataset;



import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import java.awt.*;


public class DashboardView extends BaseView {
    @Override
    public String getViewName() {
        return "Dashboard";
    }

    @Override
    protected void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 顶部标题
        JLabel titleLabel = new JLabel("Overview", SwingConstants.LEFT);
        titleLabel.setFont(AppConstants.TITLE_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        // 主内容区
        RoundedPanel gridPanel = new RoundedPanel(new GridLayout(2, 2, 15, 15));
        gridPanel.add(wrapComponent(createCardPanel(), "My Cards"));
        gridPanel.add(wrapComponent(createTransactionTable(), "Recent Transactions"));
        gridPanel.add(wrapComponent(createWeeklyChart(), "Weekly Activity"));
        gridPanel.add(wrapComponent(createExpenseChart(), "Expense Statistics"));
        add(gridPanel, BorderLayout.CENTER);

        // 底部操作栏
        add(createActionPanel(), BorderLayout.SOUTH);
    }

    // private JPanel createCardPanel() {
    //     RoundedPanel panel = new RoundedPanel(new GridLayout(3, 1, 0, 10));
    //     panel.setBackground(Color.WHITE);
        
    //     String[] cardInfo = {
    //         "Balance: $5,756", 
    //         "Cardholder: Eddy Cushuma",
    //         "Valid Thru: 12/2024"
    //     };
        
    //     for (String info : cardInfo) {
    //         JLabel label = new JLabel(info, SwingConstants.CENTER);
    //         label.setFont(new Font("Arial", Font.PLAIN, 16));
    //         panel.add(label);
    //     }
    //     return panel;
    // }
    // private JPanel createCardPanel() {
    //     // 创建带圆角的卡片容器
    //     RoundedPanel panel = new RoundedPanel(new BorderLayout()) {
    //         @Override
    //         protected void paintComponent(Graphics g) {
    //             super.paintComponent(g);
    //             // 绘制渐变背景
    //             Graphics2D g2d = (Graphics2D) g;
    //             GradientPaint gradient = new GradientPaint(
    //                 0, 0, new Color(40, 50, 75), 
    //                 getWidth(), getHeight(), new Color(70, 130, 180)
    //             );
    //             g2d.setPaint(gradient);
    //             g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
    //         }
    //     };
    //     panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    //     panel.setPreferredSize(new Dimension(350, 200)); // 标准信用卡尺寸
    
    //     // 银行卡信息层
    //     RoundedPanel cardContent = new RoundedPanel(new GridBagLayout()) {
    //         @Override
    //         public boolean isOpaque() {
    //             return false; // 透明背景
    //         }
    //     };
    //     GridBagConstraints gbc = new GridBagConstraints();
    //     gbc.anchor = GridBagConstraints.WEST;
    //     gbc.insets = new Insets(5, 10, 5, 10);
    
    //     // 卡号信息
    //     JLabel cardNumberLabel = new JLabel("••••  ••••  ••••  3456");
    //     cardNumberLabel.setFont(new Font("Arial", Font.BOLD, 20));
    //     cardNumberLabel.setForeground(Color.WHITE);
    
    //     // 卡片信息组
    //     JPanel infoPanel = new JPanel(new GridLayout(0, 1, 0, 8));
    //     infoPanel.setOpaque(false);
    //     String[] cardInfo = {
    //         "BALANCE: $5,756", 
    //         "HOLDER: EDDY CUSHOMA",
    //         "VALID: 12/24"
    //     };
    //     for (String info : cardInfo) {
    //         JLabel label = new JLabel(info);
    //         label.setFont(new Font("Arial", Font.PLAIN, 14));
    //         label.setForeground(new Color(220, 220, 220));
    //         infoPanel.add(label);
    //     }
    
    //     // 布局组合
    //     gbc.gridy = 0;
    //     cardContent.add(cardNumberLabel, gbc);
        
    //     gbc.gridy = 1;
    //     gbc.anchor = GridBagConstraints.SOUTHWEST;
    //     cardContent.add(infoPanel, gbc);
    
    //     // 添加银行标志
    //     JLabel bankLogo = new JLabel("World Bank");
    //     bankLogo.setFont(new Font("Arial", Font.BOLD, 18));
    //     bankLogo.setForeground(Color.WHITE);
    //     panel.add(bankLogo, BorderLayout.NORTH);
        
    //     panel.add(cardContent, BorderLayout.CENTER);
    
    //     // 添加3D效果
    //     panel.setBorder(BorderFactory.createCompoundBorder(
    //         BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 2),
    //         BorderFactory.createEmptyBorder(5, 5, 5, 5)
    //     ));
    
    //     return panel;
    // }
    private JPanel createCardPanel() {
        // 创建卡片容器
        RoundedPanel panel = new RoundedPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 绘制纯蓝色背景
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(40, 80, 150)); // 深蓝色
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setPreferredSize(new Dimension(320, 200)); // 标准信用卡尺寸
    
        // 主内容容器（使用网格包布局实现精确布局）
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
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        balanceLabel.setForeground(new Color(180, 180, 220));
        
        JLabel amountLabel = new JLabel("$5,756");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 24));
        amountLabel.setForeground(Color.WHITE);
        
        balancePanel.add(balanceLabel, BorderLayout.NORTH);
        balancePanel.add(amountLabel, BorderLayout.CENTER);
    
        // 卡号区域（中间偏上）
        JPanel numberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        numberPanel.setOpaque(false);
        numberPanel.add(createCardSegment("3778", 22));
        numberPanel.add(createCardSegment("****", 18));
        numberPanel.add(createCardSegment("****", 18));
        numberPanel.add(createCardSegment("1234", 22));
    
        // 底部信息区域
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.setOpaque(false);
        
        // 左侧持卡人信息
        JPanel holderPanel = new JPanel(new BorderLayout());
        holderPanel.setOpaque(false);
        JLabel holderLabel = new JLabel("CARDHOLDER");
        holderLabel.setFont(new Font("Arial", Font.BOLD, 10));
        holderLabel.setForeground(new Color(180, 180, 220));
        
        JLabel nameLabel = new JLabel("Eddy Cusuma");
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
        
        JLabel dateLabel = new JLabel("12/22");
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
    
    // 创建卡号分段组件
    private JLabel createCardSegment(String text, int fontSize) {
        JLabel segment = new JLabel(text);
        segment.setFont(new Font("Arial", Font.BOLD, fontSize));
        segment.setForeground(Color.WHITE);
        segment.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        return segment;
    }

    // private JScrollPane createTransactionTable() {
    //     String[] columns = {"Date", "Description", "Amount"};
    //     Object[][] data = {
    //         {"2023-12-25", "Amazon Purchase", "-$189.00"},
    //         {"2023-12-24", "Salary Deposit", "+$5,200.00"},
    //         {"2023-12-23", "Utility Payment", "-$350.50"}
    //     };
        
    //     JTable table = new JTable(data, columns);
    //     table.setRowHeight(35);
    //     table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
    //     return new JScrollPane(table);
    // }
    // private JScrollPane createTransactionTable() {
    //     // 提取交易数据到外部变量或方法，便于管理
    //     String[] columns = {"Date", "Description", "Amount"};
    //     Object[][] data = getTransactionData();

    //     // 创建表格并设置基本属性
    //     JTable table = new JTable(data, columns) {
    //         @Override
    //         public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
    //             Component component = super.prepareRenderer(renderer, row, column);
    //             // 设置字体和对齐方式
    //             component.setFont(new Font("Arial", Font.PLAIN, 12));
    //             ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER); // 所有列居中，可根据需要调整
    //             return component;
    //         }
    //     };

    //     // 设置行高
    //     table.setRowHeight(35);

    //     // 设置表头字体和背景（可选）
    //     table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
    //     table.getTableHeader().setOpaque(false); // 透明背景，可根据需要设置颜色
    //     table.getTableHeader().setBackground(new Color(240, 240, 240)); // 浅灰色背景，可选
    //     table.getTableHeader().setForeground(new Color(50, 50, 50)); // 深灰色字体，可选

    //     // 根据内容调整列宽（这里简单设置为固定宽度，可根据需要动态调整）
    //     table.getColumnModel().getColumn(0).setPreferredWidth(100); // 日期列
    //     table.getColumnModel().getColumn(1).setPreferredWidth(200); // 描述列
    //     table.getColumnModel().getColumn(2).setPreferredWidth(100); // 金额列

    //     // 设置滚动条策略（根据需要调整）
    //     JScrollPane scrollPane = new JScrollPane(table);
    //     scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    //     scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    //     return scrollPane;
    // }

    // // 提取交易数据到单独的方法，便于管理和扩展
    // private Object[][] getTransactionData() {
    //     return new Object[][] {
    //         {"2023-12-25", "Amazon Purchase", "-$189.00"},
    //         {"2023-12-24", "Salary Deposit", "+$5,200.00"},
    //         {"2023-12-23", "Utility Payment", "-$350.50"}
    //         // 可以根据需要添加更多数据
    //     };
    // }
    private JScrollPane createTransactionTable() {
        String[] columns = {"", "Date", "Description", "Amount"};
        Object[][] data = {
            {new ImageIcon("path/to/card_icon.png"), "28 January 2021", "Deposit from my Card", "-$850"},
            {new ImageIcon("path/to/paypal_icon.png"), "25 January 2021", "Deposit Paypal", "+$2,500"},
            {new ImageIcon("path/to/user_icon.png"), "21 January 2021", "Jemi Wilson", "+$5,400"}
        };

        JTable table = new JTable(data, columns) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (column == 0) {
                    ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    ((JLabel) component).setHorizontalAlignment(SwingConstants.LEFT);
                }
                return component;
            }
        };

        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(100, 35)); // 设置表头高度

        // 隐藏第一列标题
        table.getColumnModel().getColumn(0).setHeaderValue("");

        // 设置各列宽度
        table.getColumnModel().getColumn(0).setPreferredWidth(40); // 图标列
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // 日期列
        table.getColumnModel().getColumn(2).setPreferredWidth(200); // 描述列
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // 金额列

        // 设置字体和颜色
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        table.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        // 设置滚动条策略
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        return scrollPane;
    }

    private ChartPanel createWeeklyChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        int[] amounts = {650, 820, 720, 930, 1050, 1350, 980};
        
        for (int i = 0; i < days.length; i++) {
            dataset.addValue(amounts[i], "Spending", days[i]);
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
            "", "", "Amount ($)", 
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        chart.setBackgroundPaint(Color.WHITE);
        return new ChartPanel(chart);
    }

    private ChartPanel createExpenseChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Housing", 35);
        dataset.setValue("Food", 25);
        dataset.setValue("Transport", 15);
        dataset.setValue("Entertainment", 10);
        dataset.setValue("Savings", 15);
        
        JFreeChart chart = ChartFactory.createPieChart("", dataset, true, true, false);
        chart.setBackgroundPaint(Color.WHITE);
        return new ChartPanel(chart);
    }

    private JPanel createActionPanel() {
        RoundedPanel panel = new RoundedPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Add transactions")); // 添加标题
        panel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Add transactions"), // 标题边框
        BorderFactory.createEmptyBorder(20, 0, 0, 0) // 内边距
        ));
        panel.add(createActionButton("Manual Add"));
        panel.add(createActionButton("Import File"));
        return panel;
    }

    // 创建一个带有圆角边框的按钮，并设置其文本、字体、背景颜色、前景颜色和边框
    private JButton createActionButton(String text) {
        RoundedButton btn = new RoundedButton(text);
        btn.setFont(AppConstants.BUTTON_FONT);
        btn.setBackground(AppConstants.PRIMARY_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        return btn;
    }

    private JPanel wrapComponent(Component comp, String title) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createTitledBorder(title));
        wrapper.setBackground(Color.WHITE);
        wrapper.add(comp, BorderLayout.CENTER);
        return wrapper;
    }
}