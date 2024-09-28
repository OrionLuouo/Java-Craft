package OrionLuouo.Craft.io.documents.fs3d.source.exception;

import OrionLuouo.Craft.io.documents.fs3d.FS3DException;

import static OrionLuouo.Craft.io.documents.fs3d.source.exception.Statement.*;

public class GrammarErrorException extends FS3DException {
    public GrammarErrorException(String message) {
        super(message);
    }

    public static GrammarErrorException invalidElement(Statement statement) {
        throw new GrammarErrorException("Exception-" + FS3DException.GRAMMAR_ERROR_INVALID_ELEMENT + ": Invalid element " + elementLocation(statement) + ".\n" + contextLocation(statement));
    }

    public static GrammarErrorException unexpectedError(Statement statement) {
        throw new GrammarErrorException("Exception-" + FS3DException.GRAMMAR_ERROR_UNEXPECTED_ERROR + ": Unexpected error: " + elementLocation(statement) + ".\n" + contextLocation(statement));
    }

    public static GrammarErrorException wrongArgument(Statement statement) {
        throw new GrammarErrorException("Exception-" + FS3DException.GRAMMAR_ERROR_WRONG_ARGUMENTS + ": Wrong arguments of the function: " + elementLocation(statement) + ".\n" + contextLocation(statement));
    }
}