package OrionLuouo.Craft.StructuredDocumentCompiler.exception;

import OrionLuouo.Craft.StructuredDocumentCompiler.SDCException;

public class IncompleteRegexException extends SDCException {
    public IncompleteRegexException(String message) {
        super("SDCException-IncompleteRegex: " + message);
    }
}
