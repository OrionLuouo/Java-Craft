package OrionLuouo.Craft.system.thread.frameRender;

import java.util.LinkedList;

public class FPSCounter implements Runnable {
    Runnable render;
    LinkedList<Long> renderings;

    public FPSCounter(Runnable render) {
        this.render = render;
    }

    public FPSCounter() {
        this(() -> {
        });
        renderings = new LinkedList<>();
    }

    @Override
    public void run() {
        render.run();
        long timestamp = System.nanoTime() , timeoutTimestamp = timestamp - 1_000_000_000;
        while ((!renderings.isEmpty()) && (renderings.getFirst() < timeoutTimestamp)) {
            renderings.removeFirst();
        }
        renderings.add(System.nanoTime());
    }

    public int getCurrentFPS() {
        return renderings.size();
    }

    public int getFrameRate() {
        return renderings.size();
    }

    public void setRender(Runnable render) {
        this.render = render;
    }
}
