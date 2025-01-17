package OrionLuouo.test.Craft.gui.component.panel;

import OrionLuouo.Craft.gui.component.panel.PopPanel;
import OrionLuouo.Craft.gui.component.window.Window;

import java.awt.*;

public class PopPanelTest {
    static Window window;
    static PopPanel panel;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            window = new Window();
            window.setExitOnClose();
            panel = new PopPanel();
            panel.setBounds(0 , 0 , 100 , 100);
            panel.setBackground(Color.BLACK);
            window.add(panel);
        });
    }
}
