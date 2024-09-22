package OrionLuouo.Craft.gui.component.panel;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class OrderPanel extends JPanel {
    LinkedList<Component> list = new LinkedList<>();

    public JPanel add(Component component){
        list.add(component);
        super.add(component);
        return this;
    }

    public JPanel add(Component component , int index){
        list.add(index , component);
        super.add(component);
        return this;
    }

    public Component[] getComponents(){
        return list.toArray(new Component[list.size()]);
    }

    public int indexOf(Component component){
        return list.indexOf(component);
    }

    public void remove(Component component){
        super.remove(component);
        list.remove(component);
    }

    public void remove(int index){
        super.remove(index);
        list.remove(index);
    }

    public void setOrderOf(Component component , int order){
        if(order < 0)
            throw new RuntimeException("Order shouldn't be negative.");
        remove(component);
        add(component , order);
    }

    public void adjustOrderOf(Component component , int order){
        int index = indexOf(component);
        if((index += order) < 0)
            throw new RuntimeException("Order shouldn't be negative.");
        remove(component);
        add(component , index);
    }
}
