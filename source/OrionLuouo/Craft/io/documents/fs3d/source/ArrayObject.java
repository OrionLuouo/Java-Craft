package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.io.documents.fs3d.FS3DObject;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

public record ArrayObject(FS3DObject[] elements , ArrayType type) implements FS3DObject {
    @Override
    public FS3DType getType() {
        return type;
    }
}
