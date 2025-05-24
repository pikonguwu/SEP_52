package views;

import components.RoundedButton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import views.PrivacySettingsPanel;
import java.awt.geom.RoundRectangle2D;

/**
 * SettingsView extends BaseView to create the settings interface.
 * This view features a top navigation bar and multiple content panels,
 * allowing users to switch between different settings pages.
 */
public class SettingsView extends BaseView {
    /**
     * CardLayout manager used to switch between content panels.
     */
    private CardLayout cardLayout;
    /**
     * Container holding the different content panels.
     */
    private JPanel contentPanel;
    /**
     * Array of navigation buttons in the top bar.
     */
    private RoundedButton[] tabButtons;
    /**
     * Primary theme color.
     */
    private final Color primaryColor = new Color(0, 122, 255);
    /**
     * Secondary theme color.
     */
    private final Color secondaryColor = new Color(245, 247, 250);
    /**
     * Text color.
     */
    private final Color textColor = new Color(80, 80, 80);
    /**
     * Border color.
     */
    private final Color borderColor = new Color(225, 225, 225);
    /**
     * Font used for tab buttons and form labels.
     */
    private Font tabFont;

    /**
     * Initializes the base font, attempting to use "Segoe UI" and falling back
     * to a default sans-serif font if unavailable.
     *
     * @return the initialized Font object
     */
    private Font createBaseFont() {
        try {
            return new Font("Segoe UI", Font.PLAIN, 14);
        } catch (Exception e) {
            return new Font(Font.SANS_SERIF, Font.PLAIN, 14); // 备用字体
        }
    }

    // public SettingsView() {
    // // 在构造函数中初始化 tabFont
    // tabFont = createBaseFont();
    // initUI();
    // }

    /**
     * Returns the name of this view used for identification when switching views.
     *
     * @return the view name "Settings"
     */
    @Override
    public String getViewName() {
        return "Settings";
    }

    /**
     * Overrides initUI to initialize the user interface for the settings view.
     */
    @Override
    protected void initUI() {
        // 初始化字体
        tabFont = createBaseFont();

        // 初始化卡片布局和内容面板
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // 设置当前视图的布局和背景
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setOpaque(true);

        // 添加顶部导航栏
        add(createTopNavigation(), BorderLayout.NORTH);
        // 初始化内容面板
        initContentPanels();
        // 添加内容面板到中心位置
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Creates the top navigation bar containing multiple navigation buttons.
     *
     * @return a JPanel holding the navigation buttons
     */
    // 顶部导航系统 =============================================================
    private JPanel createTopNavigation() {
        // 创建导航面板，使用网格布局
        JPanel navPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        navPanel.setPreferredSize(new Dimension(0, 48));
        navPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        navPanel.setBackground(secondaryColor);

        // 定义导航按钮的文本
        String[] tabs = { "Profile", "Preferences", "Security" };
        tabButtons = new RoundedButton[tabs.length];

        // 创建并添加导航按钮
        for (int i = 0; i < tabs.length; i++) {
            RoundedButton btn = createNavButton(tabs[i]);
            navPanel.add(wrapNavButton(btn));
            tabButtons[i] = btn;
        }

        // 激活第一个导航按钮
        activateTab(tabButtons[0]);
        return navPanel;
    }

    /**
     * Creates a navigation button with specified text, sets its font, background,
     * border styling, and attaches mouse and action listeners.
     *
     * @param text the text to display on the button
     * @return the configured RoundedButton instance
     */
    private RoundedButton createNavButton(String text) {
        RoundedButton button = new RoundedButton(text);

        // 添加字体空指针保护
        Font buttonFont = tabFont != null ? tabFont.deriveFont(Font.BOLD, 14f)
                : new Font(Font.SANS_SERIF, Font.BOLD, 14);

        button.setFont(buttonFont);
        button.setBackground(Color.WHITE);
        button.setForeground(textColor);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // 添加鼠标事件监听器
        button.addMouseListener(new MouseAdapter() {
            /**
             * When the mouse enters the button area, if the button is not selected, change its background and border colors.
             *
             * @param e the mouse event object
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!button.isSelected()) {
                    button.setBackground(secondaryColor);
                    button.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
                            BorderFactory.createEmptyBorder(8, 20, 8, 20)));
                }
            }

            /**
             * When the mouse exits the button area, if the button is not selected, restore its background and border colors.
             *
             * @param e the mouse event object
             */
            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.isSelected()) {
                    button.setBackground(Color.WHITE);
                    button.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE),
                            BorderFactory.createEmptyBorder(8, 20, 8, 20)));
                }
            }
        });

        // 添加点击事件监听器
        button.addActionListener(e -> {
            cardLayout.show(contentPanel, text.toLowerCase());
            activateTab(button);
        });
        return button;
    }

    /**
     * Wraps a navigation button in a panel that adjusts the button's size
     * to match the parent container.
     *
     * @param btn the navigation button to wrap
     * @return a JPanel containing the wrapped button
     */
    private JPanel wrapNavButton(RoundedButton btn) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(btn);

        // 添加组件大小变化监听器
        wrapper.addComponentListener(new ComponentAdapter() {
            /**
             * When the wrapper panel is resized, set the button's preferred size to match the panel.
             *
             * @param e the component event object
             */
            @Override
            public void componentResized(ComponentEvent e) {
                btn.setPreferredSize(new Dimension(wrapper.getWidth(), wrapper.getHeight()));
            }
        });
        return wrapper;
    }

    /**
     * Activates the specified tab button by updating its background,
     * foreground, and border colors to indicate selection.
     *
     * @param selected the button to activate
     */
    private void activateTab(RoundedButton selected) {
        for (RoundedButton btn : tabButtons) {
            boolean active = btn == selected;
            btn.setSelected(active);
            if (active) {
                btn.setBackground(primaryColor);
                btn.setForeground(Color.WHITE);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
                        BorderFactory.createEmptyBorder(8, 20, 8, 20)));
            } else {
                btn.setBackground(Color.WHITE);
                btn.setForeground(textColor);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE),
                        BorderFactory.createEmptyBorder(8, 20, 8, 20)));
            }
            btn.repaint();
        }
    }

    /**
     * Initializes the content panels by adding different settings pages
     * to the contentPanel using the card layout.
     */
    // 内容面板系统 =============================================================
    private void initContentPanels() {
        contentPanel.add(buildProfilePanel(), "profile");
        contentPanel.add(buildPreferencesPanel(), "preferences");
        contentPanel.add(buildSecurityPanel(), "security");
    }

    /**
     * Builds the "Profile" settings panel, including an avatar section,
     * a form section, and a save button.
     *
     * @return the JPanel for profile settings
     */
    private JPanel buildProfilePanel() {
        ContentPanel panel = new ContentPanel();
        panel.add(createAvatarSection(), BorderLayout.WEST);
        panel.add(createFormSection(
                new String[][] {
                        { "Full Name", "Charlene Reed" },
                        { "Username", "charlene_reed" },
                        { "Email", "charlene@example.com" },
                        { "Birth Date", "1990-01-25" },
                        { "Location", "San Jose, CA" },
                        { "Postal Code", "95128" }
                }), BorderLayout.CENTER);
        panel.add(panel.createActionButton("Save Profile"), BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Builds the "Preferences" settings panel, including form fields
     * and a save button.
     *
     * @return the JPanel for preference settings
     */
    private JPanel buildPreferencesPanel() {
        ContentPanel panel = new ContentPanel();
        panel.add(createFormSection(
                new String[][] {
                        { "Currency", "USD" },
                        { "Timezone", "GMT-08:00" },
                        { "Notifications", "Enabled" },
                        { "Budget Limit", "$1,500" }
                },
                new int[] { 0, 1 }), BorderLayout.CENTER);
        panel.add(panel.createActionButton("Save Settings"), BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Builds the "Security" settings panel, including security items.
     *
     * @return the JPanel for security settings
     */
    private JPanel buildSecurityPanel() {
        ContentPanel panel = new ContentPanel();
        panel.add(createSecurityItems(
                new String[][] {
                        { "Password", "Updated 3 days ago" },
                        { "2FA", "Not enabled" },
                        { "Security Questions", "3 configured" }
                }), BorderLayout.CENTER);
        return panel;
    }

    /**
     * A generic content panel used as the base for each settings page,
     * providing layout and a method for creating action buttons.
     */
    // 通用组件模板 =============================================================
    private class ContentPanel extends JPanel {
        /**
         * Constructor that initializes the layout, border, and background of the content panel.
         */
        public ContentPanel() {
            setLayout(new BorderLayout(25, 25));
            setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
            setBackground(Color.WHITE);
        }

        /**
         * Creates an action button with specified text, sets its font,
         * background, border styling, and attaches mouse event listeners.
         *
         * @param text the label text for the button
         * @return a JComponent wrapping the action button
         */
        public JComponent createActionButton(String text) {
            RoundedButton btn = new RoundedButton(text);

            // 按钮字体保护
            Font buttonFont = tabFont != null ? tabFont.deriveFont(Font.BOLD, 14f)
                    : new Font(Font.SANS_SERIF, Font.BOLD, 14);

            btn.setFont(buttonFont);
            btn.setBackground(primaryColor);
            btn.setForeground(Color.WHITE);
            btn.setPreferredSize(new Dimension(140, 40));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // 添加鼠标事件监听器
            btn.addMouseListener(new MouseAdapter() {
                /**
                 * When the mouse enters the button area, darken the button background color.
                 *
                 * @param e the mouse event object
                 */
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(primaryColor.darker());
                }

                /**
                 * When the mouse exits the button area, restore the button background color.
                 *
                 * @param e the mouse event object
                 */
                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(primaryColor);
                }
            });

            // 包装按钮
            JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 15));
            wrapper.setOpaque(false);
            wrapper.add(btn);
            return wrapper;
        }
    }

    /**
     * Overrides paintComponent to draw a circular avatar image with a border.
     *
     * @param g the Graphics context
     */
    private JPanel createAvatarSection() {
        return new JPanel() {
            {
                setPreferredSize(new Dimension(180, 180));
                setOpaque(false);
            }

            /**
             * Overrides the component's paint method to draw a circular avatar image and its border.
             *
             * @param g the graphics context used for drawing
             */
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int diameter = Math.min(getWidth(), getHeight());
                RoundRectangle2D clip = new RoundRectangle2D.Float(
                        (getWidth() - diameter) / 2f,
                        (getHeight() - diameter) / 2f,
                        diameter, diameter, diameter, diameter);

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

    /**
     * Creates a form section with labels, text fields, and dropdowns
     * based on provided field definitions and dropdown indices.
     *
     * @param fields          a 2D array of label/value pairs
     * @param dropdownIndices indices specifying which fields should be dropdowns
     * @return a JScrollPane containing the assembled form panel
     */
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

    /**
     * Creates the security items section with labels, input fields,
     * and modify buttons for each security setting.
     *
     * @param items an array where each entry contains label and description
     * @return a JScrollPane containing the security items panel
     */
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

    /**
     * Wraps a component in a JScrollPane and applies border and background settings.
     *
     * @param view the Component to wrap
     * @return the configured JScrollPane
     */
    // 工具方法 ================================================================
    private JScrollPane wrapScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }

    /**
     * Creates a JLabel for form fields with specified text, font, and color.
     *
     * @param text the text to display in the label
     * @return the configured JLabel
     */
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        Font labelFont = tabFont != null ? tabFont.deriveFont(Font.BOLD, 14f)
                : new Font(Font.SANS_SERIF, Font.BOLD, 14);
        label.setFont(labelFont);
        label.setForeground(textColor);
        return label;
    }

    /**
     * Creates a JTextField with specified initial value, font, border, and text color.
     *
     * @param value the initial text for the field
     * @return the configured JTextField
     */
    private JTextField createInputField(String value) {
        JTextField field = new JTextField(value);
        Font fieldFont = tabFont != null ? tabFont : new Font(Font.SANS_SERIF, Font.PLAIN, 14);
        field.setFont(fieldFont);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        field.setForeground(textColor);
        return field;
    }

    /**
     * Creates a JComboBox with specified options, font, border, and color settings.
     *
     * @param options array of dropdown options
     * @return the configured JComboBox
     */
    private JComboBox<String> createDropdown(String[] options) {
        JComboBox<String> combo = new JComboBox<>(options);
        Font comboFont = tabFont != null ? tabFont : new Font(Font.SANS_SERIF, Font.PLAIN, 14);
        combo.setFont(comboFont);
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        combo.setForeground(textColor);
        return combo;
    }

    /**
     * Utility method to check if an array of indices contains a target index.
     *
     * @param indices array of indices to search
     * @param target  the index to check for
     * @return true if target is found in indices, false otherwise
     */
    private boolean containsIndex(int[] indices, int target) {
        for (int i : indices) {
            if (i == target)
                return true;
        }
        return false;
    }
}