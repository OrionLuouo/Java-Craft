package OrionLuouo.test.Craft.system.annotation;

import OrionLuouo.Craft.system.annotation.Unfinished;

public class UnfinishedTest {
    public static void main(String[] args) {
        Unfinished.Test.testClasses("OrionLuouo");
        System.out.println("================================================================");
        Unfinished.Test.testMember("OrionLuouo.Craft" , true);
    }
}
