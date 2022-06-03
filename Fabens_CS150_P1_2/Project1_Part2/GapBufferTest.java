

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * The test class GapBufferTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class GapBufferTest
{
    /**
     * Testing toString
     */
    @Test
    @DisplayName("toString")
    public void toStringTest ()
    {
        GapBuffer test_gap = new GapBuffer();
        String gap_text = test_gap.toString();
        assertTrue(gap_text.equals(""));
    }
    
    /**
     * Testing load_string
     */
    @Test
    @DisplayName("load_string")
    public void load_stringTest ()
    {
        GapBuffer test_gap = new GapBuffer();
        test_gap.load_string("testing");
        String gap_text = test_gap.toString();
        assertTrue(gap_text.equals("testing"));
    }
    
    /**
     * Testing length
     */
    @Test
    @DisplayName("length")
    public void lengthTest ()
    {
        GapBuffer test_gap = new GapBuffer();
        test_gap.load_string("a really long string that is longer than the initial buffer size");
        assertTrue(test_gap.length() == 64);
    }
    
    /**
     * Testing cursor movement
     */
    @Test
    @DisplayName("cursor movement")
    public void cursor_moveTest ()
    {
        GapBuffer test_gap = new GapBuffer();
        test_gap.load_string("testing");
        assertTrue(test_gap.cursorString().equals("testing---"));
        assertTrue(test_gap.cursor_left());                     // left
        assertTrue(test_gap.cursorString().equals("testin---g"));
        assertTrue(test_gap.cursor_left(2));                    // left 2
        assertTrue(test_gap.cursorString().equals("test---ing"));
        assertFalse(test_gap.cursor_right(6));                  // right 6
        assertTrue(test_gap.cursorString().equals("test---ing"));
        assertTrue(test_gap.cursor_move_start_line());          // move start line
        assertTrue(test_gap.cursorString().equals("---testing"));
        assertTrue(test_gap.cursor_move_end_line());            // move end line
        assertTrue(test_gap.cursorString().equals("testing---"));
    }
    
    /**
     * Testing remove_char_toleft
     */
    @Test
    @DisplayName("remove_char_toleft")
    public void remove_char_toleftTest ()
    {
        GapBuffer test_gap = new GapBuffer();
        test_gap.load_string("testing");
        assertTrue(test_gap.cursor_left());
        assertTrue(test_gap.remove_char_toleft());
        assertTrue(test_gap.cursorString().equals("testi----g"));
        assertTrue(test_gap.remove_char_toleft(2));
        assertTrue(test_gap.cursorString().equals("tes------g"));
    }
    
    /**
     * Testing insert_text(char)
     */
    @Test
    @DisplayName("insert_text_char")
    public void insert_text_charTest ()
    {
        GapBuffer test_gap = new GapBuffer();
        test_gap.load_string("testing");
        assertTrue(test_gap.cursor_left());
        assertTrue(test_gap.insert_text('o'));
        assertTrue(test_gap.cursorString().equals("testino--g"));
        assertTrue(test_gap.insert_text('r'));
        assertTrue(test_gap.cursorString().equals("testinor-g"));
        assertTrue(test_gap.insert_text('b'));
        assertTrue(test_gap.cursorString().equals("testinorb----------g"));
    }
    
    /**
     * Testing insert_text(String)
     */
    @Test
    @DisplayName("insert_text_str")
    public void insert_text_strTest ()
    {
        GapBuffer test_gap = new GapBuffer();
        test_gap.load_string("testing");
        assertTrue(test_gap.cursor_left());
        assertTrue(test_gap.insert_text("ooo"));
        assertTrue(test_gap.cursorString().equals("testinooo----------g"));
        assertTrue(test_gap.insert_text("rrr"));
        assertTrue(test_gap.cursorString().equals("testinooorrr-------g"));
        assertTrue(test_gap.insert_text("bbb"));
        assertTrue(test_gap.cursorString().equals("testinooorrrbbb----g"));
    }
}
