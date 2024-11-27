package OrionLuouo.test.Craft.system.thread;

import OrionLuouo.Craft.data.container.collection.sequence.ChunkChainList;
import OrionLuouo.Craft.data.container.collection.sequence.List;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadSleepTest {
    public static final int LOOP_TIME = 10_000;
    public static final long WAIT_TIME = 1_000
            , TIMEOUT_AGE = WAIT_TIME << 1;

    public static void main(String[] args) {

        List<Long> timeouts = new ChunkChainList<>();
        AtomicLong timestamp = new AtomicLong();
        final long[] waitTime = { 0 };
        final long[] validWaitTime = { 0 };

        final Runnable render = new Runnable() {
            @Override
            public void run() {
                long cache = System.nanoTime();
                System.out.print(cache + " , gap : ");
                cache -= timestamp.get();
                waitTime[0] += cache;
                System.out.println(cache);
                if (cache > TIMEOUT_AGE) {
                    timeouts.add(cache);
                }
                else {
                    validWaitTime[0] += cache;
                }
                timestamp.set(System.nanoTime());
            }
        };
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(3);
        scheduledExecutorService.scheduleWithFixedDelay(render , WAIT_TIME , WAIT_TIME , TimeUnit.NANOSECONDS);

        /*
        for (int index = 0 ; index < LOOP_TIME ; index++) {
            render.run();
            try {
                //TimeUnit.NANOSECONDS.sleep(WAIT_TIME);
                Thread.sleep( 0 , (int) WAIT_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
         */

        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        scheduledExecutorService.shutdown();

        System.out.println("=====================================");
        AtomicLong timeoutAge = new AtomicLong();
        timeouts.iterate(time -> {
            System.out.println(time);
            timeoutAge.addAndGet(time);
        });
        System.out.println("=====================================");
        System.out.println("Wait time : " + WAIT_TIME);
        // System.out.println("Loop time : " + LOOP_TIME);
        System.out.println("timeout times : " + timeouts.size());
        System.out.println("Average timeout age : " + (timeoutAge.get() / timeouts.size()));
        System.out.println("Average wait time : " + waitTime[0] / LOOP_TIME);
        System.out.println("Average valid wait time : " + validWaitTime[0] / (LOOP_TIME - timeouts.size()));
    }
}