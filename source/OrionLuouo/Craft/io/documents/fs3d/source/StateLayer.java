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
}

abstract class FunctionInitializerStateLayer extends StateLayer {
    List<CouplePair<TypeStatement , String>> arguments;

    FunctionInitializerStateLayer(DocumentStatement statement) {
        super(statement);
    }

    abstract TypeStatement[] getStandardArguments();

    @Unfinished
    public void wrongArguments() {

    }

    public void initialize() {
        
    }

    @Override
    public void punctuation(char punctuation) {
        if (punctuation == '(') {
            return;
        }
        if (punctuation == ',') {
            return;
        }
        if (punctuation == ')') {
            TypeStatement[] standardArguments = getStandardArguments();
            Iterator<CouplePair<TypeStatement , String>> iterator = arguments.iterator();
            for (int index = 0 ; index < standardArguments.length ;) {
                if (!iterator.hasNext()) {
                    wrongArguments();
                }
                if (iterator.next().valueA() != standardArguments[index++]) {
                    wrongArguments();
                }
            }
            initialize();
        }
        else {
            unexpected();
        }
    }

    @Override
    public void type(TypeStatement typeStatement) {
        arguments.add(new CouplePair<>(typeStatement, typeStatement.getType().getName()));
    }
}
