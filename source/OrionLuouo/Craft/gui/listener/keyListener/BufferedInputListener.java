package OrionLuouo.Craft.gui.listener.keyListener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BufferedInputListener extends KeyAdapter {
    byte[] array;
    int head = 0, tail = 0, size;

    public BufferedInputListener() {
        size = 65536;
        array = new byte[size];
    }

    public BufferedInputListener(int size) {
        this.size = size;
        array = new byte[size];
    }

    public int next() {
        byte key = array[head];
        if (++head == size)
            head = 0;
        return key;
    }

    public int keysNumber() {
        int n = tail - head;
        if (n < 0)
            n += size;
        return n;
    }

    public void keyReleased(KeyEvent e) {
        array[tail++] = (byte) e.getKeyCode();
        if (tail == size)
            tail = 0;
    }
}
