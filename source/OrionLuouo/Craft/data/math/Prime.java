package OrionLuouo.Craft.data.math;

import java.util.*;

public class Prime {
    /**
     * Some tested data:
     * CPU : Core i3 10100F
     * RAM : DDR4 8GB x2 , 2666MHZ
     * Time = 1{
     * 0 ~ 10,000 : 16 ms
     * 0 ~ 100,000 : 450 ms
     * }
     * Time = 10 {
     * 0 ~ 10,000 : 5 ms
     * 0 ~ 100,000 : 300 ms
     * 0 ~ 500,000 : 16,000 ms
     * }
     * Time = 100 {
     * 0 ~ 10,000 : 2 ms
     * 0 ~ 100,000 : 150 ms
     * }
     *
     * @param range
     */
    public static List<Integer> getPrimes_divide(int range) {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(2);
        int n = 3;
        lp:
        while (n < range) {
            Iterator<Integer> iterator = list.iterator();
            while (iterator.hasNext())
                if (n % iterator.next() == 0) {
                    n += 2;
                    continue lp;
                }
            list.add(n);
            n += 2;
        }
        return list;
    }

    /**
     * Some tested data:
     * CPU : Core i3 10100F
     * RAM : DDR4 8GB x2 , 2666MHZ
     * <p>
     * Time = 1{
     * 0 ~ 10,000 : 6 ms
     * 0 ~ 100,000 : 35 ms
     * 0 ~ 500,000 : 170 ms
     * }
     * Time = 10 {
     * 0 ~ 10,000 : 2 ms
     * 0 ~ 100,000 : 15 ms
     * 0 ~ 500,000 : 130 ms
     * }
     * Time = 100 {
     * 0 ~ 10,000 : 0.8 ms
     * 0 ~ 100,000 : 12 ms
     * 0 ~ 500,000 : 100 ms
     * }
     *
     * @param range
     */
    public static List<Integer> getPrimes_ESieve(int range) {
        Set<Integer> set = new HashSet<>();
        List<Integer> primes = new LinkedList<>();
        primes.add(2);
        for (int i = 3; i <= range; i += 2) {
            if (set.contains(i))
                continue;
            primes.add(i);
            int n = i << 1;
            while (n <= range)
                set.add(n += i);
        }
        return primes;
    }

    public static boolean isPrime(int num) {
        int end = (int) Math.ceil(Math.pow(num, 0.5));
        for (int i = 2; i <= end; i++)
            if (num % i == 0)
                return false;
        return true;
    }

}
