// src/views/BaseView.java
package views;

import javax.swing.JPanel;

/**
 * BaseView 是一个抽象类，继承自 JPanel，用于作为所有视图的基类。
 * 它定义了两个抽象方法：getViewName() 和 initUI()，并要求子类实现这些方法。
 * 在构造函数中，BaseView 会自动调用 initUI() 方法来初始化用户界面。
 */
public abstract class BaseView extends JPanel {
    /**
     * 获取视图的名称。
     * @return 返回视图的名称。
     */
    public abstract String getViewName();

    /**
     * 初始化用户界面。子类必须实现此方法以定义视图的 UI 组件和布局。
     */
    protected abstract void initUI();
    
    /**
     * BaseView 的构造函数。在创建 BaseView 实例时，会自动调用 initUI() 方法。
     */
    public BaseView() {
        initUI();
    }
}
