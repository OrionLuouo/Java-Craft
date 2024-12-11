package OrionLuouo.Craft.logic.input.SDP;

import OrionLuouo.Craft.data.CouplePair;

public abstract class SemanticUnit {
    SemanticUnit[] children;

    StateLayer getStateLayer() {
        return null;
    }

    abstract MatchState match(CouplePair<Object , WordParser.WordType> element);
}

enum MatchState {
    COMPLETE , INCOMPLETE , COMPLETE_YET_POTENTIAL , NOT_CORRESPOND
}

abstract class LeaveSemanticUnit extends SemanticUnit {
    StateLayer stateLayer;

    @Override
    StateLayer getStateLayer() {
        return stateLayer;
    }
}
