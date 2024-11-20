package OrionLuouo.Craft.system.thread;

public class Executor extends Thread {
    Runnable task;
    boolean destructed;
    int index;

    Executor() {
        super();
        start();
    }

    public void execute(Runnable task) {
        synchronized (this) {
            this.task = task;
            this.notify();
        }
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                if (task != null) {
                    task.run();
                }
                task = null;
                try {
                    this.wait();
                } catch (InterruptedException _) {
                    if (destructed) {
                        return;
                    }
                }
                if (task != null) {
                    task.run();
                }
                task = null;
            }
        }
    }

    public void destruct() {
        synchronized (this) {
            this.destructed = true;
            this.notify();
        }
    }
}