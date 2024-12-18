package OrionLuouo.Craft.logic.input.SDP;

public interface MatchUnit extends Iterable<MatchUnit> {
    default boolean isPolyUnit() {
        return false;
    }
}
