package OrionLuouo.Craft.gui.listener.mouseListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class DoubleClickedListener extends MouseAdapter {
    int waitingTime;
    long lastClick = 0;

    public DoubleClickedListener() {
        waitingTime = 400;
    }

    public DoubleClickedListener(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void mouseClicked(MouseEvent e) {
        long time = System.currentTimeMillis();
        if (time - lastClick <= waitingTime)
            doubleClicked(e);
        lastClick = time;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public abstract void doubleClicked(MouseEvent e);
}
