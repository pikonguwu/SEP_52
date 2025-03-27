package com.main.java.components;

import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {
    private int cornerRadius = 20;  // 圆角半径
    
    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);  // 取消默认填充
        setFocusPainted(false);       // 取消焦点边框
        setBorderPainted(false);      // 取消边框绘制
    }

    // 设置圆角半径
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制背景（支持悬停/按下状态）
        if (getModel().isPressed()) {
            g2.setColor(getBackground().darker());
        } else if (getModel().isRollover()) {
            g2.setColor(getBackground().brighter());
        } else {
            g2.setColor(getBackground());
        }
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        // 绘制文字
        super.paintComponent(g2);
        g2.dispose();
    }
}
