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
    HandlerAreaLayer handler;

    FunctionInitializerStateLayer(DocumentStatement statement) {
        super(statement);
        statement.stateNow = "Initialing function.";
    }

    abstract TypeStatement[] getStandardArguments();

    @Override
    public void reload() {
        arguments.add(handler.handler);
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
        handler = new HandlerAreaLayer(documentStatement);
        handler.keyword(index);
        documentStatement.coverLayer(handler);
    }

    @Override
    public void type(TypeStatement typeStatement) {
        handler = new HandlerAreaLayer(documentStatement);
        handler.type(typeStatement);
        documentStatement.coverLayer(handler);
    }

    @Override
    public void variable(Variable variable) {
        handler = new HandlerAreaLayer(documentStatement);
        handler.variable(variable);
        documentStatement.coverLayer(handler);
    }

    @Override
    public void function(int index) {
        handler = new HandlerAreaLayer(documentStatement);
        handler.function(index);
        documentStatement.coverLayer(handler);
    }

    @Override
    public void token(String token) {
        handler = new HandlerAreaLayer(documentStatement);
        handler.token(token);
        documentStatement.coverLayer(handler);
    }
}

@Unfinished
class HandlerAreaLayer extends AreaLayer {
    HandlerAreaLayer handlerAreaLayer;
    Handler handler;

    HandlerAreaLayer(DocumentStatement statement) {
        super(statement);
    }

    @Override
    public void reload() {

    }

    @Override
    public void logout() {
    }


    @Override
    public void keyword(int index) {
        switch (index) {
            case GrammarParser.INDEX_INITIALIZER -> {
                handler = new InitializerHandler(documentStatement.thisType.type);
                documentStatement.retractLayer();
            }
            case GrammarParser.INDEX_NULL -> {
                handler = new NullHandler((CustomedType) documentStatement.thisType.type);
                documentStatement.retractLayer();
            }
        }
    }

    @Unfinished
    @Override
    public void punctuation(char punctuation) {
        switch (punctuation) {
            case ',' -> {
                documentStatement.retractLayer();
            }
            case ';' -> {
                documentStatement.retractLayer();
            }
            case '+' -> {

            }
            case '-' -> {

            }
            case '*' -> {

            }
            case '/' -> {

            }
            case '%' -> {

            }
            case '<' -> {

            }
            case '>' -> {

            }
            case '^' -> {

            }
            case '|' -> {

            }
            case '&' -> {

            }
            case '[' -> {

            }
        }
    }

    @Override
    public void type(TypeStatement typeStatement) {
        documentStatement.coverLayer(new TypeHandlerAreaLayer(documentStatement , typeStatement));
    }
}

@Unfinished
class TypeHandlerAreaLayer extends HandlerAreaLayer {
    TypeStatement typeStatement;
    boolean dotted;

    TypeHandlerAreaLayer(DocumentStatement statement , TypeStatement typeStatement) {
        super(statement);
        this.typeStatement = typeStatement;
        dotted = false;
    }

    @Override
    public void punctuation(char punctuation) {
        switch (punctuation) {
            case '.' -> {
                dotted = true;
            }
        }
    }
}

@Unfinished(state = "Maybe extends HandlerAreaLayer.")
class InstanceHandlerAreaLayer extends AreaLayer {
    InstanceHandlerAreaLayer(DocumentStatement statement) {
        super(statement);
    }

    @Override
    public void reload() {

    }

    @Override
    public void logout() {

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
    HandlerAreaLayer handler;
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
                documentStatement.newLayer(handler = new HandlerAreaLayer(documentStatement));
            }
            case '>' -> {
                if (argumentType != null || commaed || entry != null) {
                    unexpected();
                }
                documentStatement.replaceLayer(new ExtendsTypesAreaLayer(documentStatement , typeStatement , this));
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
        fieldDefaultValues.put(index++ , handler.handler);
        entry = null;
        handler = null;
    }

    @Override
    public void logout() {
    }
}

class ExtendsTypesAreaLayer extends AreaLayer {
    TypeArgumentParserAreaLayer typeArgumentParserAreaLayer;
    TypeStatement typeStatement;
    TypeStatement extendedType;
    TemporarySetDefaultValueLayer temporarySetDefaultValueLayer;
    List<FS3DType> parents;


    public ExtendsTypesAreaLayer(DocumentStatement statement , TypeStatement typeStatement , TypeArgumentParserAreaLayer typeArgumentParserAreaLayer) {
        super(statement);
        this.typeStatement = typeStatement;
        this.typeArgumentParserAreaLayer = typeArgumentParserAreaLayer;
        temporarySetDefaultValueLayer = new TemporarySetDefaultValueLayer(statement , typeStatement , typeArgumentParserAreaLayer);
        parents = new LinkedList<>();
    }

    @Override
    public void reload() {
    }

    /**
     * To finish the construction of the type object.
     */
    @Override
    public void logout() {
        CustomedType type = ((CustomedType) typeStatement.type);
        type.parents = new FS3DType[parents.size()];
        Iterator<FS3DType> iterator = parents.iterator();
        for (int index = 0 ; index < parents.size() ;) {
            type.parents[index++] = iterator.next();
        }
        typeStatement.fieldDefaultValues = new Handler[typeArgumentParserAreaLayer.fieldDefaultValues.size()];
        for (var entry : typeArgumentParserAreaLayer.fieldDefaultValues.entrySet()) {
            typeStatement.fieldDefaultValues[entry.getKey()] = entry.getValue();
        }
    }

    @Override
    public void punctuation(char punctuation) {
        switch (punctuation) {
            case '<' -> {
                temporarySetDefaultValueLayer.extendedType = extendedType;
                documentStatement.coverLayer(temporarySetDefaultValueLayer);
            }
            case ',' -> {
                typeArgumentParserAreaLayer.set.addAll(((CustomedType) extendedType.type).fields.entrySet());
                extendedType = null;
            }
            case '{' -> {
                documentStatement.retractLayer();
            }
        }
    }

    @Override
    public void type(TypeStatement typeStatement) {
        extendedType = typeStatement;
        parents.add(typeStatement.type);
        int fieldIndex = typeArgumentParserAreaLayer.index - 2;
        for (int index = 0 ; index < typeStatement.fieldDefaultValues.length ;) {
            if (typeStatement.fieldDefaultValues[index++] != null) {
                typeArgumentParserAreaLayer.fieldDefaultValues.put(index + fieldIndex , typeStatement.fieldDefaultValues[index]);
            }
        }
    }
}

class TemporarySetDefaultValueLayer extends TypeAreaLayer {
    TypeStatement typeStatement;
    TypeStatement extendedType;
    FS3DType parent;
    TypeArgumentParserAreaLayer typeArgumentParserAreaLayer;
    int index;
    HandlerAreaLayer handler;

    public TemporarySetDefaultValueLayer(DocumentStatement statement , TypeStatement typeStatement , TypeArgumentParserAreaLayer typeArgumentParserAreaLayer) {
        super(statement , typeStatement);
        this.typeStatement = typeStatement;
        this.typeArgumentParserAreaLayer = typeArgumentParserAreaLayer;
        parent = extendedType.type;
    }

    @Override
    public void variable(Variable variable) {
        try {
            String variableName = (String) documentStatement.wordNow;
            if ((index = parent.getFieldIndex(variableName)) != -1) {
                index += typeArgumentParserAreaLayer.set.size();
            }
        } catch (Exception e) {
            GrammarErrorException.unexpectedError(documentStatement);
        }
    }

    @Override
    public void punctuation(char punctuation) {
        switch (punctuation) {
            case '=' -> {
                if (index == 0) {
                    GrammarErrorException.invalidElement(documentStatement);
                }
                documentStatement.coverLayer(handler = new HandlerAreaLayer(documentStatement));
            }
            case ',' -> {
                if (index != 0) {
                    GrammarErrorException.invalidElement(documentStatement);
                }
                handler = null;
            }
            case '>' -> {
                if (index != 0) {
                    GrammarErrorException.invalidElement(documentStatement);
                }
                documentStatement.retractLayer();
            }
        }
    }

    @Override
    public void reload() {
        super.reload();
        typeArgumentParserAreaLayer.fieldDefaultValues.put(index , handler.handler);
        index = 0;
    }

    @Override
    public void logout() {
        super.logout();
    }
}