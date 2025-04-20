package views;

import components.RoundedButton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * SettingsView 类继承自 BaseView，用于创建设置视图界面。
 * 该视图包含顶部导航栏和多个内容面板，用户可以通过导航栏切换不同的设置页面。
 */
public class SettingsView extends BaseView {
    /**
     * 卡片布局管理器，用于切换内容面板。
     */
    private CardLayout cardLayout;
    /**
     * 包含多个内容面板的容器。
     */
    private JPanel contentPanel;
    /**
     * 顶部导航栏的按钮数组。
     */
    private RoundedButton[] tabButtons;
    /**
     * 主题主颜色。
     */
    private final Color primaryColor = new Color(0, 122, 255);
    /**
     * 主题次要颜色。
     */
    private final Color secondaryColor = new Color(245, 247, 250);
    /**
     * 文本颜色。
     */
    private final Color textColor = new Color(80, 80, 80);
    /**
     * 边框颜色。
     */
    private final Color borderColor = new Color(225, 225, 225);
    /**
     * 导航按钮和表单标签使用的字体。
     */
    private Font tabFont;

    /**
     * 新增字体初始化方法，尝试使用 "Segoe UI" 字体，若失败则使用备用字体。
     * 
     * @return 初始化后的字体对象。
     */
    private Font createBaseFont() {
        try {
            return new Font("Segoe UI", Font.PLAIN, 14);
        } catch (Exception e) {
            return new Font(Font.SANS_SERIF, Font.PLAIN, 14); // 备用字体
        }
    }

    // public SettingsView() {
    //     // 在构造函数中初始化 tabFont
    //     tabFont = createBaseFont();
    //     initUI();
    // }

    /**
     * 获取视图的名称，用于在界面切换时标识该视图。
     * 
     * @return 视图名称 "Settings"。
     */
    @Override
    public String getViewName() {
        return "Settings";
    }

    /**
     * 重写 initUI 方法，初始化设置视图的用户界面。
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
     * 创建顶部导航系统，包含多个导航按钮。
     * 
     * @return 包含导航按钮的面板。
     */
    // 顶部导航系统 =============================================================
    private JPanel createTopNavigation() {
        // 创建导航面板，使用网格布局
        JPanel navPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        navPanel.setPreferredSize(new Dimension(0, 48));
        navPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        navPanel.setBackground(secondaryColor);

        // 定义导航按钮的文本
        String[] tabs = {"Profile", "Preferences", "Security"};
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
     * 创建导航按钮，设置按钮的字体、背景、边框等属性，并添加鼠标和点击事件监听器。
     * 
     * @param text 按钮显示的文本。
     * @return 配置好的导航按钮。
     */
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

        // 添加鼠标事件监听器
        button.addMouseListener(new MouseAdapter() {
            /**
             * 鼠标进入按钮区域时，若按钮未被选中，更改按钮背景和边框颜色。
             * 
             * @param e 鼠标事件对象。
             */
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

            /**
             * 鼠标离开按钮区域时，若按钮未被选中，恢复按钮背景和边框颜色。
             * 
             * @param e 鼠标事件对象。
             */
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

        // 添加点击事件监听器
        button.addActionListener(e -> {
            cardLayout.show(contentPanel, text.toLowerCase());
            activateTab(button);
        });
        return button;
    }

    /**
     * 包装导航按钮，使其适应父容器的大小。
     * 
     * @param btn 要包装的导航按钮。
     * @return 包装后的面板。
     */
    private JPanel wrapNavButton(RoundedButton btn) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(btn);

        // 添加组件大小变化监听器
        wrapper.addComponentListener(new ComponentAdapter() {
            /**
             * 当包装面板大小变化时，设置按钮的首选大小与面板相同。
             * 
             * @param e 组件事件对象。
             */
            @Override
            public void componentResized(ComponentEvent e) {
                btn.setPreferredSize(new Dimension(wrapper.getWidth(), wrapper.getHeight()));
            }
        });
        return wrapper;
    }

    /**
     * 激活指定的导航按钮，更改按钮的背景、前景和边框颜色。
     * 
     * @param selected 要激活的按钮。
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

    /**
     * 初始化内容面板，将不同的设置页面添加到内容面板中。
     */
    // 内容面板系统 =============================================================
    private void initContentPanels() {
        contentPanel.add(buildProfilePanel(), "profile");
        contentPanel.add(buildPreferencesPanel(), "preferences");
        contentPanel.add(buildSecurityPanel(), "security");
    }

    /**
     * 构建个人资料设置面板，包含头像区域、表单区域和保存按钮。
     * 
     * @return 个人资料设置面板。
     */
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

    /**
     * 构建偏好设置面板，包含表单区域和保存按钮。
     * 
     * @return 偏好设置面板。
     */
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

    /**
     * 构建安全设置面板，包含安全项目区域。
     * 
     * @return 安全设置面板。
     */
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

    /**
     * 通用内容面板类，用于创建设置页面的基础面板和操作按钮。
     */
    // 通用组件模板 =============================================================
    private class ContentPanel extends JPanel {
        /**
         * 构造方法，初始化内容面板的布局、边框和背景。
         */
        public ContentPanel() {
            setLayout(new BorderLayout(25, 25));
            setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
            setBackground(Color.WHITE);
        }

        /**
         * 创建操作按钮，设置按钮的字体、背景、边框等属性，并添加鼠标事件监听器。
         * 
         * @param text 按钮显示的文本。
         * @return 包装好的操作按钮组件。
         */
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

            // 添加鼠标事件监听器
            btn.addMouseListener(new MouseAdapter() {
                /**
                 * 鼠标进入按钮区域时，加深按钮背景颜色。
                 * 
                 * @param e 鼠标事件对象。
                 */
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(primaryColor.darker());
                }

                /**
                 * 鼠标离开按钮区域时，恢复按钮背景颜色。
                 * 
                 * @param e 鼠标事件对象。
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
     * 创建头像区域，绘制圆形头像并添加边框。
     * 
     * @return 包含头像的面板。
     */
    private JPanel createAvatarSection() {
        return new JPanel() {
            {
                setPreferredSize(new Dimension(180, 180));
                setOpaque(false);
            }

            /**
             * 重写绘制组件方法，绘制圆形头像和边框。
             * 
             * @param g 图形上下文对象。
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

    /**
     * 创建表单区域，根据传入的字段和下拉框索引创建标签、输入框和下拉框。
     * 
     * @param fields 表单字段数组，每个元素包含标签和初始值。
     * @param dropdownIndices 下拉框所在的索引数组。
     * @return 包装在滚动面板中的表单区域。
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
     * 创建安全项目区域，包含安全项目的标签、输入框和修改按钮。
     * 
     * @param items 安全项目数组，每个元素包含标签和描述。
     * @return 包装在滚动面板中的安全项目区域。
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
     * 工具方法，将组件包装在滚动面板中，并设置滚动面板的边框和背景。
     * 
     * @param view 要包装的组件。
     * @return 包装好的滚动面板。
     */
    // 工具方法 ================================================================
    private JScrollPane wrapScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }

    /**
     * 创建表单字段标签，设置标签的字体和颜色。
     * 
     * @param text 标签显示的文本。
     * @return 配置好的标签组件。
     */
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        Font labelFont = tabFont != null ?
                tabFont.deriveFont(Font.BOLD, 14f) :
                new Font(Font.SANS_SERIF, Font.BOLD, 14);
        label.setFont(labelFont);
        label.setForeground(textColor);
        return label;
    }

    /**
     * 创建输入框，设置输入框的字体、边框和颜色。
     * 
     * @param value 输入框的初始值。
     * @return 配置好的输入框组件。
     */
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

    /**
     * 创建下拉框，设置下拉框的字体、背景、边框和颜色。
     * 
     * @param options 下拉框的选项数组。
     * @return 配置好的下拉框组件。
     */
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

    /**
     * 工具方法，检查数组中是否包含指定的索引。
     * 
     * @param indices 索引数组。
     * @param target 要检查的目标索引。
     * @return 如果包含返回 true，否则返回 false。
     */
    private boolean containsIndex(int[] indices, int target) {
        for (int i : indices) {
            if (i == target) return true;
        }
        return false;
    }
}