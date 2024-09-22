package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.io.documents.fs3d.FS3DObject;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

import java.util.Date;

public abstract class BasicObject implements FS3DObject {
}

class IntegerObject extends BasicObject {
    int value;

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

    @Override
    public FS3DType getType() {
        return FS3DType.BASIC_TYPE_BOOLEAN;
    }

    @Override
    public boolean toBoolean() {
        return value;
    }
}