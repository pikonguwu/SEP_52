package ui;

import data.UserDataStorage;
import ui.FinanceTrackerUI;
import ui.RegistrationPage;
import components.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Represents the login page of the application.
 * This class provides a graphical user interface for users to enter
 * their username and password to log in, or navigate to the registration page.
 * It features a background image and uses custom components like RoundedButton.
 */
public class LoginPage {
    
    /**
     * The main JFrame for the login page.
     */
    private JFrame frame;
    
    /**
     * The text field where the user enters their username.
     */
    private JTextField usernameField;
    
    /**
     * The password field where the user enters their password.
     */
    private JPasswordField passwordField;

    /**
     * An inner class extending JPanel to draw a background image.
     * This allows setting a custom background for the JFrame's content pane.
     */
    class BackgroundPanel extends JPanel {
        
        /**
         * The image to be displayed as the background of the panel.
         */
        private Image backgroundImage;

        /**
         * Constructs a BackgroundPanel with the specified image path.
         * The image is loaded and stored.
         *
         * @param imagePath The path to the background image file.
         */
        public BackgroundPanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
            setLayout(new FlowLayout()); // Use FlowLayout for the panel, though components are placed using null layout on the main panel
        }

        /**
         * Overrides the paintComponent method to draw the background image onto the panel.
         * The image is scaled to fit the current dimensions of the panel.
         *
         * @param g The Graphics context used for drawing.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the background image, scaling it to the panel size
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Constructs a new LoginPage.
     * Initializes the JFrame, sets up the background panel, adds the title,
     * creates input fields for username and password, and includes Login and Register buttons
     * with their respective action listeners.
     */
    public LoginPage() {
        frame = new JFrame("Login Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);

        // Create background panel and set it as the frame's content pane
        BackgroundPanel backgroundPanel = new BackgroundPanel(
                "bin\\photo\\background.jpg");
        backgroundPanel.setLayout(null); // Use null layout on the background panel for precise positioning
        frame.setContentPane(backgroundPanel);

        // Add title label
        JLabel titleLabel = new JLabel("BuckBrain Login Page");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 50, 1100, 50); // Adjust title width to fit the new window size
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundPanel.add(titleLabel);

        // Create a panel to hold the login components (fields and buttons)
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for arranging login fields and buttons
        loginPanel.setOpaque(false); // Make the panel transparent to see the background
        loginPanel.setBounds((1200 - 400) / 2, (800 - 300) / 2, 400, 300); // Center the login panel on the background panel

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // Components fill their display area horizontally
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around components

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; // Column 0
        gbc.gridy = 0; // Row 0
        loginPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(200, 30)); // Preferred size for the text field
        gbc.gridx = 1; // Column 1
        gbc.gridy = 0; // Row 0
        loginPanel.add(usernameField, gbc);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; // Column 0
        gbc.gridy = 1; // Row 1
        loginPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30)); // Preferred size for the password field
        gbc.gridx = 1; // Column 1
        gbc.gridy = 1; // Row 1
        loginPanel.add(passwordField, gbc);

        // Panel for buttons (Login and Register)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make the button panel transparent
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Center alignment with spacing
        gbc.gridx = 0; // Start at column 0
        gbc.gridy = 2; // Row 2
        gbc.gridwidth = 2; // Span across two columns
        gbc.insets = new Insets(20, 10, 10, 10); // More top padding for spacing from fields
        loginPanel.add(buttonPanel, gbc);

        // Login Button
        RoundedButton loginButton = new RoundedButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 35)); // Preferred size for the button
        loginButton.setBackground(new Color(0, 123, 255)); // Bootstrap primary blue color
        loginButton.setForeground(Color.WHITE);
        buttonPanel.add(loginButton);
        loginButton.addActionListener(new ActionListener() {
            /**
             * Handles the action when the Login button is clicked.
             * Authenticates the user and opens the main application frame if successful,
             * otherwise displays an error message.
             * @param e The ActionEvent.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (UserDataStorage.authenticateUser(username, password)) {
                    // JOptionPane.showMessageDialog(frame, "Login Successful!");
                    new FinanceTrackerUI(username); // Open the main application UI
                    frame.dispose(); // Close the login frame
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Register Button
        RoundedButton registerButton = new RoundedButton("Register");
        registerButton.setPreferredSize(new Dimension(100, 35)); // Preferred size for the button
        registerButton.setBackground(new Color(108, 117, 125)); // Bootstrap secondary grey color
        registerButton.setForeground(Color.WHITE);
        buttonPanel.add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            /**
             * Handles the action when the Register button is clicked.
             * Opens the registration page and closes the current login frame.
             * @param e The ActionEvent.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistrationPage(); // Open the registration UI
                frame.dispose(); // Close the login frame
            }
        });

        backgroundPanel.add(loginPanel); // Add the login components panel to the background panel
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true); // Make the frame visible
    }

    /**
     * The main method to start the application by creating a LoginPage instance.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Run the GUI creation on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginPage();
            }
        });
    }
}