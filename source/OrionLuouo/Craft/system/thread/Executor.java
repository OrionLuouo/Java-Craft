package OrionLuouo.Craft.system.thread;

public class Executor extends Thread {
    Runnable task;
    boolean destructed;
    int index;

    Executor() {
        super();
        setDaemon(true);
        start();
    }

    /**
     * The Executor is an asynchronous handler of tasks,
     * though,
     * it is single-thread.
     * If the former task is incomplete yet,
     * this method will be blocked,
     * till the task's completion and the Executor being free.
     */
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