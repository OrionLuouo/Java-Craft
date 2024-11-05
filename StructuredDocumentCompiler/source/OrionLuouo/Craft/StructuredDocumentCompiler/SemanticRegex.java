package OrionLuouo.Craft.StructuredDocumentCompiler;

public interface SemanticRegex {
    static SemanticRegex compile(String regex) {

        return null;
    }

    void unit(Object unit);

    void punctuation(char punctuation);
}

class SemanticUnit {
    SemanticUnit brother , child;
    int regexIndex , branchIndex;
}