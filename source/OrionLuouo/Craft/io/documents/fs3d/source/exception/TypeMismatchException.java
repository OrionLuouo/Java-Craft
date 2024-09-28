package OrionLuouo.Craft.io.documents.fs3d.source.exception;

import OrionLuouo.Craft.io.documents.fs3d.FS3DException;

import static OrionLuouo.Craft.io.documents.fs3d.source.exception.Statement.*;

public class TypeMismatchException extends FS3DException {
    public TypeMismatchException(String message) {
        super(message);
    }

    public static TypeMismatchException assignWrongObject(Statement statement) {
        throw new TypeMismatchException("Exception-" + FS3DException.TYPE_MISMATCH_ASSIGN_WRONG_OBJECT + ": Trying to assign an object of a wrong type, as \"" + statement.getSentence() + "\".\n" + contextLocation(statement));
    }
}
