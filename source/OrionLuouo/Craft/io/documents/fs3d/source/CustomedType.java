package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.io.documents.fs3d.FS3DObject;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomedType implements FS3DType {
    record Field(FS3DType type , int index) {
    }

    String name;
    FS3DType[] parents;
    Map<String , Field> fields;
    Map<String , CustomedType> internalTypes;
    FS3DObject[] area;
    ArrayType arrayType;

    {
        fields = new LinkedHashMap<>();
        internalTypes = new LinkedHashMap<>();
    }

    CustomedType(FS3DType[] parents) {
        this.parents = parents;
    }

    CustomedType() {
        parents = new FS3DType[0];
    }

    public static CustomedType getRootType() {
        return new CustomedType();
    }

    @Override
    public boolean isParentOf(FS3DType type) {
        if (type instanceof BasicType) {
            return false;
        }
        for (FS3DType parent : type.getParents()) {
            if (parent instanceof BasicType) {
                continue;
            }
            if (parent == this || isParentOf(parent)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public FS3DType[] getParents() {
        return parents;
    }

    @Override
    public FS3DType[] getInternalTypes() {
        return internalTypes.values().toArray(new CustomedType[0]);
    }

    @Override
    public int areaSize() {
        return area.length;
    }

    @Override
    public Iterator<FS3DObject> areaIterator() {
        return new Iterator<FS3DObject>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < area.length;
            }

            @Override
            public FS3DObject next() {
                return area[index++];
            }
        };
    }

    @Override
    public FS3DType getArrayType() {
        return arrayType == null ? (arrayType = new ArrayType(this)) : arrayType;
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
        return false;
    }

    @Override
    public int getFieldIndex(String name) {
        return fields.get(name).index;
    }

    @Override
    public Iterator<CouplePair<String, FS3DType>> getFieldIterator() {
        return new Iterator<>() {
            final Iterator<Map.Entry<String , Field>> iterator = fields.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public CouplePair<String, FS3DType> next() {
                Map.Entry<String , Field> entry = iterator.next();
                return new CouplePair<>(entry.getKey() , entry.getValue().type());
            }
        };
    }
}