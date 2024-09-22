package OrionLuouo.Craft.gui.listener.mouseListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ����Ƴ������Χʱ��ʼ��ʱ��
 * �趨ʱ�������������⣬��ִ�з���exit()��
 */
public class MenuListener extends MouseAdapter {
    int waitTime;
    boolean mouseIn = true;

    public MenuListener() {
        waitTime = 250;
    }

    public MenuListener(int waitTime) {
        this.waitTime = waitTime;
    }

    public void mouseEntered(MouseEvent e) {
        mouseIn = true;
    }

    public void mouseExited(MouseEvent e) {
        mouseIn = false;
        SwingUtilities.invokeLater(() -> {
            try {
                Thread.sleep(waitTime);
                if (!mouseIn)
                    exit(e);
            } catch (InterruptedException ignored) {
            }
        });
    }

    public void exit(MouseEvent e) {
        ((Component) e.getSource()).setVisible(false);
    }
}
