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
 * BucksBrainAIChatView is a view class implementing an intelligent assistant chat interface.
 * Users can input messages, and the system calls the Baidu AI service to generate responses,
 * which are then displayed as message bubbles in the interface. It also handles
 * saving and loading conversation history.
 */
public class BucksBrainAIChatView extends BaseView {
    /**
     * The panel where message bubbles are displayed.
     */
    private JPanel chatPanel;
    
    /**
     * The text field for user input messages.
     */
    private JTextField inputField;
    
    /**
     * The button to send the user's message.
     */
    private JButton sendButton;
    
    /**
     * Service responsible for interacting with the Baidu AI API.
     */
    private BaiduAIService baiduAIService;
    
    /**
     * Scroll pane to allow scrolling through the chat history.
     */
    private JScrollPane scrollPane;
    
    /**
     * List storing the entire conversation history (user and AI messages) as formatted strings.
     */
    private final List<String> conversationHistory = new ArrayList<>();
    
    /**
     * File path for saving and loading the conversation history.
     */
    private static final String HISTORY_FILE = "conversation_history.txt";
    
    /**
     * Prefix used to identify user messages in the history file and list.
     */
    private static final String USER_PREFIX = "[用户]: ";
    
    /**
     * Prefix used to identify AI messages in the history file and list.
     */
    private static final String AI_PREFIX = "[AI]: ";

    /**
     * Constructs a new BucksBrainAIChatView.
     * Initializes the connection to the Baidu AI service and loads the
     * conversation history from the history file if it exists.
     */
    public BucksBrainAIChatView() {
        this.baiduAIService = new BaiduAIService();
        // Combines loading and displaying history
        loadAndDisplayHistory();
    }

    /**
     * Gets the name of this view.
     *
     * @return The fixed name "Bucks Brain".
     */
    @Override
    public String getViewName() {
        return "Bucks Brain";
    }
    
    /**
     * Initializes the user interface components and layout for the chat view.
     * Sets up the chat panel, input field, send button, scroll pane,
     * and arranges them using BorderLayout.
     */
    @Override
    protected void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // 初始化UI组件 (Initialize UI components)
        chatPanel = new JPanel();
        // Use BoxLayout along the Y_AXIS to stack message bubbles vertically
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chat Content"));
        // Adjust scroll speed for smoother scrolling
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Input panel at the bottom with a text field and send button
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputField = new JTextField();
        inputField.setFont(AppConstants.BODY_FONT.deriveFont(14f));

        sendButton = new JButton("Send");
        sendButton.setFont(AppConstants.BUTTON_FONT);
        // Add action listener using a method reference
        sendButton.addActionListener(this::handleSendMessage);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Add main sections to the view
        add(createHeader("Bucks Brain - AI Assistant"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    }

    /**
     * Saves the current conversation history to the predefined history file.
     * Each message is written on a new line.
     */
    private void saveConversationHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE))) {
            // Synchronize access as history might be updated from EDT and SwingWorker
            synchronized (conversationHistory) {
                for (String message : conversationHistory) {
                    writer.write(message);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to save the conversation history: " + e.getMessage());
        }
    }

    /**
     * Loads the conversation history from the history file.
     * If the file exists, it reads the messages, clears the current chat display,
     * and adds the historical messages as bubbles to the chat panel.
     * If no file exists, the panel remains empty.
     * The display update is done on the Event Dispatch Thread (EDT).
     */
    private void loadAndDisplayHistory() {
        File file = new File(HISTORY_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE))) {
                // Synchronize access while clearing and adding history
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
                System.err.println("Failed to load the conversation history: " + e.getMessage());
            }
        }

        // 显示历史记录 (Display history records) on the EDT
        SwingUtilities.invokeLater(() -> {
            // Synchronize access while updating the UI based on history
            synchronized (conversationHistory) {
                chatPanel.removeAll(); // Clear current chat display
                for (String message : conversationHistory) {
                    boolean isUser = message.startsWith(USER_PREFIX);
                    String content = isUser ? message.substring(USER_PREFIX.length())
                            : message.substring(AI_PREFIX.length());
                    chatPanel.add(createMessageBubble(content, isUser));
                }
            }
            chatPanel.revalidate(); // Re-layout the panel after adding components
            scrollToBottom(); // Scroll to the end of the loaded history
        });
    }

    /**
     * Adds a user message to the chat display and the conversation history.
     * Creates a message bubble for the text, adds it to the chat panel,
     * updates the history list, saves history, revalidates the panel,
     * and scrolls to the bottom.
     *
     * @param message The text of the user's message.
     */
    private void addUserMessage(String message) {
        // Format message with user prefix for history
        String formattedMessage = USER_PREFIX + message;
        
        // Add the message bubble to the UI
        chatPanel.add(createMessageBubble(message, true));
        
        // Update history list and save
        synchronized (conversationHistory) {
             conversationHistory.add(formattedMessage);
        }
        saveConversationHistory();
        
        // Update UI and scroll
        chatPanel.revalidate();
        scrollToBottom();
    }

    /**
     * Creates a JPanel containing a JTextPane styled as a message bubble.
     * The appearance (background color, alignment) differs based on whether
     * it's a user message or an AI message. Uses HTML for basic formatting
     * within the text pane.
     *
     * @param text   The text content of the message.
     * @param isUser True if the message is from the user, false if from the AI.
     * @return A JPanel representing the message bubble.
     */
    private JPanel createMessageBubble(String text, boolean isUser) {
        JPanel bubblePanel = new JPanel(new BorderLayout()) {
            // Custom insets for padding around the bubble panel
            @Override
            public Insets getInsets() {
                return new Insets(8, 8, 8, 8);
            }
        };

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setContentType("text/html"); // Allows basic HTML rendering
        // Wrap text in HTML body with padding and preferred width
        textPane.setText("<html><body style='width: 300px; padding: 5px;'>" + text + "</body></html>");
        textPane.setFont(AppConstants.BODY_FONT.deriveFont(14f));
        // Set background color based on sender
        textPane.setBackground(isUser ? new Color(220, 245, 255) : new Color(240, 240, 240));

        // Add rounded border (simple line border simulation here)
        textPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isUser ? new Color(180, 220, 255) : new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        bubblePanel.add(textPane, BorderLayout.CENTER);
        // Set alignment (left for user, right for AI)
        bubblePanel.setAlignmentX(isUser ? Component.LEFT_ALIGNMENT : Component.RIGHT_ALIGNMENT);

        return bubblePanel;
    }

    /**
     * Handles the action when the "Send" button is clicked or Enter is pressed in the input field.
     * Retrieves text from the input field, validates it, adds the user message to the UI,
     * initiates the AI response processing, and clears the input field.
     *
     * @param e The ActionEvent.
     */
    private void handleSendMessage(ActionEvent e) {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            // 输入验证 (Input validation)
            if (isValidInput(message)) {
                // Add user message to display and history
                addUserMessage(message);
                // Process the message to get AI response (asynchronously)
                processAIMessage(message);
                // Clear the input field
                inputField.setText("");
            } else {
                // Show error message for invalid input
                JOptionPane.showMessageDialog(this, "The input contains illegal characters. Please re-enter.");
            }
        }
    }

    /**
     * Processes a user message by sending it to the AI service asynchronously using a SwingWorker.
     * Upon receiving a response, it formats the response, adds an AI message bubble
     * to the chat display, updates the history list, saves history, revalidates the panel,
     * and scrolls to the bottom.
     *
     * @param message The user's message string to send to the AI.
     */
    public void processAIMessage(String message) {
        new SwingWorker<Void, Void>() {
            String response; // Variable to hold the AI response

            @Override
            protected Void doInBackground() throws Exception {
                try {
                    // Call the AI service (blocking operation)
                    String rawResponse = baiduAIService.getAIResponse(message);
                    // 解析JSON并提取需要的内容 (Parse JSON and extract required content)
                    response = formatAIResponse(rawResponse);
                } catch (Exception ex) {
                    // Handle exceptions during AI request
                    response = "An error occurred in the request: " + ex.getMessage();
                    System.err.println("Error getting AI response: " + ex.getMessage());
                    ex.printStackTrace();
                }
                return null; // Signal completion
            }

            @Override
            protected void done() {
                // This runs on the EDT after doInBackground is complete
                String formattedResponse = AI_PREFIX + response; // Format response for history
                
                // Add AI message bubble to the UI
                chatPanel.add(createMessageBubble(response, false));
                
                // Update history list and save
                 synchronized (conversationHistory) {
                    conversationHistory.add(formattedResponse);
                }
                saveConversationHistory();
                
                // Update UI and scroll
                chatPanel.revalidate();
                scrollToBottom();
            }
        }.execute(); // Execute the SwingWorker
    }

    /**
     * Parses the raw JSON response string received from the AI service
     * to extract the main text content from the "result" field.
     * Handles basic escape sequences within the result string (\n, \", \t)
     * for display in HTML.
     *
     * @param rawResponse The raw JSON string received from the AI service.
     * @return The formatted text content extracted from the response,
     *         or an error message if parsing fails.
     */
    private String formatAIResponse(String rawResponse) {
        try {
            // Simple extraction of the "result" field content
            int start = rawResponse.indexOf("\"result\":\"") + 10;
            int end = rawResponse.indexOf("\",\"is_truncated\""); // Look for the field after result

            if (start > 0 && end > start) {
                String result = rawResponse.substring(start, end);
                // Process escape characters for HTML display
                result = result.replace("\\n", "<br>") // Replace newline escapes with HTML break tags
                        .replace("\\\"", "\"")     // Unescape quotes
                        .replace("\\t", "    ");  // Replace tab escapes with spaces
                // Original bold tags like **text** might need more robust handling if they exist and should be preserved/converted to HTML <b>
                // This current implementation only handles the escapes listed.
                return result;
            }
            // If extraction fails, return an informative message
            return "Unable to parse the AI response: " + rawResponse;
        } catch (Exception e) {
            // Handle exceptions during parsing
            return "Error occurred when analyzing the AI response: " + e.getMessage() + "\nOriginal response:\n" + rawResponse;
        }
    }

    /**
     * Scrolls the chat panel's vertical scroll bar to the bottom,
     * ensuring the latest message is visible.
     */
    private void scrollToBottom() {
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        // Use invokeLater to ensure scrolling happens after UI updates are complete
         SwingUtilities.invokeLater(() -> {
             vertical.setValue(vertical.getMaximum());
         });
    }

    /**
     * Creates a JPanel with a JLabel displaying the title of the chat view.
     * Used for the header section.
     *
     * @param title The title text to display.
     * @return A JPanel containing the title label.
     */
    private JPanel createHeader(String title) {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Left-aligned flow layout
        JLabel titleLabel = new JLabel(title);
        // Set font and color for the title
        titleLabel.setFont(AppConstants.HEADER_FONT.deriveFont(Font.BOLD, 18f));
        titleLabel.setForeground(AppConstants.PRIMARY_COLOR);
        header.add(titleLabel);
        return header;
    }

    /**
     * Performs basic validation on the user input string using a regular expression.
     * Allows alphanumeric characters, Chinese characters, spaces, and common punctuation.
     *
     * @param input The user input string.
     * @return true if the input contains only allowed characters, false otherwise.
     */
    private boolean isValidInput(String input) {
        // Simple example: Check if the input matches the pattern of allowed characters
        // [a-zA-Z0-9] - alphanumeric characters
        // \\u4e00-\\u9fa5 - common Chinese characters Unicode range
        // \\s - whitespace characters
        // \\p{Punct} - common punctuation characters
        return input.matches("[a-zA-Z0-9\\u4e00-\\u9fa5\\s\\p{Punct}]+");
    }
}