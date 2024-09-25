package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

import java.util.HashMap;
import java.util.Map;

public class TypeStatement {
    FS3DType type;
    Map<String , Variable> variableMap;
    Map<String , TypeStatement> typeMap;
    Handler[] fieldDefaultValues;

    TypeStatement() {
        type = new CustomedType();
        variableMap = new HashMap<>();
        typeMap = new HashMap<>();
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