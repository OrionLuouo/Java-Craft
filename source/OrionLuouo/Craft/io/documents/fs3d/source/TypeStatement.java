package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

public class TypeStatement {
    FS3DType type;

    TypeStatement() {
    }

    TypeStatement(String name) {
        type = new CustomedType();
        ((CustomedType) type).name = name;
    }

    FS3DType getType() {
        return type;
    }
}

abstract class FinalTypeStatement extends TypeStatement implements FS3DType {
    {
        type = this;
    }

    FS3DType getType() {
        return this;
    }
}