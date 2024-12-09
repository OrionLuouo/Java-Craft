package OrionLuouo.Craft.logic.input.SDP;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.data.container.collection.sequence.List;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class WordParser {
    /**
     * Attention!
     * The objects of WordType will be compared directly by operator == at functioning of the parser.
     * So make sure that the types used with the same name in a single parsing system are actually the very same one instance.
     *
     * @param name
     */
    public record WordType(String name) {
    }

    public interface FreeTypeChecker {
        boolean of(char character);
        boolean of(String word);
    }

    public static final WordType TYPE_STRING = new WordType("String")
            , TYPE_PUNCTUATION = new WordType("Punctuation");

    Map<String , WordType> types;
    List<CouplePair<WordType , FreeTypeChecker>> freeTypes;
    Map<WordType , Set<String>> wordGroups;

    WordParser() {
        types = new HashMap<>();
        wordGroups = new HashMap<>();
    }

    public WordType getType(String name) {
        return types.get(name);
    }

    /**
     * To add a normal type into the pool.
     * Words potentially of the normal type being parsed will match with words in the corresponding group.
     * If it mismatches,
     * then to match with another group.
     * When mismatched all the normal types,
     * it'll be determined as of a free type.
     */
    public void addType(String name, WordType type) {
        types.put(name, type);
    }

    public void addFreeType(WordType type , FreeTypeChecker checker) {
        freeTypes.add(new CouplePair<>(type , checker));
    }

    public void addWord(String word , WordType type) {
        wordGroups.get(type).add(word);
    }

    void input(String word) {
        Iterator<Map.Entry<WordType , Set<String>>> iterator = wordGroups.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<WordType , Set<String>> entry = iterator.next();
            if (entry.getValue().contains(word)) {

                return;
            }
        }
        OrionLuouo.Craft.data.Iterator<CouplePair<WordType , FreeTypeChecker>> freeTypeIterator = freeTypes.iterator();
        while (freeTypeIterator.hasNext()) {
            CouplePair<WordType , FreeTypeChecker> pair = freeTypeIterator.next();
            if (pair.valueB().of(word)) {

                return;
            }
        }
        throw new SDPException("SDCException-UnknownWord: \"" + word +"\".");
    }
}
