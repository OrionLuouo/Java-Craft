package OrionLuouo.Craft.logic.input.SDP;

public interface MatchUnit extends Iterable<Object> {
    default boolean isPolyUnit() {
        return false;
    }
}
