package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.io.documents.fs3d.FS3DException;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;
import OrionLuouo.Craft.io.documents.fs3d.source.exception.GrammarErrorException;
import OrionLuouo.Craft.system.annotation.Unfinished;

import java.util.Iterator;
import java.util.List;

public class StateLayer {
    DocumentStatement documentStatement;

    StateLayer(DocumentStatement statement) {
        documentStatement = statement;
    }

    void unexpected() {
        throw GrammarErrorException.unexpectedError(documentStatement);
    }
    void punctuation(char punctuation) {
        unexpected();
    }
    void keyword(int index) {
        unexpected();
    }
    void newIdentifier(String identifier) {
        unexpected();
    }
    void type(TypeStatement typeStatement) {
        unexpected();
    }
    void variable(Variable variable) {
        unexpected();
    }
    void function(int index) {
        unexpected();
    }
    void token(String token) {
        unexpected();
    }
}

class HandlerStateLayer extends StateLayer {

    HandlerStateLayer(DocumentStatement statement) {
        super(statement);
    }

    public Handler getHandler() {
        return null;
    }
}