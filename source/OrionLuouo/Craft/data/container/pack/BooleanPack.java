package OrionLuouo.Craft.data.container.pack;

import OrionLuouo.Craft.data.Iterator;
import OrionLuouo.Craft.data.container.Pack;

public class BooleanPack implements Pack<Boolean> {
    long[] booleans;

    public BooleanPack(int size) {
        booleans = new long[(size >> 5) + ((size & 0x3F) == 0 ? 0 : 1)];
    }

    @Override
    public Boolean get(int index) {
        return ((booleans[index & 0xFFFF_FFC0] >> (index & 0x3F)) & 1) != 0;
    }

    @Override
    public void set(int index, Boolean element) {
        booleans[index & 0xFFFF_FFC0] ^= booleans[index & 0xFFFF_FFC0] & (1L << (index & 0x3F));
        booleans[index & 0xFFFF_FFC0] |= (1L << (index & 0x3F));
    }

    @Override
    public Boolean replace(int index, Boolean newElement) {
        long code = (newElement ? 1L : 0L) << (index & 0x3F);
        int arrayIndex = index >> 5;
        booleans[arrayIndex] ^= code;
        code ^= booleans[arrayIndex];
        booleans[arrayIndex] ^= code;
        return code >> (index & 0x3F) == 1;
    }

    @Override
    public void set(int index, Iterator<Boolean> iterator) {
        while (iterator.hasNext()) {
            set(index++, iterator.next());
        }
    }

    @Override
    public Iterator<Boolean> iterator(int index) {
        return null;
    }

    @Override
    public int size() {
        return booleans.length << 5;
    }

    @Override
    public Pack<Boolean> copy() {
        BooleanPack pack = new BooleanPack(0);
        long[] booleans = new long[this.booleans.length];
        System.arraycopy(this.booleans, 0, booleans, 0, booleans.length);
        pack.booleans = booleans;
        return pack;
    }

    class BooleanPackIterator implements Iterator<Boolean> {
        long section;
        int shiftIndex , index;

        BooleanPackIterator(int index) {
            this.index = index >> 5;
            this.shiftIndex = index & 0x3F;
            section = booleans[this.index] >> shiftIndex;
        }

        BooleanPackIterator() {
            this(0);
        }

        @Override
        public boolean hasNext() {
            return index < booleans.length;
        }

        @Override
        public Boolean next() {
            if (index == 64) {
                section = booleans[++index];
            }
            boolean value = (section & 1) == 1;
            section >>>= 1;
            shiftIndex++;
            return value;
        }
    }
}
