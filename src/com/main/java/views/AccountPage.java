package com.main.java.views;

import com.main.java.constants.AppConstants;
import com.main.java.components.RoundedButton;
import com.main.java.components.RoundedPanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel; // Ensure this import is included
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AccountPage extends BaseView {
    private JTable transactionTable;
    private DefaultTableModel tableModel;

    @Override
    public String getViewName() {
        return "Account";
    }

    @Override
    protected void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title and Import Button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        JLabel titleLabel = new JLabel("Account Overview");
        titleLabel.setFont(AppConstants.TITLE_FONT);
        topPanel.add(titleLabel);

        // Button to import Excel file
        JButton importButton = new JButton("Import Excel File");
        importButton.addActionListener(e -> importExcelFile());
        topPanel.add(importButton);

        add(topPanel, BorderLayout.NORTH);

        // Table for displaying transaction data
        tableModel = new DefaultTableModel(new Object[]{"Date", "Description", "Amount"}, 0);
        transactionTable = new JTable(tableModel); // Correct JTable initialization with DefaultTableModel
        add(new JScrollPane(transactionTable), BorderLayout.CENTER);

        // AI Analysis Button
        JButton analyzeButton = new JButton("Analyze Data with AI");
        analyzeButton.addActionListener(e -> analyzeDataWithAI());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.add(analyzeButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Import Excel file and display its data
    private void importExcelFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xls", "xlsx"));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(file);
                 XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheetAt(0);  // Assuming the data is in the first sheet

                // Clear existing table data
                tableModel.setRowCount(0);

                // Parse Excel rows into the table
                for (Row row : sheet) {
                    Cell dateCell = row.getCell(0);
                    Cell descriptionCell = row.getCell(1);
                    Cell amountCell = row.getCell(2);

                    String date = dateCell != null ? dateCell.toString() : "";
                    String description = descriptionCell != null ? descriptionCell.toString() : "";
                    String amount = amountCell != null ? amountCell.toString() : "";

                    // Add parsed data into the table model
                    tableModel.addRow(new Object[]{date, description, amount});
                }
                JOptionPane.showMessageDialog(this, "File Imported Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error importing file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Analyze data using AI
    private void analyzeDataWithAI() {
        // Dummy AI analysis (This is where you would integrate AI functionality)
        // Example: Categorize transactions or predict future expenses.

        // For now, we'll simply show a message
        JOptionPane.showMessageDialog(this, "AI Analysis started...", "AI Analysis", JOptionPane.INFORMATION_MESSAGE);

        // Send data for AI analysis (In a real-world scenario, you'd send the data to an AI model).
        // Example: categorize transactions, suggest budget predictions, etc.
        performAICategorization();
    }

    // Placeholder for AI categorization (This can be enhanced with a proper AI integration)
    private void performAICategorization() {
        // Here, we could categorize transactions or use any AI service (e.g., prediction, budget suggestion).
        // For now, let's just display the message.

        // For example: Let's pretend we categorize transactions
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String description = (String) tableModel.getValueAt(i, 1);
            if (description.contains("Netflix") || description.contains("Spotify")) {
                tableModel.setValueAt("Entertainment", i, 1);  // Categorizing as "Entertainment"
            } else if (description.contains("Salary")) {
                tableModel.setValueAt("Income", i, 1);  // Categorizing as "Income"
            } else {
                tableModel.setValueAt("Miscellaneous", i, 1);  // Default category
            }
        }

        JOptionPane.showMessageDialog(this, "AI Categorization Complete!", "AI Categorization", JOptionPane.INFORMATION_MESSAGE);
    }
}
