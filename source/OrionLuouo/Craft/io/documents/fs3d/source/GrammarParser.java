package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

import java.util.HashMap;
import java.util.Map;

public class GrammarParser {
    public static final Map<String , Integer> KEYWORDS = new HashMap<>();
    public static final int INDEX_TYPE = 0
            , INDEX_GROUP = 1
            , INDEX_FUNCTION = 16
            , INDEX_VIRTUAL = 17
            , INDEX_VOID = 32
            , INDEX_INITIALIZER = 33
            , INDEX_NULL = 34
            , INDEX_INTEGER = 64
            , INDEX_FLOAT = 65
            , INDEX_BOOLEAN = 66
            , INDEX_CHARACTER = 67
            , INDEX_STRING = 68
            , INDEX_DATE = 69
            , INDEX_TIME_STAMP = 70
            , INDEX_BYTE = 71;

    static {
        KEYWORDS.put("type" , INDEX_TYPE);
        KEYWORDS.put("group" , INDEX_GROUP);
        KEYWORDS.put("function" , INDEX_FUNCTION);
        KEYWORDS.put("virtual" , INDEX_VIRTUAL);
        KEYWORDS.put("void" , INDEX_VOID);
        KEYWORDS.put("initializer" , INDEX_INITIALIZER);
        KEYWORDS.put("null" , INDEX_NULL);
        KEYWORDS.put("int" , INDEX_INTEGER);
        KEYWORDS.put("integer" , INDEX_INTEGER);
        KEYWORDS.put("float" , INDEX_FLOAT);
        KEYWORDS.put("boolean" , INDEX_BOOLEAN);
        KEYWORDS.put("bool" , INDEX_BOOLEAN);
        KEYWORDS.put("char" , INDEX_CHARACTER);
        KEYWORDS.put("character" , INDEX_CHARACTER);
        KEYWORDS.put("string" , INDEX_STRING);
        KEYWORDS.put("date" , INDEX_DATE);
        KEYWORDS.put("timestamp" , INDEX_TIME_STAMP);
        KEYWORDS.put("byte" , INDEX_BYTE);
    }

    DocumentStatement documentStatement;

    GrammarParser(DocumentStatement documentStatement) {
        this.documentStatement = documentStatement;
    }

    void input(String word) {
        Object cache = 0;
        if ((cache = KEYWORDS.get(word)) != null) {
            documentStatement.currentLayer.keyword((int) cache);
            return;
        }
        if ((cache = FunctionInstance.FunctionKernel.FUNCTIONS.get(word)) != null) {
            documentStatement.currentLayer.function(((CouplePair<Class<FunctionInstance> , Integer>)cache).valueB());
            return;
        }
        if ((cache = documentStatement.types.get(word)) != null) {
            documentStatement.currentLayer.type((TypeStatement) cache);
            return;
        }
        if ((cache = documentStatement.variables.get(word)) != null) {
            documentStatement.currentLayer.variable((Variable) cache);
            return;
        }
        documentStatement.currentLayer.newIdentifier(word);
    }

    void input(char punctuation) {
        documentStatement.currentLayer.punctuation(punctuation);
    }
}