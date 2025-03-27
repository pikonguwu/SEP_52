package com.main.java.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RoundedPanel extends JPanel {
    private int cornerRadius = 20;  // 圆角半径

    public RoundedPanel() {
        setOpaque(false);  // 设置透明背景
    }

    public RoundedPanel(LayoutManager layout) {
        super(layout); // 调用父类构造函数
        setOpaque(false);
    }

    // 设置圆角半径
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制背景
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        // 绘制边框（可选）
        g2.setColor(new Color(200, 200, 200));
        g2.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, cornerRadius, cornerRadius));

        g2.dispose();
    }

    // 新增方法：创建 Exit 按钮
    public static JButton createExitButton(JFrame parentFrame, String text, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(new Color(0, 123, 255)); // 设置背景色
        btn.setForeground(Color.WHITE); // 设置文字颜色
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(0, 100, 200)); // 鼠标悬停时改变背景色
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(0, 123, 255)); // 鼠标离开时恢复背景色
            }
        });

        btn.addActionListener(listener);

        return btn;
    }
}
