package OrionLuouo.Craft.io.documents.fs3d.source;

public interface Operator {
    default int getInteger() {
        return 0;
    }

    default int addInteger(int value) {
        return value + getInteger();
    }

    default int subtractInteger(int value) {
        return value - getInteger();
    }

    default int multiplyInteger(int value) {
        return value * getInteger();
    }

    default int divideInteger(int value) {
        return value / getInteger();
    }

    default int modInteger(int value) {
        return value % getInteger();
    }

    default int shiftLeftInteger(int value) {
        return value << getInteger();
    }

    default int negativeInteger(int value) {
        return -getInteger();
    }

    default float getFloat() {
        return 0f;
    }

    default float addFloat(float value) {
        return value + getFloat();
    }

    default float subtractFloat(float value) {
        return value - getFloat();
    }

    default float multiplyFloat(float value) {
        return value * getFloat();
    }

    default float divideFloat(float value) {
        return value / getFloat();
    }

    default float modFloat(float value) {
        return value % getFloat();
    }

    default int shiftRightInteger(int value) {
        return value >> getInteger();
    }

    default float negativeFloat(float value) {
        return -getFloat();
    }
}
