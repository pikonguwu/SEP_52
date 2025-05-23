// src/views/BaseView.java
package views;

import javax.swing.JPanel;

/**
 * BaseView is an abstract class that extends JPanel and serves as the base class for all views in the application.
 * It defines two abstract methods, {@link #getViewName()} and {@link #initUI()}, which subclasses must implement.
 * In the constructor, BaseView automatically calls the {@link #initUI()} method to initialize the user interface.
 */
public abstract class BaseView extends JPanel {
    /**
     * Abstract method to get the name of the view.
     * This name is typically used to identify the view in a CardLayout or navigation.
     * @return Returns the name of the view.
     */
    public abstract String getViewName();

    /**
     * Abstract method to initialize the user interface of the view.
     * Subclasses must implement this method to define the UI components and layout for their specific view.
     * This method is automatically called by the BaseView constructor.
     */
    protected abstract void initUI();
    
    /**
     * Constructor for BaseView.
     * When a BaseView instance (or a subclass instance) is created, the {@link #initUI()} method is automatically called.
     */
    public BaseView() {
        initUI();
    }
}