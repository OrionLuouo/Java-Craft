package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.io.documents.fs3d.FS3DObject;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;
import OrionLuouo.Craft.system.annotation.Unfinished;

import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;

public interface Handle {
    FS3DType getType();
    FS3DObject getValue();
}

record FunctionHandle(Handle[] argumentSources , FunctionVariable function) implements Handle {
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

interface FinalValueHandle extends Handle, FS3DObject {
}

interface DigitHandle extends Handle, Operator {}

record FinalIntegerHandle(int value) implements FinalValueHandle, DigitHandle {
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
	public int negativeInteger() {
        return -this.value;
    }

    @Override
    public FS3DObject getValue() {
        return new IntegerObject(value);
    }
}

record FinalFloatHandle(float value) implements FinalValueHandle, DigitHandle {
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
	public float negativeFloat() {
        return -this.value;
    }
}

record FinalStringHandle(String value) implements FinalValueHandle {
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

record FinalDateHandle(DateObject value) implements FinalValueHandle {
    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_DATE;
    }

    @Override
    public FS3DObject getValue() {
        return value;
    }
}

record FinalTimestampHandle(TimestampObject value) implements FinalValueHandle {
    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_TIMESTAMP;
    }

    @Override
    public FS3DObject getValue() {
        return value;
    }
}

interface BooleanHandle extends Handle , BooleanOperator {
    boolean get();
}

record FinalBooleanHandle(boolean value) implements FinalValueHandle, BooleanHandle {
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

record NullHandle(CustomedType type) implements Handle {
    @Override
    public FS3DType getType() {
        return type;
    }

    @Override
    public FS3DObject getValue() {
        return new CustomedObject(type , null);
    }
}

record InitializerHandle(FS3DType type) implements Handle {
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

record IntegerIncreaseHandle(IntegerVariableHandle source) implements DigitHandle {
    @Override
    public int getInteger() {
        return source.value.value++;
    }

    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_INTEGER;
    }

    @Override
    public FS3DObject getValue() {
        return new IntegerObject(getInteger());
    }
}

record IntegerPreIncreaseHandle(IntegerVariableHandle source) implements DigitHandle {
    @Override
    public int getInteger() {
        return ++source.value.value;
    }

    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_INTEGER;
    }

    @Override
    public FS3DObject getValue() {
        return new IntegerObject(getInteger());
    }
}

record IntegerDecreaseHandle(IntegerVariableHandle source) implements DigitHandle {
    @Override
    public int getInteger() {
        return source.value.value--;
    }

    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_INTEGER;
    }

    @Override
    public FS3DObject getValue() {
        return new IntegerObject(getInteger());
    }
}

record IntegerPreDecreaseHandle(IntegerVariableHandle source) implements DigitHandle {
    @Override
    public int getInteger() {
        return --source.value.value;
    }

    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_INTEGER;
    }

    @Override
    public FS3DObject getValue() {
        return new IntegerObject(getInteger());
    }
}

record IntegerFormulaHandle(DigitHandle[] values , int[] operators) implements DigitHandle {
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

    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_INTEGER;
    }

    @Override
    public FS3DObject getValue() {
        return new IntegerObject(getInteger());
    }
}

record FloatFormulaHandle(DigitHandle[] values , int[] operators) implements DigitHandle {
    @Override
    public float getFloat() {
        float value = switch (operators[0]) {
            case 0 -> {
                yield values[0].getFloat();
            }
            case 1 -> {
                yield values[0].negativeFloat();
            }
            default -> throw new IllegalStateException("Unexpected value: " + operators[0]);
        };
        BiFunction<Operator, Float, Float> addFloat = Operator::addFloat;
        for (int index = 1 ; index < values.length ;) {
            switch (operators[index]) {
                case 0 -> {
                    value = values[index++].addFloat(value);
                }
                case 1 -> {
                    value = values[index++].subtractFloat(value);
                }
                case 2 -> {
                    value = values[index++].multiplyFloat(value);
                }
                case 3 -> {
                    value = values[index++].divideFloat(value);
                }
                case 4 -> {
                    value = values[index++].modFloat(value);
                }
                case 5 -> {
                    value = values[index++].powerFloat(value);
                }
            }
        }
        return value;
    }

    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_FLOAT;
    }

    @Override
    public FS3DObject getValue() {
        return new FloatObject(getFloat());
    }
}

record BooleanFormulaHandle(BooleanHandle[] values, int[] operators) implements BooleanHandle {
    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_BOOLEAN;
    }

    @Override
    public FS3DObject getValue() {
        return new BooleanObject(get());
    }

    @Override
    public boolean get() {
        boolean value = values[0].get();
        for (int index = 1 ; index < values.length ;) {
            switch (operators[index]) {
                case 0 -> {
                    value = values[index++].or(value);
                }
                case 1 -> {
                    value = values[index++].and(value);
                }
                case 2 -> {
                    value = values[index++].xor(value);
                }
            }
        }
        return false;
    }
}

record StringFormulaHandle(StringFormulaHandle[] values) implements Handle {
    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_STRING;
    }

    @Override
    public FS3DObject getValue() {
        return new StringObject(get());
    }

    public String get() {
        StringBuilder builder = new StringBuilder();
        for (StringFormulaHandle handle : values) {
            builder.append(handle.get());
        }
        return builder.toString();
    }
}

record TernaryFormulaHandle(BooleanHandle condition , Handle valueA , Handle valueB) implements Handle {
    @Override
    public FS3DType getType() {
        return valueA.getType();
    }

    @Override
    public FS3DObject getValue() {
        return condition.get() ? valueA.getValue() : valueB.getValue();
    }
}