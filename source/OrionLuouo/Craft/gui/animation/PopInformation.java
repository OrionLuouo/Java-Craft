package OrionLuouo.Craft.gui.animation;


import OrionLuouo.Craft.data.StringUtil;
import OrionLuouo.Craft.gui.listener.mouseListener.MenuListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PopInformation {
    public static void modify(Component component , Container container , int index , String information , Color color) {
        EventQueue.invokeLater(() -> {
            MenuListener listener = new MenuListener();
            JLabel label = new JLabel(information);
            int cache;
            label.setSize(StringUtil.getStringWidth(information , component.getFont()) * 4 / 3 , component.getFontMetrics(component.getFont()).getHeight() * 5 / 3);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            if (color != null) {
                label.setOpaque(true);
                label.setBackground(color);
            }
            label.setVisible(false);
            label.addMouseListener(listener);
            component.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    EventQueue.invokeLater(() -> {
                        if (label.isVisible()) {
                            return;
                        }
                        int componentWidth = component.getWidth() , componentHeight = component.getHeight() , x = e.getX() , y = e.getY();
                        float rowRate = (float) x / componentWidth , columnRate = (float) y / componentHeight;
                        rowRate = rowRate < 0.5 ? 1 - rowRate : rowRate;
                        columnRate = columnRate < 0.5 ? 1 - columnRate : columnRate;
                        if (rowRate > columnRate) {
                            x = x < componentWidth >> 1 ? 0 : componentWidth;
                        }
                        else {
                            y = y < componentHeight >> 1 ? 0 : componentHeight;
                        }
                        x += component.getX();
                        y += component.getY();
                        label.setLocation(x , y);
                        label.setVisible(true);
                    });
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    EventQueue.invokeLater(() -> {
                        if (!listener.isMouseIn()) {
                            label.setVisible(false);
                        }
                    });
                }
            });
            container.add(label , index);
        });
    }
}
