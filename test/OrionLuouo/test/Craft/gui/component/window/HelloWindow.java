package OrionLuouo.test.Craft.gui.component.window;

import OrionLuouo.Craft.gui.component.window.Window;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HelloWindow {
    static Window window = new Window();
    static JLabel label = new JLabel();
    static ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    static Random random = new Random();

    public static void main(String[] args) {
        window.setExitOnClose();
        label.setText("Hello 宝宝窗口!");
        label.setBounds((window.getWidth() - 600) >> 1 , (window.getHeight() - 200) >> 1 , 600, 200);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 64));
        window.add(label);
        window.repaint();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            label.setForeground(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            label.setLocation(random.nextInt(window.getWidth() - 600), random.nextInt(window.getHeight() - 200));
        } , 0 , 16 , TimeUnit.MILLISECONDS);
    }
}
