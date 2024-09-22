package OrionLuouo.test.Craft.data.container.map;

import OrionLuouo.Craft.data.container.map.DictionaryMap;

public class DictionaryMap_Test {
    public static void main(String[] args) {
        DictionaryMap<Integer> dictionary = new DictionaryMap<>();
        dictionary.add("Thinking In Java" , 0);
        dictionary.add("Introduction To Algorithm" , 1);
        dictionary.add("Advance Tech In Java" , 2);
        dictionary.add("???" , 3);
        dictionary.iterate(e -> {
            System.out.println(e.value() + ". " + e.key());
        });
    }
}
