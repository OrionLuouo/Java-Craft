package OrionLuouo.test.Craft.data.container;

import OrionLuouo.Craft.data.container.Pool;
import OrionLuouo.Craft.gui.component.text.TextArea;
import OrionLuouo.Craft.gui.component.text.TextPanel;
import OrionLuouo.Craft.gui.component.window.Window;
import OrionLuouo.Craft.system.thread.Executor;
import OrionLuouo.Craft.system.thread.ExecutorPool;

import javax.swing.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Pool_Test {
    static int counter = 0;
    static Object lock = new Object();

    public static void main(String[] args) {

        try {
            Lock lock1 = new ReentrantLock();
            lock1.lock();
            Condition condition = lock1.newCondition();
            //condition.await(60000 , TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            Window window = new Window();
            window.setDefaultCloseOperation(Window.EXIT_ON_CLOSE);
            TextArea text = new TextArea(500);
            StringBuilder builder = new StringBuilder();
            StackTraceElement[] elements = e.getStackTrace();
            for (StackTraceElement element : elements) {
                builder.append(element.toString() + "\n");
            }
            text.setText(builder.toString());
            text.setLocation(0 , 0);
            window.add(text);
            window.repaint();
            e.printStackTrace();
        }


        ExecutorPool pool = new ExecutorPool();


        for (int index = 0 ; index < 0 ; index++) {
            System.out.println(pool.availableObjects() + " " + pool.fullSize());
            final Executor executor = pool.get();
            int finalIndex = index;
            executor.execute(() -> {
                try {
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println('>' + finalIndex + " qwq!");
                pool.release(executor);
            });
        }

        new Thread(() -> {
            while (true) {
            //System.out.println(pool.availableObjects());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            }
        }).start();

        pool.batch(executor -> {
            executor.execute(() -> {
                synchronized (lock) {
                    System.out.println("hello executor " + counter++ + "! full size = " + pool.fullSize() + ", occupied objects = " + pool.occupiedObjects() + ", waiting consumers = " + pool.waitingConsumers());
                }
                pool.release(executor);
            });
        } , 1024);
        System.out.println("end");

    }
}
