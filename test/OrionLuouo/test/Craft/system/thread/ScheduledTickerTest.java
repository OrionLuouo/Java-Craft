package OrionLuouo.test.Craft.system.thread;

import OrionLuouo.Craft.data.container.collection.sequence.ChunkChainList;
import OrionLuouo.Craft.data.container.collection.sequence.Queue;
import OrionLuouo.Craft.gui.component.text.TextArea;
import OrionLuouo.Craft.gui.component.window.Window;
import OrionLuouo.Craft.gui.graphics.Colors;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledTickerTest {
    static Window window = new Window();
    static TextArea display = new TextArea(window.getWidth() - 200);
    static ScheduledExecutorService service = new ScheduledThreadPoolExecutor(2);
    static ScheduledFuture<?> future;
    static Runnable render = new Runnable() {
        Queue<Long> postFrames = new ChunkChainList<>();
        int count = 0;

        @Override
        public void run() {
            long timestamp = System.nanoTime();
            while (postFrames.size() != 0) {
                if (postFrames.getFirst() + 1_000_000_000L < timestamp) {
                    postFrames.poll();
                }
            }
            postFrames.add(timestamp);
            display.setText(timestamp + " " + postFrames.size() + " fps");
            System.out.println("tick " + count++);
        }
    };
    static JPanel console = new JPanel() {
        {
            setBounds(window.getWidth() - 200 , 0 , 200 , window.getHeight());
            setOpaque(true);
            setLayout(null);
            setBackground(Colors.GRAY);
            JTextArea textArea = new JTextArea();
            textArea.setSize(150 , 50);
            textArea.setLocation(0 , 0);
            JLabel text = new JLabel("fps");
            text.setForeground(Colors.BLUE_LIGHT);
            text.setBounds(50 , 50 , 150 , 0);
            add(text);
            add(textArea);
            JButton button = new JButton();
            button.setBounds(0 , 60 , 100 , 50);
            button.setText("set fps");
            button.setOpaque(true);
            button.setBackground(Colors.GRAY_LIGHT);
            button.addActionListener(e -> {
                int fps = Integer.parseInt(textArea.getText());
                long tick = 1_000_000L / fps;
                future.cancel(false);
                future = service.scheduleAtFixedRate(render ,0,tick , TimeUnit.MICROSECONDS);
            });
            add(button);
        }
    };

    public static void main(String[] args) {
        initialize();
        future = service.scheduleAtFixedRate(render ,0, 1_000_000L / 60 , TimeUnit.MICROSECONDS);
    }

    static void initialize() {
        window.setExitOnClose();
        window.add(display);
        window.add(console);
        window.repaint();
    }
}
