package Analysis;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import views.BucksBrainAIChatView;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import services.BaiduAIService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/**
 * 投资分析服务类，负责生成投资分析报告
 */
public class InvestmentAnalysisService {

    /**
     * 生成并显示投资分析报告
     * @param parentComponent 父组件，用于显示对话框
     */
    public void generateInvestmentReport(Component parentComponent) {
        try {
            // 1. 生成分析报告内容
            String analysisReport = analyzeInvestments();

            // 2. 显示分析报告
            showAnalysisReport(analysisReport, parentComponent);

            JOptionPane.showMessageDialog(parentComponent,
                    "投资分析报告已生成!", "成功", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentComponent,
                    "生成报告时出错: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 模拟调用大模型API分析投资数据
     * @return 分析报告
     */
    private String analyzeInvestments() throws IOException {
        // 1. 准备要分析的数据
        String investmentData = prepareInvestmentData();
        BaiduAIService baiduAIService = new BaiduAIService();

        // 2. 调用大模型API
        String prompt = "以下是一些交易记录，请用清晰易读的格式帮我进行分析并给出建议。"
                + "请按照以下格式输出：\n"
                + "1. 每笔交易单独列出，包含交易名称和金额\n"
                + "2. 分析部分简明扼要\n"
                + "3. 建议部分使用项目符号列出\n"
                + "4. 最后给出总体建议\n\n"
                + "交易记录如下：\n\n" + investmentData;

        System.out.println("发送给AI的提示词：\n" + prompt);

        try {
            String response = baiduAIService.getAIResponse(prompt);
            // 提取并格式化结果
            return formatAIResponse(response);
        } catch (Exception ex) {
            return "请求出现错误：" + ex.getMessage();
        }
    }

    private String formatAIResponse(String jsonResponse) {
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            String result = jsonObject.get("result").getAsString();

            StringBuilder formatted = new StringBuilder();
            formatted.append("================ 交易分析报告 ================\n\n");

            String[] sections = result.split("\n\n");
            for (String section : sections) {
                if (section.startsWith("总体建议：")) {
                    formatted.append("\n===== 总体建议 =====\n");
                    formatted.append(section.substring(5).replace("\n", "\n• "));
                } else if (section.matches("\\d+\\.\\s.+")) {
                    formatted.append(section.replaceFirst("(\\d+\\.)", "===== 交易$1 =====\n"))
                            .append("\n");
                } else {
                    formatted.append(section).append("\n");
                }
            }

            formatted.append("\n================ 报告结束 ================");
            return formatted.toString();
        } catch (Exception e) {
            return "原始响应：\n" + jsonResponse;
        }
    }

        private String prepareInvestmentData() throws IOException {
            // 从文件中读取投资数据
            String filePath = "transactions.txt"; // 确保文件路径正确
            StringBuilder data = new StringBuilder();
            data.append("交易数据:\n");

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    data.append(line).append("\n");
                }
            }

            return data.toString();
        }

        public static void main(String[] args) {
            InvestmentAnalysisService analyzer = new InvestmentAnalysisService();
            try {
                String report = analyzer.analyzeInvestments();
                System.out.println(report);
            } catch (IOException e) {
                System.err.println("读取文件或分析数据时发生错误：" + e.getMessage());
            }

    }

    /**
     * 显示分析报告对话框
     * @param report 分析报告内容
     * @param parentComponent 父组件
     */
    private void showAnalysisReport(String report, Component parentComponent) {
        JDialog reportDialog = new JDialog((Frame)null, "分析报告", true);
        reportDialog.setSize(700, 550);
        reportDialog.setLocationRelativeTo(parentComponent);

        JTextArea reportArea = new JTextArea(report);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        reportArea.setMargin(new Insets(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(reportArea);
        reportDialog.add(scrollPane);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("保存报告");
        saveButton.addActionListener(e -> {
            saveReportToFile(report, parentComponent);
            reportDialog.dispose();
        });

        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(e -> reportDialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);
        reportDialog.add(buttonPanel, BorderLayout.SOUTH);

        reportDialog.setVisible(true);
    }

    /**
     * 将报告保存到文件
     * @param report 报告内容
     * @param parentComponent 父组件
     */
    private void saveReportToFile(String report, Component parentComponent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("保存投资分析报告");
        fileChooser.setSelectedFile(new File("investment_analysis_report_" +
                new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt"));

        if (fileChooser.showSaveDialog(parentComponent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(report);
                JOptionPane.showMessageDialog(parentComponent,
                        "报告已保存到: " + file.getAbsolutePath(), "成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parentComponent,
                        "保存报告时出错: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}