package OrionLuouo.Craft.io.documents.fs3d.source;

public interface BooleanOperator {
    default boolean get() {
        return false;
    }
    default boolean or(boolean value) {
        return value || get();
    }
    default boolean and(boolean value) {
        return value && get();
    }
    default boolean xor(boolean value) {
        return value ^ get();
    }
}
