package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.io.documents.fs3d.FS3DException;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;
import OrionLuouo.Craft.io.documents.fs3d.source.exception.GrammarErrorException;
import OrionLuouo.Craft.system.annotation.Unfinished;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StateLayer {
    DocumentStatement documentStatement;

    StateLayer(DocumentStatement statement) {
        documentStatement = statement;
    }

    void unexpected() {
        GrammarErrorException.unexpectedError(documentStatement);
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

class WordOperatorCheckStateLayer extends StateLayer {
    HandleAreaLayer handleAreaLayer;
    char operator;

    WordOperatorCheckStateLayer(DocumentStatement statement , HandleAreaLayer handleAreaLayer , char operator) {
        super(statement);
        this.handleAreaLayer = handleAreaLayer;
        this.operator = operator;
    }

    @Override
    public void punctuation(char punctuation) {
        if (punctuation == operator) {
            handleAreaLayer.parseSource();
        }
        else {
            unexpected();
        }
    }
}