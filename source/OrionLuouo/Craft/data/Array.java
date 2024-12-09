package OrionLuouo.Craft.data;

import java.util.Arrays;

public class Array {
    static public Integer[] convertIntegers(int[] array) {
        int length = array.length;
        Integer[] integers = new Integer[length];
        for (int index =  0 ; index < integers.length ;) {
            integers[index] = array[index++];
        }
        return integers;
    }

    static public Long[] convertLongs(long[] array) {
        int length = array.length;
        Long[] longs = new Long[length];
        for (int index =  0 ; index < longs.length ;) {
            longs[index] = array[index++];
        }
        return longs;
    }

    static public Float[] convertFloats(float[] array) {
        int length = array.length;
        Float[] floats = new Float[length];
        for (int index =  0 ; index < floats.length ;) {
            floats[index] = array[index++];
        }
        return floats;
    }

    static public Double[] convertDoubles(double[] array) {
        int length = array.length;
        Double[] doubles = new Double[length];
        for (int index =  0 ; index < doubles.length ;) {
            doubles[index] = array[index++];
        }
        return doubles;
    }

    static public Boolean[] convertBooleans(boolean[] array) {
        int length = array.length;
        Boolean[] booleans = new Boolean[length];
        for (int index =  0 ; index < booleans.length ;) {
            booleans[index] = array[index++];
        }
        return booleans;
    }

    static public Character[] convertCharacters(char[] array) {
        int length = array.length;
        Character[] characters = new Character[length];
        for (int index =  0 ; index < characters.length ;) {
            characters[index] = array[index++];
        }
        return characters;
    }

    static public Byte[] convertBytes(byte[] array) {
        int length = array.length;
        Byte[] bytes = new Byte[length];
        for (int index =  0 ; index < bytes.length ;) {
            bytes[index] = array[index++];
        }
        return bytes;
    }

    static public Short[] convertShorts(short[] array) {
        int length = array.length;
        Short[] shorts = new Short[length];
        for (int index =  0 ; index < shorts.length ;) {
            shorts[index] = array[index++];
        }
        return shorts;
    }

    static public int[] convertBaseIntegers(Integer[] array) {
        int length = array.length;
        int[] baseIntegers = new int[length];
        for (int index =  0 ; index < baseIntegers.length ;) {
            baseIntegers[index] = array[index++];
        }
        return baseIntegers;
    }

    static public long[] convertBaseLongs(Long[] array) {
        int length = array.length;
        long[] baseLongs = new long[length];
        for (int index =  0 ; index < baseLongs.length ;) {
            baseLongs[index] = array[index++];
        }
        return baseLongs;
    }

    static public float[] convertBaseFloats(Float[] array) {
        int length = array.length;
        float[] baseFloats = new float[length];
        for (int index =  0 ; index < baseFloats.length ;) {
            baseFloats[index] = array[index++];
        }
        return baseFloats;
    }

    static public double[] convertBaseDoubles(Double[] array) {
        int length = array.length;
        double[] baseDoubles = new double[length];
        for (int index =  0 ; index < baseDoubles.length ;) {
            baseDoubles[index] = array[index++];
        }
        return baseDoubles;
    }

    static public boolean[] convertBaseBooleans(Boolean[] array) {
        int length = array.length;
        boolean[] baseBooleans = new boolean[length];
        for (int index =  0 ; index < baseBooleans.length ;) {
            baseBooleans[index] = array[index++];
        }
        return baseBooleans;
    }

    static public char[] convertBaseCharacters(Character[] array) {
        int length = array.length;
        char[] baseCharacters = new char[length];
        for (int index =  0 ; index < baseCharacters.length ;) {
            baseCharacters[index] = array[index++];
        }
        return baseCharacters;
    }

    static public byte[] convertBaseBytes(Byte[] array) {
        int length = array.length;
        byte[] baseBytes = new byte[length];
        for (int index =  0 ; index < baseBytes.length ;) {
            baseBytes[index] = array[index++];
        }
        return baseBytes;
    }

    static public short[] convertBaseShorts(Short[] array) {
        int length = array.length;
        short[] baseShorts = new short[length];
        for (int index =  0 ; index < baseShorts.length ;) {
            baseShorts[index] = array[index++];
        }
        return baseShorts;
    }

    static public <T> T[] expand(T[] array , int appendedLength) {
        T[] newArray = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType() , array.length + appendedLength);
        System.arraycopy(array , 0 , newArray , 0 , array.length);
        return newArray;
    }

    static public <T> T[] connect(T value , T[] array) {
        T[] newArray = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType() , array.length + 1);
        System.arraycopy(array , 0 , newArray , 1 , array.length);
        newArray[0] = value;
        return newArray;
    }

    @SafeVarargs
    static public <T> T[] connect(T[] array , T... values) {
        T[] newArray = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType() , array.length + values.length);
        System.arraycopy(array , 0 , newArray , 0 , array.length);
        System.arraycopy(values , 0 , newArray , array.length , values.length);
        return newArray;
    }

    static public <T> T[] connect(T[] array , T value) {
        T[] newArray = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType() , array.length + 1);
        System.arraycopy(array , 0 , newArray , 0 , array.length);
        newArray[array.length] = value;
        return newArray;
    }

    static public <T> T[] insert(T[] array , int index , T value) {
        T[] newArray = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType() , array.length + 1);
        if (index != 0) {
            System.arraycopy(array , 0 , newArray , 0 , index);
        }
        newArray[index] = value;
        if (index != array.length) {
            System.arraycopy(array , index , newArray , index + 1 , array.length - index);
        }
        return newArray;
    }

    static public int[] insert(int[] array , int index , int value) {
        int[] newArray = new int[array.length + 1];
        if (index != 0) {
            System.arraycopy(array , 0 , newArray , 0 , index);
        }
        newArray[index] = value;
        if (index != array.length) {
            System.arraycopy(array , index , newArray , index + 1 , array.length - index);
        }
        return newArray;
    }

    @SafeVarargs
    static public <T> T[] insert(T[] array , int index , T... values) {
        T[] newArray = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType() , array.length + 1);
        if (index != 0) {
            System.arraycopy(array , 0 , newArray , 0 , index);
        }
        System.arraycopy(values , 0 , newArray , index , values.length);
        if (index != array.length) {
            System.arraycopy(array , index , newArray , index + values.length , array.length - index);
        }
        return newArray;
    }
}
