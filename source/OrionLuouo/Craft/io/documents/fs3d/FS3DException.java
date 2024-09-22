package OrionLuouo.Craft.io.documents.fs3d;

public class FS3DException extends RuntimeException {
    public static final int GRAMMAR_ERROR_INVALID_ELEMENT = 1
            , GRAMMAR_ERROR_UNEXPECTED_ERROR = 2;

    public FS3DException(String message) {
        super(message);
    }
}
