package OrionLuouo.Craft.data;

public record CouplePair<TA , TB>(TA valueA , TB valueB) {
    @Override
    public String toString() {
        return "CouplePair{" + "valueA = " + valueA + ", valueB = " + valueB + '}';
    }
}
