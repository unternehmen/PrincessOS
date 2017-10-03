package common;

/**
 * A Pair is a generic container for two objects.
 * 
 * @param <T>  the type of the first object
 * @param <S>  the type of the second object
 */
public class Pair<T, S> {
    T head;
    S tail;
    
    /**
     * Return a Pair containing the given objects.
     * 
     * @param head  the first object
     * @param tail  the second object
     * @return  the new Pair
     */
    public Pair(T head, S tail)
    {
        this.head = head;
        this.tail = tail;
    }
    
    /**
     * Return the first object in this Pair.
     * 
     * @return the first object
     */
    public T getHead()
    {
        return head;
    }
    
    /**
     * Return the second object in this Pair.
     * 
     * @return the second object
     */
    public S getTail()
    {
        return tail;
    }
}
