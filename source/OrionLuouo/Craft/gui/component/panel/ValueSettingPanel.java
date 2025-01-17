package OrionLuouo.Craft.gui.component.panel;

import OrionLuouo.Craft.data.Processor;
import OrionLuouo.Craft.data.StringUtil;
import OrionLuouo.Craft.gui.graphics.Colors;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ValueSettingPanel extends JPanel {
    public static final int HEIGHT = 30;

    JLabel label;
    JTextArea area;
    Color color , buttonColor , buttonFocusColor , buttonPressedColor;

    public ValueSettingPanel(String setting , String defaultValue , Processor<String> settingProcessor) {
        super();
        setOpaque(true);
        setLayout(null);
        label = new JLabel();
        area = new JTextArea();
        int width = StringUtil.getStringWidth(setting , label.getFont()) + 35;
        label.setSize(width , HEIGHT);
        label.setLocation(5 , 0);
        label.setText(setting);
        add(label);
        int componentWidth = StringUtil.getStringWidth(defaultValue , label.getFont()) + 60;
        area.setSize(componentWidth , HEIGHT - 10);
        area.setLocation(width , 5 );
        area.setText(defaultValue);
        add(area);
        width += componentWidth + 5;
        setSize(width , HEIGHT);
        area.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                EventQueue.invokeLater(() -> {
                    settingProcessor.process(area.getText());
                });
            }
        });
        setBackground(Colors.GRAY_LIGHT);
    }

    @Override
    public void setBackground(Color color) {
        super.setBackground(color);
        this.color = color;
        buttonColor = color.darker();
        buttonFocusColor = buttonColor.darker();
        buttonPressedColor = buttonFocusColor.darker();
    }

    public void setValue(String value) {
        area.setText(value);
    }
}
