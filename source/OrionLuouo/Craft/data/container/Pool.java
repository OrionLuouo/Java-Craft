package OrionLuouo.Craft.data.container;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.data.container.collection.sequence.ChunkChainList;
import OrionLuouo.Craft.data.container.collection.sequence.List;
import OrionLuouo.Craft.gui.component.window.Window;

import javax.crypto.spec.PSource;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;


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
            , DECIDER_MEDIUM = (size) -> size >> 1;

    public static final long TIMEOUT_AGE_DEFAULT = 60_000;

    LinkedList<CouplePair<T , Long>> availablePool;
    //List<CouplePair<T , Long>> availablePool;
    Set<T> occupiedPool;
    long timeoutAge;
    Decider decider;
    Expander expander;
    Reducer reducer;
    Lock lock;
    Condition waitingCondition , monitorSleepingCondition;
    Thread timeoutMonitor;
    int waitingConsumers;

    /**
     * To destruct an object.
     */
    protected abstract void destruct(T object);

    /**
     * To construct a new object.
     */
    protected abstract T construct();

    protected Pool() {
        availablePool = new LinkedList<>();
        //availablePool = new ChunkChainList<>();
        occupiedPool = new HashSet<>();
        lock = new ReentrantLock();
        waitingCondition = lock.newCondition();
        monitorSleepingCondition = lock.newCondition();
        setTimeoutAge(TIMEOUT_AGE_DEFAULT);
        setDecider(DECIDER_MEDIUM);
        setExpander(new PowerExpander(1.55f));
        setReducer(new RigidReducer(5));
        timeoutMonitor = new Thread(() -> {
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
                    //System.out.println(pair);
                    if (pair.valueB() > System.currentTimeMillis()) {
                        //System.out.println("Monitor awaiting : " + (pair.valueB() - System.currentTimeMillis()));
                        try {
                            monitorSleepingCondition.await(pair.valueB() - System.currentTimeMillis() , TimeUnit.MILLISECONDS);
                        } catch (InterruptedException _) {
                        }
                        break;
                    }
                    //System.out.println("timeout age : " + pair.valueB() + ", now : " + System.currentTimeMillis());
                    if (reducer.reduce(occupiedPool.size() , availablePool.size())) {
                        availablePool.poll();
                        destruct(pair.valueA());
                        //System.out.println("Object destructed , timeout : " + (long) (System.currentTimeMillis() - pair.valueB()) + ", remaining : " + availablePool.size());
                    }
                    else {
                        try {
                            monitorSleepingCondition.await(timeoutAge , TimeUnit.MILLISECONDS);
                        } catch (InterruptedException _) {
                        }
                    }
                } while (availablePool.size() != 0);
            }
        });
        timeoutMonitor.setDaemon(true);
        timeoutMonitor.start();
    }

    public void setTimeoutAge(long milliseconds) {
        lock.lock();
        long gap = -this.timeoutAge + milliseconds;
        LinkedList<CouplePair<T , Long>> list = new LinkedList<>();
        Iterator<CouplePair<T , Long>> iterator = availablePool.iterator();
        while (iterator.hasNext()) {
            var cache = iterator.next();
            list.add(new CouplePair<>(cache.valueA() , cache.valueB() + gap));
        }
        availablePool = list;
        monitorSleepingCondition.signal();
        timeoutAge = milliseconds;
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

    /**
     * To get an object from the pool.
     * If there's no available object,
     * the pool will try to expand.
     * When expansion fails,
     * this method will be blocked,
     * till anyone releases an object to the pool,
     * and wakes up this thread.
     * So any object got from here must be released back.
     */
    public T get() {
        lock.lock();
        T cache = null;
        while (availablePool.size() == 0) {
            if (expander.expand(waitingConsumers + 1 + occupiedPool.size() , occupiedPool.size())) {
                cache = construct();
                occupiedPool.add(cache);
                lock.unlock();
                return cache;
            }
            else {
                waitingConsumers++;
                try {
                    waitingCondition.await();
                } catch (InterruptedException _) {
                }
            }
            if (availablePool.size() != 0) {
                waitingConsumers--;
                cache = availablePool.remove(decider.decide(availablePool.size())).valueA();
                occupiedPool.add(cache);
                if (availablePool.size() != 0 && waitingConsumers != 0) {
                    waitingCondition.signal();
                }
                lock.unlock();
                return cache;
            }
        }
        cache = availablePool.remove(decider.decide(availablePool.size())).valueA();
        occupiedPool.add(cache);
        if (availablePool.size() != 0 && waitingConsumers != 0) {
            waitingCondition.signal();
        }
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
            if (waitingConsumers != 0) {
                waitingCondition.signal();
                //System.out.println("signaled");
            }
        }
        availablePool.add(new CouplePair<>(object, System.currentTimeMillis() + timeoutAge));
        //System.out.println("released : " + availablePool.size() + " waiting : " + waitingConsumers + " time : " + System.currentTimeMillis() + " timeout age : " + System.currentTimeMillis() + timeoutAge);
        lock.unlock();
    }

    /**
     * This method will apply multiple subscribes to the pool.
     * One thing that different from invoke method:get by multiple times is that,
     * this method can cause expansion on larger scale,
     * for it put all the subscribes onto the pool at one time.
     * But if the available objects are still not enough,
     * it'll fall into waiting status,
     * too.
     * Also,
     * all objects got from here must be released back,
     *
     * @param consumer The handler of the objects.
     * @param count The amount of objects needed.
     */
    public void batch(Consumer<T> consumer , int count) {
        lock.lock();
        if (availablePool.size() < count) {
            count -= availablePool.size();
            availablePool.forEach(element -> {
                T object = element.valueA();
                occupiedPool.add(object);
                consumer.accept(object);
            });
            availablePool.clear();
            int size = occupiedPool.size() + count + waitingConsumers;
            while (expander.expand(size, occupiedPool.size())) {
                T object = construct();
                occupiedPool.add(object);
                consumer.accept(object);
                size--;
                if (--count == 0) {
                    while (waitingConsumers > 0) {
                        if (!expander.expand(size, occupiedPool.size())) {
                            break;
                        }
                        object = construct();
                        availablePool.add(new CouplePair<>(object , System.currentTimeMillis() + timeoutAge));
                        waitingCondition.signal();
                    }
                    lock.unlock();
                    return;
                }
            }
            waitingConsumers += count;
            //System.out.println("wait : " + waitingConsumers);
            do {
                try {
                    waitingCondition.await();
                } catch (InterruptedException _) {
                }
                //System.out.println("waked up : " + availablePool.size());
                if (availablePool.size() < count) {
                    count -= availablePool.size();
                    waitingConsumers -= availablePool.size();
                    availablePool.forEach(element -> {
                        T object = element.valueA();
                        occupiedPool.add(object);
                        consumer.accept(object);
                    });
                    availablePool.clear();
                }
                else {
                    waitingConsumers -= count;
                    while (count-- > 0) {
                        T element = availablePool.remove(decider.decide(availablePool.size())).valueA();
                        occupiedPool.add(element);
                        consumer.accept(element);
                    }
                    if (waitingConsumers != 0 && availablePool.size() != 0) {
                        waitingCondition.signal();
                    }
                }
                //System.out.println("wait loop : count = " + count + ", available ?= " + availablePool.size());
            } while (count > 0);
        }
        else {
            while (count-- > 0) {
                T element = availablePool.remove(decider.decide(availablePool.size())).valueA();
                occupiedPool.add(element);
                consumer.accept(element);
            }
        }
        lock.unlock();
    }

    public int availableObjects() {
        return availablePool.size();
    }

    public int fullSize() {
        return availablePool.size() + occupiedPool.size() + waitingConsumers;
    }

    public int waitingConsumers() {
        return waitingConsumers;
    }

    public int occupiedObjects() {
        return occupiedPool.size();
    }
}