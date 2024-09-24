package OrionLuouo.Craft.io.documents.fs3d.source.exception;

import OrionLuouo.Craft.io.documents.fs3d.FS3DException;

public class GrammarErrorException extends FS3DException {
    public GrammarErrorException(String message) {
        super(message);
    }

    static String elementLocation(Statement statement) {
        return "\"" + statement.getWord() + '\"' +  "in the sentence " + '\"' + statement.getSentence() + '\"';
    }

    static String contextLocation(Statement statement) {
        return "Location: " + statement.getLocation() + '.';
    }

    public static GrammarErrorException invalidElement(Statement statement) {
        return new GrammarErrorException("Exception-" + FS3DException.GRAMMAR_ERROR_INVALID_ELEMENT + ": Invalid element " + elementLocation(statement) + ".\n" + contextLocation(statement));
    }

    public static GrammarErrorException unexpectedError(Statement statement) {
        return new GrammarErrorException("Exception-" + FS3DException.GRAMMAR_ERROR_UNEXPECTED_ERROR + ": Unexpected error: " + elementLocation(statement) + ".\n" + contextLocation(statement));
    }

    public static GrammarErrorException wrongArgument(Statement statement) {
        return new GrammarErrorException("Exception-" + FS3DException.GRAMMAR_ERROR_WRONG_ARGUMENTS + ": Wrong arguments of the function: " + elementLocation(statement) + ".\n" + contextLocation(statement));
    }
}