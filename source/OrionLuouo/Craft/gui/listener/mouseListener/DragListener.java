package OrionLuouo.Craft.gui.listener.mouseListener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This is a.xml listener that will set a.xml component draggable.
 * Please use your component's addMouseListener() and addMouseMotionListener() methods to add this listener to it.
 * Or use the add() method of the listener.
 */
public class DragListener extends MouseAdapter {
    int mouseX, mouseY;
    int x, y;
    Component component;

    public DragListener(Component component){
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
        this.component = component;
    }

    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        x = component.getX();
        y = component.getY();
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX(),
                y = e.getY();
        component.setLocation(component.getX() + x - mouseX, component.getY() + y - mouseY);
    }

    public void rollBack() {
        component.setLocation(x, y);
    }
}
