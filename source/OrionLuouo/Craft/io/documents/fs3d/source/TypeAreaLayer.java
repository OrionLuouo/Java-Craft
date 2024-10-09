package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;
import OrionLuouo.Craft.io.documents.fs3d.source.exception.GrammarErrorException;
import OrionLuouo.Craft.system.annotation.Unfinished;

import java.util.*;

@Unfinished(state = "Parsers not complete yet.")
public class TypeAreaLayer extends AreaLayer {
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

class TypeArgumentParserAreaLayer extends AreaLayer {
    TypeStatement typeStatement;
    CustomedType customedType;
    boolean commaed;
    TypeStatement argumentType;
    int index;
    Map.Entry<String, CustomedType.Field> entry;
    Set<Map.Entry<String, CustomedType.Field>> set;
    HandleAreaLayer handle;
    Map<Integer , Handle> fieldDefaultValues;

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
                documentStatement.newLayer(handle = new HandleProxyAreaLayer(documentStatement , argumentType.type));
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
        fieldDefaultValues.put(index++ , handle.handle);
        entry = null;
        handle = null;
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
        typeStatement.fieldDefaultValues = new Handle[typeArgumentParserAreaLayer.fieldDefaultValues.size()];
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

/**
 * Temporarily assign a default value to the parent type.
 * e.g. type B <int price> : A <index = increase(0) , id = -1> {}
 */
class TemporarySetDefaultValueLayer extends TypeAreaLayer {
    TypeStatement typeStatement;
    TypeStatement extendedType;
    FS3DType parent;
    TypeArgumentParserAreaLayer typeArgumentParserAreaLayer;
    int index;
    HandleAreaLayer handler;
    Iterator<CouplePair<String, FS3DType>> fieldIterator;

    public TemporarySetDefaultValueLayer(DocumentStatement statement , TypeStatement typeStatement , TypeArgumentParserAreaLayer typeArgumentParserAreaLayer) {
        super(statement , typeStatement);
        this.typeStatement = typeStatement;
        this.typeArgumentParserAreaLayer = typeArgumentParserAreaLayer;
        parent = extendedType.type;
        fieldIterator = parent.getFieldIterator();
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
                documentStatement.coverLayer(handler = new HandleAreaLayer(documentStatement , fieldIterator.next().valueB()));
            }
            case ',' -> {
                if (index != 0) {
                    GrammarErrorException.invalidElement(documentStatement);
                }
                if (handler == null) {
                    fieldIterator.next();
                }
                else {
                    handler = null;
                }
            }
            case '>' -> {
                if (index != 0) {
                    GrammarErrorException.invalidElement(documentStatement);
                }
                documentStatement.retractLayer();
            }
        }
    }

    @Unfinished(state = "The default values' types remain unchecked.")
    @Override
    public void reload() {
        super.reload();
        typeArgumentParserAreaLayer.fieldDefaultValues.put(index , handler.handle);
        index = 0;
    }

    @Override
    public void logout() {
        super.logout();
    }
}

class PreTypeInitialingAreaLayer extends StateLayer {

    PreTypeInitialingAreaLayer(DocumentStatement statement) {
        super(statement);
    }

    @Override
    public void newIdentifier(String identifier) {
        documentStatement.newLayer(new PreTypeArgumentParserAreaLayer(documentStatement , identifier));
    }
}

class PreTypeArgumentParserAreaLayer extends StateLayer {
    String name;

    PreTypeArgumentParserAreaLayer(DocumentStatement statement , String name) {
        super(statement);
        this.name = name;
    }

    @Override
    public void punctuation(char punctuation) {
        if (punctuation == '<') {
            TypeStatement statement = new TypeStatement(name);
            documentStatement.typeStatementMap.put(statement.type , statement);
            documentStatement.newLayer(new TypeArgumentParserAreaLayer(documentStatement , statement));
        }
    }
}