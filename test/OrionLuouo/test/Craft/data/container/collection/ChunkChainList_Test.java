package OrionLuouo.test.Craft.data.container.collection;

import OrionLuouo.Craft.data.container.collection.sequence.ChunkChainList;

import java.util.LinkedList;

public class ChunkChainList_Test {
    public static final int SIZE = 1048576
            ,ADD_TIME = 1024 * 64
            ,REMOVE_TIME = 1024 * 8
            ,INSERT_TIME = 1024 * 8
            ,GET_TIME = 1024 * 16
            ,TO_ARRAY_TIME = 16;
    public static void main(String[] args) {
        Sequence_TestStandardOutput test = new Sequence_TestStandardOutput(ChunkChainList.class , SIZE , ADD_TIME , REMOVE_TIME , GET_TIME , INSERT_TIME , TO_ARRAY_TIME , new LinkedList<Integer>() , new ChunkChainList<Integer>() , 0);
    }
}
