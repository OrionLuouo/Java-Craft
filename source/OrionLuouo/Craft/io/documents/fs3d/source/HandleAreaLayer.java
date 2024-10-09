package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;
import OrionLuouo.Craft.io.documents.fs3d.source.exception.TypeMismatchException;
import OrionLuouo.Craft.system.annotation.Unfinished;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

interface HandleParser {
    Handle get();
}

public class HandleAreaLayer extends AreaLayer implements HandleParser {
    HandleParser parser;
    Handle handle;
    List<CouplePair<Handle , Integer>> handles;
    /** 0 = integer
     *  1 = float
     *  2 = string
     *  3 = boolean
     *  4 = OTHERS
     */
    int typeIndex , operator;
    final FS3DType type;

    HandleAreaLayer(DocumentStatement statement , FS3DType fs3DType) {
        super(statement);
        handles = new LinkedList<>();
        type = fs3DType;
        parseSource();
    }

    void parseSource() {
        SourceHandlerStateLayer sourceHandlerStateLayer = new SourceHandlerStateLayer(documentStatement , type);
        parser = sourceHandlerStateLayer;
        documentStatement.newLayer(sourceHandlerStateLayer);
    }

    void parseSubHandle() {
        HandleAreaLayer subHandleLayer = new HandleAreaLayer(documentStatement , type);
        parser = subHandleLayer;
        documentStatement.coverLayer(subHandleLayer);
    }

    @Override
    public void reload() {
        Handle handle = parser.get();
        if (handles.isEmpty()) {
            FS3DType type = handle.getType();
            typeIndex = type == FS3DType.BASIC_TYPE_INTEGER ? 0 : type == FS3DType.BASIC_TYPE_FLOAT ? 1 : type == FS3DType.BASIC_TYPE_STRING ? 2 : type == FS3DType.BASIC_TYPE_BOOLEAN ? 3 : 4;
        }
        handles.add(new CouplePair<>(handle , operator));
    }

    @Override
    public void logout() {
        if (handles.size() == 1) {
            handle = handles.getFirst().valueA();
            return;
        }
        if (typeIndex < 2) {
            DigitHandle[] digitHandles = new DigitHandle[handles.size()];
            int[] operators = new int[handles.size()];
            CouplePair<Handle, Integer>[] handles = this.handles.toArray(new CouplePair[this.handles.size()]);
            try {
                handle = constructFormula(handles , 0 , handles.length , 0 , typeIndex == 0 ? IntegerFormulaHandle.class.getDeclaredConstructor(DigitHandle.class.arrayType() , Integer.class.arrayType()) : FloatFormulaHandle.class.getDeclaredConstructor(DigitHandle.class.arrayType() , Integer.class.arrayType()));
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                documentStatement.stateNow = e.getMessage();
                unexpected();
            }
            handle = typeIndex == 0 ? new IntegerFormulaHandle(digitHandles, operators) : new FloatFormulaHandle(digitHandles, operators);
        }
        switch (typeIndex) {
            case 2 -> {
                StringFormulaHandle[] stringHandles = new StringFormulaHandle[handles.size()];
                Iterator<CouplePair<Handle, Integer>> iterator = handles.iterator();
                for (int index = 0; index < stringHandles.length; ) {
                    CouplePair<Handle, Integer> pair = iterator.next();
                    stringHandles[index++] = (StringFormulaHandle) pair.valueA();
                }
                handle = new StringFormulaHandle(stringHandles);
            }
            case 3 -> {
                BooleanHandle[] booleanHandles = new BooleanHandle[handles.size()];
                Iterator<CouplePair<Handle, Integer>> iterator = handles.iterator();
                int[] operators = new int[handles.size()];
                for (int index = 0; index < booleanHandles.length; ) {
                    CouplePair<Handle, Integer> pair = iterator.next();
                    booleanHandles[index] = (BooleanHandle) pair.valueA();
                    operators[index++] = pair.valueB();
                }
                handle = new BooleanFormulaHandle(booleanHandles, operators);
            }
        }
    }

    @Override
    public void keyword(int keyword) {
        if (!handles.isEmpty()) {
            unexpected();
        }
        Handle handle = switch (keyword) {
            case GrammarParser.INDEX_NULL -> new FinalObjectHandle(type.isBasicType() ? type.getDefaultValue() : new CustomedObject((CustomedType) type, null));
            case GrammarParser.INDEX_INITIALIZER -> new FinalObjectHandle(type.getDefaultValue());
            default -> {
                unexpected();
                yield null;
            }
        };
        handles.add(new CouplePair<>(handle , 0));
    }

    DigitHandle constructFormula(CouplePair<Handle , Integer>[] handles , int begin , int end , int operatorPriority , Constructor<? extends DigitHandle> constructor) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (operatorPriority == Operator.OPERATOR_PRIORITIES.length) {
            DigitHandle[] digitHandles = new DigitHandle[end - begin];
            int[] operators = new int[end - begin];
            int index = 0;
            while (begin < end) {
                digitHandles[index] = (DigitHandle) handles[begin].valueA();
                operators[index++] = handles[begin++].valueB();
            }
            return constructor.newInstance(digitHandles , operators);
        }
        List<CouplePair<DigitHandle , Integer>> subHandles = new LinkedList<>();
        int operator = 0 , head = 0;
        while (begin < end) {
            if (Operator.OPERATOR_PRIORITIES[operatorPriority].corresponds(handles[begin].valueB())) {
                if (head != 0) {
                    subHandles.add(new CouplePair<>(constructFormula(handles , head , begin , operatorPriority + 1 , constructor) , operator));
                }
                else {
                    head = begin;
                }
                operator = handles[begin].valueB();
                begin++;
            }
        }
        DigitHandle[] digitHandles = new DigitHandle[subHandles.size()];
        int[] operators = new int[subHandles.size()];
        Iterator<CouplePair<DigitHandle , Integer>> iterator = subHandles.iterator();
        CouplePair<DigitHandle , Integer> pair = iterator.next();
        digitHandles[0] = pair.valueA();
        operators[0] = 0;
        int index = 1;
        while (iterator.hasNext()) {
            pair = iterator.next();
            digitHandles[index] = pair.valueA();
            operators[index++] = pair.valueB();
        }
        return constructor.newInstance(digitHandles , operators);
    }

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
                operator = 0;
                parseSource();
            }
            case '-' -> {
                operator = 1;
                parseSource();
            }
            case '*' -> {
                operator = 2;
                parseSource();
            }
            case '/' -> {
                operator = 3;
                parseSource();
            }
            case '%' -> {
                operator = 4;
                parseSource();
            }
            case '<' -> {
                operator = 5;
                documentStatement.newLayer(new WordOperatorCheckStateLayer(documentStatement , this , punctuation));
            }
            case '>' -> {
                operator = 6;
                documentStatement.newLayer(new WordOperatorCheckStateLayer(documentStatement , this , punctuation));
            }
            case '(' -> {
                parseSubHandle();
            }
            case ')' -> {
                documentStatement.retractLayer();
                return;
            }
        }
    }

    @Override
    public Handle get() {
        return handle;
    }
}

class HandleProxyAreaLayer extends HandleAreaLayer {

    HandleProxyAreaLayer(DocumentStatement statement , FS3DType type) {
        super(statement , type);
    }

    @Override
    public void keyword(int keyword) {
        handle = switch (keyword) {
            case GrammarParser.INDEX_NULL -> new FinalObjectHandle(type.isBasicType() ? type.getDefaultValue() : new CustomedObject((CustomedType) type, null));
            case GrammarParser.INDEX_INITIALIZER -> new FinalObjectHandle(type.getDefaultValue());
            default -> {
                unexpected();
                yield null;
            }
        };
        documentStatement.retractLayer();
    }

    @Override
    public void logout() {
        super.logout();
        if (!(type == handle.getType() || type.isParentOf(handle.getType()))) {
            TypeMismatchException.assignWrongObject(documentStatement);
        }
    }
}

@Unfinished
class SourceHandlerStateLayer extends StateLayer implements HandleParser {
    HandleParser parser;
    final FS3DType type;

    SourceHandlerStateLayer(DocumentStatement statement , FS3DType type) {
        super(statement);
        this.type = type;
    }

    @Override
    public Handle get() {
        return parser.get();
    }

    @Override
    public void keyword(int keyword) {
        parser = () -> switch (keyword) {
            case GrammarParser.INDEX_NULL -> new FinalObjectHandle(type.isBasicType() ? type.getDefaultValue() : new CustomedObject((CustomedType) type, null));
            case GrammarParser.INDEX_INITIALIZER -> new FinalObjectHandle(type.getDefaultValue());
            default -> {
                unexpected();
                yield null;
            }
        };
        documentStatement.toAreaLayer();
    }

    @Unfinished
    @Override
    public void type(TypeStatement typeStatement) {
    }

    @Unfinished
    @Override
    public void variable(Variable variable) {
        if (variable.isFunctionVariable()) {
            FunctionHandleStateLayer layer = new FunctionHandleStateLayer(documentStatement);
            documentStatement.newLayer(layer);
            parser = layer;
        }

    }

    @Unfinished
    @Override
    public void function(int function) {

    }
}

class FunctionHandleStateLayer extends StateLayer implements HandleParser {

    FunctionHandleStateLayer(DocumentStatement statement) {
        super(statement);
    }

    @Override
    public Handle get() {
        return null;
    }
}

