package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.io.documents.fs3d.FS3DObject;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

import java.util.Iterator;
import java.util.Map;

public abstract class BasicType extends FinalTypeStatement {
    FS3DType arrayType;

    {
        type = this;
    }

    @Override
    public boolean isParentOf(FS3DType type) {
        return false;
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
    public FS3DType getElementType() {
        return null;
    }

    @Override
    public boolean isArrayType() {
        return false;
    }

    @Override
    public boolean isBasicType() {
        return true;
    }

    @Override
    public FS3DType getArrayType() {
        return arrayType == null ? (arrayType = new ArrayType(this)) : arrayType;
    }

    @Override
    public int getFieldIndex(String name) {
        return name.equals("value") ? 0 : -1;
    }

    @Override
    public Iterator<CouplePair<String, FS3DType>> getFieldIterator() {
        return new Iterator<>() {
            boolean yetReturned = true;

            @Override
            public boolean hasNext() {
                return yetReturned;
            }

            @Override
            public CouplePair<String, FS3DType> next() {
                yetReturned = false;
                try {
                    return new CouplePair<>("value" , (FS3DType) FS3DType.class.getDeclaredField("BASIC_TYPE_" + getName().toUpperCase()).get(null));
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}