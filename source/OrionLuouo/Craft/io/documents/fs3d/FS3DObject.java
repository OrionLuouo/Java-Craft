package OrionLuouo.Craft.io.documents.fs3d;

import java.util.Date;

public interface FS3DObject {
    FS3DType getType();
    default int toInteger() {
        return 0;
    }
    default float toFloat() {
        return 0.0f;
    }
    default Date toDate() {
        return null;
    }
    default long toTimestamp() {
        return 0;
    }
    default boolean toBoolean() {
        return false;
    }
    default byte toByte() {
        return 0;
    }
    default char toCharacter() {
        return 0;
    }
    default FS3DObject getValue(String fieldName) {
        return null;
    }
    default FS3DObject getValue(int index) {
        return null;
    }
    default FS3DObject[] getValues() {
        return null;
    }
    default FS3DObject[] getExtraArea() {
        return null;
    }
}
