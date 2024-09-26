package OrionLuouo.Craft.io.documents.fs3d.source;

import java.util.function.BiFunction;

public interface Operator {

    default int getInteger() {
        return (int) getFloat();
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

    default int shiftRightInteger(int value) {
        return value >> getInteger();
    }

    default int negativeInteger() {
        return -getInteger();
    }

    default int powerInteger(int value) {
        return (int) Math.pow(value , getInteger());
    }

    default float getFloat() {
        return (float) getInteger();
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

    default float negativeFloat() {
        return -getFloat();
    }

    default float powerFloat(float value) {
        return (float) Math.pow(value , getFloat());
    }
}
