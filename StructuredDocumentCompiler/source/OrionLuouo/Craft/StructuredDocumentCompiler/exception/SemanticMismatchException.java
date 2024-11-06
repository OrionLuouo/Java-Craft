package OrionLuouo.Craft.StructuredDocumentCompiler.exception;

import OrionLuouo.Craft.StructuredDocumentCompiler.SDCException;

public class SemanticMismatchException extends SDCException {
    public SemanticMismatchException(String message) {
        super("SDCException-SemanticMismatch: " + message);
    }
}
