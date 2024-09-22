package OrionLuouo.Craft.gui.listener.mouseListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.function.Supplier;

/**
 * 从已有组件拖出一个新组件。
 * 构造方法需要传入一个Supplier<Component>作为参数。
 * 覆写该supplier时需要将作为返回值的Component实例添加到具体容器中，
 * 且推荐将新组件与原组件分别添加到两个同级且位置、大小均相同的容器中，
 * 以保证布局不变的同时每次拖出新组件都会置于原组件之上。
 */
public abstract class DragNewComponentListener implements MouseMotionListener , MouseListener {

    Supplier<Component> supplier;
    protected Component component;
    int mouseX, mouseY;
    int x, y;

    public DragNewComponentListener(Supplier<Component> supplier) {
        this.supplier = supplier;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        component.setLocation(x + e.getX() - mouseX, y + e.getY() - mouseY);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e){

    }

    public void mousePressed(MouseEvent e){
        component = supplier.get();
        mouseX = e.getX();
        mouseY = e.getY();
        x = component.getX();
        y = component.getY();
    }

    public void mouseReleased(MouseEvent e){
        put(e);
    }

    public void mouseEntered(MouseEvent e){

    }

    public void mouseExited(MouseEvent e){

    }

    public abstract void put(MouseEvent event);

    public void addTo(Component component){
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }

    public void rollBack(){
        component.setLocation(x , y);
    }
}
