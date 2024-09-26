package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.io.documents.fs3d.FS3DObject;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

import java.util.Iterator;

public class ArrayType implements FS3DType {
    FS3DType elementType , arrayType ;

    ArrayType(FS3DType type) {
        elementType = type;
    }

    @Override
    public boolean isParentOf(FS3DType type) {
        return false;
    }

    @Override
    public String getName() {
        return elementType.getName() + "[]";
    }

    @Override
    public FS3DType[] getParents() {
        return new FS3DType[0];
    }

    @Override
    public FS3DType[] getInternalTypes() {
        return new FS3DType[0];
    }

    @Override
    public int areaSize() {
        return 0;
    }

    @Override
    public Iterator<FS3DObject> areaIterator() {
        return null;
    }

    @Override
    public FS3DType getArrayType() {
        return arrayType == null ? (arrayType = new ArrayType(this)) : arrayType;
    }

    @Override
    public FS3DType getElementType() {
        return elementType;
    }

    @Override
    public boolean isArrayType() {
        return true;
    }

    @Override
    public boolean isBasicType() {
        return false;
    }

    @Override
    public int getFieldIndex(String name) {
        return name.equals("value") ? 0 : -1;
    }

    @Override
    public Iterator<CouplePair<String, FS3DType>> getFieldIterator() {
        final ArrayType type = this;
        return new Iterator<CouplePair<String, FS3DType>>() {
            boolean got = false;

            @Override
            public boolean hasNext() {
                return !got;
            }

            @Override
            public CouplePair<String, FS3DType> next() {
                if (got) {
                    return null;
                }
                got = true;
                return new CouplePair<>("value" , type);
            }
        };
    }

    @Override
    public FS3DObject getDefaultValue() {
        return new ArrayObject(new FS3DObject[0] , this);
    }
}
