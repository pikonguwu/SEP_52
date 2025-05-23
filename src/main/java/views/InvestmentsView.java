package views;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.xy.*;

import components.RoundedPanel;

import javax.swing.*;
import java.awt.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer; // 新增
import org.jfree.chart.title.TextTitle; // 新增
import java.awt.geom.Ellipse2D; // 新增
import Analysis.InvestmentAnalysisService;
/**
 * A view class extending {@link BaseView} used to display investment-related information.
 * This view includes multiple charts showing annual and monthly income and expense trends,
 * and a bottom analysis panel.
 */
public class InvestmentsView extends BaseView {
    private InvestmentAnalysisService analysisService;

    /**
     * {@inheritDoc}
     * Returns the name of this view.
     *
     * @return The view name "Investments".
     */
    @Override
    public String getViewName() {
        return "Investments";
    }

    /**
     * {@inheritDoc}
     * Initializes the user interface components for the Investments view.
     * Sets up the layout, creates chart panels and the analysis panel.
     */
    @Override
    protected void initUI() {
        analysisService = new InvestmentAnalysisService();

        setLayout(new BorderLayout(15, 15));

        // 主图表面板（2行2列）
        RoundedPanel chartPanel = new RoundedPanel(new GridLayout(2, 2, 20, 20));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 创建带边框的图表板块
        chartPanel.add(createChartSection(createAnnualChart("Income Trend", true), "Yearly Income"));
        chartPanel.add(createChartSection(createAnnualChart("Expense Trend", false), "Yearly Expense"));
        chartPanel.add(createChartSection(createMonthlyChart("Income Trend"), "Monthly Income"));
        chartPanel.add(createChartSection(createMonthlyChart("Expense Trend"), "Monthly Expense"));

        // 底部分析面板
        JPanel analysisPanel = createAnalysisPanel();

        // 组合布局
        add(chartPanel, BorderLayout.CENTER);
        add(analysisPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates a panel section containing a title label and a chart.
     * This panel has rounded corners and a specific style.
     *
     * @param chart The ChartPanel component to display.
     * @param title The title for the chart section.
     * @return A JPanel containing the title and the chart.
     */
    private JPanel createChartSection(ChartPanel chart, String title) {
        // 创建一个圆角面板，使用边界布局
        RoundedPanel panel = new RoundedPanel(new BorderLayout());
        // 设置面板的背景颜色为白色
        panel.setBackground(Color.WHITE);

        // 标题标签
        // 创建一个标签，显示图表板块的标题，居中对齐
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        // 设置标题标签的字体为微软雅黑加粗，字号 16
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        // 设置标题标签的上下内边距为 10 像素
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        // 设置标题标签的文字颜色为深灰色
        titleLabel.setForeground(new Color(51, 51, 51));

        // 图表容器
        // 创建一个使用边界布局的面板，用于容纳图表
        JPanel chartContainer = new JPanel(new BorderLayout());
        // 设置图表容器的边框，包含一个浅灰色的线条边框和内边距
        chartContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 15, 15, 15)
        ));
        // 将图表面板添加到图表容器的中心位置
        chartContainer.add(chart, BorderLayout.CENTER);

        // 将标题标签添加到圆角面板的北部位置
        panel.add(titleLabel, BorderLayout.NORTH);
        // 将图表容器添加到圆角面板的中心位置
        panel.add(chartContainer, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates an XY line chart displaying annual income or expense data.
     * The data is currently hardcoded sample data.
     *
     * @param title The title of the chart.
     * @param isIncome If {@code true}, styles the chart for income (blue); if {@code false}, styles for expense (red).
     * @return A ChartPanel containing the annual chart.
     */
    private ChartPanel createAnnualChart(String title, boolean isIncome) {
        // 创建一个 XY 系列，用于存储数据点
        XYSeries series = new XYSeries("Value");
        // 添加 2020 年的数据点
        series.add(2020, 0);
        // 添加 2021 年的数据点
        series.add(2021, 10000);
        // 添加 2022 年的数据点
        series.add(2022, 20000);
        // 添加 2023 年的数据点
        series.add(2023, 30000);
        // 添加 2024 年的数据点
        series.add(2024, 40000);

        // 创建一个 XY 数据集，将 XY 系列添加到数据集中
        XYDataset dataset = new XYSeriesCollection(series);
        // 创建一个 XY 折线图，设置标题、坐标轴标签和数据集等信息
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "Year",
                "Amount ($)",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        // 对图表进行样式优化，根据是否为收入图表设置不同的颜色
        styleChart(chart, isIncome ? new Color(0, 122, 255) : new Color(255, 61, 61));
        // 创建一个图表面板，将图表添加到面板中并返回
        return new ChartPanel(chart);
    }

    /**
     * Creates an XY line chart displaying monthly income or expense data.
     * The data is currently randomly generated sample data.
     *
     * @param title The title of the chart.
     * @return A ChartPanel containing the monthly chart.
     */
    private ChartPanel createMonthlyChart(String title) {
        // 创建一个 XY 系列，用于存储数据点
        XYSeries series = new XYSeries("Value");
        // 循环生成 1 到 12 月的随机数据点
        for (int i = 1; i <= 12; i++) {
            series.add(i, (Math.random() * 4000));
        }

        // 创建一个 XY 数据集，将 XY 系列添加到数据集中
        XYDataset dataset = new XYSeriesCollection(series);
        // 创建一个 XY 折线图，设置标题、坐标轴标签和数据集等信息
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "Month",
                "Amount ($)",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        // 对图表进行样式优化，使用绿色
        styleChart(chart, new Color(76, 175, 80));
        // 创建一个图表面板，将图表添加到面板中并返回
        return new ChartPanel(chart);
    }

    /**
     * Applies common style settings to a JFreeChart, including background,
     * gridlines, data line appearance, title font, and axis tick label fonts.
     *
     * @param chart The JFreeChart to style.
     * @param color The color to use for the data line and data points.
     */
    private void styleChart(JFreeChart chart, Color color) {
        // 获取图表的 XY 绘图区域
        XYPlot plot = chart.getXYPlot();

        // 背景设置
        // 设置绘图区域的背景颜色为浅灰色
        plot.setBackgroundPaint(new Color(245, 245, 245));
        // 设置 X 轴网格线的颜色为浅灰色
        plot.setDomainGridlinePaint(new Color(220, 220, 220));
        // 设置 Y 轴网格线的颜色为浅灰色
        plot.setRangeGridlinePaint(new Color(220, 220, 220));

        // 数据线样式
        // 获取绘图区域的渲染器
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        // 设置数据线的颜色
        renderer.setSeriesPaint(0, color);
        // 设置数据线的宽度
        renderer.setSeriesStroke(0, new BasicStroke(2.5f));
        // 设置数据点的形状为椭圆形
        renderer.setSeriesShape(0, new Ellipse2D.Double(-3, -3, 6, 6));

        // 标题样式
        // 获取图表的标题
        TextTitle chartTitle = chart.getTitle();
        // 设置标题的字体为微软雅黑加粗，字号 14
        chartTitle.setFont(new Font("微软雅黑", Font.BOLD, 14));
        // 设置标题的文字颜色为深灰色
        chartTitle.setPaint(new Color(80, 80, 80));

        // 坐标轴样式
        // 设置 X 轴刻度标签的字体为 Arial 普通样式，字号 10
        plot.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 10));
        // 设置 Y 轴刻度标签的字体为 Arial 普通样式，字号 10
        plot.getRangeAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 10));
    }

    /**
     * Creates the bottom panel for displaying AI analysis text and a button
     * to generate a report.
     *
     * @return A JPanel containing the analysis components.
     */
    private JPanel createAnalysisPanel() {
        RoundedPanel panel = new RoundedPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("AI Analysis"));
        panel.setBackground(Color.WHITE);

        JTextArea analysisText = new JTextArea(
                "Both income and expenses show a steady upward trend over the year, " +
                        "with income and expenses being roughly equal by the end of the year. " +
                        "This suggests that while income is increasing at similar rate."
        );
        analysisText.setLineWrap(true);
        analysisText.setWrapStyleWord(true);
        analysisText.setEditable(false);
        analysisText.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton reportButton = new components.RoundedButton("Generate report");
        reportButton.setBackground(new Color(0, 122, 255));
        reportButton.setForeground(Color.WHITE);
        reportButton.setFont(new Font("Arial", Font.BOLD, 14));

        // 添加按钮点击事件
        reportButton.addActionListener(e ->
                analysisService.generateInvestmentReport(this));

        panel.add(new JScrollPane(analysisText), BorderLayout.CENTER);
        panel.add(reportButton, BorderLayout.EAST);

        return panel;
    }
}