package OrionLuouo.Craft.gui.listener.mouseListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 光标移出组件范围时开始计时，
 * 设定时间后光标仍在组件外，则执行方法exit()。
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
