package OrionLuouo.Craft.gui.animation;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * �뱣֤ʹ����Щ����ǰ������Ĵ�С��λ��Ϊ���������մ�С��λ�á�
 * �����ڲ���ʹ�ö����̡߳�
 * ����ͬʱʹ���������������Ч����ʵ�ִӽ�����֣����������ʱ����Ч�����ѡ�
 * ���벻Ҫͬʱʹ��������ͻ�ķ������� appearFromLeft & appearFromRight ��
 */
public class ComponentAppearance {
    public static final int DEFAULT_SPEED_RATING = 25,
            DEFAULT_TICK_TIME = 15;
    protected static HashMap<Component, Location> locations = new HashMap<>();
    protected static ExecutorService pool = Executors.newCachedThreadPool();

    static Location getLocation(Component component) {
        Location location = locations.get(component);
        if (location == null)
            synchronized (locations) {
                locations.put(component, (location = new Location(component)));
            }
        location.ownerNumber++;
        return location;
    }

    static void tryRemoveLocation(Component component) {
        Location location = locations.get(component);
        if (location == null)
            return;
        if (--location.ownerNumber == 0)
            synchronized (locations) {
                locations.remove(component);
            }
    }

    private static void left(Component component, int speed, int tickTime, Location location) {
        pool.submit(() -> {
            component.setSize(0, component.getHeight());
            component.setVisible(true);
            while (component.getWidth() < location.width) {
                synchronized (component) {
                    component.setSize(component.getWidth() + speed, component.getHeight());
                }
                try {
                    Thread.sleep(tickTime);
                } catch (InterruptedException e) {
                }
            }
            component.setSize(location.width, component.getHeight());
            tryRemoveLocation(component);
        });
    }

    private static void right(Component component, int speed, int tickTime, Location location) {
        pool.submit(() -> {
            synchronized (component) {
                component.setBounds(component.getX() + component.getWidth(), component.getY(), 0, component.getHeight());
            }
            component.setVisible(true);
            while (component.getWidth() < location.width) {
                synchronized (component) {
                    component.setBounds(component.getX() - speed, component.getY(), component.getWidth() + speed, component.getHeight());
                }
                try {
                    Thread.sleep(tickTime);
                } catch (InterruptedException e) {
                }
            }
            component.setBounds(location.x, component.getY(), location.width, component.getHeight());
            tryRemoveLocation(component);
        });
    }

    private static void top(Component component, int speed, int tickTime, Location location) {
        pool.submit(() -> {
            component.setSize(component.getWidth(), 0);
            component.setVisible(true);
            while (component.getHeight() < location.height) {
                synchronized (component) {
                    component.setSize(component.getWidth(), component.getHeight() + speed);
                }
                try {
                    Thread.sleep(tickTime);
                } catch (InterruptedException e) {
                }
            }
            component.setSize(component.getWidth(), location.height);
            tryRemoveLocation(component);
        });
    }

    private static void bottom(Component component, int speed, int tickTime, Location location) {
        pool.submit(() -> {
            synchronized (component) {
                component.setBounds(component.getX(), component.getY() + component.getHeight(), component.getWidth(), 0);
            }
            component.setVisible(true);
            while (component.getHeight() < location.height) {
                synchronized (component) {
                    component.setBounds(component.getX(), component.getY() - speed, component.getWidth(), component.getHeight() + speed);
                }
                try {
                    Thread.sleep(tickTime);
                } catch (InterruptedException e) {
                }
            }
            component.setBounds(component.getX(), location.y, component.getWidth(), location.height);
            tryRemoveLocation(component);
        });
    }

    /**
     * ʹ��������չ����������ʾ��������ࣩ
     */
    public static void appearFromLeft(Component component) {
        Location location = getLocation(component);
        left(component, location.width / DEFAULT_SPEED_RATING, DEFAULT_TICK_TIME, location);
    }

    public static void appearFromLeft(Component component, int speed, int tickTime) {
        left(component, speed, tickTime, getLocation(component));
    }

    /**
     * ʹ������Ҳ�Ų����������ʾ��������ࣩ
     */
    public static void appearFromRight(Component component) {
        Location location = getLocation(component);
        right(component, location.width / DEFAULT_SPEED_RATING, DEFAULT_TICK_TIME, location);
    }

    public static void appearFromRight(Component component, int speed, int tickTime) {
        right(component, speed, tickTime, getLocation(component));
    }

    /**
     * ʹ������Ϸ�չ����������ʾ������ϰ벿�֣�
     */
    public static void appearFromTop(Component component) {
        Location location = getLocation(component);
        top(component, location.height / DEFAULT_SPEED_RATING, DEFAULT_TICK_TIME, location);
    }

    public static void appearFromTop(Component component, int speed, int tickTime) {
        top(component, speed, tickTime, getLocation(component));
    }

    /**
     * ʹ������²�Ų����������ʾ������ϰ벿�֣�
     */
    public static void appearFromBottom(Component component) {
        Location location = getLocation(component);
        bottom(component, location.height / DEFAULT_SPEED_RATING, DEFAULT_TICK_TIME, location);
    }

    public static void appearFromBottom(Component component, int speed, int tickTime) {
        bottom(component, speed, tickTime, getLocation(component));
    }
}

class Location {
    int x, y, width, height;
    int ownerNumber;

    public Location(Component component) {
        x = component.getX();
        y = component.getY();
        width = component.getWidth();
        height = component.getHeight();
        ownerNumber = 0;
    }
}