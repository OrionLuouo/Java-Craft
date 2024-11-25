package OrionLuouo.test.Craft.data.container;

import OrionLuouo.Craft.data.container.Pool;
import OrionLuouo.Craft.system.thread.Executor;
import OrionLuouo.Craft.system.thread.ExecutorPool;

public class Pool_Test {
    static int counter = 0;
    static Object lock = new Object();

    public static void main(String[] args) {
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
