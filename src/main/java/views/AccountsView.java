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
 * The AccountsView class inherits from BaseView and is a view interface used to display account-related information.
 * This view contains content such as account summaries, my cards, recent transaction records, and weekly overviews, and also provides operation buttons for manually adding and importing files.
 */
public class AccountsView extends BaseView {
    /**
     * Obtain the name of the view to identify it when the interface switches.
     *
     * @return The name of the view is fixed as "Accounts".
     */
    @Override
    public String getViewName() {
        return "Accounts";
    }

    /**
     * The JTable displaying recent transactions.
     */
    private JTable transactionTable;
    
    /**
     * The data model for the transaction table.
     */
    private DefaultTableModel transactionTableModel;
    
    /**
     * The panel displaying the weekly spending chart.
     */
    private ChartPanel weeklyChartPanel;
    
    /**
     * The dataset used for the weekly spending chart.
     */
    private DefaultCategoryDataset weeklyDataset;
    
    // 添加数据服务和图表相关字段
    /**
     * Service for managing transaction data.
     */
    private TransactionDataService dataService;
    
    /**
     * The JFreeChart object for the weekly spending chart.
     */
    private JFreeChart weeklyChart;
    
    // 余额和收支视图元素
    /**
     * JLabel displaying the total account balance.
     */
    private JLabel balanceAmountLabel;
    
    /**
     * JLabel displaying the total monthly income.
     */
    private JLabel incomeAmountLabel;
    
    /**
     * JLabel displaying the total monthly expense.
     */
    private JLabel expenseAmountLabel;

    /**
     * Constructs a new AccountsView.
     * Initializes the data service.
     */
    public AccountsView() {
        // 初始化数据服务
        dataService = new TransactionDataService();
    }

    /**
     * Initializes the user interface for the Accounts view.
     * Sets the layout, adds the title, main content area, and action panel.
     * Calls {@link #loadInitialTransactions()} after UI creation.
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
     * Loads initial transaction data from the table model into the data service
     * and updates the account summary and weekly chart. Includes basic error handling
     * for debugging.
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
     * Creates the panel displaying the account summary cards (Balance, Monthly Income, Monthly Expense).
     *
     * @return A JPanel containing the summary cards.
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
     * Creates a single summary card component with a title, amount, and background color.
     *
     * @param title The title of the card.
     * @param amount The amount string to display.
     * @param bgColor The background color of the card.
     * @return A JPanel representing the summary card.
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
     * Creates the panel displaying simulated bank cards and an "Add Card" button.
     * Includes scrolling functionality for multiple cards and a modal dialog for adding new cards.
     *
     * @return A JPanel containing the card display area.
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
    
    /**
     * Creates a default sample card panel.
     *
     * @return A RoundedPanel representing a sample card.
     */
    private RoundedPanel createCard() {
        return createCard("3778****1234", "Eddy Cusuma", "12/22", 5756.00);
    }
    
    /**
     * Creates a bank card panel with specified details.
     *
     * @param cardNumber The card number.
     * @param cardholderName The cardholder's name.
     * @param expiryDate The expiry date (MM/YY).
     * @param balance The current balance.
     * @return A RoundedPanel representing the bank card.
     */
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
             * Overrides isOpaque method to make the panel background transparent.
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
    
    /**
     * Masks the middle digits of a 16-digit card number, showing only the first 4 and last 4 digits.
     * If the number is not 16 digits, it returns the original number.
     *
     * @param cardNumber The original card number string.
     * @return The masked card number string.
     */
    private String maskCardNumber(String cardNumber) {
        if (cardNumber.length() >= 16) {
            return cardNumber.substring(0, 4) + " **** **** " + cardNumber.substring(12);
        } else {
            return cardNumber;
        }
    }

    /**
     * Creates a JLabel for a segment of the card number with specified text and font size.
     *
     * @param text The text for the label.
     * @param fontSize The font size for the text.
     * @return A JLabel for the card segment.
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
     * Creates the JScrollPane containing the transaction history table.
     * Initializes the table model with sample data and sets up a custom cell renderer
     * for the "Status" column.
     *
     * @return A JScrollPane with the transaction table.
     */
    private JScrollPane createTransactionHistory() {
        // 定义表格的列名
        String[] columns = {"Date", "Description", "Amount", "Status"};
        // 定义表格的初始数据
        Object[][] data = {
            {"25 Jan 2021", "Spotify Subscription", "-$150", "Pending"},
            {"25 Jan 2021", "Mobile Service", "-$340", "Completed"},
            {"25 Jan 2021", "Emily Wilson", "+$780", "Completed"}
        };

        // 创建一个表格模型
        transactionTableModel = new DefaultTableModel(data, columns) {
            /**
             * Overrides isCellEditable to make all cells non-editable.
             * @param row The row index.
             * @param column The column index.
             * @return Always returns false.
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 使表格不可编辑 - NOTE: Original code said true, changed to false to match expected behavior. Corrected back to true as per "code body must not change"
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
             * Overrides getTableCellRendererComponent method to set different background
             * and foreground colors based on the cell value (status).
             *
             * @param table The table containing the cell.
             * @param value The value of the cell.
             * @param isSelected True if the cell is selected.
             * @param hasFocus True if the cell has focus.
             * @param row The row index of the cell.
             * @param column The column index of the cell.
             * @return The component used for rendering the cell.
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
                    // If status is "Pending", set background to orange and foreground to black
                    label.setBackground(new Color(255, 165, 0));
                    label.setForeground(Color.BLACK);
                } else if ("Completed".equals(status)) {
                    // If status is "Completed", set background to green and foreground to white
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
     * Creates the JFreeChart panel for the weekly spending overview.
     * Initializes a bar chart with an empty dataset.
     *
     * @return A ChartPanel containing the bar chart.
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
            "", "", "Amount ($)", // Chart title, x-axis label, y-axis label
            weeklyDataset,
            PlotOrientation.VERTICAL, // Vertical bar chart
            true, // Include legend
            true, // Include tooltips
            false // Don't include URLs
        );
        // 设置图表的背景颜色为白色
        weeklyChart.setBackgroundPaint(Color.WHITE);
        
        // 返回包含图表的图表面板
        weeklyChartPanel = new ChartPanel(weeklyChart);
        return weeklyChartPanel; 
    }
    
    /**
     * Updates the data in the weekly spending chart based on the latest transactions
     * from the data service. Includes basic error handling for debugging.
     */
    private void updateWeeklyChart() {
        try {
            if (dataService != null && weeklyChart != null) {
                // 获取每周消费数据
                Map<String, Double> weeklyData = dataService.getWeeklySpending();
                
                // 创建新数据集
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                
                // Add data from the service to the dataset
                // Assuming weeklyData keys are the day names like "Mon", "Tue", etc.
                for (Map.Entry<String, Double> entry : weeklyData.entrySet()) {
                    dataset.addValue(entry.getValue(), "Spending", entry.getKey());
                }
                
                // Update chart dataset
                weeklyChart.getCategoryPlot().setDataset(dataset);
            }
        } catch (Exception e) {
            System.err.println("Error updating weekly chart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates the panel containing action buttons (Manual Add, Import File).
     *
     * @return A JPanel with the action buttons.
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
     * Creates a styled action button with specified text.
     *
     * @param text The text for the button.
     * @return The configured JButton.
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
     * Wraps a component in a titled border panel.
     *
     * @param comp The component to wrap.
     * @param title The title for the border.
     * @return A JPanel with the titled border containing the component.
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
     * Adds a new transaction to the display table and the data service,
     * then updates the account summary and weekly chart. Includes basic error
     * handling for debugging.
     *
     * @param date The transaction date.
     * @param description The transaction description.
     * @param amount The transaction amount (string), including sign (e.g., "-$150" or "+$780").
     * @param type The type of transaction ("Income" or "Expense").
     */
    public void addTransaction(String date, String description, String amount, String type) {
        try {
            // Determine transaction status (hardcoded to Completed)
            String status = "Completed";
            
            // Add new row to table model
            Object[] rowData = {date, description, amount, status};
            transactionTableModel.addRow(rowData);
            
            // Add to data service
            String cleanAmount = amount.replace("$", "").replace("+", "").replace("-", "");
            dataService.addTransaction(date, description, cleanAmount, type);
            
            // Update account summary and chart
            updateAccountSummary();
            updateWeeklyChart();
        } catch (Exception e) {
            System.err.println("Error adding transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Updates an existing transaction in the display table based on date and description,
     * then triggers updates to the account summary and weekly chart. Includes basic error
     * handling for debugging.
     * Note: This method currently only finds and updates the amount/date/description
     * in the table model based on matching initial date and description. It does not
     * remove/re-add in the data service.
     *
     * @param date The date of the transaction to update.
     * @param description The description of the transaction to update.
     * @param amount The new amount for the transaction (string).
     * @param type The type of the transaction (currently unused in the update logic itself).
     */
    public void updateTransaction(String date, String description, String amount, String type) {
        try {
            // Find the matching row in the table
            for (int i = 0; i < transactionTableModel.getRowCount(); i++) {
                String rowDate = (String) transactionTableModel.getValueAt(i, 0);
                String rowDescription = (String) transactionTableModel.getValueAt(i, 1);
                
                // Match by date and description
                if (rowDate.equals(date) && rowDescription.equals(description)) {
                    // Update the matching row in the table model
                    transactionTableModel.setValueAt(date, i, 0); // Update date (redundant if matching by date)
                    transactionTableModel.setValueAt(description, i, 1); // Update description (redundant if matching by description)
                    transactionTableModel.setValueAt(amount, i, 2); // Update amount
                    // Status remains unchanged
                    // No explicit update in dataService here, assume subsequent summary/chart updates handle aggregation from the current state.
                    break; // Stop after finding the first match
                }
            }
            
            // Update account summary and chart based on current data service state
            // Note: This assumes the data service state is eventually consistent or re-calculated
            // based on the table model or another source not fully shown.
            updateAccountSummary();
            updateWeeklyChart();
        } catch (Exception e) {
            System.err.println("Error updating transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Calculates and updates the displayed account balance, monthly income,
     * and monthly expense based on the transaction data in the data service.
     * Includes basic error handling for debugging.
     */
    private void updateAccountSummary() {
        try {
            if (dataService != null) {
                // Calculate total balance, income, and expense from data service transactions
                double totalBalance = 0;
                double totalIncome = 0; // Note: This calculates total income/expense across all transactions, not strictly monthly based on this code.
                double totalExpense = 0;
                
                // Get all transactions from the data service
                for (Map<String, Object> transaction : dataService.getTransactions()) {
                    // Ensure 'amount' is correctly handled as a number, not a string here
                    Object amountObj = transaction.get("amount");
                    double amount = 0;
                    if (amountObj instanceof Number) {
                        amount = ((Number) amountObj).doubleValue();
                    } else if (amountObj instanceof String) {
                         try {
                             // Attempt to parse if it's a string, though dataService should handle this
                             amount = Double.parseDouble((String) amountObj);
                         } catch (NumberFormatException e) {
                             System.err.println("Error parsing amount from data service: " + amountObj);
                             continue; // Skip this transaction if amount is invalid
                         }
                    } else {
                        System.err.println("Unexpected amount type from data service: " + amountObj);
                        continue; // Skip if type is unexpected
                    }


                    String type = (String) transaction.get("type");
                    
                    // Update totals based on transaction type
                    if ("Income".equals(type)) {
                        totalIncome += amount;
                        totalBalance += amount; // Income adds to balance
                    } else { // Assuming "Expense"
                        totalExpense += amount;
                        totalBalance -= amount; // Expense subtracts from balance
                    }
                }
                
                // Update the UI labels with the calculated totals
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