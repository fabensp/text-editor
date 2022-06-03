
/**
 * Saves a line of text in a Linked List
 *
 * @author Peter Fabens
 * @version 2/23/2022
 */
public class LinkedListBuffer implements GapBufferInterface
{
    CharNode head;
    CharNode tail;
    CursorNode cursor;
    
    /**
     * Default constructor. Starts with null/zero values.
     */
    public LinkedListBuffer()
    {
        cursor = new CursorNode('|'); // cursor holds empty char, uses polymorphism to be in the linked list
        head = cursor;
        tail = cursor;
    }
    
    /**
     * Overloaded constructor.
     * 
     * @param text text to load in to the buffer
     */
    public LinkedListBuffer(String text)
    {
        this();
        load_string(text);
    }
    
    /**
     * Loads a new string in to the list, replacing the old contents of the list
     * 
     * @param str_value String to enter in to the array
     */
    public void load_string(String str_value)
    {
        if (str_value.length() == 0) {
            cursor.set_next(null);
            cursor.set_prev(null);
            head = cursor;
            tail = cursor;
            return; // skip the rest of the method
        }
        int count;
        head = new CharNode(str_value.charAt(0)); // overwrite whatever the current head is, cutting off whatever data was stored
        tail = head;
        CharNode ref_node = head; // head doesn't really need to be a new object each time, but it means not having to worry about if head == null
        for (count = 1; count < str_value.length(); ++count)
        {
            ref_node.set_next(new CharNode(str_value.charAt(count))); // create the next node with it's letter
            ref_node.get_next().set_prev(ref_node);
            ref_node = ref_node.get_next(); // go to that node
            tail = ref_node; // set tail to new element
        }
        tail.set_next(cursor);  // put cursor after end of list
        cursor.set_prev(tail);  // put end of list before cursor
        tail = cursor;          // cursor IS the end of list
    }
    
    /**
     * Returns the length of the string in the buffer
     */
    public int length()
    {
        int num = 0;
        CharNode ref_node = head;
        while (ref_node != null) // while we are still on a real true list element
        {
            if (ref_node != cursor) num++; // increment, but don't count the cursor node
            ref_node = ref_node.get_next(); // go to the next element and repeat
        }
        return num;
    }
    
    /**
     * Returns index of cursor in string
     */
    public int cursor_position()
    {
        int num = 0;
        CharNode ref_node = head;
        if (head == null) return 0;
        while (ref_node != cursor)
        {
            num++;
            ref_node = ref_node.get_next();
        }
        return num;
    }
    
    /**
     * Returns the string as a single cohesive String
     */
    @Override
    public String toString()
    {
        String out = "";
        CharNode ref_node = head;
        while (ref_node != null)
        {
            if (ref_node != cursor) out += ref_node.get_char() + "";
            ref_node = ref_node.get_next(); // essentially the same code as length() besides String datatype
        }
        return out;
    }
    
    /**
     * Moves the cursor one char to the left, and checks to see if the head/tail need to be changed.
     */
    public boolean cursor_left(){
        if (cursor_position() == 0) return false;
        if (cursor.left())
        {
            if (cursor == tail) tail = cursor.get_next();
            if (cursor.get_prev() == null) head = cursor;
            return true;
        } else
            return false;
    }
    
    /**
     * Calls cursor_left() multiple times, moving the cursor multiple spaces.
     * 
     * @param char_count number of chars to move
     */
    public boolean cursor_left(int char_count){
        if (cursor_position() - char_count < 0) return false;
        int count = 0;
        boolean work = false; // tracks whether the cursor moved
        while (count < char_count){
            work = cursor_left() || work;
            count++;
        }
        return work;
    }
    
    /**
     * Moves the cursor one char to the right, and checks to see if the head/tail need to be changed.
     */
    public boolean cursor_right(){
        if (cursor_position() == length()) return false;
        if (cursor.right())
        {
            if (cursor == head) head = cursor.get_prev();
            if (cursor.get_next() == null) tail = cursor;
            return true;
        } else
            return false;
    }
    
    /**
     * Calls cursor_right() multiple times, moving the cursor multiple spaces.
     * 
     * @param char_count number of chars to move
     */
    public boolean cursor_right(int char_count){
        if (cursor_position() + char_count > length()) return false;
        int count = 0;
        boolean work = false; // tracks whether the cursor moved
        while (count < char_count){
            work = cursor_right() || work;
            count++;
        }
        return work;
    }
    
    /**
     * Moves the cursor all the way to the start of the line.
     */
    public boolean cursor_move_start_line()
    {
        while (cursor_left());
        return cursor.get_prev() == null;
    }
    
    /**
     * Moves the cursor all the way to the end of the line.
     */
    public boolean cursor_move_end_line()
    {
        while (cursor_right());
        return cursor.get_next() == null;
    }
    
    /**
     * Removes char directly left of cursor, and checks to see if the cursor is now the head.
     */
    public boolean remove_char_toleft(){
        if (cursor.remove_char_toleft())
        {
            if (cursor.get_prev() == null) head = cursor;
            return true;
        } else
            return false;
    }
    
    /**
     * Calls remove_char_toleft() multiple times, removing multiple chars.
     * 
     * @param char_count number of chars to remove
     */
    public boolean remove_char_toleft(int char_count){
        int count = 0;
        boolean work = false; // tracks whether the cursor has removed anything
        while (count < char_count){
            work = remove_char_toleft() || work;
            count++;
        }
        return work;
    }
    
    /**
     * Insert a character behind the cursor
     * 
     * @param char_value char to insert
     */
    public boolean insert_text (char char_value)
    {
        if (cursor.insert(char_value)) {
            if (cursor.get_prev() != null && head == cursor) head = cursor.get_prev(); // check if head needs to move
            return true;
        }
        return false;
    }
    
    /**
     * Insert a string behind the cursor
     * 
     * @param str_value string to insert
     */
    public boolean insert_text (String str_value)
    {
        if (str_value.length() == 0) return false; // no empty strings allowed!
        int count = 0;
        boolean work = false; // tracks if any inserts have worked
        while (count < str_value.length())  work = insert_text(str_value.charAt(count++)) || work;
        return true;
    }
    
    /**
     * toString(), but with the cursor included for testing. Character shown for cursor is
     * specified in CursorNode constructor
     */
    public String cursorString()
    {
        String out = "";
        CharNode ref_node = head;
        while (ref_node != null)
        {
            out += ref_node.get_char() + "";
            ref_node = ref_node.get_next();
        }
        return out;
    }
}
