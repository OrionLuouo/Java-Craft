package OrionLuouo.Craft.io.documents.fs3d;


import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.io.documents.fs3d.source.BasicType;

import java.util.Iterator;

public interface FS3DType {
    BasicType BASIC_TYPE_INTEGER = new BasicType() {
        @Override
        public String getName() {
            return "integer";
        }
    }
    , BASIC_TYPE_FLOAT = new BasicType() {
        @Override
        public String getName() {
            return "float";
        }
    }
    , BASIC_TYPE_DATE = new BasicType() {
        @Override
        public String getName() {
            return "date";
        }
    }
    , BASIC_TYPE_TIMESTAMP = new BasicType() {
        @Override
        public String getName() {
            return "timestamp";
        }
    }
    , BASIC_TYPE_BYTE = new BasicType() {
        @Override
        public String getName() {
            return "byte";
        }
    }
    , BASIC_TYPE_CHARACTER = new BasicType() {
        @Override
        public String getName() {
            return "character";
        }
    }
    , BASIC_TYPE_BOOLEAN = new BasicType() {
        @Override
        public String getName() {
            return "boolean";
        }
    }
    , BASIC_TYPE_STRING = new BasicType() {
        @Override
        public String getName() {
            return "string";
        }
    };

    boolean isParentOf(FS3DType type);
    String getName();
    FS3DType[] getParents();
    FS3DType[] getInternalTypes();
    int areaSize();
    Iterator<FS3DObject> areaIterator();
    FS3DType getArrayType();
    FS3DType getElementType();
    boolean isArrayType();
    boolean isBasicType();
    int getFieldIndex(String name);
    Iterator<CouplePair<String , FS3DType>> getFieldIterator();
}
