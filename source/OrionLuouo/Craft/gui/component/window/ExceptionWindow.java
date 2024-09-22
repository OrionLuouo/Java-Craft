package OrionLuouo.Craft.gui.component.window;

import OrionLuouo.Craft.gui.component.text.TextPanel;
import OrionLuouo.Craft.gui.graphics.Colors;

public class ExceptionWindow extends Window {
    TextPanel p;

    public ExceptionWindow(String text, Exception e) {
        super(Window.SIZE_SMALL);
        setTitle("Exception occurred!");
        StackTraceElement[] elements = e.getStackTrace();
        StringBuilder builder = new StringBuilder(text + '\n' + "Detailed Exception information : \n==================================================================\n");
        int i = 0;
        for (StackTraceElement element : elements)
            builder.append(i++ + ". > " + element.toString() + '\n');
        p = new TextPanel();
        p.setEditable(false);
        p.setText(builder.toString());
        p.setSize(panelWidth, panelHeight);
        p.setBackground(Colors.GRAY_DEEP);
        add(p.getPanel());
        setResizable(false);
        setExitOnClose();
    }
}
