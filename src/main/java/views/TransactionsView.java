package views;

import constants.AppConstants;
import components.*;
import services.TransactionDataService;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import ui.AddCardDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Map;

/**
 * The {@code TransactionsView} class extends {@code BaseView} to display the transactions view interface.
 * This view includes a title, card panel, expense chart, and transaction table,
 * and supports viewing transaction records with potential pagination (pagination is placeholder).
 */
public class TransactionsView extends BaseView {
    /**
     * Gets the name of the view, used to identify this view.
     *
     * @return The view name "Transactions".
     */
    @Override
    public String getViewName() {
        return "Transactions";
    }

    /** Table displaying all transactions. */
    private JTable transactionTable;
    /** Model for the main transaction table. */
    private DefaultTableModel tableModel;
    /** Table displaying income transactions. */
    private JTable incomeTable;
    /** Table displaying expense transactions. */
    private JTable expenseTable;
    /** Model for the income table. */
    private DefaultTableModel incomeTableModel;
    /** Model for the expense table. */
    private DefaultTableModel expenseTableModel;

    // Add data service and chart-related fields
    /** Service for handling transaction data. */
    private TransactionDataService dataService;
    /** JFreeChart object for the expense chart. */
    private JFreeChart expenseChart;
    /** Panel displaying the expense chart. */
    private ChartPanel expenseChartPanel;

    /**
     * Constructs a new {@code TransactionsView}.
     */
    public TransactionsView() {
        // Initialize data service
        dataService = new TransactionDataService();
    }

    /**
     * Initializes the user interface.
     * Sets the layout, adds the title, card panel, expense chart, and transaction table.
     * Also initializes table models and loads initial data.
     */
    @Override
    public void initUI() {
        // Set layout manager with 15 pixel horizontal and vertical gaps
        setLayout(new BorderLayout(15, 15));
        // Set panel padding: 20 pixels top, left, bottom, right
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create top title label
        JLabel titleLabel = new JLabel("Transactions", SwingConstants.LEFT);
        // Set title font to the globally defined title font
        titleLabel.setFont(AppConstants.TITLE_FONT);
        // Set title label bottom padding to 15 pixels
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        // Add title label to the north position
        add(titleLabel, BorderLayout.NORTH);

        // Create main content panel, using a 1 row, 2 column grid layout with 15 pixel horizontal and vertical gaps
        RoundedPanel gridPanel = new RoundedPanel(new GridLayout(1, 2, 15, 15));
        // Add card panel wrapped with a title
        gridPanel.add(wrapComponent(createCardPanel(), "My Cards"));
        // Add expense chart wrapped with a title
        gridPanel.add(wrapComponent(createExpenseChart(), "My Expenses"));
        // Add main content panel to the center position
        add(gridPanel, BorderLayout.CENTER);

        // Initialize table models
        initializeTableModels();

        // Add transaction table panel to the south position
        add(createTransactionPanel(), BorderLayout.SOUTH);

        // Load initial transaction data into the data service
        loadInitialTransactions();
    }

    /**
     * Loads initial transaction data into the data service.
     * Data is currently loaded from the table model after initialization.
     */
    private void loadInitialTransactions() {
        try {
            // Get existing transaction data from the table
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String description = (String) tableModel.getValueAt(i, 0);
                String date = (String) tableModel.getValueAt(i, 4);
                String amount = (String) tableModel.getValueAt(i, 5);
                String type = amount.startsWith("-") ? "Expense" : "Income";

                // Clean up amount string
                String cleanAmount = amount.replace("$", "").replace("+", "").replace("-", "");

                // Add to data service
                dataService.addTransaction(date, description, cleanAmount, type);
            }

            // Initial chart update
            updateExpenseChart();
        } catch (Exception e) {
            // Add exception handling for better diagnosis
            System.err.println("Error in loadInitialTransactions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Initializes all table models (all transactions, income, and expense)
     * and creates the corresponding {@code JTable} instances.
     * Populates the tables with initial hardcoded data.
     */
    private void initializeTableModels() {
        // Define table column names
        String[] columnNames = { "Description", "Transaction ID", "Type", "Card", "Date", "Amount" };

        // Create main table model
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        // Create income table model
        incomeTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        // Create expense table model
        expenseTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        // Create tables
        transactionTable = new JTable(tableModel);
        incomeTable = new JTable(incomeTableModel);
        expenseTable = new JTable(expenseTableModel);

        // Set table styles
        transactionTable.setFont(new Font("Arial", Font.PLAIN, 14));
        transactionTable.setRowHeight(25);
        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        incomeTable.setFont(new Font("Arial", Font.PLAIN, 14));
        incomeTable.setRowHeight(25);
        incomeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        expenseTable.setFont(new Font("Arial", Font.PLAIN, 14));
        expenseTable.setRowHeight(25);
        expenseTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        Object[][] data = {
                { "Spotify Subscription", "123456", "Shopping", "1234******", "28 Jan, 12.30 AM", "-$2,500" },
                { "Freepik Sales", "789012", "Transfer", "5678******", "25 Jan, 10.40 PM", "+$750" },
                { "Mobile Service", "345678", "Service", "9012******", "20 Jan, 10.40 PM", "-$150" },
                { "Wilson", "901234", "Transfer", "3456******", "15 Jan, 03.29 PM", "-$1,050" },
                { "Emily", "567890", "Transfer", "7890******", "14 Jan, 10.40 PM", "+$840" }
        };

        // Add initial data to corresponding tables
        for (Object[] row : data) {
            // Add to main table
            tableModel.addRow(row);

            // Add to corresponding table based on amount (type)
            String amount = (String) row[5]; // Amount is in the 6th column
            if (amount.startsWith("-")) {
                expenseTableModel.addRow(row);
            } else {
                incomeTableModel.addRow(row);
            }
        }
    }

    /**
     * Creates the transaction table panel, including the title, tabbed pane, and pagination controls.
     *
     * @return A panel containing the transaction tables and pagination controls.
     */
    private JPanel createTransactionPanel() {
        // Create main panel using BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Top: Title
        JLabel titleLabel = new JLabel("Recent Transactions", SwingConstants.CENTER);
        // Set title font to Arial Bold, size 18
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        // Set title label top/bottom padding to 10 pixels
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        // Add title label to the north position
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Center: Tabbed pane and transaction tables
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add "All Transactions" tab
        tabbedPane.addTab("All Transactions", new JScrollPane(transactionTable));

        // Add "Income" tab
        tabbedPane.addTab("Income", new JScrollPane(incomeTable));

        // Add "Expense" tab
        tabbedPane.addTab("Expense", new JScrollPane(expenseTable));

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Bottom: Pagination controls
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prevButton = new JButton("<Previous");
        JButton nextButton = new JButton("Next>");
        JLabel pageLabel = new JLabel("1");
        // Add previous button to pagination panel
        paginationPanel.add(prevButton);
        // Add page number label to pagination panel
        paginationPanel.add(pageLabel);
        // Add next button to pagination panel
        paginationPanel.add(nextButton);
        // Add pagination panel to the south position
        mainPanel.add(paginationPanel, BorderLayout.SOUTH);

        // Pagination button events
        prevButton.addActionListener(_e -> {
            // Previous page logic
            JOptionPane.showMessageDialog(null, "上一页"); // Original Chinese comment
        });
        nextButton.addActionListener(_e -> {
            // Next page logic
            JOptionPane.showMessageDialog(null, "下一页"); // Original Chinese comment
        });

        // Set preferred size for the main panel to 800x350 pixels
        mainPanel.setPreferredSize(new Dimension(800, 350));

        return mainPanel;
    }

    /**
     * Creates the card panel, simulating bank card interfaces.
     * It contains card representations, balance, number, holder, and expiry information,
     * with horizontal scrolling and an "Add Card" function via a dialog.
     *
     * @return A panel containing the bank card information.
     */
    private JPanel createCardPanel() {
        // Create card container panel with horizontal BoxLayout
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
        
        // Create main panel
        RoundedPanel mainPanel = new RoundedPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Cast Graphics object to Graphics2D for advanced drawing features
                Graphics2D g2d = (Graphics2D) g;
                // Set drawing color to dark blue
                g2d.setColor(new Color(40, 80, 150));
                // Draw filled rounded rectangle covering the entire panel
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        // Set card panel padding: 25 pixels top, left, bottom, right
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25)); 
        // Set preferred size for the card panel to 320x200 pixels
        mainPanel.setPreferredSize(new Dimension(320, 200)); 

        // Create default card
        RoundedPanel defaultCard = createCard();
        
        // Create add button
        RoundedButton addCardButton = new RoundedButton("+ Add Card");
        addCardButton.setFont(new Font("Arial", Font.BOLD, 12));
        addCardButton.setBackground(new Color(255, 255, 255, 50));
        addCardButton.setForeground(Color.WHITE);
        addCardButton.setBorderPainted(false);
        addCardButton.setFocusPainted(false);
        addCardButton.setContentAreaFilled(false);
        addCardButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create button container panel for positioning the button
        JPanel buttonContainer = new JPanel(null); // Use absolute layout
        buttonContainer.setOpaque(false);
        buttonContainer.setPreferredSize(defaultCard.getPreferredSize());
        addCardButton.setBounds(
            defaultCard.getPreferredSize().width - 100, // Right margin
            10, // Top margin
            100, // Button width
            30  // Button height
        );
        buttonContainer.add(addCardButton);
        
        // Create layered pane for card and button overlay
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(defaultCard.getPreferredSize());
        layeredPane.add(defaultCard, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(buttonContainer, JLayeredPane.PALETTE_LAYER);
        
        // Set bounds for the card and button container
        defaultCard.setBounds(0, 0, defaultCard.getPreferredSize().width, defaultCard.getPreferredSize().height);
        buttonContainer.setBounds(0, 0, defaultCard.getPreferredSize().width, defaultCard.getPreferredSize().height);
        
        // Add button event listener
        addCardButton.addActionListener(e -> {
            AddCardDialog dialog = new AddCardDialog((Frame) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                // Get new card information
                String cardNumber = dialog.getCardNumber();
                String cardholderName = dialog.getCardholderName();
                String expiryDate = dialog.getExpiryDate();
                double balance = dialog.getBalance();
                
                // Create new card
                RoundedPanel newCard = createCard(cardNumber, cardholderName, expiryDate, balance);
                
                // Create new card's button container
                JPanel newButtonContainer = new JPanel(null);
                newButtonContainer.setOpaque(false);
                newButtonContainer.setPreferredSize(newCard.getPreferredSize());
                
                // Create new add button
                RoundedButton newAddButton = new RoundedButton("+ Add Card");
                newAddButton.setFont(addCardButton.getFont());
                newAddButton.setBackground(addCardButton.getBackground());
                newAddButton.setForeground(addCardButton.getForeground());
                newAddButton.setBorderPainted(false);
                newAddButton.setFocusPainted(false);
                newAddButton.setContentAreaFilled(false);
                newAddButton.setBorder(addCardButton.getBorder());
                newAddButton.setBounds(
                    newCard.getPreferredSize().width - 100,
                    10,
                    90,
                    30
                );
                newButtonContainer.add(newAddButton);
                
                // Create new card's layered pane
                JLayeredPane newLayeredPane = new JLayeredPane();
                newLayeredPane.setPreferredSize(newCard.getPreferredSize());
                newLayeredPane.add(newCard, JLayeredPane.DEFAULT_LAYER);
                newLayeredPane.add(newButtonContainer, JLayeredPane.PALETTE_LAYER);
                
                // Set bounds for the new card and button container
                newCard.setBounds(0, 0, newCard.getPreferredSize().width, newCard.getPreferredSize().height);
                newButtonContainer.setBounds(0, 0, newCard.getPreferredSize().width, newCard.getPreferredSize().height);
                
                // Add new card to the container
                cardsContainer.add(newLayeredPane);
                cardsContainer.add(Box.createHorizontalStrut(15)); // Add spacing between cards
                
                // Refresh container
                cardsContainer.revalidate();
                cardsContainer.repaint();
                
                // Scroll to the newly added card
                scrollPane.getHorizontalScrollBar().setValue(scrollPane.getHorizontalScrollBar().getMaximum());
                
                // Display success message
                JOptionPane.showMessageDialog(this,
                    "卡片添加成功！\n" + // Original Chinese comment
                    "卡号: " + maskCardNumber(cardNumber) + "\n" + // Original Chinese comment
                    "持卡人: " + cardholderName + "\n" + // Original Chinese comment
                    "有效期: " + expiryDate + "\n" + // Original Chinese comment
                    "余额: $" + String.format("%.2f", balance), // Original Chinese comment
                    "成功", // Original Chinese comment
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Add default card to the container
        cardsContainer.add(layeredPane);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }
    
    /**
     * Creates a default card panel with predefined data.
     *
     * @return A panel representing a bank card.
     */
    private RoundedPanel createCard() {
        return createCard("3778****1234", "Eddy Cusuma", "12/22", 5756.00);
    }
    
    /**
     * Creates a single card panel with the specified card details.
     * Uses custom painting for the rounded background and arranges details using GridBagLayout.
     *
     * @param cardNumber     The card number.
     * @param cardholderName The cardholder's name.
     * @param expiryDate     The card's expiry date (MM/YY).
     * @param balance        The card's balance.
     * @return A panel representing a bank card with the given details.
     */
    private RoundedPanel createCard(String cardNumber, String cardholderName, String expiryDate, double balance) {
        // Create rounded card container
        RoundedPanel card = new RoundedPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(40, 80, 150));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        // Set card panel padding: 25 pixels top, left, bottom, right
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25)); 
        // Set preferred size for the card panel to 320x200 pixels
        card.setPreferredSize(new Dimension(320, 200)); 

        // Main content container
        JPanel content = new JPanel(new GridBagLayout()) {
            /**
             * Override isOpaque method to make the panel background transparent.
             */
            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        // Create GridBag constraints object
        GridBagConstraints gbc = new GridBagConstraints();
        // Set component padding to 5 pixels
        gbc.insets = new Insets(5, 5, 5, 5);

        // Balance area (top-left)
        JPanel balancePanel = new JPanel(new BorderLayout());
        // Set balance panel background transparent
        balancePanel.setOpaque(false);
        JLabel balanceLabel = new JLabel("BALANCE");
        // Set balance label font to Arial Bold, size 12
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        // Set balance label font color to light gray
        balanceLabel.setForeground(new Color(180, 180, 220));

        JLabel amountLabel = new JLabel(String.format("$%.2f", balance));
        // Set amount label font to Arial Bold, size 24
        amountLabel.setFont(new Font("Arial", Font.BOLD, 24));
        // Set amount label font color to white
        amountLabel.setForeground(Color.WHITE);

        // Add balance label to the north position of the balance panel
        balancePanel.add(balanceLabel, BorderLayout.NORTH);
        // Add amount label to the center position of the balance panel
        balancePanel.add(amountLabel, BorderLayout.CENTER);

        // Card number area (middle-top)
        JPanel numberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        numberPanel.setOpaque(false);
        String[] segments = cardNumber.split(" ");
        if (segments.length == 1) {
            // If no space separator, split every 4 digits
            numberPanel.add(createCardSegment(cardNumber.substring(0, 4), 22));
            numberPanel.add(createCardSegment("****", 18));
            numberPanel.add(createCardSegment("****", 18));
            numberPanel.add(createCardSegment(cardNumber.substring(cardNumber.length() - 4), 22));
        } else {
            for (String segment : segments) {
                numberPanel.add(createCardSegment(segment, segment.length() == 4 ? 22 : 18));
            }
        }

        // Bottom information area
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        // Set bottom info panel background transparent
        bottomPanel.setOpaque(false);

        // Left side cardholder information
        JPanel holderPanel = new JPanel(new BorderLayout());
        // Set cardholder info panel background transparent
        holderPanel.setOpaque(false);
        JLabel holderLabel = new JLabel("CARDHOLDER");
        // Set cardholder label font to Arial Bold, size 10
        holderLabel.setFont(new Font("Arial", Font.BOLD, 10));
        // Set cardholder label font color to light gray
        holderLabel.setForeground(new Color(180, 180, 220));

        JLabel nameLabel = new JLabel(cardholderName);
        // Set cardholder name label font to Arial Bold, size 14
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        // Set cardholder name label font color to white
        nameLabel.setForeground(Color.WHITE);

        // Add cardholder label to the north position of the holder panel
        holderPanel.add(holderLabel, BorderLayout.NORTH);
        // Add cardholder name label to the center position of the holder panel
        holderPanel.add(nameLabel, BorderLayout.CENTER);

        // Right side expiry date information
        JPanel validPanel = new JPanel(new BorderLayout());
        // Set valid info panel background transparent
        validPanel.setOpaque(false);
        JLabel validLabel = new JLabel("VALID THRU");
        // Set valid thru label font to Arial Bold, size 10
        validLabel.setFont(new Font("Arial", Font.BOLD, 10));
        // Set valid thru label font color to light gray
        validLabel.setForeground(new Color(180, 180, 220));

        JLabel dateLabel = new JLabel(expiryDate);
        // Set expiry date label font to Arial Bold, size 14
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        // Set expiry date label font color to white
        dateLabel.setForeground(Color.WHITE);

        // Add valid thru label to the north position of the valid panel
        validPanel.add(validLabel, BorderLayout.NORTH);
        // Add expiry date label to the center position of the valid panel
        validPanel.add(dateLabel, BorderLayout.CENTER);

        // Layout combination
        // Set GridBag constraints x coordinate to 0
        gbc.gridx = 0;
        // Set GridBag constraints y coordinate to 0
        gbc.gridy = 0;
        // Set component alignment to top-left
        gbc.anchor = GridBagConstraints.NORTHWEST;
        // Add balance area to the content panel
        content.add(balancePanel, gbc);

        // Set GridBag constraints y coordinate to 1
        gbc.gridy = 1;
        // Set component horizontal weight to 1.0
        gbc.weightx = 1.0;
        // Set component fill to horizontal
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // Add card number area to the content panel
        content.add(numberPanel, gbc);

        // Add cardholder info to the bottom info panel
        bottomPanel.add(holderPanel);
        // Add valid thru info to the bottom info panel
        bottomPanel.add(validPanel);

        // Set GridBag constraints y coordinate to 2
        gbc.gridy = 2;
        // Set component vertical weight to 1.0
        gbc.weighty = 1.0;
        // Set component fill to both horizontal and vertical
        gbc.fill = GridBagConstraints.BOTH;
        // Add bottom information area to the content panel
        content.add(bottomPanel, gbc);

        card.add(content, BorderLayout.CENTER);
        return card;
    }
    
    /**
     * Masks the card number, showing only the first 4 and last 4 digits for longer numbers.
     *
     * @param cardNumber The original card number string.
     * @return The masked card number string.
     */
    private String maskCardNumber(String cardNumber) {
        if (cardNumber.length() >= 16) {
            return cardNumber.substring(0, 4) + " **** **** " + cardNumber.substring(12);
        } else {
            return cardNumber;
        }
    }

    /**
     * Creates a JLabel component for a segment of the card number.
     * Sets font, color, and padding for the segment text.
     *
     * @param text     The text content for the card segment (e.g., "1234" or "****").
     * @param fontSize The font size for the segment text.
     * @return A JLabel component displaying the card segment.
     */
    private JLabel createCardSegment(String text, int fontSize) {
        JLabel segment = new JLabel(text);
        // Set card segment label font to Arial Bold, specified size
        segment.setFont(new Font("Arial", Font.BOLD, fontSize));
        // Set card segment label font color to white
        segment.setForeground(Color.WHITE);
        // Set card segment label left/right padding to 5 pixels
        segment.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        return segment;
    }

    /**
     * Creates the expense chart, showing monthly expense breakdown by category.
     * Initializes a 3D bar chart with an empty dataset.
     *
     * @return A ChartPanel containing the 3D bar chart.
     */
    private ChartPanel createExpenseChart() {
        // Create default category dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Initialize with empty dataset, will be updated later in updateExpenseChart
        String[] categories = { "Housing", "Food", "Transport", "Entertainment", "Savings", "Others" };
        for (String category : categories) {
            dataset.addValue(0, "Expenses", category);
        }

        // Create 3D bar chart
        expenseChart = ChartFactory.createBarChart3D(
                "", // Chart title
                "Category", // Category axis label
                "Amount ($)", // Value axis label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Chart orientation
                true, // Whether to display legend
                true, // Whether to generate tooltips
                false // Whether to generate URLs
        );

        // Customize chart style
        expenseChart.setBackgroundPaint(Color.WHITE);

        // Create chart panel
        expenseChartPanel = new ChartPanel(expenseChart);
        expenseChartPanel.setPreferredSize(new Dimension(400, 300));

        return expenseChartPanel;
    }

    /**
     * Updates the expense chart with the latest data from the transaction data service.
     * Recreates the dataset based on current expense categories and their amounts.
     */
    private void updateExpenseChart() {
        try {
            if (dataService != null && expenseChart != null) {
                // Get category expense data
                Map<String, Double> categoryData = dataService.getExpenseCategories();

                // Create a new dataset
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();

                // Add data
                for (Map.Entry<String, Double> entry : categoryData.entrySet()) {
                    dataset.addValue(entry.getValue(), "Expenses", entry.getKey());
                }

                // If no data, add default category
                if (categoryData.isEmpty()) {
                    dataset.addValue(0, "Expenses", "No Data");
                }

                // Update chart dataset
                expenseChart.getCategoryPlot().setDataset(dataset);
            }
        } catch (Exception e) {
            System.err.println("Error updating expense chart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Wraps a component within a panel with a titled border and a white background.
     * Useful for organizing sections of the UI.
     *
     * @param comp  The component to wrap.
     * @param title The title for the border.
     * @return A JPanel containing the wrapped component and title border.
     */
    private JPanel wrapComponent(Component comp, String title) {
        // Create panel using BorderLayout
        JPanel wrapper = new JPanel(new BorderLayout());
        // Set titled border for the panel
        wrapper.setBorder(BorderFactory.createTitledBorder(title));
        // Set panel background color to white
        wrapper.setBackground(Color.WHITE);
        // Add component to the center position of the panel
        wrapper.add(comp, BorderLayout.CENTER);
        return wrapper;
    }

    /**
     * Adds a new transaction record to the view.
     * This includes adding the transaction to the relevant tables and updating the expense chart.
     * A random transaction ID and masked card number are generated for display.
     *
     * @param date        The date of the transaction.
     * @param description The description of the transaction.
     * @param amount      The transaction amount (e.g., "-$2,500" or "+$750").
     * @param type        The conceptual type of the transaction (e.g., "Shopping", "Transfer", "Service").
     */
    public void addTransaction(String date, String description, String amount, String type) {
        try {
            // Generate random transaction ID
            String transactionId = String.format("%06d", (int) (Math.random() * 1000000));
            // Generate random card number
            String cardNumber = String.format("%04d******", (int) (Math.random() * 10000));

            // Determine transaction type based on amount
            String actualType = amount.startsWith("-") ? "Expense" : "Income";

            // Add new row to the table
            Object[] rowData = {
                    description,
                    transactionId,
                    type,
                    cardNumber,
                    date,
                    amount
            };

            tableModel.addRow(rowData);

            // Add to corresponding tables based on actual type
            if (actualType.equals("Income")) {
                incomeTableModel.addRow(rowData);
            } else {
                expenseTableModel.addRow(rowData);
            }

            // Add to data service
            String cleanAmount = amount.replace("$", "").replace("+", "").replace("-", "");
            dataService.addTransaction(date, description, cleanAmount, type);

            // Update chart
            updateExpenseChart();
        } catch (Exception e) {
            System.err.println("Error adding transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Removes a transaction record from the view based on matching details.
     * Attempts to find and remove the transaction from the main and type-specific tables.
     * Note: The underlying data service is not fully updated by this method in the current implementation.
     * The expense chart is updated after removal from tables.
     *
     * @param date        The date of the transaction to remove.
     * @param description The description of the transaction to remove.
     * @param amount      The amount of the transaction to remove.
     * @param type        The type of the transaction (used to determine which specific table to check).
     */
    public void removeTransaction(String date, String description, String amount, String type) {
        try {
            // Find and remove matching row in the main table
            for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
                if (tableModel.getValueAt(i, 0).equals(description) &&
                        tableModel.getValueAt(i, 4).equals(date) &&
                        tableModel.getValueAt(i, 5).equals(amount)) {
                    tableModel.removeRow(i);
                    break;
                }
            }

            // Find and remove matching row in the corresponding table based on type
            String actualType = amount.startsWith("-") ? "Expense" : "Income";
            DefaultTableModel targetModel = actualType.equals("Income") ? incomeTableModel : expenseTableModel;

            for (int i = targetModel.getRowCount() - 1; i >= 0; i--) {
                if (targetModel.getValueAt(i, 0).equals(description) &&
                        targetModel.getValueAt(i, 4).equals(date) &&
                        targetModel.getValueAt(i, 5).equals(amount)) {
                    targetModel.removeRow(i);
                    break;
                }
            }

            // Data in the data service should ideally be updated here
            // However, TransactionDataService currently does not support removing single transactions
            // Therefore, all transaction data would need to be rebuilt

            // Update chart
            updateExpenseChart();
        } catch (Exception e) {
            System.err.println("Error removing transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }
}