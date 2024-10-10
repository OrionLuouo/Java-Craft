package OrionLuouo.test.Craft.data;

public class StringPerformance_Test {
    public static final String CONTEXT = "ababaqwq"
            , PROCESS_DESCRIPTION = "To sum up the characters in the context.";
    public static final int TEST_ROUND = 1048576;

    record ResultPack(long nanoTime , int testRound , String context , String processDescription) {

    }

    public static void main(String[] args) {
        printResult(testCharAt(CONTEXT) , "charAt()");
        printResult(testForEach(CONTEXT) , "forEach()");
        printResult(testOfInt(CONTEXT) , "OfInt");
    }

    static void printResult(ResultPack resultPack , String source) {
        System.out.println(">test of \"" + source + "\" costs " + resultPack.nanoTime / 1_000_000 + " ms, " + resultPack.nanoTime / resultPack.testRound + " ns per operation. ");
        System.out.println(">\ttest rount = " + resultPack.testRound + ", context = \"" + resultPack.context + "\"");
        System.out.println(">\tprocess : " + resultPack.processDescription);
    }

    static ResultPack testCharAt(CharSequence string) {
        long sum = 0 , timestamp = System.nanoTime();
        for (int index = 0 , length = string.length() ; index < TEST_ROUND ; index++) {
            for (int stringIndex = 0 ; stringIndex < length ; ) {
                sum += string.charAt(stringIndex++);
            }
        }
        return new ResultPack(System.nanoTime() - timestamp , TEST_ROUND , string.toString() , PROCESS_DESCRIPTION);
    }

    static ResultPack testForEach(CharSequence string) {
        final long[] sum = {0};
        long timestamp = System.nanoTime();
        for (int index = 0 , length = string.length() ; index < TEST_ROUND ; index++) {
            string.chars().forEach(character -> {
                sum[0] += character;
            });
        }
        return new ResultPack(System.nanoTime() - timestamp , TEST_ROUND , string.toString() , PROCESS_DESCRIPTION);
    }

    static ResultPack testOfInt(CharSequence string) {
        long sum = 0 , timestamp = System.nanoTime();
        for (int index = 0 , length = string.length() ; index < TEST_ROUND ; index++) {
            var iterator = string.chars().iterator();
            while (iterator.hasNext()) {
                sum += iterator.next();
            }
        }
        return new ResultPack(System.nanoTime() - timestamp , TEST_ROUND , string.toString() , PROCESS_DESCRIPTION);
    }
}
