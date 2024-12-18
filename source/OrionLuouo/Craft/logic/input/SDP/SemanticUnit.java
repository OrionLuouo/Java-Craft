package OrionLuouo.Craft.logic.input.SDP;

import OrionLuouo.Craft.data.CouplePair;

import java.util.Iterator;

public abstract class SemanticUnit {
    SemanticUnit[] children;
    StateLayer stateLayer;

    abstract MatchState match(CouplePair<Object , WordParser.WordType> element , MatchUnit unit, SemanticRegex regex);

    abstract CouplePair<MatchState , MatchUnit> tryMatch(CouplePair<Object , WordParser.WordType> element);
}

enum MatchState {
    MATCHED , YET_TO_BE_MATCHED , MATCHED_YET_POTENTIAL , MISMATCHED
}

class TypeSemanticUnit extends SemanticUnit {
    class TypeMatchUnit implements MatchUnit {
        Object value;

        TypeMatchUnit(Object value) {
            this.value = value;
        }

        @Override
        public Iterator<Object> iterator() {
            return new Iterator<Object>() {
                boolean got;

                {
                    got = false;
                }

                @Override
                public boolean hasNext() {
                    return !got;
                }

                @Override
                public Object next() {
                    return (got = !got) ? value : (got = !got) ? null : null;
                }
            };
        }
    }

    final WordParser.WordType type;

    TypeSemanticUnit(WordParser.WordType type) {
        this.type = type;
    }

    /**
     * Should be unused.
     */
    @Override
    MatchState match(CouplePair<Object, WordParser.WordType> element, MatchUnit unit, SemanticRegex regex) {
        throw new SDPException("SDPException-InnerError: This is an error occurred in the inner codes. It is inviable to fix, only to be reported.");
    }

    @Override
    CouplePair<MatchState, MatchUnit> tryMatch(CouplePair<Object, WordParser.WordType> element) {
        return element.valueB() == type ? new CouplePair<>(MatchState.MATCHED , new TypeMatchUnit(element.valueA())) : new CouplePair<>(MatchState.MISMATCHED , null);
    }
}

class RestrictedTypeSemanticUnit extends TypeSemanticUnit {
    final Object[] expectedValues;

    RestrictedTypeSemanticUnit(WordParser.WordType type , Object[] expectedValues) {
        super(type);
        this.expectedValues = expectedValues;
    }

    @Override
    CouplePair<MatchState , MatchUnit> tryMatch(CouplePair<Object , WordParser.WordType> element) {
        if (element.valueB() != type) {
            return new CouplePair<>(MatchState.MISMATCHED , null);
        }
        for (Object value : expectedValues) {
            if (value.equals(element.valueA())) {
                return new CouplePair<>(MatchState.MATCHED , new TypeMatchUnit(value));
            }
        }
        return new CouplePair<>(MatchState.MISMATCHED , null);
    }
}

class PotentialSemanticUnit extends SemanticUnit {
    final SemanticUnit essence;

    PotentialSemanticUnit(SemanticUnit essence) {
        this.essence = essence;
    }

    @Override
    MatchState match(CouplePair<Object, WordParser.WordType> element, MatchUnit unit, SemanticRegex regex) {
        return essence.match(element , unit, regex);
    }

    @Override
    CouplePair<MatchState, MatchUnit> tryMatch(CouplePair<Object, WordParser.WordType> element) {
        CouplePair<MatchState , MatchUnit> match = essence.tryMatch(element);
        return match.valueA() == MatchState.MISMATCHED ? new CouplePair<>(MatchState.SKIPPED , match.valueB()) : match;
    }
}

class ArbitrarySemanticUnit extends SemanticUnit {
    final SemanticUnit essence;


    ArbitrarySemanticUnit(SemanticUnit essence) {
        this.essence = essence;
    }
}

class MultipleSemanticUnit extends SemanticUnit {

}