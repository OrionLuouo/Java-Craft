package OrionLuouo.Craft.data.math;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
public class BigPrime {
    public static final BigInteger ZERO = new BigInteger("0")
            ,ONE = new BigInteger("1")
            ,TWO = new BigInteger("2");

    /**
     * 指定数位的随机奇数。
     *
     * @param digitNumber 十进制位数。
     */
    public static BigInteger randomOddBigInteger(int digitNumber) {
        Random random = new Random();
        StringBuilder string = new StringBuilder();
        string.append(random.nextInt(8) + 1);
        digitNumber -= 2;
        for (int i = 0; i < digitNumber; i++)
            string.append(random.nextInt(9));
        int n = random.nextInt(8);
        //string.append(((random.nextInt(5) + 1) << 1) - 1);
        string.append((n & 1) == 1 ? n : n + 1);
        return new BigInteger(string.toString());
    }

    /**
     * 幂取模。
     *
     * @param base    幂的底数。
     * @param time    幂的指数。
     * @param divisor 除数。
     */
    public static BigInteger modularExponentiation(BigInteger base, BigInteger time, BigInteger divisor) {
        BigInteger result = ONE;
        byte[] bytes = time.toByteArray();
        for (byte aByte : bytes) {
            int ruler = 0b1000_0000;
            for (int bi = 0; bi < 8; bi++) {
                result = result.multiply(result).mod(divisor);
                if ((aByte & ruler) != 0)
                    result = result.multiply(base).mod(divisor);
                ruler >>= 1;
            }
        }
        return result;
    }

    /**
     * 随机大素数。
     *
     * @param digitNumber 十进制位数。
     */
    public static BigInteger randomBigPrimeNumber(int digitNumber) {
        return randomBigPrimeNumber(digitNumber, 1);
    }

    /**
     * @param threadNumber 计算使用到的线程数。
     */
    public static BigInteger randomBigPrimeNumber(int digitNumber, int threadNumber) {
        Thread[] threads = new Thread[--threadNumber];
        AtomicReference<BigInteger> result = new AtomicReference<>(null);
        for (int i = 0; i < threadNumber; i++) {
            threads[i] = new Thread(() -> {
                for (; ; ) {
                    BigInteger integer = randomOddBigInteger(digitNumber);
                    if (modularExponentiation(TWO, integer.subtract(ONE), integer).equals(ONE)) {
                        synchronized (result) {
                            result.set(integer);
                            return;
                        }
                    }
                    if (result.get() != null)
                        return;
                }
            });
            threads[i].start();
        }
        for (; ; ) {
            BigInteger integer = randomOddBigInteger(digitNumber);
            if (modularExponentiation(TWO, integer.subtract(ONE), integer).mod(integer).equals(ONE)) {
                synchronized (result) {
                    result.set(integer);
                }
            }
            if (result.get() != null)
                return result.get();
        }
    }
}