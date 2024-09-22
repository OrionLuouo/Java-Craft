package OrionLuouo.Craft.gui.component.text;

import OrionLuouo.Craft.data.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelListener;

public class TextArea extends JTextArea {
    Thread lineWrapCheck;
    int length = 0;

    public TextArea(int width) {
        super();
        super.setSize(width, 0);
        super.setLineWrap(true);
    }

    public TextArea(String text, int width) {
        super();
        super.setSize(width, 0);
        super.setLineWrap(true);
        setText(text);
    }

    /**
     * @param height 请保持height参数与当前组件高度一致，
     *               否则方法不会被执行。
     *               <p>
     *               You can't change the height of this component.
     *               When you set a new text,the object will do it automatically.
     */
    public void setSize(int width, int height) {
        if (height != getHeight())
            return;
        super.setSize(width, height);
        resetSize();
    }

    /**
     * 调用后会自动调整组件高度。
     * <p>
     * The height will be adjusted automatically.
     */
    public void setText(String text) {
        super.setText(text);
        resetSize();
    }

    public void setEditable(boolean i) {
        super.setEditable(i);
        if (!i) {
            if (lineWrapCheck != null)
                lineWrapCheck.interrupt();
            lineWrapCheck = null;
        }
    }

    /**
     * 不同于父方法，该方法用于设置从外部输入时是否自动调整组件高度。
     * <p>
     * Being different from the super method,
     * this is used to set if adjust the component's height when typing.
     */
    public void setLineWrap(boolean i) {
        if (i) {
            lineWrapCheck = new Thread(() -> {
                for (; ; ) {
                    int time = 100;
                    if (getText().length() != length) {
                        length = getText().length();
                        resetSize();
                        time = 25;
                    }
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException ignored) {
                    }
                }
            });
            lineWrapCheck.start();
        } else {
            if (lineWrapCheck != null)
                lineWrapCheck.interrupt();
            lineWrapCheck = null;
        }
    }

    /**
     * 重新设置组件高度，用于setLineWrap为false且从外部输入字符导致超出组件的情况。
     * <p>
     * Reset the component's height .
     * Used when setLineWrap to false and the string typed overtakes the component's range.
     */
    public void resetSize() {
        int rowCount;
        super.setSize(getWidth(), (rowCount = StringUtil.getRowCount(getText(), getWidth(), getFont())) == 0 ? 1 : rowCount * Toolkit.getDefaultToolkit().getFontMetrics(getFont()).getHeight());
    }

    /**
     * 添加到父容器，以实现滚轮滑动该组件。
     * 该监听器会直接调整组件的位置。
     * 所以建议使该组件处于一个单独的容器中，
     * 或组件上下边线与容器的上下边线一致。
     * 默认滑动速度（像素）：滚轮滑动*4+2
     * <p>
     * Add to the super container,in order to realize scrolling the component by mouse wheel.
     * This listener will adjust the location of the component directly.
     * So I suggest take the component in an individual container,
     * or make sure the floor and ceil borders are consistent with the container's.
     * Default scrolling speed (pixel): Mouse wheel scrolling * 4 + 2
     */
    public MouseWheelListener getScrollListener() {
        return e -> {
            int eH = ((Container) e.getSource()).getHeight();
            int aH = getHeight();
            if (aH < eH)
                return;
            int floor = -aH;
            int h = e.getWheelRotation() << 2 + 2;
            int y = getY();
            floor += eH;
            if ((y -= h) > 0)
                y = 0;
            else if (y < floor)
                y = floor;
            setLocation(getX(), y);
        };
    }

    /**
     * @param speed 组件滑动速度（像素）：滚轮滑动*speed
     *              <p>
     *              Component scrolling speed (pixel): Mouse wheel scrolling * speed
     */
    public MouseWheelListener getScrollListener(int speed) {
        return e -> {
            int eH = ((Container) e.getSource()).getHeight();
            int aH = getHeight();
            if (aH < eH)
                return;
            int floor = -aH;
            int h = e.getWheelRotation() * speed;
            int y = getY();
            floor += eH;
            if ((y -= h) > 0)
                y = 0;
            else if (y < floor)
                y = floor;
            setLocation(getX(), y);
        };
    }
}
