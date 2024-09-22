package OrionLuouo.Craft.gui.animation;

import java.awt.*;

import static OrionLuouo.Craft.gui.animation.ComponentAppearance.*;

public class ComponentDisappearance {
    private static void left(Component component, int speed, int tickTime, Location location) {
        ComponentAppearance.pool.submit(() -> {
            while (component.getWidth() > 0) {
                synchronized (component) {
                    component.setSize(component.getWidth() - speed, component.getHeight());
                }
                try {
                    Thread.sleep(tickTime);
                } catch (InterruptedException e) {
                }
            }
            component.setVisible(false);
            component.setSize(location.width, location.height);
            ComponentAppearance.tryRemoveLocation(component);
        });
    }

    private static void right(Component component, int speed, int tickTime, Location location) {
        ComponentAppearance.pool.submit(() -> {
            while (component.getWidth() > 0) {
                synchronized (component) {
                    component.setBounds(component.getX() + speed, component.getY(), component.getWidth() - speed, component.getHeight());
                }
                try {
                    Thread.sleep(tickTime);
                } catch (InterruptedException e) {
                }
            }
            component.setVisible(false);
            component.setBounds(location.x, location.y, location.width, location.height);
            ComponentAppearance.tryRemoveLocation(component);
        });
    }

    private static void top(Component component, int speed, int tickTime, Location location) {
        ComponentAppearance.pool.submit(() -> {
            while (component.getHeight() > 0) {
                synchronized (component) {
                    component.setSize(component.getWidth(), component.getHeight() - speed);
                    try {
                        Thread.sleep(tickTime);
                    } catch (InterruptedException e) {
                    }
                }
            }
            component.setVisible(false);
            component.setSize(location.width, location.height);
            ComponentAppearance.tryRemoveLocation(component);
        });
    }

    private static void bottom(Component component, int speed, int tickTime, Location location) {
        ComponentAppearance.pool.submit(() -> {
            while (component.getHeight() > 0) {
                component.setBounds(component.getX(), component.getY() + speed, component.getWidth(), component.getHeight() - speed);
                try {
                    Thread.sleep(tickTime);
                } catch (InterruptedException e) {
                }
            }
            component.setVisible(false);
            component.setBounds(location.x, location.y, location.width, location.height);
            ComponentAppearance.tryRemoveLocation(component);
        });
    }

    public static void disappearToLeft(Component component) {
        Location location = ComponentAppearance.getLocation(component);
        left(component, location.width / ComponentAppearance.DEFAULT_SPEED_RATING, ComponentAppearance.DEFAULT_TICK_TIME, location);
    }

    public static void disappearToLeft(Component component, int speed, int tickTime) {
        left(component, speed, tickTime, ComponentAppearance.getLocation(component));
    }

    public static void disappearToRight(Component component) {
        Location location = ComponentAppearance.getLocation(component);
        right(component, location.width / ComponentAppearance.DEFAULT_SPEED_RATING, ComponentAppearance.DEFAULT_TICK_TIME, location);
    }

    public static void disappearToRight(Component component, int speed, int tickTime) {
        right(component, speed, tickTime, ComponentAppearance.getLocation(component));
    }

    public static void disappearFromTop(Component component) {
        Location location = ComponentAppearance.getLocation(component);
        top(component, location.height / ComponentAppearance.DEFAULT_SPEED_RATING, ComponentAppearance.DEFAULT_TICK_TIME, location);
    }

    public static void disappearToTop(Component component, int speed, int tickTime) {
        top(component, speed, tickTime, ComponentAppearance.getLocation(component));
    }

    public static void disappearToBottom(Component component) {
        Location location = ComponentAppearance.getLocation(component);
        bottom(component, location.height / ComponentAppearance.DEFAULT_SPEED_RATING, ComponentAppearance.DEFAULT_TICK_TIME, location);
    }

    public static void disappearToBottom(Component component, int speed, int tickTime) {
        bottom(component, speed, tickTime, ComponentAppearance.getLocation(component));
    }
}
