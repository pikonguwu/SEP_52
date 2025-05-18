// src/views/BucksBrainAIChatView.java
package views;

import constants.AppConstants;
import services.BaiduAIService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BucksBrainAIChatView 是一个用于实现智能助手聊天界面的视图类。
 * 用户可以输入消息，系统会调用百度 AI 服务生成回复，并以消息气泡的形式展示在界面上。
 */
public class BucksBrainAIChatView extends BaseView {
    private JPanel chatPanel;
    private JTextField inputField;
    private JButton sendButton;
    private BaiduAIService baiduAIService;
    private JScrollPane scrollPane;
    private final List<String> conversationHistory = new ArrayList<>();
    private static final String HISTORY_FILE = "conversation_history.txt";
    private static final String USER_PREFIX = "[用户]: ";
    private static final String AI_PREFIX = "[AI]: ";

    public BucksBrainAIChatView() {
        this.baiduAIService = new BaiduAIService();
        loadAndDisplayHistory(); // 整合加载和显示
    }

    @Override
    public String getViewName() {
        return "Bucks Brain";
    }
    @Override
    protected void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // 初始化UI组件
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chat Content"));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputField = new JTextField();
        inputField.setFont(AppConstants.BODY_FONT.deriveFont(14f));

        sendButton = new JButton("发送");
        sendButton.setFont(AppConstants.BUTTON_FONT);
        sendButton.addActionListener(this::handleSendMessage);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(createHeader("Bucks Brain - 智能助手"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    }


    private void saveConversationHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE))) {
            synchronized (conversationHistory) {
                for (String message : conversationHistory) {
                    writer.write(message);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("保存对话历史失败: " + e.getMessage());
        }
    }


    /**
     * 显示历史对话记录
     */
    private void loadAndDisplayHistory() {
        File file = new File(HISTORY_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE))) {
                synchronized (conversationHistory) {
                    conversationHistory.clear();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.trim().isEmpty()) {
                            conversationHistory.add(line);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("加载对话历史失败: " + e.getMessage());
            }
        }

        // 显示历史记录
        SwingUtilities.invokeLater(() -> {
            synchronized (conversationHistory) {
                chatPanel.removeAll();
                for (String message : conversationHistory) {
                    boolean isUser = message.startsWith(USER_PREFIX);
                    String content = isUser ? message.substring(USER_PREFIX.length())
                            : message.substring(AI_PREFIX.length());
                    chatPanel.add(createMessageBubble(content, isUser));
                }
                chatPanel.revalidate();
                scrollToBottom();
            }
        });
    }


    private void addUserMessage(String message) {
        String formattedMessage = USER_PREFIX + message;
        chatPanel.add(createMessageBubble(message, true));
        conversationHistory.add(formattedMessage);
        saveConversationHistory();
        chatPanel.revalidate();
        scrollToBottom();
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
        textPane.setText("<html><body style='width: 300px; padding: 5px;'>" + text + "</body></html>");
        textPane.setFont(AppConstants.BODY_FONT.deriveFont(14f));
        textPane.setBackground(isUser ? new Color(220, 245, 255) : new Color(240, 240, 240));

        // 添加圆角边框
        textPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isUser ? new Color(180, 220, 255) : new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // 设置对齐方向
        bubblePanel.add(textPane, BorderLayout.CENTER);
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
     * 处理 AI 消息，调用百度 AI 服务生成回复并添加到聊天面板中。
     *
     * @param message 用户消息
     */
    public void processAIMessage(String message) {
        new SwingWorker<Void, Void>() {
            String response;

            @Override
            public Void doInBackground() {
                try {
                    String rawResponse = baiduAIService.getAIResponse(message);
                    // 解析JSON并提取需要的内容
                    response = formatAIResponse(rawResponse);
                } catch (Exception ex) {
                    response = "请求出现错误：" + ex.getMessage();
                }
                return null;
            }

            @Override
            public void done() {
                String formattedResponse = AI_PREFIX + response;
                chatPanel.add(createMessageBubble(response, false));
                conversationHistory.add(formattedResponse);
                saveConversationHistory();
                chatPanel.revalidate();
                scrollToBottom();
            }
        }.execute();
    }

    /**
     * 格式化AI返回的JSON响应
     * @param rawResponse 原始JSON响应
     * @return 格式化后的纯文本内容
     */
    private String formatAIResponse(String rawResponse) {
        try {
            // 简单提取result字段内容
            int start = rawResponse.indexOf("\"result\":\"") + 10;
            int end = rawResponse.indexOf("\",\"is_truncated\"");
            if (start > 0 && end > start) {
                String result = rawResponse.substring(start, end);
                // 处理转义字符
                result = result.replace("\\n", "<br>")
                        .replace("\\\"", "\"")
                        .replace("\\t", "    ");
                // 保留原有的加粗标记
                return result;
            }
            return "无法解析AI响应: " + rawResponse;
        } catch (Exception e) {
            return "解析AI响应时出错: " + e.getMessage() + "\n原始响应:\n" + rawResponse;
        }
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