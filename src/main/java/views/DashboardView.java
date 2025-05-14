package views;

import constants.AppConstants;
import components.*;
import services.BaiduAIService;
import services.TransactionDataService;
import com.google.gson.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import ui.AddCardDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 定义 DashboardView 类，继承自 BaseView，用于展示仪表盘视图。
 * 该视图包含顶部标题、主内容区（包含卡片面板、交易表格、周活动图表和支出统计图表）以及底部操作栏。
 */
public class DashboardView extends BaseView {

    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private BaiduAIService aiService = new BaiduAIService();
    private TransactionsView transactionsView;
    private AccountsView accountsView;
    
    // 添加数据服务和图表相关字段
    private TransactionDataService dataService;
    private JFreeChart weeklyChart;
    private JFreeChart expenseChart;
    private ChartPanel weeklyChartPanel;
    private ChartPanel expenseChartPanel;
    private Random random = new Random();
    /**
     * 构造函数
     */
    public DashboardView() {
        // 初始化数据服务 - 确保在构造函数中就初始化
        dataService = new TransactionDataService();
        // 注意：父类的构造函数会调用initUI()，所以在此之前必须初始化dataService
    }

    /**
     * 重写 getViewName 方法，返回该视图的名称。
     * 
     * @return 视图名称 "Dashboard"
     */
    @Override
    public String getViewName() {
        return "Dashboard";
    }

    /**
     * 重写 initUI 方法，初始化用户界面。
     * 设置布局、添加标题、主内容区和底部操作栏。
     */
    @Override
    protected void initUI() {
        // 设置布局为 BorderLayout，并设置组件间的水平和垂直间距为 15 像素
        setLayout(new BorderLayout(15, 15));
        // 设置面板的内边距，上、左、下、右均为 20 像素
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 创建顶部标题标签
        JLabel titleLabel = new JLabel("Overview", SwingConstants.LEFT);
        // 设置标题字体为全局定义的标题字体
        titleLabel.setFont(AppConstants.TITLE_FONT); 
        // 设置标题标签的底部内边距为 15 像素
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); 
        // 将标题标签添加到面板的北部位置
        add(titleLabel, BorderLayout.NORTH); 

        // 创建主内容区，使用 2x2 的网格布局，组件间的水平和垂直间距为 15 像素
        RoundedPanel gridPanel = new RoundedPanel(new GridLayout(2, 2, 15, 15));
        // 添加卡片面板并包装上标题
        gridPanel.add(wrapComponent(createCardPanel(), "My Cards")); 
        // 添加交易表格并包装上标题
        gridPanel.add(wrapComponent(createTransactionTable(), "Recent Transactions")); 
        // 添加周活动图表并包装上标题
        gridPanel.add(wrapComponent(createWeeklyChart(), "Weekly Activity")); 
        // 添加支出统计图表并包装上标题
        gridPanel.add(wrapComponent(createExpenseChart(), "Expense Statistics")); 
        // 将主内容区添加到面板的中心位置
        add(gridPanel, BorderLayout.CENTER); 

        // 创建底部操作栏并添加到面板的南部位置
        add(createActionPanel(), BorderLayout.SOUTH); 
        
        // 在所有UI组件创建完毕后，加载初始交易数据
        // 确保dataService已初始化
        if (dataService != null) {
            loadInitialTransactions();
        } else {
            System.err.println("Warning: dataService is null, cannot load initial transactions");
            // 在这里重新初始化为安全措施
            dataService = new TransactionDataService();
            loadInitialTransactions();
        }
    }

    /**
     * 创建卡片面板，模拟银行卡界面。
     * 
     * @return 包含银行卡信息的面板
     */
    private JPanel createCardPanel() {
        // 创建卡片容器面板，使用水平滚动布局
        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.X_AXIS));
        cardsContainer.setOpaque(false);
        
        // 创建滚动面板
        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        // 创建主面板
        RoundedPanel mainPanel = new RoundedPanel(new BorderLayout(0, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // 添加默认卡片
        RoundedPanel defaultCard = createCard("3778****1234", "Eddy Cusuma", "12/22", 5756.00);
        
        // 创建添加按钮
        RoundedButton addCardButton = new RoundedButton("+ Add Card");
        addCardButton.setFont(new Font("Arial", Font.BOLD, 12));
        addCardButton.setBackground(new Color(255, 255, 255, 50));
        addCardButton.setForeground(Color.WHITE);
        addCardButton.setBorderPainted(false);
        addCardButton.setFocusPainted(false);
        addCardButton.setContentAreaFilled(false);
        addCardButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 创建按钮容器面板，用于定位按钮
        JPanel buttonContainer = new JPanel(null); // 使用绝对布局
        buttonContainer.setOpaque(false);
        buttonContainer.setPreferredSize(defaultCard.getPreferredSize());
        addCardButton.setBounds(
            defaultCard.getPreferredSize().width - 100, // 右边距
            10, // 上边距
            100, // 按钮宽度
            30  // 按钮高度
        );
        buttonContainer.add(addCardButton);
        
        // 创建卡片和按钮的叠加面板
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(defaultCard.getPreferredSize());
        layeredPane.add(defaultCard, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(buttonContainer, JLayeredPane.PALETTE_LAYER);
        
        // 设置卡片和按钮容器的位置
        defaultCard.setBounds(0, 0, defaultCard.getPreferredSize().width, defaultCard.getPreferredSize().height);
        buttonContainer.setBounds(0, 0, defaultCard.getPreferredSize().width, defaultCard.getPreferredSize().height);
        
        addCardButton.addActionListener(e -> {
            AddCardDialog dialog = new AddCardDialog((Frame) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                // 获取新卡片信息
                String cardNumber = dialog.getCardNumber();
                String cardholderName = dialog.getCardholderName();
                String expiryDate = dialog.getExpiryDate();
                double balance = dialog.getBalance();
                
                // 创建新卡片
                RoundedPanel newCard = createCard(cardNumber, cardholderName, expiryDate, balance);
                
                // 创建新卡片的按钮容器
                JPanel newButtonContainer = new JPanel(null);
                newButtonContainer.setOpaque(false);
                newButtonContainer.setPreferredSize(newCard.getPreferredSize());
                
                // 复制添加按钮到新卡片
                RoundedButton newAddButton = new RoundedButton("+ Add Card");
                newAddButton.setFont(addCardButton.getFont());
                newAddButton.setBackground(addCardButton.getBackground());
                newAddButton.setForeground(addCardButton.getForeground());
                newAddButton.setBorderPainted(false);
                newAddButton.setFocusPainted(false);
                newAddButton.setContentAreaFilled(false);
                newAddButton.setBackground(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                newAddButton.setBorder(addCardButton.getBorder());
                newAddButton.setBounds(
                    newCard.getPreferredSize().width - 100,
                    10,
                    90,
                    30
                );
                newButtonContainer.add(newAddButton);
                
                // 创建新卡片的叠加面板
                JLayeredPane newLayeredPane = new JLayeredPane();
                newLayeredPane.setPreferredSize(newCard.getPreferredSize());
                newLayeredPane.add(newCard, JLayeredPane.DEFAULT_LAYER);
                newLayeredPane.add(newButtonContainer, JLayeredPane.PALETTE_LAYER);
                
                // 设置新卡片和按钮容器的位置
                newCard.setBounds(0, 0, newCard.getPreferredSize().width, newCard.getPreferredSize().height);
                newButtonContainer.setBounds(0, 0, newCard.getPreferredSize().width, newCard.getPreferredSize().height);
                
                // 添加新卡片到容器
                cardsContainer.add(newLayeredPane);
                cardsContainer.add(Box.createHorizontalStrut(15)); // 添加卡片间距
                
                // 刷新容器
                cardsContainer.revalidate();
                cardsContainer.repaint();
                
                // 滚动到新添加的卡片
                scrollPane.getHorizontalScrollBar().setValue(scrollPane.getHorizontalScrollBar().getMaximum());
                
                // 显示成功消息
                JOptionPane.showMessageDialog(this,
                    "Card added successfully!\n" +
                    "Card Number: " + maskCardNumber(cardNumber) + "\n" +
                    "Cardholder: " + cardholderName + "\n" +
                    "Expiry Date: " + expiryDate + "\n" +
                    "Balance: $" + String.format("%.2f", balance),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // 添加默认卡片到容器
        cardsContainer.add(layeredPane);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }
    
    // 创建单个卡片
    private RoundedPanel createCard(String cardNumber, String cardholderName, String expiryDate, double balance) {
        // 创建带圆角的卡片容器
        RoundedPanel card = new RoundedPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(40, 80, 150));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        card.setPreferredSize(new Dimension(320, 200));
        card.setMaximumSize(new Dimension(320, 200));
        card.setMinimumSize(new Dimension(320, 200));
        
        // 主内容容器
        JPanel content = new JPanel(new GridBagLayout()) {
            /**
             * 重写 isOpaque 方法，使面板背景透明。
             * 
             * @return false，表示面板背景透明
             */
            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        // 创建网格包约束对象
        GridBagConstraints gbc = new GridBagConstraints();
        // 设置组件间的内边距为 5 像素
        gbc.insets = new Insets(5, 5, 5, 5); 

        // 余额区域（左上）
        JPanel balancePanel = new JPanel(new BorderLayout());
        // 设置余额面板背景透明
        balancePanel.setOpaque(false); 
        JLabel balanceLabel = new JLabel("BALANCE");
        // 设置余额标签字体为 Arial 加粗，字号 12
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 12)); 
        // 设置余额标签字体颜色为浅灰色
        balanceLabel.setForeground(new Color(180, 180, 220)); 
        
        JLabel amountLabel = new JLabel("$5,756");
        // 设置金额标签字体为 Arial 加粗，字号 24
        amountLabel.setFont(new Font("Arial", Font.BOLD, 24)); 
        // 设置金额标签字体颜色为白色
        amountLabel.setForeground(Color.WHITE); 
        
        // 将余额标签添加到余额面板的北部位置
        balancePanel.add(balanceLabel, BorderLayout.NORTH); 
        // 将金额标签添加到余额面板的中心位置
        balancePanel.add(amountLabel, BorderLayout.CENTER); 

        // 卡号区域（中间偏上）
        JPanel numberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        numberPanel.setOpaque(false);
        String[] segments = cardNumber.split(" ");
        for (String segment : segments) {
            numberPanel.add(createCardSegment(segment, segment.length() == 4 ? 22 : 18));
        }

        // 底部信息区域
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        // 设置底部信息面板背景透明
        bottomPanel.setOpaque(false); 
        
        // 左侧持卡人信息
        JPanel holderPanel = new JPanel(new BorderLayout());
        // 设置持卡人信息面板背景透明
        holderPanel.setOpaque(false); 
        JLabel holderLabel = new JLabel("CARDHOLDER");
        // 设置持卡人标签字体为 Arial 加粗，字号 10
        holderLabel.setFont(new Font("Arial", Font.BOLD, 10)); 
        // 设置持卡人标签字体颜色为浅灰色
        holderLabel.setForeground(new Color(180, 180, 220)); 
        
        JLabel nameLabel = new JLabel("Eddy Cusuma");
        // 设置持卡人姓名标签字体为 Arial 加粗，字号 14
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14)); 
        // 设置持卡人姓名标签字体颜色为白色
        nameLabel.setForeground(Color.WHITE); 
        
        // 将持卡人标签添加到持卡人信息面板的北部位置
        holderPanel.add(holderLabel, BorderLayout.NORTH); 
        // 将持卡人姓名标签添加到持卡人信息面板的中心位置
        holderPanel.add(nameLabel, BorderLayout.CENTER); 

        // 右侧有效期信息
        JPanel validPanel = new JPanel(new BorderLayout());
        // 设置有效期信息面板背景透明
        validPanel.setOpaque(false); 
        JLabel validLabel = new JLabel("VALID THRU");
        // 设置有效期标签字体为 Arial 加粗，字号 10
        validLabel.setFont(new Font("Arial", Font.BOLD, 10)); 
        // 设置有效期标签字体颜色为浅灰色
        validLabel.setForeground(new Color(180, 180, 220)); 
        
        JLabel dateLabel = new JLabel("12/22");
        // 设置有效期日期标签字体为 Arial 加粗，字号 14
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14)); 
        // 设置有效期日期标签字体颜色为白色
        dateLabel.setForeground(Color.WHITE); 
        
        // 将有效期标签添加到有效期信息面板的北部位置
        validPanel.add(validLabel, BorderLayout.NORTH); 
        // 将有效期日期标签添加到有效期信息面板的中心位置
        validPanel.add(dateLabel, BorderLayout.CENTER); 

        // 布局组合
        // 设置网格包约束的 x 坐标为 0
        gbc.gridx = 0;
        // 设置网格包约束的 y 坐标为 0
        gbc.gridy = 0;
        // 设置组件对齐方式为左上角对齐
        gbc.anchor = GridBagConstraints.NORTHWEST;
        // 将余额区域添加到内容面板
        content.add(balancePanel, gbc); 

        // 设置网格包约束的 y 坐标为 1
        gbc.gridy = 1;
        // 设置组件在水平方向上的权重为 1.0
        gbc.weightx = 1.0;
        // 设置组件在水平方向上填充
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // 将卡号区域添加到内容面板
        content.add(numberPanel, gbc); 

        // 将持卡人信息添加到底部信息面板
        bottomPanel.add(holderPanel); 
        // 将有效期信息添加到底部信息面板
        bottomPanel.add(validPanel); 

        // 设置网格包约束的 y 坐标为 2
        gbc.gridy = 2;
        // 设置组件在垂直方向上的权重为 1.0
        gbc.weighty = 1.0;
        // 设置组件在水平和垂直方向上填充
        gbc.fill = GridBagConstraints.BOTH;
        // 将底部信息区域添加到内容面板
        content.add(bottomPanel, gbc); 

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    /**
     * 创建卡号分段组件，用于显示银行卡号的分段信息。
     * 
     * @param text 卡号分段的文本内容
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
     * 创建交易表格，显示最近的交易记录。
     * 
     * @return 包含交易表格的滚动面板
     */
    private JScrollPane createTransactionTable() {
        // 定义表格的列名
        String[] columns = {"", "Date", "Description", "Amount"};
        // 定义表格的初始数据
        Object[][] initialData = {
            {new ImageIcon("path/to/card_icon.png"), "28 January 2021", "Deposit from my Card", "-$850"},
            {new ImageIcon("path/to/paypal_icon.png"), "25 January 2021", "Deposit Paypal", "+$2,500"},
            {new ImageIcon("path/to/user_icon.png"), "21 January 2021", "Jemi Wilson", "+$5,400"}
        };
        
        // 创建表格模型
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 使表格不可编辑
            }
        };

        // 添加初始数据
        for (Object[] row : initialData) {
            tableModel.addRow(row);
        }

        // 创建表格并重写 prepareRenderer 方法
        transactionTable = new JTable(tableModel) {
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

        // 设置表格的行高为 35 像素
        transactionTable.setRowHeight(35);
        // 设置表格表头的字体
        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        // 设置表格表头的首选大小
        transactionTable.getTableHeader().setPreferredSize(new Dimension(100, 35));

        // 隐藏第一列标题
        transactionTable.getColumnModel().getColumn(0).setHeaderValue("");

        // 设置各列宽度
        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        transactionTable.getColumnModel().getColumn(3).setPreferredWidth(100);

        // 设置字体和颜色
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        transactionTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        transactionTable.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
        transactionTable.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        transactionTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        // 创建修改按钮
        JButton editButton = new JButton("modify");
        editButton.setFont(new Font("Arial", Font.BOLD, 12));
        editButton.setBackground(AppConstants.PRIMARY_COLOR);
        editButton.setForeground(Color.WHITE);
        editButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // 添加修改按钮的点击事件，修复未使用的lambda参数
        editButton.addActionListener(_e -> {
            int selectedRow = transactionTable.getSelectedRow();
            if (selectedRow >= 0) {
                // 获取选中的行数据
                String date = (String) tableModel.getValueAt(selectedRow, 1);
                String description = (String) tableModel.getValueAt(selectedRow, 2);
                String amount = (String) tableModel.getValueAt(selectedRow, 3);
                
                // 判断交易类型
                String type = amount.startsWith("-") ? "Expense" : "Income";
                
                // 创建编辑对话框
                EditTransactionDialog dialog = new EditTransactionDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    date,
                    description,
                    amount.replace("$", "").replace("+", "").replace("-", ""),
                    type
                );
                
                dialog.setVisible(true);
                
                if (dialog.isConfirmed()) {
                    // 获取修改后的数据
                    String newDate = dialog.getDate();
                    String newDescription = dialog.getDescription();
                    String newAmount = dialog.getAmount();
                    String newType = dialog.getTransactionType();
                    
                    // 更新表格数据
                    updateTransactionInTable(selectedRow, newDate, newDescription, newAmount, newType);
                    
                    // 同步到TransactionsView
                    if (transactionsView != null) {
                        // 先删除旧数据
                        transactionsView.removeTransaction(date, description, amount, type);
                        // 添加新数据
                        transactionsView.addTransaction(newDate, newDescription, 
                            (newType.equals("Expense") ? "-" : "+") + "$" + newAmount, newType);
                    }
                    
                    // 显示成功消息
                    JOptionPane.showMessageDialog(this, 
                        "The transaction information has been successfully modified!",
                        "Modification successful",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // 如果没有选中行，显示提示消息
                JOptionPane.showMessageDialog(this, 
                    "Please select the transaction record that you want to modify first!",
                    "Hint",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(editButton);
        
        // 创建包含表格和按钮的面板
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(transactionTable, BorderLayout.CENTER);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        // 设置滚动条策略
        JScrollPane scrollPane = new JScrollPane(tablePanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        return scrollPane;
    }
    
    /**
     * 更新表格中的交易信息
     * 
     * @param row 要更新的行索引
     * @param date 新的日期
     * @param description 新的描述
     * @param amount 新的金额
     * @param type 新的交易类型
     */
    private void updateTransactionInTable(int row, String date, String description, String amount, String type) {
        // 根据交易类型选择图标
        ImageIcon icon = new ImageIcon(type.equals("Income") ? "path/to/income_icon.png" : "path/to/expense_icon.png");
        
        // 格式化金额，如果是支出则添加负号
        String formattedAmount = (type.equals("Expense") ? "-" : "+") + "$" + amount;
        
        // 更新表格数据
        tableModel.setValueAt(icon, row, 0);
        tableModel.setValueAt(date, row, 1);
        tableModel.setValueAt(description, row, 2);
        tableModel.setValueAt(formattedAmount, row, 3);
        
        // 更新数据服务中的交易数据
        dataService.updateTransaction(row, date, description, amount, type);
        
        // 更新图表
        updateCharts();
        
        // 同步更新AccountsView
        if (accountsView != null) {
            accountsView.updateTransaction(date, description, formattedAmount, type);
        }
    }

    private void addTransactionToTable(String date, String description, String amount, String type) {
        // 根据交易类型选择图标
        ImageIcon icon = new ImageIcon(type.equals("Income") ? "path/to/income_icon.png" : "path/to/expense_icon.png");
        
        // 格式化金额，如果是支出则添加负号
        String formattedAmount = (type.equals("Expense") ? "-" : "+") + String.format("%s", amount);
        
        // 在表格末尾添加新行
        tableModel.addRow(new Object[]{icon, date, description, formattedAmount});
        
        // 添加到数据服务
        dataService.addTransaction(date, description, amount, type);
        
        // 更新图表
        updateCharts();

        // 同时更新TransactionsView
        if (transactionsView != null) {
            transactionsView.addTransaction(date, description, formattedAmount, type);
        }
        
        // 同时更新AccountsView
        if (accountsView != null) {
            accountsView.addTransaction(date, description, formattedAmount, type);
        }
    }

    /**
     * 创建周活动图表，显示一周内的消费金额柱状图。
     * 
     * @return 包含周活动图表的图表面板
     */
    private ChartPanel createWeeklyChart() {
        // 创建默认的分类数据集
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // 初始化为空数据集，稍后会更新
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String day : days) {
            dataset.addValue(0, "Spending", day);
        }
        
        weeklyChart = ChartFactory.createBarChart(
            "", "", "Amount ($)", 
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        
        // 设置图表样式
        CategoryPlot plot = weeklyChart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(40, 80, 150));
        
        // 设置轴的样式
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelsVisible(true);
        domainAxis.setTickMarksVisible(true);
        
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        weeklyChart.setBackgroundPaint(Color.WHITE);
        
        weeklyChartPanel = new ChartPanel(weeklyChart);
        weeklyChartPanel.setPreferredSize(new Dimension(400, 300));
        return weeklyChartPanel;
    }

    /**
     * 创建支出统计图表，显示各项支出的占比饼图。
     * 
     * @return 包含支出统计图表的图表面板
     */
    private ChartPanel createExpenseChart() {
        // 创建默认的饼图数据集
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        // 初始化为空数据集，稍后会更新
        dataset.setValue("No Data", 100);
        
        expenseChart = ChartFactory.createPieChart("", dataset, true, true, false);
        
        // 设置饼图样式
        PiePlot plot = (PiePlot) expenseChart.getPlot();
        plot.setLabelFont(new Font("Arial", Font.PLAIN, 12));
        plot.setLabelBackgroundPaint(new Color(255, 255, 255, 200));
        
        // 为No Data设置颜色（按索引设置，因为现在只有一个数据点）
        plot.setSectionPaint(0, new Color(200, 200, 200));
        
        expenseChart.setBackgroundPaint(Color.WHITE);
        
        expenseChartPanel = new ChartPanel(expenseChart);
        expenseChartPanel.setPreferredSize(new Dimension(400, 300));
        return expenseChartPanel;
    }
    
    /**
     * 更新图表
     */
    public void updateCharts() {
        updateWeeklyChart();
        updateExpenseChart();
    }
    
    /**
     * 更新周活动图表
     */
    private void updateWeeklyChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Double> weeklyData = dataService.getWeeklySpending();
        
        for (Map.Entry<String, Double> entry : weeklyData.entrySet()) {
            dataset.addValue(entry.getValue(), "Spending", entry.getKey());
        }
        
        weeklyChart.getCategoryPlot().setDataset(dataset);
    }
    
    /**
     * 更新支出统计图表
     */
    private void updateExpenseChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Double> categoryData = dataService.getExpenseCategories();
        
        // 将数据添加到饼图数据集
        for (Map.Entry<String, Double> entry : categoryData.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        
        // 如果没有数据，添加默认分类
        if (categoryData.isEmpty()) {
            dataset.setValue("No Data", 100);
        }
        
        // 设置饼图数据集
        PiePlot plot = (PiePlot) expenseChart.getPlot();
        plot.setDataset(dataset);
        
        // 创建颜色分配映射
        Map<String, Color> colorMap = new HashMap<>();
        colorMap.put("Housing", new Color(51, 102, 204));
        colorMap.put("Food", new Color(76, 153, 0));
        colorMap.put("Transport", new Color(255, 153, 0));
        colorMap.put("Entertainment", new Color(153, 0, 153));
        colorMap.put("Savings", new Color(0, 153, 204));
        colorMap.put("Others", new Color(153, 153, 153));
        colorMap.put("No Data", new Color(200, 200, 200));
        
        // 遍历数据集的键，设置相应的颜色
        int index = 0;
        for (Object key : dataset.getKeys()) {
            String category = key.toString();
            Color color = colorMap.getOrDefault(category, new Color(100, 100, 150));
            
            // 使用索引而不是字符串键来设置颜色
            plot.setSectionPaint(index, color);
            index++;
        }
    }

    /**
     * 创建操作面板，包含手动添加和导入文件的操作按钮。
     * 
     * @return 包含操作按钮的面板
     */
    private JPanel createActionPanel() {
        // 创建圆角面板，使用居中对齐的流式布局，组件间的水平间距为 20 像素，垂直间距为 10 像素
        RoundedPanel panel = new RoundedPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        // 设置面板的复合边框，包含标题边框和内边距
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Add transactions"), 
            BorderFactory.createEmptyBorder(20, 0, 0, 0) 
        ));
        // 添加手动添加操作按钮
        panel.add(createActionButton("Manual Add")); 
        // 添加导入文件操作按钮
        panel.add(createActionButton("Import File")); 
        return panel;
    }

    private String processWithAI(String text) {
        String prompt = "Please convert the following transaction information into the standard format (Date|Description|Amount|Type), " +
                       "where Type can only be Income or Expense, the amount should be positive, and the date format should be dd/MM/yyyy.\n" +
                       "Original text:" + text + "\n" +
                       "Your response will only return the final result in standard format without any other contents. For example: 08/07/2025 | Rent Payment | 1,000 yuan | Expense.";
        // System.out.println(text);
        String response = aiService.getAIResponse(prompt);
        System.out.println("Raw AI response: " + response);
        
        // 从JSON响应中提取result字段，使用非废弃的方法
        try {
            // 使用JsonParser.parseString替代废弃的new JsonParser().parse
            JsonElement jsonElement = JsonParser.parseString(response);
            JsonObject jsonResponse = jsonElement.getAsJsonObject();
            String result = jsonResponse.get("result").getAsString();
            
            // 提取第一行交易信息（假设每行只处理一条交易）
            String[] lines = result.split("\n");
            for (String line : lines) {
                if (line.contains("|")) {
                    return line.trim();
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing AI response: " + e.getMessage());
        }
        
        return text; // 如果解析失败，返回原始文本
    }

    private void importTransactionsFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                java.util.Scanner scanner = new java.util.Scanner(file);
                int successCount = 0;
                int failCount = 0;
                
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        try {
                            // 尝试直接解析标准格式
                            String[] parts = line.split("\\|");
                            if (parts.length == 4) {
                                String date = parts[0].trim();
                                String description = parts[1].trim();
                                double amount = Double.parseDouble(parts[2].trim());
                                String type = parts[3].trim();
                                
                                if (type.equals("Income") || type.equals("Expense")) {
                                    addTransactionToTable(date, description, String.valueOf(amount), type);
                                    successCount++;
                                    continue;
                                }
                            }
                            
                            // 如果不符合标准格式，使用AI处理
                            String processedLine = processWithAI(line);
                            System.out.println("Processed line: " + processedLine);
                            parts = processedLine.split("\\|");
                            // System.out.print(parts.length);
                            if (parts.length == 4) {
                                String date = parts[0].trim();
                                String description = parts[1].trim();
                                String amount = parts[2].trim();
                                String type = parts[3].trim();
                                
                                if (type!="") {
                                    addTransactionToTable(date, description, amount, type);
                                    successCount++;
                                } else {
                                    failCount++;
                                }
                            } else {
                                failCount++;
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                            failCount++;
                        }
                    }
                }
                scanner.close();
                
                // 更新图表
                updateCharts();
                
                JOptionPane.showMessageDialog(this, 
                    "File imported successfully!\n" +
                    "Successfully imported: " + successCount + " transactions\n" +
                    "Failed to import: " + failCount + " transactions",
                    "Import Result",
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error importing file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 创建操作按钮，设置按钮的字体、背景颜色、字体颜色和内边距。
     * 
     * @param text 按钮显示的文本
     * @return 配置好的操作按钮
     */
    private JButton createActionButton(String text) {
        // 创建圆角按钮
        RoundedButton btn = new RoundedButton(text);
        // 设置按钮字体为全局定义的按钮字体
        btn.setFont(AppConstants.BUTTON_FONT); 
        // 设置按钮背景颜色为全局定义的主色调
        btn.setBackground(AppConstants.PRIMARY_COLOR); 
        // 设置按钮字体颜色为白色
        btn.setForeground(Color.WHITE); 
        // 设置按钮的内边距，上、左、下、右分别为 10、25、10、25 像素
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25)); 

        // 为Manual Add按钮添加事件处理程序，修复未使用的lambda参数
        if (text.equals("Manual Add")) {
            btn.addActionListener(_e -> {
                AddTransactionDialog dialog = new AddTransactionDialog((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);
                
                if (dialog.isConfirmed()) {
                    // 获取用户输入的交易信息
                    String date = dialog.getDate();
                    String description = dialog.getDescription();
                    double amount = dialog.getAmount();
                    String type = dialog.getTransactionType();
                    
                    // 添加交易到表格
                    addTransactionToTable(date, description, String.valueOf(amount), type);
                    
                    // 显示成功消息
                    JOptionPane.showMessageDialog(this, 
                        "Transaction added successfully!\n" +
                        "Date: " + date + "\n" +
                        "Description: " + description + "\n" +
                        "Amount: " + amount + "\n" +
                        "Type: " + type,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            });
        } else if (text.equals("Import File")) {
            btn.addActionListener(_e -> importTransactionsFromFile());
        }
        
        return btn;
    }

    /**
     * 包装组件，为组件添加标题边框并设置背景颜色。
     * 
     * @param comp 需要包装的组件
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

    /**
     * 设置TransactionsView引用，用于同步数据
     */
    public void setTransactionsView(TransactionsView view) {
        this.transactionsView = view;
    }
    
    /**
     * 设置AccountsView引用，用于同步数据
     */
    public void setAccountsView(AccountsView view) {
        this.accountsView = view;
    }
    
    /**
     * 从表格加载初始交易数据到数据服务
     */
    private void loadInitialTransactions() {
        try {
            // 从表格中获取已有的交易数据
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String date = (String) tableModel.getValueAt(i, 1);
                String description = (String) tableModel.getValueAt(i, 2);
                String amount = (String) tableModel.getValueAt(i, 3);
                String type = amount.startsWith("-") ? "Expense" : "Income";
                
                // 清理金额字符串
                String cleanAmount = amount.replace("$", "").replace("+", "").replace("-", "");
                
                // 添加到数据服务
                dataService.addTransaction(date, description, cleanAmount, type);
            }
            
            // 首次更新图表
            updateCharts();
        } catch (Exception e) {
            // 添加异常处理以便更好地诊断问题
            System.err.println("Error in loadInitialTransactions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 更新卡片显示
    private void updateCardDisplay(RoundedPanel panel, String cardNumber, String cardholderName, String expiryDate, double balance) {
        // 获取内容面板
        JPanel content = (JPanel) panel.getComponent(1);
        
        // 更新余额
        JPanel balancePanel = (JPanel) content.getComponent(0);
        JLabel amountLabel = (JLabel) balancePanel.getComponent(1);
        amountLabel.setText(String.format("$%.2f", balance));
        
        // 更新卡号
        JPanel numberPanel = (JPanel) content.getComponent(1);
        numberPanel.removeAll();
        String maskedNumber = maskCardNumber(cardNumber);
        String[] segments = maskedNumber.split(" ");
        for (String segment : segments) {
            numberPanel.add(createCardSegment(segment, segment.length() == 4 ? 22 : 18));
        }
        
        // 更新持卡人信息
        JPanel bottomPanel = (JPanel) content.getComponent(2);
        JPanel holderPanel = (JPanel) bottomPanel.getComponent(0);
        JLabel nameLabel = (JLabel) holderPanel.getComponent(1);
        nameLabel.setText(cardholderName);
        
        // 更新有效期
        JPanel validPanel = (JPanel) bottomPanel.getComponent(1);
        JLabel dateLabel = (JLabel) validPanel.getComponent(1);
        dateLabel.setText(expiryDate);
        
        // 刷新面板
        panel.revalidate();
        panel.repaint();
    }
    
    // 掩码卡号
    private String maskCardNumber(String cardNumber) {
        return cardNumber.substring(0, 4) + " **** **** " + cardNumber.substring(12);
    }
}