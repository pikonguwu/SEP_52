// src/views/InvestmentsView.java
package com.main.java.views;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.xy.*;

import com.main.java.components.*;

import javax.swing.*;
import java.awt.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer; // 新增
import org.jfree.chart.title.TextTitle; // 新增
import java.awt.geom.Ellipse2D; // 新增

public class AnalysisView extends BaseView {
    @Override
    public String getViewName() {
        return "Analysis";
    }

    @Override
    protected void initUI() {
        setLayout(new BorderLayout(15, 15));
        
        // 主图表面板（2行2列）
        RoundedPanel chartPanel = new RoundedPanel(new GridLayout(2, 2, 20, 20));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // 创建带边框的图表板块
        chartPanel.add(createChartSection(createAnnualChart("年度收入", true), "Income Trend"));
        chartPanel.add(createChartSection(createAnnualChart("年度支出", false), "Expense Trend"));
        chartPanel.add(createChartSection(createMonthlyChart("月度收入"), "Monthly Income"));
        chartPanel.add(createChartSection(createMonthlyChart("月度支出"), "Monthly Expense"));

        // 底部分析面板
        JPanel analysisPanel = createAnalysisPanel();
        
        // 组合布局
        add(chartPanel, BorderLayout.CENTER);
        add(analysisPanel, BorderLayout.SOUTH);
    }

    // 创建带标题的图表板块
    private JPanel createChartSection(ChartPanel chart, String title) {
        RoundedPanel panel = new RoundedPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // 标题标签
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titleLabel.setForeground(new Color(51, 51, 51));
        
        // 图表容器
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 15, 15, 15)
        ));
        chartContainer.add(chart, BorderLayout.CENTER);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(chartContainer, BorderLayout.CENTER);
        
        return panel;
    }

    private ChartPanel createAnnualChart(String title, boolean isIncome) {
        XYSeries series = new XYSeries("Value");
        series.add(2020, 0);
        series.add(2021, 10000);
        series.add(2022, 20000);
        series.add(2023, 30000);
        series.add(2024, 40000);

        XYDataset dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
            title,
            "Year", 
            "Amount ($)",
            dataset,
            PlotOrientation.VERTICAL,
            false, true, false
        );
        
        styleChart(chart, isIncome ? new Color(0, 122, 255) : new Color(255, 61, 61));
        return new ChartPanel(chart);
    }

    private ChartPanel createMonthlyChart(String title) {
        XYSeries series = new XYSeries("Value");
        for (int i = 1; i <= 12; i++) {
            series.add(i, (Math.random() * 4000));
        }

        XYDataset dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
            title,
            "Month", 
            "Amount ($)",
            dataset,
            PlotOrientation.VERTICAL,
            false, true, false
        );
        
        styleChart(chart, new Color(76, 175, 80));
        return new ChartPanel(chart);
    }

    // 样式优化
    private void styleChart(JFreeChart chart, Color color) {
        XYPlot plot = chart.getXYPlot();
        
        // 背景设置
        plot.setBackgroundPaint(new Color(245, 245, 245));
        plot.setDomainGridlinePaint(new Color(220, 220, 220));
        plot.setRangeGridlinePaint(new Color(220, 220, 220));
        
        // 数据线样式
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, color);
        renderer.setSeriesStroke(0, new BasicStroke(2.5f));
        renderer.setSeriesShape(0, new Ellipse2D.Double(-3, -3, 6, 6));
        
        // 标题样式
        TextTitle chartTitle = chart.getTitle();
        chartTitle.setFont(new Font("微软雅黑", Font.BOLD, 14));
        chartTitle.setPaint(new Color(80, 80, 80));
        
        // 坐标轴样式
        plot.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 10));
        plot.getRangeAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 10));
    }

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
        
        JButton reportButton = new com.main.java.components.RoundedButton("Generate report");
        reportButton.setBackground(new Color(0, 122, 255));
        reportButton.setForeground(Color.WHITE);
        reportButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        panel.add(new JScrollPane(analysisText), BorderLayout.CENTER);
        panel.add(reportButton, BorderLayout.EAST);
        
        return panel;
    }
}