package OrionLuouo.Craft.StructuredDocumentCompiler;

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

    void word(String word) {

    }

    public WordType getType(String name) {
        return new WordType(name);
    }

    public void setWordGroup(Map<String , ?> wordGroup , WordType type) {
        wordGroups.put(type, wordGroup);
    }
}
