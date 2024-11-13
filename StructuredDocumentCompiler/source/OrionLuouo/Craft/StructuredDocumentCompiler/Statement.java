package OrionLuouo.Craft.StructuredDocumentCompiler;

public interface Statement {
    String getStatement();
    int getCharacterCount();
    int getLineCount();
    int getLineCharacterCount();
    String getWord();
}
