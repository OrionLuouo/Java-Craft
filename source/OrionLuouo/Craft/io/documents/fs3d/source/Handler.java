package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.io.documents.fs3d.FS3DObject;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

import java.security.Timestamp;
import java.util.Date;

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

record FinalIntegerHandler(int value) implements FinalValueHandler , Operator {
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

record FinalFloatHandler(float value) implements FinalValueHandler , Operator {
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
	public int shiftRightInteger(int value) {
        return value >> getInteger();
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