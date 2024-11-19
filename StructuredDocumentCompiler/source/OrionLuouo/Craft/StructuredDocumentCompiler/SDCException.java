package OrionLuouo.Craft.StructuredDocumentCompiler;

public class SDCException extends RuntimeException {
    public SDCException(String message) {
        super(message);
    }

    public static final String state(Statement statement , String message) {
        return "Word: " + statement.getWord() + ", Line: " + statement.getLineCount() + ", At: " + statement.getLineCharacterCount() + ", Characters in Total: " + statement.getCharacterCount() + ".\r\tStatement: " + message;
    }
}
