package OrionLuouo.test.Craft.data;

import OrionLuouo.Craft.data.StringUtil;

public class StringUtil_Test {
    public static final int TIME = 1048576;
    public static final String STRING = "   qwq \r\n";

    public static void main(String[] args) {
        System.out.println(">String Utils Performance In Trimming Test :");
        System.out.println(">Test Time = " + TIME);
        System.out.println(">String Example = \"" + STRING + "\"");
        System.out.println("--------------------------------");
        System.out.println(">String.replaceAll() :");
        long stamp = System.nanoTime();
        for (int index = 0 ; index < TIME ; index++) {
            STRING.replaceAll("[ \r\n\t]" , "");
        }
        stamp = System.nanoTime() - stamp;
        System.out.println(">Cost time : " + stamp / 1_000 + " us , = " + stamp / 1_000_000 + " ms . " + stamp / TIME + " ns for each.");

        System.out.println("--------------------------------");
        System.out.println(">String.trim() :");
        stamp = System.nanoTime();
        for (int index = 0 ; index < TIME ; index++) {
            STRING.trim();
        }
        stamp = System.nanoTime() - stamp;
        System.out.println(">Cost time : " + stamp / 1_000 + " us , = " + stamp / 1_000_000 + " ms . " + stamp / TIME + " ns for each.");

        System.out.println("--------------------------------");
        System.out.println(">StringUtil.peel() :");
        stamp = System.nanoTime();
        for (int index = 0 ; index < TIME ; index++) {
            StringUtil.peel(STRING);
        }
        stamp = System.nanoTime() - stamp;
        System.out.println(">Cost time : " + stamp / 1_000 + " us , = " + stamp / 1_000_000 + " ms . " + stamp / TIME + " ns for each.");
    }
}
