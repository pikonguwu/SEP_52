/**
 * Define a custom rounded corner panel component, inherited from JPanel, to create panels with rounded corner effects.
 * This component supports setting the fillet radius, and can customize the drawing of the background and border.
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
     * The fillet radius, with a default value of 20 pixels. This can be modified using the {@link #setCornerRadius(int)} method.
     */
    private int cornerRadius = 20;

    /**
     * Default constructor, initializes the panel and sets the panel background to transparent.
     */
    public RoundedPanel() {
        setOpaque(false);  // Set the panel background to transparent
    }

    /**
     * Constructor with layout manager, initializes the panel and sets the layout manager, while setting the panel background to transparent.
     * 
     * @param layout The layout manager for managing the layout of the panel's child components
     */
    public RoundedPanel(LayoutManager layout) {
        super(layout); // 调用父类构造函数，设置布局管理器
        setOpaque(false); // 设置面板背景为透明
    }

    /**
     * Method to set the fillet radius.
     * After calling this method, the panel will be repainted to reflect the new fillet radius.
     * 
     * @param radius The new fillet radius value
     */
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius; // Update the fillet radius
        repaint(); // Trigger the redrawing to make the changes take effect
    }

    /**
     * Rewrite the paintComponent method and customize the drawing logic of the panel.
     * First, call the drawing method of the parent class, then draw the rounded corner background and border, and finally release the graphic context resources.
     * 
     * @param g The graphical context used for drawing components
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 

        Graphics2D g2 = (Graphics2D) g.create();


        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        g2.setColor(getBackground()); 
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius)); 


        g2.setColor(new Color(200, 200, 200)); 
        g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius)); 


        g2.dispose();
    }
}