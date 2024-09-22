package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.io.documents.fs3d.FS3DObject;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

public class FunctionVariable implements Variable {
    FunctionInstance functionInstance;

    @Override
    public FS3DType getType() {
        return null;
    }

    @Override
    public void assignValue(FS3DObject value) {
        functionInstance = new FinalFunctionInstance(value);
    }

    @Override
    public void assignFunctionInstance(FunctionInstance functionInstance) {
        this.functionInstance = functionInstance;
    }
}
