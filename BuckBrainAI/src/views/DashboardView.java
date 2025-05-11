package views;

import constants.AppConstants;
import components.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.w3c.dom.events.MouseEvent; // 此导入未使用，建议移除
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import java.awt.*;

/**
 * 定义 DashboardView 类，继承自 BaseView，用于展示仪表盘视图。
 * 该视图包含顶部标题、主内容区（包含卡片面板、交易表格、周活动图表和支出统计图表）以及底部操作栏。
 */
public class DashboardView extends BaseView {

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
    }

    /**
     * 创建卡片面板，模拟银行卡界面。
     * 
     * @return 包含银行卡信息的面板
     */
    private JPanel createCardPanel() {
        // 创建带圆角的卡片容器，重写 paintComponent 方法绘制蓝色背景
        RoundedPanel panel = new RoundedPanel(new BorderLayout()) {
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
        
        JLabel nameLabel = new JLabel("Eddy Cusuma"); // 注意："Cusuma" 可能是拼写错误
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
        Object[][] data = {
            {new ImageIcon("path/to/card_icon.png"), "28 January 2021", "Deposit from my Card", "-$850"},
            {new ImageIcon("path/to/paypal_icon.png"), "25 January 2021", "Deposit Paypal", "+$2,500"},
            {new ImageIcon("path/to/user_icon.png"), "21 January 2021", "Jemi Wilson", "+$5,400"} // 注意："Jemi" 可能是拼写错误
        };

        // 创建表格并重写 prepareRenderer 方法，设置图标列居中，其他列左对齐
        JTable table = new JTable(data, columns) {
            /**
             * 重写 prepareRenderer 方法，根据列索引设置单元格内容的对齐方式。
             * 
             * @param renderer 用于渲染单元格的渲染器
             * @param row 单元格所在的行
             * @param column 单元格所在的列
             * @return 渲染后的组件
             */
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (column == 0) {
                    // 图标列居中对齐
                    ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER); 
                } else {
                    // 其他列左对齐
                    ((JLabel) component).setHorizontalAlignment(SwingConstants.LEFT); 
                }
                return component;
            }
        };

        // 设置表格的行高为 35 像素
        table.setRowHeight(35); 
        // 设置表格表头的字体为 Arial 加粗，字号 14
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14)); 
        // 设置表格表头的首选大小为 100x35 像素
        table.getTableHeader().setPreferredSize(new Dimension(100, 35)); 

        // 隐藏第一列标题
        table.getColumnModel().getColumn(0).setHeaderValue("");

        // 设置各列宽度
        table.getColumnModel().getColumn(0).setPreferredWidth(40); // 图标列
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // 日期列
        table.getColumnModel().getColumn(2).setPreferredWidth(200); // 描述列
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // 金额列

        // 设置字体和颜色
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        // 图标列居中对齐
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER); 
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        // 日期和描述列左对齐
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT); 
        table.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        // 金额列右对齐
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT); 
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        // 设置滚动条策略，根据需要显示水平和垂直滚动条
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        return scrollPane;
    }

    /**
     * 创建周活动图表，显示一周内的消费金额柱状图。
     * 
     * @return 包含周活动图表的图表面板
     */
    private ChartPanel createWeeklyChart() {
        // 创建默认的分类数据集
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // 定义一周的日期
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        // 定义每天的消费金额
        int[] amounts = {650, 820, 720, 930, 1050, 1350, 980};
        
        // 将每天的消费金额添加到数据集中
        for (int i = 0; i < days.length; i++) {
            dataset.addValue(amounts[i], "Spending", days[i]); 
        }
        
        // 创建柱状图
        JFreeChart chart = ChartFactory.createBarChart(
            "", "", "Amount ($)", 
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );
        // 设置图表的背景颜色为白色
        chart.setBackgroundPaint(Color.WHITE); 
        // 返回包含图表的图表面板
        return new ChartPanel(chart); 
    }

    /**
     * 创建支出统计图表，显示各项支出的占比饼图。
     * 
     * @return 包含支出统计图表的图表面板
     */
    private ChartPanel createExpenseChart() {
        // 创建默认的饼图数据集
        DefaultPieDataset dataset = new DefaultPieDataset();
        // 添加住房支出数据
        dataset.setValue("Housing", 35); 
        // 添加食品支出数据
        dataset.setValue("Food", 25); 
        // 添加交通支出数据
        dataset.setValue("Transport", 15); 
        // 添加娱乐支出数据
        dataset.setValue("Entertainment", 10); 
        // 添加储蓄支出数据
        dataset.setValue("Savings", 15); 
        
        // 创建饼图
        JFreeChart chart = ChartFactory.createPieChart("", dataset, true, true, false);
        // 设置图表的背景颜色为白色
        chart.setBackgroundPaint(Color.WHITE); 
        // 返回包含图表的图表面板
        return new ChartPanel(chart); 
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
}