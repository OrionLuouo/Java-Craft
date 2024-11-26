package OrionLuouo.Craft.gui.component.text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TextPanel {
    JPanel panel;
    OrionLuouo.Craft.gui.component.text.TextArea area;
    int floor;
    int height;
    int rotationSpeed = 12;
    MouseWheelListener listener;

    public TextPanel() {
        panel = new JPanel();
        panel.setLayout(null);
        area = new TextArea("", 100);
        area.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                floor = height - area.getHeight();
            }
        });
        panel.addMouseWheelListener((listener = area.getScrollListener()));
        area.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int y = e.getY() + area.getY();
                if (y < 0)
                    rotate(y >> 4);
                else if (y > height)
                    rotate((y - height) >> 4);
            }
        });
        area.setLineWrap(true);
        area.setLocation(0, 0);
        panel.add(area);
    }

    public void setSize(int width, int height) {
        this.height = height;
        panel.setSize(width, height);
        area.setSize(width, area.getHeight());
        floor = height - area.getHeight();
    }

    private void rotate(int rotation) {
        int y = area.getY();
        if ((y -= rotation * rotationSpeed) > 0)
            y = 0;
        else if (y < floor)
            y = floor;
        area.setLocation(0, y);
    }

    public void setText(String text) {
        area.setText(text);
        floor = height - area.getHeight();
    }

    public void appendText(String text) {
        area.setText(area.getText() + text);
    }

    public void setBackground(Color color) {
        panel.setBackground(color);
        area.setBackground(color);
    }

    public void setLocation(int x, int y) {
        panel.setLocation(x, y);
    }

    public void setEditable(boolean editable) {
        area.setEditable(editable);
    }

    /**
     * ???û??????
     *
     * @param speed ??????????? speed ???????
     *              ???????????? ????????? / 16 * speed??
     */
    public void setRotationSpeed(int speed) {
        this.rotationSpeed = speed;
        area.removeMouseWheelListener(listener);
        area.addMouseWheelListener((listener = area.getScrollListener(speed)));

    }

    public JPanel getPanel() {
        return panel;
    }
}
