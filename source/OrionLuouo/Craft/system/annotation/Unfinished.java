package OrionLuouo.Craft.system.annotation;

import OrionLuouo.Craft.system.reflect.Classes;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

@Retention(RetentionPolicy.RUNTIME)
public @interface Unfinished {
    String state() default "Unfinished";

    String information() default "";

    class Test {
        public static void testClasses(String packageName) {
            Set<Class<?>> classes = Classes.getAllClasses(packageName);
            int i = 0;
            for (Class<?> c : classes) {
                Unfinished Unfinished = c.getAnnotation(Unfinished.class);
                if (Unfinished == null) {
                    continue;
                }
                System.out.printf("%d. %s%n", i++, c.getName());
                System.out.println("\tstate = " + Unfinished.state());
                System.out.println("\tinformation = \"" + Unfinished.information() + '"');
            }
        }

        public static void testMethod(String packageName , boolean testPrivate) {
            Set<Class<?>> classes = Classes.getAllClasses(packageName);
            int i = 0;
            for (Class<?> c : classes) {
                Method[] methods = c.getDeclaredMethods();
                boolean empty = true;
                for (Method method : methods) {
                    if (method.getAnnotation(Unfinished.class) != null) {
                        empty = false;
                    }
                }
                if (empty) {
                    continue;
                }
                System.out.printf("%d. %s%n", i++, c.getName());
                for (Method method : methods) {
                    if (!Modifier.isPrivate(method.getModifiers()) || testPrivate) {
                        Unfinished Unfinished = method.getAnnotation(Unfinished.class);
                        if (Unfinished == null) {
                            continue;
                        }
                        System.out.println("\tMethod: " + method.getName() + ", state = " + Unfinished.state() + ", information = \"" + Unfinished.information() + '"');
                        break;
                    }
                }
            }
        }

        public static void testField(String packageName , boolean testPrivate) {
            Set<Class<?>> classes = Classes.getAllClasses(packageName);
            int i = 0;
            for (Class<?> c : classes) {
                Field[] fields = c.getDeclaredFields();
                boolean empty = true;
                for (Field field : fields) {
                    if (field.getAnnotation(Unfinished.class) != null) {
                        empty = false;
                    }
                }
                if (empty) {
                    continue;
                }
                System.out.printf("%d. %s%n", i++, c.getName());
                for (Field field : fields) {
                    if (!Modifier.isPrivate(field.getModifiers()) || testPrivate) {
                        Unfinished Unfinished = field.getAnnotation(Unfinished.class);
                        if (Unfinished == null) {
                            continue;
                        }
                        System.out.println("\tField: " + field.getName() + ", state = " + Unfinished.state() + ", information = \"" + Unfinished.information() + '"');
                        break;
                    }
                }
            }
        }

        public static void testMember(String packageName , boolean testPrivate) {
            Set<Class<?>> classes = Classes.getAllClasses(packageName);
            int i = 0;
            for (Class<?> c : classes) {
                Field[] fields = c.getDeclaredFields();
                boolean empty = true;
                for (Field method : fields) {
                    if (method.getAnnotation(Unfinished.class) != null) {
                        empty = false;
                    }
                }
                if (empty) {
                    Method[] methods = c.getDeclaredMethods();
                    for (Method method : methods) {
                        if(method.getAnnotation(Unfinished.class) != null) {
                            empty = false;
                        }
                    }
                    if (empty) {
                        continue;
                    }
                }
                System.out.printf("%d. %s%n", i++, c.getName());
                for (Field field : fields) {
                    if (!Modifier.isPrivate(field.getModifiers()) || testPrivate) {
                        Unfinished Unfinished = field.getAnnotation(Unfinished.class);
                        if (Unfinished == null) {
                            continue;
                        }
                        System.out.println("\tField: " + field.getName() + ", state = " + Unfinished.state() + ", information = \"" + Unfinished.information() + '"');
                        break;
                    }
                }
                Method[] methods = c.getDeclaredMethods();
                for (Method method : methods) {
                    if (!Modifier.isPrivate(method.getModifiers()) || testPrivate) {
                        Unfinished Unfinished = method.getAnnotation(Unfinished.class);
                        if (Unfinished == null) {
                            continue;
                        }
                        System.out.println("\tMethod: " + method.getName() + ", state = " + Unfinished.state() + ", information = \"" + Unfinished.information() + '"');
                        break;
                    }
                }
            }
        }

        public static void testMethod(Class<?> testClass , boolean testPrivate) {
            System.out.println(testClass.getName());
            Method[] methods = testClass.getDeclaredMethods();
            for (Method method : methods) {
                if (!Modifier.isPrivate(method.getModifiers()) || testPrivate) {
                    Unfinished Unfinished = method.getAnnotation(Unfinished.class);
                    if (Unfinished == null) {
                        continue;
                    }
                    System.out.println("\tMethod: " + method.getName() + ", state = " + Unfinished.state() + ", information = \"" + Unfinished.information() + '"');
                    break;
                }
            }
        }

        public static void testField(Class<?> testClass , boolean testPrivate) {
            System.out.println(testClass.getName());
            Field[] fields = testClass.getDeclaredFields();
            for (Field field : fields) {
                if (!Modifier.isPrivate(field.getModifiers()) || testPrivate) {
                    Unfinished Unfinished = field.getAnnotation(Unfinished.class);
                    if (Unfinished == null) {
                        continue;
                    }
                    System.out.println("\tField: " + field.getName() + ", state = " + Unfinished.state() + ", information = \"" + Unfinished.information() + '"');
                    break;
                }
            }
        }

        public static void testClass(Class<?> testClass , boolean testPrivate) {
            System.out.println(testClass.getName());
            Unfinished Unfinished = testClass.getAnnotation(Unfinished.class);
            if (Unfinished != null) {
                System.out.println("\tUnfinished: " + Unfinished.state() + ", information = \"" + Unfinished.information() + '"');
            }
            Field[] fields = testClass.getDeclaredFields();
            for (Field field : fields) {
                if (!Modifier.isPrivate(field.getModifiers()) || testPrivate) {
                    Unfinished = field.getAnnotation(Unfinished.class);
                    if (Unfinished == null) {
                        continue;
                    }
                    System.out.println("\tField: " + field.getName() + ", state = " + Unfinished.state() + ", information = \"" + Unfinished.information() + '"');
                    break;
                }
            }
            Method[] methods = testClass.getDeclaredMethods();
            for (Method method : methods) {
                if (!Modifier.isPrivate(method.getModifiers()) || testPrivate) {
                    Unfinished = method.getAnnotation(Unfinished.class);
                    System.out.println("\tMethod: " + method.getName() + ", state = " + Unfinished.state() + ", information = \"" + Unfinished.information() + '"');
                    break;
                }
            }
        }
    }
}