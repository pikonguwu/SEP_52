/**
 * 定义自定义圆角面板组件，继承自JPanel，用于创建带有圆角效果的面板。
 * 该组件支持设置圆角半径，并可自定义背景和边框的绘制。
 */
package components;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.LayoutManager;

/**
 * 自定义圆角面板类，继承自JPanel，提供圆角显示和自定义背景、边框绘制的功能。
 */
public class RoundedPanel extends JPanel {
    /**
     * 圆角半径，默认值为20像素。可以通过 {@link #setCornerRadius(int)} 方法进行修改。
     */
    private int cornerRadius = 20;

    /**
     * 默认构造函数，初始化面板并将面板背景设置为透明。
     */
    public RoundedPanel() {
        setOpaque(false);  // 设置面板背景为透明
    }

    /**
     * 带布局管理器的构造函数，初始化面板并设置布局管理器，同时将面板背景设置为透明。
     * 
     * @param layout 用于管理面板子组件布局的布局管理器
     */
    public RoundedPanel(LayoutManager layout) {
        super(layout); // 调用父类构造函数，设置布局管理器
        setOpaque(false); // 设置面板背景为透明
    }

    /**
     * 设置圆角半径的方法。
     * 调用此方法后会触发重绘，使更改的圆角半径生效。
     * 
     * @param radius 新的圆角半径值
     */
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius; // 更新圆角半径
        repaint(); // 触发重绘，使更改生效
    }

    /**
     * 重写paintComponent方法，自定义面板的绘制逻辑。
     * 先调用父类的绘制方法，然后绘制圆角背景和边框，最后释放图形上下文资源。
     * 
     * @param g 用于绘制组件的图形上下文
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // 调用父类的paintComponent方法

        // 创建Graphics2D对象，用于更高级的绘图操作
        Graphics2D g2 = (Graphics2D) g.create();

        // 启用抗锯齿，使圆角更平滑
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制背景
        g2.setColor(getBackground()); // 使用面板的背景颜色
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius)); // 绘制圆角矩形背景

        // 绘制边框（可选）
        g2.setColor(new Color(200, 200, 200)); // 设置边框颜色为浅灰色
        g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius)); // 绘制圆角矩形边框

        // 释放Graphics2D对象资源
        g2.dispose();
    }
}