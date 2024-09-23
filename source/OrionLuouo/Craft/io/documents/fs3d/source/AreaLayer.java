package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.io.documents.fs3d.source.exception.GrammarErrorException;
import OrionLuouo.Craft.system.annotation.Unfinished;

import java.util.Iterator;
import java.util.List;

public abstract class AreaLayer extends StateLayer {
    AreaLayer(DocumentStatement statement) {
        super(statement);
    }

    public abstract void reload();
    public abstract void logout();
}

class TypeAreaLayer extends AreaLayer {
    TypeStatement type;

    TypeAreaLayer(DocumentStatement statement , TypeStatement type) {
        super(statement);
        this.type = type;
    }

    @Override
    public void reload() {

    }

    @Override
    public void logout() {

    }
}

abstract class FunctionInitializerStateLayer extends AreaLayer {
    List<Handler> arguments;

    HandlerStateLayer handler;

    FunctionInitializerStateLayer(DocumentStatement statement) {
        super(statement);
    }

    abstract TypeStatement[] getStandardArguments();

    @Override
    public void reload() {
        arguments.add(handler.getHandler());
    }

    @Unfinished
    public void wrongArguments() {
    }

    public abstract void initialize();

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
            Iterator<Handler> iterator = arguments.iterator();
            for (int index = 0 ; index < standardArguments.length ;) {
                if (!iterator.hasNext()) {
                    wrongArguments();
                }
                if (iterator.next().getType() != standardArguments[index++].getType()) {
                    wrongArguments();
                }
            }
            initialize();
        }
        else {
            throw GrammarErrorException.unexpectedError(documentStatement);
        }
    }

    @Override
    public void keyword(int index) {
        (documentStatement.currentLayer = new HandlerStateLayer(documentStatement)).keyword(index);
    }

    @Override
    public void type(TypeStatement typeStatement) {

    }

    @Override
    public void variable(Variable variable) {

    }

    @Override
    public void function(int index) {

    }

    @Override
    public void token(String token) {

    }
}
