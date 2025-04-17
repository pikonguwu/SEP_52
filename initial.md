```java
import javax.swing.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.*;
import java.awt.event.*;

public class BuckBrainAI extends JFrame {
    private JPanel mainContentPanel;
    private JButton currentNavButton;
    private final Color SELECTED_COLOR = new Color(0, 122, 255);
    private final Color UNSELECTED_COLOR = Color.WHITE;

    public BuckBrainAI() {
        initUI();
    }

    private void initUI() {
        setTitle("BuckBrainAI");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(createTitleBar(), BorderLayout.NORTH);
        add(createNavigationPanel(), BorderLayout.WEST);
        initializeMainContent();
    }

    // 标题栏实现
    private JPanel createTitleBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 245, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("BuckBrainAI", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(SELECTED_COLOR);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.add(createIconButton("⚙", 24));
        rightPanel.add(createIconButton("👤", 24));

        panel.add(title, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    // 左侧导航面板
    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 1, 0, 10));
        panel.setPreferredSize(new Dimension(220, 600));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        String[] navItems = {"Dashboard", "Transactions", "Accounts", "Investments", "Credit Cards", "Settings"};
        for (String item : navItems) {
            JButton btn = createNavButton(item);
            panel.add(btn);
            if (item.equals("Dashboard")) setInitialSelection(btn);
        }
        return panel;
    }

    // 主内容区域初始化
    private void initializeMainContent() {
        mainContentPanel = new JPanel(new CardLayout());
        mainContentPanel.add(createDashboardView(), "Dashboard");
        mainContentPanel.add(createPlaceholderView("Transactions View"), "Transactions");
        mainContentPanel.add(createPlaceholderView("Accounts View"), "Accounts");
        mainContentPanel.add(createPlaceholderView("Investments View"), "Investments");
        mainContentPanel.add(createPlaceholderView("Credit Cards View"), "Credit Cards");
        mainContentPanel.add(createPlaceholderView("Settings View"), "Settings");
        add(mainContentPanel, BorderLayout.CENTER);
    }

    // Dashboard视图布局
    private JPanel createDashboardView() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 顶部Overview标题
        JLabel overviewLabel = new JLabel("Overview", SwingConstants.LEFT);
        overviewLabel.setFont(new Font("Arial", Font.BOLD, 22));
        overviewLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(overviewLabel, BorderLayout.NORTH);

        // 中间2x2内容网格
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        gridPanel.add(wrapInPanel(createCardPanel(), "My Cards"));
        gridPanel.add(wrapInPanel(createTransactionPanel(), "Recent Transactions"));
        gridPanel.add(wrapInPanel(createWeeklyChart(), "Weekly Activity"));
        gridPanel.add(wrapInPanel(createExpenseChart(), "Expense Statistics"));
        mainPanel.add(gridPanel, BorderLayout.CENTER);

        // 底部操作面板
        mainPanel.add(createActionPanel(), BorderLayout.SOUTH);
        return mainPanel;
    }

    // 卡片信息面板
    private JPanel createCardPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 0, 10));
        panel.setBackground(Color.WHITE);
        
        String[] cardInfo = {
            "Balance: $5,756", 
            "Cardholder: Eddy Cushuma",
            "Valid Thru: 12/2024"
        };
        
        for (String info : cardInfo) {
            JLabel label = new JLabel(info, SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            panel.add(label);
        }
        return panel;
    }

    // 交易记录面板
    private JScrollPane createTransactionPanel() {
        String[] columns = {"Date", "Description", "Amount"};
        Object[][] data = {
            {"2023-12-25", "Amazon Purchase", "-$189.00"},
            {"2023-12-24", "Salary Deposit", "+$5,200.00"},
            {"2023-12-23", "Utility Payment", "-$350.50"}
        };
        
        JTable table = new JTable(data, columns);
        table.setFillsViewportHeight(true);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        return new JScrollPane(table);
    }

    // 周活动柱状图
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

    // 支出分布饼图
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

    // 底部操作面板
    private JPanel createActionPanel() {
        // 创建一个新的JPanel，并设置布局管理器和边框
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Add transactions")); // 添加标题
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Add transactions"), // 标题边框
            BorderFactory.createEmptyBorder(20, 0, 0, 0) // 内边距
        ));
    
        // 添加“Manual Add”按钮
        JButton manualAddBtn = createActionButton("Manual Add", SELECTED_COLOR);
        manualAddBtn.addActionListener(e -> {
            System.out.println("Manual Add button clicked");
            // 在这里添加手动添加交易的逻辑
        });
        panel.add(manualAddBtn);
    
        // 添加“Import File”按钮
        JButton importFileBtn = createActionButton("Import File", new Color(40, 167, 69));
        importFileBtn.addActionListener(e -> {
            System.out.println("Import File button clicked");
            // 在这里添加导入文件的逻辑
        });
        panel.add(importFileBtn);
    
        return panel;
    }

    // 包装组件到带标题的面板
    private JPanel wrapInPanel(Component comp, String title) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createTitledBorder(title));
        wrapper.setBackground(Color.WHITE);
        wrapper.add(comp, BorderLayout.CENTER);
        return wrapper;
    }

    // 导航按钮创建
    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Arial", Font.PLAIN, 16));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btn.setBackground(UNSELECTED_COLOR);
        
        btn.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainContentPanel.getLayout();
            cl.show(mainContentPanel, text);
            updateNavSelection(btn);
        });
        
        return btn;
    }

    // 导航按钮状态更新
    private void updateNavSelection(JButton selected) {
        if (currentNavButton != null) {
            currentNavButton.setBackground(UNSELECTED_COLOR);
            currentNavButton.setForeground(Color.BLACK);
        }
        selected.setBackground(SELECTED_COLOR);
        selected.setForeground(Color.WHITE);
        currentNavButton = selected;
    }

    // 初始化选中状态
    private void setInitialSelection(JButton btn) {
        btn.setBackground(SELECTED_COLOR);
        btn.setForeground(Color.WHITE);
        currentNavButton = btn;
    }

    // 辅助方法：创建图标按钮
    private JButton createIconButton(String icon, int size) {
        JButton btn = new JButton(icon);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, size));
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return btn;
    }

    // 辅助方法：创建操作按钮
    private JButton createActionButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(8, 25, 8, 25)
        ));
        return btn;
    }

    // 辅助方法：占位视图
    private JPanel createPlaceholderView(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.ITALIC, 24));
        label.setForeground(new Color(150, 150, 150));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            BuckBrainAI app = new BuckBrainAI();
            app.setVisible(true);
            app.setLocationRelativeTo(null); // 窗口居中
        });
    }
}
```

