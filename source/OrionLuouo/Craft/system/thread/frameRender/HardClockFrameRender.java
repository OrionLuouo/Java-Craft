package OrionLuouo.Craft.system.thread.frameRender;

import OrionLuouo.Craft.system.annotation.Unfinished;
import OrionLuouo.Craft.system.thread.FrameRender;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Unfinished
public class HardClockFrameRender extends Thread implements FrameRender {
    Runnable render;
    int frameMillisecond , frameNanosecond , framePerSecond;
    boolean closed;
    Lock lock;
    Condition condition;
    boolean sleeping;

    public HardClockFrameRender(int framePerSecond , Runnable render , int anticipatedFrameRenderingNanosecond) {
        super();
        this.render = render;
        setFramePerSecond(framePerSecond);
        addAnticipatedFrameRenderingNanosecond(anticipatedFrameRenderingNanosecond);
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    public HardClockFrameRender(Runnable render) {
        this(60 , render , 0);
    }

    public HardClockFrameRender() {
        this(60 , () -> {} , 0);
    }

    @Override
    public void run() {
        while (true) {
            try {
                render.run();
                Thread.sleep(frameMillisecond , frameNanosecond);
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
            } finally {
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
        int nanosecond = 1_000_000_000 / framePerSecond;
        frameNanosecond = nanosecond % 1_000_000;
        frameMillisecond = nanosecond / 1_000_000;
    }

    public void addAnticipatedFrameRenderingNanosecond(int anticipatedFrameRenderingNanosecond) {
        frameMillisecond -= anticipatedFrameRenderingNanosecond / 1_000_000;
        frameNanosecond -= anticipatedFrameRenderingNanosecond % 1_000_000;
    }
}
