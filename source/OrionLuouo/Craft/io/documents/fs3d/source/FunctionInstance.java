package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.data.Function;
import OrionLuouo.Craft.io.documents.fs3d.FS3DObject;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface FunctionInstance {
    class FunctionKernel {
        public static final Map<String , CouplePair<Class<FunctionInstance> , Integer>> FUNCTIONS = new HashMap<>();
        public static final List<Function<FunctionInitializerStateLayer , DocumentStatement>> INITIALIZERS = new ArrayList<>();
        static int index = 0;

        static {
            register("hidden-final", FunctionInstance.class, new Function<FunctionInitializerStateLayer, DocumentStatement>() {
                @Override
                public FunctionInitializerStateLayer process(DocumentStatement argument) {
                    return new FinalFunctionInstance.FinalFunctionInitializer(argument);
                }
            });
        }

        public static void register(String name , Class<FunctionInstance> function , Function<FunctionInitializerStateLayer , DocumentStatement> initializerSupplier) {
            FUNCTIONS.put(name , new CouplePair<>(function , index++));
            INITIALIZERS.add(initializerSupplier);
        }
    }

    FS3DType getType();
    FS3DObject invoke(FS3DObject... arguments);
}

record FinalFunctionInstance(FS3DObject value) implements FunctionInstance {
    static class FinalFunctionInitializer extends FunctionInitializerStateLayer {
        FinalFunctionInitializer(DocumentStatement statement) {
            super(statement);
        }

        @Override
        TypeStatement[] getStandardArguments() {
            return new TypeStatement[0];
        }
    }

    @Override
    public FS3DType getType() {
        return value.getType();
    }

    @Override
    public FS3DObject invoke(FS3DObject... arguments) {
        return value;
    }
}