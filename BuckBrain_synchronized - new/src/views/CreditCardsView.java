package views;

import components.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * CreditCardsView 类继承自 BaseView，用于展示信用卡相关信息的视图界面。
 * 该视图包含信用卡列表、统计图表以及添加新信用卡的表单。
 */
public class CreditCardsView extends BaseView {
    /**
     * 饼图数据集，用于存储信用卡账户统计信息。
     */
    private DefaultPieDataset pieDataset;

    /**
     * 获取视图的名称，用于在界面切换时标识该视图。
     * 
     * @return 视图的名称，固定为 "Credit Cards"
     */
    @Override
    public String getViewName() {
        return "Credit Cards";
    }

    /**
     * 初始化用户界面的方法，设置布局、添加标题、主内容区和添加卡片表单。
     */
    @Override
    protected void initUI() {
        // 设置布局管理器，组件间水平和垂直间距为 15 像素
        setLayout(new BorderLayout(15, 15));
        // 设置面板的内边距，上、左、下、右均为 20 像素
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 顶部标题
        // 创建标题标签，文本为 "My Cards"，左对齐
        JLabel titleLabel = new JLabel("My Cards", SwingConstants.LEFT);
        // 设置标题字体为 Arial 加粗，字号 24
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        // 将标题标签添加到面板的北部位置
        add(titleLabel, BorderLayout.NORTH);

        // 主内容区
        // 创建一个使用边界布局的面板，组件间水平和垂直间距为 15 像素
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));

        // 卡片列表和统计
        // 创建一个使用 1x2 网格布局的面板，组件间水平和垂直间距为 15 像素
        JPanel upperPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        // 添加信用卡列表面板
        upperPanel.add(createCardListPanel());
        // 添加统计信息面板
        upperPanel.add(createStatisticsPanel());

        // 添加卡片表单
        // 将包含卡片列表和统计信息的面板添加到主面板的中心位置
        mainPanel.add(upperPanel, BorderLayout.CENTER);
        // 将添加卡片表单添加到主面板的南部位置
        mainPanel.add(createAddCardForm(), BorderLayout.SOUTH);

        // 将主面板添加到当前视图的中心位置
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * 创建信用卡列表面板，包含一个表格显示信用卡信息。
     * 
     * @return 包含信用卡列表表格的面板
     */
    private JPanel createCardListPanel() {
        // 创建一个圆角面板，使用边界布局
        RoundedPanel panel = new RoundedPanel(new BorderLayout());
        // 为面板设置一个标题边框，标题为 "Card List"
        panel.setBorder(new TitledBorder("Card List"));

        // 定义表格的列名
        String[] columns = {"Card Type", "Bank", "Card Number", "Name", "Actions"};
        // 定义表格的初始数据
        Object[][] data = {
            {"Secondary", "DBL Bank", "**** 5600", "William", "View Details"},
            {"Secondary", "BRC Bank", "**** 7560", "Michel", "View Details"},
            {"Classic", "ABM Bank", "**** 1234", "Edward", "View Details"}
        };

        // 创建一个表格，并重写 getColumnClass 方法，指定第 4 列的类型为 JButton
        JTable table = new JTable(data, columns) {
            /**
             * 获取指定列的数据类型。
             * 
             * @param column 列索引
             * @return 列的数据类型，如果是第 4 列则返回 JButton 类，否则返回 Object 类
             */
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 4 ? JButton.class : Object.class;
            }
        };

        // 设置表格的行高为 40 像素
        table.setRowHeight(40);
        // 设置表格表头的字体为 Arial 加粗，字号 14
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        // 不显示表格的网格线
        table.setShowGrid(false);

        // 获取表格的第 4 列
        TableColumn actionColumn = table.getColumnModel().getColumn(4);
        // 为该列设置自定义的单元格渲染器
        actionColumn.setCellRenderer(new ButtonRenderer());
        // 为该列设置自定义的单元格编辑器
        actionColumn.setCellEditor(new ButtonEditor(new JCheckBox()));

        // 创建一个滚动面板，将表格添加到其中
        JScrollPane scrollPane = new JScrollPane(table);
        // 去除滚动面板的边框
        scrollPane.setBorder(null);
        // 将滚动面板添加到圆角面板的中心位置
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 创建统计信息面板，包含一个饼图显示信用卡账户统计信息。
     * 
     * @return 包含饼图的统计信息面板
     */
    private JPanel createStatisticsPanel() {
        // 创建一个圆角面板，使用边界布局
        RoundedPanel panel = new RoundedPanel(new BorderLayout());
        // 为面板设置一个标题边框，标题为 "Card Account Statistics"
        panel.setBorder(new TitledBorder("Card Account Statistics"));

        // 初始化饼图数据集
        pieDataset = new DefaultPieDataset();
        // 向数据集中添加 DBL Bank 的数据
        pieDataset.setValue("DBL Bank", 35);
        // 向数据集中添加 BRC Bank 的数据
        pieDataset.setValue("BRC Bank", 25);
        // 向数据集中添加 ABM Bank 的数据
        pieDataset.setValue("ABM Bank", 20);
        // 向数据集中添加 MCP Bank 的数据
        pieDataset.setValue("MCP Bank", 20);

        // 创建一个饼图，不设置标题，使用指定的数据集，显示图例和工具提示
        JFreeChart chart = ChartFactory.createPieChart(
            "", pieDataset, true, true, false
        );

        // 获取饼图的绘图区域
        PiePlot plot = (PiePlot) chart.getPlot();

        // 使用正确的方式设置颜色
        int index = 0;
        // 遍历数据集中的键
        for (Object key : pieDataset.getKeys()) {
            switch (index) {
                case 0:
                    // 设置第 0 个扇区的颜色
                    plot.setSectionPaint(index, new Color(40, 80, 150));
                    break;
                case 1:
                    // 设置第 1 个扇区的颜色
                    plot.setSectionPaint(index, new Color(80, 120, 200));
                    break;
                case 2:
                    // 设置第 2 个扇区的颜色
                    plot.setSectionPaint(index, new Color(120, 160, 220));
                    break;
                case 3:
                    // 设置第 3 个扇区的颜色
                    plot.setSectionPaint(index, new Color(160, 200, 240));
                    break;
            }
            index++;
        }

        // 设置绘图区域的背景颜色为 null
        plot.setBackgroundPaint(null);
        // 设置绘图区域的轮廓颜色为 null
        plot.setOutlinePaint(null);

        // 创建一个图表面板，将饼图添加到其中
        ChartPanel chartPanel = new ChartPanel(chart);
        // 将图表面板添加到圆角面板的中心位置
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 创建添加新信用卡的表单面板。
     * 
     * @return 包含添加新信用卡表单的面板
     */
    private JPanel createAddCardForm() {
        // 创建一个圆角面板
        RoundedPanel panel = new RoundedPanel();
        // 为面板设置一个标题边框，标题为 "Add New Card"
        panel.setBorder(new TitledBorder("Add New Card"));
        // 设置面板的布局为 2x1 的网格布局，组件间水平和垂直间距为 15 像素
        panel.setLayout(new GridLayout(2, 1, 15, 15));

        // 创建一个使用 2x4 网格布局的面板，用于放置表单字段
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 15, 15));

        // 创建一个下拉框，包含信用卡类型选项
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Classic", "Premium", "Platinum"});
        // 创建一个文本框，用于输入持卡人姓名
        JTextField nameField = new JTextField();
        // 创建一个文本框，用于输入信用卡号
        JTextField numberField = new JTextField();
        // 创建一个文本框，用于输入信用卡有效期
        JTextField expiryField = new JTextField();

        // 将信用卡类型字段添加到表单面板
        formPanel.add(createFormField("Card Type:", typeCombo));
        // 将持卡人姓名字段添加到表单面板
        formPanel.add(createFormField("Name On Card:", nameField));
        // 将信用卡号字段添加到表单面板
        formPanel.add(createFormField("Card Number:", numberField));
        // 将信用卡有效期字段添加到表单面板
        formPanel.add(createFormField("Expiration Date:", expiryField));

        // 创建一个圆角按钮，文本为 "Add Card"
        RoundedButton addButton = new RoundedButton("Add Card");
        // 设置按钮字体为 Arial 加粗，字号 16
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        // 设置按钮背景颜色
        addButton.setBackground(new Color(40, 80, 150));
        // 设置按钮文字颜色为白色
        addButton.setForeground(Color.WHITE);
        // 为按钮添加点击事件监听器，点击时调用 handleAddCard 方法
        addButton.addActionListener(e -> handleAddCard());

        // 将表单面板添加到圆角面板
        panel.add(formPanel);
        // 将添加按钮添加到圆角面板
        panel.add(addButton);

        return panel;
    }

    /**
     * 创建表单字段面板，包含标签和对应的输入组件。
     * 
     * @param label 字段标签文本
     * @param field 字段对应的输入组件
     * @return 包含标签和输入组件的面板
     */
    private JPanel createFormField(String label, Component field) {
        // 创建一个使用边界布局的面板，组件间水平和垂直间距为 10 像素
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        // 创建一个标签，显示指定的文本
        JLabel jLabel = new JLabel(label);
        // 设置标签字体为 Arial 普通样式，字号 14
        jLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // 如果输入组件是文本框，为其设置一个浅灰色的边框
        if(field instanceof JTextField) {
            ((JTextField) field).setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        }

        // 将标签添加到面板的西部位置
        panel.add(jLabel, BorderLayout.WEST);
        // 将输入组件添加到面板的中心位置
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    /**
     * 处理添加信用卡的逻辑，显示一个消息对话框提示添加成功。
     */
    private void handleAddCard() {
        // 显示一个消息对话框，提示信用卡添加成功
        JOptionPane.showMessageDialog(this, "Card Added Successfully!");
    }

    /**
     * 自定义的表格单元格渲染器，用于渲染表格中的按钮列。
     */
    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        /**
         * 构造方法，设置按钮背景可见。
         */
        public ButtonRenderer() {
            setOpaque(true);
        }

        /**
         * 获取用于渲染表格单元格的组件。
         * 
         * @param table 包含该单元格的表格
         * @param value 单元格的值
         * @param isSelected 单元格是否被选中
         * @param hasFocus 单元格是否有焦点
         * @param row 单元格所在的行
         * @param column 单元格所在的列
         * @return 用于渲染该单元格的组件，即当前按钮
         */
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            // 设置按钮的文本为单元格的值
            setText(value.toString());
            // 设置按钮的背景颜色
            setBackground(new Color(40, 80, 150));
            // 设置按钮的文字颜色为白色
            setForeground(Color.WHITE);
            // 不绘制按钮的边框
            setBorderPainted(false);
            return this;
        }
    }

    /**
     * 自定义的表格单元格编辑器，用于编辑表格中的按钮列。
     */
    private static class ButtonEditor extends DefaultCellEditor {
        /**
         * 用于编辑的按钮组件
         */
        private JButton button;
        /**
         * 按钮显示的文本
         */
        private String label;

        /**
         * 构造方法，初始化按钮并添加点击事件监听器。
         * 
         * @param checkBox 用于初始化父类的复选框
         */
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            // 创建一个按钮
            button = new JButton();
            // 设置按钮背景可见
            button.setOpaque(true);
            // 为按钮添加点击事件监听器，点击时触发编辑停止事件
            button.addActionListener(e -> fireEditingStopped());
        }

        /**
         * 获取用于编辑表格单元格的组件。
         * 
         * @param table 包含该单元格的表格
         * @param value 单元格的值
         * @param isSelected 单元格是否被选中
         * @param row 单元格所在的行
         * @param column 单元格所在的列
         * @return 用于编辑该单元格的组件，即按钮
         */
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            // 获取单元格的值作为按钮的文本
            label = (value == null) ? "" : value.toString();
            // 设置按钮的文本
            button.setText(label);
            return button;
        }

        /**
         * 获取编辑后的单元格值。
         * 
         * @return 编辑后的单元格值，即按钮的文本
         */
        public Object getCellEditorValue() {
            return label;
        }
    }
}