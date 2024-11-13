package OrionLuouo.Craft.StructuredDocumentCompiler;

import OrionLuouo.Craft.StructuredDocumentCompiler.exception.SemanticMismatchException;
import OrionLuouo.Craft.data.container.collection.sequence.CycledArrayList;
import OrionLuouo.Craft.data.container.collection.sequence.Queue;
import OrionLuouo.Craft.system.annotation.Unfinished;

import java.util.*;
import java.util.function.BiConsumer;

public class SemanticRegex {
    static Compiler compiler;
    SemanticUnit[] roots , leaves;
    SemanticUnit unitNow , root;
    SemanticMatch match;
    OrionLuouo.Craft.data.container.collection.sequence.List<MatchedUnit> matchedUnitQueue;

    interface MatchedUnit {
        void rematch(SemanticRegex regex);
    }

    record PunctuationMatchedUnit(char punctuation) implements MatchedUnit {
        @Override
        public void rematch(SemanticRegex regex) {
            regex.matches(punctuation);
        }
    }

    record ElementMatchedUnit(Object value , GrammarParser.WordType type) implements MatchedUnit {
        @Override
        public void rematch(SemanticRegex regex) {
            regex.matches(value , type);
        }
    }

    private SemanticRegex() {
    }

    SemanticRegex(Compiler compiler) {
        this.compiler = compiler;
        root = new SemanticUnit() {
            {
                children = new List<SemanticUnit>() {
                    @Override
                    public int size() {
                        return 0;
                    }

                    @Override
                    public boolean isEmpty() {
                        return false;
                    }

                    @Override
                    public boolean contains(Object o) {
                        return false;
                    }

                    @Override
                    public Iterator<SemanticUnit> iterator() {
                        return null;
                    }

                    @Override
                    public Object[] toArray() {
                        return new Object[0];
                    }

                    @Override
                    public <T> T[] toArray(T[] a) {
                        return null;
                    }

                    @Override
                    public boolean add(SemanticUnit semanticUnit) {
                        return false;
                    }

                    @Override
                    public boolean remove(Object o) {
                        return false;
                    }

                    @Override
                    public boolean containsAll(Collection<?> c) {
                        return false;
                    }

                    @Override
                    public boolean addAll(Collection<? extends SemanticUnit> c) {
                        return false;
                    }

                    @Override
                    public boolean addAll(int index, Collection<? extends SemanticUnit> c) {
                        return false;
                    }

                    @Override
                    public boolean removeAll(Collection<?> c) {
                        return false;
                    }

                    @Override
                    public boolean retainAll(Collection<?> c) {
                        return false;
                    }

                    @Override
                    public void clear() {

                    }

                    @Override
                    public SemanticUnit get(int index) {
                        return null;
                    }

                    @Override
                    public SemanticUnit set(int index, SemanticUnit element) {
                        return null;
                    }

                    @Override
                    public void add(int index, SemanticUnit element) {

                    }

                    @Override
                    public SemanticUnit remove(int index) {
                        return null;
                    }

                    @Override
                    public int indexOf(Object o) {
                        return 0;
                    }

                    @Override
                    public int lastIndexOf(Object o) {
                        return 0;
                    }

                    @Override
                    public ListIterator<SemanticUnit> listIterator() {
                        return new ListIterator<SemanticUnit>() {
                            int index = 0;

                            @Override
                            public boolean hasNext() {
                                return index < roots.length;
                            }

                            @Override
                            public SemanticUnit next() {
                                return roots[index++];
                            }

                            @Override
                            public boolean hasPrevious() {
                                return false;
                            }

                            @Override
                            public SemanticUnit previous() {
                                return null;
                            }

                            @Override
                            public int nextIndex() {
                                return 0;
                            }

                            @Override
                            public int previousIndex() {
                                return 0;
                            }

                            @Override
                            public void remove() {

                            }

                            @Override
                            public void set(SemanticUnit semanticUnit) {

                            }

                            @Override
                            public void add(SemanticUnit semanticUnit) {

                            }
                        };
                    }

                    @Override
                    public ListIterator<SemanticUnit> listIterator(int index) {
                        return null;
                    }

                    @Override
                    public List<SemanticUnit> subList(int fromIndex, int toIndex) {
                        return List.of();
                    }
                };
            }

            @Override
            SemanticMatch.MatchState matches(SemanticMatch match, char punctuation) {
                return null;
            }

            @Override
            SemanticMatch.MatchState matches(SemanticMatch match, Object object, GrammarParser.WordType type) {
                return null;
            }
        };
    }

    @Unfinished
    public static SemanticRegex compile(Compiler compiler , String regex) throws SDCException {
        /*
        Compiler simpleCompiler = compiler;
        SemanticUnit superRoot = new PunctuationSemanticUnit((char) 0);
        final SemanticUnit[][] units = {new SemanticUnit[]{superRoot}};
        simpleCompiler.grammarParser = new GrammarParser(simpleCompiler) {
            @Override
            public void word(String word) {

            }
        };
        simpleCompiler.semanticRegex = new SemanticRegex() {
            @Override
            public void matches(char punctuation) {
                SemanticUnit newUnit = null;
                switch (punctuation) {
                    case '/' -> {

                    }
                    case '(' -> {

                    }
                    case ')' -> {

                    }
                    case '|' -> {

                    }
                    case '?' -> {

                    }
                    case '*' -> {

                    }
                    case '+' -> {

                    }
                    default -> {
                        newUnit = new PunctuationSemanticUnit(punctuation);
                    }
                }
                for (SemanticUnit unit : units[0]) {
                    unit.children.add(newUnit);
                }
                units[0] = new SemanticUnit[] {newUnit};
            }
        };
        simpleCompiler.input(regex);
        SemanticRegex semanticRegex = new SemanticRegex();
        semanticRegex.roots = superRoot.children.toArray(new SemanticUnit[superRoot.children.size()]);
        semanticRegex.leaves = units[0];
        semanticRegex.compiler = compiler;
        return semanticRegex;
         */
        return null;
    }

    @Unfinished
    public static SemanticRegex compile(Compiler compiler , String regex, String... units) throws SDCException {

        return null;
    }

    /**
     * To paste the regex to this one.
     * The two objects will be simply merged,
     * referring to each other.
     * So if a regex is needed in multiple conditions,
     * it would be better to compile one for each.
     * And be attention that,
     * the original object will keep its state,
     * meaning you can't paste any suffix of variable:regex to this object.
     * But it's valid to use this in registration to the Compiler.
     */
    public SemanticRegex bifurcate(SemanticRegex regex) {
        Collection<SemanticUnit> collection = Arrays.asList(regex.roots);
        for (SemanticUnit unit : leaves) {
            unit.children.addAll(collection);
        }
        SemanticRegex newRegex = new SemanticRegex();
        newRegex.roots = roots;
        newRegex.leaves = regex.leaves;
        return newRegex;
    }

    public void addSemantics(int regexIndex) {
        for (SemanticUnit unit : leaves) {
            unit.regexIndex = regexIndex;
        }
    }

    void reset() {
        unitNow = root;
        match = new SemanticMatch();
        match.state = SemanticMatch.MatchState.COMPLETE;
    }

    void mismatchMakeup() {

    }

    @Unfinished
    final boolean checkMatch() {
        switch (match.state) {
            case COMPLETE -> {
                if (unitNow.regexIndex != -1) {
                    match.uncertainUnitCount++;
                    matchedUnitQueue.clean();
                    match.regexIndex = unitNow.regexIndex;
                    if (unitNow.children.isEmpty()) {
                        return true;
                    }
                }
            }
            case POTENTIAL_MISMATCH -> {
                match.state = SemanticMatch.MatchState.COMPLETE;
                matchedUnitQueue.pop().rematch(this);
            }
            case MISMATCH -> {
                mismatchMakeup();
            }
        }
        return false;
    }

    void matches(char punctuation) {
        if (match.regexIndex != -1) {
            matchedUnitQueue.add(new PunctuationMatchedUnit(punctuation));
        }
        SemanticMatch.MatchState state;
        if (match.state == SemanticMatch.MatchState.COMPLETE) {
            for (SemanticUnit unit : unitNow.children) {
                if ((state = unit.matches(match , punctuation)) != SemanticMatch.MatchState.MISMATCH && state != SemanticMatch.MatchState.POTENTIAL_MISMATCH) {
                    match.state = state;
                    if (checkMatch()) {
                        compiler.structureLayer.parseSemantics(match);
                    }
                    return;
                }
            }
            SemanticMismatchException.mismatch(compiler , "No available regex found for the content.");
        }
        else {
            match.state = unitNow.matches(match , punctuation);
            if (checkMatch()) {
                compiler.structureLayer.parseSemantics(match);
            }
        }
    }

    void matches(Object object , GrammarParser.WordType type) {
        if (match.regexIndex != -1) {
            matchedUnitQueue.add(new ElementMatchedUnit(object, type));
        }
        SemanticMatch.MatchState state;
        if (match.state == SemanticMatch.MatchState.COMPLETE) {
            for (SemanticUnit unit : unitNow.children) {
                if ((state = unit.matches(match , object , type)) != SemanticMatch.MatchState.MISMATCH && state != SemanticMatch.MatchState.POTENTIAL_MISMATCH) {
                    match.state = state;
                    if (checkMatch()) {
                        compiler.structureLayer.parseSemantics(match);
                    }
                    return;
                }
            }
            SemanticMismatchException.mismatch(compiler , "No available regex found for the content.");
        }
        else {
            match.state = unitNow.matches(match , object , type);
            if (checkMatch()) {
                compiler.structureLayer.parseSemantics(match);
            }
        }
    }
}