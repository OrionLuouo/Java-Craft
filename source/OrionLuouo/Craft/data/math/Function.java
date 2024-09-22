package OrionLuouo.Craft.data.math;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 1. Function 既是用户使用的类，也为整个Function体系提供了超父类支持。
 * 两种功能混杂导致对外和队内构造函数冲突，所以请使用Function.parse()方法来获取Function对象。
 * 2. Function支持使用任意数量的空格，底层原理是将空格去除，所以即使算术式形如 "1 2 +3"，最终也会被解析为 12+3。
 * 3. 如算术式中无未知数，请使用calculate()方法直接进行计算，
 * 如存在一个未知数，则您可使用calculate(double) 或 calculate(String[] , double[])方法，
 * 如存在多个未知数，则请使用calculate(String[] , double[])。
 * 4. 支持的运算符如下
 * 1. + 加 ， - 减
 * 2. * 乘法， / 除 ， % 取模
 * 3. ^ 乘方 ， ? 开方
 * 5. 内置函数如下
 * 1.abs(v) : 绝对值
 * 2.PI() : 圆周率近似值
 * 3.max(a , b) : 最大值
 * 4.min(a , b) : 最小值
 * 5.avg(vs...) : 平均值
 * 6.sin(v) : 三角函数sin
 * 7.cos(v) : 三角函数cos
 * 8.tan(v) : 三角函数tan
 */
public class Function {
    /**
     * Custom your own functions.
     * You need to override method calculate(double[] args),
     * and it must return the result value.
     * A funny point,
     * I won't ask you to offer a number for arg limiting.
     * So the count can be any.
     * You'd better check it before calculating.
     * Don't change the constructor and method:cal(...).
     * After overriding this class,
     * add its class instance to the library by invoking Function.addFunctionLib(...) with arg String:name.
     */
    public static abstract class CustomFunction extends Function {
        Function[] functions;

        protected CustomFunction(String func) {
            if (func.length() == 0)
                return;
            int li = 0;
            char[] chars = func.toCharArray();
            int brackets = 0;
            ArrayList<String> fs = new ArrayList<>();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '(') {
                    brackets++;
                    continue;
                }
                if (chars[i] == ')') {
                    brackets--;
                    continue;
                }
                if (brackets != 0)
                    continue;
                if (chars[i] == ',') {
                    fs.add(func.substring(li, i));
                    li = i + 1;
                }
            }
            if (li == 0)
                fs.add(func);
            functions = new Function[fs.size()];
            String[] a = new String[fs.size()];
            fs.toArray(a);
            for (int i = 0; i < a.length; i++)
                functions[i] = Function.parse(a[i]);
        }

        protected double cal(double n, Map<String, Double> args) {
            if (functions == null)
                return calculate(new double[0]);
            double[] values = new double[functions.length];
            for (int i = 0; i < values.length; i++)
                values[i] = functions[i].calculate(args);
            return calculate(values);
        }

        protected abstract double calculate(double[] args);
    }

    static final char[][] OPERATORS = {
            {'+', '-'},
            {'*', '/', '%'},
            {'^', '?'}
    };
    static final String[] FUNCTIONS_NAME = {
            "abs", "PI", "max", "min", "avg", "sin", "cos", "tan"
    };
    static final HashMap<String, Constructor<Function>> FUNCTIONS = new HashMap<>();
    static final HashMap<String, Constructor<CustomFunction>> FUNCTIONS_LIB = new HashMap<>();
    protected static Constructor<Function>[][] pieceConstructors;

    static {
        try {
            Class[][] pieceClasses = new Class[][]{
                    {Add_SubFunction.class, Sub_SubFunction.class},
                    {Mul_SubFunction.class, Div_SubFunction.class, Mod_SubFunction.class},
                    {Pow_SubFunction.class, Depow_SubFunction.class}
            };
            Class[] functionClasses = new Class[]{
                    Absolute_FunFunction.class, PI_FunFunction.class, Max_FunFunction.class, Min_FunFunction.class, Average_FunFunction.class, Sin_FunFunction.class, Cos_FunFunction.class, Tan_FunFunction.class
            };
            pieceConstructors = new Constructor[pieceClasses.length][];
            for (int i = 0; i < pieceClasses.length; i++) {
                pieceConstructors[i] = new Constructor[pieceClasses[i].length];
                for (int ci = 0; ci < pieceClasses[i].length; ci++)
                    pieceConstructors[i][ci] = pieceClasses[i][ci].getDeclaredConstructor(String.class);
            }
            for (int i = 0; i < functionClasses.length; i++)
                FUNCTIONS.put(FUNCTIONS_NAME[i], functionClasses[i].getDeclaredConstructor(String.class));
        } catch (Exception e) {
        }
    }

    protected Function() {
    }

    public static Function parse(String func) {
        func = func.replaceAll("\\s", "");
        return new SubFunction(func);
    }

    public static boolean addFunctionLib(Class<CustomFunction> function, String name) {
        try {
            FUNCTIONS_LIB.put(name, function.getDeclaredConstructor(String.class));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Constructor<CustomFunction> removeFunctionLib(String name) {
        return FUNCTIONS_LIB.remove(name);
    }

    public static Set<Map.Entry<String, Constructor<CustomFunction>>> getFunctionLib() {
        return FUNCTIONS_LIB.entrySet();
    }

    protected double cal(double n, Map<String, Double> args) {
        return 0;
    }

    public double calculate() {
        return 0;
    }

    public double calculate(double x) {
        return 0;
    }

    public double calculate(String[] arguments, double[] values) {
        return 0;
    }

    protected String removeBracket(String func) {
        int brackets = 0;
        char[] chars = func.toCharArray();
        if (chars[0] != '(')
            return func;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(')
                brackets++;
            else if (chars[i] == ')')
                brackets--;
            if (brackets == 0 && i != chars.length - 1)
                return func;
        }
        return func.substring(1, func.length() - 1);
    }

    public String[] getArguments() {
        return null;
    }

    public double calculate(Map<String, Double> args) {
        return 0;
    }
}

class SubFunction extends Function {
    Function[] functions;

    protected SubFunction(String func) {
        if (func.length() == 0)
            return;
        func = removeBracket(func);
        if (func.matches("\\d*")) {
            functions = new Function[]{new NumberFunction(func)};
            return;
        }
        ArrayList<Function> list = new ArrayList<>();
        char[] chars = func.toCharArray();
        for (int oai = 0; oai < OPERATORS.length; oai++) {
            int brackets = 0;
            int li = 0; // last index
            int loi = 0; // last operator index
            lp:
            for (int i = 0; i < chars.length; i++) {
                for (int oi = 0; oi < OPERATORS[oai].length; oi++) {
                    if (chars[i] == '(') {
                        brackets++;
                        continue lp;
                    }
                    if (chars[i] == ')') {
                        brackets--;
                        continue lp;
                    }
                    if (chars[i] == OPERATORS[oai][oi] && brackets == 0) {
                        if (li != i) {
                            try {
                                if (li == 0)
                                    list.add(pieceConstructors[0][loi].newInstance(func.substring(li, i)));
                                else list.add(pieceConstructors[oai][loi].newInstance(func.substring(li, i)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        li = i + 1;
                        loi = oi;
                    }
                }
            }
            if (li != 0) {
                try {
                    list.add(pieceConstructors[oai][loi].newInstance(func.substring(li)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                functions = new Function[list.size()];
                list.toArray(functions);
                return;
            }
        }
        if (func.matches(".*\\(.*\\)")) {
            String f = func.substring(0, func.indexOf("("));
            Constructor function = FUNCTIONS.get(f);
            if (function != null) {
                try {
                    functions = new Function[]{(Function) function.newInstance(func.substring(func.indexOf("(") + 1, func.length() - 1))};
                } catch (Exception e) {
                }
                return;
            }
            function = FUNCTIONS_LIB.get(f);
            if (function != null) {
                try {
                    functions = new Function[]{(Function) function.newInstance(func.substring(func.indexOf("(") + 1, func.length() - 1))};
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        functions = new Function[]{new ArgumentFunction(func)};
    }

    protected SubFunction() {
    }

    public String[] getArguments() {
        String[][] args = new String[functions.length][];
        int n = 0;
        int i = 0;
        for (; i < functions.length; i++) {
            args[i] = functions[i].getArguments();
            n += args[i] == null ? 0 : args[i].length;
        }
        String[] a = new String[n];
        i = 0;
        int ai = 0;
        int aai = 0;
        String[] p = args[0];
        for (; i < n; ) {
            while (ai == p.length) {
                p = args[++aai];
                ai = 0;
            }
            a[i++] = p[ai++];
        }
        return a;
    }

    protected double cal(double n, Map<String, Double> args) {
        double sum = 0;
        for (Function f : functions)
            sum = f.cal(sum, args);
        return sum;
    }

    public double calculate() {
        return cal(0, null);
    }

    public double calculate(double x) {
        return cal(0, new HashMap<String, Double>() {
            {
                put("x", x);
            }
        });
    }

    public double calculate(String[] arguments, double[] values) {
        HashMap<String, Double> map = new HashMap<>();
        for (int i = 0; i < arguments.length; i++)
            map.put(arguments[i], values[i]);
        return cal(0, map);
    }

    public double calculate(Map<String, Double> args) {
        return cal(0, args);
    }
}

class NumberFunction extends Function {
    double n;

    public NumberFunction(String func) {
        try {
            n = Double.parseDouble(func);
        } catch (NumberFormatException e) {
        }
    }

    public double cal(double n, double x) {
        return this.n;
    }

    public double cal(double n, Map<String, Double> args) {
        return this.n;
    }

    public double cal(double n) {
        return this.n;
    }
}

class ArgumentFunction extends Function {
    String argument;

    protected ArgumentFunction(String func) {
        argument = func;
    }

    public double cal(double n, double x) {
        return x;
    }

    public double cal(double n, Map<String, Double> args) {
        return args.get(argument);
    }

    public String[] getArguments() {
        return new String[]{argument};
    }
}

class FuncFunction extends SubFunction {
    protected FuncFunction(String func) {
        int li = 0;
        char[] chars = func.toCharArray();
        int brackets = 0;
        ArrayList<String> fs = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(') {
                brackets++;
                continue;
            }
            if (chars[i] == ')') {
                brackets--;
                continue;
            }
            if (brackets != 0)
                continue;
            if (chars[i] == ',') {
                fs.add(func.substring(li, i));
                li = i + 1;
            }
        }
        if (li == 0)
            fs.add(func);
        functions = new Function[fs.size()];
        String[] a = new String[fs.size()];
        fs.toArray(a);
        for (int i = 0; i < a.length; i++)
            functions[i] = new Add_SubFunction(a[i]);
    }
}

class Add_SubFunction extends SubFunction {

    protected Add_SubFunction(String func) {
        super(func);
    }

    protected double cal(double n, Map<String, Double> args) {
        return n + super.cal(n, args);
    }

}

class Sub_SubFunction extends SubFunction {

    protected Sub_SubFunction(String func) {
        super(func);
    }

    protected double cal(double n, Map<String, Double> args) {
        return n - super.cal(n, args);
    }

}

class Mul_SubFunction extends SubFunction {
    protected Mul_SubFunction(String func) {
        super(func);
    }


    protected double cal(double n, Map<String, Double> args) {
        return n * super.cal(n, args);
    }

}

class Div_SubFunction extends SubFunction {
    protected Div_SubFunction(String func) {
        super(func);
    }

    protected double cal(double n, Map<String, Double> args) {
        return n / super.cal(n, args);
    }

}

class Mod_SubFunction extends SubFunction {
    protected Mod_SubFunction(String func) {
        super(func);
    }

    protected double cal(double n, Map<String, Double> args) {
        return n % super.cal(n, args);
    }

}

class Pow_SubFunction extends SubFunction {
    protected Pow_SubFunction(String func) {
        super(func);
    }

    protected double cal(double n, Map<String, Double> args) {
        return Math.pow(n, super.cal(n, args));
    }

}

class Depow_SubFunction extends SubFunction {
    protected Depow_SubFunction(String func) {
        super(func);
    }

    protected double cal(double n, Map<String, Double> args) {
        return Math.pow(n, 1 / super.cal(n, args));
    }

}

class Absolute_FunFunction extends FuncFunction {
    String name = "abs";

    protected Absolute_FunFunction(String func) {
        super(func);
    }

    protected double cal(double n, Map<String, Double> args) {
        n = super.cal(n, args);
        return n < 0 ? -n : n;
    }
}

class PI_FunFunction extends FuncFunction {
    String name = "PI";

    protected PI_FunFunction(String func) {
        super(func);
    }

    protected double cal(double n, Map<String, Double> args) {
        return Math.PI;
    }
}

class Max_FunFunction extends FuncFunction {
    String name = "max";

    protected Max_FunFunction(String func) {
        super(func);
    }

    protected double cal(double n, Map<String, Double> args) {
        double a = functions[0].cal(0, args), b = functions[1].cal(0, args);
        return a > b ? a : b;
    }
}

class Min_FunFunction extends FuncFunction {
    String name = "min";

    protected Min_FunFunction(String func) {
        super(func);
    }

    protected double cal(double n, Map<String, Double> args) {
        double a = functions[0].cal(0, args), b = functions[1].cal(0, args);
        return a < b ? a : b;
    }
}

class Average_FunFunction extends FuncFunction {
    String name = "avg";

    protected Average_FunFunction(String func) {
        super(func);
    }

    protected double cal(double n, Map<String, Double> args) {
        if (functions == null || functions.length == 0)
            return 0;
        double avg = 0;
        for (Function function : functions)
            avg += function.cal(0, args);
        return avg / functions.length;
    }
}

class Sin_FunFunction extends FuncFunction {
    String name = "sin";

    protected Sin_FunFunction(String func) {
        super(func);
    }

    protected double cal(double n, Map<String, Double> args) {
        return Math.sin(functions[0].cal(0, args));
    }
}

class Cos_FunFunction extends FuncFunction {
    String name = "cos";

    protected Cos_FunFunction(String func) {
        super(func);
    }

    protected double cal(double n, Map<String, Double> args) {
        return Math.cos(functions[0].cal(0, args));
    }
}

class Tan_FunFunction extends FuncFunction {
    String name = "tan";

    protected Tan_FunFunction(String func) {
        super(func);
    }

    protected double cal(double n, Map<String, Double> args) {
        return Math.tan(functions[0].cal(0, args));
    }
}

class LogFunFunction extends FuncFunction {
    String name = "log";

    protected LogFunFunction(String func) {
        super(func);
    }

    protected double cal(double n, Map<String, Double> args) {
        return Math.log(functions[0].cal(0, args));
    }
}

class EFunFunction extends FuncFunction {
    String name = "E";

    protected EFunFunction(String func) {
        super(func);
    }

    protected double cal(double n, Map<String, Double> args) {
        return Math.E;
    }
}

class FloorFunFunction extends FuncFunction {
    String name = "floor";

    protected FloorFunFunction(String func) {
        super(func);
    }

    protected double cal(double n, Map<String, Double> args) {
        return Math.floor(functions[0].cal(0, args));
    }

}

class CeilFunFunction extends FuncFunction {
    String name = "ceil";

    protected CeilFunFunction(String func) {
        super(func);
    }

    protected double cal(double n, Map<String, Double> args) {
        return Math.ceil(functions[0].cal(0, args));
    }
}
