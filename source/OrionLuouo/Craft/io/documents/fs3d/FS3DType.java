package OrionLuouo.Craft.io.documents.fs3d;


import OrionLuouo.Craft.data.CouplePair;
import OrionLuouo.Craft.io.documents.fs3d.source.BasicObject;
import OrionLuouo.Craft.io.documents.fs3d.source.BasicType;

import java.util.Iterator;

public interface FS3DType {
    BasicType BASIC_TYPE_INTEGER = new BasicType() {
        @Override
        public String getName() {
            return "integer";
        }

        @Override
        public FS3DObject getDefaultValue() {
            return BasicObject.initializeInteger();
        }
    }
    , BASIC_TYPE_FLOAT = new BasicType() {
        @Override
        public String getName() {
            return "float";
        }

        @Override
        public FS3DObject getDefaultValue() {
            return BasicObject.initializeFloat();
        }
    }
    , BASIC_TYPE_DATE = new BasicType() {
        @Override
        public String getName() {
            return "date";
        }

        @Override
        public FS3DObject getDefaultValue() {
            return BasicObject.initializeDate();
        }
    }
    , BASIC_TYPE_TIMESTAMP = new BasicType() {
        @Override
        public String getName() {
            return "timestamp";
        }

        @Override
        public FS3DObject getDefaultValue() {
            return BasicObject.initializeTimestamp();
        }
    }
    , BASIC_TYPE_BYTE = new BasicType() {
        @Override
        public String getName() {
            return "byte";
        }

        @Override
        public FS3DObject getDefaultValue() {
            return BasicObject.initializeByte();
        }
    }
    , BASIC_TYPE_CHARACTER = new BasicType() {
        @Override
        public String getName() {
            return "character";
        }

        @Override
        public FS3DObject getDefaultValue() {
            return BasicObject.initializeCharacter();
        }
    }
    , BASIC_TYPE_BOOLEAN = new BasicType() {
        @Override
        public String getName() {
            return "boolean";
        }

        @Override
        public FS3DObject getDefaultValue() {
            return BasicObject.initializeBoolean();
        }
    }
    , BASIC_TYPE_STRING = new BasicType() {
        @Override
        public String getName() {
            return "string";
        }

        @Override
        public FS3DObject getDefaultValue() {
            return BasicObject.initializeString();
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
    FS3DObject getDefaultValue();
}
