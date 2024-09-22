package OrionLuouo.Craft.gui.component.window;

import OrionLuouo.Craft.gui.component.panel.ListPanel;
import OrionLuouo.Craft.gui.graphics.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;

public class FileWindow extends Window {
    File path;
    ListPanel listPanel;
    JPanel pathFrame;
    JTextField pathLabel;
    JButton backButton, enterButton;
    File focusedFile;
    ListPanel.Item focusedItem;
    JTextField selectFileText;
    JButton selectButton;
    JPanel bottomFrame;

    public FileWindow() {
        super();
    }

    public FileWindow(File path) {
        super();
        setPath(path);
    }

    protected void initialize() {
        listPanel = new ListPanel();
        panel.setBackground(Colors.GRAY);
        pathFrame = new JPanel();
        pathFrame.setLayout(null);
        pathFrame.setBackground(Colors.GRAY_DARK);
        pathLabel = new JTextField();
        pathLabel.setForeground(Colors.GRAY_LIGHT);
        pathLabel.setBackground(Colors.GRAY_DARK);
        Font font = pathLabel.getFont();
        font = new Font(font.getFontName(), font.getStyle(), 14);
        pathLabel.setFont(font);
        backButton = new JButton("back");
        backButton.addActionListener(e -> {
            if (path == null)
                return;
            String path = this.path.getAbsolutePath();
            if (path.equals("A:\\document"))
                System.out.println("abab");
            setPath(new File(path.substring(0, path.lastIndexOf("\\"))));
        });
        backButton.setBackground(Colors.GRAY_DEEP);
        enterButton = new JButton("enter");
        enterButton.setBackground(Colors.GRAY_DEEP);
        enterButton.addActionListener(e -> {
            setPath(new File(pathLabel.getText()));
        });
        selectFileText = new JTextField();
        selectFileText.setBackground(Colors.GRAY_DARK);
        selectFileText.setFont(font);
        bottomFrame = new JPanel();
        bottomFrame.setBackground(Colors.GRAY_DARK);
        selectButton = new JButton("select");
        selectButton.setBackground(Colors.GRAY_DEEP);
        selectButton.addActionListener(e -> {
            try {
                selected(new File(selectFileText.getText()));
            } catch (Exception ignored) {
            }
        });
        resize();
        panel.add(bottomFrame);
        panel.add(listPanel.getPanel());
        panel.add(pathFrame);
        bottomFrame.add(selectFileText);
        bottomFrame.add(selectButton);
        pathFrame.add(pathLabel);
        pathFrame.add(backButton);
        pathFrame.add(enterButton);
        repaint();
    }

    public void setPath(File path) {
        if (path == null || !path.exists() || path.isFile())
            return;
        try {
            this.path = path;
            File[] files = path.listFiles();
            listPanel.clear();
            for (File file : files)
                listPanel.add(new ListPanel.Item((file.isFile() ? " file :  " : "  dir :  ") + file.getName()) {
                    public void doubleClicked(MouseEvent e) {
                        setPath(file);
                    }

                    public void exited(MouseEvent e) {
                        if (focusedFile != file)
                            super.exited(e);
                    }

                    public void clicked(MouseEvent e) {
                        if (focusedFile == file && focusedFile != null) {
                            focusedItem.label.setBackground(Colors.GRAY_DEEP);
                            focusedFile = null;
                            focusedItem = null;
                            selectFileText.setText("");
                        } else {
                            if (focusedFile != null)
                                focusedItem.label.setBackground(focusedItem.label.backgroundColor);
                            focusedFile = file;
                            focusedItem = this;
                            setBackground(Colors.GRAY_DARK);
                            selectFileText.setText(file.getAbsolutePath());
                        }
                    }

                    public void entered(MouseEvent e) {
                        if (focusedItem != this)
                            super.entered(e);
                    }
                });
            pathLabel.setText(path.getAbsolutePath());
        } catch (Exception e) {
        }
    }

    public void resize() {
        listPanel.setBounds(0, 30, panelWidth, panelHeight - 60);
        pathFrame.setBounds(0, 0, panelWidth, 29);
        pathLabel.setBounds(10, 2, panelWidth - 180, 26);
        backButton.setBounds(panelWidth - 160, 2, 70, 26);
        enterButton.setBounds(panelWidth - 80, 2, 70, 26);
        selectFileText.setBounds(10, 2, panelWidth - 180, 26);
        bottomFrame.setBounds(0, panelHeight - 30, panelWidth, 30);
        selectButton.setBounds(panelWidth - 160, 2, 70, 26);
    }

    /**
     * �����´����� select ��ťʱ�����ô˷�������ʵ�ּ�������
     *
     * @param file Ϊ��ǰѡ�е��ļ���
     *             ��δѡ���ļ�����Ϊ null��
     */
    public void selected(File file) {
    }
}
