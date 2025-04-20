package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class DataEntryPage {
    private JFrame frame;
    private String username;

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

    public DataEntryPage(String username) {
        this.username = username;
        frame = new JFrame("Data Entry");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 关闭当前窗口而不是整个应用
        frame.setSize(800, 600);

        // 创建背景面板
        BackgroundPanel backgroundPanel = new BackgroundPanel(
                "Version_1.0\\src\\com\\main\\java\\photo\\background.jpg");
        backgroundPanel.setLayout(null);
        frame.setContentPane(backgroundPanel);

        // 添加标题
        JLabel titleLabel = new JLabel("Data Entry");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 30, 700, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundPanel.add(titleLabel);

        // 创建主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBounds(100, 100, 600, 400);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridwidth = 1;

        // 创建两个主要功能按钮
        JButton manualEntryButton = createFeatureButton(
                "Manual Data Entry",
                0, 0, gbc, mainPanel);

        JButton csvImportButton = createFeatureButton(
                "Import CSV File",
                0, 1, gbc, mainPanel);

        // 返回按钮
        JButton backButton = createFeatureButton(
                "Back to Main Menu",
                0, 2, gbc, mainPanel);

        // 添加按钮事件监听器
        manualEntryButton.addActionListener(e -> {
            openManualEntryDialog();
        });

        csvImportButton.addActionListener(e -> {
            openFileChooser();
        });

        backButton.addActionListener(e -> {
            frame.dispose(); // 关闭当前窗口
        });

        backgroundPanel.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void openManualEntryDialog() {
        JDialog dialog = new JDialog(frame, "Manual Entry", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 添加输入字段
        JTextField amountField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);
        JComboBox<String> categoryBox = new JComboBox<>(new String[] {
                "Food", "Transportation", "Entertainment", "Shopping", "Bills", "Others"
        });

        // 使用GridBagLayout添加组件
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

        // 添加保存按钮
        JButton saveButton = new JButton("Save");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        dialog.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            // TODO: 实现保存逻辑
            JOptionPane.showMessageDialog(dialog, "Transaction saved!");
            dialog.dispose();
        });

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".csv") || f.isDirectory();
            }

            public String getDescription() {
                return "CSV Files (*.csv)";
            }
        });

        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            // TODO: 实现CSV文件导入逻辑
            JOptionPane.showMessageDialog(frame, "Selected file: " + selectedFile.getName());
        }
    }

    private JButton createFeatureButton(String title, int x, int y, GridBagConstraints gbc, JPanel panel) {
        JButton button = new JButton(title);
        button.setPreferredSize(new Dimension(500, 70));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBackground(new Color(51, 122, 183));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(40, 96, 144));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(51, 122, 183));
            }
        });

        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(button, gbc);
        return button;
    }
}