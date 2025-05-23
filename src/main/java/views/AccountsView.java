package views;

import components.*;
import constants.AppConstants;
import services.TransactionDataService;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;
import ui.AddCardDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

/**
 * AccountsView 类继承自 BaseView，用于展示账户相关信息的视图界面。
 * 该视图包含账户摘要、我的卡片、最近交易记录和每周概览等内容，还提供手动添加和导入文件的操作按钮。
 */
public class AccountsView extends BaseView {
    /**
     * 获取视图的名称，用于在界面切换时标识该视图。
     * 
     * @return 视图的名称，固定为 "Accounts"
     */
    @Override
    public String getViewName() {
        return "Accounts";
    }

    private JTable transactionTable;
    private DefaultTableModel transactionTableModel;
    private ChartPanel weeklyChartPanel;
    private DefaultCategoryDataset weeklyDataset;
    
    // 添加数据服务和图表相关字段
    private TransactionDataService dataService;
    private JFreeChart weeklyChart;
    
    // 余额和收支视图元素
    private JLabel balanceAmountLabel;
    private JLabel incomeAmountLabel;
    private JLabel expenseAmountLabel;

    /**
     * 构造函数
     */
    public AccountsView() {
        // 初始化数据服务
        dataService = new TransactionDataService();
    }

    /**
     * 初始化用户界面的方法，设置布局、添加标题、主内容区和底部操作栏。
     */
    @Override
    protected void initUI() {
        // 设置布局管理器，并设置组件之间的水平和垂直间距为 15 像素
        setLayout(new BorderLayout(15, 15));
        // 设置面板的内边距，上、左、下、右均为 20 像素
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 顶部标题
        // 创建标题标签，文本为 "Accounts"，左对齐
        JLabel titleLabel = new JLabel("Accounts", SwingConstants.LEFT);
        // 设置标题字体为全局定义的标题字体
        titleLabel.setFont(AppConstants.TITLE_FONT);
        // 设置标题标签的底部内边距为 15 像素
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        // 将标题标签添加到面板的北部位置
        add(titleLabel, BorderLayout.NORTH);

        // 主内容区
        // 创建一个圆角面板，使用 2x2 的网格布局，组件之间的水平和垂直间距为 15 像素
        RoundedPanel mainPanel = new RoundedPanel(new GridLayout(2, 2, 15, 15));
        // 将账户摘要组件包装后添加到主面板
        mainPanel.add(wrapComponent(createAccountSummary(), "Account Summary"));
        // 将我的卡片组件包装后添加到主面板
        mainPanel.add(wrapComponent(createMyCard(), "My Card"));
        // 将最近交易记录组件包装后添加到主面板
        mainPanel.add(wrapComponent(createTransactionHistory(), "Recent Transactions"));
        // 将每周概览组件包装后添加到主面板
        mainPanel.add(wrapComponent(createWeeklyChart(), "Weekly Overview"));
        // 将主面板添加到面板的中心位置
        add(mainPanel, BorderLayout.CENTER);

        // 底部操作栏
        // 将底部操作面板添加到面板的南部位置
        add(createActionPanel(), BorderLayout.SOUTH);
        
        // 在所有UI组件创建完毕后，加载初始交易数据
        loadInitialTransactions();
    }

    /**
     * 加载初始交易数据到数据服务
     */
    private void loadInitialTransactions() {
        try {
            // 从表格中获取已有的交易数据
            for (int i = 0; i < transactionTableModel.getRowCount(); i++) {
                String date = (String) transactionTableModel.getValueAt(i, 0);
                String description = (String) transactionTableModel.getValueAt(i, 1);
                String amount = (String) transactionTableModel.getValueAt(i, 2);
                String status = (String) transactionTableModel.getValueAt(i, 3); // 忽略状态
                String type = amount.startsWith("-") ? "Expense" : "Income";
                
                // 清理金额字符串
                String cleanAmount = amount.replace("$", "").replace("+", "").replace("-", "");
                
                // 添加到数据服务
                dataService.addTransaction(date, description, cleanAmount, type);
            }
            
            // 首次更新图表和摘要
            updateWeeklyChart();
            updateAccountSummary();
        } catch (Exception e) {
            // 添加异常处理以便更好地诊断问题
            System.err.println("Error in loadInitialTransactions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 创建账户摘要面板，包含我的余额、月收入和月支出三个摘要卡片。
     * 
     * @return 包含账户摘要卡片的面板
     */
    private JPanel createAccountSummary() {
        // 创建一个面板，使用 1x3 的网格布局，组件之间的水平和垂直间距为 15 像素
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 15));
        // 设置面板背景透明
        panel.setOpaque(false);

        // 创建余额摘要卡片并保存标签引用
        JPanel balanceCard = createSummaryCard("My Balance", "$12,750", new Color(40, 80, 150));
        balanceAmountLabel = (JLabel) ((BorderLayout)balanceCard.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        panel.add(balanceCard);
        
        // 创建收入摘要卡片并保存标签引用
        JPanel incomeCard = createSummaryCard("Monthly Income", "$560", new Color(60, 120, 60));
        incomeAmountLabel = (JLabel) ((BorderLayout)incomeCard.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        panel.add(incomeCard);
        
        // 创建支出摘要卡片并保存标签引用
        JPanel expenseCard = createSummaryCard("Monthly Expense", "$346", new Color(150, 60, 60));
        expenseAmountLabel = (JLabel) ((BorderLayout)expenseCard.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        panel.add(expenseCard);

        return panel;
    }

    /**
     * 创建摘要卡片，用于显示账户相关的摘要信息。
     * 
     * @param title 卡片的标题
     * @param amount 卡片显示的金额
     * @param bgColor 卡片的背景颜色
     * @return 包含标题和金额的摘要卡片面板
     */
    private JPanel createSummaryCard(String title, String amount, Color bgColor) {
        // 创建一个圆角面板，使用边界布局
        RoundedPanel card = new RoundedPanel(new BorderLayout());
        // 设置卡片的背景颜色
        card.setBackground(bgColor);
        // 设置卡片的内边距，上、左、下、右均为 20 像素
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 创建标题标签
        JLabel titleLabel = new JLabel(title);
        // 设置标题标签的字体为 Arial 加粗，字号 16
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        // 设置标题标签的文字颜色为白色
        titleLabel.setForeground(Color.WHITE);

        // 创建金额标签
        JLabel amountLabel = new JLabel(amount);
        // 设置金额标签的字体为 Arial 加粗，字号 24
        amountLabel.setFont(new Font("Arial", Font.BOLD, 24));
        // 设置金额标签的文字颜色为白色
        amountLabel.setForeground(Color.WHITE);

        // 将标题标签添加到卡片的北部位置
        card.add(titleLabel, BorderLayout.NORTH);
        // 将金额标签添加到卡片的中心位置
        card.add(amountLabel, BorderLayout.CENTER);

        return card;
    }

    /**
     * 创建我的卡片面板，模拟银行卡的界面。
     * 
     * @return 包含银行卡信息的面板
     */
    private JPanel createMyCard() {
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
        RoundedPanel mainPanel = new RoundedPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 将 Graphics 对象转换为 Graphics2D 对象以使用更高级的绘图功能
                Graphics2D g2d = (Graphics2D) g;
                // 设置绘图颜色为深蓝色
                g2d.setColor(new Color(40, 80, 150)); 
                // 绘制圆角矩形填充整个面板
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); 
            }
        };
        // 设置卡片面板的内边距，上、左、下、右均为 25 像素
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        // 设置卡片面板的首选大小为 320x200 像素
        mainPanel.setPreferredSize(new Dimension(320, 200));

        // 创建默认卡片
        RoundedPanel defaultCard = createCard();
        
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
        
        // 添加按钮事件
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
                    "卡片添加成功！\n" +
                    "卡号: " + maskCardNumber(cardNumber) + "\n" +
                    "持卡人: " + cardholderName + "\n" +
                    "有效期: " + expiryDate + "\n" +
                    "余额: $" + String.format("%.2f", balance),
                    "成功",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // 添加默认卡片到容器
        cardsContainer.add(layeredPane);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }
    
    // 创建单个卡片 - 无参默认版本
    private RoundedPanel createCard() {
        return createCard("3778****1234", "Eddy Cusuma", "12/22", 5756.00);
    }
    
    // 创建单个卡片 - 带参数版本
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
        // 设置卡片面板的内边距，上、左、下、右均为 25 像素
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25)); 
        // 设置卡片面板的首选大小为 320x200 像素
        card.setPreferredSize(new Dimension(320, 200)); 
        
        // 主内容容器
        JPanel content = new JPanel(new GridBagLayout()) {
            /**
             * 重写 isOpaque 方法，使面板背景透明。
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

        // 余额区域
        JPanel balancePanel = new JPanel(new BorderLayout());
        // 设置余额面板背景透明
        balancePanel.setOpaque(false); 
        JLabel balanceLabel = new JLabel("BALANCE");
        // 设置余额标签字体为 Arial 加粗，字号 12
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 12)); 
        // 设置余额标签字体颜色为浅灰色
        balanceLabel.setForeground(new Color(180, 180, 220)); 
        
        JLabel amountLabel = new JLabel(String.format("$%.2f", balance));
        // 设置金额标签字体为 Arial 加粗，字号 24
        amountLabel.setFont(new Font("Arial", Font.BOLD, 24)); 
        // 设置金额标签字体颜色为白色
        amountLabel.setForeground(Color.WHITE); 
        
        // 将余额标签添加到余额面板的北部位置
        balancePanel.add(balanceLabel, BorderLayout.NORTH); 
        // 将金额标签添加到余额面板的中心位置
        balancePanel.add(amountLabel, BorderLayout.CENTER); 

        // 卡号区域
        JPanel numberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        numberPanel.setOpaque(false);
        String[] segments = cardNumber.split(" ");
        if (segments.length == 1) {
            // 如果没有空格分隔，则按照每4位分隔
            numberPanel.add(createCardSegment(cardNumber.substring(0, 4), 22));
            numberPanel.add(createCardSegment("****", 18));
            numberPanel.add(createCardSegment("****", 18));
            numberPanel.add(createCardSegment(cardNumber.substring(cardNumber.length() - 4), 22));
        } else {
            for (String segment : segments) {
                numberPanel.add(createCardSegment(segment, segment.length() == 4 ? 22 : 18));
            }
        }

        // 底部信息区域
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        // 设置底部信息面板背景透明
        bottomPanel.setOpaque(false); 
        
        // 左侧持卡人信息
        JPanel holderPanel = new JPanel(new BorderLayout());
        // 设置持卡人信息面板背景透明
        holderPanel.setOpaque(false); 
        JLabel holderLabel = new JLabel("CARD HOLDER");
        // 设置持卡人标签字体为 Arial 加粗，字号 10
        holderLabel.setFont(new Font("Arial", Font.BOLD, 10)); 
        // 设置持卡人标签字体颜色为浅灰色
        holderLabel.setForeground(new Color(180, 180, 220)); 
        
        JLabel nameLabel = new JLabel(cardholderName);
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
        
        JLabel dateLabel = new JLabel(expiryDate);
        // 设置有效期日期标签字体为 Arial 加粗，字号 14
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14)); 
        // 设置有效期日期标签字体颜色为白色
        dateLabel.setForeground(Color.WHITE); 
        
        // 将有效期标签添加到有效期信息面板的北部位置
        validPanel.add(validLabel, BorderLayout.NORTH); 
        // 将有效期日期标签添加到有效期信息面板的中心位置
        validPanel.add(dateLabel, BorderLayout.CENTER); 

        // 将持卡人信息添加到底部信息面板
        bottomPanel.add(holderPanel); 
        // 将有效期信息添加到底部信息面板
        bottomPanel.add(validPanel); 

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
    
    // 掩码卡号
    private String maskCardNumber(String cardNumber) {
        if (cardNumber.length() >= 16) {
            return cardNumber.substring(0, 4) + " **** **** " + cardNumber.substring(12);
        } else {
            return cardNumber;
        }
    }

    /**
     * 创建银行卡卡号分段标签。
     * 
     * @param text 标签显示的文本
     * @param fontSize 标签字体的大小
     * @return 包含指定文本和字体大小的标签
     */
    private JLabel createCardSegment(String text, int fontSize) {
        // 创建一个标签，显示指定的文本
        JLabel segment = new JLabel(text);
        // 设置标签的字体为 Arial 加粗，指定字号
        segment.setFont(new Font("Arial", Font.BOLD, fontSize));
        // 设置标签的文字颜色为白色
        segment.setForeground(Color.WHITE);
        // 设置标签的左右内边距为 5 像素
        segment.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        return segment;
    }

    /**
     * 创建最近交易记录的滚动面板，包含一个表格显示交易记录。
     * 
     * @return 包含交易记录表格的滚动面板
     */
    private JScrollPane createTransactionHistory() {
        // 定义表格的列名
        String[] columns = {"Date", "Description", "Amount", "Status"};
        // 定义表格的数据
        Object[][] data = {
            {"25 Jan 2021", "Spotify Subscription", "-$150", "Pending"},
            {"25 Jan 2021", "Mobile Service", "-$340", "Completed"},
            {"25 Jan 2021", "Emily Wilson", "+$780", "Completed"}
        };

        // 创建一个表格模型
        transactionTableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true; // 使表格不可编辑
            }
        };
        
        // 创建一个表格，使用指定的列名和数据
        transactionTable = new JTable(transactionTableModel);
        // 设置表格的行高为 35 像素
        transactionTable.setRowHeight(35);
        // 设置表格表头的字体为 Arial 加粗，字号 14
        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        // 设置表格表头的首选大小为 100x35 像素
        transactionTable.getTableHeader().setPreferredSize(new Dimension(100, 35));

        // 状态列渲染
        // 为表格的状态列设置自定义渲染器
        transactionTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            /**
             * 重写 getTableCellRendererComponent 方法，根据单元格的值设置不同的背景和前景颜色。
             * 
             * @param table 包含该单元格的表格
             * @param value 单元格的值
             * @param isSelected 单元格是否被选中
             * @param hasFocus 单元格是否有焦点
             * @param row 单元格所在的行
             * @param column 单元格所在的列
             * @return 用于渲染该单元格的组件
             */
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                // 调用父类的方法获取默认的渲染组件
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, 
                        isSelected, hasFocus, row, column);
                // 设置标签的文本居中对齐
                label.setHorizontalAlignment(SwingConstants.CENTER);
                // 设置标签的背景可见
                label.setOpaque(true);
                // 获取单元格的状态值
                String status = (String) value;
                if ("Pending".equals(status)) {
                    // 如果状态为 "Pending"，设置背景颜色为橙色，文字颜色为黑色
                    label.setBackground(new Color(255, 165, 0));
                    label.setForeground(Color.BLACK);
                } else if ("Completed".equals(status)) {
                    // 如果状态为 "Completed"，设置背景颜色为绿色，文字颜色为白色
                    label.setBackground(new Color(50, 205, 50));
                    label.setForeground(Color.WHITE);
                }
                return label;
            }
        });

        // 将表格添加到滚动面板中并返回
        return new JScrollPane(transactionTable);
    }

    /**
     * 创建每周概览的图表面板，显示每周的收入和支出柱状图。
     * 
     * @return 包含每周概览图表的面板
     */
    private ChartPanel createWeeklyChart() {
        // 创建一个默认的分类数据集
        weeklyDataset = new DefaultCategoryDataset();
        // 定义一周的日期
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        // 设置初始值为0
        for (String day : days) {
            weeklyDataset.addValue(0, "Spending", day); 
        }
        
        // 创建柱状图
        weeklyChart = ChartFactory.createBarChart(
            "", "", "Amount ($)", 
            weeklyDataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        // 设置图表的背景颜色为白色
        weeklyChart.setBackgroundPaint(Color.WHITE);
        
        // 返回包含图表的图表面板
        weeklyChartPanel = new ChartPanel(weeklyChart);
        return weeklyChartPanel; 
    }
    
    /**
     * 更新每周概览图表
     */
    private void updateWeeklyChart() {
        try {
            if (dataService != null && weeklyChart != null) {
                // 获取每周消费数据
                Map<String, Double> weeklyData = dataService.getWeeklySpending();
                
                // 创建新数据集
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                
                // 添加数据
                for (Map.Entry<String, Double> entry : weeklyData.entrySet()) {
                    dataset.addValue(entry.getValue(), "Spending", entry.getKey());
                }
                
                // 更新图表数据集
                weeklyChart.getCategoryPlot().setDataset(dataset);
            }
        } catch (Exception e) {
            System.err.println("Error updating weekly chart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 创建底部操作面板，包含手动添加和导入文件两个操作按钮。
     * 
     * @return 包含操作按钮的面板
     */
    private JPanel createActionPanel() {
        // 创建一个圆角面板，使用居中对齐的流式布局，组件之间的水平间距为 20 像素，垂直间距为 10 像素
        RoundedPanel panel = new RoundedPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        // 添加手动添加操作按钮
        panel.add(createActionButton("Manual Add"));
        // 添加导入文件操作按钮
        panel.add(createActionButton("Import File"));
        return panel;
    }

    /**
     * 创建操作按钮，设置按钮的字体、背景颜色、文字颜色和内边距。
     * 
     * @param text 按钮显示的文本
     * @return 配置好的操作按钮
     */
    private JButton createActionButton(String text) {
        // 创建一个圆角按钮，显示指定的文本
        RoundedButton btn = new RoundedButton(text);
        // 设置按钮的字体为全局定义的按钮字体
        btn.setFont(AppConstants.BUTTON_FONT);
        // 设置按钮的背景颜色为全局定义的主色调
        btn.setBackground(AppConstants.PRIMARY_COLOR);
        // 设置按钮的文字颜色为白色
        btn.setForeground(Color.WHITE);
        // 设置按钮的内边距，上、左、下、右分别为 10、25、10、25 像素
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        return btn;
    }

    /**
     * 将指定组件包装在一个带有标题边框的面板中。
     * 
     * @param comp 需要包装的组件
     * @param title 包装面板的标题
     * @return 包含指定组件和标题边框的面板
     */
    private JPanel wrapComponent(Component comp, String title) {
        // 创建一个使用边界布局的面板
        JPanel wrapper = new JPanel(new BorderLayout());
        // 为面板设置一个带有指定标题的边框
        wrapper.setBorder(BorderFactory.createTitledBorder(title));
        // 设置面板的背景颜色为白色
        wrapper.setBackground(Color.WHITE);
        // 将指定组件添加到包装面板的中心位置
        wrapper.add(comp, BorderLayout.CENTER);
        return wrapper;
    }

    /**
     * 添加交易记录到账户视图
     * 
     * @param date 交易日期
     * @param description 交易描述
     * @param amount 交易金额
     * @param type 交易类型
     */
    public void addTransaction(String date, String description, String amount, String type) {
        try {
            // 确定交易状态
            String status = "Completed";
            
            // 添加新行到表格
            Object[] rowData = {date, description, amount, status};
            transactionTableModel.addRow(rowData);
            
            // 添加到数据服务
            String cleanAmount = amount.replace("$", "").replace("+", "").replace("-", "");
            dataService.addTransaction(date, description, cleanAmount, type);
            
            // 更新账户摘要和图表
            updateAccountSummary();
            updateWeeklyChart();
        } catch (Exception e) {
            System.err.println("Error adding transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 更新交易记录
     * 
     * @param date 交易日期
     * @param description 交易描述
     * @param amount 交易金额
     * @param type 交易类型
     */
    public void updateTransaction(String date, String description, String amount, String type) {
        try {
            // 在表格中查找匹配的行
            for (int i = 0; i < transactionTableModel.getRowCount(); i++) {
                String rowDate = (String) transactionTableModel.getValueAt(i, 0);
                String rowDescription = (String) transactionTableModel.getValueAt(i, 1);
                
                if (rowDate.equals(date) && rowDescription.equals(description)) {
                    // 更新匹配的行
                    transactionTableModel.setValueAt(date, i, 0);
                    transactionTableModel.setValueAt(description, i, 1);
                    transactionTableModel.setValueAt(amount, i, 2);
                    // 状态保持不变
                    break;
                }
            }
            
            // 更新账户摘要和图表
            updateAccountSummary();
            updateWeeklyChart();
        } catch (Exception e) {
            System.err.println("Error updating transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 更新账户摘要信息
     */
    private void updateAccountSummary() {
        try {
            if (dataService != null) {
                // 计算总余额
                double totalBalance = 0;
                double totalIncome = 0;
                double totalExpense = 0;
                
                // 获取所有交易
                for (Map<String, Object> transaction : dataService.getTransactions()) {
                    double amount = (Double) transaction.get("amount");
                    String type = (String) transaction.get("type");
                    
                    // 根据类型更新对应的总额
                    if ("Income".equals(type)) {
                        totalIncome += amount;
                        totalBalance += amount;
                    } else { // Expense
                        totalExpense += amount;
                        totalBalance -= amount;
                    }
                }
                
                // 更新UI组件
                balanceAmountLabel.setText(String.format("$%.2f", totalBalance));
                incomeAmountLabel.setText(String.format("$%.2f", totalIncome));
                expenseAmountLabel.setText(String.format("$%.2f", totalExpense));
            }
        } catch (Exception e) {
            System.err.println("Error updating account summary: " + e.getMessage());
            e.printStackTrace();
        }
    }
}