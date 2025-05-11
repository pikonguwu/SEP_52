/**
 * 定义自定义圆角按钮组件，继承自JButton，用于创建带有圆角效果的按钮。
 * 该组件可以根据鼠标状态（按下、悬停）改变背景颜色，并支持自定义圆角半径。
 */
package components;

import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

/**
 * 自定义圆角按钮类，继承自JButton，提供圆角显示和根据鼠标状态改变背景颜色的功能。
 */
public class RoundedButton extends JButton {
    /**
     * 圆角半径，默认值为20像素。可以通过 {@link #setCornerRadius(int)} 方法进行修改。
     */
    private int cornerRadius = 20;

    /**
     * 构造函数，初始化按钮。
     * 
     * @param text 按钮上显示的文本
     */
    public RoundedButton(String text) {
        super(text); // 调用父类构造函数，设置按钮文本
        setContentAreaFilled(false);  // 取消默认的背景填充
        setFocusPainted(false);       // 取消焦点状态下的边框绘制
        setBorderPainted(false);      // 取消默认的边框绘制
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
     * 重写paintComponent方法，自定义按钮的绘制逻辑。
     * 根据按钮的不同状态（按下、悬停、默认）设置不同的背景颜色，
     * 并绘制圆角矩形背景，最后调用父类方法绘制按钮文本。
     * 
     * @param g 用于绘制组件的图形上下文
     */
    @Override
    protected void paintComponent(Graphics g) {
        // 创建Graphics2D对象，用于更高级的绘图操作
        Graphics2D g2 = (Graphics2D) g.create();

        // 启用抗锯齿，使圆角更平滑
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 根据按钮状态设置背景颜色
        if (getModel().isPressed()) {
            // 如果按钮被按下，使用更深的背景色
            g2.setColor(getBackground().darker());
        } else if (getModel().isRollover()) {
            // 如果鼠标悬停在按钮上，使用更亮的背景色
            g2.setColor(getBackground().brighter());
        } else {
            // 默认状态，使用正常背景色
            g2.setColor(getBackground());
        }

        // 绘制圆角矩形背景
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        // 调用父类的paintComponent方法，绘制按钮文本
        super.paintComponent(g2);

        // 释放Graphics2D对象资源
        g2.dispose();
    }
}