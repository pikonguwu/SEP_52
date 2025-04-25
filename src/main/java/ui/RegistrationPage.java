package ui;

import data.UserDataStorage;
import ui.LoginPage;
import components.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationPage {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    // 添加自定义面板类来支持背景图片
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
            setLayout(new FlowLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public RegistrationPage() {
        frame = new JFrame("Registration Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);

        // 创建背景面板
        BackgroundPanel backgroundPanel = new BackgroundPanel(
                "Version_1.0\\src\\com\\main\\java\\photo\\background.jpg");
        backgroundPanel.setLayout(null);
        frame.setContentPane(backgroundPanel);

        // 添加标题
        JLabel titleLabel = new JLabel("Registration Page");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 50, 1100, 50); // 调整标题宽度以适应新窗口
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundPanel.add(titleLabel);

        // 创建一个面板来容纳注册组件
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridBagLayout());
        registerPanel.setOpaque(false);
        registerPanel.setBounds((1200 - 400) / 2, (800 - 300) / 2, 400, 300); // 居中

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        registerPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 0;
        registerPanel.add(usernameField, gbc);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        registerPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 1;
        registerPanel.add(passwordField, gbc);

        // Confirm Password label and field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setForeground(Color.WHITE);
        confirmPasswordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        registerPanel.add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 2;
        registerPanel.add(confirmPasswordField, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // 居中对齐
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        registerPanel.add(buttonPanel, gbc);

        // Register Button
        RoundedButton registerButton = new RoundedButton("Register");
        registerButton.setPreferredSize(new Dimension(100, 35));
        registerButton.setBackground(new Color(0, 123, 255));
        registerButton.setForeground(Color.WHITE);
        buttonPanel.add(registerButton);
        registerButton.addActionListener(new ActionListener() {
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
                    UserDataStorage.registerUser(username, password);
                    JOptionPane.showMessageDialog(frame, "Registration Successful!");
                    new LoginPage();
                    frame.dispose();
                }
            }
        });

        // Back to Login Button
        RoundedButton backButton = new RoundedButton("Back to Login");
        backButton.setPreferredSize(new Dimension(100, 35));
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        buttonPanel.add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginPage();
                frame.dispose();
            }
        });

        backgroundPanel.add(registerPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
