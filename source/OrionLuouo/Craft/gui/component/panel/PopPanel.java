package OrionLuouo.Craft.gui.component.panel;

import OrionLuouo.Craft.gui.graphics.Colors;
import OrionLuouo.Craft.data.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PopPanel extends OrderPanel {
    public static final Color GRAY = new Color(160, 160, 160);
    JPanel head, body;
    JLabel icon, title;
    ActionLabel close;
    MouseListener listener = new MouseAdapter() {
        public void mousePressed(MouseEvent event) {
            close.setBackground(Colors.GRAY_LIGHT);
        }

        public void mouseEntered(MouseEvent event) {
            close.setBackground(GRAY);
        }

        public void mouseExited(MouseEvent event) {
            close.setBackground(Colors.GRAY);
        }

        public void mouseClicked(MouseEvent event) {
            setVisible(false);
            close();
        }
    };

    public PopPanel() {
        super();
        head = new JPanel();
        head.setLayout(null);
        head.setBackground(Colors.GRAY);
        head.setOpaque(true);
        add(head);
        close = new ActionLabel(this);
        close.setSize(45, 30);
        close.addMouseListener(listener);
        close.setOpaque(true);
        close.setBackground(Colors.GRAY);
        head.add(close);
    }

    private void close() {
        getParent().remove(this);
    }

    public void setBody(JPanel panel) {
        if (body != null)
            remove(body);
        this.body = panel;
        add(body);
        body.setLocation(0, 30);
    }

    public void setSize(int width, int height) {
        super.setSize(width, height);
        adjustSize(width, height);
    }

    private void adjustSize(int width, int height) {
        head.setSize(width, 30);
        if (body != null)
            body.setSize(width, height - 30);
        close.setLocation(width - 45, 0);
        if (title != null)
            title.setSize(width > getWidth() - (icon == null ? 0 : 30) - 135 ? getWidth() - (icon == null ? 0 : 30) - 135 : width, 30);
    }

    public void setSize(Dimension dimension) {
        setSize(dimension.width, dimension.height);
    }

    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        adjustSize(width, height);
    }

    public void setBounds(Rectangle rectangle) {
        setBounds(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public void setHeadBackground(Color color) {
        head.setBackground(color);
    }

    public void setHeadOpaque(boolean i) {
        head.setOpaque(i);
    }

    public void setTitle(String title) {
        if (this.title == null) {
            this.title = new JLabel();
            head.add(this.title);
        }
        this.title.setText(title);
        int width = StringUtil.getStringWidth(title, this.title.getFont());
        this.title.setSize(width > getWidth() - (icon == null ? 0 : 30) - 135 ? getWidth() - (icon == null ? 0 : 30) - 135 : width, 30);
        this.title.setLocation(icon == null ? 0 : 30, 0);
    }

    public void setIcon(Icon icon) {
        if (this.icon == null) {
            this.icon = new JLabel();
            this.icon.setBounds(0, 0, 30, 30);
            head.add(this.icon);
        }
        this.icon.setIcon(icon);
        if (title != null)
            title.setLocation(30, 0);
    }

    class ActionLabel extends JLabel {
        JPanel panel;

        public ActionLabel(JPanel panel) {
            super();
            this.panel = panel;
        }

        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            graphics.drawLine(18, 9, 29, 20);
            graphics.drawLine(18, 20, 29, 9);
        }
    }
}
