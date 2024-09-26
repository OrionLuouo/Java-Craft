package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.io.documents.fs3d.FS3DObject;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

import java.security.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Handler {
    FS3DType getType();
    FS3DObject getValue();
}

record FunctionHandler(Handler[] argumentSources , FunctionVariable function) implements Handler {
    @Override
    public FS3DType getType() {
        return function.getType();
    }

    @Override
    public FS3DObject getValue() {
        if (argumentSources == null) {
            return function.functionInstance.invoke(null);
        }
        FS3DObject[] arguments = new FS3DObject[argumentSources.length];
        for (int i = 0; i < argumentSources.length; i++) {
            arguments[i] = argumentSources[i].getValue();
        }
        return function.functionInstance.invoke(arguments);
    }
}

interface FinalValueHandler extends Handler , FS3DObject {
}

interface DigitHandler extends Handler , Operator {}

record FinalIntegerHandler(int value) implements FinalValueHandler , DigitHandler {
    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_INTEGER;
    }
    
    @Override
	public int getInteger() {
        return value;
    }

    @Override
	public int addInteger(int value) {
        return value + this.value;
    }

    @Override
	public int subtractInteger(int value) {
        return value - this.value;
    }

    @Override
	public int multiplyInteger(int value) {
        return value * this.value;
    }

    @Override
	public int divideInteger(int value) {
        return value / this.value;
    }

    @Override
	public int modInteger(int value) {
        return value % this.value;
    }

    @Override
	public int shiftLeftInteger(int value) {
        return value << this.value;
    }

    @Override
	public int negativeInteger(int value) {
        return -this.value;
    }

    @Override
    public FS3DObject getValue() {
        return new IntegerObject(value);
    }
}

record FinalFloatHandler(float value) implements FinalValueHandler , DigitHandler {
    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_FLOAT;
    }

    @Override
    public FS3DObject getValue() {
        return new FloatObject(value);
    }

    @Override
	public float getFloat() {
        return value;
    }

    @Override
	public float addFloat(float value) {
        return value + this.value;
    }

    @Override
	public float subtractFloat(float value) {
        return value - this.value;
    }

    @Override
	public float multiplyFloat(float value) {
        return value * this.value;
    }

    @Override
	public float divideFloat(float value) {
        return value / this.value;
    }

    @Override
	public float modFloat(float value) {
        return value % this.value;
    }

    @Override
	public float negativeFloat(float value) {
        return -this.value;
    }
}

record FinalStringHandler(String value) implements FinalValueHandler {
    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_STRING;
    }

    @Override
    public FS3DObject getValue() {
        return new StringObject(value);
    }

    public String connect(String value) {
        return value + this.value;
    }
}

record FinalDateHandler(DateObject value) implements FinalValueHandler {
    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_DATE;
    }

    @Override
    public FS3DObject getValue() {
        return value;
    }
}

record FinalTimestampHandler(TimestampObject value) implements FinalValueHandler {
    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_TIMESTAMP;
    }

    @Override
    public FS3DObject getValue() {
        return value;
    }
}

record BooleanHandler(boolean value) implements FinalValueHandler , BooleanOperator {
    @Override
    public boolean get() {
        return value;
    }

    @Override
    public boolean or(boolean value) {
        return value || this.value;
    }

    @Override
    public boolean and(boolean value) {
        return value && this.value;
    }

    @Override
    public boolean xor(boolean value) {
        return value ^ this.value;
    }

    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_BOOLEAN;
    }

    @Override
    public FS3DObject getValue() {
        return new BooleanObject(value);
    }
}

record NullHandler(CustomedType type) implements Handler {
    @Override
    public FS3DType getType() {
        return type;
    }

    @Override
    public FS3DObject getValue() {
        return new CustomedObject(type , null);
    }
}

record InitializerHandler(FS3DType type) implements Handler {
    @Override
    public FS3DType getType() {
        return type;
    }

    @Override
    public FS3DObject getValue() {
        return initialize(type);
    }

    FS3DObject initialize(FS3DType type) {
        if (type instanceof CustomedType customedType) {
            CustomedObject object = customedType.getDefaultValue();
            Iterator<Map.Entry<String , CustomedType.Field>> iterator = customedType.fields.entrySet().iterator();
            for (int index = 0 , size = customedType.fields.size() ; index < size ;) {
                object.values[index++] = initialize(iterator.next().getValue().type());
            }
            return object;
        }
        else {
            return type.getDefaultValue();
        }
    }
}

record FormulaHandler(DigitHandler[] values , int[] operators) implements DigitHandler {
    @Override
    public int getInteger() {
        int value = switch (operators[0]) {
            case 0 -> {
                yield values[0].getInteger();
            }
            case 1 -> {
                yield values[0].negativeInteger();
            }
            default -> throw new IllegalStateException("Unexpected value: " + operators[0]);
        };
        BiFunction<Operator, Integer, Integer> addInteger = Operator::addInteger;
        for (int index = 1 ; index < values.length ;) {
            switch (operators[index]) {
                case 0 -> {
                    value = values[index++].addInteger(value);
                }
                case 1 -> {
                    value = values[index++].subtractInteger(value);
                }
                case 2 -> {
                    value = values[index++].multiplyInteger(value);
                }
                case 3 -> {
                    value = values[index++].divideInteger(value);
                }
                case 4 -> {
                    value = values[index++].modInteger(value);
                }
                case 5 -> {
                    value = values[index++].shiftLeftInteger(value);
                }
                case 6 -> {
                    value = values[index++].shiftRightInteger(value);
                }
                case 7 -> {
                    value = values[index++].powerInteger(value);
                }
            }
        }
        return value;
    }
    
    public 

    @Override
    public FS3DType getType() {
        return null;
    }

    @Override
    public FS3DObject getValue() {
        return null;
    }
}

record BooleanFormulaHandler(BooleanHandler[] values, int[] operators) implements Handler {
    @Override
    public FS3DType getType() {
        return null;
    }

    @Override
    public FS3DObject getValue() {
        return null;
    }
}

record StringFormulaHandler(StringFormulaHandler[] values) implements Handler {

    @Override
    public FS3DType getType() {
        return null;
    }

    @Override
    public FS3DObject getValue() {
        return null;
    }
}

record TernaryFormulaHandler(BooleanHandler condition , Handler valueA , Handler valueB) implements Handler {

    @Override
    public FS3DType getType() {
        return null;
    }

    @Override
    public FS3DObject getValue() {
        return null;
    }
}