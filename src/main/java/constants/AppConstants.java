/**
 * Define constants class for storing global styles and configurations of the application.
 * This class contains constants such as colors and fonts that can be reused throughout the application.
 */
package constants;

import java.awt.Color;
import java.awt.Font;

/**
 * Constants class that stores global styles and configuration information for the application, such as colors and fonts.
 */
public class AppConstants {
    /**
     * Primary color used for buttons, titles and other main elements.
     */
    public static final Color PRIMARY_COLOR = new Color(0, 122, 255);

    /**
     * Background color used for page or container backgrounds.
     */
    public static final Color BACKGROUND_COLOR = new Color(240, 245, 255);

    /**
     * Title font used for page or module titles, using Arial font, bold, size 24.
     */
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);

    /**
     * Button font used for text on buttons, using Microsoft YaHei font, bold, size 14.
     */
    // public static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 16);
    public static final Font BUTTON_FONT = new Font("Microsoft YaHei", Font.BOLD, 14);

    /**
     * Income color used for elements related to income.
     */
    public static final Color INCOME_COLOR = new Color(0, 122, 255);

    /**
     * Expense color used for elements related to expenses.
     */
    public static final Color EXPENSE_COLOR = new Color(255, 61, 61);

    /**
     * Trend color used for representing growth or positive trends.
     */
    public static final Color TREND_COLOR = new Color(76, 175, 80);

    /**
     * Secondary background color used for cards or secondary containers.
     */
    public static final Color SECONDARY_COLOR = new Color(245, 247, 250);

    /**
     * Font specifications (commented alternative options)
     * Originally planned to use Segoe UI font, now changed to Microsoft YaHei font.
     */
    // public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 20);
    // public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    /**
     * Header font using Microsoft YaHei, for module or section titles, bold, size 18.
     */
    public static final Font HEADER_FONT = new Font("Microsoft YaHei", Font.BOLD, 18);

    /**
     * Body font using Microsoft YaHei, for regular text content, normal style, size 14.
     */
    public static final Font BODY_FONT = new Font("Microsoft YaHei", Font.PLAIN, 14);
}