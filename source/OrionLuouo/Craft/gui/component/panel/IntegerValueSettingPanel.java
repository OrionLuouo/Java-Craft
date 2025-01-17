package OrionLuouo.Craft.gui.component.panel;

import OrionLuouo.Craft.data.Processor;
import OrionLuouo.Craft.data.StringUtil;
import OrionLuouo.Craft.gui.animation.PopInformation;
import OrionLuouo.Craft.gui.graphics.Colors;
import OrionLuouo.Craft.gui.listener.mouseListener.MenuListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class IntegerValueSettingPanel extends JPanel {
    public static final int HEIGHT = 30;

    JLabel label;
    JTextArea area;
    Color color , buttonColor , buttonFocusColor , buttonPressedColor;
    JLabel decrementButton, incrementButton;

    public IntegerValueSettingPanel(String setting , int defaultValue , Processor<Integer> settingProcessor , boolean button) {
        super();
        setOpaque(true);
        setLayout(null);
        label = new JLabel();
        area = new JTextArea();
        int width = StringUtil.getStringWidth(setting , label.getFont());
        label.setSize(width , HEIGHT);
        width += 25;
        label.setLocation(5 , 0);
        label.setText(setting);
        add(label);
        int componentWidth = StringUtil.getStringWidth(String.valueOf(defaultValue) , label.getFont()) + 60;
        area.setSize(componentWidth , HEIGHT - 10);
        area.setLocation(width , 5 );
        area.setText(String.valueOf(defaultValue));
        add(area);
        width += componentWidth + 5;
        if (button) {
            decrementButton = new JLabel();
            incrementButton = new JLabel();
            decrementButton.setOpaque(true);
            incrementButton.setOpaque(true);
            decrementButton.setHorizontalAlignment(SwingConstants.CENTER);
            incrementButton.setHorizontalAlignment(SwingConstants.CENTER);
            decrementButton.setBackground(Colors.GRAY);
            incrementButton.setBackground(Colors.GRAY);
            decrementButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    EventQueue.invokeLater(() -> {
                        e.getComponent().setBackground(buttonFocusColor);
                    });
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    EventQueue.invokeLater(() -> {
                        e.getComponent().setBackground(buttonFocusColor);
                        int value = Integer.parseInt(area.getText());
                        value--;
                        area.setText(String.valueOf(value));
                        settingProcessor.process(value);
                    });
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    EventQueue.invokeLater(() -> {
                        e.getComponent().setBackground(buttonColor);
                    });
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    EventQueue.invokeLater(() -> {
                        e.getComponent().setBackground(buttonPressedColor);
                    });
                }
            });
            incrementButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    EventQueue.invokeLater(() -> {
                        e.getComponent().setBackground(buttonFocusColor);
                    });
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    EventQueue.invokeLater(() -> {
                        e.getComponent().setBackground(buttonFocusColor);
                        int value = Integer.parseInt(area.getText());
                        value++;
                        area.setText(String.valueOf(value));
                        settingProcessor.process(value);
                    });
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    EventQueue.invokeLater(() -> {
                        e.getComponent().setBackground(buttonColor);
                    });
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    EventQueue.invokeLater(() -> {
                        e.getComponent().setBackground(buttonPressedColor);
                    });
                }
            });
            decrementButton.setText("-");
            decrementButton.setSize(40, HEIGHT - 4);
            decrementButton.setLocation(width, 2);
            add(decrementButton);
            width += 45;
            incrementButton.setText("+");
            incrementButton.setSize(40, HEIGHT - 4);
            incrementButton.setLocation(width, 2);
            add(incrementButton);
            width += 45;
        }
        setSize(width , HEIGHT);
        area.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                EventQueue.invokeLater(() -> {
                    if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                        e.consume();
                    }
                    else {
                        try {
                            settingProcessor.process(Integer.parseInt(area.getText()));
                        } catch (NumberFormatException _) {
                        }
                    }
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
        if (decrementButton == null) {
            return;
        }
        decrementButton.setBackground(buttonColor);
        incrementButton.setBackground(buttonColor);
    }

    public void setValue(int value) {
        area.setText(String.valueOf(value));
    }

    public void setInformation(String information , Container container) {
        final JPanel panel = this;
        EventQueue.invokeLater(() -> {
            MenuListener listener = new MenuListener();
            JLabel informationLabel = new JLabel(information);
            int cache;
            informationLabel.setSize(StringUtil.getStringWidth(information , label.getFont()) * 4 / 3 , label.getFontMetrics(label.getFont()).getHeight() * 5 / 3);
            informationLabel.setHorizontalAlignment(SwingConstants.CENTER);
            informationLabel.setOpaque(true);
            informationLabel.setBackground(buttonFocusColor);
            informationLabel.setVisible(false);
            informationLabel.addMouseListener(listener);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    EventQueue.invokeLater(() -> {
                        if (informationLabel.isVisible()) {
                            return;
                        }
                        int componentWidth = label.getWidth() , componentHeight = label.getHeight() , x = e.getX() , y = e.getY();
                        float rowRate = (float) x / componentWidth , columnRate = (float) y / componentHeight;
                        rowRate = rowRate < 0.5 ? 1 - rowRate : rowRate;
                        columnRate = columnRate < 0.5 ? 1 - columnRate : columnRate;
                        if (rowRate > columnRate) {
                            x = x < componentWidth >> 1 ? 0 : componentWidth;
                        }
                        else {
                            y = y < componentHeight >> 1 ? 0 : componentHeight;
                        }
                        x += label.getX() + panel.getX();
                        y += label.getY() + panel.getY();
                        informationLabel.setLocation(x , y);
                        informationLabel.setVisible(true);
                    });
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    EventQueue.invokeLater(() -> {
                        if (!listener.isMouseIn()) {
                            informationLabel.setVisible(false);
                        }
                    });
                }
            });
            container.add(informationLabel , 0);
        });
    }

    public int getValue() {
        return Integer.parseInt(area.getText());
    }
}
