package OrionLuouo.Craft.logic.input.SDP;

import OrionLuouo.Craft.data.CouplePair;


public interface SemanticUnit {
    boolean match(CouplePair<Object , WordParser.WordType> element);
}

record RestrictedSemanticUnit(WordParser.WordType type , Object[] expectedValues) implements SemanticUnit {

    @Override
    public boolean match(CouplePair<Object, WordParser.WordType> element) {
        if (element.valueB() != type) {
            return false;
        }
        for (Object expectedValue : expectedValues) {
            if (element.valueA().equals(expectedValue)) {
                return true;
            }
        }
        return false;
    }
}

record ValueSemanticUnit(WordParser.WordType type, Object expectedValue) implements SemanticUnit {
    @Override
    public boolean match(CouplePair<Object, WordParser.WordType> element) {
        return element.valueB() == expectedValue || element.valueA().equals(expectedValue);
    }
}

record TypeSemanticUnit(WordParser.WordType type) implements SemanticUnit {
    @Override
    public boolean match(CouplePair<Object, WordParser.WordType> element) {
        return element.valueB() == type;
    }
}