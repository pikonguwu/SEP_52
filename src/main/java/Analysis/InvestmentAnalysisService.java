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
 * Investment analysis service category, responsible for generating investment analysis reports
 */
public class InvestmentAnalysisService {

    /**
     * Generate and display the investment analysis report
     * @param parentComponent Parent component, used to display dialog boxes
     */
    public void generateInvestmentReport(Component parentComponent) {
        try {
            // Generate the content of the analysis report
            String analysisReport = analyzeInvestments();

            // 2. Display the analysis report
            showAnalysisReport(analysisReport, parentComponent);

            JOptionPane.showMessageDialog(parentComponent,
                    "Investment analysis report has been generated!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentComponent,
                    "Error generating report:" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Simulate calling the large model API to analyze investment data
     * @return Analysis report
     */
    private String analyzeInvestments() throws IOException {
        // 1. 准备要分析的数据
        String investmentData = prepareInvestmentData();
        BaiduAIService baiduAIService = new BaiduAIService();

        // 2. 调用大模型API
        String prompt = "Below are some transaction records. Please analyze them in a clear and readable format and provide recommendations. Use English to answer."
        + "Please format the output as follows:\n"
        + "1. List each transaction separately, including transaction name and amount\n"
        + "2. Keep the analysis section concise\n"
        + "3. Use bullet points for recommendations\n"
        + "4. Provide overall recommendations at the end\n\n"
        + "Transaction records:\n\n" + investmentData;

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
            formatted.append("================ TRANSACTION ANALYSIS REPORT ================\n\n");

            String[] sections = result.split("\n\n");
            for (String section : sections) {
                if (section.startsWith("Overall recommendations:")) {
                    formatted.append("\n===== Overall recommendations =====\n");
                    formatted.append(section.substring(5).replace("\n", "\n• "));
                } else if (section.matches("\\d+\\.\\s.+")) {
                    formatted.append(section.replaceFirst("(\\d+\\.)", "===== TRANSACTION$1 =====\n"))
                            .append("\n");
                } else {
                    formatted.append(section).append("\n");
                }
            }

            formatted.append("\n================ REPORT END ================");
            return formatted.toString();
        } catch (Exception e) {
            return "原始响应：\n" + jsonResponse;
        }
    }

        private String prepareInvestmentData() throws IOException {
            // 从文件中读取投资数据
            String filePath = "transactions.txt"; // 确保文件路径正确
            StringBuilder data = new StringBuilder();
            data.append("Transaction data:\n");

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
     * Display the analysis report dialog
     * @param report Analysis report content
     * @param parentComponent Parent component
     */
    private void showAnalysisReport(String report, Component parentComponent) {
        JDialog reportDialog = new JDialog((Frame)null, "Analysis Report", true);
        reportDialog.setSize(700, 550);
        reportDialog.setLocationRelativeTo(parentComponent);

        JTextArea reportArea = new JTextArea(report);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        reportArea.setMargin(new Insets(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(reportArea);
        reportDialog.add(scrollPane);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save Report");
        saveButton.addActionListener(e -> {
            saveReportToFile(report, parentComponent);
            reportDialog.dispose();
        });

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> reportDialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);
        reportDialog.add(buttonPanel, BorderLayout.SOUTH);

        reportDialog.setVisible(true);
    }

    /**
     * Save the report to a file
     * @param report Report content
     * @param parentComponent Parent component
     */
    private void saveReportToFile(String report, Component parentComponent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Investment Analysis Report");
        fileChooser.setSelectedFile(new File("investment_analysis_report_" +
                new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt"));

        if (fileChooser.showSaveDialog(parentComponent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(report);
                JOptionPane.showMessageDialog(parentComponent,
                        "Report has been saved to: " + file.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parentComponent,
                        "Error saving report:" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}