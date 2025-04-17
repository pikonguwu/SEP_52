/**
 * 导入所需的包和类
 */
import constants.AppConstants;
import views.*;
import components.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 主应用程序类，继承自JFrame，用于创建和管理BuckBrainAI应用的主窗口。
 */
public class BuckBrainAI extends JFrame {
    /**
     * 主内容面板，用于显示不同的视图，采用CardLayout布局管理视图切换。
     */
    private JPanel mainContentPanel;
    /**
     * 当前选中的导航按钮，用于标记导航栏中当前处于选中状态的按钮。
     */
    private JButton currentNavButton;

    /**
     * 构造函数，初始化UI界面。
     */
    public BuckBrainAI() {
        initUI();
    }

    /**
     * 初始化用户界面的方法，设置窗口的基本属性，添加标题栏、导航面板和主内容面板，并将窗口居中显示。
     */
    private void initUI() {
        setTitle("BuckBrainAI"); // 设置窗口标题
        setSize(1200, 800); // 设置窗口大小
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 设置关闭操作
        setLayout(new BorderLayout(10, 10)); // 设置布局为BorderLayout，并指定间距

        add(createTitleBar(), BorderLayout.NORTH); // 添加标题栏到窗口顶部
        add(createNavigationPanel(), BorderLayout.WEST); // 添加导航面板到窗口左侧
        initializeMainContent(); // 初始化主内容面板
        setLocationRelativeTo(null); // 将窗口居中显示
    }

    /**
     * 创建标题栏的方法，返回一个包含标题和右侧操作按钮的圆角面板。
     * 
     * @return 标题栏面板
     */
    private JPanel createTitleBar() {
        RoundedPanel panel = new RoundedPanel(new BorderLayout()); // 创建圆角面板
        panel.setBackground(AppConstants.BACKGROUND_COLOR); // 设置背景颜色
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // 设置内边距

        JLabel title = new JLabel("BuckBrainAI", SwingConstants.LEFT); // 创建标题标签
        title.setFont(AppConstants.TITLE_FONT); // 设置字体
        title.setForeground(AppConstants.PRIMARY_COLOR); // 设置文字颜色

        RoundedPanel rightPanel = new RoundedPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0)); // 创建右侧面板
        rightPanel.add(createIconButton("⚙")); // 添加设置按钮
        rightPanel.add(createIconButton("👤")); // 添加用户按钮

        panel.add(title, BorderLayout.WEST); // 将标题添加到面板左侧
        panel.add(rightPanel, BorderLayout.EAST); // 将右侧面板添加到面板右侧
        return panel;
    }

    /**
     * 创建导航面板的方法，返回一个包含多个导航按钮的圆角面板。
     * 默认选中“Dashboard”按钮。
     * 
     * @return 导航面板
     */
    private JPanel createNavigationPanel() {
        RoundedPanel panel = new RoundedPanel(new GridLayout(7, 1, 0, 10)); // 创建圆角面板，使用GridLayout布局
        panel.setPreferredSize(new Dimension(220, 600)); // 设置面板大小
        panel.setBackground(AppConstants.BACKGROUND_COLOR); // 设置背景颜色
        panel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10)); // 设置内边距

        String[] navItems = {"Dashboard", "Transactions", "Accounts", "Analysis", "Credit Cards", "BuckBrainAI Chat", "Settings"}; // 导航项
        for (String item : navItems) {
            JButton btn = createNavButton(item); // 创建导航按钮
            panel.add(btn); // 将按钮添加到面板
            if (item.equals("Dashboard")) {
                btn.setBackground(AppConstants.PRIMARY_COLOR); // 设置默认选中的按钮背景颜色
                btn.setForeground(Color.WHITE); // 设置默认选中的按钮文字颜色
                currentNavButton = btn; // 记录当前选中的按钮
            }
        }
        return panel;
    }

    /**
     * 初始化主内容面板的方法，添加多个视图到主内容面板，并将主内容面板添加到窗口中心。
     */
    private void initializeMainContent() {
        mainContentPanel = new RoundedPanel(new CardLayout()); // 创建圆角面板，使用CardLayout布局
        
        mainContentPanel.add(new DashboardView(), "Dashboard"); // 添加仪表盘视图
        mainContentPanel.add(new TransactionsView(), "Transactions"); // 添加交易视图
        mainContentPanel.add(new AccountsView(), "Accounts"); // 添加账户视图
        mainContentPanel.add(new InvestmentsView(), "Analysis"); // 添加投资视图
        mainContentPanel.add(new CreditCardsView(), "Credit Cards"); // 添加信用卡视图
        mainContentPanel.add(new BucksBrainAIChatView(), "BuckBrainAI Chat"); // 添加聊天视图
        mainContentPanel.add(new SettingsView(), "Settings"); // 添加设置视图
        // 添加其他视图...

        add(mainContentPanel, BorderLayout.CENTER); // 将主内容面板添加到窗口中心
    }

    /**
     * 创建导航按钮的方法，返回一个带有点击事件的圆角按钮。
     * 点击按钮时，更新当前选中的按钮状态，并显示对应的视图。
     * 
     * @param text 按钮上显示的文本
     * @return 导航按钮
     */
    private JButton createNavButton(String text) {
        RoundedButton btn = new RoundedButton(text); // 创建圆角按钮
        btn.setHorizontalAlignment(SwingConstants.LEFT); // 设置文字左对齐
        btn.setFont(AppConstants.BUTTON_FONT); // 设置字体
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20)); // 设置内边距
        btn.setBackground(Color.WHITE); // 设置背景颜色
        
        btn.addActionListener(e -> {
            currentNavButton.setBackground(Color.WHITE); // 重置当前按钮的背景颜色
            currentNavButton.setForeground(Color.BLACK); // 重置当前按钮的文字颜色
            
            btn.setBackground(AppConstants.PRIMARY_COLOR); // 设置选中按钮的背景颜色
            btn.setForeground(Color.WHITE); // 设置选中按钮的文字颜色
            currentNavButton = btn; // 更新当前选中的按钮
            
            CardLayout cl = (CardLayout) mainContentPanel.getLayout(); // 获取CardLayout
            cl.show(mainContentPanel, text); // 显示对应的视图
        });
        return btn;
    }

    /**
     * 创建图标按钮的方法，返回一个带有特定图标的圆角按钮。
     * 根据图标的不同，点击按钮时显示对应的视图并更新导航按钮选中状态。
     * 
     * @param icon 按钮上显示的图标
     * @return 图标按钮
     */
    private JButton createIconButton(String icon) {
        RoundedButton btn = new RoundedButton(icon); // 创建圆角按钮
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24)); // 设置字体
        btn.setContentAreaFilled(false); // 取消默认填充
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // 设置内边距
        if (icon.equals("⚙")) {
            btn.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContentPanel.getLayout(); // 获取CardLayout
                cl.show(mainContentPanel, "Settings"); // 显示设置视图
                updateNavSelection(findNavButton("Settings")); // 更新导航按钮选中状态
            });
        } else if (icon.equals("👤")) {
            btn.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContentPanel.getLayout(); // 获取CardLayout
                cl.show(mainContentPanel, "Accounts"); // 显示账户视图
                updateNavSelection(findNavButton("Accounts")); // 更新导航按钮选中状态
            });
        }
        return btn;
    }

    /**
     * 查找导航按钮的辅助方法，根据按钮文本查找导航面板中的按钮。
     * 
     * @param text 要查找的按钮文本
     * @return 匹配的按钮，如果未找到则返回null
     */
    private JButton findNavButton(String text) {
        Component[] components = ((JPanel)getContentPane().getComponent(1)).getComponents(); // 获取导航面板的所有组件
        for (Component comp : components) {
            if (comp instanceof JButton && ((JButton)comp).getText().equals(text)) {
                return (JButton) comp; // 返回匹配的按钮
            }
        }
        return null;
    }

    /**
     * 更新导航按钮选中状态的方法，将当前选中的按钮重置为默认状态，
     * 并将指定的按钮设置为选中状态。
     * 
     * @param selectedButton 要设置为选中状态的按钮
     */
    private void updateNavSelection(JButton selectedButton) {
        if (currentNavButton != null) {
            currentNavButton.setBackground(Color.WHITE); // 重置当前按钮的背景颜色
            currentNavButton.setForeground(Color.BLACK); // 重置当前按钮的文字颜色
        }
        selectedButton.setBackground(AppConstants.PRIMARY_COLOR); // 设置选中按钮的背景颜色
        selectedButton.setForeground(Color.WHITE); // 设置选中按钮的文字颜色
        currentNavButton = selectedButton; // 更新当前选中的按钮
    }

    /**
     * 主方法，启动应用程序。使用EventQueue.invokeLater确保在事件调度线程中创建和显示窗口。
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new BuckBrainAI().setVisible(true); // 创建并显示主窗口
        });
    }
}