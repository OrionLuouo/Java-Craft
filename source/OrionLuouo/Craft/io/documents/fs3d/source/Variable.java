package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.io.documents.fs3d.FS3DObject;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

public interface Variable {
    FS3DType getType();
    void assignValue(FS3DObject value);
    default void assignFunctionInstance(FunctionInstance functionInstance) {
        assignValue(functionInstance.invoke(null));
    }
}
