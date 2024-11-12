package OrionLuouo.Craft.StructuredDocumentCompiler;

import java.util.ArrayList;
import java.util.List;

public abstract class SemanticUnit {
    List<SemanticUnit> children;
    int regexIndex;

    SemanticUnit() {
        children = new ArrayList<>();
        regexIndex = -1;
    }

    void matches(SemanticMatch match , char punctuation) {
    }

    abstract void matches(SemanticMatch match , Object object , GrammarParser.WordType type);
}