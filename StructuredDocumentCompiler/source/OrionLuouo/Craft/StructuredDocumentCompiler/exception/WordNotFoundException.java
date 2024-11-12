package OrionLuouo.Craft.StructuredDocumentCompiler.exception;

import OrionLuouo.Craft.StructuredDocumentCompiler.SDCException;

public class WordNotFoundException extends SDCException {
    public WordNotFoundException(String message) {
        super("SDCException-WordNotFound: " + message);
    }
}
