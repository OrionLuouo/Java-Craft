package OrionLuouo.Craft.logic.input.SDP;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.data.container.collection.sequence.ChunkChainList;
import OrionLuouo.Craft.data.container.collection.sequence.List;

import java.util.*;

public class WordParser {
    /**
     * Attention!<p>
     * The objects of WordType will be compared directly by operator == at functioning of the parser.<p>
     * So make sure that the types used with the same name in a single parsing system are actually the very same one instance.
     */
    public record WordType(String name) {
    }

    public interface FreeTypeChecker {
        Object of(String word);
    }

    void setRegexLayer(RegexLayer layer) {
        this.regexLayer = layer;
    }

    public static final WordType TYPE_STRING = new WordType("String")
            , TYPE_IDENTIFIER = new WordType("Identifier")
            , TYPE_PUNCTUATION = new WordType("Punctuation")
            , TYPE_INTEGER = new WordType("Integer")
            , TYPE_FLOAT = new WordType("Float")
            , TYPE_BOOLEAN = new WordType("Boolean")
            , TYPE_DATE = new WordType("Date")
            , TYPE_TIMESTAMP = new WordType("Timestamp");

    public static final FreeTypeChecker CHECKER_NULL = word -> null
            , CHECKER_STRING = word -> word
            , CHECKER_IDENTIFIER = word -> {
        char cache = word.charAt(0);
        if (cache < 'A' || cache > 'z') {
            throw new SDPException("SDPException-InvalidIdentifier: The identifier \"" + word + "\" doesn't follow the format.");
        }
        return word;
    }
            , CHECKER_PUNCTUATION = word -> {
                if (word.length() != 1) {
                    return null;
                }
                char character = word.charAt(0);
                return (character < BlankStreamParser.DEFAULT_DELIMITERS.length && BlankStreamParser.DEFAULT_DELIMITERS[character]) ? character : null;
            }
            , CHECKER_INTEGER = word -> {
        try {
            return Integer.parseInt(word);
        } catch (NumberFormatException _) {
            return null;
        }
    }
            , CHECKER_FLOAT = word -> {
        try {
            return Float.parseFloat(word);
        } catch (NumberFormatException _) {
            return null;
        }
    }
            , CHECKER_BOOLEAN = word -> {
        return word.equals("true") ? Boolean.TRUE : word.equals("false") ? false : null;
    }
            , CHECKER_DATE = word -> {
        try {
            return new Date(word);
        } catch (IllegalArgumentException _) {
            return null;
        }
    }
            , CHECKER_TIMESTAMP = word -> {
        try {
            return Long.parseLong(word);
        } catch (NumberFormatException _) {
            return null;
        }
    };

    Map<String , WordType> types;
    List<CouplePair<WordType , FreeTypeChecker>> freeTypes;
    Map<WordType , Map<String , Object>> wordGroups;
    RegexLayer regexLayer;

    WordParser() {
        types = new HashMap<>();
        wordGroups = new HashMap<>();
        freeTypes = new ChunkChainList<>();
    }

    public WordType getType(String name) {
        return types.get(name);
    }

    /**
     * To add a normal type into the pool.<p>
     * Words potentially of the normal type being parsed will match with words in the corresponding group.<p>
     * If it mismatches,<p>
     * then to match with another group.<p>
     * When mismatched all the normal types,<p>
     * it'll be determined as of a free type.
     */
    public void addType(String name, WordType type) {
        types.put(name, type);
    }

    public void addFreeType(WordType type , FreeTypeChecker checker) {
        freeTypes.add(new CouplePair<>(type , checker));
        types.put(type.name(), type);
    }

    public void addWord(String word , Object element , WordType type) {
        wordGroups.get(type).put(word , element);
    }

    /**
     * Add the basic mark types to the type pool.<p>
     * That includes String, Integer, Float and so on frequently-used marks.
     */
    public void addBasicMarkTypes() {
        addFreeType(TYPE_PUNCTUATION , CHECKER_PUNCTUATION);
        addFreeType(TYPE_INTEGER , CHECKER_INTEGER);
        addFreeType(TYPE_FLOAT , CHECKER_FLOAT);
        addFreeType(TYPE_TIMESTAMP , CHECKER_TIMESTAMP);
        addFreeType(TYPE_BOOLEAN , CHECKER_BOOLEAN);
        addFreeType(TYPE_DATE , CHECKER_DATE);
        addFreeType(TYPE_IDENTIFIER , CHECKER_IDENTIFIER);
        addFreeType(TYPE_STRING , CHECKER_NULL);
    }

    void input(String word) {
        Iterator<Map.Entry<WordType, Map<String, Object>>> iterator = wordGroups.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<WordType, Map<String, Object>> entry = iterator.next();
            Object element = entry.getValue().get(word);
            if (element != null) {
                regexLayer.input(element , entry.getKey());
                return;
            }
        }
        OrionLuouo.Craft.data.Iterator<CouplePair<WordType , FreeTypeChecker>> freeTypeIterator = freeTypes.iterator();
        while (freeTypeIterator.hasNext()) {
            CouplePair<WordType , FreeTypeChecker> pair = freeTypeIterator.next();
            Object element = pair.valueB().of(word);
            if (element != null) {
                regexLayer.input(element , pair.valueA());
                return;
            }
        }
        throw new SDPException("SDCException-UnknownWord: \"" + word +"\".");
    }
}
