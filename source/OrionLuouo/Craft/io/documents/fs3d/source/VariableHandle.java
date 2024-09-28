package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.io.documents.fs3d.FS3DObject;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

public class VariableHandle implements Handle, Variable {
    FS3DObject value;
    FS3DType type;

    @Override
    public FS3DType getType() {
        return type;
    }

    @Override
    public FS3DObject getValue() {
        return value;
    }

    @Override
    public void assignValue(FS3DObject value) {
        this.value = value;
    }
}

class IntegerVariableHandle extends VariableHandle {
    IntegerObject value;
}