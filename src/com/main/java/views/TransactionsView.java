package com.main.java.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.main.java.components.RoundedButton; // Importing the custom RoundedButton class
import com.main.java.constants.AppConstants;

public class TransactionsView extends BaseView {

    @Override
    public String getViewName() {
        return "Transactions";
    }

    @Override
    protected void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Bar (Card Info Section)
        JPanel cardPanel = createCardPanel();
        add(cardPanel, BorderLayout.NORTH);

        // Transaction Filters and Table (Tabs for All, Income, Expense)
        JPanel filterAndTablePanel = new JPanel(new BorderLayout());
        JTabbedPane transactionTabs = new JTabbedPane();

        // Transaction Table for All Transactions
        JPanel allTransactionsPanel = new JPanel(new BorderLayout());
        allTransactionsPanel.add(createTransactionTable(), BorderLayout.CENTER);
        transactionTabs.addTab("All Transactions", allTransactionsPanel);

        // Placeholder for Income and Expense tabs
        JPanel incomePanel = new JPanel(new BorderLayout());
        incomePanel.add(createTransactionTable(), BorderLayout.CENTER);
        transactionTabs.addTab("Income", incomePanel);

        JPanel expensePanel = new JPanel(new BorderLayout());
        expensePanel.add(createTransactionTable(), BorderLayout.CENTER);
        transactionTabs.addTab("Expense", expensePanel);

        filterAndTablePanel.add(transactionTabs, BorderLayout.CENTER);
        add(filterAndTablePanel, BorderLayout.CENTER);

        // Expense Graph Placeholder (right side)
        JPanel expensePanelSide = createExpenseChartPanel();
        add(expensePanelSide, BorderLayout.EAST);
    }

    // Create Card Info Panel (Balance, Cardholder, Valid Thru)
    private JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));
        panel.setBackground(AppConstants.BACKGROUND_COLOR);

        // Card Details Section
        JLabel cardLabel = new JLabel("My Cards", SwingConstants.CENTER);
        cardLabel.setFont(AppConstants.TITLE_FONT);
        cardLabel.setForeground(AppConstants.PRIMARY_COLOR);
        panel.add(cardLabel);

        JPanel cardDetailsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        cardDetailsPanel.setBackground(AppConstants.BACKGROUND_COLOR);
        cardDetailsPanel.add(new JLabel("Balance: $5,756", SwingConstants.CENTER));
        cardDetailsPanel.add(new JLabel("CARD HOLDER: Eddy Cusuma", SwingConstants.CENTER));
        cardDetailsPanel.add(new JLabel("Valid Through: 12/22", SwingConstants.CENTER));

        panel.add(cardDetailsPanel);

        // Add Card Button (using RoundedButton)
        RoundedButton addCardButton = new RoundedButton("+ Add Card");
        addCardButton.setBackground(AppConstants.PRIMARY_COLOR);
        addCardButton.setForeground(Color.WHITE);
        panel.add(addCardButton);

        return panel;
    }

    // Create Transaction Table
    private JScrollPane createTransactionTable() {
        String[] columns = {"Description", "Transaction ID", "Type", "Card", "Date", "Amount", "Action"};
        Object[][] data = {
                {"Spotify Subscription", "#12548796", "Shopping", "1234 ****", "28 Jan, 12:30 AM", "-$2,500", "amend"},
                {"Freepik Sales", "#12548796", "Transfer", "1234 ****", "25 Jan, 10:40 PM", "+$750", "amend"},
                {"Mobile Service", "#12548796", "Service", "1234 ****", "20 Jan, 10:40 PM", "-$150", "amend"},
                {"Wilson", "#12548796", "Transfer", "1234 ****", "15 Jan, 03:29 PM", "-$1,050", "amend"},
                {"Emily", "#12548796", "Transfer", "1234 ****", "14 Jan, 10:40 PM", "+$840", "amend"}
        };

        DefaultTableModel tableModel = new DefaultTableModel(data, columns);
        JTable table = new JTable(tableModel);

        // Make the last column (Action) a button column (for "amend" actions)
        TableColumn actionColumn = table.getColumnModel().getColumn(6);
        actionColumn.setCellEditor(new ButtonEditor(new RoundedButton("Amend"))); // Using custom ButtonEditor for "Amend"
        actionColumn.setCellRenderer(new ButtonRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        return scrollPane;
    }

    // Create Placeholder for Expense Graph Panel (Right Side)
    private JPanel createExpenseChartPanel() {
        JPanel expenseChartPanel = new JPanel();
        expenseChartPanel.setLayout(new BorderLayout());

        // Placeholder Label for Expense Graph
        JLabel expenseLabel = new JLabel("My Expense", SwingConstants.CENTER);
        expenseLabel.setFont(AppConstants.TITLE_FONT);
        expenseLabel.setForeground(AppConstants.PRIMARY_COLOR);
        expenseChartPanel.add(expenseLabel, BorderLayout.NORTH);

        // Placeholder Expense Bar Chart (Can be replaced with actual graph logic)
        JPanel expenseGraphPanel = new JPanel();
        expenseGraphPanel.setBackground(Color.LIGHT_GRAY); // Placeholder background for chart
        expenseChartPanel.add(expenseGraphPanel, BorderLayout.CENTER);

        return expenseChartPanel;
    }

    // Custom Button Renderer for the Amend Button Column in the Transaction Table
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Amend");
            return this;
        }
    }

    // Custom TableCellEditor for Buttons
    static class ButtonEditor extends DefaultCellEditor {
        private RoundedButton button;

        public ButtonEditor(RoundedButton button) {
            super(new JCheckBox());
            this.button = button;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped(); // This is required for editing to stop when the button is clicked
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText((String) value); // Set the text on the button for each row
            return button;
        }
    }
}
