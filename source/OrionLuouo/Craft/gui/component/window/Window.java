package OrionLuouo.Craft.gui.component.window;

import OrionLuouo.Craft.gui.graphics.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Window extends JFrame {
    public static final int SIZE_SMALL = 0,
            SIZE_MIDDLE = 1;    public static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight(),
            SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
            MIDDLE_WINDOW_HEIGHT = SCREEN_HEIGHT / 3 * 2,
            MIDDLE_WINDOW_WIDTH = SCREEN_WIDTH / 3 * 2,
            SMALL_WINDOW_HEIGHT = SCREEN_HEIGHT / 3,
            SMALL_WINDOW_WIDTH = SCREEN_WIDTH / 3;
    protected Container panel;
    protected int panelWidth, panelHeight;
    protected ComponentListener componentListener;

    public Window() {
        super();
        panel = new JPanel();
        setBounds(SCREEN_WIDTH / 6, SCREEN_HEIGHT / 6, MIDDLE_WINDOW_WIDTH, MIDDLE_WINDOW_HEIGHT);
        initialize();
    }

    /**
     * @param size 所有预设均为居于屏幕正中
     *             SIZE_SMALL：屏幕大小的三分之一
     *             SIZE_MIDDLE：屏幕大小的三分之二
     *             <p>
     *             All the presets are setting to the middle of the screen.
     *             SIZE_SMALL: One third of the screen's size
     *             SIZE_MIDDLE: Two thirds of the screen's size
     */
    public Window(int size) {
        super();
        panel = new JPanel();
        switch (size) {
            case 0 -> setBounds(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 3, SCREEN_WIDTH / 3, SCREEN_HEIGHT / 3);
            case 1 -> setBounds(SCREEN_WIDTH / 6, SCREEN_HEIGHT / 6, MIDDLE_WINDOW_WIDTH, MIDDLE_WINDOW_HEIGHT);
        }
        initialize();
    }

    /**
     * 你可以提供一个Container作为Window的内置超容器，代替默认的JPanel。
     * <p>
     * You can provide a.xml Container object for Window's inner super container instead the default JPanel object.
     */
    public Window(Container container) {
        super();
        this.panel = container;
        initialize();
    }

    public Window(Container container, int size) {
        super();
        this.panel = container;
        switch (size) {
            case 0 -> setBounds(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 3, SMALL_WINDOW_WIDTH, SMALL_WINDOW_HEIGHT);
            case 1 -> setBounds(SCREEN_WIDTH / 6, SCREEN_HEIGHT / 6, MIDDLE_WINDOW_WIDTH, MIDDLE_WINDOW_HEIGHT);
        }
        initialize();
    }

    public Window(int width, int height) {
        super();
        panel = new JPanel();
        setBounds((SCREEN_WIDTH - width) / 2, (SCREEN_HEIGHT - height) / 2, width, height);
        initialize();
    }

    protected void initialize() {
        setContentPane(panel);
        panel.setLayout(null);
        setVisible(true);
        panelWidth = panel.getWidth();
        panelHeight = panel.getHeight();
        addComponentListener(componentListener = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                refresh();
            }
        });
    }

    public Window setExitOnClose() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        return this;
    }

    /**
     * 你可以直接调用该方法，不必添加一个超容器JPanel，
     * Window对象内置一个JPanel。
     * <p>
     * You can invoke this,
     * but not necessarily add a.xml super container JPanel.
     * The Window object has an inner JPanel object.
     */
    public Window add(Component component) {
        panel.add(component);
        return this;
    }

    public Window add(Component component , int index){
        panel.add(component , index);
        return this;
    }

    void refresh() {
        panelWidth = panel.getWidth();
        panelHeight = panel.getHeight();
        try {
            resize();
        } catch (Exception ignored) {
        }
    }

    public Component getComponent(int index){
        return panel.getComponent(index);
    }

    public void resize() {
    }

    @Override
    public void setBackground(Color color) {
        if (panel == null) {
            super.setBackground(color);
        }
        else {
            panel.setBackground(color);
        }
    }
}