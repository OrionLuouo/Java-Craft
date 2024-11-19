package OrionLuouo.Craft.data.container;

import OrionLuouo.Craft.data.container.collection.Set;
import OrionLuouo.Craft.data.container.collection.sequence.ChunkChainList;
import OrionLuouo.Craft.data.container.collection.sequence.List;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public abstract class Pool<T> {
    List<T> availablePool;
    Set<T> occupiedPool;
    long timeoutAge;
    Decider decider;
    Expander expander;
    Reducer reducer;
    Lock lock;
    Condition waitingCondition;
    Thread timeoutMonitor;

    public interface Decider {
        /**
         * To decide which object to use.
         *
         * @param size The size of the pool,
         *             meaning how many objects are available now.
         *
         * @return The index of the chosen object.
         *         The less it is,
         *         the former object being chosen,
         *         which means the more object will be kept in the pool;
         */
        int decide(int size);
    }

    public interface Expander {
        /**
         * To decide whether expand the pool or not.
         *
         * @param size The amount of objects in the pool.
         *             As Expander only comes to use when there is no available objects,
         *             so the size is actually the number of occupied objects plus the number of waiting object consumers.
         *
         * @return If to expand.
         */
        boolean expand(int size);
    }

    public interface Reducer {
        /**
         * To decide whether reduce the pool or not.
         * Invoked when there's a object time-out.
         *
         * @param occupiedObjects The amount of occupied objects in the pool.
         *
         * @param availableObjects The amount of objects available in the pool.
         *
         * @return If to reduce.
         */
        boolean reduce(int occupiedObjects , int availableObjects);
    }

    public static final Decider DECIDER_MAXIMUM = (size) -> 0
            , DECIDER_MINIMUM = (size) -> --size;

    protected abstract void destruct(T object);

    protected abstract T construct();

    Pool(String sqlURL, String sqlPassword, String sqlUser) {
        availablePool = new ChunkChainList<>();
        lock = new ReentrantLock();
        waitingCondition = lock.newCondition();
        (timeoutMonitor = new Thread() {
            @Override
            public void run() {

            }
        }).start();
    }

    public void setTimeoutAge(long milliseconds) {
        
    }

    public void setExpander(Expander expander) {
        this.expander = expander;
    }

    public void setReducer(Reducer reducer) {
        this.reducer = reducer;
    }

    public void setDecider(Decider decider) {
        this.decider = decider;
    }

    public T get() {
        return null;
    }
    
    public void release(T object) {
        
    }
}