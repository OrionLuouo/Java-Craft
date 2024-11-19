package OrionLuouo.Craft.StructuredDocumentCompiler.exception;

import OrionLuouo.Craft.StructuredDocumentCompiler.SDCException;
import OrionLuouo.Craft.StructuredDocumentCompiler.Statement;

public class WordNotFoundException extends SDCException {
    public WordNotFoundException(String message) {
        super("SDCException-WordNotFound: " + message);
    }

    public static final void notFount(Statement statement , String message) {
        throw new WordNotFoundException("The word not found in the groups in GrammarParser. " + SDCException.state(statement , message));
    }
}
