package OrionLuouo.Craft.StructuredDocumentCompiler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SemanticMatchUnit {
    /*
    interface MakeupUnit {
        void input(SemanticRegex regex);

        record PunctuationMakeupUnit(char punctuation) implements MakeupUnit {
            @Override
            public void input(SemanticRegex regex) {
                regex.matches(punctuation);
            }
        }

        record WordMakeupUnit(Object value , GrammarParser.WordType type) implements MakeupUnit {
            @Override
            public void input(SemanticRegex regex) {
                regex.matches(value , type);
            }
        }
    }
     */

    public Object get() {
        return null;
    }

    public Iterator<Object> iterator() {
        return new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
    }
}

class ElementSemanticMatchUnit extends SemanticMatchUnit {
    final Object value;

    ElementSemanticMatchUnit(Object value) {
        this.value = value;
    }
}

class ArbitraryElementSemanticMatchUnit extends SemanticMatchUnit {
    final List<Object> values;

    ArbitraryElementSemanticMatchUnit() {
        values = new ArrayList<>();
    }
}

class ArbitraryMergeSemanticMatchUnit extends SemanticMatchUnit {
    List<SemanticMatch> matches;

    ArbitraryMergeSemanticMatchUnit() {
        matches = new ArrayList<>();
    }
}