package OrionLuouo.Craft.system.thread;

public interface FrameExecutor extends Runnable {
    void suspend();
    void restart();
    void close();
}
