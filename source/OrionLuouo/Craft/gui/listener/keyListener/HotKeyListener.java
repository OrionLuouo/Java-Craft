package OrionLuouo.Craft.gui.listener.keyListener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This listener allows you to add some key sets,
 * and combine each of them with a.xml method.
 * If the hot keys have some overlapping,e.g. "A+B" and "A+B+Ctrl",
 * and the user is pressing "A","B" and "Ctrl",then just the later will be activated.
 * <p>
 * Usage example:
 * JLabel label=new JLabel();
 * HotKeyListener listener=new HotKeyListener();
 * listener.add(()->System.out.println("Hello,HotKeyListener!"),KeyEvent.VK_A);
 * label.addKeyListener(listener);
 */
public class HotKeyListener extends KeyAdapter {
    Map<String, Runnable> map = new HashMap<>();
    boolean pressingInvoke = true;
    ArrayList<Integer> list = new ArrayList<>();

    private String code(int[] keys) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int key : keys)
            stringBuilder.append(key).append(',');
        return stringBuilder.toString();
    }

    private int[] getPressingKeys() {
        int[] array = new int[list.size()];
        for (int i = 0; i < array.length; i++)
            array[i] = list.get(i);
        Arrays.sort(array);
        return array;
    }

    public HotKeyListener add(Runnable runnable, int... keys) {
        Arrays.sort(keys);
        map.put(code(keys), runnable);
        return this;
    }

    public HotKeyListener remove(int... keys) {
        Arrays.sort(keys);
        map.remove(code(keys));
        return this;
    }

    /**
     * If set to true,then when the user is pressing the hot key,it'll be
     */
    public void setPressingInvoke(boolean ifInvoke) {
        pressingInvoke = ifInvoke;
    }

    public void keyPressed(KeyEvent e) {
        if (!list.contains(e.getKeyCode())) {
            list.add(e.getKeyCode());
            if (pressingInvoke)
                run();
        }
    }

    public void keyReleased(KeyEvent e) {
        run();
        int index = list.indexOf(e.getKeyCode());
        if (index != -1)
            list.remove(index);
    }

    private void run() {
        Runnable runnable = map.get(code(getPressingKeys()));
        if (runnable != null)
            runnable.run();
    }
}