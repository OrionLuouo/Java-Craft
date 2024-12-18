package OrionLuouo.Craft.logic.input.SDP;

import OrionLuouo.Craft.data.Array;
import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.data.container.collection.sequence.List;
import OrionLuouo.Craft.system.annotation.Unfinished;

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
    StructuredDocumentParser structuredDocumentParser;
    SemanticUnit root , unitNow;
    MatchState lastMatchState;
    SemanticUnit[] leaves;
    SemanticMatch match;
    MatchUnit matchUnit;

    private void end() {
        match.record = null;
        match.branch.sentence(match);
        match = null;
    }

    private void rollback() {
        List<CouplePair<Object , WordParser.WordType>> record = match.record;
        match.rollback();
        end();
        record.iterate(element -> {
            structuredDocumentParser.semanticRegex.input(element.valueA() , element.valueB());
        });
    }

    void input(Object value , WordParser.WordType type) {
        CouplePair<Object , WordParser.WordType> element = new CouplePair<>(value, type);
        switch (lastMatchState) {
            case MATCHED -> {
                matchNext(element);
            }
            case MATCHED_YET_POTENTIAL -> {
                matchPotential(element);
            }
            case YET_TO_BE_MATCHED -> {
                match.record.add(element);
                lastMatchState = unitNow.match(element , matchUnit, this);
            }
        }
    }

    private void matchNext(CouplePair<Object , WordParser.WordType> element) {
        if (unitNow.children == null) {
            end();
            structuredDocumentParser.semanticRegex.input(element.valueA() , element.valueB());
            return;
        }
        CouplePair<MatchState , MatchUnit> match;
        for (int index = 0 ; index < unitNow.children.length ; ) {
            SemanticUnit unit = unitNow.children[index++];
            if ((match = unit.tryMatch(element)).valueA() == MatchState.MISMATCHED) {
                continue;
            }
            this.match.matchUnits.add(matchUnit = match.valueB());
            if (unitNow.stateLayer != null) {
                this.match.branch = unitNow.stateLayer;
                this.match.branchCount = 0;
                this.match.record.clean();
            }
            this.match.record.add(element);
            return;
        }
        rollback();
    }

    private void matchPotential(CouplePair<Object , WordParser.WordType> element) {
        MatchState state = unitNow.match(element , matchUnit, this);
        if (state != MatchState.MISMATCHED) {
            match.record.add(element);
            return;
        }
        matchNext(element);
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