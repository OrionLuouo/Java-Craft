package OrionLuouo.Craft.io.documents.fs3d.source;

import java.util.Set;
import java.util.function.BiFunction;

public interface Operator {
    Set<Character> POTENTIAL_OPERATOR_CHARACTERS = Set.of('+' , '-' , '*' , '/' , '%' , '=' , '?' , ':' , '<' , '>' , '^' , '&' , '|' , '!');
    OperatorPriority[] OPERATOR_PRIORITIES = new OperatorPriority[]{
            new SingleOperatorPriority(7) // ^
            , new RangeOperatorPriority(5 , 6) // << >>
            , new RangeOperatorPriority(2, 4) // * / %
            , new RangeOperatorPriority(0, 1) // + -
    };

    interface OperatorPriority {
        boolean corresponds(int operator);
    }

    record SingleOperatorPriority(int operator) implements OperatorPriority {
        @Override
        public boolean corresponds(int operator) {
            return operator == this.operator;
        }
    }

    record RangeOperatorPriority(int begin , int end) implements OperatorPriority {
        @Override
        public boolean corresponds(int operator) {
            return operator >= begin && operator <= end;
        }
    }

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
