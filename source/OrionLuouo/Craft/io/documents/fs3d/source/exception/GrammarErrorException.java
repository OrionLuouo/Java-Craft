package OrionLuouo.Craft.io.documents.fs3d.source.exception;

import OrionLuouo.Craft.io.documents.fs3d.FS3DException;

public class GrammarErrorException extends FS3DException {
    public GrammarErrorException(String message) {
        super(message);
    }

    public static GrammarErrorException invalidElement(Statement statement) {
        return new GrammarErrorException("Exception-" + FS3DException.GRAMMAR_ERROR_INVALID_ELEMENT + ": Invalid element \"" + statement.getWord() + "\" in the sentence \"" + statement.getSentence() + ".\nLocation: " + statement.getLocation() + '.');
    }

    public static GrammarErrorException unexpectedError(Statement statement) {
        return new GrammarErrorException("Exception-" + FS3DException.GRAMMAR_ERROR_UNEXPECTED_ERROR + ": Unexpected error." + ".\nLocation: " + statement.getLocation() + ". Sentence: \"" + statement.getSentence() + "\". Word: \"" + statement.getWord() + "\"." );
    }
}