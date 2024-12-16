package OrionLuouo.Craft.logic.input.SDP;

import OrionLuouo.Craft.data.Array;
import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.data.Iterable;
import OrionLuouo.Craft.data.Iterator;
import OrionLuouo.Craft.data.container.collection.sequence.ChunkChainList;
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
 * as the classification of elements.<p>
 * The Match process obeys some greedy match rules.<p>
 * If it has begun matching a repeatable element,
 * yet failed,
 * it won't try to truncate the repeated sequence.<p>
 * But the regex will make record points at where the regex bifurcates and has a StateLayer,
 * like when an element is potential as a whole.<p>
 * That is,
 * whenever the regex fails to match,
 * it trys to roll back to the last record point.
 */
@Unfinished
public class SemanticRegex {
    static final StateLayer ROOT_LAYER = abandoned -> {
        throw new SDPException("SDPException-SemanticMismatch: No available regex to match.");
    };

    SemanticUnit root , unitNow;
    MatchState lastMatchState;
    LeaveSemanticUnit[] leaves;
    List<MatchRecord> inputRecord;
    SemanticMatch match;

    @Unfinished
    void rollback(CouplePair<Object , WordParser.WordType> element) {

    }

    @Unfinished
    void input(Object value , WordParser.WordType type) {
        CouplePair<Object , WordParser.WordType> element = new CouplePair<>(value, type);
        switch (lastMatchState) {
            case MATCHED -> {
                SemanticUnit unit;
                for (int index = 0 ; index < unitNow.children.length ; ) {
                    unit = unitNow.children[index++];
                    MatchState state =
                    if (state == MatchState.MISMATCHED) {
                        continue;
                    }
                    if (index != unitNow.children.length) {



                    }
                }
            }
            case COMPLETE_YET_POTENTIAL -> {

            }
            case YET_TO_BE_MATCHED -> {

            }
            case MATCHED_YET_POTENTIAL -> {

            }
        }
    }

    void reset() {
        unitNow = root;
        lastMatchState = MatchState.MATCHED;
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

@Unfinished
class MatchRecord extends ChunkChainList<CouplePair<Object , WordParser.WordType>> {
    SemanticUnit branchNode;
    int branchIndex;

    MatchRecord(SemanticUnit branchNode) {
        super(8);
        this.branchNode = branchNode;
    }

    @Override
    public Iterator<CouplePair<Object, WordParser.WordType>> iterator() {
        return null;
    }
}