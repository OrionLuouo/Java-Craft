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

    abstract SemanticMatch.MatchState matches(SemanticMatch match , char punctuation);

    abstract SemanticMatch.MatchState matches(SemanticMatch match , Object object , GrammarParser.WordType type);
}