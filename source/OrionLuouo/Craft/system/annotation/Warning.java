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
public @interface Warning {
    enum State {
        UNTESTED , HIDDEN_BUG
    }

    State state() default State.UNTESTED;

    String information() default "";

    class Test {
        public static void testClasses(String packageName) {
            Set<Class<?>> classes = Classes.getAllClasses(packageName);
            int i = 0;
            for (Class<?> c : classes) {
                Warning warning = c.getAnnotation(Warning.class);
                if (warning == null) {
                    continue;
                }
                System.out.printf("%d. %s%n", i++, c.getName());
                System.out.println("\tstate = " + warning.state());
                System.out.println("\tinformation = \"" + warning.information() + '"');
            }
        }

        public static void testMethod(String packageName , boolean testPrivate) {
            Set<Class<?>> classes = Classes.getAllClasses(packageName);
            int i = 0;
            for (Class<?> c : classes) {
                Method[] methods = c.getDeclaredMethods();
                boolean empty = true;
                for (Method method : methods) {
                    if (method.getAnnotation(Warning.class) != null) {
                        empty = false;
                    }
                }
                if (empty) {
                    continue;
                }
                System.out.printf("%d. %s%n", i++, c.getName());
                for (Method method : methods) {
                    if (!Modifier.isPrivate(method.getModifiers()) || testPrivate) {
                        Warning warning = method.getAnnotation(Warning.class);
                        if (warning == null) {
                            continue;
                        }
                        System.out.println("\tMethod: " + method.getName() + ", state = " + warning.state() + ", information = \"" + warning.information() + '"');
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
                    if (field.getAnnotation(Warning.class) != null) {
                        empty = false;
                    }
                }
                if (empty) {
                    continue;
                }
                System.out.printf("%d. %s%n", i++, c.getName());
                for (Field field : fields) {
                    if (!Modifier.isPrivate(field.getModifiers()) || testPrivate) {
                        Warning warning = field.getAnnotation(Warning.class);
                        if (warning == null) {
                            continue;
                        }
                        System.out.println("\tField: " + field.getName() + ", state = " + warning.state() + ", information = \"" + warning.information() + '"');
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
                    if (method.getAnnotation(Warning.class) != null) {
                        empty = false;
                    }
                }
                if (empty) {
                    Method[] methods = c.getDeclaredMethods();
                    for (Method method : methods) {
                        if(method.getAnnotation(Warning.class) != null) {
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
                        Warning warning = field.getAnnotation(Warning.class);
                        if (warning == null) {
                            continue;
                        }
                        System.out.println("\tField: " + field.getName() + ", state = " + warning.state() + ", information = \"" + warning.information() + '"');
                        break;
                    }
                }
                Method[] methods = c.getDeclaredMethods();
                for (Method method : methods) {
                    if (!Modifier.isPrivate(method.getModifiers()) || testPrivate) {
                        Warning warning = method.getAnnotation(Warning.class);
                        if (warning == null) {
                            continue;
                        }
                        System.out.println("\tMethod: " + method.getName() + ", state = " + warning.state() + ", information = \"" + warning.information() + '"');
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
                    Warning warning = method.getAnnotation(Warning.class);
                    if (warning == null) {
                        continue;
                    }
                    System.out.println("\tMethod: " + method.getName() + ", state = " + warning.state() + ", information = \"" + warning.information() + '"');
                    break;
                }
            }
        }

        public static void testField(Class<?> testClass , boolean testPrivate) {
            System.out.println(testClass.getName());
            Field[] fields = testClass.getDeclaredFields();
            for (Field field : fields) {
                if (!Modifier.isPrivate(field.getModifiers()) || testPrivate) {
                    Warning warning = field.getAnnotation(Warning.class);
                    if (warning == null) {
                        continue;
                    }
                    System.out.println("\tField: " + field.getName() + ", state = " + warning.state() + ", information = \"" + warning.information() + '"');
                    break;
                }
            }
        }

        public static void testClass(Class<?> testClass , boolean testPrivate) {
            System.out.println(testClass.getName());
            Warning warning = testClass.getAnnotation(Warning.class);
            if (warning != null) {
                System.out.println("\tWarning: " + warning.state() + ", information = \"" + warning.information() + '"');
            }
            Field[] fields = testClass.getDeclaredFields();
            for (Field field : fields) {
                if (!Modifier.isPrivate(field.getModifiers()) || testPrivate) {
                    warning = field.getAnnotation(Warning.class);
                    if (warning == null) {
                        continue;
                    }
                    System.out.println("\tField: " + field.getName() + ", state = " + warning.state() + ", information = \"" + warning.information() + '"');
                    break;
                }
            }
            Method[] methods = testClass.getDeclaredMethods();
            for (Method method : methods) {
                if (!Modifier.isPrivate(method.getModifiers()) || testPrivate) {
                    warning = method.getAnnotation(Warning.class);
                    System.out.println("\tMethod: " + method.getName() + ", state = " + warning.state() + ", information = \"" + warning.information() + '"');
                    break;
                }
            }
        }
    }
}
