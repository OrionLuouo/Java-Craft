package OrionLuouo.Craft.StructuredDocumentCompiler;

import java.util.List;

public class SemanticMatchUnit {
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
}
