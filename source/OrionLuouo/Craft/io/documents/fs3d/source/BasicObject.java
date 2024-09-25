package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.io.documents.fs3d.FS3DObject;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

import java.util.Date;

public abstract class BasicObject implements FS3DObject {
}

class IntegerObject extends BasicObject {
    int value;

    public IntegerObject(int value) {
        this.value = value;
    }

    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_INTEGER;
    }

    @Override
    public int toInteger() {
        return value;
    }
}

class FloatObject extends BasicObject {
    float value;

    public FloatObject(float value) {
        this.value = value;
    }

    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_FLOAT;
    }

    @Override
    public float toFloat() {
        return value;
    }
}

class DateObject extends BasicObject {
    Date value;

    public DateObject(Date value) {
        this.value = value;
    }

    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_DATE;
    }

    @Override
    public Date toDate() {
        return value;
    }
}

class StringObject extends BasicObject {
    String value;

    public StringObject(String value) {
        this.value = value;
    }

    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_STRING;
    }

    @Override
    public String toString() {
        return value;
    }
}

class ByteObject extends BasicObject {
    byte value;

    public ByteObject(byte value) {
        this.value = value;
    }

    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_BYTE;
    }

    @Override
    public int toInteger() {
        return value;
    }

    @Override
    public byte toByte() {
        return value;
    }
}

class CharacterObject extends BasicObject {
    char value;

    public CharacterObject(char value) {
        this.value = value;
    }

    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_CHARACTER;
    }

    @Override
    public int toInteger() {
        return value;
    }

    @Override
    public char toCharacter() {
        return value;
    }
}

class BooleanObject extends BasicObject {
    boolean value;

    public BooleanObject(boolean value) {
        this.value = value;
    }

    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_BOOLEAN;
    }

    @Override
    public boolean toBoolean() {
        return value;
    }
}

class TimestampObject extends BasicObject {
    long value;

    public TimestampObject(long value) {
        this.value = value;
    }

    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_TIMESTAMP;
    }

    @Override
    public long toTimestamp() {
        return value;
    }
}