
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * The test class BufferStructureTest.
 *
 * @author  Peter Fabens
 * @version 2/16/2022
 */
public class BufferStructureTest
{
    String type = "linked";
    
    /**
     * Test for load_line_at_position method and offshoots
     */
    @Test
    @DisplayName("load_line")
    public void load_lineTest()
    {
        BufferStructure test_struct = new BufferStructure(type);
        test_struct.load_line_at_end("testing the buffer");
        assertTrue(test_struct.toString().equals("testing the buffer"));
        test_struct.load_line_at_start("buffering the test");
        assertTrue(test_struct.toString().equals("testing the buffer"));
        test_struct.cursor_up();
        test_struct.load_line_at_position("testering the buff", 0);
        assertTrue(test_struct.toString().equals("buffering the test"));
        test_struct.cursor_down();
        assertTrue(test_struct.toString().equals("testing the buffer"));
    }
    
    /**
     * Test for line_count()
     */
    @Test
    @DisplayName("line_count")
    public void line_countTest()
    {
        BufferStructure test_struct = new BufferStructure(type);
        test_struct.load_line_at_end("testing the buffer");
        assertEquals(1, test_struct.line_count());
        test_struct.load_line_at_start("buffering the test");
        assertEquals(2, test_struct.line_count());
        test_struct.load_line_at_position("testering the buff", 0);
        assertEquals(3, test_struct.line_count());
    }
    
    /**
     * Test for curr_line_length()
     */
    @Test
    @DisplayName("curr_line_length")
    public void curr_line_lengthTest()
    {
        BufferStructure test_struct = new BufferStructure(type);
        assertEquals(-1, test_struct.curr_line_length());       // returns -1 since there is no current line
        test_struct.load_line_at_end("Buffy the buff buffle");
        test_struct.load_line_at_end("testing the buffer");
        assertTrue(test_struct.cursor_down());
        assertEquals(18, test_struct.curr_line_length());
        assertFalse(test_struct.cursor_down());
        test_struct.load_line_at_start("buffering the");
        assertTrue(test_struct.cursor_up(2));
        assertEquals(13, test_struct.curr_line_length());
        test_struct.load_line_at_position("testering", 0);
        assertTrue(test_struct.cursor_up());
        assertEquals(9, test_struct.curr_line_length());
    }
    
    /**
     * Test for cursor_position_in_line()
     */
    @Test
    @DisplayName("cursor_position_in_line")
    public void cursor_position_in_lineTest()
    {
        BufferStructure test_struct = new BufferStructure(type);
        assertEquals(0, test_struct.cursor_position_in_line());
        test_struct.load_line_at_end("testing the buffer");
        test_struct.load_line_at_end("buff buffer buffs briefly");
        test_struct.load_line_at_end("sunshine and rainbows");
        assert(test_struct.cursor_down());
        assert(test_struct.cursor_right());
        assertEquals(19, test_struct.cursor_position_in_line());
        assert(test_struct.cursor_left(10));
        assertEquals(9, test_struct.cursor_position_in_line());
        assertFalse(test_struct.cursor_left(12));
        assertEquals(9, test_struct.cursor_position_in_line());
    }
    
    /**
     * Test for cursor_movement()
     */
    @Test
    @DisplayName("cursor_movement")
    public void cursor_movementTest()
    {
        BufferStructure test_struct = new BufferStructure(type);
        test_struct.load_line_at_start("testing the buffer");
        test_struct.load_line_at_end("a whole bunch of nothing");
        test_struct.load_line_at_end("this buffer sure has gaps");
        assert(test_struct.cursor_left());
        assertEquals(0, test_struct.cursor_line_position());    // left
        assertEquals(17, test_struct.cursor_position_in_line());
        assert(test_struct.cursor_left(5));
        assert(test_struct.cursor_line_position() == 0);    // left 5
        assert(test_struct.cursor_position_in_line() == 12);
        assert(test_struct.cursor_right());
        assert(test_struct.cursor_line_position() == 0);    // right
        assert(test_struct.cursor_position_in_line() == 13);
        assert(test_struct.cursor_right(2));
        assert(test_struct.cursor_line_position() == 0);    // right 2
        assert(test_struct.cursor_position_in_line() == 15);
        assert(test_struct.cursor_move_start_line());
        assert(test_struct.cursor_line_position() == 0);    // start of line
        assert(test_struct.cursor_position_in_line() == 0);
        assert(test_struct.cursor_move_end_line());
        assert(test_struct.cursor_line_position() == 0);    // end of line
        assert(test_struct.cursor_position_in_line() == 18);
        assert(test_struct.cursor_down());
        assert(test_struct.cursor_line_position() == 1);    // down
        assertEquals(18, test_struct.cursor_position_in_line());
        assert(test_struct.cursor_up());
        assert(test_struct.cursor_line_position() == 0);    // up
        assert(test_struct.cursor_position_in_line() == 18);
        assert(test_struct.cursor_down(2));
        assert(test_struct.cursor_line_position() == 2);    // down 2
        assertEquals(18, test_struct.cursor_position_in_line());
        assert(test_struct.cursor_right(3));
        assert(test_struct.cursor_line_position() == 2);    //right 3
        assert(test_struct.cursor_position_in_line() == 21);
        assert(test_struct.cursor_up(2));
        assert(test_struct.cursor_line_position() == 0);    // up 2 (has to also move back)
        assert(test_struct.cursor_position_in_line() == 18);
        assert(test_struct.cursor_move_last_line());
        assert(test_struct.cursor_line_position() == 2);    // last line
        assert(test_struct.cursor_position_in_line() == 18);
        assert(test_struct.cursor_move_first_line());
        assert(test_struct.cursor_line_position() == 0);    // first line
        assert(test_struct.cursor_position_in_line() == 18);
    }
    
    /**
     * Test for remove()
     */
    @Test
    @DisplayName("remove")
    public void removeTest()
    {
        BufferStructure test_struct = new BufferStructure(type);
        test_struct.load_line_at_start("testing the buffer");
        test_struct.load_line_at_start("a whole bunch of nothing");
        test_struct.load_line_at_start("this buffer sure has gaps");
        assert(test_struct.remove_line());
        assert(test_struct.line_count() == 2);
        assert(test_struct.remove_line());
        assert(test_struct.line_count() == 1);
        assert(test_struct.remove_line());
        assert(test_struct.line_count() == 0);
        assertFalse(test_struct.remove_line()); // doesn't do anything, but also doesn't break anything!
    }
    
    /**
     * Test for remove_char_toleft()
     */
    @Test
    @DisplayName("remove_char_toleft")
    public void remove_char_toleftTest()
    {
        BufferStructure test_struct = new BufferStructure(type);
        test_struct.load_line_at_end("testing the buffer");
        assert(test_struct.remove_char_toleft());
        assert(test_struct.curr_line_length() == 17);
        assert(test_struct.toString().equals("testing the buffe"));
        assert(test_struct.cursor_left(2));
        assert(test_struct.remove_char_toleft());
        assert(test_struct.curr_line_length() == 16);
        assert(test_struct.toString().equals("testing the bufe"));
        assert(test_struct.cursor_move_start_line());
        assertFalse(test_struct.remove_char_toleft());
        assert(test_struct.curr_line_length() == 16);
        assert(test_struct.toString().equals("testing the bufe"));
        assert(test_struct.cursor_move_end_line());
        assert(test_struct.remove_char_toleft(6));
        assert(test_struct.curr_line_length() == 10);
        assert(test_struct.toString().equals("testing th"));
        assert(test_struct.cursor_left());
        assert(test_struct.remove_char_toleft(3));
        assert(test_struct.curr_line_length() == 7);
        assert(test_struct.toString().equals("testinh"));
        assert(test_struct.cursor_move_start_line());
        assertFalse(test_struct.remove_char_toleft(8));
    }
    
    /**
     * Testing insert_text(char)
     */
    @Test
    @DisplayName("insert_text_char")
    public void insert_text_charTest ()
    {
        BufferStructure test_struct = new BufferStructure(type);
        test_struct.load_line_at_end("testing");
        assertTrue(test_struct.cursor_left());
        assertTrue(test_struct.insert_text('o'));
        assertTrue(test_struct.cursorString().equals((type.equals("gap"))? "testino----g" : "testino|g"));
        assertTrue(test_struct.insert_text('r'));
        assertTrue(test_struct.cursorString().equals((type.equals("gap"))? "testinor----g" : "testinor|g"));
        assertTrue(test_struct.insert_text('b'));
        assertTrue(test_struct.cursorString().equals((type.equals("gap"))? "testinorb----g" : "testinorb|g"));
    }
    
    /**
     * Testing insert_text(String)
     */
    @Test
    @DisplayName("insert_text_str")
    public void insert_text_strTest ()
    {
        BufferStructure test_struct = new BufferStructure(type);
        test_struct.load_line_at_end("testing");
        assertTrue(test_struct.cursor_left());
        assertTrue(test_struct.insert_text("ooo"));
        assertTrue(test_struct.cursorString().equals((type.equals("gap"))? "testinooo----------g" : "testinooo|g"));
        assertTrue(test_struct.insert_text("rrr"));
        assertTrue(test_struct.cursorString().equals((type.equals("gap"))? "testinooorrr-------g" : "testinooorrr|g"));
        assertTrue(test_struct.insert_text("bbb"));
        assertTrue(test_struct.cursorString().equals((type.equals("gap"))? "testinooorrrbbb----g" : "testinooorrrbbb|g"));
    }
}
