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

    StructuredDocumentParser structuredDocumentParser;
    SemanticUnit root , unitNow;
    MatchState lastMatchState;
    SemanticUnit[] leaves;
    SemanticMatch match;
    MatchUnit matchUnit;
    MatchRecord record;

    @Unfinished
    void rollback() {

    }

    @Unfinished
    void input(Object value , WordParser.WordType type) {
        CouplePair<Object , WordParser.WordType> element = new CouplePair<>(value, type);
        switch (lastMatchState) {
            case MATCHED -> {
                matchNext(element);
            }
            case COMPLETE_YET_POTENTIAL -> {
                MatchState state = unitNow.match(element , matchUnit);
                if (state == MatchState.MISMATCHED) {
                    
                }
                else {
                    lastMatchState = state;
                }
            }
            case YET_TO_BE_MATCHED -> {
                MatchState state = unitNow.match(element , matchUnit);
                record.add(element);
                if (state == MatchState.MISMATCHED) {
                    rollback();
                }
                else {
                    lastMatchState = state;
                }
            }
            case MATCHED_YET_POTENTIAL -> {
                matchPotential(element);
            }
        }
    }

    private void matchNext(CouplePair<Object , WordParser.WordType> element) {
        SemanticUnit unit;
        for (int index = 0 ; index < unitNow.children.length ; ) {
            unit = unitNow.children[index++];
            CouplePair<MatchState , MatchUnit> result = unit.tryMatch(element);
            if (result.valueA() == MatchState.MISMATCHED) {
                continue;
            }
            if (index != unitNow.children.length) {
                match.record.add(record = new MatchRecord(unitNow , index));
            }
            unitNow = unit;
            match.matchUnits.add(matchUnit = result.valueB());
            record.add(element);
        }
    }

    private void matchPotential(CouplePair<Object , WordParser.WordType> element) {
        MatchState state = unitNow.match(element , matchUnit);
        if (state == MatchState.MISMATCHED) {
            matchNext(element);
        }
        else {
            this.lastMatchState = state;
            record.add(element);
        }
    }

    void reset() {
        unitNow = root;
        lastMatchState = MatchState.MATCHED;
        match = new SemanticMatch();
        matchUnit = null;
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
        for (SemanticUnit leave : leaves) {
            leave.stateLayer = stateLayer;
        }
    }
}