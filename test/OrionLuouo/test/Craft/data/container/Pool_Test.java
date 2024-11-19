package OrionLuouo.test.Craft.data.container;

import OrionLuouo.Craft.data.container.Pool;

public class Pool_Test {
    static class Executor extends Thread {
        Runnable task;
        boolean destructed;

        Executor() {
            super();
            start();
        }

        public void assign(Runnable task) {
            synchronized (this) {
                this.task = task;
                this.notify();
            }
        }

        @Override
        public void run() {
            while (true) {
                synchronized (this) {
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

    static class ThreadPool extends Pool<Executor> {
        @Override
        protected void destruct(Executor object) {
        }

        @Override
        protected Executor construct() {
            return new Executor();
        }
    }

    public static void main(String[] args) {
        ThreadPool pool = new ThreadPool();
        for (int index = 0 ; index < 1024 ; index++) {
            Executor executor = pool.get();
            int finalIndex = index;
            executor.assign(() -> {
                System.out.println(finalIndex + " qwq!");
            });
        }
    }
}
