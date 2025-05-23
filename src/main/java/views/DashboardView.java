package views;

import constants.AppConstants;
import components.*;
import services.BaiduAIService;
import services.TransactionDataService;
import com.google.gson.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import ui.AddCardDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Defines the DashboardView class, which extends BaseView and is used to display the dashboard view.
 * This view includes a top title, a main content area (containing a card panel, transaction table,
 * weekly activity chart, and expense statistics chart), and a bottom action bar.
 */
public class DashboardView extends BaseView {

    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private BaiduAIService aiService = new BaiduAIService();
    private TransactionsView transactionsView;
    private AccountsView accountsView;

    // Add data service and chart-related fields
    private TransactionDataService dataService;
    private JFreeChart weeklyChart;
    private JFreeChart expenseChart;
    private ChartPanel weeklyChartPanel;
    private ChartPanel expenseChartPanel;
    private Random random = new Random();
    /**
     * Constructor for the DashboardView.
     */
    public DashboardView() {
        // Initialize data service - ensuring it is initialized in the constructor
        dataService = new TransactionDataService();
        // Note: The parent class constructor calls initUI(), so dataService must be initialized before that
    }

    /**
     * Overrides the getViewName method to return the name of this view.
     *
     * @return The name of the view, "Dashboard".
     */
    @Override
    public String getViewName() {
        return "Dashboard";
    }

    /**
     * Overrides the initUI method to initialize the user interface.
     * Sets the layout, adds the title, main content area, and bottom action bar.
     */
    @Override
    protected void initUI() {
        // Set layout manager with horizontal and vertical gaps of 15 pixels
        setLayout(new BorderLayout(15, 15));
        // Set panel padding for top, left, bottom, and right to 20 pixels
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create the top title label
        JLabel titleLabel = new JLabel("Overview", SwingConstants.LEFT);
        // Set the title font to the globally defined title font
        titleLabel.setFont(AppConstants.TITLE_FONT);
        // Set the bottom border of the title label to 15 pixels (padding)
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        // Add the title label to the NORTH position of the panel
        add(titleLabel, BorderLayout.NORTH);

        // Create the main content area using a 2x2 GridLayout with 15 pixel gaps
        RoundedPanel gridPanel = new RoundedPanel(new GridLayout(2, 2, 15, 15));
        // Add the card panel and wrap it with a titled border
        gridPanel.add(wrapComponent(createCardPanel(), "My Cards"));
        // Add the transaction table and wrap it with a titled border
        gridPanel.add(wrapComponent(createTransactionTable(), "Recent Transactions"));
        // Add the weekly activity chart and wrap it with a titled border
        gridPanel.add(wrapComponent(createWeeklyChart(), "Weekly Activity"));
        // Add the expense statistics chart and wrap it with a titled border
        gridPanel.add(wrapComponent(createExpenseChart(), "Expense Statistics"));
        // Add the main content area to the CENTER position of the panel
        add(gridPanel, BorderLayout.CENTER);

        // Create the bottom action panel and add it to the SOUTH position
        add(createActionPanel(), BorderLayout.SOUTH);

        // After all UI components are created, load initial transaction data
        // Ensure dataService is initialized
        if (dataService != null) {
            loadInitialTransactions();
        } else {
            System.err.println("Warning: dataService is null, cannot load initial transactions");
            // Re-initialize here as a safety measure
            dataService = new TransactionDataService();
            loadInitialTransactions();
        }
    }

    /**
     * Creates the card panel, simulating a bank card interface.
     *
     * @return A panel containing bank card information.
     */
    private JPanel createCardPanel() {
        // Create card container panel using BoxLayout for horizontal arrangement
        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.X_AXIS));
        cardsContainer.setOpaque(false);

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Create the main panel
        RoundedPanel mainPanel = new RoundedPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Cast Graphics object to Graphics2D for advanced drawing features
                Graphics2D g2d = (Graphics2D) g;
                // Set drawing color to dark blue
                g2d.setColor(new Color(40, 80, 150));
                // Draw a filled rounded rectangle covering the entire panel
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        // Set internal padding for the card panel (top, left, bottom, right are 25 pixels)
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        // Set the preferred size of the card panel to 320x200 pixels
        mainPanel.setPreferredSize(new Dimension(320, 200));

        // Add a default card
        RoundedPanel defaultCard = createCard("3778****1234", "Eddy Cusuma", "12/22", 5756.00);

        // Create the add button
        RoundedButton addCardButton = new RoundedButton("+ Add Card");
        addCardButton.setFont(new Font("Arial", Font.BOLD, 12));
        addCardButton.setBackground(new Color(255, 255, 255, 50));
        addCardButton.setForeground(Color.WHITE);
        addCardButton.setBorderPainted(false);
        addCardButton.setFocusPainted(false);
        addCardButton.setContentAreaFilled(false);
        addCardButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a button container panel for positioning the button
        JPanel buttonContainer = new JPanel(null); // Use null layout (absolute positioning)
        buttonContainer.setOpaque(false);
        buttonContainer.setPreferredSize(defaultCard.getPreferredSize());
        addCardButton.setBounds(
            defaultCard.getPreferredSize().width - 100, // Right margin
            10, // Top margin
            100, // Button width
            30  // Button height
        );
        buttonContainer.add(addCardButton);

        // Create a layered pane for the card and button
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(defaultCard.getPreferredSize());
        layeredPane.add(defaultCard, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(buttonContainer, JLayeredPane.PALETTE_LAYER);

        // Set bounds for the card and button container
        defaultCard.setBounds(0, 0, defaultCard.getPreferredSize().width, defaultCard.getPreferredSize().height);
        buttonContainer.setBounds(0, 0, defaultCard.getPreferredSize().width, defaultCard.getPreferredSize().height);

        addCardButton.addActionListener(e -> {
            AddCardDialog dialog = new AddCardDialog((Frame) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                // Get new card information
                String cardNumber = dialog.getCardNumber();
                String cardholderName = dialog.getCardholderName();
                String expiryDate = dialog.getExpiryDate();
                double balance = dialog.getBalance();

                // Create the new card
                RoundedPanel newCard = createCard(cardNumber, cardholderName, expiryDate, balance);

                // Create the button container for the new card
                JPanel newButtonContainer = new JPanel(null);
                newButtonContainer.setOpaque(false);
                newButtonContainer.setPreferredSize(newCard.getPreferredSize());

                // Create a new instance of the add button (for display on the new card)
                RoundedButton newAddButton = new RoundedButton("+ Add Card");
                newAddButton.setFont(addCardButton.getFont());
                newAddButton.setBackground(addCardButton.getBackground());
                newAddButton.setForeground(addCardButton.getForeground());
                newAddButton.setBorderPainted(false);
                newAddButton.setFocusPainted(false);
                newAddButton.setContentAreaFilled(false);
                newAddButton.setBackground(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                newAddButton.setBorder(addCardButton.getBorder());
                newAddButton.setBounds(
                    newCard.getPreferredSize().width - 100,
                    10,
                    90,
                    30
                );
                newButtonContainer.add(newAddButton);

                // Create the layered pane for the new card
                JLayeredPane newLayeredPane = new JLayeredPane();
                newLayeredPane.setPreferredSize(newCard.getPreferredSize());
                newLayeredPane.add(newCard, JLayeredPane.DEFAULT_LAYER);
                newLayeredPane.add(newButtonContainer, JLayeredPane.PALETTE_LAYER);

                // Set bounds for the new card and button container
                newCard.setBounds(0, 0, newCard.getPreferredSize().width, newCard.getPreferredSize().height);
                newButtonContainer.setBounds(0, 0, newCard.getPreferredSize().width, newCard.getPreferredSize().height);

                // Add the new card to the container
                cardsContainer.add(newLayeredPane);
                cardsContainer.add(Box.createHorizontalStrut(15)); // Add spacing between cards

                // Refresh/revalidate the container
                cardsContainer.revalidate();
                cardsContainer.repaint();

                // Scroll to the newly added card
                scrollPane.getHorizontalScrollBar().setValue(scrollPane.getHorizontalScrollBar().getMaximum());

                // Display success message
                JOptionPane.showMessageDialog(this,
                    "Card added successfully!\n" +
                    "Card Number: " + maskCardNumber(cardNumber) + "\n" +
                    "Cardholder: " + cardholderName + "\n" +
                    "Expiry Date: " + expiryDate + "\n" +
                    "Balance: $" + String.format("%.2f", balance),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add the default card (with its layered pane) to the container
        cardsContainer.add(layeredPane);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }

    /**
     * Creates a single card panel with specified details.
     *
     * @param cardNumber     The masked card number.
     * @param cardholderName The cardholder's name.
     * @param expiryDate     The card's expiry date.
     * @param balance        The card's balance.
     * @return A RoundedPanel representing the card.
     */
    private RoundedPanel createCard(String cardNumber, String cardholderName, String expiryDate, double balance) {
        // Create the rounded card container
        RoundedPanel card = new RoundedPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(40, 80, 150));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        // Set internal padding for the card panel (top, left, bottom, right are 25 pixels)
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        // Set the preferred size of the card panel to 320x200 pixels
        card.setPreferredSize(new Dimension(320, 200));

        // Main content container
        JPanel content = new JPanel(new GridBagLayout()) {
            /**
             * Overrides the isOpaque method to make the panel background transparent.
             *
             * @return false, indicating the panel background is transparent.
             */
            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        // Create a GridBag constraints object
        GridBagConstraints gbc = new GridBagConstraints();
        // Set internal padding for components to 5 pixels
        gbc.insets = new Insets(5, 5, 5, 5);

        // Balance area (top left)
        JPanel balancePanel = new JPanel(new BorderLayout());
        // Set balance panel background to transparent
        balancePanel.setOpaque(false);
        JLabel balanceLabel = new JLabel("BALANCE");
        // Set balance label font to Arial Bold, size 12
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        // Set balance label font color to light gray
        balanceLabel.setForeground(new Color(180, 180, 220));

        JLabel amountLabel = new JLabel("$5,756");
        // Set amount label font to Arial Bold, size 24
        amountLabel.setFont(new Font("Arial", Font.BOLD, 24));
        // Set amount label font color to white
        amountLabel.setForeground(Color.WHITE);

        // Add the balance label to the NORTH position of the balance panel
        balancePanel.add(balanceLabel, BorderLayout.NORTH);
        // Add the amount label to the CENTER position of the balance panel
        balancePanel.add(amountLabel, BorderLayout.CENTER);

        // Card number area (middle top)
        JPanel numberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        numberPanel.setOpaque(false);
        String[] segments = cardNumber.split(" ");
        for (String segment : segments) {
            numberPanel.add(createCardSegment(segment, segment.length() == 4 ? 22 : 18));
        }

        // Bottom info area
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        // Set bottom info panel background to transparent
        bottomPanel.setOpaque(false);

        // Left cardholder information
        JPanel holderPanel = new JPanel(new BorderLayout());
        // Set cardholder info panel background to transparent
        holderPanel.setOpaque(false);
        JLabel holderLabel = new JLabel("CARDHOLDER");
        // Set cardholder label font to Arial Bold, size 10
        holderLabel.setFont(new Font("Arial", Font.BOLD, 10));
        // Set cardholder label font color to light gray
        holderLabel.setForeground(new Color(180, 180, 220));

        JLabel nameLabel = new JLabel("Eddy Cusuma");
        // Set cardholder name label font to Arial Bold, size 14
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        // Set cardholder name label font color to white
        nameLabel.setForeground(Color.WHITE);

        // Add the cardholder label to the NORTH position of the cardholder info panel
        holderPanel.add(holderLabel, BorderLayout.NORTH);
        // Add the cardholder name label to the CENTER position of the cardholder info panel
        holderPanel.add(nameLabel, BorderLayout.CENTER);

        // Right validity information
        JPanel validPanel = new JPanel(new BorderLayout());
        // Set validity information panel background to transparent
        validPanel.setOpaque(false);
        JLabel validLabel = new JLabel("VALID THRU");
        // Set validity label font to Arial Bold, size 10
        validLabel.setFont(new Font("Arial", Font.BOLD, 10));
        // Set validity label font color to light gray
        validLabel.setForeground(new Color(180, 180, 220));

        JLabel dateLabel = new JLabel("12/22");
        // Set validity date label font to Arial Bold, size 14
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        // Set validity date label font color to white
        dateLabel.setForeground(Color.WHITE);

        // Add the validity label to the NORTH position of the validity info panel
        validPanel.add(validLabel, BorderLayout.NORTH);
        // Add the validity date label to the CENTER position of the validity info panel
        validPanel.add(dateLabel, BorderLayout.CENTER);

        // Combine layout components
        // Set GridBag constraint x coordinate to 0
        gbc.gridx = 0;
        // Set GridBag constraint y coordinate to 0
        gbc.gridy = 0;
        // Set component anchor to NORTHWEST
        gbc.anchor = GridBagConstraints.NORTHWEST;
        // Add the balance area to the content panel
        content.add(balancePanel, gbc);

        // Set GridBag constraint y coordinate to 1
        gbc.gridy = 1;
        // Set component horizontal weight to 1.0
        gbc.weightx = 1.0;
        // Set component to fill horizontally
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Add the card number area to the content panel
        content.add(numberPanel, gbc);

        // Add cardholder information to the bottom panel
        bottomPanel.add(holderPanel);
        // Add validity information to the bottom panel
        bottomPanel.add(validPanel);

        // Set GridBag constraint y coordinate to 2
        gbc.gridy = 2;
        // Set component vertical weight to 1.0
        gbc.weighty = 1.0;
        // Set component to fill horizontally and vertically
        gbc.fill = GridBagConstraints.BOTH;
        // Add the bottom information area to the content panel
        content.add(bottomPanel, gbc);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    /**
     * Creates a card number segment component, used to display segments of a bank card number.
     *
     * @param text The text content of the card number segment.
     * @param fontSize The font size of the card number segment text.
     * @return A JLabel component containing the card number segment information.
     */
    private JLabel createCardSegment(String text, int fontSize) {
        JLabel segment = new JLabel(text);
        // Set the font for the card number segment label
        segment.setFont(new Font("Arial", Font.BOLD, fontSize));
        // Set the font color for the card number segment label to white
        segment.setForeground(Color.WHITE);
        // Set left and right internal padding for the label to 5 pixels
        segment.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        return segment;
    }

    /**
     * Creates the transaction table, displaying recent transaction records.
     *
     * @return A scroll pane containing the transaction table.
     */
    private JScrollPane createTransactionTable() {
        // Define table column names
        String[] columns = {"", "Date", "Description", "Amount"};
        // Define initial table data
        Object[][] initialData = {
            {new ImageIcon("path/to/card_icon.png"), "28 January 2021", "Deposit from my Card", "-$850"},
            {new ImageIcon("path/to/paypal_icon.png"), "25 January 2021", "Deposit Paypal", "+$2,500"},
            {new ImageIcon("path/to/user_icon.png"), "21 January 2021", "Jemi Wilson", "+$5,400"}
        };

        // Create the table model
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table uneditable
            }
        };

        // Add initial data
        for (Object[] row : initialData) {
            tableModel.addRow(row);
        }

        // Create the table and override the prepareRenderer method
        transactionTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (column == 0) {
                    ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    ((JLabel) component).setHorizontalAlignment(SwingConstants.LEFT);
                }
                return component;
            }
        };

        // Set table row height to 35 pixels
        transactionTable.setRowHeight(35);
        // Set the font for the table header
        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        // Set the preferred size for the table header
        transactionTable.getTableHeader().setPreferredSize(new Dimension(100, 35));

        // Hide the header for the first column
        transactionTable.getColumnModel().getColumn(0).setHeaderValue("");

        // Set preferred widths for columns
        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        transactionTable.getColumnModel().getColumn(3).setPreferredWidth(100);

        // Set font and color for cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        transactionTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        transactionTable.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
        transactionTable.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        transactionTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        // Create modify button
        JButton editButton = new JButton("modify");
        editButton.setFont(new Font("Arial", Font.BOLD, 12));
        editButton.setBackground(AppConstants.PRIMARY_COLOR);
        editButton.setForeground(Color.WHITE);
        editButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Add action listener for the modify button (ignoring unused lambda parameter)
        editButton.addActionListener(_e -> {
            int selectedRow = transactionTable.getSelectedRow();
            if (selectedRow >= 0) {
                // Get data from the selected row
                String date = (String) tableModel.getValueAt(selectedRow, 1);
                String description = (String) tableModel.getValueAt(selectedRow, 2);
                String amount = (String) tableModel.getValueAt(selectedRow, 3);

                // Determine transaction type
                String type = amount.startsWith("-") ? "Expense" : "Income";

                // Create the edit dialog
                EditTransactionDialog dialog = new EditTransactionDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    date,
                    description,
                    amount.replace("$", "").replace("+", "").replace("-", ""),
                    type
                );

                dialog.setVisible(true);

                if (dialog.isConfirmed()) {
                    // Get the modified data
                    String newDate = dialog.getDate();
                    String newDescription = dialog.getDescription();
                    String newAmount = dialog.getAmount();
                    String newType = dialog.getTransactionType();

                    // Update the table data
                    updateTransactionInTable(selectedRow, newDate, newDescription, newAmount, newType);

                    // Synchronize with TransactionsView
                    if (transactionsView != null) {
                        // First remove the old data
                        transactionsView.removeTransaction(date, description, amount, type);
                        // Add the new data
                        transactionsView.addTransaction(newDate, newDescription,
                            (newType.equals("Expense") ? "-" : "+") + "$" + newAmount, newType);
                    }

                    // Display success message
                    JOptionPane.showMessageDialog(this,
                        "The transaction information has been successfully modified!",
                        "Modification successful",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // If no row is selected, display a hint message
                JOptionPane.showMessageDialog(this,
                    "Please select the transaction record that you want to modify first!",
                    "Hint",
                    JOptionPane.WARNING_MESSAGE);
            }
        });

        // Create the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(editButton);

        // Create a panel to hold the table and button panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(transactionTable, BorderLayout.CENTER);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set scroll bar policies
        JScrollPane scrollPane = new JScrollPane(tablePanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        return scrollPane;
    }

    /**
     * Updates transaction information in the table.
     *
     * @param row The index of the row to update.
     * @param date The new date.
     * @param description The new description.
     * @param amount The new amount.
     * @param type The new transaction type.
     */
    private void updateTransactionInTable(int row, String date, String description, String amount, String type) {
        // Select icon based on transaction type
        ImageIcon icon = new ImageIcon(type.equals("Income") ? "path/to/income_icon.png" : "path/to/expense_icon.png");

        // Format amount, add negative sign if it's an expense
        String formattedAmount = (type.equals("Expense") ? "-" : "+") + "$" + amount;

        // Update table data
        tableModel.setValueAt(icon, row, 0);
        tableModel.setValueAt(date, row, 1);
        tableModel.setValueAt(description, row, 2);
        tableModel.setValueAt(formattedAmount, row, 3);

        // Update transaction data in the data service
        dataService.updateTransaction(row, date, description, amount, type);

        // Update the charts
        updateCharts();

        // Synchronize update with AccountsView
        if (accountsView != null) {
            accountsView.updateTransaction(date, description, formattedAmount, type);
        }
    }

    /**
     * Adds a new transaction to the table and data service.
     *
     * @param date The transaction date.
     * @param description The transaction description.
     * @param amount The transaction amount.
     * @param type The transaction type (Income or Expense).
     */
    private void addTransactionToTable(String date, String description, String amount, String type) {
        // Select icon based on transaction type
        ImageIcon icon = new ImageIcon(type.equals("Income") ? "path/to/income_icon.png" : "path/to/expense_icon.png");

        // Format amount, add negative sign if it's an expense
        String formattedAmount = (type.equals("Expense") ? "-" : "+") + String.format("%s", amount);

        // Add a new row to the end of the table
        tableModel.addRow(new Object[]{icon, date, description, formattedAmount});

        // Add to the data service
        dataService.addTransaction(date, description, amount, type);

        // Update charts
        updateCharts();

        // Also update TransactionsView
        if (transactionsView != null) {
            transactionsView.addTransaction(date, description, formattedAmount, type);
        }

        // Also update AccountsView
        if (accountsView != null) {
            accountsView.addTransaction(date, description, formattedAmount, type);
        }
    }

    /**
     * Creates the weekly activity chart, displaying a bar chart of spending amounts within a week.
     *
     * @return A chart panel containing the weekly activity chart.
     */
    private ChartPanel createWeeklyChart() {
        // Create a default category dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Initialize with empty dataset, will be updated later
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String day : days) {
            dataset.addValue(0, "Spending", day);
        }

        weeklyChart = ChartFactory.createBarChart(
            "", "", "Amount ($)",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false
        );

        // Set chart style
        CategoryPlot plot = weeklyChart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(40, 80, 150));

        // Set axis style
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelsVisible(true);
        domainAxis.setTickMarksVisible(true);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        weeklyChart.setBackgroundPaint(Color.WHITE);

        weeklyChartPanel = new ChartPanel(weeklyChart);
        weeklyChartPanel.setPreferredSize(new Dimension(400, 300));
        return weeklyChartPanel;
    }

    /**
     * Creates the expense statistics chart, displaying a pie chart of the proportion of each expense category.
     *
     * @return A chart panel containing the expense statistics chart.
     */
    private ChartPanel createExpenseChart() {
        // Create a default pie dataset
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Initialize with empty dataset, will be updated later
        dataset.setValue("No Data", 100);

        expenseChart = ChartFactory.createPieChart("", dataset, true, true, false);

        // Set pie chart style
        PiePlot plot = (PiePlot) expenseChart.getPlot();
        plot.setLabelFont(new Font("Arial", Font.PLAIN, 12));
        plot.setLabelBackgroundPaint(new Color(255, 255, 255, 200));

        // Set color for "No Data" (by index, since there is only one data point initially)
        plot.setSectionPaint(0, new Color(200, 200, 200));

        expenseChart.setBackgroundPaint(Color.WHITE);

        expenseChartPanel = new ChartPanel(expenseChart);
        expenseChartPanel.setPreferredSize(new Dimension(400, 300));
        return expenseChartPanel;
    }

    /**
     * Updates both the weekly activity chart and the expense statistics chart.
     */
    public void updateCharts() {
        updateWeeklyChart();
        updateExpenseChart();
    }

    /**
     * Updates the weekly activity chart with current data.
     */
    private void updateWeeklyChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Double> weeklyData = dataService.getWeeklySpending();

        for (Map.Entry<String, Double> entry : weeklyData.entrySet()) {
            dataset.addValue(entry.getValue(), "Spending", entry.getKey());
        }

        weeklyChart.getCategoryPlot().setDataset(dataset);
    }

    /**
     * Updates the expense statistics chart with current data.
     */
    private void updateExpenseChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Double> categoryData = dataService.getExpenseCategories();

        // Add data to the pie dataset
        for (Map.Entry<String, Double> entry : categoryData.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        // If there is no data, add a default "No Data" entry
        if (categoryData.isEmpty()) {
            dataset.setValue("No Data", 100);
        }

        // Set the pie chart dataset
        PiePlot plot = (PiePlot) expenseChart.getPlot();
        plot.setDataset(dataset);

        // Create a color mapping for categories
        Map<String, Color> colorMap = new HashMap<>();
        colorMap.put("Housing", new Color(51, 102, 204));
        colorMap.put("Food", new Color(76, 153, 0));
        colorMap.put("Transport", new Color(255, 153, 0));
        colorMap.put("Entertainment", new Color(153, 0, 153));
        colorMap.put("Savings", new Color(0, 153, 204));
        colorMap.put("Others", new Color(153, 153, 153));
        colorMap.put("No Data", new Color(200, 200, 200));

        // Iterate through the dataset keys and set the corresponding colors
        int index = 0;
        for (Object key : dataset.getKeys()) {
            String category = key.toString();
            Color color = colorMap.getOrDefault(category, new Color(100, 100, 150));

            // Use index instead of string key to set the color
            plot.setSectionPaint(index, color);
            index++;
        }
    }

    /**
     * Creates the action panel, containing buttons for manual add and file import operations.
     *
     * @return A panel containing the action buttons.
     */
    private JPanel createActionPanel() {
        // Create a rounded panel using a center-aligned FlowLayout with 20px horizontal and 10px vertical gaps
        RoundedPanel panel = new RoundedPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        // Set a compound border for the panel, including a titled border and inner padding
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Add transactions"),
            BorderFactory.createEmptyBorder(20, 0, 0, 0)
        ));
        // Add the "Manual Add" action button
        panel.add(createActionButton("Manual Add"));
        // Add the "Import File" action button
        panel.add(createActionButton("Import File"));
        return panel;
    }

    /**
     * Processes raw transaction text using AI to format it into a standard string.
     *
     * @param text The raw transaction text.
     * @return The formatted transaction string (Date|Description|Amount|Type) or the original text if processing fails.
     */
    private String processWithAI(String text) {
        String prompt = "Please convert the following transaction information into the standard format (Date|Description|Amount|Type), " +
                       "where Type can only be Income or Expense, the amount should be positive, and the date format should be dd/MM/yyyy.\n" +
                       "Original text:" + text + "\n" +
                       "Your response will only return the final result in standard format without any other contents. For example: 08/07/2025 | Rent Payment | 1,000 yuan | Expense.";
        // System.out.println(text);
        String response = aiService.getAIResponse(prompt);
        System.out.println("Raw AI response: " + response);

        // Extract the 'result' field from the JSON response using the new JsonParser.parseString method
        try {
            // Use the new JsonParser.parseString method
            JsonElement jsonElement = JsonParser.parseString(response);
            JsonObject jsonResponse = jsonElement.getAsJsonObject();
            String result = jsonResponse.get("result").getAsString();

            // Extract the first line of transaction information (assuming only one transaction per line)
            String[] lines = result.split("\n");
            for (String line : lines) {
                if (line.contains("|")) {
                    return line.trim();
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing AI response: " + e.getMessage());
        }

        // If parsing fails, return the original text
        return text;
    }

    /**
     * Handles the file import process for transactions.
     * Reads a selected text file, processes each line (using AI if necessary),
     * and adds valid transactions to the table and data service.
     */
    private void importTransactionsFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                java.util.Scanner scanner = new java.util.Scanner(file);
                int successCount = 0;
                int failCount = 0;

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        try {
                            // Attempt to directly parse the standard format
                            String[] parts = line.split("\\|");
                            if (parts.length == 4) {
                                String date = parts[0].trim();
                                String description = parts[1].trim();
                                double amount = Double.parseDouble(parts[2].trim());
                                String type = parts[3].trim();

                                if (type.equals("Income") || type.equals("Expense")) {
                                    addTransactionToTable(date, description, String.valueOf(amount), type);
                                    successCount++;
                                    continue;
                                }
                            }

                            // If it doesn't match the standard format, process using AI
                            String processedLine = processWithAI(line);
                            System.out.println("Processed line: " + processedLine);
                            parts = processedLine.split("\\|");
                            // System.out.print(parts.length);
                            if (parts.length == 4) {
                                String date = parts[0].trim();
                                String description = parts[1].trim();
                                String amount = parts[2].trim();
                                String type = parts[3].trim();

                                if (type!="") {
                                    addTransactionToTable(date, description, amount, type);
                                    successCount++;
                                } else {
                                    failCount++;
                                }
                            } else {
                                failCount++;
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                            failCount++;
                        }
                    }
                }
                scanner.close();

                // Update charts
                updateCharts();

                JOptionPane.showMessageDialog(this,
                    "File imported successfully!\n" +
                    "Successfully imported: " + successCount + " transactions\n" +
                    "Failed to import: " + failCount + " transactions",
                    "Import Result",
                    JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error importing file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Creates an action button, setting its font, background color, foreground color, and padding.
     *
     * @param text The text displayed on the button.
     * @return The configured action button.
     */
    private JButton createActionButton(String text) {
        // Create a rounded button
        RoundedButton btn = new RoundedButton(text);
        // Set the button font to the globally defined button font
        btn.setFont(AppConstants.BUTTON_FONT);
        // Set the button background color to the globally defined primary color
        btn.setBackground(AppConstants.PRIMARY_COLOR);
        // Set the button foreground color to white
        btn.setForeground(Color.WHITE);
        // Set the internal padding for the button (top, left, bottom, right are 10, 25, 10, 25 pixels)
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // Add an action listener for the "Manual Add" button (ignoring unused lambda parameter)
        if (text.equals("Manual Add")) {
            btn.addActionListener(_e -> {
                AddTransactionDialog dialog = new AddTransactionDialog((Frame) SwingUtilities.getWindowAncestor(this));
                dialog.setVisible(true);

                if (dialog.isConfirmed()) {
                    // Get the transaction information entered by the user
                    String date = dialog.getDate();
                    String description = dialog.getDescription();
                    double amount = dialog.getAmount();
                    String type = dialog.getTransactionType();

                    // Add the transaction to the table
                    addTransactionToTable(date, description, String.valueOf(amount), type);

                    // Display success message
                    JOptionPane.showMessageDialog(this,
                        "Transaction added successfully!\n" +
                        "Date: " + date + "\n" +
                        "Description: " + description + "\n" +
                        "Amount: " + amount + "\n" +
                        "Type: " + type,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            });
        } else if (text.equals("Import File")) {
            btn.addActionListener(_e -> importTransactionsFromFile());
        }

        return btn;
    }

    /**
     * Wraps a component, adding a titled border and setting the background color.
     *
     * @param comp The component to be wrapped.
     * @param title The title for the wrapper component.
     * @return The wrapped panel.
     */
    private JPanel wrapComponent(Component comp, String title) {
        // Create a panel using BorderLayout
        JPanel wrapper = new JPanel(new BorderLayout());
        // Set a titled border for the panel
        wrapper.setBorder(BorderFactory.createTitledBorder(title));
        // Set the background color of the panel to white
        wrapper.setBackground(Color.WHITE);
        // Add the component to the CENTER position of the panel
        wrapper.add(comp, BorderLayout.CENTER);
        return wrapper;
    }

    /**
     * Sets the reference to the TransactionsView for data synchronization.
     *
     * @param view The TransactionsView instance.
     */
    public void setTransactionsView(TransactionsView view) {
        this.transactionsView = view;
    }

    /**
     * Sets the reference to the AccountsView for data synchronization.
     *
     * @param view The AccountsView instance.
     */
    public void setAccountsView(AccountsView view) {
        this.accountsView = view;
    }

    /**
     * Loads initial transaction data from the table model into the data service.
     */
    private void loadInitialTransactions() {
        try {
            // Get existing transaction data from the table model
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String date = (String) tableModel.getValueAt(i, 1);
                String description = (String) tableModel.getValueAt(i, 2);
                String amount = (String) tableModel.getValueAt(i, 3);
                String type = amount.startsWith("-") ? "Expense" : "Income";

                // Clean up the amount string
                String cleanAmount = amount.replace("$", "").replace("+", "").replace("-", "");

                // Add to the data service
                dataService.addTransaction(date, description, cleanAmount, type);
            }

            // Update charts for the first time
            updateCharts();
        } catch (Exception e) {
            // Add exception handling for better diagnosis
            System.err.println("Error in loadInitialTransactions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Updates the display of a single card panel with new information.
     *
     * @param panel The card panel to update.
     * @param cardNumber The new card number.
     * @param cardholderName The new cardholder name.
     * @param expiryDate The new expiry date.
     * @param balance The new balance.
     */
    private void updateCardDisplay(RoundedPanel panel, String cardNumber, String cardholderName, String expiryDate, double balance) {
        // Get the content panel
        JPanel content = (JPanel) panel.getComponent(1);

        // Update balance
        JPanel balancePanel = (JPanel) content.getComponent(0);
        JLabel amountLabel = (JLabel) balancePanel.getComponent(1);
        amountLabel.setText(String.format("$%.2f", balance));

        // Update card number
        JPanel numberPanel = (JPanel) content.getComponent(1);
        numberPanel.removeAll();
        String maskedNumber = maskCardNumber(cardNumber);
        String[] segments = maskedNumber.split(" ");
        for (String segment : segments) {
            numberPanel.add(createCardSegment(segment, segment.length() == 4 ? 22 : 18));
        }

        // Update cardholder information
        JPanel bottomPanel = (JPanel) content.getComponent(2);
        JPanel holderPanel = (JPanel) bottomPanel.getComponent(0);
        JLabel nameLabel = (JLabel) holderPanel.getComponent(1);
        nameLabel.setText(cardholderName);

        // Update expiry date
        JPanel validPanel = (JPanel) bottomPanel.getComponent(1);
        JLabel dateLabel = (JLabel) validPanel.getComponent(1);
        dateLabel.setText(expiryDate);

        // Revalidate and repaint the panel
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Masks the middle part of a card number, showing only the first and last four digits.
     *
     * @param cardNumber The original card number string.
     * @return The masked card number string.
     */
    private String maskCardNumber(String cardNumber) {
        return cardNumber.substring(0, 4) + " **** **** " + cardNumber.substring(12);
    }
}