package OrionLuouo.Craft.logic.input.SDP;

import OrionLuouo.Craft.system.annotation.Unfinished;

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
public class GrammarParser {
    void input(Object element , WordParser.WordType type) {
        System.out.println("GrammarParser accepted: <" + element.getClass() + ':' + element + ", " + type.name() + ">");
    }
}
