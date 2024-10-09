package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;
import OrionLuouo.Craft.io.documents.fs3d.source.exception.GrammarErrorException;
import OrionLuouo.Craft.io.documents.fs3d.source.exception.TypeMismatchException;
import OrionLuouo.Craft.system.annotation.Unfinished;

import java.util.*;

public abstract class AreaLayer extends StateLayer {
    AreaLayer(DocumentStatement statement) {
        super(statement);
    }

    public abstract void reload();
    public abstract void logout();
}

abstract class FunctionInitializerStateLayer extends AreaLayer {
    List<Handle> arguments;
    FunctionInstance functionInstance;
    HandleAreaLayer handler;

    abstract FS3DType getArgumentType(int index);

    FunctionInitializerStateLayer(DocumentStatement statement) {
        super(statement);
        statement.stateNow = "Initialing function.";
    }

    abstract TypeStatement[] getStandardArguments();

    @Override
    public void reload() {
        arguments.add(handler.handle);
    }

    public void wrongArguments() {
        GrammarErrorException.wrongArgument(documentStatement);
    }

    public abstract FunctionInstance initialize();

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
            Iterator<Handle> iterator = arguments.iterator();
            for (int index = 0 ; index < standardArguments.length ;) {
                if (!iterator.hasNext()) {
                    wrongArguments();
                }
                FS3DType type = iterator.next().getType();
                if (!(type == standardArguments[index].getType() || type.isParentOf(standardArguments[index].getType()))) {
                    wrongArguments();
                }
                index++;
            }
            functionInstance = initialize();
            documentStatement.retractLayer();
        }
        else {
            GrammarErrorException.unexpectedError(documentStatement);
        }
    }

    @Override
    public void keyword(int index) {
        handler = new HandleAreaLayer(documentStatement , getArgumentType(arguments.size()));
        handler.keyword(index);
        documentStatement.coverLayer(handler);
    }

    @Override
    public void type(TypeStatement typeStatement) {
        handler = new HandleAreaLayer(documentStatement , getArgumentType(arguments.size()));
        handler.type(typeStatement);
        documentStatement.coverLayer(handler);
    }

    @Override
    public void variable(Variable variable) {
        handler = new HandleAreaLayer(documentStatement , getArgumentType(arguments.size()));
        handler.variable(variable);
        documentStatement.coverLayer(handler);
    }

    @Override
    public void function(int index) {
        handler = new HandleAreaLayer(documentStatement , getArgumentType(arguments.size()));
        handler.function(index);
        documentStatement.coverLayer(handler);
    }

    @Override
    public void token(String token) {
        handler = new HandleAreaLayer(documentStatement , getArgumentType(arguments.size()));
        handler.token(token);
        documentStatement.coverLayer(handler);
    }
}