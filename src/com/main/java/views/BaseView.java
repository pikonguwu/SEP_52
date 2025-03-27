// src/views/BaseView.java
package com.main.java.views;

import javax.swing.JPanel;

public abstract class BaseView extends JPanel {
    public abstract String getViewName();
    protected abstract void initUI();
    
    public BaseView() {
        initUI();
    }
}