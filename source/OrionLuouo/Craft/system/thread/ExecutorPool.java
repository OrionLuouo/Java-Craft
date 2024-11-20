package OrionLuouo.Craft.system.thread;


import OrionLuouo.Craft.data.container.Pool;

public class ExecutorPool extends Pool<Executor> {
    @Override
    protected void destruct(Executor object) {
        object.destruct();
    }

    @Override
    protected Executor construct() {
        return new Executor();
    }
}