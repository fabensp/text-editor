
/**
 * Class describing a cursor which is able 
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class CursorNode extends CharNode
{
    /**
     * Constructor, creates cursor in essentially the same way as a normal Node.
     */
    public CursorNode(char cursor_char){
        super();
        value = cursor_char;
    }
    
    /** 
     * Deliberately breaks the superclass set_char method, as the cursor shouldn't be able to have value changed
     * but if for some reason iterating through the whole list it would be annoying to have to avoid the cursor
     */
    public void set_char(char input){}
    
    /**
     * Move cursor left
     * 
     * @return whether it worked and the cursor moved
     */
    public boolean left()
    {
        if (prev == null) return false;
        CharNode start_next = next; // current next/prev, before moving. saving
        CharNode start_prev = prev; // these references is mostly for readability
        if (start_next != null) start_next.set_prev(start_prev); // test if null to avoid NullPointerException  when cursor is at right edge
        start_prev.set_next(start_next);    // make the things currently on either side of cursor point at each other
        next = start_prev;               // insert self in between start_prev and start_prev's prev
        prev = start_prev.get_prev();
        if (start_prev.get_prev() != null) start_prev.get_prev().set_next(this); // same as above, test if null in case cursor is near end of line
        start_prev.set_prev(this);          // make start_prev point back at self
        return true;
    }
    
    /**
     * Move cursor right
     * 
     * @return whether it worked and the cursor moved
     */
    public boolean right()
    {
        if (next == null) return false;
        CharNode start_next = next; // current next/prev, before moving. saving
        CharNode start_prev = prev; // these references is mostly for readability
        if (start_prev != null) start_prev.set_next(start_next); // everything from this line down is just the same code from left() but prev and next has been switched
        start_next.set_prev(start_prev);
        prev = start_next;
        next = start_next.get_next();
        if (start_next.get_next() != null) start_next.get_next().set_prev(this);
        start_next.set_next(this);
        return true;
    }
    
    /**
     * Remove Node to the left of the cursor
     */
    public boolean remove_char_toleft(){
        if (get_prev() == null) return false; // if at front of list, can't remove
        if (get_prev().get_prev() != null) get_prev().get_prev().set_next(this); // make node pointing at to-be-removed node point forward to cursor
        set_prev(get_prev().get_prev()); // point back to node before the one being removed
        return true;
    }
    
    /**
     * Insert a new node to the left of the cursor
     */
    
    public boolean insert(char char_value)
    {
        if (char_value < 32) return false; // don't insert empty chars
        prev = new CharNode(char_value, prev, this); // make prev point to a new char which points at prev and cursor
        if (prev.get_prev() != null) prev.get_prev().set_next(prev); // point back at new node
        return true;
    }
}
