package OrionLuouo.Craft.io.documents.fs3d;

public class FS3DException extends RuntimeException {
    public static final int GRAMMAR_ERROR_INVALID_ELEMENT = 1
            , GRAMMAR_ERROR_UNEXPECTED_ERROR = 2
            , GRAMMAR_ERROR_WRONG_ARGUMENTS = 3
            , TYPE_MISMATCH_ASSIGN_WRONG_OBJECT = 4;

    public FS3DException(String message) {
        super(message);
    }
}
