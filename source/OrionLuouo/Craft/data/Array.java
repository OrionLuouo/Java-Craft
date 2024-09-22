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

    static public <T> T[] expand(T[] array , int length) {
        T[] newArray = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType() , array.length + length);
        System.arraycopy(array , 0 , newArray , 0 , array.length);
        return newArray;
    }
}
