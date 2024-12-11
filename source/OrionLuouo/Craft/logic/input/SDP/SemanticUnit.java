package OrionLuouo.Craft.logic.input.SDP;

import OrionLuouo.Craft.data.CouplePair;

public abstract class SemanticUnit {
    SemanticUnit[] children;

    StateLayer getStateLayer() {
        return null;
    }

    abstract MatchState match(CouplePair<Object , WordParser.WordType> element , SemanticMatch match);
}

enum MatchState {
    MATCHED , YET_TO_BE_MATCHED , MISMATCHED , COMPLETE , COMPLETE_YET_POTENTIAL
}

abstract class LeaveSemanticUnit extends SemanticUnit {
    StateLayer stateLayer;

    @Override
    StateLayer getStateLayer() {
        return stateLayer;
    }
}
