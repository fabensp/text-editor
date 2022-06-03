
/**
 * Holds a single line of text within a document
 *
 * @author Peter Fabens
 * @version 2/20/2022
 */
public class GapBuffer implements GapBufferInterface
{
    private char[]  line;
    private int     cursor_index;
    private int     growth;
    /**
     * Initializes the gap buffer with a set number of spaces, as well as other variables.
     */
    public GapBuffer ()
    {
        growth          = 10;
        line            = new char[growth];
        cursor_index    = 0;
    }
    
    /**
     * Loads a string in to the array, replacing the previous string.
     * 
     * @param str_value The string to load in to the array.
     */
    public void load_string (String str_value)
    {
        int count;
        char[] temp_array = new char[growth * (str_value.length() / growth + 1)];
        for (count = 0; count < str_value.length(); ++count)
        {
            temp_array[count] = str_value.charAt(count);
        }
        line = temp_array;
        cursor_index = str_value.length();
    }
    
    /**
     * Return a string of the text the gap buffer holds
     */
    public String toString()
    {
        String output = "";
        for (char c : line)
        {
            if (c > 0) output += c; // Conditional prevents the printing of empty chars. That was annoying to debug.
        }
        return output;
    }
    
    /**
     * Return a string of the string the gap buffer holds, formatted as an array
     */
    public String cursorString()
    {
        String output = "";
        for (char c : line)
        {
            if (c > 0) output += c + ""; // Conditional prevents the printing of empty chars.
            else output += "-";
        }
        return output;
    }
    
    /**
     * Return the current index of the cursor position
     */
    public int cursor_position(){
        return cursor_index;
    }
    
    /**
     * Return the length of the stored string
     */
    public int length()
    {
        return toString().length();
    }
    
    /** Move cursor left by 1*/
    public boolean cursor_left() {                  return move_cursor(-1);}
    
    /** Move cursor left by a number @param char_count number of spaces to move*/
    public boolean cursor_left(int char_count) {    return move_cursor(-1 * Math.abs(char_count));}
    
    /** Move cursor right by 1*/
    public boolean cursor_right() {                 return move_cursor(1);}
    
    /** Move cursor right by a number @param char_count number of spaces to move*/
    public boolean cursor_right(int char_count) {   return move_cursor(Math.abs(char_count));}
    
    /** Move cursor to start of line*/
    public boolean cursor_move_start_line() {       return move_cursor(-cursor_index);}
    
    /** Move cursor to end of line*/
    public boolean cursor_move_end_line() {         return move_cursor(length() - cursor_index);}
    
    /**
     * Removes char in array element directly to the left of the cursor
     */
    public boolean remove_char_toleft()
    {
        if (cursor_index == 0) return false;
        line[cursor_index - 1] = 0;
        return cursor_left();
    }
    
    /**
     * Removes a number of chars in the array elements directly to the left of the cursor
     * 
     * @param char_count Number of chars to remove.
     */
    public boolean remove_char_toleft(int char_count)
    {
        if (cursor_index < char_count) return false;
        int count;
        for(count = cursor_index - 1; count >= cursor_index - char_count; --count)
        {
            line[count] = 0;
        }
        return cursor_left(char_count);
    }
    
    /**
     * Inserts a string into the array
     * 
     * @param str_value String to insert
     */
    public boolean insert_text(String str_value)
    {
        int count;
        for (count = 0; count < str_value.length(); ++count)
        {
            insert_text(str_value.charAt(count)); // go through the string adding each char individually
        }
        return true;
    }
    
    /**
     * Inserts a single char into the array
     * 
     * @param char_value Char to insert
     */
    public boolean insert_text(char char_value)
    {
        if (char_value == 0) return false; // if one were allowed to insert the empty char in to the string it would just screw with the cursor position
        if (line.length - length() <= 1) grow(growth); // if there isn't enough space in the buffer, grow
        line[cursor_index] = char_value;
        cursor_right();
        return true;
    }
    
    /* 
     * PRIVATE LAND: Trespassers will be shocked!
     * Below are all the private methods used within the public methods to avoid duplicate code.
     */
    /**
     * A more user-friendly version of set_cursor() that avoids throwing exceptions if you try to move out of bounds.
     * 
     * @param move Distance to move
     */
    private boolean move_cursor(int move){
        if ((move < 0 && cursor_index >= Math.abs(move)) || (move > 0 && cursor_index <= length() - Math.abs(move)))
        {
            set_cursor(cursor_index + move); // set cursor to new location, or as close as possible to i
            return true;
        }
        return false;
    }
    
    /**
     * Set cursor index
     * 
     * @param index Index to place the cursor.
     */
    private void set_cursor(int index)
    {
        int count;
        int limit = Math.abs(index - cursor_index);
        int buffer = line.length - length();
        while (cursor_index != index) // if target hasn't been reached yet,
        {
            if (index > cursor_index) // if target is to the right
            {
                if (line[cursor_index + buffer] != 0) // only move chars if the space you're moving through isn't empty
                {
                    line[cursor_index] = line[cursor_index + buffer]; // shift chars
                    line[cursor_index + buffer] = 0; // set char at end of tail to empty
                }
                cursor_index += 1; // move cursor
            } else // if target is to the left (or if current == target, but while condition covers that)
            {
                if (line[cursor_index - 1] != 0) 
                {
                    line[cursor_index + buffer - 1] = line[cursor_index - 1];
                    line[cursor_index - 1] = 0;
                }
                cursor_index -= 1;
            }
        }
    }
    
    /**
     * Grows the array by a certain size.
     * 
     * @param e Number of elements to add to the array.
     */
    private void grow(int e)
    {
        char[] temp_array = new char[line.length + e]; // new temp array of desired size
        int count;
        int cursor_temp = cursor_index;
        for (count = 0; count < Math.min(length(), line.length + e); ++count)
        {
            temp_array[count] = toString().charAt(count); // copy over elements
        }
        line = temp_array; // set to real array
        cursor_index = length();
        set_cursor(cursor_temp);
    }
}
