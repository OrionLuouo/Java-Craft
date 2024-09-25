package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.io.documents.fs3d.FS3DType;
import OrionLuouo.Craft.io.documents.fs3d.source.exception.GrammarErrorException;
import OrionLuouo.Craft.system.annotation.Unfinished;

import java.util.*;

public abstract class AreaLayer extends StateLayer {
    AreaLayer(DocumentStatement statement) {
        super(statement);
    }

    public abstract void reload();
    public abstract void logout();
}

@Unfinished(state = "Parsers not complete yet.")
class TypeAreaLayer extends AreaLayer {
    TypeStatement type;
    Map<String , Variable> conflictedVariables;
    Map<String , TypeStatement> conflictedTypes;

    TypeAreaLayer(DocumentStatement statement , TypeStatement type) {
        super(statement);
        this.type = type;
        conflictedVariables = new HashMap<>();
        conflictedTypes = new HashMap<>();
        Set<Map.Entry<String , Variable>> variableSet = statement.variables.entrySet();
        for (Map.Entry<String , Variable> entry : type.variableMap.entrySet()) {
            if (statement.variables.containsKey(entry.getKey())) {
                conflictedVariables.put(entry.getKey(), statement.variables.get(entry.getKey()));
                statement.variables.put(entry.getKey(), Variable.CONFLICTED_VARIABLE);
            }
            else {
                variableSet.add(entry);
            }
        }
        Set<Map.Entry<String , TypeStatement>> typeSet = statement.types.entrySet();
        for (Map.Entry<String , TypeStatement> entry : typeSet) {
            if (statement.types.containsKey(entry.getKey())) {
                conflictedTypes.put(entry.getKey(), statement.types.get(entry.getKey()));
                statement.types.put(entry.getKey() , CustomedType.CONFLICTED_TYPE);
            }
            else {
                typeSet.add(entry);
            }
        }
    }

    @Override
    public void reload() {
    }

    @Override
    public void logout() {
        for (String key : type.variableMap.keySet()) {
            documentStatement.variables.remove(key);
        }
        for (String key : type.typeMap.keySet()) {
            documentStatement.types.remove(key);
        }
        documentStatement.variables.putAll(conflictedVariables);
        documentStatement.types.putAll(conflictedTypes);
    }

    @Override
    public void keyword(int index) {
        switch (index) {
            case GrammarParser.INDEX_TYPE -> {
                documentStatement.newLayer(new PreTypeInitialingAreaLayer(documentStatement));
            }
            case GrammarParser.INDEX_GROUP -> {

            }
        }
    }
}

abstract class FunctionInitializerStateLayer extends AreaLayer {
    List<Handler> arguments;
    FunctionInstance functionInstance;
    HandlerStateLayer handler;

    FunctionInitializerStateLayer(DocumentStatement statement) {
        super(statement);
        statement.stateNow = "Initialing function.";
    }

    abstract TypeStatement[] getStandardArguments();

    @Override
    public void reload() {
        arguments.add(handler.getHandler());
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
            Iterator<Handler> iterator = arguments.iterator();
            for (int index = 0 ; index < standardArguments.length ;) {
                if (!iterator.hasNext()) {
                    wrongArguments();
                }
                if (iterator.next().getType() != standardArguments[index++].getType()) {
                    wrongArguments();
                }
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
        (documentStatement.currentLayer = new HandlerStateLayer(documentStatement)).keyword(index);
    }

    @Override
    public void type(TypeStatement typeStatement) {
        (documentStatement.currentLayer = new HandlerStateLayer(documentStatement)).type(typeStatement);
    }

    @Override
    public void variable(Variable variable) {
        (documentStatement.currentLayer = new HandlerStateLayer(documentStatement)).variable(variable);
    }

    @Override
    public void function(int index) {
        (documentStatement.currentLayer = new HandlerStateLayer(documentStatement)).function(index);
    }

    @Override
    public void token(String token) {
        (documentStatement.currentLayer = new HandlerStateLayer(documentStatement)).token(token);
    }
}

class TypeArgumentParserAreaLayer extends AreaLayer {
    TypeStatement typeStatement;
    CustomedType customedType;
    boolean commaed;
    TypeStatement argumentType;
    int index;
    Map.Entry<String, CustomedType.Field> entry;
    Set<Map.Entry<String, CustomedType.Field>> set;
    HandlerStateLayer handler;
    Map<Integer , Handler> fieldDefaultValues;

    public TypeArgumentParserAreaLayer(DocumentStatement statement, TypeStatement typeStatement) {
        super(statement);
        this.typeStatement = typeStatement;
        customedType = (CustomedType) typeStatement.type;
        set = customedType.fields.entrySet();
        fieldDefaultValues = new HashMap<>();
    }

    @Override
    public void punctuation(char punctuation) {
        switch (punctuation) {
            case ',' -> {
                if (commaed || argumentType == null) {
                    GrammarErrorException.invalidElement(documentStatement);
                }
                commaed = true;
                set.add(entry);
                entry = null;
            }
            case '=' -> {
                if (entry == null) {
                    unexpected();
                }
                documentStatement.newLayer(handler = new HandlerStateLayer(documentStatement));
            }
            case '>' -> {
                if (argumentType != null || commaed || entry != null) {
                    unexpected();
                }
                documentStatement.replaceLayer(new ExtendsTypesAreaLayer(documentStatement));
            }
        }
    }

    @Override
    public void type(TypeStatement typeStatement) {
        if (!commaed || argumentType != null) {
            GrammarErrorException.invalidElement(documentStatement);
        }
        argumentType = typeStatement;
    }

    @Override
    public void newIdentifier(String identifier) {
        if (commaed || argumentType == null) {
            unexpected();
        }
        entry = new AbstractMap.SimpleEntry<>(identifier , new CustomedType.Field(argumentType.type , index++));
        argumentType = null;
    }

    @Override
    public void reload() {
        set.add(entry);
        fieldDefaultValues.put(index++ , handler.getHandler());
        entry = null;
        handler = null;
    }

    @Override
    public void logout() {
    }
}

class ExtendsTypesAreaLayer extends AreaLayer {
    TypeStatement extendedType;
    TemporarySetDefaultValueLayer temporarySetDefaultValueLayer;

    public ExtendsTypesAreaLayer(DocumentStatement statement) {
        super(statement);
        temporarySetDefaultValueLayer = new TemporarySetDefaultValueLayer(statement);
    }

    @Override
    public void reload() {
    }

    @Override
    public void logout() {
    }

    @Override
    public void punctuation(char punctuation) {
        switch (punctuation) {
            case '<' -> {
                documentStatement.coverLayer(temporarySetDefaultValueLayer);
            }
        }
    }
}

class TemporarySetDefaultValueLayer extends AreaLayer {
    public TemporarySetDefaultValueLayer(DocumentStatement statement) {
        super(statement);
    }

    @Override
    public void reload() {

    }

    @Override
    public void logout() {

    }
}