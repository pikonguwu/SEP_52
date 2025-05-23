package ui;

import components.RoundedButton;
import components.RoundedPanel;
import constants.AppConstants;
import views.*;
import javax.swing.*;
import java.awt.*;

/**
 * The main frame of the Finance Tracker application.
 * It provides the main window structure including a top bar, a sidebar for navigation,
 * and a central content area that displays different views (dashboard, transactions, etc.)
 * using a CardLayout.
 */
public class FinanceTrackerUI extends JFrame {
    
    /**
     * The username of the currently logged-in user. Used for greeting.
     */
    private String username;
    
    /**
     * The panel that holds and manages the different views using a CardLayout.
     * This is the main content area of the application.
     */
    private JPanel mainContentPanel;
    
    /**
     * A reference to the currently selected (highlighted) navigation button in the sidebar.
     */
    private JButton currentNavButton;
    
    /**
     * The panel containing the navigation buttons on the left side of the frame.
     */
    private JPanel sideBarPanel;

    /**
     * Constructs the main FinanceTrackerUI frame.
     * Initializes the main window with a title, size, default close operation,
     * and sets up the layout and initial components (top bar, sidebar, main content area).
     * Sets the initial view to the Dashboard.
     *
     * @param username The username of the user currently using the application.
     */
    public FinanceTrackerUI(String username) {
        this.username = username;
        setTitle("BuckBrainAI");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Using BorderLayout with spacing for layout of elements
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 255, 250)); // 整体窗口背景：淡绿

        // Create Top Bar Panel
        JPanel topBarPanel = createTopBarPanel();
        add(topBarPanel, BorderLayout.NORTH);

        // Create Left Sidebar Panel
        sideBarPanel = createSideBarPanel();
        add(sideBarPanel, BorderLayout.WEST);

        // Main Content Area with CardLayout to manage multiple views
        mainContentPanel = new RoundedPanel(new CardLayout());

        // 创建视图实例
        DashboardView dashboardView = new DashboardView();
        TransactionsView transactionsView = new TransactionsView();
        AccountsView accountsView = new AccountsView();

        // 设置视图之间的引用关系
        dashboardView.setTransactionsView(transactionsView);
        dashboardView.setAccountsView(accountsView);

        // Add different views to the main content panel, each with a unique key string
        mainContentPanel.add(dashboardView, "Dashboard");
        mainContentPanel.add(transactionsView, "Transactions");
        mainContentPanel.add(new InvestmentsView(), "Analysis");
        mainContentPanel.add(new SettingsView(), "Settings");
        mainContentPanel.add(new BucksBrainAIChatView(), "BuckBrainAI Chat");
        mainContentPanel.add(accountsView, "Accounts");
        mainContentPanel.add(new CreditCardsView(), "Credit Cards");
        mainContentPanel.add(new CurrencySettingViewPanel(), "Currency Converter");

        add(mainContentPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Creates and returns the top bar panel.
     * This panel contains the application title ("BuckBrainAI") on the left
     * and a user section with a greeting, settings button, and avatar button on the right.
     *
     * @return The configured JPanel for the top bar.
     */
    private JPanel createTopBarPanel() {
        RoundedPanel topBar = new RoundedPanel(new BorderLayout());
        // topBar.setBackground(AppConstants.BACKGROUND_COLOR);
        topBar.setBackground(new Color(245, 255, 250)); // 顶部栏背景：更亮绿

        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Left section with BuckBrainAI title
        JLabel titleLabel = new JLabel("BuckBrainAI");
        titleLabel.setFont(AppConstants.TITLE_FONT);
        titleLabel.setForeground(AppConstants.PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        topBar.add(titleLabel, BorderLayout.WEST);

        // Right section with user greeting, settings, and avatar button
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setOpaque(false); // Transparent background
        JLabel userNameLabel = new JLabel("Hello, " + username);
        userNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userPanel.add(userNameLabel);

        // Settings button (gear icon)
        JButton settingsButton = createIconButton("⚙");
        userPanel.add(settingsButton);

        // User avatar button (icon)
        JButton avatarButton = createIconButton("👤");
        userPanel.add(avatarButton);

        topBar.add(userPanel, BorderLayout.EAST);
        return topBar;
    }

    /**
     * Creates and returns the left sidebar panel containing navigation buttons.
     * Each button corresponds to a different view managed by the CardLayout
     * in the main content panel. The "Dashboard" button is initially highlighted.
     *
     * @return The configured JPanel for the sidebar.
     */
    private JPanel createSideBarPanel() {
        RoundedPanel navPanel = new RoundedPanel(new GridLayout(8, 1, 0, 10));
        navPanel.setPreferredSize(new Dimension(220, 0));
        // navPanel.setBackground(AppConstants.BACKGROUND_COLOR);
        navPanel.setBackground(new Color(235, 255, 240)); // 侧边栏背景：柔和绿
        navPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // Navigation options (buttons)
        String[] navItems = { "Dashboard", "Transactions", "Accounts", "Analysis", "Credit Cards", "Settings",
                "BuckBrainAI Chat", "Currency Converter" };
        for (String item : navItems) {
            JButton navButton = createNavButton(item);
            navPanel.add(navButton);
            // Default selection of Dashboard
            if (item.equals("Dashboard")) {
                navButton.setBackground(AppConstants.PRIMARY_COLOR);
                navButton.setForeground(Color.WHITE);
                currentNavButton = navButton;
            }
        }
        return navPanel;
    }

    /**
     * Creates a single navigation button for the sidebar.
     * Configures its appearance and adds an action listener to switch views
     * in the main content panel and update the button highlighting.
     *
     * @param text The text to display on the button, also used as the view key.
     * @return The configured JButton.
     */
    private JButton createNavButton(String text) {
        RoundedButton button = new RoundedButton(text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(AppConstants.BUTTON_FONT);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);

        // Action listener to handle view change and button highlight
        button.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainContentPanel.getLayout();
            cl.show(mainContentPanel, text);
            // Update sidebar button highlight
            if (currentNavButton != null) {
                currentNavButton.setBackground(Color.WHITE);
                currentNavButton.setForeground(Color.BLACK);
            }
            button.setBackground(AppConstants.PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
            currentNavButton = button;
        });

        return button;
    }

    /**
     * Creates a small, rounded button typically used for icons (like settings or avatar)
     * in the top bar. Adds action listeners to navigate to specific views.
     *
     * @param iconChar The character or string to display on the button, representing an icon.
     * @return The configured JButton.
     */
    private JButton createIconButton(String iconChar) {
        RoundedButton button = new RoundedButton(iconChar);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Handle actions based on icon (settings or avatar)
        if (iconChar.equals("⚙")) { // Settings icon
            button.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContentPanel.getLayout();
                cl.show(mainContentPanel, "Settings");
                updateNavSelection(findNavButton("Settings"));
            });
        } else if (iconChar.equals("👤")) { // Avatar/User icon
            button.addActionListener(e -> {
                CardLayout cl = (CardLayout) mainContentPanel.getLayout();
                cl.show(mainContentPanel, "Accounts");
                updateNavSelection(findNavButton("Accounts"));
            });
        }
        return button;
    }

    /**
     * Finds the navigation button in the sidebar that matches the given text.
     *
     * @param text The text of the button to find.
     * @return The JButton if found, otherwise null.
     */
    private JButton findNavButton(String text) {
        for (Component comp : sideBarPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn.getText().equals(text)) {
                    return btn;
                }
            }
        }
        return null;
    }

    /**
     * Updates the visual highlight in the sidebar to indicate the selected navigation button.
     * Deselects the previously selected button and highlights the new one.
     *
     * @param selectedButton The button to highlight.
     */
    private void updateNavSelection(JButton selectedButton) {
        if (selectedButton == null)
            return;
        if (currentNavButton != null) {
            currentNavButton.setBackground(Color.WHITE);
            currentNavButton.setForeground(Color.BLACK);
        }
        selectedButton.setBackground(AppConstants.PRIMARY_COLOR);
        selectedButton.setForeground(Color.WHITE);
        currentNavButton = selectedButton;
    }
}