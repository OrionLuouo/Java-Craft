package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

import java.util.HashMap;
import java.util.Map;

public class GrammarParser {
    public static final Map<String , Integer> KEYWORDS = new HashMap<>();

    static {
        KEYWORDS.put("type" , 0);
        KEYWORDS.put("group" , 1);
        KEYWORDS.put("function" , 16);
        KEYWORDS.put("virtual" , 17);
        KEYWORDS.put("void" , 32);
        KEYWORDS.put("initializer" , 33);
        KEYWORDS.put("null" , 34);
        KEYWORDS.put("int" , 64);
        KEYWORDS.put("integer" , 64);
        KEYWORDS.put("float" , 65);
        KEYWORDS.put("boolean" , 66);
        KEYWORDS.put("bool" , 66);
        KEYWORDS.put("char" , 67);
        KEYWORDS.put("character" , 68);
        KEYWORDS.put("string" , 69);
        KEYWORDS.put("date" , 70);
        KEYWORDS.put("timestamp" , 71);
        KEYWORDS.put("byte" , 72);
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