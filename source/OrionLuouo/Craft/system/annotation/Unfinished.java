package OrionLuouo.Craft.system.annotation;

import OrionLuouo.Craft.system.reflect.Classes;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

@Retention(RetentionPolicy.RUNTIME)
public @interface Unfinished {
    String state() default "Unfinished";

    String info() default "empty";

    class UnfinishedClassTest {
        public static void test(String packageName) {
            Set<Class<?>> classes = Classes.getAllClasses(packageName);
            int i = 0;
            for (Class<?> c : classes) {
                Annotation[] annotations = c.getAnnotations();
                for (Annotation annotation : annotations)
                    if (annotation instanceof Unfinished) {
                        System.out.printf("%d. %s%n", i++, c.getName());
                        System.out.println("  state  = " + ((Unfinished) annotation).state());
                        System.out.println("  info  = " + ((Unfinished) annotation).info());
                        break;
                    }
            }
        }
    }
    class UnfinishedMemberTest {

        public static void test(String packageName) {
            Set<Class<?>> classes = Classes.getAllClasses(packageName);
            int ci = 0;
            for (Class<?> c : classes) {
                Annotation[] annotations = c.getAnnotations();
                Annotation a = null;
                for (Annotation annotation : annotations)
                    if (annotation instanceof Unfinished) {
                        a = annotation;
                        break;
                    }
                if (a == null)
                    continue;
                System.out.printf("%d. %s%n", ci++, c.getName());
                System.out.println("  state = " + ((Unfinished) a).state() + " , info = " + ((Unfinished) a).info());
                Method[] methods = c.getDeclaredMethods();
                if (methods == null)
                    continue;
                testMethod(methods);
                Field[] fields = c.getDeclaredFields();
                if(fields == null)
                    continue;
                testFields(fields);
            }
        }

        public static void test(Class<?> c) {
            Annotation[] annotations = c.getAnnotations();
            Annotation a = null;
            for (Annotation annotation : annotations)
                if (annotation instanceof Unfinished) {
                    a = annotation;
                    break;
                }
            if (a == null)
                return;
            System.out.printf("%s%n", c.getName());
            System.out.println("  state = " + ((Unfinished) a).state() + " , info = " + ((Unfinished) a).info());
            Method[] methods = c.getDeclaredMethods();
            if (methods == null)
                return;
            testMethod(methods);
            Field[] fields = c.getDeclaredFields();
            if(fields == null)
                return;
            testFields(fields);
        }

        private static void testMethod(Method[] methods) {
            Annotation a = null;
            Annotation[] annotations;
            int mi = 0;
            for (Method m : methods) {
                annotations = m.getAnnotations();
                for (Annotation annotation : annotations)
                    if (annotation instanceof Unfinished) {
                        a = annotation;
                        break;
                    }
                if (a == null)
                    continue;
                Class<?>[] types = m.getParameterTypes();
                System.out.printf("    %d. %s(", mi, m.getName());
                for (int i = 0; i < types.length; i++) {
                    System.out.print((i == 0 ? "" : ',') + types[i].getSimpleName());
                }
                System.out.print(")");
                System.out.printf(" : state = %s , info = %s%n", ((Unfinished) a).state(), ((Unfinished) a).info());
                a = null;
            }
        }

        private static void testFields(Field[] fields){
            Annotation a = null;
            Annotation[] annotations;
            int fi = 0;
            for (Field field : fields) {
                annotations = field.getAnnotations();
                for (Annotation annotation : annotations)
                    if (annotation instanceof Unfinished) {
                        a = annotation;
                        break;
                    }
                if (a == null)
                    continue;
                System.out.printf("    %d.%s %s : state = %s , info = %s%n", fi++ , field.getType().getSimpleName() , field.getName() , ((Unfinished) a).state(), ((Unfinished) a).info());
                a = null;
            }
        }
    }

}