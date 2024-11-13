package OrionLuouo.Craft.StructuredDocumentCompiler.exception;

import OrionLuouo.Craft.StructuredDocumentCompiler.SDCException;
import OrionLuouo.Craft.StructuredDocumentCompiler.Statement;

public class SemanticMismatchException extends SDCException {
    public SemanticMismatchException(String message) {
        super("SDCException-SemanticMismatch: " + message);
    }

    public static final void notCorrespond(Statement statement) {
        throw new SemanticMismatchException("SDCException-SemanticMismatch: The content not corresponding to the regex. Word: " + statement.getWord() + ", at line: " + statement.getLineCount() + ", character: " + statement.getLineCharacterCount() + ", characters in total: " + statement.getCharacterCount() + "\n\tStatement: " + statement.getStatement());
    }
}
