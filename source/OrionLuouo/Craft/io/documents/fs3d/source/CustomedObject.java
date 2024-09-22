package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.io.documents.fs3d.FS3DObject;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

public class CustomedObject implements FS3DObject {
    CustomedType type;
    FS3DObject[] values;

    @Override
    public FS3DType getType() {
        return type;
    }

    @Override
    public FS3DObject getValue(String name) {
        return values[type.fields.get(name).index()];
    }

    @Override
    public FS3DObject getValue(int index) {
        return values[index];
    }

    @Override
    public FS3DObject[] getValues() {
        return values;
    }
}

class ExtraCustomedObject extends CustomedObject {
    FS3DObject[] extraArea;

    @Override
    public FS3DObject[] getExtraArea() {
        return extraArea;
    }
}