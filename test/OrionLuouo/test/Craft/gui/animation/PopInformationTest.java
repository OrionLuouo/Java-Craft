package OrionLuouo.test.Craft.gui.animation;

import OrionLuouo.Craft.gui.animation.PopInformation;
import OrionLuouo.Craft.gui.component.window.Window;
import OrionLuouo.Craft.gui.graphics.Colors;

import javax.swing.*;
import java.awt.*;

public class PopInformationTest {
    static Window window;
    static JPanel container;
    static JLabel body;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            window = new Window();
            window.setExitOnClose();
            container = new JPanel();
            container.setLayout(null);
            container.setBounds(0 , 0 , window.getWidth() , window.getHeight());
            window.add(container);
            body = new JLabel();
            body.setBounds(0 , 0 , 100 , 100);
            body.setOpaque(true);
            body.setBackground(Colors.GRAY_80);
            container.add(body);
            PopInformation.modify(body , container , 0 , "qwq" , Colors.GRAY_DEEP);
        });
    }
}
