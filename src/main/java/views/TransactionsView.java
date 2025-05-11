package views;

import constants.AppConstants;
import components.*;
import services.TransactionDataService;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Map;

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

    // 添加数据服务和图表相关字段
    private TransactionDataService dataService;
    private JFreeChart expenseChart;
    private ChartPanel expenseChartPanel;

    /**
     * 构造函数
     */
    public TransactionsView() {
        // 初始化数据服务
        dataService = new TransactionDataService();
    }

    /**
     * 初始化用户界面，设置布局、添加标题、卡片面板、费用图表和交易表格。
     */
    @Override
    public void initUI() {
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

        // 创建主内容面板，使用 1 行 2 列的网格布局，组件间水平和垂直间距为 15 像素
        RoundedPanel gridPanel = new RoundedPanel(new GridLayout(1, 2, 15, 15));
        // 添加卡片面板并包装上标题
        gridPanel.add(wrapComponent(createCardPanel(), "My Cards"));
        // 添加费用图表并包装上标题
        gridPanel.add(wrapComponent(createExpenseChart(), "My Expenses"));
        // 将主内容面板添加到面板的中心位置
        add(gridPanel, BorderLayout.CENTER);

        // 初始化表格模型
        initializeTableModels();

        // 将交易表格面板添加到面板的南部位置
        add(createTransactionPanel(), BorderLayout.SOUTH);

        // 加载初始交易数据到数据服务
        loadInitialTransactions();
    }

    /**
     * 加载初始交易数据到数据服务
     */
    private void loadInitialTransactions() {
        try {
            // 从表格中获取已有的交易数据
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String description = (String) tableModel.getValueAt(i, 0);
                String date = (String) tableModel.getValueAt(i, 4);
                String amount = (String) tableModel.getValueAt(i, 5);
                String type = amount.startsWith("-") ? "Expense" : "Income";

                // 清理金额字符串
                String cleanAmount = amount.replace("$", "").replace("+", "").replace("-", "");

                // 添加到数据服务
                dataService.addTransaction(date, description, cleanAmount, type);
            }

            // 首次更新图表
            updateExpenseChart();
        } catch (Exception e) {
            // 添加异常处理以便更好地诊断问题
            System.err.println("Error in loadInitialTransactions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 初始化所有表格模型
     */
    private void initializeTableModels() {
        // 定义表格的列名
        String[] columnNames = { "Description", "Transaction ID", "Type", "Card", "Date", "Amount" };

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
                { "Spotify Subscription", "123456", "Shopping", "1234******", "28 Jan, 12.30 AM", "-$2,500" },
                { "Freepik Sales", "789012", "Transfer", "5678******", "25 Jan, 10.40 PM", "+$750" },
                { "Mobile Service", "345678", "Service", "9012******", "20 Jan, 10.40 PM", "-$150" },
                { "Wilson", "901234", "Transfer", "3456******", "15 Jan, 03.29 PM", "-$1,050" },
                { "Emily", "567890", "Transfer", "7890******", "14 Jan, 10.40 PM", "+$840" }
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
        prevButton.addActionListener(_e -> {
            // 上一页逻辑
            JOptionPane.showMessageDialog(null, "上一页");
        });
        nextButton.addActionListener(_e -> {
            // 下一页逻辑
            JOptionPane.showMessageDialog(null, "下一页");
        });

        // 设置主面板的首选大小为 800x350 像素
        mainPanel.setPreferredSize(new Dimension(800, 350));

        return mainPanel;
    }

    /**
     * 创建卡片面板，模拟银行卡界面，包含余额、卡号、持卡人信息和有效期信息。
     *
     * @return 包含银行卡信息的面板
     */
    private JPanel createCardPanel() {
        // 创建带圆角的面板，重写 paintComponent 方法绘制蓝色背景
        RoundedPanel panel = new RoundedPanel(new GridLayout()) {
            /**
             * 重写 paintComponent 方法，绘制纯蓝色的圆角矩形背景。
             *
             * @param g 用于绘制的 Graphics 对象
             */
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
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        // 设置卡片面板的首选大小为 320x200 像素
        panel.setPreferredSize(new Dimension(320, 200));

        // 主内容容器，使用网格包布局实现精确布局，背景设置为透明
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

        // 余额区域
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

        // 卡号区域
        JPanel numberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        // 设置卡号面板背景透明
        numberPanel.setOpaque(false);
        // 添加卡号分段标签
        numberPanel.add(createCardSegment("3778", 22));
        numberPanel.add(createCardSegment("****", 18));
        numberPanel.add(createCardSegment("****", 18));
        numberPanel.add(createCardSegment("1234", 22));

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

        // 将内容面板添加到卡片面板的中心位置
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

        // 初始化为空数据集，稍后会在updateExpenseChart中更新
        String[] categories = { "Housing", "Food", "Transport", "Entertainment", "Savings", "Others" };
        for (String category : categories) {
            dataset.addValue(0, "Expenses", category);
        }

        // 创建 3D 柱状图
        expenseChart = ChartFactory.createBarChart3D(
                "", // 图表标题
                "Category", // 分类轴标签
                "Amount ($)", // 数值轴标签
                dataset, // 数据集
                PlotOrientation.VERTICAL, // 图表方向
                true, // 是否显示图例
                true, // 是否生成工具提示
                false // 是否生成网址链接
        );

        // 自定义图表样式
        expenseChart.setBackgroundPaint(Color.WHITE);

        // 创建图表面板
        expenseChartPanel = new ChartPanel(expenseChart);
        expenseChartPanel.setPreferredSize(new Dimension(400, 300));

        return expenseChartPanel;
    }

    /**
     * 更新费用图表
     */
    private void updateExpenseChart() {
        try {
            if (dataService != null && expenseChart != null) {
                // 获取各类支出数据
                Map<String, Double> categoryData = dataService.getExpenseCategories();

                // 创建新的数据集
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                // 添加数据
                for (Map.Entry<String, Double> entry : categoryData.entrySet()) {
                    dataset.addValue(entry.getValue(), "Expenses", entry.getKey());
                }

                // 如果没有数据，添加默认分类
                if (categoryData.isEmpty()) {
                    dataset.addValue(0, "Expenses", "No Data");
                }

                // 更新图表数据集
                expenseChart.getCategoryPlot().setDataset(dataset);
            }
        } catch (Exception e) {
            System.err.println("Error updating expense chart: " + e.getMessage());
            e.printStackTrace();
        }
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

    /**
     * 添加交易记录
     * 
     * @param date        交易日期
     * @param description 交易描述
     * @param amount      交易金额
     * @param type        交易类型
     */
    public void addTransaction(String date, String description, String amount, String type) {
        try {
            // 生成随机交易ID
            String transactionId = String.format("%06d", (int) (Math.random() * 1000000));
            // 生成随机卡号
            String cardNumber = String.format("%04d******", (int) (Math.random() * 10000));

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

            // 添加到数据服务
            String cleanAmount = amount.replace("$", "").replace("+", "").replace("-", "");
            dataService.addTransaction(date, description, cleanAmount, type);

            // 更新图表
            updateExpenseChart();
        } catch (Exception e) {
            System.err.println("Error adding transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 删除交易记录
     * 
     * @param date        交易日期
     * @param description 交易描述
     * @param amount      交易金额
     * @param type        交易类型
     */
    public void removeTransaction(String date, String description, String amount, String type) {
        try {
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

            // 此处应该更新数据服务中的数据
            // 但TransactionDataService当前不支持删除单个交易
            // 因此需要重新构建所有交易数据

            // 更新图表
            updateExpenseChart();
        } catch (Exception e) {
            System.err.println("Error removing transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }
}