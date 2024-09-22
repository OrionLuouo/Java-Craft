package OrionLuouo.Craft.data;

/**
 * 
 * @param <R> : Return
 * @param <A> : Argument
 */
public interface Function<R , A> {
    R process(A argument);
}
