package OrionLuouo.Craft.StructuredDocumentCompiler.exception;

import OrionLuouo.Craft.StructuredDocumentCompiler.SDCException;
import OrionLuouo.Craft.StructuredDocumentCompiler.Statement;

public class SemanticMismatchException extends SDCException {
    SemanticMismatchException(String message) {
        super("SDCException-SemanticMismatch: " + message);
    }

    public static final void mismatch(Statement statement , String message) {
        throw new SemanticMismatchException("The content not corresponding to the regex. Word: " + statement.getWord() + ", Line: " + statement.getLineCount() + ", At: " + statement.getLineCharacterCount() + ", Characters in Total: " + statement.getCharacterCount() + ".\r\tStatement: " + message);
    }
}
