package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.io.documents.fs3d.FS3DObject;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

public interface Variable {
    public static final Variable CONFLICTED_VARIABLE = new Variable() {

        @Override
        public FS3DType getType() {
            return CustomedType.CONFLICTED_TYPE;
        }

        @Override
        public void assignValue(FS3DObject value) {
        }
    };

    FS3DType getType();
    void assignValue(FS3DObject value);
    default void assignFunctionInstance(FunctionInstance functionInstance) {
        assignValue(functionInstance.invoke(null));
    }
}
