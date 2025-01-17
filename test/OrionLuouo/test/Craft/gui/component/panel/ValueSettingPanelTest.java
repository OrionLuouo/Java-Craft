package OrionLuouo.test.Craft.gui.component.panel;

import OrionLuouo.Craft.gui.component.panel.IntegerValueSettingPanel;
import OrionLuouo.Craft.gui.component.window.Window;

import java.awt.*;

public class ValueSettingPanelTest {
    public static IntegerValueSettingPanel panel;

    public static void main(String[] args) {
        Window window = new Window();
        window.setExitOnClose();
        EventQueue.invokeLater(() -> {
            panel = new IntegerValueSettingPanel("qwq", 0, value -> {
                EventQueue.invokeLater(() -> {
                    //panel.setValue(0);
                });
                System.out.println(value);
            } , false);
            panel.setLocation(0, 0);
            window.add(panel);
        });
    }
}
