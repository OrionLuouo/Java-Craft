package OrionLuouo.Craft.logic.input.SDP;

import OrionLuouo.Craft.data.Array;
import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.data.container.collection.sequence.ChunkChainList;
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
    StructuredDocumentParser structuredDocumentParser;
    StateLayer stateLayer;
    SemanticUnit[] units;
    /*
     * Value A is the point that rollback to.
     * Value B is the destination unit.
     * If A == -1,
     * not rollback;
     * or -2,
     * this is the new record point;
     * or -3,
     * the regex mismatched.
     */
    int index;
    List<CouplePair<Object , WordParser.WordType>> record;
    List<Integer> recordPoints;
    Operation[] matchOperations , mismatchOperations;

    void input(Object value , WordParser.WordType type) {
        CouplePair<Object , WordParser.WordType> element = new CouplePair<>(value, type);
        boolean matched = units[index].match(element);
        record.add(element);
        Operation operation = matched ? matchOperations[index] : mismatchOperations[index];
        switch (operation.operator()) {
            case MATCH -> {

            }
            case MISMATCH -> {

            }
            case ROLLBACK -> {

            }
            case CONTINUE -> {
                
            }
        }
    }

    void reset() {
        index = 0;
        record = new ChunkChainList<>();
        recordPoints = new ChunkChainList<>();
    }

    public void setStateLayer(StateLayer stateLayer) {
        this.stateLayer = stateLayer;
    }
}