package OrionLuouo.Craft.StructuredDocumentCompiler;

import java.util.List;

public abstract class SemanticUnit {
    List<SemanticUnit> children;
    int regexIndex;

    SemanticUnit() {
        regexIndex = -1;
    }

    void matches(SemanticMatch match , char punctuation) {

    }

    void matches(SemanticMatch match , Object object , GrammarParser.WordType type) {

    }

    boolean isPotential() {
        return false;
    }

    abstract SemanticMatchUnit.MakeupUnit makeup(SemanticMatchUnit matchUnit);
}
