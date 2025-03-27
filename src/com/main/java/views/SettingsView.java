// // src/views/SettingsView.java
// package views;

// import components.RoundedButton;
// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.*;

// public class SettingsView extends BaseView {
//     private CardLayout cardLayout;
//     private JPanel contentPanel;

//     @Override
//     public String getViewName() {
//         return "Settings";
//     }

//     @Override
//     protected void initUI() {
//         // 确保先初始化成员变量
//         cardLayout = new CardLayout();
//         contentPanel = new JPanel(cardLayout);
        
//         setLayout(new BorderLayout());
//         setBackground(Color.WHITE);
        
//         // 左侧导航
//         JPanel navPanel = new JPanel(new GridLayout(5, 1, 0, 15));
//         navPanel.setPreferredSize(new Dimension(220, 400));
//         navPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
//         navPanel.setBackground(new Color(245, 247, 250));

//         String[] menuItems = {"Edit Profile", "Preferences", "Security"};
//         for (String item : menuItems) {
//             RoundedButton btn = new RoundedButton(item);
//             btn.setHorizontalAlignment(SwingConstants.LEFT);
//             btn.setBackground(Color.WHITE);
//             btn.setFont(new Font("Arial", Font.BOLD, 14));
//             btn.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
//             btn.addActionListener(e -> cardLayout.show(contentPanel, item.toLowerCase().replace(" ", "")));
            
//             navPanel.add(btn);
//         }

//         // 内容面板初始化
//         contentPanel.add(createEditProfilePanel(), "editprofile");
//         contentPanel.add(createPreferencesPanel(), "preferences");
//         contentPanel.add(createSecurityPanel(), "security");

//         add(navPanel, BorderLayout.WEST);
//         add(contentPanel, BorderLayout.CENTER);
//     }

//     // 左侧导航 =================================================================
//     private JPanel createNavigationPanel() {
//         JPanel navPanel = new JPanel(new GridLayout(5, 1, 0, 15));
//         navPanel.setPreferredSize(new Dimension(220, 400));
//         navPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
//         navPanel.setBackground(new Color(245, 247, 250));

//         String[] menuItems = {"Edit Profile", "Preferences", "Security"};
//         for (String item : menuItems) {
//             RoundedButton btn = new RoundedButton(item);
//             btn.setHorizontalAlignment(SwingConstants.LEFT);
//             btn.setBackground(Color.WHITE);
//             btn.setFont(new Font("Arial", Font.BOLD, 14));
//             btn.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
//             btn.addActionListener(e -> cardLayout.show(contentPanel, item.toLowerCase().replace(" ", "")));
            
//             navPanel.add(btn);
//         }

//         return navPanel;
//     }

//     // Preferences 面板 ========================================================
//     private JPanel createPreferencesPanel() {
//         JPanel panel = new JPanel(new BorderLayout(20, 20));
//         panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
//         panel.setBackground(Color.WHITE);

//         // 表单区域
//         JPanel formPanel = new JPanel(new GridLayout(4, 2, 25, 25));
        
//         // 货币选择
//         formPanel.add(createFormLabel("Currency"));
//         JComboBox<String> currencyCombo = createStyledCombo(new String[]{"USD", "EUR", "GBP", "CNY"});
//         formPanel.add(currencyCombo);

//         // 时区选择
//         formPanel.add(createFormLabel("Time Zone"));
//         JComboBox<String> timezoneCombo = createStyledCombo(new String[]{
//             "(GMT-12:00) International Date Line West",
//             "(GMT-08:00) Pacific Time",
//             "(GMT+00:00) London"
//         });
//         formPanel.add(timezoneCombo);

//         // 通知设置
//         formPanel.add(createFormLabel("Notification"));
//         JCheckBox reminderSwitch = new JCheckBox("Smart Budget Reminder");
//         reminderSwitch.setFont(new Font("Arial", Font.PLAIN, 14));
//         formPanel.add(reminderSwitch);

//         // 预算设置
//         formPanel.add(createFormLabel("Set Budget"));
//         JTextField budgetField = createStyledField("$1,000");
//         formPanel.add(budgetField);

//         // 保存按钮
//         RoundedButton saveBtn = createSaveButton();
//         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//         buttonPanel.setBackground(Color.WHITE);
//         buttonPanel.add(saveBtn);

//         panel.add(formPanel, BorderLayout.CENTER);
//         panel.add(buttonPanel, BorderLayout.SOUTH);

//         return panel;
//     }

//     // Edit Profile 面板 ========================================================
//     private JPanel createEditProfilePanel() {
//         JPanel panel = new JPanel(new BorderLayout(20, 20));
//         panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
//         panel.setBackground(Color.WHITE);

//         // 表单字段
//         String[] labels = {"Your Name", "User Name", "Email", "Date of Birth", 
//                          "Present Address", "Permanent Address", "City", "Postal Code", "Country"};
//         String[] values = {"Charlene Reed", "Chariene Reed", "charienereed@gmail.com",
//                          "25 January 1990", "San Jose, California, USA", 
//                          "San Jose, California, USA", "San Jose", "45962", "USA"};

//         JPanel formPanel = new JPanel(new GridLayout(9, 2, 15, 25));
//         for (int i = 0; i < labels.length; i++) {
//             formPanel.add(createFormLabel(labels[i]));
//             formPanel.add(createStyledField(values[i]));
//         }

//         // 保存按钮
//         RoundedButton saveBtn = createSaveButton();
//         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//         buttonPanel.setBackground(Color.WHITE);
//         buttonPanel.add(saveBtn);

//         panel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
//         panel.add(buttonPanel, BorderLayout.SOUTH);

//         return panel;
//     }

//     // Security 面板 ============================================================
//     private JPanel createSecurityPanel() {
//         JPanel panel = new JPanel(new BorderLayout(20, 20));
//         panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
//         panel.setBackground(Color.WHITE);

//         // 安全设置内容
//         JPanel securityContent = new JPanel(new GridLayout(3, 1, 0, 30));
//         securityContent.add(createSecurityItem("Password", "**********"));
//         securityContent.add(createSecurityItem("2FA Authentication", "Not enabled"));
//         securityContent.add(createSecurityItem("Security Questions", "3 questions set"));

//         panel.add(securityContent, BorderLayout.CENTER);
//         return panel;
//     }

//     // 通用组件 =================================================================
//     private JLabel createFormLabel(String text) {
//         JLabel label = new JLabel(text);
//         label.setFont(new Font("Arial", Font.BOLD, 14));
//         label.setForeground(new Color(80, 80, 80));
//         return label;
//     }

//     private JTextField createStyledField(String value) {
//         JTextField field = new JTextField(value);
//         field.setFont(new Font("Arial", Font.PLAIN, 14));
//         field.setBorder(BorderFactory.createCompoundBorder(
//             BorderFactory.createLineBorder(new Color(220, 220, 220)),
//             BorderFactory.createEmptyBorder(12, 15, 12, 15)
//         ));
//         return field;
//     }

//     private JComboBox<String> createStyledCombo(String[] items) {
//         JComboBox<String> combo = new JComboBox<>(items);
//         combo.setFont(new Font("Arial", Font.PLAIN, 14));
//         combo.setBackground(Color.WHITE);
//         combo.setBorder(BorderFactory.createCompoundBorder(
//             BorderFactory.createLineBorder(new Color(220, 220, 220)),
//             BorderFactory.createEmptyBorder(8, 12, 8, 12)
//         ));
//         return combo;
//     }

//     private RoundedButton createSaveButton() {
//         RoundedButton btn = new RoundedButton("Save");
//         btn.setBackground(new Color(0, 122, 255));
//         btn.setForeground(Color.WHITE);
//         btn.setFont(new Font("Arial", Font.BOLD, 14));
//         btn.setPreferredSize(new Dimension(120, 45));
//         return btn;
//     }

//     private JPanel createSecurityItem(String title, String value) {
//         JPanel itemPanel = new JPanel(new BorderLayout());
        
//         JLabel titleLabel = new JLabel(title);
//         titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
//         JLabel valueLabel = new JLabel(value);
//         valueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
//         valueLabel.setForeground(new Color(150, 150, 150));
        
//         RoundedButton actionBtn = new RoundedButton("Change");
//         actionBtn.setBackground(new Color(230, 230, 230));
//         actionBtn.setForeground(Color.BLACK);
//         actionBtn.setFont(new Font("Arial", Font.BOLD, 12));
//         actionBtn.setPreferredSize(new Dimension(100, 35));
        
//         itemPanel.add(titleLabel, BorderLayout.NORTH);
//         itemPanel.add(valueLabel, BorderLayout.CENTER);
//         itemPanel.add(actionBtn, BorderLayout.EAST);
        
//         return itemPanel;
//     }
// }

package com.main.java.views;

import com.main.java.components.RoundedButton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class SettingsView extends BaseView {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private RoundedButton[] tabButtons;
    // 主题颜色
    private final Color primaryColor = new Color(0, 122, 255);
    private final Color secondaryColor = new Color(245, 247, 250);
    private final Color textColor = new Color(80, 80, 80);
    private final Color borderColor = new Color(225, 225, 225);
    private Font tabFont;

    // 新增字体初始化方法
    private Font createBaseFont() {
        try {
            return new Font("Segoe UI", Font.PLAIN, 14);
        } catch (Exception e) {
            return new Font(Font.SANS_SERIF, Font.PLAIN, 14); // 备用字体
        }
    }

    public SettingsView() {
        // 在构造函数中初始化 tabFont
        tabFont = createBaseFont();
        initUI();
    }

    @Override
    public String getViewName() {
        return "Settings";
    }

    @Override
    protected void initUI() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setOpaque(true);

        add(createTopNavigation(), BorderLayout.NORTH);
        initContentPanels();
        add(contentPanel, BorderLayout.CENTER);
    }

    // 顶部导航系统 =============================================================
    private JPanel createTopNavigation() {
        JPanel navPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        navPanel.setPreferredSize(new Dimension(0, 48));
        navPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        navPanel.setBackground(secondaryColor);

        String[] tabs = {"Profile", "Preferences", "Security"};
        tabButtons = new RoundedButton[tabs.length];

        for (int i = 0; i < tabs.length; i++) {
            RoundedButton btn = createNavButton(tabs[i]);
            navPanel.add(wrapNavButton(btn));
            tabButtons[i] = btn;
        }

        activateTab(tabButtons[0]);
        return navPanel;
    }

    private RoundedButton createNavButton(String text) {
        RoundedButton button = new RoundedButton(text);

        // 添加字体空指针保护
        Font buttonFont = tabFont != null ?
                tabFont.deriveFont(Font.BOLD, 14f) :
                new Font(Font.SANS_SERIF, Font.BOLD, 14);

        button.setFont(buttonFont);
        button.setBackground(Color.WHITE);
        button.setForeground(textColor);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!button.isSelected()) {
                    button.setBackground(secondaryColor);
                    button.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
                            BorderFactory.createEmptyBorder(8, 20, 8, 20)
                    ));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.isSelected()) {
                    button.setBackground(Color.WHITE);
                    button.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE),
                            BorderFactory.createEmptyBorder(8, 20, 8, 20)
                    ));
                }
            }
        });

        button.addActionListener(e -> {
            cardLayout.show(contentPanel, text.toLowerCase());
            activateTab(button);
        });
        return button;
    }

    private JPanel wrapNavButton(RoundedButton btn) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(btn);

        wrapper.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                btn.setPreferredSize(new Dimension(wrapper.getWidth(), wrapper.getHeight()));
            }
        });
        return wrapper;
    }

    private void activateTab(RoundedButton selected) {
        for (RoundedButton btn : tabButtons) {
            boolean active = btn == selected;
            btn.setSelected(active);
            if (active) {
                btn.setBackground(primaryColor);
                btn.setForeground(Color.WHITE);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
                        BorderFactory.createEmptyBorder(8, 20, 8, 20)
                ));
            } else {
                btn.setBackground(Color.WHITE);
                btn.setForeground(textColor);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE),
                        BorderFactory.createEmptyBorder(8, 20, 8, 20)
                ));
            }
            btn.repaint();
        }
    }

    // 内容面板系统 =============================================================
    private void initContentPanels() {
        contentPanel.add(buildProfilePanel(), "profile");
        contentPanel.add(buildPreferencesPanel(), "preferences");
        contentPanel.add(buildSecurityPanel(), "security");
    }

    private JPanel buildProfilePanel() {
        ContentPanel panel = new ContentPanel();
        panel.add(createAvatarSection(), BorderLayout.WEST);
        panel.add(createFormSection(
                new String[][]{
                        {"Full Name", "Charlene Reed"},
                        {"Username", "charlene_reed"},
                        {"Email", "charlene@example.com"},
                        {"Birth Date", "1990-01-25"},
                        {"Location", "San Jose, CA"},
                        {"Postal Code", "95128"}
                }
        ), BorderLayout.CENTER);
        panel.add(panel.createActionButton("Save Profile"), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildPreferencesPanel() {
        ContentPanel panel = new ContentPanel();
        panel.add(createFormSection(
                new String[][]{
                        {"Currency", "USD"},
                        {"Timezone", "GMT-08:00"},
                        {"Notifications", "Enabled"},
                        {"Budget Limit", "$1,500"}
                },
                new int[]{0, 1}
        ), BorderLayout.CENTER);
        panel.add(panel.createActionButton("Save Settings"), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildSecurityPanel() {
        ContentPanel panel = new ContentPanel();
        panel.add(createSecurityItems(
                new String[][]{
                        {"Password", "Updated 3 days ago"},
                        {"2FA", "Not enabled"},
                        {"Security Questions", "3 configured"}
                }
        ), BorderLayout.CENTER);
        return panel;
    }

    // 通用组件模板 =============================================================
    private class ContentPanel extends JPanel {
        public ContentPanel() {
            setLayout(new BorderLayout(25, 25));
            setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
            setBackground(Color.WHITE);
        }

        public JComponent createActionButton(String text) {
            RoundedButton btn = new RoundedButton(text);

            // 按钮字体保护
            Font buttonFont = tabFont != null ?
                    tabFont.deriveFont(Font.BOLD, 14f) :
                    new Font(Font.SANS_SERIF, Font.BOLD, 14);

            btn.setFont(buttonFont);
            btn.setBackground(primaryColor);
            btn.setForeground(Color.WHITE);
            btn.setPreferredSize(new Dimension(140, 40));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(primaryColor.darker());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(primaryColor);
                }
            });

            JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 15));
            wrapper.setOpaque(false);
            wrapper.add(btn);
            return wrapper;
        }
    }

    private JPanel createAvatarSection() {
        return new JPanel() {
            {
                setPreferredSize(new Dimension(180, 180));
                setOpaque(false);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int diameter = Math.min(getWidth(), getHeight());
                RoundRectangle2D clip = new RoundRectangle2D.Float(
                        (getWidth() - diameter) / 2f,
                        (getHeight() - diameter) / 2f,
                        diameter, diameter, diameter, diameter
                );

                g2.setClip(clip);
                g2.drawImage(new ImageIcon("avatar.jpg").getImage(),
                        (getWidth() - diameter) / 2, (getHeight() - diameter) / 2,
                        diameter, diameter, this);

                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(2));
                g2.draw(clip);
                g2.dispose();
            }
        };
    }

    private JComponent createFormSection(String[][] fields, int... dropdownIndices) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 15, 15));
        panel.setOpaque(false);

        for (int i = 0; i < fields.length; i++) {
            panel.add(createFieldLabel(fields[i][0]));
            if (containsIndex(dropdownIndices, i)) {
                panel.add(createDropdown(fields[i][1].split(" ")));
            } else {
                panel.add(createInputField(fields[i][1]));
            }
        }
        return wrapScrollPane(panel);
    }

    private JComponent createSecurityItems(String[][] items) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 15));
        panel.setOpaque(false);

        for (String[] item : items) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setOpaque(false);
            row.add(createFieldLabel(item[0]), BorderLayout.WEST);
            row.add(createInputField(item[1]), BorderLayout.CENTER);

            ContentPanel parentPanel = (ContentPanel) SwingUtilities.getAncestorOfClass(ContentPanel.class, panel);
            if (parentPanel != null) {
                row.add(parentPanel.createActionButton("Modify"), BorderLayout.EAST);
            }
            panel.add(row);
        }
        return wrapScrollPane(panel);
    }

    // 工具方法 ================================================================
    private JScrollPane wrapScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        Font labelFont = tabFont != null ?
                tabFont.deriveFont(Font.BOLD, 14f) :
                new Font(Font.SANS_SERIF, Font.BOLD, 14);
        label.setFont(labelFont);
        label.setForeground(textColor);
        return label;
    }

    private JTextField createInputField(String value) {
        JTextField field = new JTextField(value);
        Font fieldFont = tabFont != null ? tabFont : new Font(Font.SANS_SERIF, Font.PLAIN, 14);
        field.setFont(fieldFont);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setForeground(textColor);
        return field;
    }

    private JComboBox<String> createDropdown(String[] options) {
        JComboBox<String> combo = new JComboBox<>(options);
        Font comboFont = tabFont != null ? tabFont : new Font(Font.SANS_SERIF, Font.PLAIN, 14);
        combo.setFont(comboFont);
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        combo.setForeground(textColor);
        return combo;
    }

    private boolean containsIndex(int[] indices, int target) {
        for (int i : indices) {
            if (i == target) return true;
        }
        return false;
    }
}