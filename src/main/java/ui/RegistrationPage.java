package ui;

import data.UserDataStorage;
import ui.LoginPage;
import components.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Represents the registration page of the application.
 * This class provides a graphical user interface for users to register
 * by entering a desired username and password. It includes input validation
 * and navigates back to the login page upon successful registration or cancellation.
 * It features a background image and uses custom components like RoundedButton.
 */
public class RegistrationPage {
    
    /**
     * The main JFrame for the registration page.
     */
    private JFrame frame;
    
    /**
     * The text field where the user enters their desired username.
     */
    private JTextField usernameField;
    
    /**
     * The password field where the user enters their desired password.
     */
    private JPasswordField passwordField;
    
    /**
     * The password field where the user re-enters their password for confirmation.
     */
    private JPasswordField confirmPasswordField;

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
     * Constructs a new RegistrationPage.
     * Initializes the JFrame, sets up the background panel, adds the title,
     * creates input fields for username, password, and password confirmation,
     * and includes Register and Back to Login buttons with their respective
     * action listeners. Sets up layout using GridBagLayout for input fields
     * and FlowLayout for buttons.
     */
    public RegistrationPage() {
        frame = new JFrame("Registration Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);

        // 创建背景面板
        BackgroundPanel backgroundPanel = new BackgroundPanel(
                "Version_1.0\\src\\com\\main\\java\\photo\\background.jpg");
        backgroundPanel.setLayout(null); // Use null layout on the background panel for precise positioning
        frame.setContentPane(backgroundPanel);

        // 添加标题
        JLabel titleLabel = new JLabel("Registration Page");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 50, 1100, 50); // Adjust title width to fit the new window size
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundPanel.add(titleLabel);

        // 创建一个面板来容纳注册组件
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for arranging registration fields and buttons
        registerPanel.setOpaque(false); // Make the panel transparent to see the background
        registerPanel.setBounds((1200 - 400) / 2, (800 - 300) / 2, 400, 300); // Center the registration panel on the background panel

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // Components fill their display area horizontally
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around components

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; // Column 0
        gbc.gridy = 0; // Row 0
        registerPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(200, 30)); // Preferred size for the text field
        gbc.gridx = 1; // Column 1
        gbc.gridy = 0; // Row 0
        registerPanel.add(usernameField, gbc);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; // Column 0
        gbc.gridy = 1; // Row 1
        registerPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30)); // Preferred size for the password field
        gbc.gridx = 1; // Column 1
        gbc.gridy = 1; // Row 1
        registerPanel.add(passwordField, gbc);

        // Confirm Password label and field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setForeground(Color.WHITE);
        confirmPasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; // Column 0
        gbc.gridy = 2; // Row 2
        registerPanel.add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setPreferredSize(new Dimension(200, 30)); // Preferred size for the password field
        gbc.gridx = 1; // Column 1
        gbc.gridy = 2; // Row 2
        registerPanel.add(confirmPasswordField, gbc);

        // Panel for buttons (Register and Back to Login)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make the button panel transparent
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Center alignment with spacing
        gbc.gridx = 0; // Start at column 0
        gbc.gridy = 3; // Row 3
        gbc.gridwidth = 2; // Span across two columns
        gbc.insets = new Insets(20, 10, 10, 10); // More top padding for spacing from fields
        registerPanel.add(buttonPanel, gbc);

        // Register Button
        RoundedButton registerButton = new RoundedButton("Register");
        registerButton.setPreferredSize(new Dimension(100, 35)); // Preferred size for the button
        registerButton.setBackground(new Color(0, 123, 255)); // Bootstrap primary blue color
        registerButton.setForeground(Color.WHITE);
        buttonPanel.add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            /**
             * Handles the action when the Register button is clicked.
             * Validates input fields, checks if passwords match, attempts to register
             * the user, displays messages, and navigates to the login page on success.
             * @param e The ActionEvent.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(frame, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Assume registerUser handles saving the new user data
                    UserDataStorage.registerUser(username, password);
                    JOptionPane.showMessageDialog(frame, "Registration Successful!");
                    new LoginPage(); // Open the login UI
                    frame.dispose(); // Close the registration frame
                }
            }
        });

        // Back to Login Button
        RoundedButton backButton = new RoundedButton("Back to Login");
        backButton.setPreferredSize(new Dimension(100, 35)); // Preferred size for the button
        backButton.setBackground(new Color(108, 117, 125)); // Bootstrap secondary grey color
        backButton.setForeground(Color.WHITE);
        buttonPanel.add(backButton);
        backButton.addActionListener(new ActionListener() {
            /**
             * Handles the action when the Back to Login button is clicked.
             * Opens the login page and closes the current registration frame.
             * @param e The ActionEvent.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginPage(); // Open the login UI
                frame.dispose(); // Close the registration frame
            }
        });

        backgroundPanel.add(registerPanel); // Add the registration components panel to the background panel
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true); // Make the frame visible
    }
}