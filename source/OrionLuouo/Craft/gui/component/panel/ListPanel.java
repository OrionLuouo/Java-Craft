package OrionLuouo.Craft.gui.component.panel;

import OrionLuouo.Craft.gui.graphics.Colors;
import OrionLuouo.Craft.gui.listener.mouseListener.DoubleClickedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

public class ListPanel {
    public static final MouseWheelListener LISTENER = e -> {
        ListPanel listPanel = ((Panel) e.getSource()).parent;
        int y = listPanel.table.getY();
        y -= e.getWheelRotation() << 4;
        if (y > 0)
            y = 0;
        else if (y < listPanel.floor)
            y = listPanel.floor;
        listPanel.table.setLocation(0, y);
    };
    DoubleClickedListener doubleClickedListener;
    Panel panel = new Panel(this);
    JPanel table = new JPanel();
    ArrayList<Label> labels = new ArrayList<>();
    int height = 0;
    int labelHeight;
    int floor;

    public ListPanel() {
        this(400);
    }

    public ListPanel(int doubleClickedGapTime) {
        doubleClickedListener = new DoubleClickedListener(doubleClickedGapTime) {
            public void doubleClicked(MouseEvent e) {
                ((Label) e.getSource()).item.doubleClicked(e);
            }

            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ((Label) e.getSource()).item.clicked(e);
            }

            public void mousePressed(MouseEvent e) {
                ((Label) e.getSource()).item.pressed(e);
            }

            public void mouseReleased(MouseEvent e) {
                ((Label) e.getSource()).item.released(e);
            }

            public void mouseEntered(MouseEvent e) {
                ((Label) e.getSource()).item.entered(e);
            }

            public void mouseExited(MouseEvent e) {
                ((Label) e.getSource()).item.exited(e);
            }
        };
        panel.setLayout(null);
        panel.add(table);
        panel.setBackground(Colors.GRAY);
        panel.addMouseWheelListener(LISTENER);
        table.setLayout(null);
        table.setLocation(0, 0);
        table.setBackground(Colors.GRAY);
        labelHeight = 25;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void add(Item item) {
        Label label = new Label(item, (labels.size() & 1) == 1 ? Colors.GRAY : Colors.GRAY_LIGHT, doubleClickedListener);
        labels.add(label);
        item.label = label;
        label.setBounds(0, height, table.getWidth(), labelHeight);
        table.add(label);
        height += labelHeight;
        table.setSize(table.getWidth(), height);
        label.setBackground(label.backgroundColor);
        label.setOpaque(true);
        floor = panel.getHeight() - height;
    }

    public void clear() {
        table.removeAll();
        labels.clear();
        height = 0;
    }

    public void setLocation(int x, int y) {
        panel.setLocation(x, y);
    }

    public void setSize(int width, int height) {
        panel.setSize(width, height);
        floor = height - this.height;
        table.setSize(width, table.getHeight());
        for (Label label : labels)
            label.setSize(width, labelHeight);
    }

    public void setBounds(int x, int y, int width, int height) {
        setLocation(x, y);
        setSize(width, height);
    }

    public static class Label extends JLabel {
        public Color backgroundColor;
        Item item;

        public Label(Item item, Color backgroundColor, DoubleClickedListener doubleClickedListener) {
            super(item.item);
            this.backgroundColor = backgroundColor;
            addMouseListener(doubleClickedListener);
            setBackground(Colors.GRAY);
            this.item = item;
        }
    }

    public static class Item {
        public String item;
        public Label label;

        public Item(String item) {
            this.item = item;
        }

        public void clicked(MouseEvent e) {

        }

        public void pressed(MouseEvent e) {
            label.setBackground(Colors.GRAY_DARK);
        }

        public void released(MouseEvent e) {
        }

        public void entered(MouseEvent e) {
            label.setBackground(Colors.GRAY_DEEP);
        }

        public void exited(MouseEvent e) {
            label.setBackground(label.backgroundColor);
        }

        public void doubleClicked(MouseEvent e) {

        }
    }

    class Panel extends JPanel {
        ListPanel parent;

        public Panel(ListPanel panel) {
            super();
            parent = panel;
        }
    }
}