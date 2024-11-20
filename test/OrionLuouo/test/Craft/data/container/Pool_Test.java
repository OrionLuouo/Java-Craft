package OrionLuouo.test.Craft.data.container;

import OrionLuouo.Craft.data.container.Pool;

public class Pool_Test {
    static class Executor extends Thread {
        Runnable task;
        boolean destructed;
        int index;

        static int counter = 0;

        Executor() {
            super();
            start();
            index = counter++;
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

    static class ThreadPool extends Pool<Executor> {
        @Override
        protected void destruct(Executor object) {
            object.destruct();
        }

        @Override
        protected Executor construct() {
            return new Executor();
        }
    }

    public static void main(String[] args) {
        ThreadPool pool = new ThreadPool();
        for (int index = 0 ; index < 1024 ; index++) {
            System.out.println(pool.availableObjects() + " " + pool.fullSize());
            final Executor executor = pool.get();
            int finalIndex = index;
            executor.assign(() -> {
                try {
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println('>' + finalIndex + " qwq!");
                pool.release(executor);
            });
        }
    }
}
