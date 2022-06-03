
/**
 * One char, stored in a node, with a reference to another node
 *
 * @author Peter Fabens
 * @version 2/23/2022
 */
public class CharNode
{
    protected char value;
    protected CharNode next;
    protected CharNode prev;
    
    /**
     * Default constructor. Starts with null/empty values
     */
    public CharNode () {
        value = 0;
        next = null;
        prev = null;
    }
    
    /**
     * Overloaded constructor. Starts with specified value
     * 
     * @param value char to pass to the node to be held
     */
    public CharNode (char value) {
        this.value = value;
        next = null;
        prev = null;
    }
    
    /**
     * Overloaded constructor. Starts with specified value and references
     * 
     * @param value char to pass to the node to be held
     * @param prev  previous Node in list
     * @param next  next Node in list
     */
    public CharNode (char value, CharNode prev, CharNode next) {
        this.value = value;
        this.prev = prev;
        this.next = next;
    }
    
    /**
     * Value getter
     */
    public char get_char(){
        return value;
    }
    
    /**
     * Value setter
     * 
     * @param input char to set as value
     */
    public void set_char(char input){
        value = input;
    }
    
    /**
     * Next node reference getter
     */
    public CharNode get_next(){
        return next;
    }
    
    /**
     * Next node reference setter
     * 
     * @param input reference to set
     */
    public void set_next(CharNode input){
        next = input;
    }
    
    /**
     * Last node reference getter
     */
    public CharNode get_prev(){
        return prev;
    }
    
    /**
     * Last node reference setter
     * 
     * @param input reference to set
     */
    public void set_prev(CharNode input){
        prev = input;
    }
    
    /**
     * Overridden toString() returns the value held as a string.
     */
    @Override
    public String toString(){
        return value + "";
    }
}
