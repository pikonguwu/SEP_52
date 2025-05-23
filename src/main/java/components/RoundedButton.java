/**
 * Define a custom rounded corner button component, inherited from JButton, to create buttons with rounded corner effects.
 * This component can change the background color according to the mouse state (pressing, hovering), and supports custom fillet radius.
 */
package components;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

/**
 * The custom rounded corner button class, inherited from JButton, provides the functions of rounded corner display and changing the background color according to the mouse state.
 */
public class RoundedButton extends JButton {
    /**
     * The corner radius, with a default value of 20 pixels. It can be modified through the {@link #setCornerRadius(int)} method.
     */
    private int cornerRadius = 45;// 默认更大圆角
    private boolean hovered = false;
    private final Color hoverColor = new Color(72, 201, 107);

    /**
     * Constructor, initialization button.
     * 
     * @param text The text displayed on the button
     */
    public RoundedButton(String text) {
        super(text); // Call the parent class constructor, set the button text
        setContentAreaFilled(false); // Cancel the default background fill
        setFocusPainted(false); // Cancel the border drawing in the focused state
        setBorderPainted(false); // Cancel the default border drawing
        setFont(new Font("Segoe UI", Font.BOLD, 14)); // Uniform font

        // Add mouse hover listener
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                hovered = false;
                repaint();
            }
        });
    }

    /**
     * The method of setting the radius of the fillet.
     * After calling this method, a redrawing will be triggered, making the changed fillet radius take effect.
     * 
     * @param radius The new fillet radius value
     */
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius; // Update the fillet radius
        repaint(); // Trigger the redrawing to make the changes take effect
    }

    /**
     * Rewrite the paintComponent method and customize the drawing logic of the button.
     * Set different background colors according to the different states of the buttons (pressed, hover, default).
     * Draw a rounded rectangle background, and finally call the parent class method to draw the button text.
     * 
     * @param g The graphics context for drawing components
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Create a Graphics2D object for higher-level drawing operations
        Graphics2D g2 = (Graphics2D) g.create();

        // Enable anti-aliasing to make the fillet smoother
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set the background color according to the button state
        if (getModel().isPressed()) {
            // If the button is pressed, use a darker background color
            g2.setColor(getBackground().darker());
        } else if (getModel().isRollover()) {
            // If the mouse hovers over the button, use a brighter background color
            g2.setColor(getBackground().brighter());
        } else {
            // Default state: Use the normal background color
            g2.setColor(getBackground());
        }

        // Draw the rounded rectangle background
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

        // Call the parent class paintComponent method to draw the button text
        super.paintComponent(g2);

        // Release the Graphics2D object resource
        g2.dispose();
    }
}