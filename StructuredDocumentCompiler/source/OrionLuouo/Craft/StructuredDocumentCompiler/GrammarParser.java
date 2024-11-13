package OrionLuouo.Craft.StructuredDocumentCompiler;

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

    Compiler compiler;
    Map<WordType , Map<String , ?>> wordGroups;

    GrammarParser(Compiler compiler) {
        wordGroups = new LinkedHashMap<>();
    }

    void word(String word) throws SDCException {
        compiler.wordNow = word;
        Iterator<Map.Entry<WordType , Map<String , ?>>> groupIterator = wordGroups.entrySet().iterator();
        while (groupIterator.hasNext()) {
            Map.Entry<WordType , Map<String , ?>> group = groupIterator.next();
            Iterator<? extends Map.Entry<String, ?>> wordIterator = group.getValue().entrySet().iterator();
            while (wordIterator.hasNext()) {
                Map.Entry<String, ?> wordEntry = wordIterator.next();
                if (word.equals(wordEntry.getKey())) {
                    compiler.semanticRegex.matches(wordEntry.getValue() , group.getKey());
                    return;
                }
            }
        }
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
