package OrionLuouo.test.Craft.system.thread;

import OrionLuouo.Craft.system.thread.frameRender.FPSCounter;
import OrionLuouo.Craft.system.thread.frameRender.ClockFrameRender;

public class ClockFrameRenderTest {
    public static void main(String[] args) {
        FPSCounter fpsCounter = new FPSCounter();
        fpsCounter.setRender(() -> {
            System.out.println("FPS : " + fpsCounter.getCurrentFPS());
        });
        ClockFrameRender render = new ClockFrameRender(1000000 , fpsCounter);
        render.start();
    }
}
