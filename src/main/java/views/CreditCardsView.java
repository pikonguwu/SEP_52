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
 * The CreditCardsView class extends BaseView and provides a view interface for displaying credit card-related information.
 * This view includes a list of credit cards, statistical charts, and a form for adding new credit cards.
 */
public class CreditCardsView extends BaseView {
    /**
     * Pie chart dataset, used to store credit card account statistics.
     */
    private DefaultPieDataset pieDataset;

    /**
     * Gets the name of the view, used to identify this view when switching between views.
     *
     * @return The name of the view, fixed as "Credit Cards".
     */
    @Override
    public String getViewName() {
        return "Credit Cards";
    }

    /**
     * Initializes the user interface, setting the layout, adding the title, main content area, and add card form.
     */
    @Override
    protected void initUI() {
        // Set layout manager, horizontal and vertical gap is 15 pixels
        setLayout(new BorderLayout(15, 15));
        // Set panel padding, top, left, bottom, right are all 20 pixels
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top title
        // Create title label, text is "My Cards", left aligned
        JLabel titleLabel = new JLabel("My Cards", SwingConstants.LEFT);
        // Set title font to Arial Bold, size 24
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        // Add title label to the north position of the panel
        add(titleLabel, BorderLayout.NORTH);

        // Main content area
        // Create a panel using BorderLayout, horizontal and vertical gap is 15 pixels
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));

        // Card list and statistics
        // Create a panel using 1x2 GridLayout, horizontal and vertical gap is 15 pixels
        JPanel upperPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        // Add credit card list panel
        upperPanel.add(createCardListPanel());
        // Add statistics panel
        upperPanel.add(createStatisticsPanel());

        // Add card form
        // Add the panel containing card list and statistics to the center position of the main panel
        mainPanel.add(upperPanel, BorderLayout.CENTER);
        // Add the add card form to the south position of the main panel
        mainPanel.add(createAddCardForm(), BorderLayout.SOUTH);

        // Add the main panel to the center position of the current view
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Creates the credit card list panel, containing a table to display credit card information.
     *
     * @return A panel containing the credit card list table.
     */
    private JPanel createCardListPanel() {
        // Create a rounded panel, using BorderLayout
        RoundedPanel panel = new RoundedPanel(new BorderLayout());
        // Set a titled border for the panel, title is "Card List"
        panel.setBorder(new TitledBorder("Card List"));

        // Define table column names
        String[] columns = {"Card Type", "Bank", "Card Number", "Name", "Actions"};
        // Define table initial data
        Object[][] data = {
            {"Secondary", "DBL Bank", "**** 5600", "William", "View Details"},
            {"Secondary", "BRC Bank", "**** 7560", "Michel", "View Details"},
            {"Classic", "ABM Bank", "**** 1234", "Edward", "View Details"}
        };

        // Create a table and override the getColumnClass method to specify the type of the 4th column as JButton
        JTable table = new JTable(data, columns) {
            /**
             * Gets the data type of the specified column.
             *
             * @param column The column index.
             * @return The data type of the column, returns JButton class if it is the 4th column, otherwise returns Object class.
             */
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 4 ? JButton.class : Object.class;
            }
        };

        // Set table row height to 40 pixels
        table.setRowHeight(40);
        // Set table header font to Arial Bold, size 14
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        // Do not show table grid lines
        table.setShowGrid(false);

        // Get the 4th column of the table
        TableColumn actionColumn = table.getColumnModel().getColumn(4);
        // Set custom cell renderer for this column
        actionColumn.setCellRenderer(new ButtonRenderer());
        // Set custom cell editor for this column
        actionColumn.setCellEditor(new ButtonEditor(new JCheckBox()));

        // Create a scroll pane and add the table to it
        JScrollPane scrollPane = new JScrollPane(table);
        // Remove scroll pane border
        scrollPane.setBorder(null);
        // Add the scroll pane to the center position of the rounded panel
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the statistics panel, containing a pie chart to display credit card account statistics.
     *
     * @return A statistics panel containing the pie chart.
     */
    private JPanel createStatisticsPanel() {
        // Create a rounded panel, using BorderLayout
        RoundedPanel panel = new RoundedPanel(new BorderLayout());
        // Set a titled border for the panel, title is "Card Account Statistics"
        panel.setBorder(new TitledBorder("Card Account Statistics"));

        // Initialize pie chart dataset
        pieDataset = new DefaultPieDataset();
        // Add DBL Bank data to the dataset
        pieDataset.setValue("DBL Bank", 35);
        // Add BRC Bank data to the dataset
        pieDataset.setValue("BRC Bank", 25);
        // Add ABM Bank data to the dataset
        pieDataset.setValue("ABM Bank", 20);
        // Add MCP Bank data to the dataset
        pieDataset.setValue("MCP Bank", 20);

        // Create a pie chart, no title, use the specified dataset, display legend and tooltips
        JFreeChart chart = ChartFactory.createPieChart(
            "", pieDataset, true, true, false
        );

        // Get the pie chart plot area
        PiePlot plot = (PiePlot) chart.getPlot();

        // Set colors using the correct approach
        int index = 0;
        // Iterate through keys in the dataset
        for (Object key : pieDataset.getKeys()) {
            switch (index) {
                case 0:
                    // Set color for the 0th section
                    plot.setSectionPaint(index, new Color(40, 80, 150));
                    break;
                case 1:
                    // Set color for the 1st section
                    plot.setSectionPaint(index, new Color(80, 120, 200));
                    break;
                case 2:
                    // Set color for the 2nd section
                    plot.setSectionPaint(index, new Color(120, 160, 220));
                    break;
                case 3:
                    // Set color for the 3rd section
                    plot.setSectionPaint(index, new Color(160, 200, 240));
                    break;
            }
            index++;
        }

        // Set plot area background color to null
        plot.setBackgroundPaint(null);
        // Set plot area outline color to null
        plot.setOutlinePaint(null);

        // Create a chart panel and add the pie chart to it
        ChartPanel chartPanel = new ChartPanel(chart);
        // Add the chart panel to the center position of the rounded panel
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates the form panel for adding a new credit card.
     *
     * @return A panel containing the add new credit card form.
     */
    private JPanel createAddCardForm() {
        // Create a rounded panel
        RoundedPanel panel = new RoundedPanel();
        // Set a titled border for the panel, title is "Add New Card"
        panel.setBorder(new TitledBorder("Add New Card"));
        // Set panel layout to 2x1 GridLayout, horizontal and vertical gap is 15 pixels
        panel.setLayout(new GridLayout(2, 1, 15, 15));

        // Create a panel using 2x4 GridLayout, for placing form fields
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 15, 15));

        // Create a combo box containing credit card type options
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Classic", "Premium", "Platinum"});
        // Create a text field for entering cardholder name
        JTextField nameField = new JTextField();
        // Create a text field for entering credit card number
        JTextField numberField = new JTextField();
        // Create a text field for entering credit card expiry date
        JTextField expiryField = new JTextField();

        // Add credit card type field to the form panel
        formPanel.add(createFormField("Card Type:", typeCombo));
        // Add cardholder name field to the form panel
        formPanel.add(createFormField("Name On Card:", nameField));
        // Add credit card number field to the form panel
        formPanel.add(createFormField("Card Number:", numberField));
        // Add credit card expiry date field to the form panel
        formPanel.add(createFormField("Expiration Date:", expiryField));

        // Create a rounded button, text is "Add Card"
        RoundedButton addButton = new RoundedButton("Add Card");
        // Set button font to Arial Bold, size 16
        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        // Set button background color
        addButton.setBackground(new Color(40, 80, 150));
        // Set button text color to white
        addButton.setForeground(Color.WHITE);
        // Add action listener to the button, call handleAddCard method when clicked
        addButton.addActionListener(e -> handleAddCard());

        // Add the form panel to the rounded panel
        panel.add(formPanel);
        // Add the add button to the rounded panel
        panel.add(addButton);

        return panel;
    }

    /**
     * Creates a form field panel, containing a label and the corresponding input component.
     *
     * @param label The field label text.
     * @param field The input component corresponding to the field.
     * @return A panel containing the label and the input component.
     */
    private JPanel createFormField(String label, Component field) {
        // Create a panel using BorderLayout, horizontal and vertical gap is 10 pixels
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        // Create a label, displaying the specified text
        JLabel jLabel = new JLabel(label);
        // Set label font to Arial Plain, size 14
        jLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // If the input component is a JTextField, set a light gray border for it
        if(field instanceof JTextField) {
            ((JTextField) field).setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        }

        // Add the label to the west position of the panel
        panel.add(jLabel, BorderLayout.WEST);
        // Add the input component to the center position of the panel
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Handles the logic for adding a credit card, displaying a message dialog indicating successful addition.
     */
    private void handleAddCard() {
        // Display a message dialog, indicating successful credit card addition
        JOptionPane.showMessageDialog(this, "Card Added Successfully!");
    }

    /**
     * Custom table cell renderer, used for rendering button columns in the table.
     */
    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        /**
         * Constructor, sets button background visible.
         */
        public ButtonRenderer() {
            setOpaque(true);
        }

        /**
         * Gets the component used for rendering the table cell.
         *
         * @param table      The table containing the cell.
         * @param value      The value of the cell.
         * @param isSelected Whether the cell is selected.
         * @param hasFocus   Whether the cell has focus.
         * @param row        The row of the cell.
         * @param column     The column of the cell.
         * @return The component used for rendering the cell, which is this button.
         */
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            // Set button text to the cell value
            setText(value.toString());
            // Set button background color
            setBackground(new Color(40, 80, 150));
            // Set button text color to white
            setForeground(Color.WHITE);
            // Do not paint button border
            setBorderPainted(false);
            return this;
        }
    }

    /**
     * Custom table cell editor, used for editing button columns in the table.
     */
    private static class ButtonEditor extends DefaultCellEditor {
        /**
         * Button component used for editing.
         */
        private JButton button;
        /**
         * The text displayed on the button.
         */
        private String label;

        /**
         * Constructor, initializes the button and adds an action listener.
         *
         * @param checkBox The checkbox used to initialize the superclass.
         */
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            // Create a button
            button = new JButton();
            // Set button background visible
            button.setOpaque(true);
            // Add action listener to the button, trigger editing stopped event when clicked
            button.addActionListener(e -> fireEditingStopped());
        }

        /**
         * Gets the component used for editing the table cell.
         *
         * @param table      The table containing the cell.
         * @param value      The value of the cell.
         * @param isSelected Whether the cell is selected.
         * @param row        The row of the cell.
         * @param column     The column of the cell.
         * @return The component used for editing the cell, which is the button.
         */
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            // Get the cell value as the button text
            label = (value == null) ? "" : value.toString();
            // Set the button text
            button.setText(label);
            return button;
        }

        /**
         * Gets the edited cell value.
         *
         * @return The edited cell value, which is the button text.
         */
        public Object getCellEditorValue() {
            return label;
        }
    }
}