package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * A Swing JFrame providing options for the user to input financial data.
 * It offers two primary methods: manual entry through a dialog and
 * importing data from a CSV file. The frame features a background image
 * and navigation buttons.
 */
public class DataEntryPage {
    
    /**
     * The main JFrame for this data entry page.
     */
    private JFrame frame;
    
    /**
     * The username of the user, used for potential personalization
     * (though not explicitly shown in this code).
     */
    private String username;

    /**
     * An inner class extending JPanel to draw a background image.
     */
    class BackgroundPanel extends JPanel {
        
        /**
         * The image to be displayed as the background of the panel.
         */
        private Image backgroundImage;

        /**
         * Constructs a BackgroundPanel with the specified image path.
         * The image is loaded and scaled to fit the panel.
         *
         * @param imagePath The path to the background image file.
         */
        public BackgroundPanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
            setLayout(new FlowLayout());
        }

        /**
         * Overrides the paintComponent method to draw the background image onto the panel.
         *
         * @param g The Graphics context used for drawing.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Constructs a new DataEntryPage frame.
     * Initializes the UI components, including the background, title,
     * feature buttons, and their action listeners.
     *
     * @param username The username for whom this data entry page is displayed.
     */
    public DataEntryPage(String username) {
        this.username = username;
        frame = new JFrame("Data Entry");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window, not the entire application
        frame.setSize(800, 600);

        // Create background panel
        BackgroundPanel backgroundPanel = new BackgroundPanel(
                "Version_1.0\\src\\com\\main\\java\\photo\\background.jpg");
        backgroundPanel.setLayout(null);
        frame.setContentPane(backgroundPanel);

        // Add title label
        JLabel titleLabel = new JLabel("Data Entry");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 30, 700, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundPanel.add(titleLabel);

        // Create main panel for buttons
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setOpaque(false); // Make the panel transparent to show the background
        mainPanel.setBounds(100, 100, 600, 400); // Position and size relative to backgroundPanel

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 20, 20, 20); // Spacing between components
        gbc.gridwidth = 1; // Default to 1 column

        // Create two main feature buttons
        JButton manualEntryButton = createFeatureButton(
                "Manual Data Entry",
                0, 0, gbc, mainPanel); // gridx=0, gridy=0

        JButton csvImportButton = createFeatureButton(
                "Import CSV File",
                0, 1, gbc, mainPanel); // gridx=0, gridy=1

        // Back button
        JButton backButton = createFeatureButton(
                "Back to Main Menu",
                0, 2, gbc, mainPanel); // gridx=0, gridy=2

        // Add action listeners to buttons
        manualEntryButton.addActionListener(e -> {
            openManualEntryDialog();
        });

        csvImportButton.addActionListener(e -> {
            openFileChooser();
        });

        backButton.addActionListener(e -> {
            frame.dispose(); // Close the current window
        });

        backgroundPanel.add(mainPanel);
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
    }

    /**
     * Opens a modal dialog window for manual transaction data entry.
     * Provides fields for amount, description, and category, with a placeholder
     * for the save logic.
     */
    private void openManualEntryDialog() {
        JDialog dialog = new JDialog(frame, "Manual Entry", true); // Modal dialog
        dialog.setSize(400, 300);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Spacing within the dialog

        // Add input fields
        JTextField amountField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);
        JComboBox<String> categoryBox = new JComboBox<>(new String[] {
                "Food", "Transportation", "Entertainment", "Shopping", "Bills", "Others"
        });

        // Add components using GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Amount: "), gbc);
        gbc.gridx = 1;
        dialog.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Description: "), gbc);
        gbc.gridx = 1;
        dialog.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Category: "), gbc);
        gbc.gridx = 1;
        dialog.add(categoryBox, gbc);

        // Add save button
        JButton saveButton = new JButton("Save");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span across two columns
        dialog.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            // TODO: Implement actual save logic here
            JOptionPane.showMessageDialog(dialog, "Transaction saved!");
            dialog.dispose(); // Close dialog after saving
        });

        dialog.setLocationRelativeTo(frame); // Center dialog relative to the main frame
        dialog.setVisible(true);
    }

    /**
     * Opens a file chooser dialog allowing the user to select a CSV file for import.
     * Includes a file filter specifically for CSV files.
     */
    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        // Set file filter for CSV files
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            /**
             * Accepts directories or files ending with .csv (case-insensitive).
             * @param f The file to check.
             * @return true if the file is a directory or ends with .csv, false otherwise.
             */
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".csv") || f.isDirectory();
            }

            /**
             * Returns the description of the file filter.
             * @return The filter description string.
             */
            public String getDescription() {
                return "CSV Files (*.csv)";
            }
        });

        int result = fileChooser.showOpenDialog(frame); // Show open dialog
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            // TODO: Implement CSV file import logic here
            JOptionPane.showMessageDialog(frame, "Selected file: " + selectedFile.getName());
        }
    }

    /**
     * Creates a styled JButton with a specific title and adds it to the given
     * panel using GridBagLayout. Includes basic hover effects.
     *
     * @param title The text to display on the button.
     * @param x The gridx position for the button in the GridBagLayout.
     * @param y The gridy position for the button in the GridBagLayout.
     * @param gbc The GridBagConstraints to use for adding the button.
     * @param panel The JPanel to which the button should be added.
     * @return The created JButton.
     */
    private JButton createFeatureButton(String title, int x, int y, GridBagConstraints gbc, JPanel panel) {
        JButton button = new JButton(title);
        button.setPreferredSize(new Dimension(500, 70)); // Set preferred size for uniform buttons
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false); // Remove focus border
        button.setBackground(new Color(51, 122, 183)); // Bootstrap primary blue color
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false); // Remove border
        button.setOpaque(true); // Ensure background is painted

        // Add mouse listeners for hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            /**
             * Changes button background color when mouse enters.
             * @param evt The mouse event.
             */
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(40, 96, 144)); // Darker blue on hover
            }

            /**
             * Restores button background color when mouse exits.
             * @param evt The mouse event.
             */
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(51, 122, 183)); // Original color
            }
        });

        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(button, gbc); // Add button to the panel with specified constraints
        return button;
    }
}