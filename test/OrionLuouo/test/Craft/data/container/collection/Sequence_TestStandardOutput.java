package OrionLuouo.test.Craft.data.container.collection;

import OrionLuouo.Craft.data.container.collection.sequence.List;

import java.lang.reflect.Array;

public class Sequence_TestStandardOutput<E> {
    int size , addCount , removeCount , getCount , insertCount , toArrayCount;
    Class<?> testClass;
    long controlAddTime , controlRemoveTime , controlGetTime , controlInsertTime , controlToArrayTime ,
            trialAddTime , trialRemoveTime , trialGetTime , trialInsertTime , trialToArrayTime;
    Class<?> controlType;

    public Sequence_TestStandardOutput(Class<?> testClass , int size , int addCount , int removeCount , int getCount , int insertCount , int toArrayCount , java.util.List<E> controlGroup , List<E> trialGroup , E element) {
        this.testClass = testClass;
        this.controlRemoveTime = controlAddTime;
        this.size = size;
        this.addCount = addCount;
        this.removeCount = removeCount;
        this.getCount = getCount;
        this.insertCount = insertCount;
        this.toArrayCount = toArrayCount;
        standardOutput();
        test(controlGroup , trialGroup , element);
        result();
    }

    void processTestingData() {
        long time , forTime;
        time = System.nanoTime();
        for (int index = 0 ; index < addCount ; index++) {}
        forTime = System.nanoTime() - time;
        this.controlAddTime = controlAddTime - forTime;
        this.trialAddTime = trialAddTime - forTime;
        time = System.nanoTime();
        for (int index = 0 ; index < removeCount ; index++) {}
        forTime = System.nanoTime() - time;
        this.controlRemoveTime = controlRemoveTime - forTime;
        this.trialRemoveTime = trialRemoveTime - forTime;
        time = System.nanoTime();
        for (int index = 0 ; index < getCount ; index++) {}
        forTime = System.nanoTime() - time;
        this.controlGetTime = controlGetTime - forTime;
        this.trialGetTime = trialGetTime - forTime;
        time = System.nanoTime();
        for (int index = 0 ; index < insertCount ; index++) {}
        forTime = System.nanoTime() - time;
        this.controlInsertTime = controlInsertTime - forTime;
        this.trialInsertTime = trialInsertTime - forTime;
    }

    void standardOutput() {
        System.out.println("------------------------------------------------------------");
        System.out.println("OrionLuouo test : OrionLuouo Craft Library");
        System.out.println("Test class : " + testClass);
        System.out.println("Test standard :");
        System.out.println("Basic sequence size : " + size);
        System.out.println("Add elements by " + addCount + " times.");
        System.out.println("Remove elements at midpoint by " + removeCount + " times.");
        System.out.println("Get elements at midpoint by " + getCount + " times.");
        System.out.println("Insert elements at midpoint by " + insertCount + " times.");
        System.out.println("Convert to array by " + toArrayCount + " times.");
        System.out.println("------------------------------------------------------------");
    }

    void result() {
        System.out.println("All test finished.");
        System.out.println("Results as following :");
        System.out.println("------------------------------------------------------------");
        System.out.println("Control group (" + controlType.getName() + ") :");
        printSingleResult(controlAddTime , controlRemoveTime , controlGetTime , controlInsertTime , controlToArrayTime);
        System.out.println("------------------------------------------------------------");
        System.out.println("Trial group (OrionLuouo Craft Library -" + testClass + " ) :");
        printSingleResult(trialAddTime , trialRemoveTime , trialGetTime , trialInsertTime , trialToArrayTime);
        System.out.println("------------------------------------------------------------");
        compare();
    }

    void compare() {
        System.out.println("Numerical comparison :");
        System.out.println("Adding elements by " + removeCount + " times :");
        System.out.println(">>> " + (controlAddTime > trialAddTime ? "(better)" : "(worse)") + " time gap = " + (controlAddTime - trialAddTime) / 1_000_000 + " ms , " + (controlAddTime - trialAddTime) / addCount + " ns (" + (controlAddTime - trialAddTime) / addCount / 1000 + " us) for each.");
        System.out.println("Removing elements aat midpoint by " + removeCount + " times :");
        System.out.println(">>> " + (controlRemoveTime > trialRemoveTime ? "(better)" : "(worse)") + " time gap = " + (controlRemoveTime - trialRemoveTime) / 1_000_000 + " ms , " + (controlRemoveTime - trialRemoveTime) / removeCount + " ns (" + (controlRemoveTime - trialRemoveTime) / removeCount / 1000 + " us) for each.");
        System.out.println("Getting elements at midpoint by " + getCount + " times :");
        System.out.println(">>> " + (controlGetTime > trialGetTime ? "(better)" : "(worse)") + " time gap = " + (controlGetTime - trialGetTime) / 1_000_000 + " ms , " + (controlGetTime - trialGetTime) / getCount + " ns (" + (controlGetTime - trialGetTime) / getCount / 1000 + " us) for each.");
        System.out.println("Inserting elements at midpoint by " + insertCount + " times :");
        System.out.println(">>> " + (controlInsertTime > trialInsertTime ? "(better)" : "(worse)") + " time gap = " + (controlInsertTime - trialInsertTime) / 1_000_000 + " ms , " + (controlInsertTime - trialInsertTime) / insertCount + " ns (" + (controlInsertTime - trialInsertTime) / insertCount / 1000 + " us) for each.");
        System.out.println("Converting to array by " + toArrayCount + " times :");
        System.out.println(">>> " + (controlToArrayTime > trialToArrayTime ? "(better)" : "(worse)") + " time gap = " + (controlToArrayTime - trialToArrayTime) / 1_000_000 + " ms , " + (controlToArrayTime - trialToArrayTime) / toArrayCount + " ns (" + (controlToArrayTime - trialToArrayTime) / toArrayCount / 1000 + " us) for each.");
    }

    void printSingleResult(long addTime , long removeTime , long getTime , long insertTime , long toArrayTime) {
        System.out.println("Adding elements by " + removeCount + " times costs :");
        System.out.println(">>> " + addTime / 1_000_000 + " ms, " + addTime / addCount + " ns (" + addTime / addCount / 1000 + " us) for each");
        System.out.println("Removing elements aat midpoint by " + removeCount + " times costs :");
        System.out.println(">>> " + removeTime / 1_000_000 + " ms, " + removeTime / removeCount + " ns (" + removeTime / removeCount / 1000 + " us) for each");
        System.out.println("Getting elements at midpoint by " + getCount + " times costs :");
        System.out.println(">>> " + getTime / 1_000_000 + " ms, " + getTime / getCount + " ns (" + getTime / getCount / 1000 + " us) for each");
        System.out.println("Inserting elements at midpoint by " + insertCount + " times costs :");
        System.out.println(">>> " + insertTime / 1_000_000 + " ms, " + insertTime / insertCount + " ns (" + insertTime / insertCount / 1000 + " us) for each");
        System.out.println("Converting to array by " + toArrayCount + " times costs :");
        System.out.println(">>> " + toArrayTime / 1_000_000 + " ms, " + toArrayTime / toArrayCount + " ns (" + toArrayTime / toArrayCount / 1000 + " us) for each");
    }

    public <E> void test(java.util.List<E> controlList , List<E> trialList , E element) {
        controlType = controlList.getClass();
        long time;
        int newSize = size + addCount - removeCount + insertCount;
        int mid = size >> 1;
        E[] array = (E[]) Array.newInstance(element.getClass() , newSize);
        for (int index = 0 ; index < size ; index++) {
            controlList.add(element);
        }
        time = System.nanoTime();
        System.out.println("Control group add testing...");
        for (int index = 0 ; index < addCount ; index++) {
            controlList.add(element);
        }
        controlAddTime = System.nanoTime() - time;
        time = System.nanoTime();
        System.out.println("Control group remove testing...");
        for (int index = 0 ; index < removeCount ; index++) {
            controlList.remove(mid);
        }
        controlRemoveTime = System.nanoTime() - time;
        time = System.nanoTime();
        System.out.println("Control group get testing...");
        for (int index = 0 ; index < getCount ; index++) {
            controlList.get(mid);
        }
        controlGetTime = System.nanoTime() - time;
        time = System.nanoTime();
        System.out.println("Control group insert testing...");
        for (int index = 0 ; index < insertCount ; index++) {
            controlList.add(mid , element);
        }
        controlInsertTime = System.nanoTime() - time;
        time = System.nanoTime();
        System.out.println("Control group toArray testing...");
        for (int index = 0 ; index < toArrayCount ; index++) {
            controlList.toArray(array);
        }
        System.out.println("Control group test finished.");
        controlToArrayTime = System.nanoTime() - time;
        for (int index = 0 ; index < size ; index++) {
            trialList.add(element);
        }
        time = System.nanoTime();
        System.out.println("Trial group add testing...");
        for (int index = 0 ; index < addCount ; index++) {
            trialList.add(element);
        }
        trialAddTime = System.nanoTime() - time;
        time = System.nanoTime();
        System.out.println("Trial group remove testing...");
        for (int index = 0 ; index < removeCount ; index++) {
            trialList.remove(mid);
        }
        trialRemoveTime = System.nanoTime() - time;
        time = System.nanoTime();
        System.out.println("Trial group get testing...");
        for (int index = 0 ; index < getCount ; index++) {
            trialList.get(mid);
        }
        trialGetTime = System.nanoTime() - time;
        time = System.nanoTime();
        System.out.println("Trial group insert testing...");
        for (int index = 0 ; index < insertCount ; index++) {
            trialList.insert(mid , element);
        }
        trialInsertTime = System.nanoTime() - time;
        time = System.nanoTime();
        System.out.println("Trial group toArray testing...");
        for (int index = 0 ; index < toArrayCount ; index++) {
            trialList.toArray(array);
        }
        trialToArrayTime = System.nanoTime() - time;
        System.out.println("Trial group test finished.");
    }
}
