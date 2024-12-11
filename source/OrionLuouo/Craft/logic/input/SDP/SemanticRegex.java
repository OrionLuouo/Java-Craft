package OrionLuouo.Craft.logic.input.SDP;

import OrionLuouo.Craft.data.Array;
import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.data.container.collection.sequence.CycledArrayList;
import OrionLuouo.Craft.data.container.collection.sequence.List;
import OrionLuouo.Craft.system.annotation.Unfinished;

import java.util.ArrayList;

/**
 * The final level of the parsing chain,
 * also the executing level.<p>
 * You can customize the logic structure of the document at your full will.<p>
 * All the morphemes here are defined as elements (A element is an object determined by the word and its type),
 * and the GrammarParser needs SemanticRegex constructed by dynamic combination of elements as its drive.<p>
 * Also,
 * the WordType will come to use here,
 * as the classification of elements.
 */
@Unfinished
public class SemanticRegex {
    static final StateLayer ROOT_LAYER = abandoned -> {
        throw new SDPException("SDPException-SemanticMismatch: No available regex to match.");
    };

    StructuredDocumentParser document;
    SemanticUnit root , unitNow , matchedUnit;
    MatchState lastMatchState;
    LeaveSemanticUnit[] leaves;
    List<CouplePair<Object , WordParser.WordType>> inputRecord;
    SemanticMatch match;

    public static SemanticRegex compile(String regex , StructuredDocumentParser document) {
        return null;
    }

    void rollback(CouplePair<Object , WordParser.WordType> element) {

    }

    void input(Object value , WordParser.WordType type) {
        CouplePair<Object , WordParser.WordType> element = new CouplePair<>(value, type);
        inputRecord.add(element);
        if (lastMatchState == MatchState.MATCHED) {
            for (SemanticUnit unit : unitNow.children) {
                lastMatchState = unit.match(element , match);
                if (lastMatchState == MatchState.MISMATCHED) {
                    continue;
                }
                unitNow = unit;
                switch (lastMatchState) {
                    case COMPLETE -> {
                        inputRecord.clean();
                        unit.getStateLayer().sentence(match);
                    }
                    case COMPLETE_YET_POTENTIAL -> {
                        inputRecord.clean();
                        match.record();
                        matchedUnit = unit;
                    }
                }
                return;
            }
            rollback(element);
        }
        else if (lastMatchState == MatchState.YET_TO_BE_MATCHED) {
            switch ((lastMatchState = unitNow.match(element , match))) {

            }
        }
    }

    void reset() {
        unitNow = matchedUnit = root;
        lastMatchState = MatchState.MATCHED;
        match = new SemanticMatch();
        if (inputRecord == null) {
            inputRecord = new CycledArrayList<>(16);
        }
    }

    /**
     * To connect the branch's root with the leaves of this regex.<p>
     * For later other operations on the regex,
     * its leaves would change.<p>
     * Meaning you can regard SemanticRegex as a reference to the SemanticUnits it manages.
     */
    public void bifurcate(SemanticRegex branch) {
        for (SemanticUnit leave : leaves) {
            leave.children = leave.children == null ? new SemanticUnit[] {
                    branch.root
            } : Array.connect(leave.children , branch.root.children);
        }
    }

    public void setStateLayer(StateLayer stateLayer) {
        for (LeaveSemanticUnit leave : leaves) {
            leave.stateLayer = stateLayer;
        }
    }
}
