package OrionLuouo.Craft.system.thread;

public class Ticker extends Thread {
    interface Timer {
        void waitTick() throws InterruptedException;
    }

    volatile Timer timer;
    volatile Runnable render;
    boolean suspended , continuable;

    public Ticker(Runnable render) {
        this.render = render;
    }

    @Override
    public void run() {
        tick:while (true) {
            try {
                timer.waitTick();
                render.run();
            } catch (InterruptedException _) {
                while (suspended) {
                    if (continuable) {
                        continue tick;
                    }
                }
                return;
            }
        }
    }

    public void pause() {
        suspended = true;
        continuable = false;
        interrupt();
    }

    public void restart() {
        suspended = false;
        continuable = true;
        interrupt();
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void setRender(Runnable render) {
        this.render = render;
    }
}
