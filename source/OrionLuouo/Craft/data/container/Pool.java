package OrionLuouo.Craft.data.container;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.data.Iterator;
import OrionLuouo.Craft.data.container.collection.sequence.ChunkChainList;
import OrionLuouo.Craft.data.container.collection.sequence.List;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public abstract class Pool<T> {
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
         * @param occupiedObjects The amount of occupied objects in the pool.
         *                        In the other words,
         *                        of the existed objects.
         *
         * @return If to expand.
         */
        boolean expand(int size , int occupiedObjects);
    }

    /**
     * The RigidExpander is as it says,
     * rigid and simple.
     * It'll hold a maximum limitation of the objects,
     * As long as the objects are less than which,
     * it will decide to expand.
     *
     * @param max
     */
    public record RigidExpander(int max) implements Expander {
        @Override
        public boolean expand(int size , int occupiedObjects) {
            return occupiedObjects < max;
        }
    }

    /**
     * The IndexExpander is an index calculator.
     * As the objects accumulate,
     * it will tend to keep the amount and not expand.
     * To the details,
     * it will power the base by occupiedObjects,
     * thus get a maximum limitation of the objects.
     * If the size is actually larger than max,
     * then expand.
     */
    public record IndexExpander(float base) implements Expander {
        @Override
        public boolean expand(int size , int occupiedObjects) {
            return size > Math.pow(base, occupiedObjects);
        }
    }

    /**
     * The PowerExpander is similar to IndexExpander,
     * but only to power the occupiedObjects by index.
     * Therefore,
     * the limitation will increase slower than the IndexExpander,
     * and the PowerExpander tends to expand more.
     */
    public record PowerExpander(float index) implements Expander {
        @Override
        public boolean expand(int size , int occupiedObjects) {
            return size > Math.pow(occupiedObjects , index);
        }
    }

    /**
     * Should be aware to that the Reducer is invoked when the object is timeout.
     * It means the object is to be destructed,
     * but the pool can still keep some in case of emergency.
     * So Reducer is just a security,
     * but not the real rule of whether the object should be destructed.
     * Once the Reducer decide that the object shouldn't be destructed,
     * its timer will be refreshed,
     * and timing for the next timeout.
     */
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

    /**
     * RigidReducer will hold a minimum amount of objects all the time.
     */
    public record RigidReducer(int minimum) implements Reducer {
        @Override
        public boolean reduce(int occupiedObjects , int availableObjects) {
            return (occupiedObjects + availableObjects) > minimum;
        }
    }

    /**
     * HalfAvailableReducer will obey a rule that keep half the amount of occupiedObjects,
     * which will guarantee there are enough object for consumer surge,
     * yet not stock too many objects.
     */
    public static class HalfAvailableReducer implements Reducer {
        @Override
        public boolean reduce(int occupiedObjects , int availableObjects) {
            return availableObjects > (occupiedObjects << 1);
        }
    }

    public static final Decider DECIDER_MAXIMUM = (size) -> 0
            , DECIDER_MINIMUM = (size) -> --size
            , DECIDER_MEDIUM = (size) -> size << 1;

    public static final long TIMEOUT_AGE_DEFAULT = 60_000;

    List<CouplePair<T , Long>> availablePool;
    Set<T> occupiedPool;
    long timeoutAge;
    Decider decider;
    Expander expander;
    Reducer reducer;
    Lock lock;
    Condition waitingCondition , monitorSleepingCondition;
    Thread timeoutMonitor;

    /**
     * To destruct an object.
     */
    protected abstract void destruct(T object);

    /**
     * To construct a new object.
     */
    protected abstract T construct();

    protected Pool() {
        availablePool = new ChunkChainList<>();
        occupiedPool = new HashSet<>();
        lock = new ReentrantLock();
        waitingCondition = lock.newCondition();
        monitorSleepingCondition = lock.newCondition();
        setTimeoutAge(TIMEOUT_AGE_DEFAULT);
        setDecider(DECIDER_MEDIUM);
        setExpander(new PowerExpander(1.55f));
        setReducer(new RigidReducer(5));
        (timeoutMonitor = new Thread(() -> {
            lock.lock();
            while (true) {
                while (availablePool.size() == 0) {
                    try {
                        monitorSleepingCondition.await();
                    } catch (InterruptedException _) {
                    }
                }
                do {
                    CouplePair<T, Long> pair = availablePool.getFirst();
                    if (pair.valueB() > System.currentTimeMillis()) {
                        try {
                            monitorSleepingCondition.await(pair.valueB() - System.currentTimeMillis() , TimeUnit.MILLISECONDS);
                        } catch (InterruptedException _) {
                        }
                        break;
                    }
                    if (reducer.reduce(occupiedPool.size() , availablePool.size())) {
                        availablePool.poll();
                        destruct(pair.valueA());
                    }
                    else {
                        break;
                    }
                } while (availablePool.size() != 0);
            }
        })).start();
    }

    public void setTimeoutAge(long milliseconds) {
        lock.lock();
        long gap = -this.timeoutAge + milliseconds;
        List<CouplePair<T , Long>> list = new ChunkChainList<>();
        Iterator<CouplePair<T , Long>> iterator = availablePool.iterator();
        while (iterator.hasNext()) {
            var cache = iterator.next();
            list.add(new CouplePair<>(cache.valueA() , cache.valueB() + gap));
        }
        availablePool = list;
        monitorSleepingCondition.signal();
        lock.unlock();
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
        lock.lock();
        T cache = availablePool.remove(decider.decide(availablePool.size())).valueA();
        occupiedPool.add(cache);
        lock.unlock();
        return cache;
    }

    /**
     * To release the occupation and return it to the pool.
     * Also,
     * this method can be used to add some external objects to the pool.
     * But that is usually only by initialization,
     * and you'd better do not do so in the routine of the pool,
     * for which may interrupt the monitor and cause chaos.
     */
    public void release(T object) {
        lock.lock();
        occupiedPool.remove(object);
        if (availablePool.size() == 0) {
            monitorSleepingCondition.signal();
        }
        availablePool.add(new CouplePair<>(object, System.currentTimeMillis() + timeoutAge));
        lock.unlock();
    }
}