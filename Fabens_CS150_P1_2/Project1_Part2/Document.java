import java.io.*;
/**
 * Document keeps track of IO and all of the operations done by the buffer structure. The heart of the editor.
 *
 * @author Peter Fabens
 * @version 2/20/2022
 */
public class Document implements DocumentInterface
{
    private BufferStructure struct;
    private DocumentIO doc_io;
    private String type = "";
    /**
     * Initializes the BufferStructure and DocumentIO objects.
     */
    public Document (String type)
    {
        struct = new BufferStructure(type);
        doc_io = new DocumentIO();
        this.type = type;
    }
    
    /**
     * Overwrites current contents of the BufferStructure with the contents of a file.
     * 
     * @param filename name of file to read from
     */
    public boolean load_file(String filename) throws IOException
    {
        struct = new BufferStructure(type); // wipe structure to prepare it for being refilled with file data
        doc_io.open_file(filename); // open file to make sure it's the one being loaded, in case there was a different one previously
        while(doc_io.has_more_lines()) // if there's more data to read,
        {
            struct.load_line_at_end(doc_io.read_line()); // put that data in the bufferstructure
        }
        return true; // return true if it works, i.e. if no exceptions get thrown
    }
    
    /**
     * Writes current contents of the BufferStructure to a specified file.
     * 
     * @param filename name of file to write to.
     */
    public boolean store_file(String filename) throws IOException
    {
        doc_io.open_file(filename); // set the document io file reference to the specified file
        doc_io.write_file(struct.toStringStruct()); // write contents of bufferstructure to file
        return true;
    }
    
    /**
     * Writes current contents of the BufferStructure to whatever file is already open
     * 
     * @throws NullPointerException if attempting to store to a file that has not been identified, either by loading or storing
     */
    public boolean store_file() throws IOException, NullPointerException
    {
        doc_io.write_file(struct.toStringStruct()); // write contents of bufferstructure to whatever the previously specified file was
        return true;
    }
    
    /** Returns current file name*/
    public String current_file_name () {            return doc_io.file_name();}
    
    /** Returns whether a given file exists
     * @param file file to check for*/
    public boolean file_exists(String file) {       return doc_io.file_exists(file); }
    
    /** Returns complete contents of document, with lines separated by \n*/
    public String toStringDocument () {             return struct.toStringStruct();}
    
    /** Returns string representation of a specified line 
     * @param line_index index of line to return*/
    public String toStringLine(int line_index) {    return struct.toString(line_index);}
    
    /** Returns string representation of current line
     */
    public String toStringLine() {  return struct.toString();}
    
    /** Returns length of a specified line 
     * @param line_index index of line to return the length of*/
    public int line_length(int line_index) {        return struct.length(line_index);}
    /** Returns length of whatever line the cursor is currently on*/
    public int line_length(){                       return struct.curr_line_length();}
    
    /** Returns number of lines in the document*/
    public int line_count(){                        return struct.line_count();}
    
    /**
     * Returns index of the cursor's position within the line
     */
    public int cursor_position_in_line()
    {
        return struct.cursor_position_in_line();
    }
    
    /**
     * Returns the index of the line the cursor is on currently.
     */
    public int cursor_line_position()
    {
        return struct.cursor_line_position();
    }
    
    /** Moves cursor one char left*/
    public boolean cursor_left(){                   return struct.cursor_left();}
    
    /** Moves cursor to the left 
     * @param char_count number of characters to move*/
    public boolean cursor_left(int char_count){     return struct.cursor_left(char_count);}
    
    /** Moves cursor one char to the right*/
    public boolean cursor_right(){                  return struct.cursor_right();}
    
    /** Moves cursor to the right 
     * @param char_count number of characters to move*/
    public boolean cursor_right(int char_count){    return struct.cursor_right(char_count);}
    
    /** Moves cursor up one line*/
    public boolean cursor_up(){                     return struct.cursor_up();}
    
    /** Moves cursor up 
     * @param line_count number of lines to move*/
    public boolean cursor_up(int line_count){       return struct.cursor_up(line_count);}
    
    /** Moves cursor down one line*/
    public boolean cursor_down(){                   return struct.cursor_down();}
    
    /** Moves cursor down 
     * @param line_count number of lines to move*/
    public boolean cursor_down(int line_count){     return struct.cursor_down(line_count);}
    
    /** Moves cursor to end of current line*/
    public boolean cursor_move_end_line(){          return struct.cursor_move_end_line();}
    
    /** Moves cursor to start of current line*/
    public boolean cursor_move_start_line(){          return struct.cursor_move_start_line();}
    
    /** Moves cursor to last line*/
    public boolean cursor_move_last_line(){          return struct.cursor_move_last_line();}
    
    /** Moves cursor to first line*/
    public boolean cursor_move_first_line(){          return struct.cursor_move_first_line();}
    
    /** Inserts an empty line directly above the cursor*/
    public boolean insert_line_above(){             return struct.insert_empty_line_above();}
    
    /** Inserts an empty line directly below the cursor*/
    public boolean insert_line_below(){             return struct.insert_empty_line_below();}
    
    /** Removes the line that the cursor is on*/
    public boolean remove_line(){                   return struct.remove_line();}
    
    /** Removes char directly above cursor in char array, and moves cursor up to that empty space*/
    public boolean remove_char_toleft(){            return struct.remove_char_toleft();}
    
    /** Removes chars directly above cursor in char array, and moves cursor up to the first empty space 
     * @param char_count chars to remove*/
    public boolean remove_char_toleft(int char_count){  return struct.remove_char_toleft(char_count);}
    
    /** Insert a string at the current cursor position 
     * @param str_value string to insert*/
    public boolean insert_text(String str_value){   return struct.insert_text(str_value);}
    
    /** Insert a char at the current cursor positon 
     * @param char_value char to insert*/
    public boolean insert_text(char char_value){    return struct.insert_text(char_value);}
}
