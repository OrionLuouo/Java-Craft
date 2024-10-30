package OrionLuouo.Craft.system.thread.frameRender;

import OrionLuouo.Craft.system.annotation.Unfinished;
import OrionLuouo.Craft.system.thread.FrameRender;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Unfinished
public class ClockFrameRender extends Thread implements FrameRender {
    Runnable render;
    int framePerSecond , frameNanosecond;
    boolean closed;
    Lock lock;
    Condition condition;
    boolean sleeping;
    long lastRenderingTimestamp;

    public ClockFrameRender(int framePerSecond , Runnable render) {
        super();
        this.render = render;
        setFramePerSecond(framePerSecond);
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    public ClockFrameRender(Runnable render) {
        this(60 , render);
    }

    public ClockFrameRender() {
        this(60 , () -> {});
    }

    @Override
    public void run() {
        while (true) {
            try {
                long timestamp = System.nanoTime() , gap = timestamp - lastRenderingTimestamp;
                int remainingNanosecond = (int) (frameNanosecond - gap);
                if (remainingNanosecond > 0) {
                    Thread.sleep(remainingNanosecond / 1_000_000 , remainingNanosecond % 1_000_000);
                }
                lastRenderingTimestamp = System.nanoTime();
                render.run();
            } catch (InterruptedException e) {
                lock.lock();
                sleeping = true;
                while (sleeping) {
                    try {
                        condition.await();
                    } catch (InterruptedException ex) {
                    }
                }
                if (closed) {
                    break;
                }
                lock.unlock();
            }
        }
    }

    public void wakeUp() {
        try {
            lock.lock();
            while (!sleeping) {
            }
            sleeping = false;
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void close() {
        if (!isInterrupted()) {
            interrupt();
        }
        closed = true;
        wakeUp();
    }

    /**
     * Better to interrupt the thread first,
     * and then set the render.
     */
    @Override
    public void setRender(Runnable render) {
        this.render = render;
    }

    /**
     * Better to interrupt the thread first,
     * and then set the fps.
     */
    @Override
    public void setFramePerSecond(int framePerSecond) {
        this.framePerSecond = framePerSecond;
        frameNanosecond = 1_000_000_000 / framePerSecond;
    }
}
