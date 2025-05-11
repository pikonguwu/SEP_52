// src/views/BucksBrainAIChatView.java
package views;

import constants.AppConstants;
import services.BaiduAIService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.plaf.basic.BasicScrollBarUI;


/**
 * BucksBrainAIChatView 是一个用于实现智能助手聊天界面的视图类。
 * 用户可以输入消息，系统会调用百度 AI 服务生成回复，并以消息气泡的形式展示在界面上。
 */
public class BucksBrainAIChatView extends BaseView {
    private JPanel chatPanel;  // 用于显示聊天内容的面板
    private JTextField inputField;  // 用户输入消息的文本框
    private JButton sendButton;  // 发送消息的按钮
    private BaiduAIService baiduAIService;  // 百度 AI 服务实例
    private JScrollPane scrollPane;  // 用于支持聊天内容滚动的滚动面板

    /**
     * 构造函数，初始化百度 AI 服务实例。
     */
    public BucksBrainAIChatView() {
        baiduAIService = new BaiduAIService();
    }

    /**
     * 获取当前视图的名称。
     *
     * @return 视图名称
     */
    @Override
    public String getViewName() {
        return "Bucks Brain";
    }

    /**
     * 初始化用户界面，包括聊天内容区域、输入面板和发送按钮。
     */
    @Override
    protected void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // 设置背景颜色或渐变背景
        setBackground(new Color(245, 245, 245));

        // 初始化聊天内容区域
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.WHITE);

        // 初始化滚动面板
        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI()); // 自定义滚动条样式

        // 初始化输入面板
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputField = new JTextField();
        inputField.setFont(AppConstants.BODY_FONT.deriveFont(14f));
        inputField.setToolTipText("请输入消息..."); // 增加占位符提示
        inputField.addActionListener(e -> sendButton.doClick()); // 支持按下 Enter 键发送消息

        sendButton = new JButton("发送");
        sendButton.setFont(AppConstants.BUTTON_FONT);
        sendButton.addActionListener(this::handleSendMessage);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // 将组件添加到主界面
        add(createHeader("Bucks Brain - 智能助手"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    }

    /**
     * 创建带样式的消息气泡。
     *
     * @param text   消息内容
     * @param isUser 是否为用户发送的消息
     * @return 包含消息气泡的面板
     */
    private JPanel createMessageBubble(String text, boolean isUser) {
        JPanel bubblePanel = new JPanel(new BorderLayout()) {
            @Override
            public Insets getInsets() {
                return new Insets(8, 8, 8, 8);
            }
        };
    
        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setContentType("text/html");
        textPane.setText("<html><body style='width: auto; padding: 5px;'>" + text + "</body></html>");
        textPane.setFont(AppConstants.BODY_FONT.deriveFont(14f));
        textPane.setBackground(isUser ? new Color(220, 245, 255) : new Color(240, 240, 240));
    
        // 添加圆角边框和阴影效果
        textPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isUser ? new Color(180, 220, 255) : new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        bubblePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1)
        ));
    
        // 添加时间戳
        JLabel timestampLabel = new JLabel(new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date()));
        timestampLabel.setFont(AppConstants.BODY_FONT.deriveFont(10f));
        timestampLabel.setForeground(Color.GRAY);
    
        // 设置对齐方向
        bubblePanel.add(textPane, BorderLayout.CENTER);
        bubblePanel.add(timestampLabel, BorderLayout.SOUTH);
        bubblePanel.setAlignmentX(isUser ? Component.LEFT_ALIGNMENT : Component.RIGHT_ALIGNMENT);
    
        return bubblePanel;
    }

    /**
     * 处理发送消息事件。
     *
     * @param e 事件对象
     */
    private void handleSendMessage(ActionEvent e) {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            // 输入验证
            if (isValidInput(message)) {
                addUserMessage(message);
                processAIMessage(message);
                inputField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "输入包含非法字符，请重新输入。");
            }
        }
    }

    /**
     * 将用户消息添加到聊天面板中。
     *
     * @param message 用户消息
     */
    private void addUserMessage(String message) {
        chatPanel.add(createMessageBubble(message, true));
        chatPanel.revalidate();
        scrollToBottom();
    }

    /**
     * 处理 AI 消息，调用百度 AI 服务生成回复并添加到聊天面板中。
     *
     * @param message 用户消息
     */
    private void processAIMessage(String message) {
        new SwingWorker<Void, Void>() {
            String response;
    
            @Override
            protected Void doInBackground() {
                try {
                    // 显示加载动画
                    chatPanel.add(createMessageBubble("正在生成回复...", false));
                    chatPanel.revalidate();
                    scrollToBottom();
    
                    response = baiduAIService.getAIResponse(message);
                } catch (Exception ex) {
                    response = "请求出现错误：" + ex.getMessage();
                }
                return null;
            }
    
            @Override
            protected void done() {
                // 移除加载动画并显示实际回复
                chatPanel.remove(chatPanel.getComponentCount() - 1);
                chatPanel.add(createMessageBubble(response, false));
                chatPanel.revalidate();
                scrollToBottom();
            }
        }.execute();
    }

    /**
     * 自动滚动聊天面板到底部。
     */
    private void scrollToBottom() {
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    /**
     * 创建支持中文的标题面板。
     *
     * @param title 标题内容
     * @return 包含标题的面板
     */
    private JPanel createHeader(String title) {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppConstants.HEADER_FONT.deriveFont(Font.BOLD, 18f));
        titleLabel.setForeground(AppConstants.PRIMARY_COLOR);
        header.add(titleLabel);
        return header;
    }

    /**
     * 验证用户输入是否合法。
     *
     * @param input 用户输入
     * @return 如果输入合法返回 true，否则返回 false
     */
    private boolean isValidInput(String input) {
        // 简单示例，检查是否包含非法字符
        return input.matches("[a-zA-Z0-9\\u4e00-\\u9fa5\\s\\p{Punct}]+");
    }
}
