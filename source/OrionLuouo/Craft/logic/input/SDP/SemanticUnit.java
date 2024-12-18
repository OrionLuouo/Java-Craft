package OrionLuouo.Craft.logic.input.SDP;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.data.Iterable;

public abstract class SemanticUnit {
    SemanticUnit[] children;
    StateLayer stateLayer;

    StateLayer getStateLayer() {
        return stateLayer;
    }

    abstract MatchState match(CouplePair<Object , WordParser.WordType> element , MatchUnit unit);

    abstract CouplePair<MatchState , MatchUnit> tryMatch(CouplePair<Object , WordParser.WordType> element);
}

enum MatchState {
    MATCHED , YET_TO_BE_MATCHED , MATCHED_YET_POTENTIAL , MISMATCHED , COMPLETE , COMPLETE_YET_POTENTIAL
}