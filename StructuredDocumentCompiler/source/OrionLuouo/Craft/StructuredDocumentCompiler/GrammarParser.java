package OrionLuouo.Craft.StructuredDocumentCompiler;

import OrionLuouo.Craft.StructuredDocumentCompiler.exception.WordNotFoundException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class GrammarParser {
    public record WordType(String name) {
        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    public static final WordType TYPE_STRING = new WordType("string")
            , TYPE_INTEGER = new WordType("integer")
            , TYPE_FLOAT = new WordType("float")
            , TYPE_BOOLEAN = new WordType("boolean");

    public static final Map<String , ?> GROUP_STRING = new HashMap<String , String>() {
        @Override
        public String get(Object key) {
            return (String) key;
        }
    } , GROUP_INTEGER = new HashMap<String , Integer>() {
        @Override
        public Integer get(Object key) {
            try {
                return Integer.parseInt((String) key);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    } , GROUP_FLOAT = new HashMap<String , Float>() {
        @Override
        public Float get(Object key) {
            try {
                return Float.parseFloat((String) key);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    } , GROUP_BOOLEAN = new HashMap<String , Boolean>() {
        @Override
        public Boolean get(Object key) {
            try {
                return Boolean.parseBoolean((String) key);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    };

    Compiler compiler;
    Map<WordType , Map<String , ?>> wordGroups;

    GrammarParser(Compiler compiler) {
        wordGroups = new LinkedHashMap<>();
    }

    void word(String word) throws SDCException {
        compiler.wordNow = word;
        Iterator<Map.Entry<WordType , Map<String , ?>>> groupIterator = wordGroups.entrySet().iterator();
        while (groupIterator.hasNext()) {
            Map.Entry<WordType , Map<String , ?>> entry = groupIterator.next();
            Object value = entry.getValue().get(word);
            if (value != null) {
                compiler.semanticRegex.matches(value , entry.getKey());
                return;
            }
        }
        WordNotFoundException.notFount(compiler , "May need to add the String group to catch new or unexpected words.");
    }

    public WordType getType(String name) {
        return new WordType(name);
    }

    /**
     * The first time a type is set,
     * it will be registered in the GrammarParser.
     * And the parsing order is decided by the registration.
     * That means if you registered type A first,
     * the GrammarParser will check if a word belongs to type A first,
     * too.
     * And importantly,
     * the order is unchangeable.
     * So you'd better organise it foresightedly and fixedly.
     *
     * @param wordGroup The map that contains words in the group,
     *                  and the corresponding values.
     *                  When the input matches any word in the group,
     *                  the GrammarParser will send the value it points to to the next-level compiler.
     *                  So if the group needs update,
     *                  like it's group Variable and a new variable is defined,
     *                  just add it to the Map<String , ?> is enough.
     */
    public void setWordGroup(Map<String , ?> wordGroup , WordType type) {
        wordGroups.put(type, wordGroup);
    }
}
