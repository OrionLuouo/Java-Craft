package OrionLuouo.Craft.system.thread;

public interface FrameRender extends Runnable {
    void wakeUp();
    void close();
    void setRender(Runnable render);
    void setFramePerSecond(int framePerSecond);
}