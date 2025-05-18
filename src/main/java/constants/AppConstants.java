/**
 * 定义常量类，用于存储应用程序的全局样式和配置。
 * 该类包含了颜色、字体等常量，可在整个应用程序中复用。
 */
package constants;

import java.awt.Color;
import java.awt.Font;

/**
 * 常量类，存储应用程序的全局样式和配置信息，如颜色、字体等。
 */
public class AppConstants {
    /**
     * 主色调，用于按钮、标题等主要元素。
     */
    public static final Color PRIMARY_COLOR = new Color(0, 122, 255);

    /**
     * 背景色，用于页面背景或容器背景。
     */
    public static final Color BACKGROUND_COLOR = new Color(240, 245, 255);

    /**
     * 标题字体，用于页面或模块的标题，使用Arial字体，加粗，字号24。
     */
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);

    /**
     * 按钮字体，用于按钮上的文字，使用微软雅黑字体，加粗，字号14。
     */
    // public static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 16);
    public static final Font BUTTON_FONT = new Font("Microsoft YaHei", Font.BOLD, 14);

    /**
     * 收入颜色，用于表示收入相关的元素。
     */
    public static final Color INCOME_COLOR = new Color(0, 122, 255);

    /**
     * 支出颜色，用于表示支出相关的元素。
     */
    public static final Color EXPENSE_COLOR = new Color(255, 61, 61);

    /**
     * 趋势颜色，用于表示增长或积极的趋势。
     */
    public static final Color TREND_COLOR = new Color(76, 175, 80);

    /**
     * 次级背景色，用于卡片或次要容器。
     */
    public static final Color SECONDARY_COLOR = new Color(245, 247, 250);

    /**
     * 字体规范（已注释的备选方案）
     * 原计划使用Segoe UI字体，现改为微软雅黑字体。
     */
    // public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 20);
    // public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    /**
     * 标题字体，使用微软雅黑，用于模块或部分的标题，加粗，字号18。
     */
    public static final Font HEADER_FONT = new Font("Microsoft YaHei", Font.BOLD, 18);

    /**
     * 正文字体，使用微软雅黑，用于普通文本内容，正常样式，字号14。
     */
    public static final Font BODY_FONT = new Font("Microsoft YaHei", Font.PLAIN, 14);
}