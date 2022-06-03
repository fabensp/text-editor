/**
 * Structure holding a list of Es, now in a new and improved Linked List format! 
 *
 * @author Peter Fabens
 * @version 2/20/2022
 */
public class BufferStructure implements BufferStructureInterface
{
    StructNode head; // top node in buffer structure
    int cursor; // index of cursor
    String type = ""; // type of text buffer to use
    /**
     * Default constructor. Just makes sure values are initialized at starting vals.
     */
    public BufferStructure(String type)
    {
        head = null;
        cursor = 0;
        this.type = type;
    }
    
    /**
     * Returns the length of the specified line.
     * 
     * @param line line to analysze the length of
     */
    public int length(int line)
    {
        if (line >= line_count() || line < 0) throw new IndexOutOfBoundsException();
        return get_node(line).item().length();
    }
    
    /**
     * Returns the specified line in the structure.
     * 
     * @param line line to return the contents of
     */
    public String toString(int line) throws IndexOutOfBoundsException
    {
        if (line >= line_count() || line < 0) throw new IndexOutOfBoundsException("Index " + line + " is OOB!");
        return get_node(line).item().toString();
    }
    
    /**
     * Returns the line the cursor is currently on.
     */
    public String toString()
    {
        return toString(cursor);
    }
    
    /**
     * Returns the line with the cursor position indicated. For gap buffer, this means the array with empty spaces as '-'. 
     * For linked list buffer, cursor is indicated by a specified character.
     */
    public String cursorString()
    {
        return get_node(cursor).item().cursorString();
    }
    
    /**
     * Prints out the entire structure, with lines separated with newlines.
     */
    public String toStringStruct()
    {
        String out = "";
        StructNode ref_node = head;
        while (ref_node != null)
        {
            out += ref_node.item().toString() + "\n";
            ref_node = ref_node.next();
        }
        return out.trim();
    }
    
    /**
     * Creates a new line at the top of the structure and fills it with the desired data.
     * 
     * Assumption: Cursor should move with the line it was previously on.
     * 
     * @param str_value String to pass and store to buffer 
     */
    public void load_line_at_start(String str_value)
    {
        StructNode new_node = new StructNode(get_new_type_obj());
        new_node.s_next(head);
        head = new_node;
        new_node.item().load_string(str_value);
        if (cursor < line_count() - 1)
            cursor_down();
    }
    
    /**
     * Creates a new line at the bottom of the structure and loads the desired data to it.
     * 
     * Assumption: Cursor should stay put.
     * 
     * @param str_value String to pass and store to buffer
     */
    public void load_line_at_end (String str_value)
    {
        if (head == null)
        {
            cursor_up();
            load_line_at_start(str_value);
        } else {
            StructNode new_node = new StructNode(get_new_type_obj());
            get_node(line_count() - 1).s_next(new_node);
            new_node.item().load_string(str_value);
        }
    }
    
    /**
     * Loads specified line at specified position.
     * 
     * Assumption: Cursor should shift down if lines are loaded above or at it's position.
     * 
     * @param str_value String to load in to buffer
     * @param position  index to add the line in at
     */
    public void load_line_at_position (String str_value, int position) throws IndexOutOfBoundsException
    {
        if (head == null) {
            head = new StructNode(get_new_type_obj());
            return;
        }
        if (position > line_count() || position < 0) throw new IndexOutOfBoundsException("Index " + position + " is OOB!");
        StructNode new_node = new StructNode(get_new_type_obj());
        new_node.s_next(get_node(position));
        if (position == 0) {
            head = new_node;
        } else {
            get_node(position - 1).s_next(new_node);
        }
        new_node.item().load_string(str_value);
        if (position <= cursor && cursor < line_count() - 1) cursor_down();
    }
    
    /**
     * Inserts a line with no characters directly above the current line.
     * 
     * Assumption: Cursor should shift down with current line.
     */
    public boolean insert_empty_line_above()
    {
        try{
            load_line_at_position("", cursor); // try to insert the line
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false; // if it fails, it failed
        }
        return true;
    }
    
    /**
     * Inserts a line with no characters directly below the current line.
     */
    public boolean insert_empty_line_below()
    {
        try{
            load_line_at_position("", cursor + 1);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * Returns the number of lines in the structure.
     */
    public int line_count()
    {
        StructNode ref_node = head;
        int num = 0;
        while (ref_node != null)
        {
            num++;
            ref_node = ref_node.next();
        }
        return num;
    }
    
    /**
     * Returns the length of the line the cursor is on.
     */
    public int curr_line_length()
    {
        if (line_count() == 0) return -1;
        return length(cursor);
    }
    
    /**
     * Returns the index of the line the cursor is on.
     */
    public int cursor_line_position()
    {
        return cursor;
    }
    
    /**
     * Returns the index of the cursor in the current line.
     */
    public int cursor_position_in_line()
    {
        if (line_count() == 0) return 0;
        return get_node(cursor).item().cursor_position();
    }
    
    /**
     * Move cursor left 1
     * 
     * @returns whether the operation worked
     */
    public boolean cursor_left()
    {
        return get_node(cursor).item().cursor_left();
    }
    
    /**
     * Move cursor left
     * 
     * @param char_count dist to move
     * 
     * @returns whether cursor moved at all
     */
    public boolean cursor_left(int char_count)
    {
        return get_node(cursor).item().cursor_left(char_count);
    }
    
    /**
     * Move cursor right 1
     * 
     * @returns whether the operation worked
     */
    public boolean cursor_right()
    {
        return get_node(cursor).item().cursor_right();
    }
    
    /**
     * Move cursor right
     * 
     * @param char_count dist to move
     * 
     * @returns whether cursor moved at all
     */
    public boolean cursor_right(int char_count)
    {
        return get_node(cursor).item().cursor_right(char_count);
    }
    
    /**
     * Move cursor up
     * 
     * @returns whether the operation worked
     */
    public boolean cursor_up()
    {
        if (cursor == 0) return false;
        int curr_c = cursor_position_in_line();
        get_node(cursor).item().cursor_move_start_line();
        cursor--;
        get_node(cursor).item().cursor_right(curr_c);
        return true;
    }
    
    /**
     * Move cursor up
     * 
     * @param line_count dist to move
     * 
     * @returns whether cursor moved at all
     */
    public boolean cursor_up(int line_count)
    {
        if (line_count < 1) return false;
        int target = Math.max(cursor - line_count, 0);
        boolean work = false;
        while (cursor > target)
        {
            work = cursor_up() || work;
        }
        return work;
    }
    
    /**
     * Move cursor down
     * 
     * @returns whether the operation worked
     */
    public boolean cursor_down()
    {
        if (cursor == line_count() - 1 || line_count() == 0) return false;
        int curr_c = cursor_position_in_line();
        get_node(cursor).item().cursor_move_end_line();
        cursor++;
        get_node(cursor).item().cursor_move_end_line();
        get_node(cursor).item().cursor_left(get_node(cursor).item().length() - curr_c);
        return true;
    }
    
    /**
     * Move cursor down
     * 
     * @param line_count dist to move
     * 
     * @returns whether cursor moved at all
     */
    public boolean cursor_down(int line_count)
    {
        if (line_count < 1) return false;
        int target = Math.min(cursor + line_count, line_count() - 1);
        boolean work = false;
        while (cursor < target)
        {
            work = cursor_down() || work;
        }
        return work;
    }
    
    /**
     * Move cursor all the way up
     * 
     * @returns whether cursor moved at all
     */
    public boolean cursor_move_first_line()
    {
        while (cursor_up());
        
        if (cursor == 0) {
            return true;
        }
        return false;
    }
    
    /**
     * Move cursor all the way down
     * 
     * @returns whether cursor moved at all
     */
    public boolean cursor_move_last_line()
    {
        while (cursor_down());
        
        if (cursor == line_count() - 1) {
            return true;
        }
        return false;
    }
    
    /**
     * Move cursor all the way left
     * 
     * @returns whether cursor moved at all
     */
    public boolean cursor_move_start_line()
    {
        return get_node(cursor).item().cursor_move_start_line();
    }
    
    /**
     * Move cursor all the way right
     * 
     * @returns whether cursor moved at all
     */
    public boolean cursor_move_end_line()
    {
        return get_node(cursor).item().cursor_move_end_line();
    }
    
    /**
     * Remove current line
     * 
     * @returns whether the operation worked
     */
    public boolean remove_line()
    {
        int lines = line_count(); // starting lines
        if (lines == 0) return false; // return false if it can't do anything (struct already empty)
        else if (cursor == 0)
        {
            cursor_down(); // we want the functionality of the cursor moving down (moving the individual line cursors to the end of their lines, etc
            cursor = Math.max(0, cursor - 1); // but the cursor int itself shouldn't really change, since it's still on the same line index-wise (but don't go below 0)
            head = head.next();
        } else {
            cursor_up();
            get_node(cursor).s_next(get_node(cursor).next().next()); // normal case, just go up an element and then dereference the line the cursor was on
        }
        if (lines != line_count()) return true; // if anything was removed, it worked
        return false;
    }
    
    /**
     * Remove 1 char left of cursor
     * 
     * @returns whether char was removed
     */
    public boolean remove_char_toleft()
    {
        return remove_char_toleft(1);
    }
    
    /**
     * Remove chars left of cursor
     * 
     * @param char_count chars to delete
     * 
     * @returns whether char was removed
     */
    public boolean remove_char_toleft(int char_count)
    {
        if (head == null) return false;
        return get_node(cursor).item().remove_char_toleft(char_count);
    }
    
    /**
     * Insert text at cursor
     * 
     * Assumption: If there are no lines, create one
     * 
     * @param str_value string to insert
     * 
     * @returns whether insert worked
     */
    public boolean insert_text(String str_value)
    {
        if (head == null) head = new StructNode(get_new_type_obj()); // if there are no lines, create one
        return get_node(cursor).item().insert_text(str_value);
    }
    
    /**
     * Insert char at cursor
     * 
     * @param char_value char to insert
     * 
     * @returns whether insert worked
     */
    public boolean insert_text(char char_value)
    {
        return insert_text("" + char_value);
    }
    
    /**
     * Returns the node at the specified index
     * 
     * @param index index of node to get
     */
    private StructNode get_node(int index) throws IndexOutOfBoundsException
    {
        if (index > line_count() || index < 0) throw new IndexOutOfBoundsException();
        StructNode ref_node = head;
        int count = 0;
        while (count < index) // go down node chain until index is reached
        {
            ref_node = ref_node.next();
            count++;
        }
        return ref_node;
    }
    
    /**
     * Returns a new object of the type specified by the 'type' string.
     */
    private GapBufferInterface get_new_type_obj ()
    {
        if (type.equals("gap"))
        {
            return new GapBuffer();
        } else // defaults to linkedlist (don't tell anyone, but i like linkedlist more so i'm making it the default.)
        {
            return new LinkedListBuffer();
        }
    }
}