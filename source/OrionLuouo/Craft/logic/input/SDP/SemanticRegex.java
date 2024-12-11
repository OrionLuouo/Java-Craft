package OrionLuouo.Craft.logic.input.SDP;

import OrionLuouo.Craft.data.Array;
import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.data.container.collection.sequence.CycledArrayList;
import OrionLuouo.Craft.data.container.collection.sequence.List;
import OrionLuouo.Craft.system.annotation.Unfinished;

import java.util.ArrayList;

/**
 * The final level of the parsing chain,
 * also the executing level.
 * You can customize the logic structure of the document at your full will.
 * All the morphemes here are defined as elements (A element is an object determined by the word and its type),
 * and the GrammarParser needs SemanticRegex constructed by dynamic combination of elements as its drive.
 * Also,
 * the WordType will come to use here,
 * as the classification of elements.
 */
@Unfinished
public class SemanticRegex {
    static final StateLayer ROOT_LAYER = abandoned -> {
        throw new SDPException("SDPException-SemanticMismatch: No available regex to match.");
    };

    SemanticUnit root , unitNow , matchedUnit;
    LeaveSemanticUnit[] leaves;
    List<CouplePair<Object , WordParser.WordType>> inputRecord;
    SemanticMatch match;

    public static SemanticRegex compile(String regex) {
        return null;
    }

    void input(Object value , WordParser.WordType type) {

        System.out.println("GrammarParser accepted: <" + value.getClass() + ':' + value + ", " + type.name() + ">");
    }

    void reset() {
        unitNow = matchedUnit = root;
        match = new SemanticMatch();
        if (inputRecord == null) {
            inputRecord = new CycledArrayList<>(16);
        }
        else {
            inputRecord.clean();
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
